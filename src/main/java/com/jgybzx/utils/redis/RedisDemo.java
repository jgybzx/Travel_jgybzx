package com.jgybzx.utils.redis;


import com.jgybzx.utils.RedisUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

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
    @Test
    public void TsestRedis(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "192.168.0.106", 6379);
        Jedis jedis = jedisPool.getResource();
        jedis.set("name","giao");
        System.out.println("jedis.get(\"name\") = " + jedis.get("name"));
        jedis.close();
    }
}
