package com.example.demo.common;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

public class RedisHelper {
    public static JedisPoolConfig config;
    public static Set<HostAndPort> jedisClusterNode;
    static{
        config = new JedisPoolConfig();
        config.setMaxTotal(3);//设置最大连接数
        config.setMaxIdle(3); //设置最大空闲数
        config.setMaxWaitMillis(3000);//设置超时时间
        config.setTestOnBorrow(true);

        jedisClusterNode = new HashSet<HostAndPort>();
        jedisClusterNode.add(new HostAndPort("127.0.0.1", Integer.parseInt("35041")));
    }

    public static Jedis getJedis(){
        Jedis jedis = new Jedis("127.0.0.1",35004);
        jedis.auth("123456");
        return jedis;
    }

    public static JedisCluster getJedisCluster(){
        JedisCluster jc = new JedisCluster(jedisClusterNode, config);
        return jc;
    }
}
