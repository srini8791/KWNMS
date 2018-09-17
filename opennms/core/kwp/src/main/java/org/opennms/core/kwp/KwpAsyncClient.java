/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.core.kwp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CompletableFuture;

public class KwpAsyncClient {
    private String host;
    private int port = 7861;
    private AsynchronousSocketChannel client = null;

    public KwpAsyncClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public KwpAsyncClient() {
    }


    public CompletableFuture<KwpGetResponseDTO> sendAndReceive(KwpGetRequestDTO request) throws IOException {
        this.host = request.getHost();
        CompletableFuture<KwpGetResponseDTO> result = new CompletableFuture<>();
        client = AsynchronousSocketChannel.open();
        client.connect(new InetSocketAddress(host, port),null, new CompletionHandler<Void, Void>() {
            @Override
            public void completed(Void v, Void attachment) {
                byte[] bytes = null;
                try {
                    bytes = request.getPacket().toByteArray();

                } catch (Exception ex) {

                }
                ByteBuffer buffer = ByteBuffer.wrap(bytes);
                client.write(buffer, null, new CompletionHandler<Integer, Void>() {

                    @Override
                    public void completed(Integer length, Void attachment) {
                        if (buffer.remaining() > 0) {
                            client.write(buffer, null, this);
                        } else {
                            ByteBuffer readBuffer = ByteBuffer.allocate(1306);
                            client.read(readBuffer, null, new CompletionHandler<Integer, Void>() {

                                @Override
                                public void completed(Integer length, Void attachment) {
                                    if (length > 0 && readBuffer.hasRemaining()) {
                                        client.read(readBuffer, null, this);
                                    } else {
                                        byte[] responseBytes = new byte[readBuffer.position()];
                                        readBuffer.position(0);
                                        readBuffer.get(responseBytes);
                                        KwpGetResponseDTO responseDTO = new KwpGetResponseDTO(responseBytes);
                                        result.complete(responseDTO);
                                    }
                                }

                                @Override
                                public void failed(Throwable exc, Void attachment) {
                                    result.completeExceptionally(exc);
                                }
                            });
                        }

                    }

                    @Override
                    public void failed(Throwable exc, Void attachment) {
                        result.completeExceptionally(exc);
                    }
                });
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                result.completeExceptionally(exc);
            }
        });
        return result;
    }

    public CompletableFuture<KwpGetResponseDTO> send(KwpGetRequestDTO request) {
        return null;
    }
}
