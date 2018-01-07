package com.example.thanh.mobilefinal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AutomobileEditGasDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automobile_edit_gas_details);

        /*ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("AutomobileActivity is essential...");
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        Bundle fragmentDetails = getIntent().getExtras();
        if (fragmentDetails != null){

            // add title of the fragment and button text
            fragmentDetails.putString("btnText", getResources().getString(R.string.btn_save_details));
            fragmentDetails.putString("fragmentTitle", getResources().getString(R.string.edit_gas_title));

            AutomobileGasDetailsFragment loadedFragment = new AutomobileGasDetailsFragment();
            loadedFragment.setArguments(fragmentDetails);

            getFragmentManager().beginTransaction().
                    replace(R.id.flEditGasDetails, loadedFragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.auto_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch(menuItem.getItemId()){

            case R.id.menu_exercise:
                startActivity(new Intent(AutomobileEditGasDetails.this, ExercisesActivity.class));
                break;

            case R.id.menu_food:
                startActivity(new Intent(AutomobileEditGasDetails.this, FoodActivity.class));
                break;

            case R.id.menu_thermostat:
                startActivity(new Intent(AutomobileEditGasDetails.this, HouseThermostatActivity.class));
                break;

            case R.id.menu_home:
                startActivity(new Intent(AutomobileEditGasDetails.this, AutomobileActivity.class));
                break;

            case R.id.menu_help:
                LayoutInflater inflater = getLayoutInflater();
                LinearLayout rootView
                        = (LinearLayout) inflater.inflate(R.layout.automobile_alert_dialog, null);

                TextView tvHelpMessage = rootView.findViewById(R.id.tvAlertMessage);
                tvHelpMessage.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                tvHelpMessage.setText(getResources().getText(R.string.help_menu));

                AlertDialog.Builder builder = new AlertDialog.Builder(AutomobileEditGasDetails.this);
                builder.setView(rootView);
                builder.setPositiveButton(getResources().getString(R.string.done),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }
                );

                AlertDialog alert = builder.create();
                alert.show();
                break;
        }
        return true;
    }



    public void updateGasDetail(Bundle gasDetails){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("gasDetails", gasDetails);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

}
