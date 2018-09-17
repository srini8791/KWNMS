package org.opennms.core.kwp;

import org.opennms.core.rpc.api.RpcRequest;

public class KdpGetRequestDTO implements RpcRequest {

    private KeywestPacket packet;

    private String host;

    public KdpGetRequestDTO() {

    }

    public void buildGetRequest(KeywestPacketHeader header) {
        if (this.packet == null) {
            this.packet = new KeywestPacket();
        }
        this.packet.setHeader(header);
    }

    public KeywestPacket getPacket() {
        return packet;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPacket(KeywestPacket packet) {
        this.packet = packet;
    }

    private byte getRequestId = 1;


    @Override
    public String getLocation() {
        return null;
    }

    @Override
    public String getSystemId() {
        return null;
    }

    @Override
    public Long getTimeToLiveMs() {
        return null;
    }
}
