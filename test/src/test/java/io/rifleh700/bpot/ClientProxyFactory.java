package io.rifleh700.bpot;

import java.util.function.Supplier;

public interface ClientProxyFactory {

    <T> T build(Class<T> api);
}
