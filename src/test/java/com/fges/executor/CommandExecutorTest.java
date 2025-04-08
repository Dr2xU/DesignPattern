package com.fges.executor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link CommandExecutor}, covering core operations such as
 * adding, removing, and listing grocery items using various argument scenarios.
 */
class CommandExecutorTest {

    private CommandExecutor executor;
    private CommandExecutor infoExecutor;

    @TempDir
    Path tempDir;

    private Path testFile;
    private final PrintStream standardOut = System.out;
    private ByteArrayOutputStream outputStreamCaptor;

    /**
     * Sets up a temporary test JSON file and initializes the CommandExecutor.
     */
    @BeforeEach
    void setUp() throws IOException {
        testFile = tempDir.resolve("test-executor.json");
        executor = new CommandExecutor(testFile.toString(), "json", "dairy");
        infoExecutor = new CommandExecutor("dummy-file.json", "json", "info");
        
        // Set up output capture for testing display methods
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
     * Verifies that a valid "add" command adds an item successfully.
     */
    @Test
    void should_add_item_with_valid_args() throws IOException {
        int result = executor.execute("add", List.of("add", "Milk", "3"));
        assertThat(result).isEqualTo(0);
        assertThat(executor.getManager().listItems()).contains("Milk: 3");
    }

    /**
     * Verifies that an exception is thrown if arguments are missing for "add".
     */
    @Test
    void should_throw_when_add_args_are_missing() {
        assertThatThrownBy(() -> executor.execute("add", List.of("add", "Milk")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Missing arguments");
    }

    /**
     * Verifies that an exception is thrown when the quantity is not a valid number.
     */
    @Test
    void should_throw_when_quantity_is_invalid() {
        assertThatThrownBy(() -> executor.execute("add", List.of("add", "Milk", "three")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quantity must be a number");
    }

    /**
     * Verifies that an item can be removed with a valid "remove" command.
     */
    @Test
    void should_remove_item_with_valid_args() throws IOException {
        executor.execute("add", List.of("add", "Cheese", "2"));
        int result = executor.execute("remove", List.of("remove", "Cheese"));
        assertThat(result).isEqualTo(0);
        assertThat(executor.getManager().listItems()).doesNotContain("Cheese: 2");
    }

    /**
     * Ensures the system does not fail when trying to remove an item that doesn't exist.
     */
    @Test
    void should_not_fail_when_removing_non_existent_item() throws IOException {
        executor.execute("add", List.of("add", "Apple", "1"));
        int result = executor.execute("remove", List.of("remove", "NonExistentItem"));
        assertThat(result).isEqualTo(0);
        assertThat(executor.getManager().listItems()).contains("Apple: 1");
    }

    /**
     * Verifies that missing arguments for the "remove" command throw an exception.
     */
    @Test
    void should_throw_when_remove_args_are_missing() {
        assertThatThrownBy(() -> executor.execute("remove", List.of("remove")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Missing arguments");
    }

    /**
     * Verifies successful execution of the "list" command.
     */
    @Test
    void should_return_0_when_list_command_is_executed() throws IOException {
        executor.execute("add", List.of("add", "Juice", "2"));
        int result = executor.execute("list", List.of("list"));
        assertThat(result).isEqualTo(0);
    }

    /**
     * Ensures that an unknown command results in an appropriate exception.
     */
    @Test
    void should_throw_when_command_is_unknown() {
        assertThatThrownBy(() -> executor.execute("invalid", List.of("invalid")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown command");
    }

    /**
     * Ensures that items are grouped under the correct category in the output.
     */
    @Test
    void should_group_items_under_specified_category() throws IOException {
        executor.execute("add", List.of("add", "Milk", "3"));
        List<String> output = executor.getManager().listItems();

        assertThat(output).containsExactly(
            "# dairy:",
            "Milk: 3"
        );
    }
    
    /**
     * Verifies successful execution of the "info" command.
     */
    @Test
    void should_display_system_info_when_info_command_executed() throws IOException {
        int result = infoExecutor.execute("info", List.of("info"));
        
        assertThat(result).isEqualTo(0);
        String output = outputStreamCaptor.toString().trim();
        
        String expectedDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        assertThat(output).contains("Today's date: " + expectedDate);
        assertThat(output).contains("Operating System: ");
        assertThat(output).contains("Java version: ");
    }
    
    /**
     * Verifies that other commands fail when using an info-only executor.
     */
    @Test
    void should_fail_when_using_normal_commands_with_info_executor() {
        assertThatThrownBy(() -> infoExecutor.execute("add", List.of("add", "Milk", "3")))
                .isInstanceOf(IllegalStateException.class);
    }
}