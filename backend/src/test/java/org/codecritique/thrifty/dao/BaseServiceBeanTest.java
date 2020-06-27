package org.codecritique.thrifty.dao;

import java.util.Random;
import java.util.function.Supplier;

public abstract class BaseServiceBeanTest {
    protected Supplier<String> rNameGen = () -> {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append((char) (65 + random.nextInt(24)));
        }
        return sb.toString();
    };
}
