package com.example.sklep_projekt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class AboutMe extends ChangeLanguage {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        loadLocale();

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
