package com.example.thanh.mobilefinal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AutomobileGasHistory extends AppCompatActivity {

    private ListView lvGasSummary;
    private ArrayList<AutomobileGasStatus> gasPurchasesPerMonth;
    private GasHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automobile_gas_history);

        /*ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("AutomobileActivity is essential...");
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        Bundle extras = getIntent().getExtras();
        Bundle data = extras.getBundle("data");

        final TextView tvNoHistory = findViewById(R.id.tvNoHistory);
        gasPurchasesPerMonth = data.getParcelableArrayList("gasPurchasesPerMonth");

        if(gasPurchasesPerMonth == null || gasPurchasesPerMonth.isEmpty()){
            tvNoHistory.setText(getResources().getString(R.string.gas_purchased_history_none));
            tvNoHistory.setVisibility(View.VISIBLE);
        }

        lvGasSummary = findViewById(R.id.lvGasSummary);
        adapter = new GasHistoryAdapter(this);
        lvGasSummary.setAdapter(adapter);


        double prevMonthGasPriceAvg = data.getDouble("prevMonthGasPriceAvg");
        final TextView tvPrevMonthAvgGasPrice = findViewById(R.id.tvPrevMonthAvgGasPrice);
        tvPrevMonthAvgGasPrice.setText(prevMonthGasPriceAvg == -1 ? "N/A"
                : String.format("$ %.2f", prevMonthGasPriceAvg));

        double prevMonthGasPriceTot = data.getDouble("prevMonthGasPriceTot");
        final TextView tvPrevMonthTotalGas = findViewById(R.id.tvPrevMonthTotalGas);
        tvPrevMonthTotalGas.setText(String.format("$ %.2f", prevMonthGasPriceTot));

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
                startActivity(new Intent(AutomobileGasHistory.this, ExercisesActivity.class));
                break;

            case R.id.menu_food:
                startActivity(new Intent(AutomobileGasHistory.this, FoodActivity.class));
                break;

            case R.id.menu_thermostat:
                startActivity(new Intent(AutomobileGasHistory.this, HouseThermostatActivity.class));
                break;

            case R.id.menu_home:
                startActivity(new Intent(AutomobileGasHistory.this, AutomobileActivity.class));
                break;

            case R.id.menu_help:
                LayoutInflater inflater = getLayoutInflater();
                LinearLayout rootView
                        = (LinearLayout) inflater.inflate(R.layout.automobile_alert_dialog, null);

                TextView tvHelpMessage = rootView.findViewById(R.id.tvAlertMessage);
                tvHelpMessage.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                tvHelpMessage.setText(getResources().getText(R.string.help_menu));

                AlertDialog.Builder builder = new AlertDialog.Builder(AutomobileGasHistory.this);
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


    private class GasHistoryAdapter extends ArrayAdapter<AutomobileGasStatus> {

        private GasHistoryAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public int getCount() {
            return gasPurchasesPerMonth.size();
        }

        @Override
        public AutomobileGasStatus getItem(int position) {
            return gasPurchasesPerMonth.get(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View view = convertView;

            if (view == null){
                LayoutInflater inflater = AutomobileGasHistory.this.getLayoutInflater();
                view = inflater.inflate(R.layout.gas_history_row, null);
            }

            LinearLayout llGasHistoryRow = view.findViewById(R.id.llGasHistoryRow);
            if ((position % 2) == 0){
                llGasHistoryRow.setBackgroundColor(getResources().getColor(R.color.colorGreenLight));
            } else {
                llGasHistoryRow.setBackgroundColor(getResources().getColor(R.color.colorBlueLight));
            }

            AutomobileGasStatus stats = getItem(position);

            TextView tvHistoryLabel = view.findViewById(R.id.tvHistoryLabel);
            tvHistoryLabel.setText(stats.getMonthYear());

            TextView tvHistoryPurchased = view.findViewById(R.id.tvHistoryPurchased);
            tvHistoryPurchased.setText(String.format("$ %.2f", stats.getTotalPurchases()));

            return view;
        }
    }

}
