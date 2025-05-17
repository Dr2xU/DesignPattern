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
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link GroceryListManager}.
 * Covers JSON and CSV persistence behavior, category grouping, item merging, and thread-safe data access.
 */
class GroceryListManagerTest {

    @TempDir
    Path tempDir;

    Path jsonFile;
    Path csvFile;

    /**
     * Initializes test file paths for JSON and CSV files before each test.
     */
    @BeforeEach
    void setup() {
        jsonFile = tempDir.resolve("grocery.json");
        csvFile = tempDir.resolve("grocery.csv");
    }

    /**
     * Should return an empty list when the JSON file doesn't exist.
     */
    @Test
    void should_initialize_empty_list_when_json_file_does_not_exist() throws IOException {
        GroceryListManager manager = new GroceryListManager(new JsonGroceryListDAO(jsonFile.toString()));
        assertThat(manager.listItems()).isEmpty();
    }

    /**
     * Should add an item and store it under the "default" category.
     */
    @Test
    void should_add_item_and_save_to_json_file() throws IOException {
        GroceryListManager manager = new GroceryListManager(new JsonGroceryListDAO(jsonFile.toString()));
        manager.addItem("Milk", 3);

        Map<String, List<GroceryItem>> result = manager.listItems();
        assertThat(result).containsKey("default");
        assertThat(result.get("default"))
            .extracting(GroceryItem::getName, GroceryItem::getQuantity)
            .containsExactly(tuple("Milk", 3));
    }

    /**
     * Should merge quantities when adding duplicate items in the same category.
     */
    @Test
    void should_merge_item_quantity_if_item_already_exists() throws IOException {
        GroceryListManager manager = new GroceryListManager(new JsonGroceryListDAO(jsonFile.toString()));
        manager.addItem("Apple", 2, "fruit");
        manager.addItem("Apple", 3, "fruit");

        Map<String, List<GroceryItem>> result = manager.listItems();
        assertThat(result.get("fruit"))
            .extracting(GroceryItem::getName, GroceryItem::getQuantity)
            .containsExactly(tuple("Apple", 5));
    }

    /**
     * Should group items under their respective categories when listing.
     */
    @Test
    void should_group_items_by_category_in_output() throws IOException {
        GroceryListManager manager = new GroceryListManager(new JsonGroceryListDAO(jsonFile.toString()));
        manager.addItem("Carrot", 4, "vegetables");
        manager.addItem("Chips", 1);

        Map<String, List<GroceryItem>> result = manager.listItems();
        assertThat(result.keySet()).containsExactly("default", "vegetables");
        assertThat(result.get("default")).extracting(GroceryItem::getName).containsExactly("Chips");
        assertThat(result.get("vegetables")).extracting(GroceryItem::getName).containsExactly("Carrot");
    }

    /**
     * Should remove an existing item from the list.
     */
    @Test
    void should_remove_item_from_json_list() throws IOException {
        GroceryListManager manager = new GroceryListManager(new JsonGroceryListDAO(jsonFile.toString()));
        manager.addItem("Juice", 2);
        manager.addItem("Milk", 3);
        manager.removeItem("Milk");

        Map<String, List<GroceryItem>> result = manager.listItems();
        assertThat(result.get("default"))
            .extracting(GroceryItem::getName)
            .containsExactly("Juice");
    }

    /**
     * Should not fail or throw when attempting to remove a non-existent item.
     */
    @Test
    void should_not_fail_when_removing_nonexistent_item() throws IOException {
        GroceryListManager manager = new GroceryListManager(new JsonGroceryListDAO(jsonFile.toString()));
        manager.addItem("Water", 1);
        manager.removeItem("Nonexistent");

        Map<String, List<GroceryItem>> result = manager.listItems();
        assertThat(result.get("default"))
            .extracting(GroceryItem::getName)
            .containsExactly("Water");
    }

    /**
     * Should persist state between manager instances using the same DAO.
     */
    @Test
    void should_persist_json_between_sessions() throws IOException {
        GroceryListDAO dao = new JsonGroceryListDAO(jsonFile.toString());
        GroceryListManager manager1 = new GroceryListManager(dao);
        manager1.addItem("Banana", 5, "fruits");

        GroceryListManager manager2 = new GroceryListManager(dao);
        assertThat(manager2.listItems().get("fruits"))
            .extracting(GroceryItem::getName, GroceryItem::getQuantity)
            .containsExactly(tuple("Banana", 5));
    }

    /**
     * Should throw IOException when trying to load invalid JSON.
     */
    @Test
    void should_throw_ioexception_for_invalid_json_file() throws IOException {
        Files.writeString(jsonFile, "{ invalid json }");

        assertThatThrownBy(() -> new GroceryListManager(new JsonGroceryListDAO(jsonFile.toString())))
            .isInstanceOf(IOException.class);
    }

    /**
     * Should correctly write and reload items from a CSV file.
     */
    @Test
    void should_handle_csv_format() throws IOException {
        GroceryListDAO dao = new CsvGroceryListDAO(csvFile.toString());
        GroceryListManager manager = new GroceryListManager(dao);
        manager.addItem("Eggs", 12, "breakfast");
        manager.addItem("Bread", 1); // default

        var lines = Files.readAllLines(csvFile);
        assertThat(lines).containsExactly(
            "Item,Quantity,Category",
            "Eggs,12,breakfast",
            "Bread,1,default"
        );

        GroceryListManager reloaded = new GroceryListManager(new CsvGroceryListDAO(csvFile.toString()));
        Map<String, List<GroceryItem>> result = reloaded.listItems();

        assertThat(result.get("breakfast")).extracting(GroceryItem::getName).containsExactly("Eggs");
        assertThat(result.get("default")).extracting(GroceryItem::getName).containsExactly("Bread");
    }

    /**
     * Should normalize category names by trimming and lowercasing them.
     */
    @Test
    void should_normalize_category_to_lowercase_and_trim_spaces() throws IOException {
        GroceryListManager manager = new GroceryListManager(new JsonGroceryListDAO(jsonFile.toString()));
        manager.addItem("Apple", 2, "  FRUITS  ");
        manager.addItem("Banana", 3, "fruits");

        List<GroceryItem> fruits = manager.listItems().get("fruits");
        assertThat(fruits).extracting(GroceryItem::getName).containsExactly("Apple", "Banana");
    }
}