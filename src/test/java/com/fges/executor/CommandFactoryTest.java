package com.fges.executor;

import com.fges.core.GroceryItem;
import com.fges.core.GroceryListManager;
import com.fges.dao.GroceryListDAO;
import com.fges.executor.commands.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link CommandFactory}.
 * Verifies instantiation of command types and input validations.
 */
class CommandFactoryTest {

    private GroceryListManager manager;

    /**
     * Sets up a fake in-memory DAO and manager before each test.
     */
    @BeforeEach
    void setup() throws IOException {
        GroceryListDAO fakeDAO = new GroceryListDAO() {
            private List<GroceryItem> items = new java.util.ArrayList<>();

            @Override
            public List<GroceryItem> load() {
                return items;
            }

            @Override
            public void save(List<GroceryItem> items) {
                this.items = new java.util.ArrayList<>(items);
            }
        };

        manager = new GroceryListManager(fakeDAO);
    }

    /**
     * Should create an InfoCommand.
     */
    @Test
    void should_create_info_command() {
        Command command = CommandFactory.create("info");
        assertThat(command).isInstanceOf(InfoCommand.class);
    }

    /**
     * Should create AddCommand when given valid manager and category.
     */
    @Test
    void should_create_add_command() {
        Command command = CommandFactory.create("add", manager, "dairy");
        assertThat(command).isInstanceOf(AddCommand.class);
    }

    /**
     * Should create ListCommand.
     */
    @Test
    void should_create_list_command() {
        Command command = CommandFactory.create("list", manager, "ignored");
        assertThat(command).isInstanceOf(ListCommand.class);
    }

    /**
     * Should create RemoveCommand.
     */
    @Test
    void should_create_remove_command() {
        Command command = CommandFactory.create("remove", manager, "ignored");
        assertThat(command).isInstanceOf(RemoveCommand.class);
    }

    /**
     * Should throw error if command name is null when creating manager-based command.
     */
    @Test
    void should_throw_when_command_name_is_null_for_manager_command() {
        assertThatThrownBy(() -> CommandFactory.create(null, manager, "fruit"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must not be null or empty");
    }

    /**
     * Should throw error if manager is null.
     */
    @Test
    void should_throw_when_manager_is_null() {
        assertThatThrownBy(() -> CommandFactory.create("add", null, "fruit"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must not be null");
    }

    /**
     * Should throw if category is blank for AddCommand.
     */
    @Test
    void should_throw_when_add_command_has_blank_category() {
        assertThatThrownBy(() -> CommandFactory.create("add", manager, " "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Category must be provided");
    }

    /**
     * Should throw for unsupported commands.
     */
    @Test
    void should_throw_when_command_is_unknown() {
        assertThatThrownBy(() -> CommandFactory.create("explode", manager, "cat"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("unsupported command");
    }

    /**
     * Should throw if top-level info command is blank.
     */
    @Test
    void should_throw_when_info_command_is_blank() {
        assertThatThrownBy(() -> CommandFactory.create(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must not be null or empty");
    }
}