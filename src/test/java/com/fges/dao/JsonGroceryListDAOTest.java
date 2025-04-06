package com.fges.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.fges.core.GroceryItem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
     * Verifies that items saved using the DAO can be successfully reloaded.
     *
     * @throws IOException if file operations fail
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
     * Ensures that an empty list is returned if the JSON file does not exist.
     *
     * @throws IOException if load fails unexpectedly
     */
    @Test
    void should_return_empty_list_when_file_does_not_exist() throws IOException {
        assertThat(dao.load()).isEmpty();
    }
}
