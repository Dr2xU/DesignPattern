package com.fges.core;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link GroceryItem}.
 * Covers construction, validation, merging, and equality logic.
 */
class GroceryItemTest {

    /**
     * Should create item with valid data.
     */
    @Test
    void should_create_valid_item() {
        GroceryItem item = new GroceryItem("Milk", 2, "dairy");

        assertThat(item.getName()).isEqualTo("Milk");
        assertThat(item.getQuantity()).isEqualTo(2);
        assertThat(item.getCategory()).isEqualTo("dairy");
    }

    /**
     * Should normalize blank category to null.
     */
    @Test
    void should_normalize_blank_category() {
        GroceryItem item = new GroceryItem("Juice", 1, "   ");
        assertThat(item.getCategory()).isNull();
    }

    /**
     * Should normalize category case.
     */
    @Test
    void should_normalize_category_case_to_lowercase() {
        GroceryItem item = new GroceryItem("Milk", 2, "DAIRY");
        assertThat(item.getCategory()).isEqualTo("dairy");
    }

    /**
     * Should throw when name is null or blank.
     */
    @Test
    void should_throw_when_name_is_invalid() {
        assertThatThrownBy(() -> new GroceryItem(null, 1, "snack"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("name");

        assertThatThrownBy(() -> new GroceryItem("   ", 1, "snack"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("name");
    }

    /**
     * Should throw when quantity is negative.
     */
    @Test
    void should_throw_when_quantity_negative() {
        assertThatThrownBy(() -> new GroceryItem("Banana", -1, "fruit"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("non-negative");
    }

    /**
     * Should pass validation for well-formed item.
     */
    @Test
    void should_validate_successfully() {
        GroceryItem item = new GroceryItem("Bread", 2, "bakery");
        item.validate();
    }

    /**
     * Should fail validation if any field is invalid.
     */
    @Test
    void should_fail_validation_for_invalid_fields() {
        GroceryItem invalid1 = new GroceryItem("Valid", 0, "fruit");
        assertThatThrownBy(invalid1::validate).hasMessageContaining("positive");

        GroceryItem invalid2 = new GroceryItem("Valid", 1, "  ");
        assertThatThrownBy(invalid2::validate).hasMessageContaining("category");
    }

    /**
     * Should merge quantities for same item.
     */
    @Test
    void should_merge_with_same_item() {
        GroceryItem a = new GroceryItem("Apple", 2, "fruit");
        GroceryItem b = new GroceryItem("apple", 3, "fruit");

        a.mergeWith(b);

        assertThat(a.getQuantity()).isEqualTo(5);
    }

    /**
     * Should not merge with different items.
     */
    @Test
    void should_throw_if_merge_different_items() {
        GroceryItem a = new GroceryItem("Apple", 2, "fruit");
        GroceryItem b = new GroceryItem("Orange", 3, "fruit");

        assertThatThrownBy(() -> a.mergeWith(b))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("different name or category");
    }

    /**
     * Should test equals and hashCode based on name and category.
     */
    @Test
    void should_test_equals_and_hashcode() {
        GroceryItem item1 = new GroceryItem("Milk", 2, "dairy");
        GroceryItem item2 = new GroceryItem("milk", 5, "dairy");

        assertThat(item1).isEqualTo(item2);
        assertThat(item1.hashCode()).isEqualTo(item2.hashCode());
    }

    /**
     * Should return readable toString format.
     */
    @Test
    void should_format_to_string() {
        GroceryItem item = new GroceryItem("Water", 3, "drinks");

        assertThat(item.toString()).isEqualTo("Water: 3 (drinks)");
    }
}