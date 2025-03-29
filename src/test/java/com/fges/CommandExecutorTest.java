// CommandExecutorTest.java

package com.fges;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class CommandExecutorTest {

    private GroceryListManager manager;
    private CommandExecutor executor;

    @TempDir
    Path tempDir;

    private Path testFile;

    @BeforeEach
    void setUp() throws IOException {
        testFile = tempDir.resolve("test-executor.json");
        manager = new GroceryListManager(testFile.toString());
        executor = new CommandExecutor(manager);
    }

    @Test
    void should_add_item_with_valid_args() throws IOException {
        int result = executor.execute("add", List.of("add", "Milk", "3"));
        assertThat(result).isEqualTo(0);
        assertThat(manager.listItems()).contains("Milk: 3");
    }

    @Test
    void should_throw_when_add_args_are_missing() {
        assertThatThrownBy(() -> executor.execute("add", List.of("add", "Milk")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Missing arguments");
    }

    @Test
    void should_throw_when_quantity_is_invalid() {
        assertThatThrownBy(() -> executor.execute("add", List.of("add", "Milk", "three")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quantity must be a number");
    }

    @Test
    void should_remove_item_with_valid_args() throws IOException {
        executor.execute("add", List.of("add", "Cheese", "2"));
        int result = executor.execute("remove", List.of("remove", "Cheese"));
        assertThat(result).isEqualTo(0);
        assertThat(manager.listItems()).doesNotContain("Cheese: 2");
    }

    @Test
    void should_not_fail_when_removing_non_existent_item() throws IOException {
        executor.execute("add", List.of("add", "Apple", "1"));
        int result = executor.execute("remove", List.of("remove", "NonExistentItem"));
        assertThat(result).isEqualTo(0);
        assertThat(manager.listItems()).contains("Apple: 1");
    }

    @Test
    void should_throw_when_remove_args_are_missing() {
        assertThatThrownBy(() -> executor.execute("remove", List.of("remove")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Missing arguments");
    }

    @Test
    void should_return_0_when_list_command_is_executed() throws IOException {
        executor.execute("add", List.of("add", "Juice", "2"));
        int result = executor.execute("list", List.of("list"));
        assertThat(result).isEqualTo(0);
    }

    @Test
    void should_throw_when_command_is_unknown() {
        assertThatThrownBy(() -> executor.execute("invalid", List.of("invalid")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown command");
    }
}
