package com.fges.core;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import com.fges.dao.GroceryListDAO;

/**
 * Manages grocery list operations such as adding, removing, and listing items.
 * Uses a DAO implementation to persist and retrieve data.
 */
public class GroceryListManager {

    private static final Logger LOGGER = Logger.getLogger(GroceryListManager.class.getName());

    private final GroceryListDAO dao;
    private List<GroceryItem> groceryList;

    /**
     * Constructs a GroceryListManager using the given DAO.
     *
     * @param dao the data access object for persisting grocery items
     * @throws IOException if loading from the DAO fails
     */
    public GroceryListManager(GroceryListDAO dao) throws IOException {
        this.dao = dao;
        this.groceryList = dao.load();
        LOGGER.info("Grocery list loaded successfully.");
    }

    /**
     * Adds a grocery item with no category (defaults to null) and saves it.
     *
     * @param name     the item name
     * @param quantity the quantity of the item
     * @throws IOException if saving the updated list fails
     */
    public void addItem(String name, int quantity) throws IOException {
        groceryList.add(new GroceryItem(name, quantity, null));
        dao.save(groceryList);
        LOGGER.info("Added item: " + name + " (" + quantity + ")");
    }

    /**
     * Adds a grocery item with a category and saves it.
     *
     * @param name     the item name
     * @param quantity the quantity of the item
     * @param category the category of the item
     * @throws IOException if saving the updated list fails
     */
    public void addItem(String name, int quantity, String category) throws IOException {
        groceryList.add(new GroceryItem(name, quantity, category));
        dao.save(groceryList);
        LOGGER.info("Added item: " + name + " (" + quantity + ") in category [" + category + "]");
    }

    /**
     * Returns the list of items, grouped and formatted by category.
     *
     * @return a list of formatted strings grouped by category
     */
    public List<String> listItems() {
        Map<String, List<String>> categorizedItems = new TreeMap<>();

        for (GroceryItem item : groceryList) {
            String category = item.getCategory() != null ? item.getCategory() : "default";
            categorizedItems
                .computeIfAbsent(category, k -> new ArrayList<>())
                .add(item.getName() + ": " + item.getQuantity());
        }

        List<String> output = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : categorizedItems.entrySet()) {
            output.add("# " + entry.getKey() + ":");
            output.addAll(entry.getValue());
            output.add("");
        }

        if (!output.isEmpty()) {
            output.remove(output.size() - 1);
        }

        return output;
    }

    /**
     * Removes the item by name (case-insensitive) and saves the list.
     *
     * @param itemName the name of the item to remove
     * @throws IOException if saving the updated list fails
     */
    public void removeItem(String itemName) throws IOException {
        boolean removed = groceryList.removeIf(item -> item.getName().equalsIgnoreCase(itemName));
        dao.save(groceryList);

        if (removed) {
            LOGGER.info("Removed item: " + itemName);
        } else {
            LOGGER.warning("Attempted to remove non-existent item: " + itemName);
        }
    }
}
