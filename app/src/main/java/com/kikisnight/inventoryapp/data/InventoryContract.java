package com.kikisnight.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the Inventory app.
 */
public final class InventoryContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private InventoryContract() {}

    /**
     * The "Content authority" for our inventory app
     */
    public static final String CONTENT_AUTHORITY = "com.kikisnight.inventoryapp";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.kikisnight.inventoryapp/ is a valid path for
     * looking at inventory data.
     */
    public static final String PATH_INVENTORY = "inventory";

    /**
     * Inner class that defines constant values for the inventory database table.
     * Each entry in the table represents a single inventory.
     */
    public static final class InventoryEntry implements BaseColumns {

        /** The content Uri to access the pat data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);

        /** Name of database table for inventory */
        public final static String TABLE_NAME = "inventory";

        /**
         * Unique ID number for the each product in the inventory (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the product in the inventory
         *
         * Type: TEXT
         */
        public final static String COLUMN_INVENTORY_NAME ="name";

        /**
         * Price of the product
         *
         * Type: INTEGER
         */
        public final static String COLUMN_INVENTORY_PRICE = "price";

        /**
         * Provider of the product
         *
         * Type: TEXT
         */
        public final static String COLUMN_INVENTORY_SUPPLIER = "supplier";

        /**
         * Email of the provider for each product
         *
         * Type: TEXT
         */
        public final static String COLUMN_INVENTORY_EMAIL = "email";

        /**
         * Quantity for each product storage
         *
         * Type: INTEGER
         */
        public final static String COLUMN_INVENTORY_QUANTITY = "quantity";

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of products in the inventory.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single product in the inventory
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

    }
}
