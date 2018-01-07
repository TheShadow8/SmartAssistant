package com.example.thanh.mobilefinal;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FoodEdit extends AppCompatActivity {

    View view;
    Button cancel, update, delete;
    EditText Edit_type, Edit_Date,Edit_Time,Edit_Calories, Edit_Total_Fat, Edit_Total_Carbohydrate;
    SQLiteDatabase writeableDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_edit);

        Edit_type = findViewById(R.id.f_type_value);
        Edit_Date = findViewById(R.id.f_date_value);
        Edit_Time = findViewById(R.id.f_time_value);
        delete = findViewById(R.id.f_delete_button_food);
        update=findViewById(R.id.updatebutton);
        cancel= findViewById(R.id.cancelButton_food);
        Edit_Calories = findViewById(R.id.f_Calories_value);
        Edit_Total_Fat = findViewById(R.id.f_Total_Fat_value);
        Edit_Total_Carbohydrate = findViewById(R.id.f_Carbohydrate_value);

        final FoodDatabase dbHelper = new FoodDatabase(this);
        writeableDB = dbHelper.getWritableDatabase();

        Bundle bundle = getIntent().getBundleExtra("bundle");
        final long rowId = bundle.getLong(FoodActivity.ID);
        String type = bundle.getString("type");
        String date = bundle.getString("date");
        String time = bundle.getString("time");
        String calories = bundle.getString("calories");
        String total_Fat = bundle.getString("total_Fat");
        String carbohydrate = bundle.getString("carbohydrate");


        Edit_type.setText(type);
        Edit_Date.setText(date);
        Edit_Time.setText(time);
        Edit_Calories.setText(calories);
        Edit_Total_Fat.setText(total_Fat);
        Edit_Total_Carbohydrate.setText(carbohydrate);

        final Intent startIntent = new Intent(this,FoodActivity.class);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(FoodEdit.this);
                builder1.setTitle(getResources().getString(R.string.f_wanna_update));
                builder1.setPositiveButton(R.string.f_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String type = Edit_type.getText().toString();
                        String date = Edit_Date.getText().toString();
                        String time =  Edit_Time .getText().toString();
                        String calories  =  Edit_Calories .getText().toString();
                        String total_Fat = Edit_Total_Fat.getText().toString();
                        String carbohydrate = Edit_Total_Carbohydrate .getText().toString();

                        dbHelper.update(rowId ,type,date,time,calories,total_Fat,carbohydrate);
                        finish();
                        startActivity(startIntent);
                    }
                });
                builder1.setNegativeButton(R.string.f_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                android.app.AlertDialog dialog1 = builder1.create();
                dialog1.show();

            }
        });
        delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodEdit.this);
                builder.setTitle(R.string.sure_to_delete);
                builder.setPositiveButton(R.string.f_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dbHelper.delete(rowId);
                        Toast toast = Toast.makeText(FoodEdit.this,
                                getResources().getString(R.string.f_delete_success), Toast.LENGTH_SHORT);
                        toast.show();
                        finish();
                        startActivity(startIntent);

                    }
                });
                builder.setNegativeButton(R.string.f_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodEdit.this);
                builder.setTitle(R.string.sure_to_cancel);
                // Add the buttons
                builder.setPositiveButton(R.string.f_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        startActivity(startIntent);
                    }
                });
                builder.setNegativeButton(R.string.f_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {

            case R.id.menu_home:
                startActivity(new Intent(FoodEdit.this, FoodActivity.class));
                return true;

            case R.id.menu_exercise:
                Intent foodIntent = new Intent(FoodEdit.this, ExercisesActivity.class );
                startActivity(foodIntent);
                Toast.makeText(this, "Nutritional information tracker clicked", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_thermostat:
                Intent thermostatIntent = new Intent(FoodEdit.this, HouseThermostatActivity.class );
                startActivity(thermostatIntent);
                Toast.makeText(this, "Thermostat clicked", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_automobile:
                Intent automobileIntent = new Intent(FoodEdit.this, AutomobileActivity.class );
                startActivity(automobileIntent);
                Toast.makeText(this, "AutomobileActivity clicked", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.food_menu, menu);
        return true;
    }
}
