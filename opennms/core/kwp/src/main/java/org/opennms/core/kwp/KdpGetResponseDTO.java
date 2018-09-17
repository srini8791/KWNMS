package org.opennms.core.kwp;

import org.opennms.core.rpc.api.RpcResponse;

public class KdpGetResponseDTO implements RpcResponse {

    private KeywestPacket packet;

    public KdpGetResponseDTO(byte[] responseBytes) {
        this.packet = new KeywestPacket(responseBytes);

    }

    @Override
    public String getErrorMessage() {
        return null;
    }
}
