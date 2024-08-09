package com.example.springbootstudy;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;

import java.util.Random;
import java.util.Set;

/**
 * @author MCW 2022/7/24
 */
//@SpringBootTest
public class RedisDemoTest {

    static Logger logger = LoggerFactory.getLogger(RedisDemoTest.class);

    public static Jedis jedis;


    @BeforeAll
    public static void connectionRedis() {
        HostAndPort hostAndPort = new HostAndPort("192.168.190.12", 6379);
        jedis = new Jedis(hostAndPort);

        String ping = jedis.ping();
        System.out.println(ping);
    }

    @AfterAll
    public static void closeConnection() {
        jedis.close();
    }

    @Test
    public void jedisForString() {
        jedis.set("s2", "哈哈哈哈哈");
        jedis.set("s3", "string");
        jedis.set("s4", "clipboard");
        jedis.set("s5", "mine");

        jedis.mset("v1", "13", "v2", "12");

        Set<String> keys = jedis.keys("*");
        for (String key : keys) {
            System.out.println("key is :" + key + ", value is:" + jedis.get(key));
        }

        System.out.println(jedis.exists("s3"));
        System.out.println(jedis.ttl("s3"));

    }

    @Nested
    public class phoneCode {

        String code;
        final String phone="12345867915";

        @BeforeEach
        public void before(){
            verifyCode(phone);
        }

        @Test
        public void login(){
            getRedisCode(phone,code);
        }

        public String getCode() {
            Random random = new Random();
            StringBuilder code = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                int rand = random.nextInt(10);
                code.append(rand);
            }
            System.out.println(code.toString());
            return code.toString();
        }

        public void verifyCode(String phone) {
            String countKey = "VerifyCode" + phone + ":count";
            String codeKey = "VerifyCode" + phone + ":code";

            String count = jedis.get(countKey);
            if (null == count) {
                jedis.setex(countKey, 24 * 60 * 60, "1");
            } else if (Integer.parseInt(count) <= 2) {
                jedis.incr(codeKey);
            } else if (Integer.parseInt(count) > 2) {
                System.out.println("今天发送次数已超过三次");
                return;
            }

            code = getCode();
            jedis.setex(codeKey, 180, code);
        }

        public void getRedisCode(String phone,String code){
            String key="VerifyCode" + phone + ":code";
            String redisCode = jedis.get(key);
            System.out.println("redisCode------"+redisCode);

            if (code.equals(redisCode)){
                System.out.println("成功");
            }else {
                System.out.println("失败");
            }
        }
    }


}
