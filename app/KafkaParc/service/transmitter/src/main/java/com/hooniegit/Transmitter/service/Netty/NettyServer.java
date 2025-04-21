package com.hooniegit.Transmitter.service.Netty;

import com.hooniegit.NettyDataProtocol.Tools.Server;
import com.hooniegit.SourceData.Interface.TagData;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import org.springframework.stereotype.Service;

@Service
public class NettyServer {

    private Server<TagData<Double>> nettyServer;

    @PostConstruct
    public void start() throws Exception {
        int port = 13500;
        int bossThreads = 1;
        int workerThreads = Runtime.getRuntime().availableProcessors();

        nettyServer = new Server<>(
                port,
                bossThreads,
                workerThreads,
                Handler::new
        );

        nettyServer.start();
        System.out.println("Netty TCP Server Started on Port " + port);
    }

    @PreDestroy
    public void stop() throws Exception {
        if (nettyServer != null) {
            nettyServer.stop();
            System.out.println("Netty TCP Server Stopped.");
        }
    }

}