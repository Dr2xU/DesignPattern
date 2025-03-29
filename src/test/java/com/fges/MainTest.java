// MainTest.java

package com.fges;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

class MainTest {

    private static final String TEST_FILE = "test-grocery.json";

    @BeforeEach
    void deleteTestFileIfExists() throws IOException {
        Files.deleteIfExists(new File(TEST_FILE).toPath());
    }

    @Test
    void should_return_1_when_no_arguments_given() throws IOException {
        int result = Main.exec(new String[]{});
        assertThat(result).isEqualTo(1);
    }

    @Test
    void should_return_1_when_source_option_missing() throws IOException {
        int result = Main.exec(new String[]{"add", "apple", "2"});
        assertThat(result).isEqualTo(1);
    }

    @Test
    void should_return_1_when_command_missing() throws IOException {
        int result = Main.exec(new String[]{"-s", TEST_FILE});
        assertThat(result).isEqualTo(1);
    }

    @Test
    void should_return_1_when_quantity_invalid() throws IOException {
        int result = Main.exec(new String[]{"-s", TEST_FILE, "add", "apple", "two"});
        assertThat(result).isEqualTo(1);
    }

    @Test
    void should_return_1_when_command_unknown() throws IOException {
        int result = Main.exec(new String[]{"-s", TEST_FILE, "unknown"});
        assertThat(result).isEqualTo(1);
    }

    @Test
    void should_return_0_when_list_command_valid() throws IOException {
        int result = Main.exec(new String[]{"-s", TEST_FILE, "list"});
        assertThat(result).isEqualTo(0);
    }

    @Test
    void should_return_0_when_add_command_valid() throws IOException {
        int result = Main.exec(new String[]{"-s", TEST_FILE, "add", "Eggs", "6"});
        assertThat(result).isEqualTo(0);
    }
}