package org.opennms.core.kwp.proxy.impl;

import org.opennms.core.rpc.api.RpcModule;
import org.opennms.core.rpc.api.RpcRequest;
import org.opennms.core.rpc.api.RpcResponse;

import java.util.concurrent.CompletableFuture;

public class KdpProxyRpcModule<S extends RpcRequest,T extends RpcResponse> implements RpcModule<S, T> {

    public static final KdpProxyRpcModule INSTANCE = new KdpProxyRpcModule();
    public static final String KDP_MODULE_ID = "KDP";


    @Override
    public String getId() {
        return null;
    }

    @Override
    public String marshalRequest(S request) {
        return null;
    }

    @Override
    public S unmarshalRequest(String request) {
        return null;
    }

    @Override
    public String marshalResponse(T response) {
        return null;
    }

    @Override
    public T unmarshalResponse(String response) {
        return null;
    }

    @Override
    public T createResponseWithException(Throwable ex) {
        return null;
    }

    @Override
    public CompletableFuture<T> execute(S request) {
        return null;
    }
}
