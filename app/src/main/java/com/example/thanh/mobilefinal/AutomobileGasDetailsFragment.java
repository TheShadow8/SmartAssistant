package com.example.thanh.mobilefinal;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class AutomobileGasDetailsFragment extends Fragment {
    private Activity callingActivity;
    Bundle gasDetails;
    String alertMsgText = "";

    public AutomobileGasDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_automobile_gas_details, container, false);

        final TextView gasDetailsTitle = view.findViewById(R.id.gasDetailsTitle);
        final EditText etPrice = view.findViewById(R.id.etPrice);
        final EditText etLitres = view.findViewById(R.id.etLitres);
        final EditText etKilometers = view.findViewById(R.id.etKilometers);
        final EditText etDate = view.findViewById(R.id.etDate);
        final Button btnGasDetailsSubmit = view.findViewById(R.id.btnGasDetailsSubmit);
        final Button btnGasDetailsCancel = view.findViewById(R.id.btnGasDetailsCancel);

        Bundle fragmentDetails = getArguments();

        if (fragmentDetails != null){
            String title = fragmentDetails.getString("fragmentTitle");
            String btnText = fragmentDetails.getString("btnText");
            gasDetailsTitle.setText(title);
            btnGasDetailsSubmit.setText(btnText);

            gasDetails = fragmentDetails.getBundle("gasDetails");

            // Edit request, populate the previous field values
            if (gasDetails != null){
                alertMsgText = getResources().getString(R.string.alert_edit);
                double price = gasDetails.getDouble("price");
                double litres = gasDetails.getDouble("litres");
                double kilometers = gasDetails.getDouble("kilometers");
                long longDate = gasDetails.getLong("date");

                etPrice.setText(String.valueOf(price));
                etLitres.setText(String.valueOf(litres));
                etKilometers.setText(String.valueOf(kilometers));
                etDate.setText(String.valueOf(AutomobileActivity.DD_MM_YYYY.format(longDate)));
            }
            // Add request, just populate today's date
            else {
                alertMsgText = getResources().getString(R.string.alert_add);
                etDate.setText(String.valueOf(AutomobileActivity.DD_MM_YYYY.format(new Date())));
            }
        }

        btnGasDetailsSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    // get values in EditText fields
                    double price = Double.parseDouble(etPrice.getText().toString());
                    double litres = Double.parseDouble(etLitres.getText().toString());
                    double kilometers = Double.parseDouble(etKilometers.getText().toString());
                    String stringDate = etDate.getText().toString();
                    Date date =  AutomobileActivity.DD_MM_YYYY.parse(stringDate);

                    // add details request
                    if (gasDetails == null){
                        gasDetails = new Bundle();
                    }

                    // update the gas details bundle
                    gasDetails.putDouble("price", price);
                    gasDetails.putDouble("litres", litres);
                    gasDetails.putDouble("kilometers", kilometers);
                    gasDetails.putLong("date", date.getTime());

                    switch(callingActivity.getLocalClassName()){

                        // The fragment was called from portrait orientation and navigated to a
                        //   new activity
                        case "AutomobileEditGasDetails":
                            ((AutomobileEditGasDetails)callingActivity).updateGasDetail(gasDetails);
                            break;
                        case "AutomobileAddGasDetails":
                            ((AutomobileAddGasDetails)callingActivity).addGasDetail(gasDetails);
                            break;

                        // The fragment was called from landscape orientation and was loaded
                        //  into the FrameLayout view
                        case "AutomobileActivity":
                            // no id present, add fuel detail
                            if (gasDetails.getLong("id", -1) == -1){
                                ((AutomobileActivity)callingActivity).addGasDetail(gasDetails);
                            }
                            // id present, edit fuel detail
                            else {
                                ((AutomobileActivity)callingActivity).updateGasDetail(gasDetails);
                            }
                            callingActivity.getFragmentManager().beginTransaction()
                                    .remove(AutomobileGasDetailsFragment.this).commit();
                            break;
                    }

                } catch (Exception e) {

                    LayoutInflater inflater = callingActivity.getLayoutInflater();
                    LinearLayout rootView
                            = (LinearLayout) inflater.inflate(R.layout.automobile_alert_dialog, null);

                    ((TextView)rootView.findViewById(R.id.tvAlertMessage)).setText(alertMsgText);

                    AlertDialog.Builder builder = new AlertDialog.Builder(callingActivity);
                    builder.setView(rootView);
                    builder.setPositiveButton(getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }
                    );

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });


        btnGasDetailsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callingActivity.setResult(Activity.RESULT_CANCELED);

                switch(callingActivity.getLocalClassName()){

                    // Finish the edit and add activities if in portrait orientation
                    case "AutomobileEditGasDetails":
                    case "AutomobileAddGasDetails":
                        callingActivity.finish();
                        break;

                    // remove fragment if in landscape orientation
                    case "AutomobileActivity":
                        callingActivity.getFragmentManager().beginTransaction()
                                .remove(AutomobileGasDetailsFragment.this).commit();
                        break;

                }
            }
        });


        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutomobileDatePickerFragment datePicker = new AutomobileDatePickerFragment();
                datePicker.setDisplay(etDate);
                datePicker.show(getFragmentManager(), "Date Picker");
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.callingActivity = activity;
    }

}
