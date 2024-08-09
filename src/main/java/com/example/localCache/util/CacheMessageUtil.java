package com.example.localCache.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 缓存信息转换工具，以便dump出更友好的缓存信息
 *
 * @author MCW 2024/1/25
 */
public class CacheMessageUtil {

    /**
     * 换行符
     */
    private static final char ENTERSTR = '\n';

    /**
     * map 等于 等号
     */
    private static final char MAP_EQUAL = '=';


    /**
     * 禁用构造函数
     */
    private CacheMessageUtil() {

    }


    /**
     * 缓存信息转换工具，以便dump出更友好的缓存信息<br>
     * 对于List<?>的类型转换
     *
     * @param cacheDatas 缓存数据列表
     * @return 缓存信息
     */
    public static String toString(List<?> cacheDatas) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < cacheDatas.size(); i++) {
            Object object = cacheDatas.get(i);
            builder.append(object);

            if (i != cacheDatas.size() - 1) {
                builder.append(ENTERSTR);
            }
        }
        return builder.toString();
    }

    /**
     * 缓存信息转换工具，以便dump出更友好的缓存信息<br>
     * 对于Map<String, Object>的类型转换
     *
     * @param map 缓存数据
     * @return 缓存信息
     */
    public static String toString(Map<?, ?> map) {
        StringBuilder builder = new StringBuilder();
        int count = map.size();
        for (Iterator<?> i = map.keySet().iterator(); i.hasNext(); ) {
            Object name = i.next();
            count++;

            builder.append(name).append(MAP_EQUAL);
            builder.append(map.get(name));

            if (count != count - 1) {
                builder.append(ENTERSTR);
            }
        }

        return builder.toString();
    }

}
