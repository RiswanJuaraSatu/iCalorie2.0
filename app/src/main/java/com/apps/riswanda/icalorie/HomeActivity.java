package com.apps.riswanda.icalorie;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    public ArrayList<aktivModel> CustomAktivList = new ArrayList<>();

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("https://icalorie.firebaseio.com/");

    private TextView nama, umur, tinggi, berat,kelamin, pinggang, aktivitas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        nama = (TextView) findViewById(R.id.textNamaUser);
        umur = (TextView) findViewById(R.id.textUmurUser);
        tinggi = (TextView) findViewById(R.id.textTinggiUser);
        berat = (TextView) findViewById(R.id.textBeratUser);
        kelamin = (TextView) findViewById(R.id.textJenisKelamin);
        pinggang = (TextView) findViewById(R.id.textLpUser);
        aktivitas = (TextView) findViewById(R.id.textAktivUser);


        userRetrieve();



    }

    private void userRetrieve() {
        DatabaseReference refUser = ref.child("Users");

        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                nama.setText(user.nama);
                umur.setText(user.usia);
                tinggi.setText(user.tinggi);
                berat.setText(user.berat);
                kelamin.setText(user.jeniskelamin);
                pinggang.setText(user.lPinggang);
                aktivitas.setText(user.aktiv);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
