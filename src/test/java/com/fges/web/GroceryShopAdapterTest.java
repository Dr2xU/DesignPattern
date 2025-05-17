package com.fges.web;

import com.fges.core.GroceryItem;
import com.fges.core.GroceryListManager;
import com.fges.dao.GroceryListDAO;
import fr.anthonyquere.MyGroceryShop.Runtime;
import fr.anthonyquere.MyGroceryShop.WebGroceryItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link GroceryShopAdapter}.
 * Verifies it bridges correctly between web interface and core logic.
 */
class GroceryShopAdapterTest {

    private GroceryListManager manager;
    private GroceryShopAdapter adapter;

    /**
     * Sets up in-memory manager and adapter before each test.
     */
    @BeforeEach
    void setUp() throws IOException {
        GroceryListDAO dao = new GroceryListDAO() {
            private List<GroceryItem> items = new ArrayList<>();

            @Override
            public List<GroceryItem> load() {
                return new ArrayList<>(items);
            }

            @Override
            public void save(List<GroceryItem> items) {
                this.items = new ArrayList<>(items);
            }
        };

        manager = new GroceryListManager(dao);
        adapter = new GroceryShopAdapter(manager);
    }

    /**
     * Should return grocery items from manager mapped to WebGroceryItem.
     */
    @Test
    void should_return_groceries_as_web_grocery_items() throws IOException {
        manager.addItem("Tomato", 4, "vegetables");

        List<WebGroceryItem> items = adapter.getGroceries();
        assertThat(items).hasSize(1);
        assertThat(items.get(0).name()).isEqualTo("Tomato");
        assertThat(items.get(0).quantity()).isEqualTo(4);
        assertThat(items.get(0).category()).isEqualTo("vegetables");
    }

    /**
     * Should delegate addition of grocery items to the manager.
     */
    @Test
    void should_add_item_via_adapter() {
        adapter.addGroceryItem("Eggs", 6, "breakfast");
        List<WebGroceryItem> items = adapter.getGroceries();

        assertThat(items).anySatisfy(item -> {
            assertThat(item.name()).isEqualTo("Eggs");
            assertThat(item.quantity()).isEqualTo(6);
            assertThat(item.category()).isEqualTo("breakfast");
        });
    }

    /**
     * Should remove items from manager through adapter.
     */
    @Test
    void should_remove_item_via_adapter() {
        adapter.addGroceryItem("Juice", 1, "drinks");
        adapter.removeGroceryItem("Juice");

        List<WebGroceryItem> items = adapter.getGroceries();
        assertThat(items).isEmpty();
    }

    /**
     * Should return valid runtime information.
     */
    @Test
    void should_return_runtime_info() {
        Runtime runtime = adapter.getRuntime();

        assertThat(runtime.todayDate()).isEqualTo(LocalDate.now());
        assertThat(runtime.javaVersion()).isNotBlank();
        assertThat(runtime.osName()).isNotBlank();
    }

    /**
     * Should throw if manager is null.
     */
    @Test
    void should_throw_if_manager_is_null() {
        assertThatThrownBy(() -> new GroceryShopAdapter(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("GroceryListManager cannot be null");
    }
}