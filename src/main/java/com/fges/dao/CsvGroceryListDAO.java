package com.fges.dao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.fges.core.GroceryItem;

/**
 * Implementation of GroceryListDAO for CSV file format.
 * Provides methods to load and save grocery items to a CSV file.
 */
public class CsvGroceryListDAO implements GroceryListDAO {

    private final File file;

    /**
     * Constructs a CsvGroceryListDAO with the specified filename.
     *
     * @param fileName the path to the CSV file
     */
    public CsvGroceryListDAO(String fileName) {
        this.file = new File(fileName);
    }

    /**
     * Loads the grocery list from the CSV file.
     *
     * @return a list of GroceryItem objects
     * @throws IOException if file reading or parsing fails
     */
    @Override
    public List<GroceryItem> load() throws IOException {
        List<GroceryItem> items = new ArrayList<>();

        if (!file.exists()) return items;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String name = parts[0].trim();
                    int quantity = Integer.parseInt(parts[1].trim());
                    String category = parts.length >= 3 ? parts[2].trim() : null;
                    items.add(new GroceryItem(name, quantity, category));
                }
            }
        }

        return items;
    }

    /**
     * Saves the grocery list to the CSV file.
     *
     * @param items the list of GroceryItem objects to save
     * @throws IOException if file writing fails
     */
    @Override
    public void save(List<GroceryItem> items) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Item,Quantity,Category\n"); // Header row
            for (GroceryItem item : items) {
                writer.write(item.getName() + "," + item.getQuantity() + "," +
                        (item.getCategory() != null ? item.getCategory() : "") + "\n");
            }
        }
    }
}
