// CommandLineArgsTest.java

package com.fges;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CommandLineArgsTest {

    @Test
    void should_return_command_and_arguments() {
        String fileName = "test.json";
        List<String> arguments = List.of("add", "Bread", "2");
        CommandLineArgs args = new CommandLineArgs(fileName, arguments);

        assertThat(args.getFileName()).isEqualTo(fileName);
        assertThat(args.getCommand()).isEqualTo("add");
        assertThat(args.getArguments()).containsExactlyElementsOf(arguments);
    }
}
