package com.fges.executor.commands;

import java.io.IOException;
import java.util.List;
import com.fges.core.GroceryListManager;

/**
 * Command to remove items from the grocery list.
 * This command removes the specified item from the grocery list.
 */
public class RemoveCommand implements Command {

    /** The grocery list manager responsible for removing the item */
    private final GroceryListManager manager;

    /**
     * Constructs the RemoveCommand with the specified manager.
     *
     * @param manager the grocery list manager responsible for removing the item
     */
    public RemoveCommand(GroceryListManager manager) {
        if (manager == null) {
            throw new IllegalArgumentException("GroceryListManager cannot be null.");
        }
        this.manager = manager;
    }

    /**
     * Executes the 'remove' command to delete an item from the grocery list.
     * If the item exists, it will be removed from the list.
     *
     * @param args the arguments for the remove command, which includes the item name to be removed
     * @return 0 if successful
     * @throws IOException if saving the updated list fails
     * @throws IllegalArgumentException if arguments are missing
     */
    @Override
    public int execute(List<String> args) throws IOException {
        if (args.size() < 1) {
            throw new IllegalArgumentException("""
                Usage: remove <item-name>
                Example: remove Milk
                """);
        }

        String itemName = args.get(0);
        if (itemName.isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be empty.");
        }
        
        manager.removeItem(itemName);
        return 0;
    }
}