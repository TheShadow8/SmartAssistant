package com.example.thanh.mobilefinal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AutomobileActivity extends AppCompatActivity {

    public static final DateFormat DD_MM_YYYY = new SimpleDateFormat("dd/MM/yyyy", Locale.CANADA);

    public static final int ADD_DETAILS_REQUEST = 1;
    public static final int EDIT_DETAILS_REQUEST = 2;

    private static final String AVERAGE = "average";
    private static final String TOTAL = "total";

    private AutomobileGasDetailsFragment loadedFragment = null;
    private boolean frameLayoutExists;
    private View parentLayout;
    private ListView lvGasPurchase;

    private GridLayout glLoading;
    private ProgressBar pbLoadGasDetails;
    private TextView tvLoadingPercentage;


    Button btnAddPurchase;
    Button btnHistory;

    private ArrayList<AutomobileGasDetails> gasDetailsList;
    private GasDetailsAdapter adapter;
    private AutomobileDatabaseHelper automobileDbHelper;
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automobile);

        /*ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Gas is essential...");
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        automobileDbHelper = new AutomobileDatabaseHelper(AutomobileActivity.this);
        db = automobileDbHelper.getWritableDatabase();

        frameLayoutExists = (findViewById(R.id.flGasDetailsHolder) != null);
        parentLayout = findViewById(R.id.gasDetailsParent);
        lvGasPurchase = (ListView)findViewById(R.id.lvGasPurchase);


        pbLoadGasDetails = findViewById(R.id.pbLoadGasDetails);
        glLoading = findViewById(R.id.glLoading);
        tvLoadingPercentage = findViewById(R.id.tvLoadingPercentage);


        btnAddPurchase=(Button)findViewById(R.id.btnAddPurchase);
        btnAddPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // landscape orientation
                if (frameLayoutExists){
                    AutomobileGasDetailsFragment addFragment = new AutomobileGasDetailsFragment();
                    Bundle fragmentDetails = new Bundle();
                    fragmentDetails.putString("btnText", getResources().getString(R.string.btn_add));
                    fragmentDetails.putString("fragmentTitle", getResources().getString(R.string.add_gas_title));
                    fragmentDetails.putBoolean("isLandscape", true);

                    //AutomobileGasDetailsFragment addFragment = new AutomobileGasDetailsFragment();
                    addFragment.setArguments(fragmentDetails);


                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.flGasDetailsHolder, addFragment);
                    ft.addToBackStack("A string");
                    ft.commit();
                }

                // portrait orientation
                else {
                    Intent intent = new Intent(AutomobileActivity.this,
                            AutomobileAddGasDetails.class);
                    startActivityForResult(intent, ADD_DETAILS_REQUEST);
                }
            }
        });

        btnHistory = (Button) findViewById(R.id.btnHistory);
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AutomobileActivity.this,
                        AutomobileGasHistory.class);

                Bundle data = new Bundle();
                data.putParcelableArrayList("gasPurchasesPerMonth", getPrevGasPurchasesByMonth());
                data.putDouble("prevMonthGasPriceAvg", getPrevMonthGasStat(AVERAGE));
                data.putDouble("prevMonthGasPriceTot", getPrevMonthGasStat(TOTAL));

                intent.putExtra("data", data);
                startActivity(intent);
            }
        });

        lvGasPurchase = (ListView)findViewById(R.id.lvGasPurchase);
        lvGasPurchase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                AutomobileGasDetails details = gasDetailsList.get(position);
                Bundle gasDetails = new Bundle();
                gasDetails.putDouble("price", details.getPrice());
                gasDetails.putDouble("litres", details.getLitres());
                gasDetails.putDouble("kilometers", details.getKilometers());
                gasDetails.putLong("date", details.getDate().getTime());
                gasDetails.putLong("id", id);
                gasDetails.putInt("position", position);

                // the device is in landscape mode
                if (frameLayoutExists){
                    Bundle fragmentDetails = new Bundle();
                    fragmentDetails.putString("btnText", getResources().getString(R.string.btn_save_details));
                    fragmentDetails.putString("fragmentTitle", getResources().getString(R.string.edit_gas_title));
                    fragmentDetails.putBundle("gasDetails", gasDetails);

                    AutomobileGasDetailsFragment editFragment = new AutomobileGasDetailsFragment();
                    editFragment.setArguments(fragmentDetails);

                    // cache the fragment so it can be removed
                    loadedFragment = editFragment;

                    getFragmentManager().beginTransaction()
                            .replace(R.id.flGasDetailsHolder, editFragment).commit();
                }

                // the device is in portrait mode
                else {
                    Intent intent = new Intent(AutomobileActivity.this, AutomobileEditGasDetails.class);
                    intent.putExtra("gasDetails", gasDetails);
                    startActivityForResult(intent, EDIT_DETAILS_REQUEST);
                }
            }
        });



        gasDetailsList = new ArrayList<>();
        adapter = new GasDetailsAdapter(this);
        lvGasPurchase.setAdapter(adapter);

        new DataBaseQuery().execute();

    }

    private ArrayList<AutomobileGasStatus> getPrevGasPurchasesByMonth(){
        ArrayList<AutomobileGasStatus> purchases = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        cursor = db.rawQuery(AutomobileDatabaseHelper.SELECT_ALL_SQL, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) {
            long longDate = cursor.getLong(cursor.getColumnIndex(AutomobileDatabaseHelper.KEY_DATE));
            calendar.setTime(new Date(longDate));
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);

            String monthYear = getResources().getStringArray(R.array.months)[month] + " " + String.valueOf(year);

            double purchasePrice = cursor.getDouble(cursor.getColumnIndex(AutomobileDatabaseHelper.KEY_PRICE))
                    * cursor.getDouble(cursor.getColumnIndex(AutomobileDatabaseHelper.KEY_LITRES));

            AutomobileGasStatus status;

            // no entries or current monthYear is different from the last
            if (purchases.isEmpty() || !purchases.get(purchases.size()-1).getMonthYear().equals(monthYear)){
                status = new AutomobileGasStatus(monthYear, purchasePrice);
                purchases.add(status);
            } else {
                status = purchases.get(purchases.size()-1);
                status.setTotalPurchases(status.getTotalPurchases() + purchasePrice);
            }

            cursor.moveToNext();
        }
        return purchases;
    }

    private double getPrevMonthGasStat(String stat){
        String table = AutomobileDatabaseHelper.GAS_DETAILS_TABLE;
        String where = AutomobileDatabaseHelper.KEY_DATE + " >= ? AND " + AutomobileDatabaseHelper.KEY_DATE + " <= ?";
        String[] whereArgs = {
                String.valueOf(getFirstTimestampOfPrevMonth()),
                String.valueOf(getLastTimestampOfPrevMonth())
        };

        cursor = db.query(table, null, where, whereArgs, null, null, null);
        cursor.moveToFirst();

        double gasPriceSum = 0;
        double totalPrice = 0;

        while(!cursor.isAfterLast()){
            double gasPrice = cursor.getDouble(cursor.getColumnIndex(AutomobileDatabaseHelper.KEY_PRICE));
            double litres = cursor.getDouble(cursor.getColumnIndex(AutomobileDatabaseHelper.KEY_LITRES));

            gasPriceSum += gasPrice;
            totalPrice += (gasPrice * litres);
            cursor.moveToNext();
        }

        if (stat.equals(AVERAGE) && cursor.getCount() != 0){
            return (gasPriceSum / cursor.getCount());
        }
        else if (stat.equals(TOTAL)){
            return totalPrice;
        }
        return -1; // no results
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.auto_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
        automobileDbHelper.close();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (loadedFragment != null){
            getFragmentManager().beginTransaction().remove(loadedFragment).commit();
        }
        super.onConfigurationChanged(newConfig);
        startActivity(new Intent(this, AutomobileActivity.class));
    }

   @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch(menuItem.getItemId()){

            case R.id.menu_exercise:
                startActivity(new Intent(AutomobileActivity.this, ExercisesActivity.class));
                Toast.makeText(this, "Exercise Tracking clicked", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_food:
                startActivity(new Intent(AutomobileActivity.this, FoodActivity.class));
                Toast.makeText(this, "Nutritional information tracker clicked", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_thermostat:
                startActivity(new Intent(AutomobileActivity.this, HouseThermostatActivity.class));
                Toast.makeText(this, "Thermostat clicked", Toast.LENGTH_SHORT).show();
                break;

           case R.id.menu_home:
                startActivity(new Intent(AutomobileActivity.this, MainActivity.class));
                break;

            case R.id.menu_help:
                LayoutInflater inflater = getLayoutInflater();
                LinearLayout rootView
                        = (LinearLayout) inflater.inflate(R.layout.automobile_alert_dialog, null);

                TextView tvHelpMessage = rootView.findViewById(R.id.tvAlertMessage);
                tvHelpMessage.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                tvHelpMessage.setText(getResources().getText(R.string.help_menu));

                AlertDialog.Builder builder = new AlertDialog.Builder(AutomobileActivity.this);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if (resultCode == Activity.RESULT_OK){

            if (requestCode == EDIT_DETAILS_REQUEST){
                Bundle extras = data.getExtras();
                Bundle gasDetails = extras.getBundle("gasDetails");
                updateGasDetail(gasDetails);
            }

            if (requestCode == ADD_DETAILS_REQUEST){
                Bundle extras = data.getExtras();
                Bundle gasDetails = extras.getBundle("gasDetails");
                addGasDetail(gasDetails);
            }
        }
    }

    private class DataBaseQuery extends AsyncTask<String, Integer, ArrayList<AutomobileGasDetails>> {

        @Override
        protected ArrayList<AutomobileGasDetails> doInBackground(String[] args){

            //testFillDB();
            cursor = db.rawQuery(AutomobileDatabaseHelper.SELECT_ALL_SQL, null);

            // build an array list in the background and pass it back to the GUI thread
            //  after the resource intense processing is complete
            ArrayList<AutomobileGasDetails> detailsList = new ArrayList<>();

            // used double to perform division and display overall progress
            double totalRecords = cursor.getCount();
            double counter = 0;

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                try {
                    double price = cursor.getDouble(cursor.getColumnIndex(AutomobileDatabaseHelper.KEY_PRICE));
                    double litres = cursor.getDouble(cursor.getColumnIndex(AutomobileDatabaseHelper.KEY_LITRES));
                    double kilometers = cursor.getDouble(cursor.getColumnIndex(AutomobileDatabaseHelper.KEY_KILOMETERS));
                    long longDateRepresentation = cursor.getLong(cursor.getColumnIndex(AutomobileDatabaseHelper.KEY_DATE));
                    Date date = new Date(longDateRepresentation);

                    AutomobileGasDetails details = new AutomobileGasDetails(price, litres, kilometers, date);
                    detailsList.add(details);

                    Integer progress = (int )Math.round((++counter / totalRecords) * 100);
                    publishProgress(progress);

                    Thread.sleep(100);

                    cursor.moveToNext();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return detailsList;
        }

        @Override
        protected void onProgressUpdate(Integer[] value){
            int progress = value[0];

            glLoading.setVisibility(View.VISIBLE);
            tvLoadingPercentage.setText(String.valueOf(progress).concat("%"));
            pbLoadGasDetails.setProgress(progress);
        }

        @Override
        protected void onPostExecute(ArrayList<AutomobileGasDetails> details){
            gasDetailsList.clear();
            gasDetailsList.addAll(details);
            adapter.notifyDataSetChanged();
            glLoading.setVisibility(View.GONE);
            lvGasPurchase.setVisibility(View.VISIBLE);
        }
    }


    protected void updateGasDetail(Bundle gasDetails){

        if (gasDetails != null){
            double price = gasDetails.getDouble("price");
            double litres = gasDetails.getDouble("litres");
            double kilometers = gasDetails.getDouble("kilometers");
            long longDate = gasDetails.getLong("date");
            long id = gasDetails.getLong("id");
            int position = gasDetails.getInt("position");

            ContentValues contentValues = new ContentValues();
            contentValues.put(AutomobileDatabaseHelper.KEY_PRICE, price);
            contentValues.put(AutomobileDatabaseHelper.KEY_LITRES, litres);
            contentValues.put(AutomobileDatabaseHelper.KEY_KILOMETERS, kilometers);
            contentValues.put(AutomobileDatabaseHelper.KEY_DATE, longDate);

            db.update(AutomobileDatabaseHelper.GAS_DETAILS_TABLE,
                    contentValues,
                    AutomobileDatabaseHelper.KEY_ID + "=" + id,
                    null);

            AutomobileGasDetails agd = new AutomobileGasDetails(price, litres, kilometers, new Date(longDate));
            gasDetailsList.set(position, agd);
            adapter.notifyDataSetChanged();
            Toast.makeText(AutomobileActivity.this,
                    getResources().getString(R.string.toast_details_saved),
                    Toast.LENGTH_LONG).show();
        }
    }

    protected void addGasDetail(Bundle gasDetails) {
        if (gasDetails != null) {
            double price = gasDetails.getDouble("price");
            double litres = gasDetails.getDouble("litres");
            double kilometers = gasDetails.getDouble("kilometers");
            long longDate = gasDetails.getLong("date");

            ContentValues contentValues = new ContentValues();
            contentValues.put(AutomobileDatabaseHelper.KEY_PRICE, price);
            contentValues.put(AutomobileDatabaseHelper.KEY_LITRES, litres);
            contentValues.put(AutomobileDatabaseHelper.KEY_KILOMETERS, kilometers);
            contentValues.put(AutomobileDatabaseHelper.KEY_DATE, longDate);

            db.insert(AutomobileDatabaseHelper.GAS_DETAILS_TABLE,
                    "",
                    contentValues);

            AutomobileGasDetails agd = new AutomobileGasDetails(price, litres, kilometers, new Date(longDate));
            gasDetailsList.add(agd);
            adapter.notifyDataSetChanged();
            Toast.makeText(AutomobileActivity.this,
                    getResources().getString(R.string.toast_details_added),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void deleteGasDetail(long id, int position){

        // remove fragment (edit / add details) if it exsits
        if (loadedFragment != null){
            getFragmentManager().beginTransaction().remove(loadedFragment).commit();
        }

        gasDetailsList.remove(position);
        db.delete(AutomobileDatabaseHelper.GAS_DETAILS_TABLE,
                AutomobileDatabaseHelper.KEY_ID + "=" + id,
                null);
        adapter.notifyDataSetChanged();

        Snackbar.make(parentLayout,
                getResources().getString(R.string.delete_successful),
                Snackbar.LENGTH_LONG).show();
    }

    private class GasDetailsAdapter extends ArrayAdapter<AutomobileGasDetails> {

        private GasDetailsAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public int getCount() {
            return gasDetailsList.size();
        }

        @Override
        public AutomobileGasDetails getItem(int position) {
            return gasDetailsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            if (cursor == null)
                throw new NullPointerException("ERROR: cursor is null");

            cursor = db.rawQuery(AutomobileDatabaseHelper.SELECT_ALL_SQL, null);
            cursor.moveToPosition(position);
            return cursor.getLong(cursor.getColumnIndex(AutomobileDatabaseHelper.KEY_ID));
        }

        @Override
        @NonNull
        public View getView(final int position, View convertView, ViewGroup parent) {

            View view = convertView;

            if (view == null){
                LayoutInflater inflater = AutomobileActivity.this.getLayoutInflater();
                view = inflater.inflate(R.layout.gas_details_row, parent, false);
            }

            LinearLayout llGasDetailsRow = view.findViewById(R.id.llGasDetailsRow);
            if ((position % 2) == 0){
                llGasDetailsRow.setBackgroundColor(getResources().getColor(R.color.colorGreenLight));
            } else {
                llGasDetailsRow.setBackgroundColor(getResources().getColor(R.color.colorBlueLight));
            }

            ImageView ivDeleteGas = view.findViewById(R.id.ivDeleteGas);
            ivDeleteGas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    LayoutInflater inflater = getLayoutInflater();
                    LinearLayout rootView
                            = (LinearLayout) inflater.inflate(R.layout.automobile_alert_dialog, null);

                    ((TextView)rootView.findViewById(R.id.tvAlertMessage))
                            .setText(getResources().getString(R.string.alert_delete_massage));

                    AlertDialog.Builder builder = new AlertDialog.Builder(AutomobileActivity.this);
                    builder.setView(rootView);
                    builder.setPositiveButton(getResources().getString(R.string.agree),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    long id = adapter.getItemId(position);
                                    deleteGasDetail(id, position);
                                }
                            }
                    );

                    builder.setNegativeButton(getResources().getString(R.string.disagree),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {}
                            }
                    );

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });

            AutomobileGasDetails details = getItem(position);
            DecimalFormat df = new DecimalFormat("0.00");

            if (details != null){
                TextView tvPrice = view.findViewById(R.id.tvPrice);
                tvPrice.setText(String.valueOf(df.format(details.getPrice())));


                TextView tvAmount = view.findViewById(R.id.tvAmount);
                tvAmount.setText(String.valueOf(details.getLitres()));

                TextView tvDistance = view.findViewById(R.id.tvDistance);
                tvDistance.setText(String.valueOf(details.getKilometers()));

                TextView tvDate = view.findViewById(R.id.tvDate);
                tvDate.setText(DD_MM_YYYY.format(details.getDate()));
            }
            return view;
        }

    }

    private long getFirstTimestampOfPrevMonth(){
        Calendar calendar = getPrevMonthAndYear();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date firstDateOfPrevMonth = calendar.getTime();
        return firstDateOfPrevMonth.getTime();
    }

    // java2s getthelastdayofamonth
    private long getLastTimestampOfPrevMonth(){
        Calendar calendar = getPrevMonthAndYear();
        int lastDate = calendar.getActualMaximum(Calendar.DATE);
        calendar.set(Calendar.DATE, lastDate);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date lastDayOfPrevMonth = calendar.getTime();
        return lastDayOfPrevMonth.getTime();
    }

    private Calendar getPrevMonthAndYear(){
        Calendar calendar = Calendar.getInstance();
        int currMonth = calendar.get(Calendar.MONTH);
        int prevMonth = currMonth == 0 ? 11 : currMonth-1;
        int currYear = calendar.get(Calendar.YEAR);
        int prevYear = currMonth == 0 ? currYear-1 : currYear;
        calendar.set(Calendar.MONTH, prevMonth);
        calendar.set(Calendar.YEAR, prevYear);
        return calendar;
    }

}
