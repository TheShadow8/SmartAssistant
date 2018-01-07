package com.example.thanh.mobilefinal;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.app.ActionBar;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FoodActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    Button addButton_food;
    Button calculateAvg;

    private ArrayList<Map> foodList = new ArrayList();
    protected static final String ACTIVITY_NAME = "food_startActivity ";
    FoodDatabase f_db;
    SQLiteDatabase sqLiteDB;
    Cursor cursor_food;
    Boolean add_clicked;
    Boolean Avg_clicked;
    public static final String ID = "id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        /*ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Gas is essential...");
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        frameLayout = findViewById(R.id.f_listview_Frame);
        addButton_food = (Button) findViewById(R.id.addButton_food);
        foodStartActivity init = new foodStartActivity();
        init.execute();
        calculateAvg = (Button) findViewById(R.id.calculateButton);
    }

    class foodStartActivity extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            SystemClock.sleep(100);

            f_db = new FoodDatabase(FoodActivity.this);
            sqLiteDB = f_db.getWritableDatabase();
            cursor_food = sqLiteDB.rawQuery("select * from " + FoodDatabase.table, null);
            cursor_food.moveToFirst();

            while (!cursor_food.isAfterLast()) {
                Map<String, String> f_infor = new HashMap<>();
                f_infor.put("id", cursor_food.getString(cursor_food.getColumnIndex(FoodDatabase.RowID)));
                f_infor.put("type", cursor_food.getString(cursor_food.getColumnIndex(FoodDatabase.food_TYPE)));
                f_infor.put("date",cursor_food.getString(cursor_food.getColumnIndex(FoodDatabase.DATE)));
                f_infor.put("time", cursor_food.getString(cursor_food.getColumnIndex(FoodDatabase.TIME)));
                f_infor.put("calories", cursor_food.getString(cursor_food.getColumnIndex(FoodDatabase.Calories)));
                f_infor.put("total_Fat", cursor_food.getString(cursor_food.getColumnIndex(FoodDatabase.Fat)));
                f_infor.put("carbohydrate", cursor_food.getString(cursor_food.getColumnIndex(FoodDatabase.Carbohydrate)));
                foodList.add(f_infor);
                cursor_food.moveToNext();
            }

            addButton_food.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View view) {
                    add_clicked = true;
                    Bundle empty_bundle = new Bundle();
                    empty_bundle.putInt("forempty", 1);

                    Intent intent = new Intent(FoodActivity.this, FoodAdd.class);
                    intent.putExtra("food_bundle", empty_bundle);
                    startActivityForResult(intent, 10);
                }
            });

            calculateAvg.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View view){
                    Avg_clicked = true;
                    Bundle empty_bundle = new Bundle();
                    empty_bundle.putInt("forempty", 1);

                    Intent intent = new Intent(FoodActivity.this, food_avg.class);
                    intent.putExtra("food_bundle", empty_bundle);
                    startActivityForResult(intent,10);

                }
            });

            SystemClock.sleep(500);
            return null;
        }

        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }


        @Override
        protected void onPostExecute(String result) {
            //progressBar.setVisibility(View.INVISIBLE);
            FoodEditFragment listViewFragment = new FoodEditFragment();
            listViewFragment.init(foodList);
            addFragment(listViewFragment);
        }
    }
    private void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
            fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.f_listview_Frame, fragment).addToBackStack(null).commit();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {

            case R.id.menu_home:
                startActivity(new Intent(FoodActivity.this, MainActivity.class));
                return true;

            case R.id.menu_exercise:
                Intent foodIntent = new Intent(FoodActivity.this, ExercisesActivity.class );
                startActivity(foodIntent);
                Toast.makeText(this, "Nutritional information tracker clicked", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_thermostat:
                Intent thermostatIntent = new Intent(FoodActivity.this, HouseThermostatActivity.class );
                startActivity(thermostatIntent);
                Toast.makeText(this, "Thermostat clicked", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_automobile:
                Intent automobileIntent = new Intent(FoodActivity.this, AutomobileActivity.class );
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

