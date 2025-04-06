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
     * Verifies that items saved using the DAO can be successfully reloaded.
     *
     * @throws IOException if file operations fail
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
     * Ensures that an empty list is returned if the CSV file does not exist.
     *
     * @throws IOException if load fails unexpectedly
     */
    @Test
    void should_return_empty_list_when_file_does_not_exist() throws IOException {
        assertThat(dao.load()).isEmpty();
    }
}
