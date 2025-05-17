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
 * Unit tests for {@link RemoveCommand}.
 * Verifies item removal logic and input validation.
 */
class RemoveCommandTest {

    private GroceryListManager manager;

    /**
     * Sets up an in-memory GroceryListManager for testing.
     */
    @BeforeEach
    void setUp() throws IOException {
        GroceryListDAO dao = new GroceryListDAO() {
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

        manager = new GroceryListManager(dao);
    }

    /**
     * Should remove item by name successfully and return 0.
     */
    @Test
    void should_remove_item_successfully() throws IOException {
        manager.addItem("Cheese", 2, "dairy");

        Map<String, List<GroceryItem>> before = manager.listItems();
        assertThat(before).containsKey("dairy");
        assertThat(before.get("dairy")).extracting(GroceryItem::getName).contains("Cheese");

        RemoveCommand command = new RemoveCommand(manager);
        int result = command.execute(List.of("Cheese"));

        assertThat(result).isEqualTo(0);
        Map<String, List<GroceryItem>> after = manager.listItems();
        assertThat(after.getOrDefault("dairy", List.of()))
                .extracting(GroceryItem::getName)
                .doesNotContain("Cheese");
    }

    /**
     * Should throw if item name argument is missing.
     */
    @Test
    void should_throw_when_arguments_missing() {
        RemoveCommand command = new RemoveCommand(manager);

        assertThatThrownBy(() -> command.execute(List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Usage");
    }

    /**
     * Should throw if item name is an empty string.
     */
    @Test
    void should_throw_when_item_name_is_empty() {
        RemoveCommand command = new RemoveCommand(manager);

        assertThatThrownBy(() -> command.execute(List.of("")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cannot be empty");
    }

    /**
     * Should throw if manager is null.
     */
    @Test
    void should_throw_when_manager_is_null() {
        assertThatThrownBy(() -> new RemoveCommand(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cannot be null");
    }
}