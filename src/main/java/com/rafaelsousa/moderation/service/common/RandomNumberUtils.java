package com.rafaelsousa.moderation.service.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RandomNumberUtils {
    public static int generateBetween(int start, int end) {
        return ThreadLocalRandom.current().nextInt(start, end + 1);
    }
}