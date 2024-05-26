package com.example.sklep_projekt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.widget.Toast;

public class HomeActivity extends ChangeLanguage {
    String[] deskiDescriptions = {
            "Wybierz deskę",
            "DESKA INNERFORCE LAYER ALC.S",
            "DESKA PETR KORBEL XXS",
            "DESKA OVTCHAROV S1 XXS"
    };
    int[] deskiImages = {
            0,
            R.drawable.deska_1,
            R.drawable.deska_2,
            R.drawable.deska_3
    };
    double[] deskiPrices = {0, 720, 190, 265};

    String[] stolyDescriptions = {
            "Wybierz stół",
            "STÓŁ SPACE SAVER 22",
            "STÓŁ EUROPA II",
            "STÓŁ MIDI BUTTERFLY"
    };
    int[] stolyImages = {
            0,
            R.drawable.stol_1,
            R.drawable.stol_2,
            R.drawable.stol_3
    };
    double[] stolyPrices = {0,2900, 3750, 700};

    String[] okladzinyDescriptions = {
            "Wybierz okładzinę",
            "OKŁADZINA TENERGY 05 FX",
            "OKŁADZINA DIGNICS 09C"
    };
    int[] okladzinyImages = {
            0,
            R.drawable.okladzina_1,
            R.drawable.okladzina_2
    };
    double[] okladzinyPrices = {0,290, 385};

    String[] butyDescriptions = {
            "Wybierz parę butów",
            "BUTY LEZOLINE RIFONES FIOLETOWE",
            "BUTY LEZOLINE RIFONES CZARNE",
            "BUTY LEZOLINE MACH NIEBIESKIE"
    };
    int[] butyImages = {
            0,
            R.drawable.buty_1,
            R.drawable.buty_2,
            R.drawable.buty_3
    };
    double[] butyPrices = {0,300, 500, 350};
    int[] deskiQuantities = {0, 0, 0};
    int[] stolyQuantities = {0, 0, 0};
    int[] okladzinyQuantities = {0, 0};
    int[] butyQuantities = {0, 0, 0};
    int selectedCategoryIndex = -1;
    private ImageView imageViewSelectedItem;
    private TextView textViewSelectedCategory;
    private TextView textViewSelectedPrice;
    private TextView textViewTotalPrice;
    private Spinner spinnerDeski, spinnerStoly, spinnerOkladziny, spinnerButy;
    private EditText editTextQuantity, editTextUserName, editTextEmail;
    private Button buttonSendEmail;

    double totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loadLocale();

        String username = getIntent().getStringExtra("username");

        editTextUserName = findViewById(R.id.editTextUserName);
        if (username != null) {
            editTextUserName.setText(username);
        }

        imageViewSelectedItem = findViewById(R.id.imageViewSelectedItem);
        textViewSelectedCategory = findViewById(R.id.textViewSelectedCategory);
        textViewSelectedPrice = findViewById(R.id.textViewSelectedPrice);
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice);
        spinnerDeski = findViewById(R.id.spinnerDeski);
        spinnerStoly = findViewById(R.id.spinnerStoly);
        spinnerOkladziny = findViewById(R.id.spinnerOkladziny);
        spinnerButy = findViewById(R.id.spinnerButy);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        editTextEmail = findViewById(R.id.editTextEmail);

        buttonSendEmail = findViewById(R.id.buttonSendEmail);
        buttonSendEmail.setOnClickListener(v -> sendEmailWithLatestOrder());

        textViewTotalPrice.setText(String.format(Locale.getDefault(), getString(R.string.total_price), 0.00));

        SpinnerAdapter deskiAdapter = new SpinnerAdapter(this, deskiImages, deskiDescriptions, deskiPrices);
        spinnerDeski.setAdapter(deskiAdapter);
        spinnerDeski.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    updateSelectedItem(deskiImages[position], deskiDescriptions[position], deskiPrices[position], position - 1, deskiQuantities);
                } else {
                    resetSelection();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        SpinnerAdapter stolyAdapter = new SpinnerAdapter(this, stolyImages, stolyDescriptions, stolyPrices);
        spinnerStoly.setAdapter(stolyAdapter);
        spinnerStoly.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    updateSelectedItem(stolyImages[position], stolyDescriptions[position], stolyPrices[position], position - 1, stolyQuantities);
                } else {
                    resetSelection();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        SpinnerAdapter okladzinyAdapter = new SpinnerAdapter(this, okladzinyImages, okladzinyDescriptions, okladzinyPrices);
        spinnerOkladziny.setAdapter(okladzinyAdapter);
        spinnerOkladziny.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    updateSelectedItem(okladzinyImages[position], okladzinyDescriptions[position], okladzinyPrices[position], position - 1, okladzinyQuantities);
                } else {
                    resetSelection();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        SpinnerAdapter butyAdapter = new SpinnerAdapter(this, butyImages, butyDescriptions, butyPrices);
        spinnerButy.setAdapter(butyAdapter);
        spinnerButy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    updateSelectedItem(butyImages[position], butyDescriptions[position], butyPrices[position], position - 1, butyQuantities);
                } else {
                    resetSelection();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        findViewById(R.id.buttonConfirmQuantity).setOnClickListener(v -> {
            int quantity = Integer.parseInt(editTextQuantity.getText().toString());
            String selectedItemText = textViewSelectedCategory.getText().toString();

            if (selectedItemText.contains("DESKA")) {
                deskiQuantities[selectedCategoryIndex] = quantity;
            } else if (selectedItemText.contains("STÓŁ")) {
                stolyQuantities[selectedCategoryIndex] = quantity;
            } else if (selectedItemText.contains("OKŁADZINA")) {
                okladzinyQuantities[selectedCategoryIndex] = quantity;
            } else if (selectedItemText.contains("BUTY")) {
                butyQuantities[selectedCategoryIndex] = quantity;
            }
            updateTotalPrice();
        });
    }
    private void updateSelectedItem(int imageResId, String description, double price, int categoryIndex, int[] quantities) {
        imageViewSelectedItem.setImageResource(imageResId);
        textViewSelectedCategory.setText(description);
        textViewSelectedPrice.setText(String.format(Locale.getDefault(), "%.2f zł", price));

        textViewSelectedCategory.setVisibility(View.VISIBLE);
        textViewSelectedPrice.setVisibility(View.VISIBLE);
        imageViewSelectedItem.setVisibility(View.VISIBLE);
        editTextQuantity.setVisibility(View.VISIBLE);
        findViewById(R.id.buttonConfirmQuantity).setVisibility(View.VISIBLE);

        editTextQuantity.setText(String.valueOf(quantities[categoryIndex]));
        selectedCategoryIndex = categoryIndex;
    }
    private void updateTotalPrice() {
        totalPrice = 0;

        for (int i = 1; i < deskiPrices.length; i++) {
            totalPrice += deskiQuantities[i - 1] * deskiPrices[i];
        }
        for (int i = 1; i < stolyPrices.length; i++) {
            totalPrice += stolyQuantities[i - 1] * stolyPrices[i];
        }
        for (int i = 1; i < okladzinyPrices.length; i++) {
            totalPrice += okladzinyQuantities[i - 1] * okladzinyPrices[i];
        }
        for (int i = 1; i < butyPrices.length; i++) {
            totalPrice += butyQuantities[i - 1] * butyPrices[i];
        }

        String totalPriceText = String.format(Locale.getDefault(), getString(R.string.total_price), totalPrice);
        textViewTotalPrice.setText(totalPriceText);
    }


    private String encodeImageToBase64(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    public void saveOrder(View view) {
        String userName = editTextUserName.getText().toString();

        if (userName.trim().isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_username), Toast.LENGTH_SHORT).show();
            return;
        }

        Database db = new Database(this);
        String currentDate = getCurrentDate();
        String currentTime = getCurrentTime();

        for (int i = 1; i < deskiDescriptions.length; i++) {
            if (deskiQuantities[i - 1] > 0) {
                String encodedImage = encodeImageToBase64(((BitmapDrawable) getResources().getDrawable(deskiImages[i])).getBitmap());
                db.insertOrder(deskiDescriptions[i], deskiPrices[i] * deskiQuantities[i - 1], currentDate, currentTime, userName, encodedImage, deskiQuantities[i - 1]);
            }
        }

        for (int i = 1; i < stolyDescriptions.length; i++) {
            if (stolyQuantities[i - 1] > 0) {
                String encodedImage = encodeImageToBase64(((BitmapDrawable) getResources().getDrawable(stolyImages[i])).getBitmap());
                db.insertOrder(stolyDescriptions[i], stolyPrices[i] * stolyQuantities[i - 1], currentDate, currentTime, userName, encodedImage, stolyQuantities[i - 1]);
            }
        }

        for (int i = 1; i < okladzinyDescriptions.length; i++) {
            if (okladzinyQuantities[i - 1] > 0) {
                String encodedImage = encodeImageToBase64(((BitmapDrawable) getResources().getDrawable(okladzinyImages[i])).getBitmap());
                db.insertOrder(okladzinyDescriptions[i], okladzinyPrices[i] * okladzinyQuantities[i - 1], currentDate, currentTime, userName, encodedImage, okladzinyQuantities[i - 1]);
            }
        }

        for (int i = 1; i < butyDescriptions.length; i++) {
            if (butyQuantities[i - 1] > 0) {
                String encodedImage = encodeImageToBase64(((BitmapDrawable) getResources().getDrawable(butyImages[i])).getBitmap());
                db.insertOrder(butyDescriptions[i], butyPrices[i] * butyQuantities[i - 1], currentDate, currentTime, userName, encodedImage, butyQuantities[i - 1]);
            }
        }

        resetQuantitiesAndUI();
    }
    private void resetQuantitiesAndUI() {
        for (int i = 0; i < deskiQuantities.length; i++) {
            deskiQuantities[i] = 0;
        }
        for (int i = 0; i < stolyQuantities.length; i++) {
            stolyQuantities[i] = 0;
        }
        for (int i = 0; i < okladzinyQuantities.length; i++) {
            okladzinyQuantities[i] = 0;
        }
        for (int i = 0; i < butyQuantities.length; i++) {
            butyQuantities[i] = 0;
        }

        editTextUserName.setText("");
        editTextQuantity.setText("0");

        totalPrice = 0;
        textViewTotalPrice.setText(String.format(Locale.getDefault(), getString(R.string.total_price), totalPrice));
    }

    private void sendEmailWithLatestOrder() {
        Database database = new Database(this);
        Cursor cursor = database.fetchLatestOrder();

        if (cursor != null && cursor.moveToFirst()) {
            String description = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_DESCRIPTION));
            String price = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_PRICE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_DATE));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_TIME));
            String userName = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_USER_NAME));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_QUANTITY));

            String orderDetails = description + " x" + quantity + "\n" + price + "\n" + date + "-->" + time + "\n" + userName;

            String email = editTextEmail.getText().toString();

            if (!TextUtils.isEmpty(email)) {
                EmailSender.sendEmailInBackground(email, getString(R.string.order), orderDetails);
                Toast.makeText(this, getString(R.string.toast_home), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.enter_username), Toast.LENGTH_SHORT).show();
            }

            cursor.close();
        } else {
            Toast.makeText(this, getString(R.string.toast_sms), Toast.LENGTH_SHORT).show();
        }
    }
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_orders) {
            Intent intent = new Intent(this, OrdersListActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.send_sms) {
            Intent intent = new Intent(this, SendSmsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.share) {
            shareLatestOrder();
            return true;
        }

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
        if (id == R.id.about_me) {
            Intent intent = new Intent(this, AboutMe.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareLatestOrder() {
        Database database = new Database(this);
        Cursor cursor = database.fetchLatestOrder();

        if (cursor != null && cursor.moveToFirst()) {
            String description = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_DESCRIPTION));
            String price = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_PRICE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_DATE));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_TIME));
            String userName = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_USER_NAME));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_QUANTITY));

            String orderDetails = description + " x" + quantity + "\n" + price + "\n" + date + " --> " + time + "\n" + userName;

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, orderDetails);
            shareIntent.setType("text/plain");
            startActivity(Intent.createChooser(shareIntent, getString(R.string.order)));

            cursor.close();
        } else {
            Toast.makeText(this, getString(R.string.no_order_to_share), Toast.LENGTH_SHORT).show();
        }
    }
    private void resetSelection() {
        textViewSelectedCategory.setVisibility(View.GONE);
        textViewSelectedPrice.setVisibility(View.GONE);
        imageViewSelectedItem.setVisibility(View.GONE);
        editTextQuantity.setVisibility(View.GONE);
        findViewById(R.id.buttonConfirmQuantity).setVisibility(View.GONE);
    }
}

