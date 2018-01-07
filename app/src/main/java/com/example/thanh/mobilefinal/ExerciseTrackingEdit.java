package com.example.thanh.mobilefinal;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ExerciseTrackingEdit extends AppCompatActivity {

    private SQLiteDatabase writeableDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_tracking_edit);

        final ExerciseTrackingDatabaseHelper dbHelper = new ExerciseTrackingDatabaseHelper(this);
        writeableDB = dbHelper.getWritableDatabase();

        Bundle bundle = getIntent().getBundleExtra("bundle");
        final long rowId = bundle.getLong(ExercisesActivity.ID);
        String type = bundle.getString(ExerciseTrackingDatabaseHelper.TYPE);
        String time = bundle.getString(ExerciseTrackingDatabaseHelper.TIME);
        String duration = bundle.getString(ExerciseTrackingDatabaseHelper.DURATION);
        String comment = bundle.getString(ExerciseTrackingDatabaseHelper.COMMENT);

        final Spinner spinner = (Spinner)findViewById(R.id.t_type_value);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.t_activities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(this.getPosition(type));

        final EditText timeView = (EditText)findViewById(R.id.t_time_value);
        timeView.setText(time);
        final EditText durationView = (EditText)findViewById(R.id.t_duration_value);
        durationView.setText(duration);
        final EditText commentView = (EditText)findViewById(R.id.t_comment_value);
        commentView.setText(comment);


        final Intent startIntent = new Intent(this, ExercisesActivity.class);
        Button update = findViewById(R.id.t_button_update_activity);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ExerciseTrackingEdit.this);
                builder1.setTitle(getResources().getString(R.string.t_wanna_update));
                // Add the buttons
                builder1.setPositiveButton(R.string.t_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //update value
                        String type = spinner.getSelectedItem().toString();
                        String time = timeView.getText().toString();
                        String duration = durationView.getText().toString();
                        String comment = commentView.getText().toString();

                        ContentValues newData = new ContentValues();
                        newData.put(ExerciseTrackingDatabaseHelper.TYPE, type);
                        newData.put(ExerciseTrackingDatabaseHelper.TIME, time);
                        newData.put(ExerciseTrackingDatabaseHelper.DURATION, duration);
                        newData.put(ExerciseTrackingDatabaseHelper.COMMENT, comment);
                        writeableDB.update(ExerciseTrackingDatabaseHelper.TABLE_NAME, newData, ExerciseTrackingDatabaseHelper.ID + "=" + rowId, null);

                        Toast toast = Toast.makeText(ExerciseTrackingEdit.this,
                                getResources().getString(R.string.t_act_update_success), Toast.LENGTH_SHORT);
                        toast.show();
                        finish();
                        startActivity(startIntent);
                    }
                });
                builder1.setNegativeButton(R.string.t_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                // Create the AlertDialog
                AlertDialog dialog1 = builder1.create();

                dialog1.show();

            }
        });

        Button delete = findViewById(R.id.t_button_delete_activity);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builderDelete = new AlertDialog.Builder(ExerciseTrackingEdit.this);
                builderDelete.setTitle(getResources().getString(R.string.t_wanna_delete));
                // Add the buttons
                builderDelete.setPositiveButton(R.string.t_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //delete value
//                        this line delete all: writeableDB.delete(dbHelper.TABLE_NAME, null, null);
                        writeableDB.delete(dbHelper.TABLE_NAME, dbHelper.ID + "=" + rowId, null);
                        Toast toast = Toast.makeText(ExerciseTrackingEdit.this,
                                getResources().getString(R.string.t_act_delete_success), Toast.LENGTH_SHORT);
                        toast.show();
                        finish();
                        startActivity(startIntent);
                    }
                });
                builderDelete.setNegativeButton(R.string.t_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                // Create the AlertDialog
                AlertDialog dialogDelete = builderDelete.create();

                dialogDelete.show();

            }
        });

        Button cancel = findViewById(R.id.t_buttone_cancel_edit_activity);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(startIntent);
            }
        });
    }


    private int getPosition(String type) {
        switch (type) {
            case "Running":
                return 0;
            case "Walking":
                return 1;
            case "Biking":
                return 2;
            case "Swimming":
                return 3;
            case "Skating":
                return 4;
        }
        return 0;
    }

}
