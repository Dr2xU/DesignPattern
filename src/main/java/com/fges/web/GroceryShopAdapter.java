package com.fges.web;

import com.fges.core.GroceryListManager;
import fr.anthonyquere.MyGroceryShop;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Adapter class that bridges the {@link GroceryListManager}
 * to the {@link MyGroceryShop} interface for use with the web UI.
 */
public class GroceryShopAdapter implements MyGroceryShop {

    private final GroceryListManager manager;

    /**
     * Constructs a GroceryShopAdapter using a {@link GroceryListManager}.
     *
     * @param manager the core manager to delegate data operations
     */
    public GroceryShopAdapter(GroceryListManager manager) {
        this.manager = Objects.requireNonNull(manager, "GroceryListManager cannot be null");
    }

    /**
     * Returns a list of grocery items formatted for the web frontend.
     *
     * @return a list of {@link WebGroceryItem}
     */
    @Override
    public List<MyGroceryShop.WebGroceryItem> getGroceries() {
        return manager.getItems().stream()
                .map(item -> new WebGroceryItem(item.getName(), item.getQuantity(), item.getCategory()))
                .collect(Collectors.toList());
    }

    /**
     * Adds a new item via the underlying manager.
     *
     * @param name     the name of the grocery item
     * @param quantity the quantity of the grocery item
     * @param category the category of the grocery item
     */
    @Override
    public void addGroceryItem(String name, int quantity, String category) {
        try {
            manager.addItem(name, quantity, category);
        } catch (IOException e) {
            throw new RuntimeException("Failed to add grocery item", e);
        }
    }

    /**
     * Removes a grocery item by name.
     *
     * @param name the name of the item to remove
     */
    @Override
    public void removeGroceryItem(String name) {
        try {
            manager.removeItem(name);
        } catch (IOException e) {
            throw new RuntimeException("Failed to remove grocery item", e);
        }
    }

    /**
     * Returns basic runtime info for display in the UI.
     *
     * @return the {@link Runtime} record for the system
     */
    @Override
    public Runtime getRuntime() {
        return new Runtime(
                LocalDate.now(),
                System.getProperty("java.version"),
                System.getProperty("os.name")
        );
    }
}