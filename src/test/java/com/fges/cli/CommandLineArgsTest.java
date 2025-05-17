package com.fges.cli;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link CommandLineArgs}.
 * Ensures proper construction and behavior for argument access methods.
 */
class CommandLineArgsTest {

    @Test
    void should_return_command_and_arguments_correctly() {
        CommandLineArgs args = new CommandLineArgs(
                "test.json", "json", "fruits", List.of("add", "Banana", "3"));

        assertThat(args.getFileName()).isEqualTo("test.json");
        assertThat(args.getFormat()).isEqualTo("json");
        assertThat(args.getCategory()).isEqualTo("fruits");
        assertThat(args.getCommand()).isEqualTo("add");
        assertThat(args.getCommandArgs()).containsExactly("Banana", "3");
        assertThat(args.getRawArgs()).containsExactly("add", "Banana", "3");
    }

    @Test
    void should_handle_empty_argument_list() {
        CommandLineArgs args = new CommandLineArgs("file.csv", "csv", "default", Collections.emptyList());

        assertThat(args.getCommand()).isEmpty();
        assertThat(args.getCommandArgs()).isEmpty();
        assertThat(args.getRawArgs()).isEmpty();
    }

    @Test
    void should_return_command_and_no_args_if_only_command_given() {
        CommandLineArgs args = new CommandLineArgs("data.json", "json", "snacks", List.of("list"));

        assertThat(args.getCommand()).isEqualTo("list");
        assertThat(args.getCommandArgs()).isEmpty();
        assertThat(args.getRawArgs()).containsExactly("list");
    }

    @Test
    void should_preserve_full_argument_list_and_split_command_args() {
        CommandLineArgs args = new CommandLineArgs("items.csv", "csv", "beverages", List.of("remove", "Tea"));

        assertThat(args.getCommand()).isEqualTo("remove");
        assertThat(args.getCommandArgs()).containsExactly("Tea");
        assertThat(args.getRawArgs()).containsExactly("remove", "Tea");
    }
}