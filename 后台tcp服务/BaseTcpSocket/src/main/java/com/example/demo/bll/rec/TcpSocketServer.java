package com.example.demo.bll.rec;

import com.example.demo.bll.DeviceLogManager;
import com.example.demo.props.TcpSocketProperties;
import lombok.extern.java.Log;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * netty官方文档：http://netty.io/3.10/api/index.html
 */
@Log
@Component
public class TcpSocketServer extends Thread {
    
    public static Map<String,Boolean> isLoginMap = new HashMap<String,Boolean>();
    public static Map<String,ChannelHandlerContext> channelMap = new HashMap<String,ChannelHandlerContext>();
    public static Map<String,String> devIdMap = new HashMap<String,String>();

    // 这些都是默认值，会被配置文件（application-socket.yml）覆盖
    public String charsetName = "UTF-8";
    public int port = 45003;

    public static String redis_open_pc_key = "open_pc:开机命令队列";
    public static String redis_shutdown_pc_key = "open_pc:关机命令队列";

    @Autowired
    public TcpSocketProperties tcpSocketProperties;

    public static void Login(String ip_port){
        if(!isLoginMap.containsKey(ip_port)){
            isLoginMap.put(ip_port, false);
        }
        if(isLoginMap.get(ip_port) != true){
            isLoginMap.put(ip_port, true);
        }
    }


    @PostConstruct
    public void init(){
        this.port = tcpSocketProperties.getPort();
        this.charsetName = tcpSocketProperties.getCharsetName();

        redis_open_pc_key = tcpSocketProperties.getRedis_open_pc_key();
        redis_shutdown_pc_key = tcpSocketProperties.getRedis_shutdown_pc_key();
    }


    @Override
    public void run() {

        //服务类
        ServerBootstrap bootStrap=new ServerBootstrap();

        //创建两个线程池
        ExecutorService boss=Executors.newCachedThreadPool();   //线程池中的线程是client，主要负责端口的监听
        ExecutorService worker=Executors.newCachedThreadPool(); //主要负责读写任务

        //设置niosocket工厂
        bootStrap.setFactory(new NioServerSocketChannelFactory(boss, worker));

        // 设置超时控制
//        Timer trigger=new HashedWheelTimer();
//        final ChannelHandler timeOutHandler=new ReadTimeoutHandler(trigger,10);

        //设置管道的工厂
        bootStrap.setPipelineFactory(new ChannelPipelineFactory() {

            @Override
            public ChannelPipeline getPipeline() throws Exception {

                ChannelPipeline pipeline=Channels.pipeline();
                Charset charset = Charset.forName(charsetName);

                // 设置超时控制
//                pipeline.addLast("too_late_check", new ReadTimeoutHandler(trigger,10));
//                pipeline.addLast("too_late_handle", new TooLateHandler());

                // （1）直接读取方式
                //解码
                pipeline.addLast("decoder", new StringDecoder(charset));
                //编码
                pipeline.addLast("encoder", new StringEncoder(charset));

                // 设置解码和编码器
//                pipeline.addLast("decoder1",new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
//                pipeline.addLast("decoder2",new StringDecoder(charset));
//                pipeline.addLast("encoder", new StringEncoder(charset));

                pipeline.addLast("ConnectHandler", new ConnectHandler());

                return pipeline;
            }
        });

        bootStrap.bind(new InetSocketAddress(port));
        log.info("tcp监听程序启动, 端口：" + port);
    }

}