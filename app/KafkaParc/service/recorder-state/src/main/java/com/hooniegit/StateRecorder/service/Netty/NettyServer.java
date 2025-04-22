package com.hooniegit.StateRecorder.service.Netty;

import com.hooniegit.NettyDataProtocol.Tools.Server;
import com.hooniegit.SourceData.Interface.TagData;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NettyServer {

    @Autowired
    private com.hooniegit.StateRecorder.service.MSSQL.StateService service;

    private Server<TagData<Integer>> nettyServer;

    @PostConstruct
    public void start() throws Exception {
        int port = 13511;
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
