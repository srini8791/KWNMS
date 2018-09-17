package org.opennms.core.kwp.proxy.impl;

import org.opennms.core.kwp.KdpGetRequestDTO;
import org.opennms.core.kwp.KeywestLTVPacket;
import org.opennms.core.kwp.KeywestPacket;
import org.opennms.core.kwp.KeywestPacketHeader;
import org.opennms.core.kwp.proxy.KdpRequestBuilder;
import org.opennms.core.kwp.proxy.LocationAwareKdpClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LocationAwareKdpClientImpl implements LocationAwareKdpClient {


    @Override
    public KdpRequestBuilder<KeywestPacket> get(String host, byte id, byte type, byte subType) {

        return null;
    }

    @Override
    public KdpRequestBuilder<KeywestPacket> get(String host, KeywestPacketHeader header) {
        KdpGetRequestDTO getRequestDTO = new KdpGetRequestDTO();
        getRequestDTO.buildGetRequest(header);
        return new KdpRequestBuilderImpl();
    }

    @Override
    public KdpRequestBuilder<KeywestPacket> get(String host, KeywestPacketHeader header, List<KeywestLTVPacket> ltvs) {
        return null;
    }

    /*
    @Override
    public KdpRequestBuilder<KeywestPacket> get(String host,) {
        return new KdpRequestBuilder<KeywestPacket>() {
            @Override
            public KdpRequestBuilder<KeywestPacket> withLocation(String location) {
                return null;
            }

            @Override
            public CompletableFuture<KeywestPacket> execute() {
                return null;
            }
        };
    }*/
}
