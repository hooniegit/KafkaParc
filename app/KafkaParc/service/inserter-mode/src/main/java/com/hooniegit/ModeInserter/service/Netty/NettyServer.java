package com.hooniegit.ModeInserter.service.Netty;

import com.hooniegit.ModeInserter.service.MSSQL.StateService;
import com.hooniegit.NettyDataProtocol.Tools.Server;
import com.hooniegit.SourceData.Interface.TagData;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NettyServer {

    @Autowired
    private StateService service;

    private Server<TagData<Boolean>> nettyServer;

    @PostConstruct
    public void start() throws Exception {
        int port = 13510;
        int bossThreads = 1;
        int workerThreads = Runtime.getRuntime().availableProcessors();
//        int workerThreads = 16;

        nettyServer = new Server<>(
                port,
                bossThreads,
                workerThreads,
//                TagHandler::new
                () -> new Handler(this.service)
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
