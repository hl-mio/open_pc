package com.example.demo.bll.rec;

import lombok.extern.java.Log;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.timeout.ReadTimeoutException;


@Log
public class TooLateHandler extends SimpleChannelHandler {


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        System.out.println("超时并捕获了异常，即将关闭通道");
        System.out.println(Thread.currentThread().getName());
        System.out.println(Thread.currentThread().getId());
        System.out.println(System.identityHashCode(this));
        System.out.println(this.hashCode());

        Throwable cause = e.getCause();
        if (cause instanceof ReadTimeoutException) {
            // The connection was OK but there was no traffic for last period.
            System.out.println("Disconnecting due to no inbound traffic");
        } else {
            cause.printStackTrace();
            //  super.exceptionCaught(ctx, e);
        }
        ctx.getChannel().close();
    }


}