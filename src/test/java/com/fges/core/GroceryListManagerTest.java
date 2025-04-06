package com.fges.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.fges.dao.CsvGroceryListDAO;
import com.fges.dao.GroceryListDAO;
import com.fges.dao.JsonGroceryListDAO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link GroceryListManager}.
 * Covers JSON and CSV persistence behavior, category grouping, and basic list operations.
 */
class GroceryListManagerTest {

    @TempDir
    Path tempDir;

    Path jsonFile;
    Path csvFile;

    /**
     * Initializes temporary JSON and CSV file paths before each test.
     */
    @BeforeEach
    void setup() {
        jsonFile = tempDir.resolve("grocery.json");
        csvFile = tempDir.resolve("grocery.csv");
    }

    /**
     * Verifies the grocery list is empty when a JSON file doesn't exist.
     */
    @Test
    void should_initialize_empty_list_when_json_file_does_not_exist() throws IOException {
        GroceryListDAO dao = new JsonGroceryListDAO(jsonFile.toString());
        GroceryListManager manager = new GroceryListManager(dao);

        assertThat(manager.listItems()).isEmpty();
    }

    /**
     * Verifies that an item is added and persisted correctly to a JSON file.
     */
    @Test
    void should_add_item_and_save_to_json_file() throws IOException {
        GroceryListDAO dao = new JsonGroceryListDAO(jsonFile.toString());
        GroceryListManager manager = new GroceryListManager(dao);
        manager.addItem("Milk", 3);

        assertThat(manager.listItems()).containsExactly("# default:", "Milk: 3");
    }

    /**
     * Verifies that an item is removed from a JSON-based list.
     */
    @Test
    void should_remove_item_from_json_list() throws IOException {
        GroceryListManager manager = new GroceryListManager(new JsonGroceryListDAO(jsonFile.toString()));
        manager.addItem("Milk", 3);
        manager.addItem("Juice", 2);
        manager.removeItem("Milk");

        assertThat(manager.listItems()).containsExactly("# default:", "Juice: 2");
    }

    /**
     * Verifies correct grouping of items under their respective categories.
     */
    @Test
    void should_group_items_by_category_in_output() throws IOException {
        GroceryListManager manager = new GroceryListManager(new JsonGroceryListDAO(jsonFile.toString()));
        manager.addItem("Carrot", 4, "vegetables");
        manager.addItem("Chips", 1); // default category

        assertThat(manager.listItems()).containsExactly(
                "# default:",
                "Chips: 1",
                "",
                "# vegetables:",
                "Carrot: 4"
        );
    }

    /**
     * Verifies data persistence across multiple instances for JSON format.
     */
    @Test
    void should_persist_json_between_sessions() throws IOException {
        GroceryListDAO dao = new JsonGroceryListDAO(jsonFile.toString());
        GroceryListManager manager1 = new GroceryListManager(dao);
        manager1.addItem("Banana", 5, "fruits");

        GroceryListManager manager2 = new GroceryListManager(dao);
        assertThat(manager2.listItems()).containsExactly("# fruits:", "Banana: 5");
    }

    /**
     * Ensures no exception is thrown when removing a non-existent item.
     */
    @Test
    void should_not_fail_when_removing_nonexistent_item() throws IOException {
        GroceryListManager manager = new GroceryListManager(new JsonGroceryListDAO(jsonFile.toString()));
        manager.addItem("Water", 1);
        manager.removeItem("Nonexistent");

        assertThat(manager.listItems()).containsExactly("# default:", "Water: 1");
    }

    /**
     * Verifies proper exception is thrown for invalid JSON format.
     */
    @Test
    void should_throw_ioexception_for_invalid_json_file() throws IOException {
        Files.writeString(jsonFile, "{ invalid json }");

        assertThatThrownBy(() -> new GroceryListManager(new JsonGroceryListDAO(jsonFile.toString())))
                .isInstanceOf(IOException.class);
    }

    /**
     * Verifies full CSV-based persistence with correct formatting and reloading.
     */
    @Test
    void should_handle_csv_format() throws IOException {
        GroceryListDAO dao = new CsvGroceryListDAO(csvFile.toString());
        GroceryListManager manager = new GroceryListManager(dao);
        manager.addItem("Eggs", 12, "breakfast");
        manager.addItem("Bread", 1);

        var lines = Files.readAllLines(csvFile);
        assertThat(lines).containsExactly(
                "Item,Quantity,Category",
                "Eggs,12,breakfast",
                "Bread,1,"
        );

        GroceryListManager reloaded = new GroceryListManager(new CsvGroceryListDAO(csvFile.toString()));
        assertThat(reloaded.listItems()).containsExactly(
                "# breakfast:",
                "Eggs: 12",
                "",
                "# default:",
                "Bread: 1"
        );
    }
}
