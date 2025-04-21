package com.hooniegit.StateInserter.service.Netty;



import com.hooniegit.NettyDataProtocol.Tools.DefaultHandler;
import com.hooniegit.SourceData.Interface.TagData;
import com.hooniegit.StateInserter.service.MSSQL.StateService;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 *
 */
public class Handler extends DefaultHandler<TagData<Integer>> {

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
    protected void channelRead0(ChannelHandlerContext ctx, List<TagData<Integer>> msg) {
        System.out.println("Received " + msg.size() + " tags");
        this.service.check(msg);
    }

}
