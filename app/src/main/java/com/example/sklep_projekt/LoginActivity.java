package com.example.sklep_projekt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends ChangeLanguage {
    EditText usernameEditText, passwordEditText;
    Button loginButton;
    Database databaseHelper;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        databaseHelper = new Database(this);
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (databaseHelper.checkUser(username, password)) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("logged_user", username);
                editor.apply();
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, getString(R.string.toast_login), Toast.LENGTH_SHORT).show();
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
