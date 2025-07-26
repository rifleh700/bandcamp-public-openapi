package io.rifleh700.bpot;

import io.rifleh700.bpot.logging.LoggedFilter;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.ext.ContextResolver;
import org.eclipse.yasson.YassonConfig;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

public class RestEasyClientProxyFactory implements ClientProxyFactory {

    private final ResteasyWebTarget target;

    public RestEasyClientProxyFactory() {

        this.target = (ResteasyWebTarget) ClientBuilder
                .newBuilder()
                .register(new JsonbConfiguration())
                .register(LoggedFilter.class)
                .build()
                .target("https://bandcamp.com/api");
    }

    @Override
    public <T> T build(Class<T> api) {

        return target.proxy(api);
    }

    private static class JsonbConfiguration implements ContextResolver<Jsonb> {

        private Jsonb jsonb;

        public JsonbConfiguration() {

            this.jsonb = JsonbBuilder.create(new YassonConfig()
                    .withFailOnUnknownProperties(true));
        }

        @Override
        public Jsonb getContext(Class<?> type) {

            return jsonb;
        }

    }
}
