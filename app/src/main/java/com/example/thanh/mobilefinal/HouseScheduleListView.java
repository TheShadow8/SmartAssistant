package com.example.thanh.mobilefinal;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class HouseScheduleListView extends AppCompatActivity {


    Button btnSaveasnew;
    Button btnSave;
    EditText newDay;
    EditText newTime;
    EditText newTem;
    ListView listView;
    protected static final String ACTIVITY_NAME = "edit_schedule";
    HouseTempDatabaseHelper db;
    private String selectedDay;
    private String selectedTime;
    private String selectedTem;
    private int selectedID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_schedule_list_view);

        // final Cursor data = db.getData();
        listView = (ListView) findViewById(R.id.list_view);
        db = new HouseTempDatabaseHelper(this);
        populateListview();
    }

    private void populateListview() {
        final Cursor data = db.getData();
        ArrayList<String> listData = new ArrayList<>();
        while (data.moveToNext()){
            listData.add(data.getString(4));
        }
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String newDay = parent.getItemAtPosition(position).toString();

                Cursor data = db.getItemID(newDay);
                int itemID = -1;
                while (data.moveToNext()) {
                    itemID = data.getInt(0);

                }
                if (itemID > -1) {
                    Intent editScreen = new Intent(HouseScheduleListView.this, HouseEditSchedule.class);
                    editScreen.putExtra("id", itemID);
                    editScreen.putExtra("newDay", newDay);
                    //   editScreen.putExtra("newTime", newTime);
                    //  editScreen.putExtra("newTem", newTem);
                    startActivity(editScreen);
                } else {
                    Toast.makeText(HouseScheduleListView.this, "No Data", Toast.LENGTH_LONG).show();
                }

            }



        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {

            case R.id.menu_home:
                startActivity(new Intent(HouseScheduleListView.this, HouseThermostatActivity.class));
                return true;

            case R.id.menu_exercise:
                Intent foodIntent = new Intent(HouseScheduleListView.this, ExercisesActivity.class );
                startActivity(foodIntent);
                Toast.makeText(this, "Nutritional information tracker clicked", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_food:
                Intent thermostatIntent = new Intent(HouseScheduleListView.this, FoodActivity.class );
                startActivity(thermostatIntent);
                Toast.makeText(this, "Thermostat clicked", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_automobile:
                Intent automobileIntent = new Intent(HouseScheduleListView.this, AutomobileActivity.class );
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
