package com.apps.riswanda.icalorie;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SignUp extends AppCompatActivity {

    String umur;

    private static final String TAG = "SignUp";

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //pilih tanggal
        mDisplayDate = (TextView) (findViewById(R.id.tanggalLahir));

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int tahun = cal.get(Calendar.YEAR);
                int bulan = cal.get(Calendar.MONTH);
                int tanggal = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(SignUp.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, tahun, bulan, tanggal);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int tahun, int bulan, int tanggal) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, tahun);
                cal.set(Calendar.MONTH, bulan);
                cal.set(Calendar.DAY_OF_MONTH, tanggal);
                String format = new SimpleDateFormat("dd MMM YYYY").format(cal.getTime());
                mDisplayDate.setText(format);
                umur = (Integer.toString(calculateAge(cal.getTimeInMillis())));
            }

        };
    }

    private int calculateAge(long date) {
        Calendar dob = Calendar.getInstance();
        dob.setTimeInMillis(date);

        Calendar today = Calendar.getInstance();

        int umur = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
            umur--;
        }

        return umur;
    }

}
