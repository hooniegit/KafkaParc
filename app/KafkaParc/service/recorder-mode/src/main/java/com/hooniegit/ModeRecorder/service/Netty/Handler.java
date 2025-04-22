package com.hooniegit.ModeRecorder.service.Netty;

import com.hooniegit.ModeRecorder.service.MSSQL.StateService;
import com.hooniegit.NettyDataProtocol.Tools.DefaultHandler;
import com.hooniegit.SourceData.Interface.TagData;

import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 *
 */
public class Handler extends DefaultHandler<TagData<Boolean>> {

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
    protected void channelRead0(ChannelHandlerContext ctx, List<TagData<Boolean>> msg) {
        System.out.println("Received " + msg.size() + " tags");
        this.service.check(msg);
    }

}
