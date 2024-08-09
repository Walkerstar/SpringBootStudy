package com.example.mybatisstudy.handler;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.example.mybatisstudy.entity.Encrypt;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * BaseTypeHandler 是 MyBatis 框架中的一个类型处理器，用于处理数据库字段与 Java 对象之间的类型转换。
 * 在配置文件中通过该参数，批量引入自定义处理器，  mybatis.type-handlers-package: com.example.mybatisstudy.handle
 *
 * @author mcw 2022/2/10 17:24
 */
@Component
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(Encrypt.class)
public class EncryptTypeHandler extends BaseTypeHandler<Encrypt> {

    private static final byte[] KEYS = "12345678abcdefgh".getBytes(StandardCharsets.UTF_8);

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Encrypt parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null || parameter.getValue() == null) {
            preparedStatement.setString(i, null);
            return;
        }
        AES aes = SecureUtil.aes(KEYS);
        String encrypt = aes.encryptHex(parameter.getValue());
        preparedStatement.setString(i, encrypt);
    }

    @Override
    public Encrypt getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return decrypt(resultSet.getString(s));
    }

    @Override
    public Encrypt getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return decrypt(resultSet.getString(i));
    }

    @Override
    public Encrypt getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return decrypt(callableStatement.getString(i));
    }

    public Encrypt decrypt(String value) {
        if (null == value) {
            return null;
        }
        return new Encrypt(SecureUtil.aes(KEYS).decryptStr(value));
    }
}
