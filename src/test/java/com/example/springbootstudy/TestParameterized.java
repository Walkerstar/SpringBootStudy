package com.example.springbootstudy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;
import java.util.stream.Stream;

/**
 * @author MCW 2022/7/24
 */
@DisplayName("参数化测试")
public class TestParameterized {

    @ParameterizedTest
    @ValueSource(ints={1,2,3})
    public void testParameterized(int i){
        System.out.println(i);
    }

    @ParameterizedTest
    @MethodSource("stringProvider")
    void testWithExplicitLocalMethodSource(String argument) {
        assertNotNull(argument);
        System.out.println(argument);
    }

    static Stream<String> stringProvider() {
        return Stream.of("apple", "banana");
    }
}
