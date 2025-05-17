package com.fges.executor.commands;

import com.fges.core.GroceryItem;
import com.fges.core.GroceryListManager;
import com.fges.dao.GroceryListDAO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link ListCommand}.
 * Verifies correct list output and behavior on valid and invalid inputs.
 */
class ListCommandTest {

    private GroceryListManager manager;
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    /**
     * Sets up a test manager and captures standard output.
     */
    @BeforeEach
    void setUp() throws Exception {
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
        System.setOut(new PrintStream(output));
    }

    /**
     * Restores original system output.
     */
    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    /**
     * Should list all items grouped by category and return 0.
     */
    @Test
    void should_list_items_grouped_by_category() throws Exception {
        manager.addItem("Apple", 3, "fruits");
        manager.addItem("Milk", 1, "dairy");

        ListCommand command = new ListCommand(manager);
        int result = command.execute(List.of());

        assertThat(result).isEqualTo(0);
        String out = output.toString();

        assertThat(out).contains("# fruits:", "Apple: 3");
        assertThat(out).contains("# dairy:", "Milk: 1");
    }

    /**
     * Should return 0 and still run when extra arguments are passed.
     */
    @Test
    void should_ignore_arguments_and_succeed() {
        ListCommand command = new ListCommand(manager);
        int result = command.execute(List.of("extra", "ignored"));

        assertThat(result).isEqualTo(0);
    }

    /**
     * Should throw when manager is null.
     */
    @Test
    void should_throw_if_manager_is_null() {
        assertThatThrownBy(() -> new ListCommand(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("cannot be null");
    }
}