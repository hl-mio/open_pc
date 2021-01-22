package com.example.demo.bll.rec;

import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.example.demo.bll.rec.TcpSocketServer.*;

@Log
public class MessageHandler {

    public static SimpleDateFormat oracleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // "yyyy-MM-dd hh:mm:ss.ff"

    @SneakyThrows
    public static String handler(ChannelHandlerContext ctx, String message){
        String rstMessage = null;

        if (StringUtils.isEmpty(message)) {
            message = "";
        }

        InetSocketAddress ipSocket = (InetSocketAddress) ctx.getChannel().getRemoteAddress();
        String clientIp = ipSocket.getAddress().getHostAddress();
        String ip_port = clientIp+":"+ipSocket.getPort();

        System.out.println("");
        System.out.println(oracleDateFormat.format(new Date()));
        System.out.println("来自 " + ip_port);
        System.out.println("接收到的信息是：");
        System.out.println(message);
        System.out.println("长度：" + message.length());

        //String hexMessage = message.replace("-", " ");
        //String asciiMessage = HexStrChange.hexStr2Str(hexMessage, String.valueOf(Charset.defaultCharset()));
        //System.out.println("转码后：" + asciiMessage);

        //info指的是解码后的数据
        String info = message;

        info = info.trim();
        if(info.startsWith("id:=")){
            String[] partStrs = info.split(":=");
            String id = partStrs[1];
            devIdMap.put(ip_port,id);

            rstMessage = "200";
            return rstMessage;
        }

        if(!info.startsWith("keep")){
            rstMessage = "hello world";
        }
        return rstMessage;
    }

}
