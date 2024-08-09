package com.example.springbootstudy.progressBar;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MCW 2023/8/3
 */
public class ExportController {
    @Resource
    AirEnvironmentExportServiceImpl airEnvironmentExportService;

    /**
     * 总表导出
     */
    @PostMapping("/propSummaryData/export")
    public Map<String, Object> propSummaryData4Export(@RequestBody Map<String, Object> airEnvQulityQueryVo) {
        Assert.notNull(airEnvQulityQueryVo, "查询参数不能为空");
        Assert.notNull(airEnvQulityQueryVo.get("StartTime"), "开始时间不能为空");
        Assert.notNull(airEnvQulityQueryVo.get("EndTime"), "结束时间不能为空");
        Assert.isTrue(StringUtils.isNotBlank((String) airEnvQulityQueryVo.get("QueryType")), "查询类型不能为空");
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String key = "propSummaryData:" + format.format(new Date());
        AsyncUtil.submitTask(key, () -> {
            // 获取并组织 excel 数据
            String url;
            try {
                url = airEnvironmentExportService.propSummaryData4Export(airEnvQulityQueryVo, key);
            } catch (Exception e) {
                throw e;
            }
            return url;
        });
        return new HashMap<>() {{
            put("key", key);
        }};
    }


    /**
     * 根据key获取导出接口
     *
     * @param key
     * @return
     */
    @GetMapping("getRedisResult/{key}")
    public /*RestMessage*/ Map getRedisResult(@PathVariable String key) {
        Assert.hasLength(key, "key不能为空");
        // return RestBuilders.successBuilder().data(AsyncUtil.getResult(key)).build();
        return Collections.emptyMap();
    }
}
