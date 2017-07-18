package com.kikisnight.inventoryapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.kikisnight.inventoryapp.data.InventoryContract.InventoryEntry;

public class DetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the inventory data loader
     */
    private static final int EXISTING_INVENTORY_LOADER = 1;

    /**
     * Content URI for the existing product
     */
    private Uri mCurrentProductUri;

    /**
     * TextView field to set the product's name
     */
    private TextView mNameTextView;

    /**
     * TextView field to set the product's price
     */
    private TextView mPriceTextView;

    /**
     * TextView field to set the product's supplier
     */
    private TextView mSupplierTextView;

    /**
     * TextView field to set the supplier's email
     */
    private TextView mEmailTextView;

    /**
     * TextView field to set the product's quantity
     */
    private TextView mQuantityTextView;

    /*
    * URI Loader
    */
    private static final int DETAIL_INVENTORY_LOADER = 0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_detail);

            // Receive Uri data from intent
            Intent intent = getIntent();
            mCurrentProductUri = intent.getData();
            if (mCurrentProductUri != null) {
                // Kick off LoaderManager
                getLoaderManager().initLoader(DETAIL_INVENTORY_LOADER, null, this);
            }

            // Find all relevant views that we will need to read user input from
            mNameTextView = (TextView) findViewById(R.id.view_product_name);
            mPriceTextView = (TextView) findViewById(R.id.view_product_price);
            mSupplierTextView = (TextView) findViewById(R.id.view_product_supplier);
            mEmailTextView = (TextView) findViewById(R.id.view_product_email);
            mQuantityTextView = (TextView) findViewById(R.id.view_product_quantity);

    }

        @Override
        public Loader<Cursor> onCreateLoader ( int id, Bundle args){
            String[] projection = {
                    InventoryEntry._ID,
                    InventoryEntry.COLUMN_INVENTORY_NAME,
                    InventoryEntry.COLUMN_INVENTORY_PRICE,
                    InventoryEntry.COLUMN_INVENTORY_SUPPLIER,
                    InventoryEntry.COLUMN_INVENTORY_EMAIL,
                    InventoryEntry.COLUMN_INVENTORY_QUANTITY };

            return new CursorLoader(this,       // Parent activity context
                    mCurrentProductUri,         // Table to query
                    projection,                 // Projection
                    null,                       // Selection clause
                    null,                       // Selection arguments
                    null);                      // Default sort order
        }

        @Override
        public void onLoadFinished (Loader < Cursor > loader, Cursor cursor){
            // Proceed with moving to the first row of the cursor and reading data from it
            // (This should be the only row in the cursor)
            if (cursor.moveToFirst()) {
                // Find the columns of products attributes that we're interested in
                int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_NAME);
                int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_PRICE);
                int supplierColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_SUPPLIER);
                int emailColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_EMAIL);
                int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_QUANTITY);

                // Extract out the value from the Cursor for the given column index
                String name = cursor.getString(nameColumnIndex);
                int price = cursor.getInt(priceColumnIndex);
                String supplier = cursor.getString(supplierColumnIndex);
                String email = cursor.getString(emailColumnIndex);
                int quantity = cursor.getInt(quantityColumnIndex);

                // Update the views on the screen with the values from the database
                mNameTextView.setText(name);
                mPriceTextView.setText(Integer.toString(price));
                mSupplierTextView.setText(supplier);
                mEmailTextView.setText(email);
                mQuantityTextView.setText(Integer.toString(quantity));
            }
        }

        @Override
        public void onLoaderReset (Loader < Cursor > loader) {
            // If the loader is invalidated, clear out all the data from the input fields.
            mNameTextView.setText("");
            mPriceTextView.setText("");
            mSupplierTextView.setText("");
            mEmailTextView.setText("");
            mQuantityTextView.setText("");
        }
    }