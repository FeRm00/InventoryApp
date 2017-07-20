package com.kikisnight.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import static android.R.attr.id;
import static android.net.Uri.parse;

import com.kikisnight.inventoryapp.data.InventoryContract.InventoryEntry;

/**
 * {@link InventoryCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of inventory data as its data source. This adapter knows
 * how to create list items for each row of product in the inventory data in the {@link Cursor}.
 */
public class InventoryCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link InventoryCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the inventory data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current product can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        TextView supplierTextView = (TextView) view.findViewById(R.id.supplier);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        ImageView imageImageView = (ImageView) view.findViewById(R.id.image);

        // Find the columns of inventory attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_PRICE);
        int supplierColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_SUPPLIER);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_QUANTITY);
        int imageColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVENTORY_IMAGE);

        // Read the inventory attributes from the Cursor for the current product
        String inventoryName = cursor.getString(nameColumnIndex);
        double inventoryPrice = cursor.getDouble(priceColumnIndex);
        String inventorySupplier = cursor.getString(supplierColumnIndex);
        final int inventoryQuantity = cursor.getInt(quantityColumnIndex);
        final String inventoryImage = cursor.getString(imageColumnIndex);
        final Uri uri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, cursor.getInt(cursor.getColumnIndexOrThrow(InventoryEntry._ID)));


        // Update the TextViews with the attributes for the current product
        nameTextView.setText(inventoryName);
        priceTextView.setText(context.getString(R.string.price) + " " + inventoryPrice + "€");
        supplierTextView.setText(context.getString(R.string.supplier) + " " + inventorySupplier);
        quantityTextView.setText(context.getString(R.string.stock) + " " + inventoryQuantity);

        Uri imageUri = parse(inventoryImage);
        imageImageView.setImageURI(imageUri);

        //Find detail_activity button
        Button detailActivity = (Button) view.findViewById(R.id.detail_activity);
        //Set up the detail_activity button
        detailActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);

                intent.setData(uri);
                context.startActivity(intent);
            }
        });

        //Find decrease_one_unit button
        Button decreaseOneUnit = (Button) view.findViewById(R.id.decrease_one_unit);
        decreaseOneUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newStock = inventoryQuantity;
                if (inventoryQuantity > 0) {
                    newStock--;
                } else {
                    newStock = 0;
                }
                //Update in the data base the new value
                ContentValues values = new ContentValues();
                values.put(InventoryEntry.COLUMN_INVENTORY_QUANTITY, newStock);
                Uri currentProductUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);
                int numRowsUpdated = context.getContentResolver().update(uri, values, null, null);
            }
        });
    }
}