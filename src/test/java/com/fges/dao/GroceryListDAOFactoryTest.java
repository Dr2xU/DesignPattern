package com.fges.dao;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link GroceryListDAOFactory}.
 * Verifies correct DAO implementation selection and error handling.
 */
class GroceryListDAOFactoryTest {

    /**
     * Should return JsonGroceryListDAO for "json" format.
     */
    @Test
    void should_return_json_dao_when_format_is_json() {
        GroceryListDAO dao = GroceryListDAOFactory.create("json", "file.json");
        assertThat(dao).isInstanceOf(JsonGroceryListDAO.class);
    }

    /**
     * Should return CsvGroceryListDAO for "csv" format.
     */
    @Test
    void should_return_csv_dao_when_format_is_csv() {
        GroceryListDAO dao = GroceryListDAOFactory.create("csv", "file.csv");
        assertThat(dao).isInstanceOf(CsvGroceryListDAO.class);
    }

    /**
     * Should trim and lowercase input before matching.
     */
    @Test
    void should_ignore_case_and_whitespace() {
        GroceryListDAO dao = GroceryListDAOFactory.create("  JsOn  ", "grocery.json");
        assertThat(dao).isInstanceOf(JsonGroceryListDAO.class);
    }

    /**
     * Should throw when format is unknown.
     */
    @Test
    void should_throw_when_format_is_unknown() {
        assertThatThrownBy(() -> GroceryListDAOFactory.create("xml", "data.xml"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unsupported format");
    }

    /**
     * Should throw when format is null.
     */
    @Test
    void should_throw_when_format_is_null() {
        assertThatThrownBy(() -> GroceryListDAOFactory.create(null, "file.txt"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unsupported format");
    }
}