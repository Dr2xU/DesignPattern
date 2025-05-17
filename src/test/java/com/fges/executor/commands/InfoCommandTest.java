package com.fges.executor.commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link InfoCommand}.
 * Verifies output content and successful execution status.
 */
class InfoCommandTest {

    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    /**
     * Captures standard output before each test.
     */
    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(output));
    }

    /**
     * Restores standard output after each test.
     */
    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    /**
     * Should print current system info and return 0.
     */
    @Test
    void should_display_system_info_and_return_0() {
        InfoCommand command = new InfoCommand();
        int result = command.execute(List.of());

        assertThat(result).isEqualTo(0);
        String outputText = output.toString();

        assertThat(outputText).contains("Today's date:");
        assertThat(outputText).contains("Operating System:");
        assertThat(outputText).contains("Java version:");
    }

    /**
     * Should ignore extra arguments and still succeed.
     */
    @Test
    void should_ignore_arguments_and_run_successfully() {
        InfoCommand command = new InfoCommand();
        int result = command.execute(List.of("extra", "values", "ignored"));

        assertThat(result).isEqualTo(0);
        assertThat(output.toString()).contains("Java version:");
    }
}