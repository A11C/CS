package com.fiuady.hadp.compustore;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OrdersActivity extends AppCompatActivity {

    private static boolean datei, datef;
    private Spinner spinneror, spinnercl;
    private CheckBox chkdi, chkdf;
    private TextView datefin, dateini;
    private RecyclerView recyclerview;
    private static String string;

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            //this.getContext();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formatDate = DateFormat.getDateInstance(DateFormat.LONG).format(new Date());
            string = formatDate;

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        spinnercl = (Spinner) findViewById(R.id.spinnerOr);
        spinneror = (Spinner) findViewById(R.id.spinnerC);
        dateini = (TextView) findViewById(R.id.dateini);
        chkdf = (CheckBox) findViewById(R.id.orderFf_checkbox);
        chkdi = (CheckBox) findViewById(R.id.orderFi_checkbox);
        datefin = (TextView) findViewById(R.id.datefin);

        chkdf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                   // datef = true;
                   // datei = false;
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getSupportFragmentManager(),"datePicker2");
                    datefin.setText(string);
                }
            }
        });

        chkdi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                   // datei = true;
                   // datef = false;
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getSupportFragmentManager(),"datePicker");
                    dateini.setText(string);
                }
            }
        });

       // if (Build.VERSION.SDK_INT >= 21) {
        //    getWindow().setNavigationBarColor(getResources().getColor(R.color.colorOrders));
        //    getWindow().setStatusBarColor(getResources().getColor(R.color.colorOrders));
        //}
    }
}
