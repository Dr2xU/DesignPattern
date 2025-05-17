package com.fges.core;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.fges.dao.GroceryListDAO;

/**
 * Manages grocery list operations such as adding, removing, and listing items.
 * Uses a DAO implementation to persist and retrieve data.
 * Thread-safe to support concurrent access (e.g., web usage).
 */
public class GroceryListManager {

    private static final Logger LOGGER = Logger.getLogger(GroceryListManager.class.getName());
    private static final String DEFAULT_CATEGORY = "default";

    private final GroceryListDAO dao;

    // Wrapped in synchronizedList for safety, but still guarded by synchronized methods.
    private final List<GroceryItem> groceryList;

    /**
     * Constructs a GroceryListManager using the given DAO.
     *
     * @param dao the data access object for persisting grocery items
     * @throws IOException if loading from the DAO fails
     */
    public GroceryListManager(GroceryListDAO dao) throws IOException {
        this.dao = Objects.requireNonNull(dao);
        this.groceryList = Collections.synchronizedList(new ArrayList<>(dao.load()));
        LOGGER.info("Grocery list loaded successfully.");
    }

    /**
     * Adds a grocery item with optional category.
     * Merges with existing items by name and category.
     *
     * @param name     item name
     * @param quantity item quantity
     * @param category optional category
     * @throws IOException if persistence fails
     */
    public synchronized void addItem(String name, int quantity, String category) throws IOException {
        String normalizedCategory = normalizeCategory(category).toLowerCase();
        GroceryItem newItem = new GroceryItem(name, quantity, normalizedCategory);

        Optional<GroceryItem> existing = groceryList.stream()
                .filter(i -> i.equals(newItem))
                .findFirst();

        if (existing.isPresent()) {
            existing.get().mergeWith(newItem);
            LOGGER.info("Updated existing item: " + name + " (+" + quantity + ") in [" + normalizedCategory + "]");
        } else {
            groceryList.add(newItem);
            LOGGER.info("Added new item: " + name + " (" + quantity + ") in [" + normalizedCategory + "]");
        }

        persist();
    }

    /**
     * Adds item using default category.
     */
    public synchronized void addItem(String name, int quantity) throws IOException {
        addItem(name, quantity, null);
    }

    /**
     * Lists all items grouped by normalized category.
     *
     * @return a sorted map of category to item list
     */
    public synchronized Map<String, List<GroceryItem>> listItems() {
        return groceryList.stream()
                .collect(Collectors.groupingBy(
                        item -> normalizeCategory(item.getCategory()),
                        TreeMap::new,
                        Collectors.toList()
                ));
    }

    /**
     * Removes an item by name (case-insensitive).
     *
     * @param itemName name to match
     * @throws IOException if persistence fails
     */
    public synchronized void removeItem(String itemName) throws IOException {
        boolean removed = groceryList.removeIf(matchesName(itemName));

        if (removed) {
            LOGGER.info("Removed item(s): " + itemName);
        } else {
            LOGGER.warning("Item not found for removal: " + itemName);
        }

        persist();
    }

    /**
     * Persists current list state via DAO.
     */
    private synchronized void persist() throws IOException {
        try {
            dao.save(groceryList);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to persist grocery list.", e);
            throw e;
        }
    }

    /**
     * Returns a copy of the grocery list (read-only).
     *
     * @return defensive copy
     */
    public synchronized List<GroceryItem> getItems() {
        return new ArrayList<>(groceryList);
    }

    /**
     * Converts blank or null category to "default".
     */
    private String normalizeCategory(String category) {
        return (category == null || category.isBlank()) ? DEFAULT_CATEGORY : category.trim();
    }

    /**
     * Case-insensitive match for removal.
     */
    private Predicate<GroceryItem> matchesName(String name) {
        return item -> item.getName().equalsIgnoreCase(name);
    }
}