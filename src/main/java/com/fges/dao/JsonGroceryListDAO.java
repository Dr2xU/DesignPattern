package com.fges.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fges.core.GroceryItem;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object (DAO) implementation for reading and writing grocery list data in JSON format.
 */
public class JsonGroceryListDAO implements GroceryListDAO {

    private static final Logger LOGGER = Logger.getLogger(JsonGroceryListDAO.class.getName());

    private final ObjectMapper mapper = new ObjectMapper();
    private final File file;

    /**
     * Constructs a new JsonGroceryListDAO for the specified file.
     *
     * @param fileName the name of the JSON file to read/write grocery data
     */
    public JsonGroceryListDAO(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("JSON file name must not be null or empty.");
        }

        this.file = new File(fileName);
    }

    /**
     * Loads grocery items from the JSON file.
     *
     * @return a list of GroceryItem objects; returns an empty list if the file doesn't exist
     * @throws IOException if the file exists but cannot be read or parsed
     */
    @Override
    public List<GroceryItem> load() throws IOException {
        if (!file.exists()) {
            LOGGER.info("JSON file does not exist. Returning empty grocery list.");
            return new ArrayList<>();
        }

        if (file.length() == 0) {
            LOGGER.warning("JSON file is empty. Returning empty grocery list: " + file.getAbsolutePath());
            return new ArrayList<>();
        }

        try {
            return mapper.readValue(file, new TypeReference<>() {});
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load grocery list from JSON file: " + file.getName(), e);
            throw e;
        }
    }

    /**
     * Saves the list of grocery items to the JSON file.
     *
     * @param items the list of GroceryItem objects to be saved
     * @throws IOException if the file cannot be written
     */
    @Override
    public void save(List<GroceryItem> items) throws IOException {
        for (GroceryItem item : items) {
            item.validate();
        }

        try {
            if (!file.exists() && file.createNewFile()) {
                LOGGER.info("Created new JSON file: " + file.getAbsolutePath());
            }

            mapper.writeValue(file, items);
            LOGGER.info("Grocery list saved to: " + file.getAbsolutePath());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save grocery list to JSON file: " + file.getName(), e);
            throw e;
        }
    }
}