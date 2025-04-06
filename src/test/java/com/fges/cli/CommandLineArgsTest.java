package com.fges.cli;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link CommandLineArgs}.
 * Ensures correct storage and retrieval of parsed command-line values.
 */
class CommandLineArgsTest {

    /**
     * Tests that all command-line argument values (file name, format, category, and command/arguments)
     * are correctly stored and retrievable.
     */
    @Test
    void should_return_command_arguments_format_and_category() {
        String fileName = "test.json";
        String format = "json";
        String category = "fruits";
        List<String> arguments = List.of("add", "Banana", "3");

        CommandLineArgs args = new CommandLineArgs(fileName, format, category, arguments);

        assertThat(args.getFileName()).isEqualTo(fileName);
        assertThat(args.getFormat()).isEqualTo("json");
        assertThat(args.getCategory()).isEqualTo("fruits");
        assertThat(args.getCommand()).isEqualTo("add");
        assertThat(args.getArguments()).containsExactlyElementsOf(arguments);
    }
}
