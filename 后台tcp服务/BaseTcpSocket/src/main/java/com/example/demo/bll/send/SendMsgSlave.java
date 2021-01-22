package com.example.demo.bll.send;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.demo.bll.rec.TcpSocketServer.channelMap;


@Log
@AllArgsConstructor
public class SendMsgSlave extends Thread {

    public String ip_port = "";
    public String message = "";

    @Override
    public void run() {
        System.out.println();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
        System.out.println("发给：" + ip_port);
        System.out.println(message);
        channelMap.get(ip_port).getChannel().write(message);
    }
}