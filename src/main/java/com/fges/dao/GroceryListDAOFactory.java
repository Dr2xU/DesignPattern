package com.fges.dao;

/**
 * Factory class responsible for creating a GroceryListDAO instance
 * based on the provided format (e.g., "json", "csv").
 */
public class GroceryListDAOFactory {

    /**
     * Creates the appropriate DAO implementation based on the file format.
     *
     * @param format the format of the file ("json" or "csv")
     * @param fileName the name/path of the file to use
     * @return an instance of GroceryListDAO
     * @throws IllegalArgumentException if the format is unsupported
     */
    public static GroceryListDAO create(String format, String fileName) {
        String normalized = format == null ? "" : format.trim().toLowerCase();
        return switch (normalized) {
            case "json" -> new JsonGroceryListDAO(fileName);
            case "csv" -> new CsvGroceryListDAO(fileName);
            default -> throw new IllegalArgumentException("Unsupported format: " + format);
        };
    }
}