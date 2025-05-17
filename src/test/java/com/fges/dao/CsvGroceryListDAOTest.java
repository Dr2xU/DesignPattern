package com.fges.dao;

import com.fges.core.GroceryItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link CsvGroceryListDAO}, which handles
 * reading from and writing to CSV files for grocery items.
 */
class CsvGroceryListDAOTest {

    @TempDir
    Path tempDir;

    private File csvFile;
    private CsvGroceryListDAO dao;

    /**
     * Initializes the test CSV file and DAO before each test.
     */
    @BeforeEach
    void setUp() {
        csvFile = tempDir.resolve("test.csv").toFile();
        dao = new CsvGroceryListDAO(csvFile.getAbsolutePath());
    }

    /**
     * Should save and then reload all items.
     */
    @Test
    void should_save_and_load_items() throws IOException {
        List<GroceryItem> original = List.of(
            new GroceryItem("Bread", 1, "bakery"),
            new GroceryItem("Orange", 3, "fruits")
        );

        dao.save(original);
        List<GroceryItem> loaded = dao.load();

        assertThat(loaded).hasSize(2);
        assertThat(loaded).extracting(GroceryItem::getName).containsExactly("Bread", "Orange");
    }

    /**
     * Should return empty list if CSV file does not exist.
     */
    @Test
    void should_return_empty_list_when_file_does_not_exist() throws IOException {
        assertThat(dao.load()).isEmpty();
    }

    /**
     * Should return empty list if file exists but has no content.
     */
    @Test
    void should_return_empty_list_when_file_is_empty() throws IOException {
        Files.createFile(csvFile.toPath()); // create empty file
        assertThat(dao.load()).isEmpty();
    }

    /**
     * Should skip header row and parse valid entries.
     */
    @Test
    void should_skip_header_and_parse_lines() throws IOException {
        Files.writeString(csvFile.toPath(), "Item,Quantity,Category\nMilk,2,dairy\nBread,1,bakery");

        List<GroceryItem> items = dao.load();
        assertThat(items).hasSize(2);
        assertThat(items).extracting(GroceryItem::getName).contains("Milk", "Bread");
    }

    /**
     * Should create CSV file on save if it doesn't exist.
     */
    @Test
    void should_create_file_if_not_exists_on_save() throws IOException {
        assertThat(csvFile).doesNotExist();

        dao.save(List.of(new GroceryItem("Eggs", 12, "breakfast")));

        assertThat(csvFile).exists().isFile();
        assertThat(dao.load()).hasSize(1);
    }

    /**
     * Should tolerate extra commas in category field.
     */
    @Test
    void should_handle_category_with_comma_field() throws IOException {
        Files.writeString(csvFile.toPath(), "Item,Quantity,Category\nBanana,2,\"fruit,fresh\"");

        List<GroceryItem> items = dao.load();

        assertThat(items).hasSize(1);
        assertThat(items.get(0).getName()).isEqualTo("Banana");
        assertThat(items.get(0).getCategory()).isEqualTo("fruit,fresh");
    }
}