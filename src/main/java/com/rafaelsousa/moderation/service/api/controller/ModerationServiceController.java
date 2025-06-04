package com.rafaelsousa.moderation.service.api.controller;

import com.rafaelsousa.moderation.service.api.model.ModerationInput;
import com.rafaelsousa.moderation.service.api.model.ModerationOutput;
import com.rafaelsousa.moderation.service.common.RandomNumberUtils;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.data.util.Optionals.ifPresentOrElse;

@RestController
@RequestMapping("/api/moderate")
public class ModerationServiceController {
    private static final String[] PROHIBITED_WORDS = {"ódio", "xingamento"};

    @SneakyThrows
    @PostMapping
    public ModerationOutput moderate(@RequestBody ModerationInput input) {
        int randomNumber = RandomNumberUtils.generateBetween(0, 6);

        if (randomNumber % 2 == 0 && randomNumber != 2) {
           Thread.sleep(Duration.ofSeconds(10));
        }

        if (randomNumber == 2) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return moderateComment(input.getText());
    }

    private ModerationOutput moderateComment(String text) {
        AtomicReference<ModerationOutput> result = new AtomicReference<>();

        ifPresentOrElse(
                Arrays.stream(PROHIBITED_WORDS)
                        .filter(text::contains)
                        .findFirst(),
                word -> result.set(ModerationOutput.builder()
                        .approved(false)
                        .reason("Prohibited word found: '" + word + "'")
                        .build()),
                () -> result.set(ModerationOutput.builder()
                        .approved(true)
                        .reason("Text not contains prohibited words")
                        .build())
        );

        return result.get();
    }
}