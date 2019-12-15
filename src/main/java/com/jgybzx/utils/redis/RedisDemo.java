package com.jgybzx.utils.redis;


import com.jgybzx.utils.RedisUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author: guojy
 * @date: 2019/12/15 15:32
 * @Description:
 * @version:
 */
public class RedisDemo {
    public static void main(String[] args) throws InterruptedException {
        Jedis jedisBean = RedisUtils.getJedisBean();
        String s = jedisBean.get("123");
        System.out.println(s);
        jedisBean.close();
    }
}
