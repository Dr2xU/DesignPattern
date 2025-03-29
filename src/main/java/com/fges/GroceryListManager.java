// GroceryListManager.java

package com.fges;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GroceryListManager {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final String fileName;
    private List<String> groceryList;

    public GroceryListManager(String fileName) throws IOException {
        this.fileName = fileName;
        this.groceryList = loadGroceryList();
    }

    private List<String> loadGroceryList() throws IOException {
        Path filePath = Paths.get(fileName);
        
        if (Files.exists(filePath)) {
            String fileContent = Files.readString(filePath);
            var parsedList = OBJECT_MAPPER.readValue(fileContent, new TypeReference<List<String>>() {});
            return new ArrayList<>(parsedList);
        } else {
            return new ArrayList<>();
        }
    }

    private void saveGroceryList() throws IOException {
        OBJECT_MAPPER.writeValue(new File(fileName), groceryList);
    }

    public void addItem(String itemName, int quantity) throws IOException {
        groceryList.add(itemName + ": " + quantity);
        saveGroceryList();
    }

    public List<String> listItems() {
        return groceryList;
    }

    public void removeItem(String itemName) throws IOException {
        var newGroceryList = groceryList.stream()
                .filter(item -> !item.contains(itemName))
                .toList();
        groceryList = new ArrayList<>(newGroceryList);
        saveGroceryList();
    }
}