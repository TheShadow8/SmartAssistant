package com.example.thanh.mobilefinal;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FoodAdd extends AppCompatActivity {

    EditText Edittype,Editdate,Edittime,Editcarlories,Edittotal_Fat,Editcarbohydrate;
    Button save,cancel;
    private SQLiteDatabase writeableDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_add);

        Edittype=(EditText) findViewById(R.id.f_type_value);
        Editdate=(EditText) findViewById(R.id.f_date_value);
        Edittime=(EditText) findViewById(R.id.f_time_value);
        Editcarlories=(EditText) findViewById(R.id.f_Calories_value);
        Edittotal_Fat=(EditText) findViewById(R.id.f_Total_Fat_value);
        Editcarbohydrate=(EditText) findViewById(R.id.f_Carbohydrate_value);
        SimpleDateFormat format = new SimpleDateFormat("yyyy MMM dd");
        Editdate.setText(format.format(new Date()));
        SimpleDateFormat f2 = new SimpleDateFormat("hh:mm");
        Edittime.setText(f2.format(new Date()));

        save = (Button) findViewById(R.id.savebutton_add);
        cancel= (Button) findViewById(R.id.cancelButton_add);


        FoodDatabase f_db = new FoodDatabase(this);
        writeableDB = f_db.getWritableDatabase();


        final Intent intent = new Intent(this, FoodActivity.class );
        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                String type= Edittype.getText().toString();
                String date= Editdate.getText().toString();
                String time =Edittime.getText().toString();
                String calories=Editcarlories.getText().toString();
                String total_Fat=Edittotal_Fat.getText().toString();
                String carbohydrate = Editcarbohydrate.getText().toString();

                ContentValues newData = new ContentValues();
                newData.put(FoodDatabase.food_TYPE, type);
                newData.put(FoodDatabase.DATE, date);
                newData.put(FoodDatabase.TIME, time);
                newData.put(FoodDatabase.Calories, calories);
                newData.put(FoodDatabase.Fat, total_Fat);
                newData.put(FoodDatabase.Carbohydrate, carbohydrate);
                writeableDB.insert(FoodDatabase.table, "" , newData);

                Toast t = Toast.makeText(getApplicationContext(), R.string.f_saveConfirm, Toast.LENGTH_LONG);
                t.show();
                finish();
                startActivity(intent);

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(FoodAdd.this);
                builder.setTitle(R.string.sure_to_cancel);
                builder.setPositiveButton(R.string.f_ok, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.f_no, new DialogInterface.OnClickListener() {
                    @Override
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
        final Intent startIntent = new Intent(this, FoodActivity.class);
        switch (item.getItemId()) {

            case R.id.menu_home:
                startActivity(new Intent(FoodAdd.this, FoodActivity.class));
                return true;

            case R.id.menu_exercise:
                Intent foodIntent = new Intent(FoodAdd.this, ExercisesActivity.class );
                startActivity(foodIntent);
                Toast.makeText(this, "Nutritional information tracker clicked", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_thermostat:
                Intent thermostatIntent = new Intent(FoodAdd.this, HouseThermostatActivity.class );
                startActivity(thermostatIntent);
                Toast.makeText(this, "Thermostat clicked", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_automobile:
                Intent automobileIntent = new Intent(FoodAdd.this, AutomobileActivity.class );
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

