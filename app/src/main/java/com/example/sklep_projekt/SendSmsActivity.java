package com.example.sklep_projekt;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SendSmsActivity extends ChangeLanguage {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 2;
    private TextView textViewOrderDetails;
    private EditText editTextPhoneNumber;
    private Button buttonSendSMS;
    private Database databaseHelper;
    private String phoneNumber = "";
    private String text = "";
    private SmsManager smsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);

        loadLocale();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, MY_PERMISSIONS_REQUEST_RECEIVE_SMS);
        }

        textViewOrderDetails = findViewById(R.id.textViewOrderDetails);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        buttonSendSMS = findViewById(R.id.buttonSendSMS);
        databaseHelper = new Database(this);

        loadLatestOrder();

        buttonSendSMS.setOnClickListener(v -> sendSMS());
    }

    private void loadLatestOrder() {
        Cursor cursor = databaseHelper.fetchLatestOrder();
        if (cursor != null && cursor.moveToFirst()) {
            String description = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_DESCRIPTION));
            String price = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_PRICE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_DATE));
            String userName = cursor.getString(cursor.getColumnIndexOrThrow(Database.COLUMN_USER_NAME));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(Database.COLUMN_QUANTITY));

            String orderDetails =
                    description + " x" + quantity + ", " +
                            price + ", " + date + " -> " + userName;
            textViewOrderDetails.setText(orderDetails);
            cursor.close();
        } else {
            Toast.makeText(this, getString(R.string.toast_sms), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermission(String permission) {
        int result = ContextCompat.checkSelfPermission(this, permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void sendSMS() {
        if (checkPermission(Manifest.permission.SEND_SMS)) {
            phoneNumber = editTextPhoneNumber.getText().toString();
            text = textViewOrderDetails.getText().toString();

            if (!phoneNumber.equals("") && !text.equals("")) {
                smsManager = SmsManager.getDefault();

                try {
                    smsManager.sendTextMessage(
                            phoneNumber,
                            null,
                            text,
                            null,
                            null
                    );

                    Toast.makeText(this, getString(R.string.toast_sms2), Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(this, getString(R.string.toast_sms3), Toast.LENGTH_SHORT).show();

                }
            } else {
                Toast.makeText(this, getString(R.string.toast_sms4), Toast.LENGTH_SHORT).show();
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
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
