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
 * Unit tests for {@link JsonGroceryListDAO}, which handles
 * reading from and writing to JSON files for grocery items.
 */
class JsonGroceryListDAOTest {

    @TempDir
    Path tempDir;

    private File jsonFile;
    private JsonGroceryListDAO dao;

    /**
     * Initializes the test JSON file and DAO before each test.
     */
    @BeforeEach
    void setUp() {
        jsonFile = tempDir.resolve("test.json").toFile();
        dao = new JsonGroceryListDAO(jsonFile.getAbsolutePath());
    }

    /**
     * Should save a list of grocery items and load them back successfully.
     */
    @Test
    void should_save_and_load_items() throws IOException {
        List<GroceryItem> original = List.of(
            new GroceryItem("Milk", 2, "dairy"),
            new GroceryItem("Apples", 5, "fruits")
        );

        dao.save(original);

        List<GroceryItem> loaded = dao.load();
        assertThat(loaded).hasSize(2);
        assertThat(loaded).extracting(GroceryItem::getName).containsExactly("Milk", "Apples");
    }

    /**
     * Should return an empty list if file does not exist.
     */
    @Test
    void should_return_empty_list_when_file_does_not_exist() throws IOException {
        assertThat(dao.load()).isEmpty();
    }

    /**
     * Should return an empty list if the file is empty.
     */
    @Test
    void should_return_empty_list_when_file_is_empty() throws IOException {
        Files.createFile(jsonFile.toPath());
        assertThat(dao.load()).isEmpty();
    }

    /**
     * Should throw exception for invalid JSON content.
     */
    @Test
    void should_throw_when_json_is_invalid() throws IOException {
        Files.writeString(jsonFile.toPath(), "{ invalid json");

        assertThatThrownBy(() -> dao.load())
            .isInstanceOf(IOException.class)
            .hasMessageContaining("Cannot deserialize");
    }

    /**
     * Should create a new file if one does not exist when saving.
     */
    @Test
    void should_create_file_if_not_exists_on_save() throws IOException {
        List<GroceryItem> items = List.of(new GroceryItem("Eggs", 6, "breakfast"));
        assertThat(jsonFile.exists()).isFalse();

        dao.save(items);

        assertThat(jsonFile).exists().isFile();
        assertThat(dao.load()).hasSize(1);
    }
}