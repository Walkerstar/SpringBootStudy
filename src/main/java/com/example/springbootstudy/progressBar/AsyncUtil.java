package com.example.springbootstudy.progressBar;

import cn.hutool.core.collection.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;


/**
 * @author MCW 2023/8/3
 */
@Component
public class AsyncUtil implements ApplicationContextAware {

    static Logger LOG = LoggerFactory.getLogger(AsyncUtil.class);
    public static ExecutorService executor = Executors.newFixedThreadPool(40);
    public static ScheduledExecutorService ex = Executors.newScheduledThreadPool(1);
    static List<String> keys = new ArrayList<>();
    static boolean scheduled = false;
    private static OSSBaseService ossBaseService;

     static boolean scheduleIsStart= false;

     private static ApplicationContext springUtils;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ossBaseService = applicationContext.getBean(OSSBaseService.class);
        springUtils=applicationContext;
    }

    public static RedisTemplate<String, RedisAsyResultData> getRedisTemplate() {
        //return SpringUtils.getBean("redisTemplate", RedisTemplate.class);
        RedisTemplate redisTemplate = springUtils.getBean("redisTemplate", RedisTemplate.class);
        return redisTemplate;
    }

    static void updateKeyLiveTime() {
        if (!scheduleIsStart) {
            // 更新redis中缓存的过期时间
            ex.scheduleAtFixedRate(() -> {
                try {
                    LOG.info("----- update AsyncResult keys length:{} -----", keys.size());
                    if (CollectionUtil.isNotEmpty(keys)) {
                        List<RedisAsyResultData> multiGet = getRedisTemplate().opsForValue().multiGet(keys);
                        for (RedisAsyResultData result : multiGet) {
                            if (result != null) {
                                String key = result.getRedisKey();
                                getRedisTemplate().expire(key, 5, TimeUnit.MINUTES);
                            }
                        }
                    }
                } catch (Exception e) {
                    scheduleIsStart = false;
                    LOG.error(e.getMessage(), e);
                }
            }, 1, 3, TimeUnit.MINUTES);
            scheduleIsStart = true;
        }
    }

    public static RedisAsyResultData submitExportTask(String key, Supplier supplier) {
        RedisAsyResultData rs = new RedisAsyResultData();
        rs.setSuccess(false);
        rs.setRedisKey(key);
        rs.setDone(0);
        rs.setTotal(100);
        setToRedis(rs, key);
        if (!keys.contains(key)) {
            keys.add(key);
        }
        String finalKey = key;
        executor.submit(() -> {
            String msg = null;
            try {
                Object o = supplier.get();
                rs.setData(o);
                rs.setFlag(true);
            } catch (Exception e) {
                rs.setFlag(false);
                msg = e.getMessage();
                LOG.error(e.getMessage(), e);
            }
            rs.setSuccess(true);
            rs.setDone(rs.getTotal());
            if (null != msg) {
                rs.setError(msg);
            }
            keys.remove(finalKey);
            setToRedis(rs, finalKey);
        });
        updateKeyLiveTime();
        return rs;
    }

    /**
     * 设置进度
     *
     * @param key
     * @param done
     * @return
     */
    public static void setDone(String key, Integer done) {
        RedisAsyResultData result = getResult(key);
        Optional.ofNullable(result).ifPresent(re -> {
            re.setDone(done);
            saveResult(key, result);
        });
    }

    /**
     * 设置总数
     *
     * @param key
     * @param total
     * @return
     */
    public static void setTotal(String key, Integer total) {
        RedisAsyResultData result = getResult(key);
        Optional.ofNullable(result).ifPresent(re -> {
            re.setTotal(total);
            saveResult(key, result);
        });
    }

    public static RedisAsyResultData submitTask(String key, Supplier supplier) {
        AtomicReference<RedisAsyResultData> rs = new AtomicReference<>(new RedisAsyResultData());
        rs.get().setSuccess(false);
        rs.get().setRedisKey(key);
        rs.get().setDone(0);
        rs.get().setTotal(100);
        setToRedis(rs.get(), key);
        if (!keys.contains(key)) {
            keys.add(key);
        }
        String finalKey = key;
        executor.submit(() -> {
            String msg = null;
            try {
                Object o = supplier.get();
                RedisAsyResultData result = getResult(key);
                if (null != result) {
                    rs.set(result);
                }
                rs.get().setData(o);
                rs.get().setFlag(true);
            } catch (Exception e) {
                rs.get().setFlag(false);
                msg = e.getMessage();
                LOG.error(e.getMessage(), e);
            }
            rs.get().setSuccess(true);

            rs.get().setDone(rs.get().getTotal());
            if (null != msg) {
                rs.get().setError(msg);
            }
            keys.remove(finalKey);
            setToRedis(rs.get(), finalKey);
        });
        updateKeyLiveTime();
        return rs.get();
    }

    private static void setToRedis(RedisAsyResultData result, String redisKey) {
        getRedisTemplate().opsForValue().set(redisKey, result, 5, TimeUnit.MINUTES);
    }

    public static RedisAsyResultData getResult(String key) {
        RedisAsyResultData excelResult = getRedisTemplate().opsForValue().get(key);
        if (null != excelResult) {
            return excelResult;
        }
        return null;
    }

    public static void saveResult(String key, RedisAsyResultData result) {
        setToRedis(result, key);
    }

    public static byte[] FileToByte(String filePath) throws Exception {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            fis = new FileInputStream(filePath);
            bis = new BufferedInputStream(fis);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int c = bis.read();
            while (c != -1) {
                // 数据存储到ByteArrayOutputStream中
                baos.write(c);
                c = bis.read();
            }
            fis.close();
            bis.close();
            // 转换成二进制
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            } finally {
                try {
                    if (bis != null) {
                        bis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
    }
}
