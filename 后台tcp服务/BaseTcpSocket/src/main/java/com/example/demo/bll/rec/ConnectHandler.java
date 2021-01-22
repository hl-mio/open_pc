package com.example.demo.bll.rec;

import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.channel.*;

import java.net.InetSocketAddress;

import static com.example.demo.bll.rec.TcpSocketServer.*;

@Log
public class ConnectHandler extends SimpleChannelHandler {

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        InetSocketAddress ipSocket = (InetSocketAddress) ctx.getChannel().getRemoteAddress();
        String clientIp = ipSocket.getAddress().getHostAddress();
        String ip_port = clientIp+":"+ipSocket.getPort();
        //if(!channelMap.containsKey(ip_port)){
        //    channelMap.put(ip_port,ctx);
        //}
        channelMap.put(ip_port,ctx);
        log.info("新来Client：" + clientIp + ":" + ipSocket.getPort());
        super.channelConnected(ctx, e);
    }


    /**
     * 必须是链接已经建立，关闭通道的时候会触发
     */
    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        InetSocketAddress ipSocket = (InetSocketAddress) ctx.getChannel().getRemoteAddress();
        String clientIp = ipSocket.getAddress().getHostAddress();
        String ip_port = clientIp+":"+ipSocket.getPort();
        System.out.println("channelDisconnected");
        if(channelMap.containsKey(ip_port)){
            channelMap.remove(ip_port);
            isLoginMap.remove(ip_port);
            devIdMap.remove(ip_port);
        }
        super.channelDisconnected(ctx, e);
    }


    /**
     * channel关闭的时候触发
     */
    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelClosed");
        super.channelClosed(ctx, e);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        System.out.println("exceptionCaught");
        super.exceptionCaught(ctx, e);
    }


    /**
     * 接收消息
     */
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
//        InetSocketAddress ipSocket = (InetSocketAddress) ctx.getChannel().getRemoteAddress();
//        String clientIp = ipSocket.getAddress().getHostAddress();
//        String ip_port = clientIp+":"+ipSocket.getPort();
//        System.out.println("来自 " + ip_port);
        String message=(String) e.getMessage();
//        System.out.println("接收的数据");
//        System.out.println(message);
//        System.out.println("");

        super.messageReceived(ctx, e);

        String rstMessage = "";
        try {
            rstMessage = MessageHandler.handler(ctx, message);
        }catch (Exception ex){
            log.info("消息接收处理失败,异常信息如下:");
            log.info(ex.getMessage());
            System.out.println(ex);
        }

        //回写数据
        if (StringUtils.isNotEmpty(rstMessage)) {
            ctx.getChannel().write(rstMessage);
        }

    }

}