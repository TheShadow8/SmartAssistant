package com.example.thanh.mobilefinal;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExercisesActivity extends AppCompatActivity {

    public static final String ID = "id";
    public static final String DESCRIPTION = "description";
    private static final String ACTIVITY_NAME = "ActivityTracking";
    private SQLiteDatabase writeableDB;
    private ArrayList<Map> exerciseList = new ArrayList();
    private Cursor cursor;
    private ProgressBar progressBar;
    private ObjectAnimator animation;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        final Intent startIntent = new Intent(this, ExercisesActivity.class);
        switch (item.getItemId()) {

            case R.id.menu_home:
                startActivity(new Intent(ExercisesActivity.this, MainActivity.class));
                return true;
            case R.id.t_help:
                AlertDialog.Builder builderHelp = new AlertDialog.Builder(this);
                builderHelp.setTitle(getResources().getString(R.string.t_help_title));
                LayoutInflater inflater = this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.fragment_exercise_tracking_help, null);
                ((TextView)dialogView.findViewById(R.id.t_help)).setMovementMethod(new ScrollingMovementMethod());
                builderHelp.setView(dialogView);
                // Add the buttons
                builderHelp.setPositiveButton(R.string.t_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        startActivity(startIntent);
                    }
                });

                // Create the AlertDialog
                AlertDialog dialogHelp = builderHelp.create();

                dialogHelp.show();
                return true;
            case R.id.t_stats:
                ExerciseTrackingStatisticsFragment statsFragment = new ExerciseTrackingStatisticsFragment();
                statsFragment.init(this.exerciseList);
                addFragment(statsFragment);
                return true;

            case R.id.menu_food:
                Intent foodIntent = new Intent(ExercisesActivity.this, FoodActivity.class );
                startActivity(foodIntent);
                Toast.makeText(this, "Nutritional information tracker clicked", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_thermostat:
                Intent thermostatIntent = new Intent(ExercisesActivity.this, HouseThermostatActivity.class );
                startActivity(thermostatIntent);
                Toast.makeText(this, "Thermostat clicked", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_automobile:
                Intent automobileIntent = new Intent(ExercisesActivity.this, AutomobileActivity.class );
                startActivity(automobileIntent);
                Toast.makeText(this, "AutomobileActivity clicked", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exercise_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        progressBar = (ProgressBar) findViewById(R.id.t_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        InitActivityTracking init = new InitActivityTracking();
        init.execute();

    }

    class InitActivityTracking extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            SystemClock.sleep(100);
            progressBar.setProgress(10);
            ExerciseTrackingDatabaseHelper dbHelper = new ExerciseTrackingDatabaseHelper(ExercisesActivity.this);
            writeableDB = dbHelper.getWritableDatabase();
            SystemClock.sleep(200);
            progressBar.setProgress(30);
            //populate activity list
            cursor = writeableDB.rawQuery("select * from " + ExerciseTrackingDatabaseHelper.TABLE_NAME,null );
            cursor.moveToFirst();
            while(!cursor.isAfterLast() ) {
                Map<String, Object> row = new HashMap<>();
                row.put(ID, cursor.getString(cursor.getColumnIndex(ExerciseTrackingDatabaseHelper.ID)));
                String type = cursor.getString(cursor.getColumnIndex(ExerciseTrackingDatabaseHelper.TYPE));
                String time = cursor.getString(cursor.getColumnIndex(ExerciseTrackingDatabaseHelper.TIME));
                String duration = cursor.getString(cursor.getColumnIndex(ExerciseTrackingDatabaseHelper.DURATION));
                String comment = cursor.getString(cursor.getColumnIndex(ExerciseTrackingDatabaseHelper.COMMENT));
                row.put(ExerciseTrackingDatabaseHelper.TYPE, type);
                row.put(ExerciseTrackingDatabaseHelper.TIME, time);
                row.put(ExerciseTrackingDatabaseHelper.DURATION, duration);
                row.put(ExerciseTrackingDatabaseHelper.COMMENT, comment);
                row.put(DESCRIPTION, getResources().getString(R.string.t_start_at) + time +", " + type +
                        getResources().getString(R.string.t_for) + duration +
                        getResources().getString(R.string.t_min_note)+ comment);

                exerciseList.add(row);
                cursor.moveToNext();
            }
            SystemClock.sleep(500);
            progressBar.setProgress(80);
            final Intent intent = new Intent(ExercisesActivity.this, ExerciseTrackingAdd.class);
            FloatingActionButton fab;
            fab = findViewById(R.id.t_newActivity);
                    fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    startActivity(intent);
                }
            });
            SystemClock.sleep(200);
            progressBar.setProgress(100);
            return null;
        }

        protected void onProgressUpdate(Integer ...values){
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.INVISIBLE );
            ExerciseTrackingListViewFragment listViewFragment = new ExerciseTrackingListViewFragment();
            listViewFragment.init(exerciseList);

            addFragment(listViewFragment);
        }
    }

    private void addFragment(Fragment fragment) {

        FragmentManager fragmentManager =getFragmentManager();
        //remove previous fragment
        if (fragmentManager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
            fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.t_listview_Frame, fragment).addToBackStack(null).commit();
    }

}
