package com.fges.core;

/**
 * Represents a grocery item with a name, quantity, and optional category.
 * This class is used for serialization/deserialization and data handling.
 */
public class GroceryItem {
    private String name;
    private int quantity;
    private String category;

    /**
     * Default constructor required for JSON deserialization (e.g., by Jackson).
     */
    public GroceryItem() {}

    /**
     * Constructs a new grocery item with the specified values.
     *
     * @param name     the name of the item
     * @param quantity the quantity of the item
     * @param category the category the item belongs to (optional)
     */
    public GroceryItem(String name, int quantity, String category) {
        this.name = name;
        this.quantity = quantity;
        this.category = category;
    }

    /**
     * @return the name of the grocery item
     */
    public String getName() {
        return name;
    }

    /**
     * @return the quantity of the grocery item
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @return the category of the grocery item, or null if not categorized
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the name of the grocery item.
     *
     * @param name the name to assign
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the quantity of the grocery item.
     *
     * @param quantity the quantity to assign
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Sets the category of the grocery item.
     *
     * @param category the category to assign
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Returns a readable representation of the grocery item.
     *
     * @return a string in the format "name: quantity (category)"
     */
    @Override
    public String toString() {
        return name + ": " + quantity + (category != null ? " (" + category + ")" : "");
    }
}
