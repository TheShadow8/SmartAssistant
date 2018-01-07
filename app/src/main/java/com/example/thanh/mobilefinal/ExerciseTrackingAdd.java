package com.example.thanh.mobilefinal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExerciseTrackingAdd extends AppCompatActivity {

    private SQLiteDatabase writeableDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_tracking_add);

        ExerciseTrackingDatabaseHelper dbHelper = new ExerciseTrackingDatabaseHelper(this);
        writeableDB = dbHelper.getWritableDatabase();

        Spinner spinner = findViewById(R.id.t_type_value);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.t_activities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        TextView timeView = findViewById(R.id.t_time_value);
        SimpleDateFormat format = new SimpleDateFormat("yyyy MMM dd hh:mm");
        timeView.setText(format.format(new Date()));

        Button cancel = findViewById(R.id.t_cancel_new_activity);
        final Intent startIntent = new Intent(this, ExercisesActivity.class);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ExerciseTrackingAdd.this);
                builder1.setTitle(getResources().getString(R.string.t_wanna_back));
                // Add the buttons
                builder1.setPositiveButton(R.string.t_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast toast = Toast.makeText(ExerciseTrackingAdd.this, R.string.t_cancel, Toast.LENGTH_SHORT);
                        toast.show();
                        finish();
                        startActivity(startIntent);
                    }
                });
                builder1.setNegativeButton(R.string.t_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.t_add_activity_layout), R.string.t_stay, Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }
                });
                // Create the AlertDialog
                AlertDialog dialog1 = builder1.create();

                dialog1.show();

            }
        });

        Button save = findViewById(R.id.t_new_activity);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = ((Spinner)findViewById(R.id.t_type_value)).getSelectedItem().toString();
                String time = ((TextView)findViewById(R.id.t_time_value)).getText().toString();
                String duration = ((EditText)findViewById(R.id.t_duration_value)).getText().toString();
                String comment = ((EditText)findViewById(R.id.t_comment_value)).getText().toString();

                ContentValues newData = new ContentValues();
                newData.put(ExerciseTrackingDatabaseHelper.TYPE, type);
                newData.put(ExerciseTrackingDatabaseHelper.TIME, time);
                newData.put(ExerciseTrackingDatabaseHelper.DURATION, duration);
                newData.put(ExerciseTrackingDatabaseHelper.COMMENT, comment);
                writeableDB.insert(ExerciseTrackingDatabaseHelper.TABLE_NAME,"", newData);
                Toast toast = Toast.makeText(ExerciseTrackingAdd.this,
                        getResources().getString(R.string.t_act_save_success), Toast.LENGTH_SHORT);
                toast.show();
                finish();
                startActivity(startIntent);
            }
        });
    }
}
