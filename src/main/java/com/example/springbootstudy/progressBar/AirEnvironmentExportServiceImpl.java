package com.example.springbootstudy.progressBar;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author MCW 2023/8/3
 */
@Component
public class AirEnvironmentExportServiceImpl {

    public String propSummaryData4Export(Map<String, Object> airEnvQualityQueryVo, String key) {

       /* // 获取汇聚数据
        AirEnvQualityResultOverviewVo resultOverviewVo = airEnvironmentQualityStatisticsService.getAirEnvQualityResultOverviewVo(airEnvQualityQueryVo);
        // 数据转换
        resultOverviewVo.setTqRateCompStr(rateHandlerStr(resultOverviewVo.getTqRateComp()));
        resultOverviewVo.setSqRateCompStr(rateHandlerStr(resultOverviewVo.getSqRateComp()));
        // 获取或者数据
        List<AirEnvQualityPropSummaryVo> airEnvQualityPropSummaryVos = airEnvironmentQualityStatisticsService.propSummaryData(airEnvQualityQueryVo);
        AtomicInteger done = new AtomicInteger();
        AsyncUtil.setTotal(key, airEnvQualityPropSummaryVos.size());
        airEnvQualityPropSummaryVos.forEach(vo -> {
            // 数据转换
            vo.setBqReachRateStr(rateHandler(vo.getBqReachRate()));
            vo.setTqReachRateCompStr(rateHandlerStr(vo.getTqReachRateComp()));
            vo.setSqReachRateCompStr(rateHandlerStr(vo.getSqReachRateComp()));
            vo.setBqExceedRateStr(rateHandler(vo.getBqExceedRate()));
            vo.setTqExceedRateCompStr(rateHandlerStr(vo.getTqExceedRateComp()));
            vo.setSqExceedRateCompStr(rateHandlerStr(vo.getSqExceedRateComp()));
            done.getAndIncrement();
            AsyncUtil.setDone(key, done.get());
        });
        // 组织导出数据
        Map<String, Object> map = new HashMap<>();
        map.put("p", resultOverviewVo);
        map.put("w", airEnvQualityPropSummaryVos);
        String url = getExcelUrl(map, "propSum.xlsx", "因子分析汇总");*/
        String url="";
        return url;
    }

}
