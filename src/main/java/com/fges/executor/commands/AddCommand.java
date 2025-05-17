package com.fges.executor.commands;

import java.io.IOException;
import java.util.List;
import com.fges.core.GroceryListManager;

/**
 * Command to add items to the grocery list.
 * This command adds an item with a specified quantity and category to the grocery list.
 */
public class AddCommand implements Command {

    /** The grocery list manager responsible for adding the item */
    private final GroceryListManager manager;

    /** The category associated with the added items */
    private final String category;

    /**
     * Constructs the AddCommand with the specified manager and category.
     *
     * @param manager the grocery list manager responsible for the operations
     * @param category the category for the added item
     */
    public AddCommand(GroceryListManager manager, String category) {
        if (manager == null) {
            throw new IllegalArgumentException("GroceryListManager cannot be null.");
        }
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Category cannot be null or blank for 'add' command.");
        }

        this.manager = manager;
        this.category = category;
    }

    /**
     * Executes the 'add' command to add an item with a specified quantity and category.
     *
     * @param args the arguments for the add command; expects item name, quantity, and category
     * @return 0 if successful
     * @throws IOException if there is an error while saving the item
     */
    @Override
    public int execute(List<String> args) throws IOException {
        if (args == null || args.size() < 2) {
            throw new IllegalArgumentException("""
                Usage: add <item-name> <quantity>
                Example: add Milk 2
                """);
        }
    
        String itemName = args.get(0);
        int quantity;
    
        try {
            quantity = Integer.parseInt(args.get(1));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Quantity must be a number");
        }
    
        if (itemName == null || itemName.isBlank()) {
            throw new IllegalArgumentException("Item name must not be blank.");
        }
    
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must be non-negative.");
        }
    
        manager.addItem(itemName, quantity, category);
        return 0;
    }
}