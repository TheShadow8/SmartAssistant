package com.example.thanh.mobilefinal;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    LinearLayout llActivities, llNutrition, llThermostat, llAutomobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Welcome");
        actionBar.setDisplayShowCustomEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.welcome, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        llActivities = findViewById(R.id.llActivities);
        llActivities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exercisesIntent = new Intent(MainActivity.this, ExercisesActivity.class );
                startActivity(exercisesIntent);
            }
        });

        llNutrition = findViewById(R.id.llNutrition);
        llNutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FoodActivity.class );
                startActivity(intent);
            }
        });

        llThermostat = findViewById(R.id.llThermostat);
        llThermostat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HouseThermostatActivity.class );
                startActivity(intent);
            }
        });

        llAutomobile = findViewById(R.id.llAutomobile);
        llAutomobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent automobileIntent = new Intent(MainActivity.this, AutomobileActivity.class );
                startActivity(automobileIntent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_exercise:
                Intent exercisesIntent = new Intent(MainActivity.this, ExercisesActivity.class );
                startActivity(exercisesIntent);
                Toast.makeText(this, "Exercise Tracking clicked", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_food:
                Intent foodIntent = new Intent(MainActivity.this, FoodActivity.class );
                startActivity(foodIntent);
                Toast.makeText(this, "Nutritional information tracker clicked", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_thermostat:
                Intent thermostatIntent = new Intent(MainActivity.this, HouseThermostatActivity.class );
                startActivity(thermostatIntent);
                Toast.makeText(this, "Thermostat clicked", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_automobile:
                Intent automobileIntent = new Intent(MainActivity.this, AutomobileActivity.class );
                startActivity(automobileIntent);
                Toast.makeText(this, "AutomobileActivity clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
