package org.opennms.core.kwp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CompletableFuture;

public class KDPAsyncClient {
    private String host;
    private int port = 7861;
    private AsynchronousSocketChannel client = null;

    public KDPAsyncClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public KDPAsyncClient() {
    }


    public CompletableFuture<KdpGetResponseDTO> sendAndReceive(KdpGetRequestDTO request) throws IOException {
        this.host = request.getHost();
        CompletableFuture<KdpGetResponseDTO> result = new CompletableFuture<>();
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
                                        KdpGetResponseDTO responseDTO = new KdpGetResponseDTO(responseBytes);
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

    public CompletableFuture<KdpGetResponseDTO> send(KdpGetRequestDTO request) {
        return null;
    }
}
