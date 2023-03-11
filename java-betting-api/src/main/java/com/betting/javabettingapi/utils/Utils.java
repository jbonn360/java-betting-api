package com.betting.javabettingapi.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Utils {
    public boolean generateRandomBoolean() {
        return new Random().nextBoolean();
    }
}
