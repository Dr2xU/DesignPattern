package com.fges.executor.commands;

import java.util.List;
import java.util.Map;
import com.fges.core.GroceryItem;
import com.fges.core.GroceryListManager;

/**
 * Command to list items from the grocery list.
 * This command retrieves and prints all the items in the grocery list, grouped by category.
 */
public class ListCommand implements Command {

    /** The grocery list manager responsible for listing the items */
    private final GroceryListManager manager;

    /**
     * Constructs the ListCommand with the specified manager.
     *
     * @param manager the grocery list manager responsible for listing the items
     */
    public ListCommand(GroceryListManager manager) {
        if (manager == null) {
            throw new IllegalArgumentException("GroceryListManager cannot be null.");
        }
        this.manager = manager;
    }

    /**
     * Executes the 'list' command to display all items from the grocery list.
     * The items are printed to the console grouped by category.
     *
     * @param args the arguments for the list command (ignored in this case)
     * @return 0 if successful
     */
    @Override
    public int execute(List<String> args) {
        Map<String, List<GroceryItem>> groupedItems = manager.listItems();

        groupedItems.forEach((category, items) -> {
            System.out.println("# " + category + ":");
            for (GroceryItem item : items) {
                System.out.println(item.getName() + ": " + item.getQuantity());
            }
            System.out.println();
        });

        return 0;
    }
}