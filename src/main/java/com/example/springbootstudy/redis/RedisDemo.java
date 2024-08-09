package com.example.springbootstudy.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author MCW 2022/7/23
 */
@RestController
public class RedisDemo {
    Logger logger = LoggerFactory.getLogger(RedisDemo.class);

    @Resource
    JedisPool jedisPool;
    //@Resource
    //private RedisTemplate<Object, Object> redisTemplate;
    //
    //
    //@RequestMapping("/redisUse")
    //@ResponseBody
    //public String redisUse() {
    //    String s1 = (String) redisTemplate.opsForValue().get("s1");
    //    logger.info("获取到的值是：" + s1);
    //    return "获取到的值是：" + s1;
    //}

    /**
     * 秒杀案例 /secKill/0000/0101
     *
     * ab 并发测试：
     * ab -n 1000 -c 100 -p ~/postfile -T application/x-www-form-urlencoded http://192.168.0.102:8080/secKill/0000/0101
     */
    @RequestMapping("/secKill/{uid}/{prodId}")
    public boolean doSecKill(@PathVariable("uid") String uid, @PathVariable("prodId") String prodId) {
        //1.uid 和 prodId 非空判断
        if (uid == null || prodId == null) {
            return false;
        }

        //2.连接redis
        //Jedis jedis = new Jedis(new HostAndPort("192.168.190.12", 6379));
        Jedis jedis=jedisPool.getResource();

        //3.拼接key
        //3.1 库存 key
        String kcKey = "sk:" + prodId + ":qt";

        //3.2 秒杀成功用户 Key
        String userKey = "sk:" + prodId + ":user";


        jedis.watch(kcKey);
        //4.获取库存，如果库存null,秒杀还没开始
        String kcValue = jedis.get(kcKey);
        if (kcValue == null) {
            logger.info("秒杀还未开始，请等候！");
            jedis.set(kcKey,"10");
            jedis.close();
            return false;
        }

        //5.判断用户是否重复秒杀操作
        if (jedis.sismember(userKey, uid)) {
            logger.info("已经秒杀成功了，不能再次秒杀");
            jedis.close();
            return false;
        }

        //6.判断如果商品数量，库存数量小于1，秒杀结束
        if (Integer.parseInt(kcValue) <= 0) {
            logger.info("秒杀已经结束了");
            jedis.close();
            return false;
        }

        //7.秒杀过程
        Transaction multi = jedis.multi();
        multi.decr(kcKey);
        multi.sadd(userKey,uid);
        List<Object> exec = multi.exec();

        if (exec==null || exec.size()==0){
            logger.info("秒杀失败了");
            jedis.close();
            return false;
        }
        //7.1 库存 -1
        //jedis.decr(kcKey);

        //7.2 把秒杀成功的用户添加到清单里面
        //jedis.sadd(userKey, uid);
        jedis.close();
        return true;
    }


    @RequestMapping("/secKillByScript/{uid}/{prodId}")
    public void byScript(@PathVariable("uid") String uid, @PathVariable("prodId") String prodId){
        logger.info("secKillByScript-------------------------");
        String script1=
                " local userid=KEYS[1]; " +
                " local prodid=KEYS[2]; " +
                " local qtkey=\"sk:\"..prodid..\":qt\"; " +
                " local usersKey=\"sk:\"..prodid..\":usr\"; " +
                //" local userExists=redis.call(\"sismember\",usersKey,userid); "+
                //" if tonumber(userExists)==1 then " +
                //"   return 2 ;"+
                //" end "+
                " local num=redis.call(\"get\",qtkey); "+
                " if tonumber(num) <=0 then "+
                " return 0; "+
                " else "+
                " redis.call(\"decr\",qtkey); "+
                " redis.call(\"sadd\",usersKey,userid); "+
                "end "+
                "return 1;";

        Jedis jedis = jedisPool.getResource();
        String s = jedis.scriptLoad(script1);
        Object result = jedis.evalsha(s, 2, uid, prodId);

        String reString = String.valueOf(result);
        if ("0".equals(reString)){
            logger.info("已抢空!");
        }else if ("1".equals(reString)){
            logger.info("抢购成功！");
        }else if ("2".equals(reString)){
            logger.info("该用户已抢过！！");
        }else {
            logger.info("抢购异常！！");
        }
        jedis.close();
    }

    @RequestMapping("/test")
    public String test(){
        return "test";
    }
}
