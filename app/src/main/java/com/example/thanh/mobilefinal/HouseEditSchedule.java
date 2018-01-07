package com.example.thanh.mobilefinal;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class HouseEditSchedule extends AppCompatActivity {

    Button btnSaveasnew;
    Button btnSave;
    EditText newSch;
    EditText newTime;
    EditText newTem;
    protected static final String ACTIVITY_NAME = "edit_schedule";
    HouseTempDatabaseHelper db;
    private String selectedDay;
    Spinner spinner1;
    private int selectedID;
    private String selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_edit_schedule);

        newSch = (EditText) findViewById(R.id.newDay) ;
        newTime = (EditText) findViewById(R.id.newTime) ;
        newTem = (EditText) findViewById(R.id.newTem) ;
        btnSaveasnew = (Button) findViewById(R.id.saveasnew);
        db = new HouseTempDatabaseHelper(this);
        btnSave = (Button) findViewById(R.id.save);

        spinner1 = (Spinner) findViewById(R.id.spinner1) ;
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(HouseEditSchedule.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.days));
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner1.setAdapter(spAdapter);

        Intent receivedIntent = getIntent();
        selectedID = receivedIntent.getIntExtra("id",-1);
        selectedDay = receivedIntent.getStringExtra("newDay");

        newSch.setText(selectedDay);

        newTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(HouseEditSchedule.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        newTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        btnSaveasnew.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String inDay = spinner1.getSelectedItem().toString();
                String inTime = newTime.getText().toString();
                String inTemp = newTem.getText().toString();
                String sch = inDay + " " + inTime + " " + inTemp + "C\u00b0";
                if ((!inDay.equals("")) && (!inTime.equals("")) && (!inTemp.equals(""))) {


                    db.insertData(inDay, inTime, inTemp, sch);
                    newTime.setText(" ");
                    newTem.setText(" ");

                    Toast.makeText(HouseEditSchedule.this, "New Schedule Added", Toast.LENGTH_LONG).show();
                    Intent editScreen = new Intent(HouseEditSchedule.this, HouseThermostatActivity.class);
                    startActivity(editScreen);
                }   else {
                    Toast.makeText(HouseEditSchedule.this, "Must select day, time and temperature", Toast.LENGTH_LONG).show();
                }
            }

        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String inDay = spinner1.getSelectedItem().toString();
                String inTime = newTime.getText().toString();
                String inTemp = newTem.getText().toString();
                if ((!inDay.equals("")) && (!inTime.equals("")) && (!inTemp.equals(""))) {
                    String sch = inDay + " " + inTime + " " + inTemp + "C\u00b0";
                    db.updateData(sch, selectedID, selectedDay);
                    Toast.makeText(HouseEditSchedule.this, "Schedule Saved", Toast.LENGTH_LONG).show();
                    Intent editScreen = new Intent(HouseEditSchedule.this, HouseThermostatActivity.class);
                    startActivity(editScreen);
                } else {
                    Toast.makeText(HouseEditSchedule.this, "Must select day, time and temperature", Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {

            case R.id.menu_home:
                startActivity(new Intent(HouseEditSchedule.this, HouseThermostatActivity.class));
                return true;

            case R.id.menu_exercise:
                Intent foodIntent = new Intent(HouseEditSchedule.this, ExercisesActivity.class );
                startActivity(foodIntent);
                Toast.makeText(this, "Nutritional information tracker clicked", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_food:
                Intent thermostatIntent = new Intent(HouseEditSchedule.this, FoodActivity.class );
                startActivity(thermostatIntent);
                Toast.makeText(this, "Thermostat clicked", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_automobile:
                Intent automobileIntent = new Intent(HouseEditSchedule.this, AutomobileActivity.class );
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
