package com.example.springbootstudy.listener;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * CommandLineRunner 感知特定阶段： 感知应用就绪 ready
 *
 * @author MCW 2022/8/25
 */
@Component
public class MyCommandLineRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("MyCommandLineRunner...................run");
    }
}
