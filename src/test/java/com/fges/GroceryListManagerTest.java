// GroceryListManagerTest.java

package com.fges;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GroceryListManagerTest {

    @TempDir
    Path tempDir;

    Path groceryFile;

    @BeforeEach
    void initTestFile() {
        groceryFile = tempDir.resolve("grocery.json");
    }

    @Test
    void should_initialize_empty_list_when_file_does_not_exist() throws IOException {
        var manager = new GroceryListManager(groceryFile.toString());
        assertThat(manager.listItems()).isEmpty();
    }

    @Test
    void should_add_item_and_save_to_file() throws IOException {
        var manager = new GroceryListManager(groceryFile.toString());
        manager.addItem("Milk", 3);
        assertThat(manager.listItems()).containsExactly("Milk: 3");
    }

    @Test
    void should_remove_item_from_list() throws IOException {
        var manager = new GroceryListManager(groceryFile.toString());
        manager.addItem("Milk", 3);
        manager.addItem("Juice", 2);
        manager.removeItem("Milk");

        assertThat(manager.listItems()).containsExactly("Juice: 2");
    }

    @Test
    void should_list_items_correctly() throws IOException {
        var manager = new GroceryListManager(groceryFile.toString());
        manager.addItem("Carrot", 4);
        var list = manager.listItems();
        assertThat(list).containsExactly("Carrot: 4");
    }

    @Test
    void should_persist_items_between_sessions() throws IOException {
        var manager1 = new GroceryListManager(groceryFile.toString());
        manager1.addItem("Banana", 5);

        var manager2 = new GroceryListManager(groceryFile.toString());
        assertThat(manager2.listItems()).containsExactly("Banana: 5");
    }

    @Test
    void should_not_fail_when_removing_non_existent_item() throws IOException {
        var manager = new GroceryListManager(groceryFile.toString());
        manager.addItem("Water", 1);
        manager.removeItem("Nonexistent");
        assertThat(manager.listItems()).containsExactly("Water: 1");
    }

    @Test
    void should_throw_ioexception_when_file_is_invalid() throws IOException {
        Files.writeString(groceryFile, "{ invalid json }");

        assertThatThrownBy(() -> new GroceryListManager(groceryFile.toString()))
            .isInstanceOf(IOException.class);
    }
}