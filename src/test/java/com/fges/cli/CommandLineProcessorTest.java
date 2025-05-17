package com.fges.cli;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link CommandLineProcessor}.
 * Validates correct parsing of CLI arguments and error handling.
 */
class CommandLineProcessorTest {

    /**
     * Should correctly parse source file and command with default format and category.
     */
    @Test
    void should_parse_valid_arguments_with_default_format() {
        String[] rawArgs = {"-s", "groceries.json", "add", "Eggs", "5"};
        CommandLineArgs parsedArgs = new CommandLineProcessor().parseArgs(rawArgs);

        assertThat(parsedArgs.getFileName()).isEqualTo("groceries.json");
        assertThat(parsedArgs.getFormat()).isEqualTo("json");
        assertThat(parsedArgs.getCategory()).isEqualTo("default");
        assertThat(parsedArgs.getCommand()).isEqualTo("add");
        assertThat(parsedArgs.getRawArgs()).containsExactly("add", "Eggs", "5");
    }

    /**
     * Should correctly parse format and category when provided.
     */
    @Test
    void should_parse_valid_arguments_with_csv_format_and_category() {
        String[] rawArgs = {"-s", "groceries.csv", "--format", "csv", "--category", "dairy", "add", "Milk", "2"};
        CommandLineArgs parsedArgs = new CommandLineProcessor().parseArgs(rawArgs);

        assertThat(parsedArgs.getFileName()).isEqualTo("groceries.csv");
        assertThat(parsedArgs.getFormat()).isEqualTo("csv");
        assertThat(parsedArgs.getCategory()).isEqualTo("dairy");
        assertThat(parsedArgs.getCommand()).isEqualTo("add");
        assertThat(parsedArgs.getRawArgs()).containsExactly("add", "Milk", "2");
    }

    /**
     * Should support short flag for category.
     */
    @Test
    void should_support_category_short_flag() {
        String[] rawArgs = {"-s", "groceries.json", "-c", "veggies", "add", "Carrot", "1"};
        CommandLineArgs parsedArgs = new CommandLineProcessor().parseArgs(rawArgs);

        assertThat(parsedArgs.getCategory()).isEqualTo("veggies");
    }

    /**
     * Should throw error if command is missing.
     */
    @Test
    void should_throw_when_command_is_missing() {
        String[] rawArgs = {"-s", "groceries.json"};
        assertThatThrownBy(() -> new CommandLineProcessor().parseArgs(rawArgs))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Missing command");
    }

    /**
     * Should allow the 'info' command without requiring source.
     */
    @Test
    void should_allow_info_command_without_source() {
        String[] rawArgs = {"info"};
        CommandLineArgs parsedArgs = new CommandLineProcessor().parseArgs(rawArgs);

        assertThat(parsedArgs.getCommand()).isEqualTo("info");
        assertThat(parsedArgs.getFileName()).isNull();
    }

    /**
     * Should allow the 'web' command without requiring source.
     */
    @Test
    void should_allow_web_command_without_source() {
        String[] rawArgs = {"web", "8080"};
        CommandLineArgs parsedArgs = new CommandLineProcessor().parseArgs(rawArgs);

        assertThat(parsedArgs.getCommand()).isEqualTo("web");
        assertThat(parsedArgs.getCommandArgs()).containsExactly("8080");
        assertThat(parsedArgs.getFileName()).isNull();
    }
}