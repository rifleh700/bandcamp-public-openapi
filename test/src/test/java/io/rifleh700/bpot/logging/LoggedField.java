package io.rifleh700.bpot.logging;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * List of context fields to be written in MDC.
 */
public enum LoggedField {

    REQUEST_ID("request-id"),
    REQUEST_METHOD("request-method"),
    REQUEST_URI("request-uri"),
    REQUEST_PARAMETERS("request-parameters"),
    REQUEST_BODY("request-body"),
    RESPONSE_BODY("response-body"),
    RESPONSE_STATUS("response-status"),
    RESOURCE_CLASS("resource-class"),
    RESOURCE_METHOD("resource-method"),
    DURATION("duration");

    private final String defaultField;

    /**
     * Creates a new context field to be logged in MDC.
     *
     * @param defaultField The default MDC field name
     */
    LoggedField(String defaultField) {
        this.defaultField = defaultField;
    }

    /**
     * Gets a {@link Map} with the enumeration name as key and the default field name as value.
     *
     * @return The corresponding {@link Map}
     */
    public static Map<String, String> getDefaultFields() {
        Map<String, String> map = new HashMap<>();
        Stream.of(LoggedField.values()).forEach(entry -> map.put(entry.name(), entry.getDefaultField()));
        return map;
    }

    /**
     * Gets the default MDC field name to be used.
     *
     * @return The default name
     */
    public String getDefaultField() {
        return this.defaultField;
    }

}
