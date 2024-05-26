package com.example.sklep_projekt;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class OrdersListActivity extends ChangeLanguage {
    Database databaseHelper;
    ListView listViewOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_list);

        databaseHelper = new Database(this);
        listViewOrders = findViewById(R.id.listViewOrders);

        loadOrders();
    }
    private void loadOrders() {
        Cursor cursor = databaseHelper.fetchAllOrders();
        String[] from = {
                Database.COLUMN_DESCRIPTION,
                Database.COLUMN_PRICE,
                Database.COLUMN_DATE,
                Database.COLUMN_TIME,
                Database.COLUMN_USER_NAME,
                Database.COLUMN_IMAGE,
                Database.COLUMN_QUANTITY
        };
        int[] to = {
                R.id.textViewDescription,
                R.id.textViewPrice,
                R.id.textViewDate,
                R.id.textViewTime,
                R.id.textViewUserName,
                R.id.imageViewOrder
        };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.order_list_item,
                cursor,
                from,
                to,
                0
        ) {
            @Override
            public void setViewText(TextView v, String text) {
                int columnIndex = cursor.getColumnIndex(Database.COLUMN_QUANTITY);
                if (columnIndex != -1) {
                    int quantity = cursor.getInt(columnIndex);
                    if (v.getId() == R.id.textViewDescription && quantity > 0) {
                        String fullText = text + " <font color='#4CAF50'> x" + quantity + "</font>";
                        v.setText(Html.fromHtml(fullText));
                        return;
                    }
                }
                super.setViewText(v, text);
            }

            @Override
            public void setViewImage(ImageView v, String value) {
                if (value != null && !value.isEmpty()) {
                    byte[] decodedString = Base64.decode(value, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    v.setImageBitmap(decodedByte);
                    v.setVisibility(View.VISIBLE);
                }
            }
        };
        listViewOrders.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.second_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.change_language) {
            SharedPreferences prefs = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            String currentLanguage = prefs.getString("My_Lang", "");
            if (currentLanguage.equals("en")) {
                setLocale("pl");
            } else {
                setLocale("en");
            }

            recreate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
