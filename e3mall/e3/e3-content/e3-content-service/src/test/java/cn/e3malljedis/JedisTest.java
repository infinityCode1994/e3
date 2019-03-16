package cn.e3malljedis;

import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

public class JedisTest {
    @Test
    public void testJedis() throws  Exception{
        //创建一个连接Jedis对象，参数：host、port
        Jedis jedis = new Jedis("192.168.25.129",6379);
        //直接使用jedis操作Redis。所有jedis的命令对应一个方法
        jedis.set("test123","my first jedis test");
        String str = jedis.get("test123");
        System.out.println(str);
        //关闭连接
        jedis.close();
    }
    @Test
    public void testjedisPool() throws Exception{
        //创建一个连接池对象，两个参数host、port
        JedisPool jedisPool = new JedisPool("192.168.25.129",6379);
        //从连接池获得一个连接，就是一个连接对象
        Jedis jedis = jedisPool.getResource();
        //使用jedis操作Redis
        String str2 = jedis.get("test123");
        System.out.println(str2);
        //关闭连接，每次用完后关闭连接。连接处回收资源。
        jedis.close();
        //关闭连接池
        jedisPool.close();
    }
    @Test
    public void  testJedisCluster() throws  Exception{
        //创建一个jediscluster对象，有一个参数nodes是一个set类型。set中包含若干个hostandport对象。
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.25.129",7001));
        nodes.add(new HostAndPort("192.168.25.129",7002));
        nodes.add(new HostAndPort("192.168.25.129",7003));
        nodes.add(new HostAndPort("192.168.25.129",7004));
        nodes.add(new HostAndPort("192.168.25.129",7005));
        nodes.add(new HostAndPort("192.168.25.129",7006));
        JedisCluster jedisCluster = new JedisCluster(nodes);
        //直接使用jediscluster对象操作Redis
        jedisCluster.set("test","123");
        String str3 = jedisCluster.get("test");
        System.out.println(str3);
        //关闭JedisCluster对象
        jedisCluster.close();
    }
}
