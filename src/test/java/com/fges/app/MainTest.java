package com.fges.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for the {@link Main} application class.
 * These tests simulate full CLI invocations by invoking Main.exec with various arguments.
 */
class MainTest {

    private static final String TEST_FILE = "test-grocery.json";

    @TempDir
    Path tempDir;

    private Path tempJson;
    private Path tempCsv;
    private final PrintStream standardOut = System.out;
    private ByteArrayOutputStream outputStreamCaptor;

    /**
     * Sets up temporary JSON and CSV file paths before each test and ensures they are clean.
     */
    @BeforeEach
    void setup() throws IOException {
        tempJson = tempDir.resolve(TEST_FILE);
        tempCsv = tempDir.resolve("test-grocery.csv");
        Files.deleteIfExists(tempJson);
        Files.deleteIfExists(tempCsv);
        
        outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
    }
    
    /**
     * Clean up after tests
     */
    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        System.setOut(standardOut);
    }

    /**
     * Should return 1 when no arguments are provided.
     */
    @Test
    void should_return_1_when_no_arguments_given() throws IOException {
        int result = Main.exec(new String[]{});
        assertThat(result).isEqualTo(1);
    }

    /**
     * Should return 1 when the required source option is missing.
     */
    @Test
    void should_return_1_when_source_option_missing() throws IOException {
        int result = Main.exec(new String[]{"add", "apple", "2"});
        assertThat(result).isEqualTo(1);
    }

    /**
     * Should return 1 when a command is not provided even though the source file is specified.
     */
    @Test
    void should_return_1_when_command_missing() throws IOException {
        int result = Main.exec(new String[]{"-s", tempJson.toString()});
        assertThat(result).isEqualTo(1);
    }

    /**
     * Should return 1 when a non-integer quantity is passed for the 'add' command.
     */
    @Test
    void should_return_1_when_quantity_invalid() throws IOException {
        int result = Main.exec(new String[]{"-s", tempJson.toString(), "add", "apple", "two"});
        assertThat(result).isEqualTo(1);
    }

    /**
     * Should return 1 when an unknown command is used.
     */
    @Test
    void should_return_1_when_command_unknown() throws IOException {
        int result = Main.exec(new String[]{"-s", tempJson.toString(), "unknown"});
        assertThat(result).isEqualTo(1);
    }

    /**
     * Should return 0 when 'list' command is executed on a valid JSON source file.
     */
    @Test
    void should_return_0_when_list_command_valid() throws IOException {
        int result = Main.exec(new String[]{"-s", tempJson.toString(), "list"});
        assertThat(result).isEqualTo(0);
    }

    /**
     * Should return 0 when 'add' command is executed correctly with valid arguments.
     */
    @Test
    void should_return_0_when_add_command_valid() throws IOException {
        int result = Main.exec(new String[]{"-s", tempJson.toString(), "add", "Eggs", "6"});
        assertThat(result).isEqualTo(0);
    }

    /**
     * Should support CSV format and properly add/list items using it.
     */
    @Test
    void should_return_0_when_csv_file_used() throws IOException {
        int result = Main.exec(new String[]{"-s", tempCsv.toString(), "-f", "csv", "add", "Milk", "1"});
        assertThat(result).isEqualTo(0);

        int listResult = Main.exec(new String[]{"-s", tempCsv.toString(), "-f", "csv", "list"});
        assertThat(listResult).isEqualTo(0);

        var lines = Files.readAllLines(tempCsv);
        assertThat(lines.get(0)).isEqualTo("Item,Quantity,Category");
        assertThat(lines).anyMatch(line -> line.startsWith("Milk,1"));
    }
    
    /**
     * Should return 0 when 'info' command is executed and display system information.
     */
    @Test
    void should_return_0_when_info_command_executed() throws IOException {
        int result = Main.exec(new String[]{"info"});
        
        assertThat(result).isEqualTo(0);
        String output = outputStreamCaptor.toString().trim();
        
        assertThat(output).contains("Today's date:");
        assertThat(output).contains("Operating System:");
        assertThat(output).contains("Java version:");
    }
    
    /**
     * Should return 0 when 'info' command is executed even with source option.
     */
    @Test
    void should_return_0_when_info_command_executed_with_source() throws IOException {
        int result = Main.exec(new String[]{"-s", tempJson.toString(), "info"});
        
        assertThat(result).isEqualTo(0);
        String output = outputStreamCaptor.toString().trim();
        
        assertThat(output).contains("Today's date:");
        assertThat(output).contains("Operating System:");
        assertThat(output).contains("Java version:");
    }

    /**
     * Should return 0 and launch the web server on default port.
     * The test interrupts the thread to simulate graceful shutdown.
     */
    @Test
    void should_return_0_when_web_command_executed() throws Exception {
        Thread webThread = new Thread(() -> {
            int result = Main.exec(new String[]{
                    "-s", tempJson.toString(), "-f", "json", "web", "12345"
            });

            assertThat(result).isEqualTo(0);
        });

        webThread.start();
        Thread.sleep(500);
        webThread.interrupt();
    }
}