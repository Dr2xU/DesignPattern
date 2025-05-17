package com.fges.executor;

import java.util.List;

import com.fges.cli.CommandLineArgs;
import com.fges.core.GroceryListManager;
import com.fges.dao.GroceryListDAOFactory;
import com.fges.executor.commands.*;

/**
 * Factory class for creating command instances based on a command name.
 * Supports both file-based and non-file-based commands.
 */
public class CommandFactory {

    /**
     * Creates a command that does NOT require a manager or category (e.g., "info").
     *
     * @param commandName the command to create
     * @return the command instance
     */
    public static Command create(String commandName) {
        if (commandName == null || commandName.isBlank()) {
            throw new IllegalArgumentException("Command name must not be null or empty.");
        }

        return switch (commandName.toLowerCase()) {
            case "info" -> new InfoCommand();
            default -> throw new IllegalArgumentException("Unknown or unsupported command: " + commandName);
        };
    }

    /**
     * Creates a command that requires a manager and possibly a category.
     *
     * @param commandName the command to create (e.g., "add", "list", "remove")
     * @param manager the GroceryListManager to use
     * @param category the category to use for "add"
     * @return the command instance
     */
    public static Command create(String commandName, GroceryListManager manager, String category) {
        if (commandName == null || commandName.isBlank()) {
            throw new IllegalArgumentException("Command name must not be null or empty.");
        }
        if (manager == null) {
            throw new IllegalArgumentException("GroceryListManager must not be null.");
        }

        return switch (commandName.toLowerCase()) {
            case "add" -> {
                if (category == null || category.isBlank()) {
                    throw new IllegalArgumentException("Category must be provided for 'add' command.");
                }
                yield new AddCommand(manager, category);
            }
            case "list" -> new ListCommand(manager);
            case "remove" -> new RemoveCommand(manager);
            default -> throw new IllegalArgumentException("Unknown or unsupported command: " + commandName);
        };
    }

    /**
     * Creates a command using the full parsed command-line arguments.
     * Needed for commands like "web" that need access to raw args.
     *
     * @param args the full CLI argument object
     * @return the command instance
     * @throws Exception if any DAO/manager setup fails
     */
    public static Command create(CommandLineArgs args) throws Exception {
        if (args == null) {
            throw new IllegalArgumentException("CommandLineArgs must not be null.");
        }

        String command = args.getCommand().toLowerCase();

        if (!List.of("info", "web").contains(command.toLowerCase()) &&
            (args.getFileName() == null || args.getFileName().isBlank())) {
            throw new IllegalArgumentException("Missing required option: --source");
        }

        return switch (command) {
            case "web" -> new WebCommand(args);
            case "info" -> new InfoCommand();
            case "add", "list", "remove" -> {
                var dao = GroceryListDAOFactory.create(args.getFormat(), args.getFileName());
                var manager = new GroceryListManager(dao);
                yield create(command, manager, args.getCategory());
            }
            default -> throw new IllegalArgumentException("Unknown or unsupported command: " + command);
        };
    }
}