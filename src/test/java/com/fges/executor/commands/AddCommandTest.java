package com.fges.executor.commands;

import com.fges.core.GroceryItem;
import com.fges.core.GroceryListManager;
import com.fges.dao.GroceryListDAO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link AddCommand}.
 * Validates item addition logic and input validation.
 */
class AddCommandTest {

    private GroceryListManager manager;

    /**
     * Creates a test GroceryListManager with an in-memory DAO.
     */
    @BeforeEach
    void setup() throws IOException {
        GroceryListDAO inMemoryDAO = new GroceryListDAO() {
            private List<GroceryItem> items = new ArrayList<>();

            @Override
            public List<GroceryItem> load() {
                return new ArrayList<>(items);
            }

            @Override
            public void save(List<GroceryItem> items) {
                this.items = new ArrayList<>(items);
            }
        };

        manager = new GroceryListManager(inMemoryDAO);
    }

    /**
     * Should add item with correct quantity and category.
     */
    @Test
    void should_add_item_correctly() throws IOException {
        AddCommand command = new AddCommand(manager, "fruits");
        int result = command.execute(List.of("Banana", "4"));

        assertThat(result).isEqualTo(0);

        Map<String, List<GroceryItem>> grouped = manager.listItems();
        assertThat(grouped).containsKey("fruits");
        assertThat(grouped.get("fruits"))
                .extracting(GroceryItem::getName, GroceryItem::getQuantity)
                .containsExactly(tuple("Banana", 4));
    }

    /**
     * Should throw if arguments are missing.
     */
    @Test
    void should_throw_when_arguments_are_missing() {
        AddCommand command = new AddCommand(manager, "snacks");

        assertThatThrownBy(() -> command.execute(List.of("add")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Usage");
    }

    /**
     * Should throw if quantity is not a number.
     */
    @Test
    void should_throw_when_quantity_is_not_numeric() {
        AddCommand command = new AddCommand(manager, "drinks");

        assertThatThrownBy(() -> command.execute(List.of("add", "Juice", "two")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quantity must be a number");
    }

    /**
     * Should throw if manager is null.
     */
    @Test
    void should_throw_when_manager_is_null() {
        assertThatThrownBy(() -> new AddCommand(null, "fruit"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Manager");
    }

    /**
     * Should throw if category is null or blank.
     */
    @Test
    void should_throw_when_category_is_invalid() {
        assertThatThrownBy(() -> new AddCommand(manager, " "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Category");

        assertThatThrownBy(() -> new AddCommand(manager, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Category");
    }
}