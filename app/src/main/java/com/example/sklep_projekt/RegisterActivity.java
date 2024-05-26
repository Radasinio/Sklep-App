package com.example.sklep_projekt;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterActivity extends ChangeLanguage {
    EditText usernameEditText, passwordEditText, confirmPasswordEditText;
    Button registerButton;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirmPass);
        registerButton = findViewById(R.id.registerBut);
        db = new Database(this);

        registerButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();

            if (password.equals(confirmPassword)) {
                if (db.insertUser(username, password)) {
                    Toast.makeText(RegisterActivity.this, getString(R.string.toast_register), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, getString(R.string.toast_register2), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RegisterActivity.this, getString(R.string.toast_register3), Toast.LENGTH_SHORT).show();
            }
        });
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
