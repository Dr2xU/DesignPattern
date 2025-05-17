package com.fges.executor;

import com.fges.cli.CommandLineArgs;
import com.fges.core.GroceryItem;
import com.fges.dao.GroceryListDAO;
import com.fges.core.GroceryListManager;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link CommandExecutor}.
 * Validates delegation of parsed commands to the appropriate Command implementation.
 */
class CommandExecutorTest {

    /**
     * Mocks a minimal in-memory DAO for test use.
     */
    private GroceryListDAO inMemoryDAO() {
        return new GroceryListDAO() {
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
    }

    /**
     * Should execute info command successfully.
     */
    @Test
    void should_execute_info_command() throws Exception {
        CommandLineArgs args = new CommandLineArgs(
                "dummy.json", "json", "default", List.of("info")
        );

        CommandExecutor executor = new CommandExecutor(args);
        int result = executor.execute();

        assertThat(result).isEqualTo(0);
    }

    /**
     * Should execute add command and persist item.
     */
    @Test
    void should_execute_add_command() throws Exception {
        GroceryListDAO dao = new GroceryListDAO() {
            private List<GroceryItem> items = new ArrayList<>();

            @Override
            public List<GroceryItem> load() {
                return new ArrayList<>(items);
            }

            @Override
            public void save(List<GroceryItem> newItems) {
                this.items = new ArrayList<>(newItems);
            }
        };

        CommandLineArgs args = new CommandLineArgs(
                "ignored.json", "json", "fruits", List.of("add", "Apple", "3")
        );

        CommandExecutor executor = new CommandExecutor(args) {
            @Override
            public int execute() throws Exception {
                GroceryListManager manager = new GroceryListManager(dao);
                var command = CommandFactory.create(args.getCommand(), manager, args.getCategory());
                return command.execute(args.getCommandArgs());
            }
        };

        int result = executor.execute();
        assertThat(result).isEqualTo(0);

        GroceryListManager validation = new GroceryListManager(dao);
        Map<String, List<GroceryItem>> grouped = validation.listItems();

        assertThat(grouped).containsKey("fruits");
        assertThat(grouped.get("fruits"))
                .extracting(GroceryItem::getName, GroceryItem::getQuantity)
                .containsExactly(tuple("Apple", 3));
    }

    /**
     * Should execute list command on valid data source.
     */
    @Test
    void should_execute_list_command() throws Exception {
        GroceryListDAO dao = inMemoryDAO();
        GroceryListManager manager = new GroceryListManager(dao);
        manager.addItem("Juice", 2, "drinks");

        CommandLineArgs args = new CommandLineArgs(
                "file.json", "json", "drinks", List.of("list")
        );

        CommandExecutor executor = new CommandExecutor(args);
        int result = executor.execute();

        assertThat(result).isEqualTo(0);
    }

    /**
     * Should fail gracefully on unknown command.
     */
    @Test
    void should_throw_on_unknown_command() {
        CommandLineArgs args = new CommandLineArgs(
                "data.json", "json", "none", List.of("explode")
        );

        CommandExecutor executor = new CommandExecutor(args);

        assertThatThrownBy(executor::execute)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("unsupported command");
    }

    /**
     * Should throw when CommandLineArgs is null.
     */
    @Test
    void should_throw_when_args_are_null() {
        assertThatThrownBy(() -> new CommandExecutor(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must not be null");
    }
}