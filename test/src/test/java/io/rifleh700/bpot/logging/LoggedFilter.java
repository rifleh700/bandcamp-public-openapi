package io.rifleh700.bpot.logging;

import jakarta.ws.rs.ConstrainedTo;
import jakarta.ws.rs.RuntimeType;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.*;
import org.apache.commons.io.input.TeeInputStream;
import org.apache.commons.io.output.TeeOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

import static io.rifleh700.bpot.logging.LoggedField.*;
import static java.lang.String.valueOf;
import static java.lang.System.nanoTime;
import static java.util.Objects.requireNonNullElse;
import static java.util.Optional.of;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.StringUtils.*;


@Provider
@ConstrainedTo(RuntimeType.CLIENT)
public class LoggedFilter implements ClientRequestFilter, ClientResponseFilter, ReaderInterceptor, WriterInterceptor {

    protected static final Logger log = LoggerFactory.getLogger(LoggedFilter.class);

    /**
     * Name of the property stored in container context to compute the duration time.
     */
    protected static final String REQUEST_TIME_PROPERTY = "request-time";

    /**
     * Name of the property stored in container context to retrieve the request body after its processing.
     */
    protected static final String REQUEST_BODY_PROPERTY = "request-body";

    /**
     * Names of MDC fields to be used for all logged fields.
     * Allows changes from children classes.
     */
    protected final Map<String, String> mdcFields = getDefaultFields();

    @Context
    ResourceInfo resourceInfo;

    /**
     * Puts a diagnostic context value identified by the given field into the current thread's context map.
     *
     * @param field The field for which put the given value
     * @param value The value to be associated with the given field
     */
    private void putMdc(LoggedField field, String value) {
        if (value != null) {
            MDC.put(mdcFields.get(field.name()), value);
        }
    }

    /**
     * Gets a diagnostic context value identified by the given field from the current thread's context map.
     *
     * @param field The field for which get the value
     * @return The value associated with the given field
     */
    private String getMdc(LoggedField field) {
        return MDC.get(mdcFields.get(field.name()));
    }

    /**
     * Gets the request identifier that will be stored in MDC for the complete request processing.
     * Returns the header value of {@code X-Request-ID} or a random UUID when not present.
     *
     * @param requestContext The context of the request received
     * @return The request identifier
     */
    protected String getRequestId(ClientRequestContext requestContext) {
        return of(requestContext)
                .map(ClientRequestContext::getHeaders)
                .map(headers -> String.valueOf(headers.getFirst("X-Request-ID")))
                .orElse(randomUUID().toString());
    }

    @Override
    public void filter(ClientRequestContext requestContext) {

        putMdc(REQUEST_URI, requestContext.getUri().toString());
        putMdc(REQUEST_PARAMETERS, requestContext.getUri()
                .getQuery());
        putMdc(REQUEST_METHOD, requestContext.getMethod());
        Optional.ofNullable(resourceInfo.getResourceClass())
                .map(Class::getSimpleName)
                .ifPresent(value -> putMdc(RESOURCE_CLASS, value));
        Optional.ofNullable(resourceInfo.getResourceMethod())
                .map(Method::getName)
                .ifPresent(value -> putMdc(RESOURCE_METHOD, value));

        // Logs directly from filter in case no request body is expected as aroundReadFrom will not be called
        if (!(requestContext.hasEntity())) {
            logRequest(EMPTY);
        }
    }

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {

        Object entity;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TeeInputStream teeInputStream = new TeeInputStream(
                context.getInputStream(),
                new BoundedOutputStream(outputStream, -1));
        context.setInputStream(teeInputStream);
        entity = context.proceed();
        String body = outputStream.toString();
        if (isNotBlank(body)) {
            logResponse(body);
        }

        return entity;
    }

    /**
     * Logs the request received by the server.
     * Note that the request method and URI must have been stored in MDC before calling this method.
     *
     * @param requestBody The request body to be logged
     */
    protected void logRequest(String requestBody) {
        log.info("{} {}{}{}",
                getMdc(REQUEST_METHOD),
                getMdc(REQUEST_URI),
                isNotBlank(requestBody) ? LF : EMPTY,
                requestBody);
    }

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) {
        long requestStartTime = Optional.ofNullable(requestContext.getProperty(REQUEST_TIME_PROPERTY))
                .map(Object::toString)
                .map(Long::parseLong)
                .orElse(nanoTime());
        long duration = (nanoTime() - requestStartTime) / 1_000_000;
        putMdc(DURATION, valueOf(duration));
        putMdc(RESPONSE_STATUS, valueOf(responseContext.getStatus()));

        // Logs directly from filter in case no response body is present as aroundWriteTo will not be called
        if (!responseContext.hasEntity()) {
            logResponse(EMPTY);
        }
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            TeeOutputStream teeOutputStream = new TeeOutputStream(
                    context.getOutputStream(),
                    new BoundedOutputStream(outputStream, -1));
            context.setOutputStream(teeOutputStream);
            context.proceed();

            logRequest(requireNonNullElse(outputStream.toString(), EMPTY));
        }
    }

    protected void logResponse(String responseBody) {
        try {
            log.info("{} {} {} {}ms{}{}",
                    getMdc(REQUEST_METHOD),
                    getMdc(REQUEST_URI),
                    getMdc(RESPONSE_STATUS),
                    getMdc(DURATION),
                    isNotBlank(responseBody) ? LF : EMPTY,
                    responseBody);

        } finally {
            cleanupMdc();
        }
    }

    protected void cleanupMdc() {
        mdcFields.values().forEach(MDC::remove);
    }

}