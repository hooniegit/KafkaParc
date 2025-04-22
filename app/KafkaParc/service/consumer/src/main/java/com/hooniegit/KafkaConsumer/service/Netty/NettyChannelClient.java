package com.hooniegit.KafkaConsumer.service.Netty;

import com.hooniegit.NettyDataProtocol.Tools.Decoder;
import com.hooniegit.NettyDataProtocol.Tools.Encoder;

import com.hooniegit.SourceData.Interface.TagData;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Netty TCP Client Component
 * - Manage Configurations
 * - Initialize Connections
 * - Send Datas to Netty Server
 */
@Component
public class NettyChannelClient {

    // [tag] Client Initialization
    private volatile boolean initialized_tag = false;

    private static final int CHANNEL_COUNT_tag = 16;
    private static final String HOST_tag = "localhost";
    private static final int PORT_tag = 13500;

    private final List<Channel> channels_tag = new ArrayList<>(CHANNEL_COUNT_tag);
    private final NioEventLoopGroup group_tag = new NioEventLoopGroup();
    private final AtomicInteger index_tag = new AtomicInteger(0);

    // [mode] Client Initialization
    private volatile boolean initialized_mode = false;

    private static final int CHANNEL_COUNT_mode = 16;
    private static final String HOST_mode = "localhost";
    private static final int PORT_mode = 13510;

    private final List<Channel> channels_mode = new ArrayList<>(CHANNEL_COUNT_mode);
    private final NioEventLoopGroup group_mode = new NioEventLoopGroup();
    private final AtomicInteger index_mode = new AtomicInteger(0);

    // [state] Client Initialization
    private volatile boolean initialized_state = false;

    private static final int CHANNEL_COUNT_state = 16;
    private static final String HOST_state = "localhost";
    private static final int PORT_state = 13511;

    private final List<Channel> channels_state = new ArrayList<>(CHANNEL_COUNT_state);
    private final NioEventLoopGroup group_state = new NioEventLoopGroup();
    private final AtomicInteger index_state = new AtomicInteger(0);

    // [statusOne] Client Initialization
    private volatile boolean initialized_statusOne = false;

    private static final int CHANNEL_COUNT_statusOne = 16;
    private static final String HOST_statusOne = "localhost";
    private static final int PORT_statusOne = 13512;

    private final List<Channel> channels_statusOne = new ArrayList<>(CHANNEL_COUNT_statusOne);
    private final NioEventLoopGroup group_statusOne = new NioEventLoopGroup();
    private final AtomicInteger index_statusOne = new AtomicInteger(0);

    // [statusTwo] Client Initialization
    private volatile boolean initialized_statusTwo = false;

    private static final int CHANNEL_COUNT_statusTwo = 16;
    private static final String HOST_statusTwo = "localhost";
    private static final int PORT_statusTwo = 13513;

    private final List<Channel> channels_statusTwo = new ArrayList<>(CHANNEL_COUNT_statusTwo);
    private final NioEventLoopGroup group_statusTwo = new NioEventLoopGroup();
    private final AtomicInteger index_statusTwo = new AtomicInteger(0);

    // Initializers //////////////////////////////////////////////////

    public synchronized void init_tag() {
        if (initialized_tag) return;

        // [tag] Client Initialization
        Bootstrap bootstrap_tag = new Bootstrap()
                .group(group_tag)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(
                                new Encoder<TagData<Double>>(),
                                new Decoder(),
                                new ChannelInboundHandlerAdapter()
                        );
                    }
                });

        try {
            for (int i = 0; i < CHANNEL_COUNT_tag; i++) {
                Channel channel = bootstrap_tag.connect(HOST_tag, PORT_tag).sync().channel();
                channels_tag.add(channel);
            }
            initialized_tag = true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize channels", e);
        }
    }

    public synchronized void init_mode() {
        if (initialized_mode) return;

        // [mode] Client Initialization
        Bootstrap bootstrap_mode = new Bootstrap()
                .group(group_mode)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(
                                new Encoder<TagData<Double>>(),
                                new Decoder(),
                                new ChannelInboundHandlerAdapter()
                        );
                    }
                });

        try {
            for (int i = 0; i < CHANNEL_COUNT_mode; i++) {
                Channel channel = bootstrap_mode.connect(HOST_mode, PORT_mode).sync().channel();
                channels_mode.add(channel);
            }
            initialized_mode = true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize channels", e);
        }
    }

    public synchronized void init_state() {
        if (initialized_state) return;

        // [state] Client Initialization
        Bootstrap bootstrap_state = new Bootstrap()
                .group(group_state)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(
                                new Encoder<TagData<Double>>(),
                                new Decoder(),
                                new ChannelInboundHandlerAdapter()
                        );
                    }
                });

        try {
            for (int i = 0; i < CHANNEL_COUNT_state; i++) {
                Channel channel = bootstrap_state.connect(HOST_state, PORT_state).sync().channel();
                channels_state.add(channel);
            }
            initialized_state = true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize channels", e);
        }
    }

    public synchronized void init_statusOne() {
        if (initialized_statusOne) return;

        // [statusOne] Client Initialization
        Bootstrap bootstrap_statusOne = new Bootstrap()
                .group(group_statusOne)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(
                                new Encoder<TagData<Double>>(),
                                new Decoder(),
                                new ChannelInboundHandlerAdapter()
                        );
                    }
                });

        try {
            for (int i = 0; i < CHANNEL_COUNT_statusOne; i++) {
                Channel channel = bootstrap_statusOne.connect(HOST_statusOne, PORT_statusOne).sync().channel();
                channels_statusOne.add(channel);
            }
            initialized_statusOne = true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize channels", e);
        }
    }

    public synchronized void init_statusTwo() {
        if (initialized_statusTwo) return;

        // [statusTwo] Client Initialization
        Bootstrap bootstrap = new Bootstrap()
                .group(group_statusTwo)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(
                                new Encoder<TagData<Double>>(),
                                new Decoder(),
                                new ChannelInboundHandlerAdapter()
                        );
                    }
                });

        try {
            for (int i = 0; i < CHANNEL_COUNT_statusTwo; i++) {
                Channel channel = bootstrap.connect(HOST_statusTwo, PORT_statusTwo).sync().channel();
                channels_statusTwo.add(channel);
            }
            initialized_statusTwo = true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize channels", e);
        }
    }

    // Data Transmitters //////////////////////////////////////////////////

    public void sendData(List<TagData<Double>> data) {
        if (!initialized_tag) {
            throw new IllegalStateException("Client not initialized. Call init() first.");
        }

        int channelIndex = index_tag.getAndUpdate(i -> (i + 1) % CHANNEL_COUNT_tag);
        Channel channel = channels_tag.get(channelIndex);

        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(data);
        } else {
            System.err.println("Channel " + channelIndex + " is not active.");
        }
    }

    public void sendMode(List<TagData<Boolean>> data) {
        if (!initialized_mode) {
            throw new IllegalStateException("Client not initialized. Call init() first.");
        }

        int channelIndex = index_mode.getAndUpdate(i -> (i + 1) % CHANNEL_COUNT_mode);
        Channel channel = channels_mode.get(channelIndex);

        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(data);
        } else {
            System.err.println("Channel " + channelIndex + " is not active.");
        }
    }

    public void sendState(List<TagData<Integer>> data) {
        if (!initialized_state) {
            throw new IllegalStateException("Client not initialized. Call init() first.");
        }

        int channelIndex = index_state.getAndUpdate(i -> (i + 1) % CHANNEL_COUNT_state);
        Channel channel = channels_state.get(channelIndex);

        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(data);
        } else {
            System.err.println("Channel " + channelIndex + " is not active.");
        }
    }

    public void sendStatusOne(List<TagData<String>> data) {
        if (!initialized_statusOne) {
            throw new IllegalStateException("Client not initialized. Call init() first.");
        }

        int channelIndex = index_statusOne.getAndUpdate(i -> (i + 1) % CHANNEL_COUNT_statusOne);
        Channel channel = channels_statusOne.get(channelIndex);

        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(data);
        } else {
            System.err.println("Channel " + channelIndex + " is not active.");
        }
    }

    public void sendStatusTwo(List<TagData<String>> data) {
        if (!initialized_statusTwo) {
            throw new IllegalStateException("Client not initialized. Call init() first.");
        }

        int channelIndex = index_statusTwo.getAndUpdate(i -> (i + 1) % CHANNEL_COUNT_statusTwo);
        Channel channel = channels_statusTwo.get(channelIndex);

        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(data);
        } else {
            System.err.println("Channel " + channelIndex + " is not active.");
        }
    }

    // Others //////////////////////////////////////////////////
    
    @PreDestroy
    public void shutdown() {
        for (Channel channel : channels_tag) {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
        }
        group_tag.shutdownGracefully();
        System.out.println("Client shutdown.");
    }

}


