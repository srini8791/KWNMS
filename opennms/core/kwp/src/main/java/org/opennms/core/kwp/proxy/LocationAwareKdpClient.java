package org.opennms.core.kwp.proxy;

import org.opennms.core.kwp.KeywestLTVPacket;
import org.opennms.core.kwp.KeywestPacket;
import org.opennms.core.kwp.KeywestPacketHeader;
import org.opennms.core.kwp.proxy.KdpRequestBuilder;

import java.util.List;

public interface LocationAwareKdpClient {

    KdpRequestBuilder<KeywestPacket> get(String host, byte id, byte type, byte subType);

    KdpRequestBuilder<KeywestPacket> get(String host, KeywestPacketHeader header);

    KdpRequestBuilder<KeywestPacket> get(String host, KeywestPacketHeader header, List<KeywestLTVPacket> ltvs);


}
