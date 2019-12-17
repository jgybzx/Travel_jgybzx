package com.jgybzx.utils;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ResourceBundle;

/**
 * @author: guojy
 * @date: 2019/12/15 16:24
 * @Description: 从连接池中获取对象并返回
 * @version:
 */
public class RedisUtils {
    //首先需要初始化一个池，但是考虑到，每次调用都会创建一个，为了节省资源，想到静态加载，
    private static JedisPool jedisPool = null;

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        ResourceBundle bundle = ResourceBundle.getBundle("jedis");
        String host = bundle.getString("jedis.host");
        int post = Integer.parseInt(bundle.getString("jedis.post"));
        jedisPool = new JedisPool(config, host, post);
    }

    public static Jedis getJedisBean() {
        Jedis jedis = jedisPool.getResource();
        return jedis;
        //记住需要手动关闭
    }
}
