package com.fges.executor;

import java.io.IOException;
import java.util.List;

import com.fges.core.GroceryListManager;
import com.fges.dao.CsvGroceryListDAO;
import com.fges.dao.GroceryListDAO;
import com.fges.dao.JsonGroceryListDAO;

/**
 * Handles the execution of grocery list commands such as add, list, and remove.
 * Uses a specific data access strategy (JSON or CSV) and supports categorization.
 */
public class CommandExecutor {

    /** The grocery list manager responsible for item operations */
    private final GroceryListManager manager;

    /** The category to associate with added items */
    private final String category;

    /**
     * Constructs a CommandExecutor with the given file format, source, and category.
     *
     * @param fileName the grocery list file name
     * @param format   the file format (json or csv)
     * @param category the item category for added items
     * @throws IOException if the file cannot be read or created
     */
    public CommandExecutor(String fileName, String format, String category) throws IOException {
        GroceryListDAO dao;

        if ("csv".equalsIgnoreCase(format)) {
            dao = new CsvGroceryListDAO(fileName);
        } else {
            dao = new JsonGroceryListDAO(fileName);
        }

        this.manager = new GroceryListManager(dao);
        this.category = category;
    }

    /**
     * Executes the specified command with provided arguments.
     *
     * @param command the command to execute ("add", "list", "remove")
     * @param args    the arguments associated with the command
     * @return 0 if successful, or an error code
     * @throws IOException if an I/O error occurs during execution
     */
    public int execute(String command, List<String> args) throws IOException {
        return switch (command) {
            case "add" -> addItem(args);
            case "list" -> listItems();
            case "remove" -> removeItem(args);
            default -> throw new IllegalArgumentException("Unknown command: " + command);
        };
    }

    /**
     * @return the manager used for list operations (mainly for testing)
     */
    public GroceryListManager getManager() {
        return manager;
    }

    /**
     * Adds an item to the grocery list with a quantity and category.
     *
     * @param args the arguments for the add command
     * @return 0 if successful
     * @throws IOException if saving the item fails
     */
    private int addItem(List<String> args) throws IOException {
        if (args.size() < 3) {
            throw new IllegalArgumentException("Missing arguments for 'add' command");
        }

        String itemName = args.get(1);
        int quantity;

        try {
            quantity = Integer.parseInt(args.get(2));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Quantity must be a number");
        }

        manager.addItem(itemName, quantity, category);
        return 0;
    }

    /**
     * Lists all items grouped by category and prints them to the console.
     *
     * @return 0 if successful
     */
    private int listItems() {
        manager.listItems().forEach(System.out::println);
        return 0;
    }

    /**
     * Removes an item from the grocery list.
     *
     * @param args the arguments for the remove command
     * @return 0 if successful
     * @throws IOException if saving changes fails
     */
    private int removeItem(List<String> args) throws IOException {
        if (args.size() < 2) {
            throw new IllegalArgumentException("Missing arguments for 'remove' command");
        }

        String itemName = args.get(1);
        manager.removeItem(itemName);
        return 0;
    }
}
