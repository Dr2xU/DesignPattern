package com.fges.cli;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link CommandLineProcessor}.
 * Validates parsing of CLI arguments and error handling for incorrect inputs.
 */
class CommandLineProcessorTest {

    /**
     * Verifies parsing of minimal valid arguments using default format and category.
     */
    @Test
    void should_parse_valid_arguments_with_default_format() throws Exception {
        String[] rawArgs = {"-s", "groceries.json", "add", "Eggs", "5"};
        CommandLineArgs parsedArgs = new CommandLineProcessor().parseArgs(rawArgs);

        assertThat(parsedArgs.getFileName()).isEqualTo("groceries.json");
        assertThat(parsedArgs.getFormat()).isEqualTo("json");
        assertThat(parsedArgs.getCategory()).isEqualTo("default");
        assertThat(parsedArgs.getCommand()).isEqualTo("add");
        assertThat(parsedArgs.getArguments()).containsExactly("add", "Eggs", "5");
    }

    /**
     * Verifies parsing of full arguments including CSV format and explicit category.
     */
    @Test
    void should_parse_valid_arguments_with_csv_format_and_category() throws Exception {
        String[] rawArgs = {"-s", "groceries.csv", "--format", "csv", "--category", "dairy", "add", "Milk", "2"};
        CommandLineArgs parsedArgs = new CommandLineProcessor().parseArgs(rawArgs);

        assertThat(parsedArgs.getFileName()).isEqualTo("groceries.csv");
        assertThat(parsedArgs.getFormat()).isEqualTo("csv");
        assertThat(parsedArgs.getCategory()).isEqualTo("dairy");
        assertThat(parsedArgs.getCommand()).isEqualTo("add");
        assertThat(parsedArgs.getArguments()).containsExactly("add", "Milk", "2");
    }

    /**
     * Verifies that the short flag (-c) for category is supported.
     */
    @Test
    void should_support_category_short_flag() throws Exception {
        String[] rawArgs = {"-s", "groceries.json", "-c", "veggies", "add", "Carrot", "1"};
        CommandLineArgs parsedArgs = new CommandLineProcessor().parseArgs(rawArgs);

        assertThat(parsedArgs.getCategory()).isEqualTo("veggies");
    }

    /**
     * Verifies that missing required -s (source) option throws a ParseException.
     */
    @Test
    void should_throw_when_source_option_is_missing() {
        String[] rawArgs = {"add", "Milk", "2"};
        assertThatThrownBy(() -> new CommandLineProcessor().parseArgs(rawArgs))
                .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * Verifies that missing positional command throws an IllegalArgumentException.
     */
    @Test
    void should_throw_when_command_is_missing() {
        String[] rawArgs = {"-s", "groceries.json"};
        assertThatThrownBy(() -> new CommandLineProcessor().parseArgs(rawArgs))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Missing Command");
    }
    
    /**
     * Verifies parsing of the "info" command which doesn't require a source file.
     */
    @Test
    void should_parse_info_command_with_no_source() throws Exception {
        String[] rawArgs = {"info"};
        CommandLineArgs parsedArgs = new CommandLineProcessor().parseArgs(rawArgs);

        assertThat(parsedArgs.getFileName()).isEqualTo("dummy-file.json");
        assertThat(parsedArgs.getFormat()).isEqualTo("json");
        assertThat(parsedArgs.getCategory()).isEqualTo("info");
        assertThat(parsedArgs.getCommand()).isEqualTo("info");
        assertThat(parsedArgs.getArguments()).containsExactly("info");
    }
    
    /**
     * Verifies that the "info" command ignores provided options.
     */
    @Test
    void should_ignore_options_when_info_command_used() throws Exception {
        String[] rawArgs = {"-s", "groceries.json", "-f", "csv", "-c", "dairy", "info"};
        CommandLineArgs parsedArgs = new CommandLineProcessor().parseArgs(rawArgs);

        assertThat(parsedArgs.getCommand()).isEqualTo("info");
        assertThat(parsedArgs.getCategory()).isEqualTo("info");
        assertThat(parsedArgs.getFileName()).isEqualTo("dummy-file.json");
        assertThat(parsedArgs.getFormat()).isEqualTo("json");
    }
}