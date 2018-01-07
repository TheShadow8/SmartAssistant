package com.example.thanh.mobilefinal;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class HouseThermostatActivity extends AppCompatActivity {

    Button btnTimePicker;
    Button btnView;
    Button btnSentSetting;
    EditText txtTime;
    Spinner spinner;
    EditText txtTemperatue;
    private ListView listView;
    public ArrayList<String> timeTempArray;
    protected static final String ACTIVITY_NAME = "HouseThermostatActivity";
    private HouseTempDatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_thermostat);

        btnTimePicker = (Button) findViewById(R.id.btn_time);
        txtTime = (EditText) findViewById(R.id.in_time);

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(HouseThermostatActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.days));
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(spAdapter);


        txtTemperatue = (EditText) findViewById(R.id.input_temp);
        btnSentSetting = (Button) findViewById(R.id.set);
        btnView = (Button) findViewById(R.id.viewsch);

        dbHelper = new HouseTempDatabaseHelper(this);
        database = dbHelper.getWritableDatabase();


        btnSentSetting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String inDay = spinner.getSelectedItem().toString();
                String inTime = txtTime.getText().toString();
                String inTemp = txtTemperatue.getText().toString();
                String sch = inDay + " " + inTime + " " + inTemp + "C\u00b0";
                if ((!inDay.equals("")) && (!inTime.equals("")) && (!inTemp.equals(""))) {

                    dbHelper.insertData(inDay, inTime, inTemp, sch);
                    txtTime.setText(" ");
                    txtTemperatue.setText(" ");
                }

            }

        });

        btnView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent editScreen = new Intent(HouseThermostatActivity.this, HouseScheduleListView.class);
                startActivity(editScreen);
            }


        });

        //timepicker

        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(HouseThermostatActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        txtTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(HouseThermostatActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        txtTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

    }
    /**
     *The activity is finishing or has been destroyed by system
     */

    public void onDestroy() {

        if (cursor != null) {
            cursor.close();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
        database.close();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {

            case R.id.menu_home:
                startActivity(new Intent(HouseThermostatActivity.this, MainActivity.class));
                return true;

            case R.id.menu_exercise:
                Intent foodIntent = new Intent(HouseThermostatActivity.this, ExercisesActivity.class );
                startActivity(foodIntent);
                Toast.makeText(this, "Nutritional information tracker clicked", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_food:
                Intent thermostatIntent = new Intent(HouseThermostatActivity.this, FoodActivity.class );
                startActivity(thermostatIntent);
                Toast.makeText(this, "Thermostat clicked", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_automobile:
                Intent automobileIntent = new Intent(HouseThermostatActivity.this, AutomobileActivity.class );
                startActivity(automobileIntent);
                Toast.makeText(this, "AutomobileActivity clicked", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.house_thermostat_menu, menu);
        return true;
    }

}
