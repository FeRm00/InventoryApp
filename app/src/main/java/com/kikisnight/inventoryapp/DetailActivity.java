package com.kikisnight.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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

    /*
    * TextView to show the units to decrease or increase the current stock
    */
    private  TextView mStockTextView;

    /*
    * Switch to decrease or increase the quantity of the products in the inventory
    */
    private Switch mSwitchCheck;

    /*
    * Variable to modify the stock regarding the quantity on the TextView.
    */
    private int mNewStock = 0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_detail);

            // Find all relevant views that we will need to write all the input data
            mNameTextView = (TextView) findViewById(R.id.view_product_name);
            mPriceTextView = (TextView) findViewById(R.id.view_product_price);
            mSupplierTextView = (TextView) findViewById(R.id.view_product_supplier);
            mEmailTextView = (TextView) findViewById(R.id.view_product_email);
            mQuantityTextView = (TextView) findViewById(R.id.view_product_quantity);
            //TextView for modify the stock
            mStockTextView = (TextView) findViewById((R.id.view_adjust_stock));
            //Switch for decrease or increase quantity
            mSwitchCheck = (Switch) findViewById(R.id.switchAdjust);

            // Receive Uri data from intent
            Intent intent = getIntent();
            mCurrentProductUri = intent.getData();
            if (mCurrentProductUri != null) {
                // Kick off LoaderManager
                getLoaderManager().initLoader(DETAIL_INVENTORY_LOADER, null, this);
            }

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

        /*
        / Adjust the current stock with the quantity in the TextView view_adjust_stock
         */
        public void adjustStock (View view) {
            int currentStock = Integer.parseInt(mQuantityTextView.getText().toString());
            int updateStock;

            if(mSwitchCheck.isChecked()) {
                updateStock = currentStock + mNewStock;
            } else if (currentStock - mNewStock > 0){
                updateStock = currentStock - mNewStock;
            } else {
                updateStock = 0;
            }

            //Update in the data base the new value
            ContentValues values = new ContentValues();
            values.put(InventoryEntry.COLUMN_INVENTORY_QUANTITY, updateStock);
            int numRowsUpdated = getContentResolver().update(mCurrentProductUri, values, null, null);
        }

        /*
        * Decrease de quantity on the view_adjust_stock
        */
        public void actionDecrease (View view) {
            if(mCurrentProductUri != null) {
                if (mNewStock > 0) {
                    mNewStock --;
                    mStockTextView.setText(String.valueOf(mNewStock));
                } else {
                    mNewStock = 0;
                    mStockTextView.setText(String.valueOf(mNewStock));
                    Toast.makeText(this, getString(R.string.toast_invalid_quantity),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }

        /*
        * Decrease de quantity on the view_adjust_stock
        */
        public void actionIncrease (View view) {
            if(mCurrentProductUri != null) {
                    mNewStock ++;
                    mStockTextView.setText(String.valueOf(mNewStock));
            }
        }

        /*
        * Order a new stock to the supplier with the email saved in the inventory data base.
        */
         public void sendEmail (View view) {
             Intent intent = new Intent(Intent.ACTION_SEND);
             intent.setData(Uri.parse("mailto:"));
             intent.setType("text/plain");
             intent.putExtra(Intent.EXTRA_EMAIL, mEmailTextView.getText().toString());
             intent.putExtra(Intent.EXTRA_SUBJECT, mSupplierTextView.getText().toString());

             startActivity(Intent.createChooser(intent, "Send Email"));
        }

        public void stateMessage (View view) {
            if (mSwitchCheck.isChecked()) {
                Toast.makeText(this, getString(R.string.switch_increase), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.switch_decrease), Toast.LENGTH_SHORT).show();
            }
        }
    }