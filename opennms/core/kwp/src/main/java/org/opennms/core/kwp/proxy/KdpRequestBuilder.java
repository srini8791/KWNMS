package org.opennms.core.kwp.proxy;

import org.opennms.core.kwp.KeywestPacket;

import java.util.concurrent.CompletableFuture;

public interface KdpRequestBuilder<T> {

    KdpRequestBuilder<T> withLocation(String location);

    KdpRequestBuilder<T> buildRequest();

    CompletableFuture<T> execute();

    KeywestPacket processResponse();

}
