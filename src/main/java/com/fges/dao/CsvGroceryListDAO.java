package com.fges.dao;

import com.fges.core.GroceryItem;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link GroceryListDAO} for handling CSV file operations.
 * Uses simple parsing and validation to support saving and loading grocery items.
 */
public class CsvGroceryListDAO implements GroceryListDAO {

    private final Path path;

    /**
     * Constructs a new CsvGroceryListDAO for a given file path.
     *
     * @param fileName the CSV file to load from and save to
     */
    public CsvGroceryListDAO(String fileName) {
        this.path = Path.of(fileName);
    }

    /**
     * Loads grocery items from a CSV file.
     *
     * @return list of {@link GroceryItem} parsed from the file
     */
    @Override
    public List<GroceryItem> load() throws IOException {
        List<GroceryItem> items = new ArrayList<>();
    
        if (!Files.exists(path)) {
            return items;
        }
    
        List<String> lines = Files.readAllLines(path);
        for (int i = 1; i < lines.size(); i++) { // Skip header
            String line = lines.get(i);
            String[] tokens = parseCsvLine(line);
            if (tokens.length != 3) continue;
    
            GroceryItem item = new GroceryItem(tokens[0].trim(), Integer.parseInt(tokens[1].trim()), tokens[2].trim());
            items.add(item);
        }
    
        return items;
    }
    
    /**
     * Simple CSV parser that handles quoted fields with commas.
     */
    private String[] parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean insideQuotes = false;
    
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
    
            if (c == '"') {
                insideQuotes = !insideQuotes;
            } else if (c == ',' && !insideQuotes) {
                fields.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
    
        fields.add(current.toString());
        return fields.toArray(new String[0]);
    }

    /**
     * Saves grocery items to a CSV file.
     *
     * @param items the list of {@link GroceryItem} to persist
     */
    @Override
    public void save(List<GroceryItem> items) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("Item,Quantity,Category");

        for (GroceryItem item : items) {
            lines.add(item.getName() + "," + item.getQuantity() + "," + item.getCategory());
        }

        Files.write(path, lines);
    }
}