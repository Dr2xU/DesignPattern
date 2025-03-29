package com.fges;

import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CommandLineProcessorTest {

    @Test
    void should_parse_valid_arguments() throws Exception {
        String[] rawArgs = {"-s", "groceries.json", "add", "Eggs", "5"};
        CommandLineArgs parsedArgs = new CommandLineProcessor().parseArgs(rawArgs);

        assertThat(parsedArgs.getFileName()).isEqualTo("groceries.json");
        assertThat(parsedArgs.getCommand()).isEqualTo("add");
        assertThat(parsedArgs.getArguments()).containsExactly("add", "Eggs", "5");
    }

    @Test
    void should_throw_when_source_option_is_missing() {
        String[] rawArgs = {"add", "Milk", "2"};

        assertThatThrownBy(() -> new CommandLineProcessor().parseArgs(rawArgs))
                .isInstanceOf(ParseException.class);
    }

    @Test
    void should_throw_when_command_is_missing() {
        String[] rawArgs = {"-s", "groceries.json"};

        assertThatThrownBy(() -> new CommandLineProcessor().parseArgs(rawArgs))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Missing Command");
    }
}
