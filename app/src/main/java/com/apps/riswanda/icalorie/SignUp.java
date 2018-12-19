package com.apps.riswanda.icalorie;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SignUp extends AppCompatActivity {

    private String umur;
    private String gender;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgess;

    private Button btnSignup;
    private EditText inputNama, inputEmail, inputKataSandi, inputBB, inlPing, inputTB;
    private EditText inaktiv;
    private RadioGroup mGender;
    private RadioButton mGenderOption;


    private static final String TAG = "SignUp";

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mProgess = new ProgressDialog(this);

        inputNama = (EditText) findViewById(R.id.inputNama);
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputKataSandi = (EditText) findViewById(R.id.inputKatasandi);
        mGender = findViewById(R.id.rb_kelamin);
        inputBB = findViewById(R.id.ibb);
        inputTB = findViewById(R.id.itb);
        inlPing = findViewById(R.id.iLP);
        btnSignup = (Button) findViewById(R.id.btn_signUp);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignUp();
            }
        });

        //radio button gender
        mGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int i) {
                mGenderOption = mGender.findViewById(i);
                switch (i) {
                    case R.id.rbLk:
                        gender = mGenderOption.getText().toString();
                        break;
                    case R.id.rbPr:
                        gender = mGenderOption.getText().toString();
                        break;
                    default:
                }
            }
        });

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

    private void startSignUp() {
        final String nama = inputNama.getText().toString().trim();
        final String email = inputEmail.getText().toString().trim();
        final String katasandi = inputKataSandi.getText().toString().trim();
        final String tanggal_lahir = mDisplayDate.getText().toString().trim();
        final String tinggi = inputTB.getText().toString().trim();
        final String berat = inputBB.getText().toString().trim() ;
        final String lPing = inlPing.getText().toString().trim();
        /*final String aktiv;*/
        if (!TextUtils.isEmpty(nama) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(katasandi)
                && !TextUtils.isEmpty(tanggal_lahir)) {


            final Users user = new Users(nama,email,katasandi,umur,gender,tanggal_lahir,tinggi,berat,lPing/*, aktiv*/);

            mProgess.setMessage("Signing Up ...");
            mProgess.show();
            mAuth.createUserWithEmailAndPassword(email, katasandi).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String user_id = mAuth.getCurrentUser().getUid();

                        DatabaseReference cureent_user_db = mDatabase.child(user_id);

                        cureent_user_db.child("nama").setValue(user);

                        mProgess.dismiss();

                        Intent mainIntent = new Intent(SignUp.this, HomeActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    } else{
                        mProgess.dismiss();
                        Toast.makeText(SignUp.this,"Regritation Failed",Toast.LENGTH_LONG).show();
                    }
                    //isExist
                }
            });
        }

    }

}
