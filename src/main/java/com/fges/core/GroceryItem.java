package com.fges.core;

import java.util.Objects;

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
        setName(name);
        setQuantity(quantity);
        setCategory(category);
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
     * Sets the name of the grocery item. Name must not be null or blank.
     *
     * @param name the name to assign
     */
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Item name must not be null or empty");
        }
        this.name = name;
    }

    /**
     * Sets the quantity of the grocery item.
     *
     * @param quantity the quantity to assign
     */
    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must be non-negative");
        }
        this.quantity = quantity;
    }

    /**
     * Sets the category of the grocery item.
     *
     * @param category the category to assign
     */
    public void setCategory(String category) {
        this.category = (category != null && !category.isBlank()) ? category.trim().toLowerCase() : null;
    }      

    /**
     * Validates the item fields and throws if any are invalid.
     */
    public void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Item name cannot be null or blank.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Item quantity must be positive.");
        }
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Item category cannot be null or blank.");
        }
    }

    /**
     * Combines this item with another of the same identity by adding quantities.
     * @param other the other item to merge
     */
    public void mergeWith(GroceryItem other) {
        if (other == null) return;
        if (!this.equals(other)) {
            throw new IllegalArgumentException("Cannot merge items with different name or category.");
        }
        this.quantity += other.quantity;
    }

    /**
     * Returns a readable representation of the grocery item.
     *
     * @return a string in the format "name: quantity (category)"
     */
    @Override
    public String toString() {
        return String.format("%s: %d%s", name, quantity, category != null ? " (" + category + ")" : "");
    }

    /**
     * Equality is based on case-insensitive name and category match.
     *
     * @param o the object to compare with
     * @return true if both items have the same name and category
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroceryItem other)) return false;
        return name != null && name.equalsIgnoreCase(other.name) &&
            Objects.equals(category, other.category);
    }

    /**
     * Hash code matches equals() — lowercased name + category.
     *
     * @return a hash code based on name and category
     */
    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase(), category);
    }
}