package com.example.mybatisstudy.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Properties;

/**
 * 基于 Mybatis 拦截器实现数据范围权限
 *
 * @author MCW 2023/8/3
 */
//@Component
@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, Cache.class, BoundSql.class})
})
public class MySqlInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // MappedStatement statement = (MappedStatement) invocation.getArgs()[0];
        // Object parameter = invocation.getArgs()[1];
        // BoundSql boundSql = statement.getBoundSql(parameter);
        // String originSql = boundSql.getSql();
        // Object parameterObject = boundSql.getParameterObject();
        // SqlLimit sqlLimit = isLimit(statement);
        // if (sqlLimit == null) {
        //     return invocation.proceed();
        // }
        // RequestAttributes req = RequestContextHolder.getRequestAttributes();
        // if (req == null) {
        //     return invocation.proceed();
        // }
        // // 处理 request
        // HttpServletRequest request = ((ServletRequestAttributes) req).getRequest();
        //
        // // 实际业务处理
        // // Cpip2UserDeptVo userVo = Cpip2UserDeptVoUtil.getUserDeptINfo(request);
        // // String deptId = userVo.getDeptId();
        //
        // // 模拟数据
        // String deptId = "1234";
        // String sql = addTenantCondition(originSql, deptId, sqlLimit.alias());
        // log.info("原SQL：{}，数据权限替换后的SQL:{}", originSql, sql);
        // BoundSql newBoundSql = new BoundSql(statement.getConfiguration(), sql, boundSql.getParameterMappings(), parameterObject);
        // MappedStatement newStatement = copyFormMappedStatement(statement, new BoundSqlSqlSource(newBoundSql));
        // invocation.getArgs()[0] = newStatement;
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Interceptor.super.plugin(target);
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }

    /**
     * 重新拼接SQL
     */
    public String addTenantCondition(String originSql, String deptId, String alias) {
        String field = "dept_id";
        if (StringUtils.isNoneBlank(alias)) {
            field = alias + "." + field;
        }
        StringBuilder sb = new StringBuilder(originSql);
        int index = sb.indexOf("where");
        if (index < 0) {
            sb.append(" where ").append(field).append(" = ").append(deptId);
        } else {
            sb.insert(index + 5, " " + field + " = " + deptId + " and ");
        }
        return sb.toString();
    }

    private MappedStatement copyFormMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.cache(ms.getCache());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    /**
     * 通过注解判断是否需要限制数据
     */
    private SqlLimit isLimit(MappedStatement mappedStatement) {
        SqlLimit sqlLimit = null;
        try {
            String id = mappedStatement.getId();
            String className = id.substring(0, id.lastIndexOf("."));
            String methodName = id.substring(id.lastIndexOf(".") + 1, id.length());
            final Class<?> cls = Class.forName(className);
            final Method[] method = cls.getMethods();
            for (Method me : method) {
                if (me.getName().equals(methodName) && me.isAnnotationPresent(SqlLimit.class)) {
                    sqlLimit = me.getAnnotation(SqlLimit.class);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlLimit;
    }

    public static class BoundSqlSqlSource implements SqlSource {

        private final BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object o) {
            return boundSql;
        }
    }

}
