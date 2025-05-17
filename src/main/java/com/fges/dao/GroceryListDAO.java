package com.fges.dao;

import java.io.IOException;
import java.util.List;

import com.fges.core.GroceryItem;

/**
 * Interface defining the contract for loading and saving grocery items.
 * Implementations can use various formats such as JSON, CSV, or databases.
 */
public interface GroceryListDAO {

    /**
     * Loads a list of grocery items from the underlying storage.
     *
     * @return a list of {@link GroceryItem} objects
     * @throws IOException if reading from the storage fails
     */
    List<GroceryItem> load() throws IOException;

    /**
     * Saves the provided list of grocery items to the underlying storage.
     *
     * @param items the list of {@link GroceryItem} to save
     * @throws IOException if writing to the storage fails
     */
    void save(List<GroceryItem> items) throws IOException;
}