package com.apps.riswanda.icalorie;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SignUp extends AppCompatActivity {

    boolean checkEmailExist;
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
        mGender = (RadioGroup) findViewById(R.id.rb_kelamin);
        inputBB = (EditText) findViewById(R.id.ibb);
        inputTB = (EditText) findViewById(R.id.itb);
        inlPing = (EditText) findViewById(R.id.iLP);
        btnSignup = (Button) findViewById(R.id.btn_signUp);

        inputKataSandi.addTextChangedListener(passW);

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
        mDisplayDate = (EditText) (findViewById(R.id.tanggalLahirE));
        mDisplayDate.addTextChangedListener(DBOW);

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
                String format = new SimpleDateFormat("dd MMM yyyy").format(cal.getTime());
                mDisplayDate.setText(format);
                umur = (Integer.toString(calculateAge(cal.getTimeInMillis())));
            }

        };
    }

    private TextWatcher DBOW = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!mDisplayDate.getText().toString().isEmpty()){
                mDisplayDate.setError(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher passW = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (inputKataSandi.getText().toString().trim().length() < 6) {
                inputKataSandi.setError("Kata Sandi minimal 6 Karakter.");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


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
        final String dbo = mDisplayDate.getText().toString().trim();
        final String tinggi = inputTB.getText().toString().trim();
        final String fGender = gender;
        final String fUmur = umur;
        final String berat = inputBB.getText().toString().trim();
        final String lPing = inlPing.getText().toString().trim();

        Log.d(TAG, "startSignUp: " + nama);
        Log.d(TAG, "startSignUp: " + email);
        Log.d(TAG, "startSignUp: " + katasandi);
        Log.d(TAG, "startSignUp: " + dbo);
        Log.d(TAG, "startSignUp: " + fUmur);
        Log.d(TAG, "startSignUp: " + fGender);
        Log.d(TAG, "startSignUp: " + berat);
        Log.d(TAG, "startSignUp: " + lPing);
        /*final String aktiv;*/
        if (nama.isEmpty()) inputNama.setError("Nama wajib diisi!");
        if (email.isEmpty()) inputEmail.setError("Email wajib diisi!");
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            inputEmail.setError("Masukkan Email Dengan Benar");
        if (katasandi.isEmpty()) inputKataSandi.setError("Kata Sandi wajib diisi!");
        if (dbo.isEmpty()) mDisplayDate.setError("Tanggal Lahir wajib diisi!");
        if (berat.isEmpty()) inputBB.setError("Berat Badan wajib diisi!");
        if (tinggi.isEmpty()) inputTB.setError("Tinggi Badan wajib diisi!");
        if (lPing.isEmpty()) inlPing.setError("Linggkar Perut wajib diisi!");

        if (!TextUtils.isEmpty(nama) && !checkEmailExist && !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches() && !TextUtils.isEmpty(katasandi)
                && !TextUtils.isEmpty(dbo) && !TextUtils.isEmpty(fGender) && !TextUtils.isEmpty(tinggi) && !TextUtils.isEmpty(berat) && !TextUtils.isEmpty(lPing)) {
            final Users user = new Users(nama, email, katasandi, fUmur, fGender, dbo, tinggi, berat, lPing);

            mProgess.setMessage("Signing Up ...");
            mProgess.show();
            mAuth.createUserWithEmailAndPassword(email, katasandi).addOnCompleteListener(new OnCompleteListener<AuthResult>() {


                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        mProgess.dismiss();
                        Toast.makeText(SignUp.this, "Registrasi Gagal. Email sudah Terdaftar", Toast.LENGTH_LONG).show();
                        inputEmail.setError("Email Sudah Terdaftar.");
                    }

                    if (task.isSuccessful()) {
                        String user_id = mAuth.getCurrentUser().getUid();

                        DatabaseReference cureent_user_db = mDatabase.child(user_id);

                        cureent_user_db.setValue(user);

                        mProgess.dismiss();

                        Intent mainIntent = new Intent(SignUp.this, HomeActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    } else {

                    }
                }
            });
        }

    }


}
