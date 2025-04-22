package com.hooniegit.StatusTwoRecorder.service.Netty;

import com.hooniegit.NettyDataProtocol.Tools.DefaultHandler;
import com.hooniegit.SourceData.Interface.TagData;
import com.hooniegit.StatusTwoRecorder.service.MSSQL.StateService;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 *
 */
public class Handler extends DefaultHandler<TagData<String>> {

    private final StateService service;

    public Handler(StateService service) {
        this.service = service;
    }
    /**
     *
     * @param ctx
     * @param msg
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, List<TagData<String>> msg) {
        System.out.println("Received " + msg.size() + " tags");
        this.service.check(msg);
    }

}
