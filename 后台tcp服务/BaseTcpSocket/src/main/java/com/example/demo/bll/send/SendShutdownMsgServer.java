package com.example.demo.bll.send;

import com.example.demo.common.RedisHelper;
import com.example.demo.common.core.app;
import com.example.demo.props.TcpSocketProperties;
import lombok.extern.java.Log;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Map;

import static com.example.demo.bll.rec.TcpSocketServer.devIdMap;

@Log
@Component
public class SendShutdownMsgServer extends Thread {

    @Override
    public void run() {

        try(Jedis redis = RedisHelper.getJedis()) {
            TcpSocketProperties conf = (TcpSocketProperties) app.getBean("tcpSocketProperties");
            String redis_shutdown_pc_key = conf.getRedis_shutdown_pc_key();
            String message = "202";
            String devId = null;
            //监听消息队列
            while (true) {
                try {
                    devId = redis.brpop(0,redis_shutdown_pc_key).get(1);
                }
                catch (Exception e) {
                    log.info("关机信息发送程序 未获取redis的数据,异常信息: " + e.getMessage());
                    Thread.sleep(1000);
                    devId = null;
                }

                try {
                    if (devId != null) {
                        for(Map.Entry entry:devIdMap.entrySet()){
                            if(devId.equals(entry.getValue())){
                                //启动一个线程来处理数据发送
                                SendMsgSlave job = new SendMsgSlave((String)entry.getKey(),message);
                                job.start();
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    log.info("关机信息发送程序 报错,异常信息: " + e.getMessage());
                }
            }
        }
        catch (Exception e) {
            log.info("关机信息发送程序 异常关闭,异常信息: " + e.getMessage());
        }
    }
}