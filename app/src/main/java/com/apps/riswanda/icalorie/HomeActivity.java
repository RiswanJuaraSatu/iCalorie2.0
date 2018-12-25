package com.apps.riswanda.icalorie;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

   // ExpandableRelativeLayout tampilSarapan, tampilMakanSiang, tampilMakanMalam;

    Double nR,nN,nT,nST,tb, bbideal = 0.0;

    private static final String TAG = "HomeActivity/";
    FirebaseAuth mAuth;
//    public ArrayList<aktivModel> CustomAktivList = new ArrayList<>();

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();

    private TextView nama, umur, tinggi, berat, kelamin, pinggang, aktivitas, beratIdeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        nama = (TextView) findViewById(R.id.textNamaUser);
        umur = (TextView) findViewById(R.id.textUmurUser);
        tinggi = (TextView) findViewById(R.id.textTinggiUser);
        berat = (TextView) findViewById(R.id.textBeratUser);
        kelamin = (TextView) findViewById(R.id.textJenisKelamin);
        pinggang = (TextView) findViewById(R.id.textLpUser);
        aktivitas = (TextView) findViewById(R.id.textAktivUser);

        beratIdeal = (TextView) findViewById(R.id.textBbIdeal);


        userRetrieve();


    }

    private void userRetrieve() {
        String uID = mAuth.getCurrentUser().getUid();
        Log.d(TAG, "userRetrieve: " + uID);
        DatabaseReference refUser = ref.child("Users/" + uID);

        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                Log.d(TAG, "onDataChange: " + user.nama);

                nama.setText(user.nama);
                umur.setText(user.usia);
                tinggi.setText(user.tinggi);
                berat.setText(user.berat);
                kelamin.setText(user.jeniskelamin);
                pinggang.setText(user.lPerut);
//                aktivitas.setText(user.aktiv);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //BeratBadan IDEAL

    private void beratIdeal() {

        tb = Double.valueOf(tinggi.getText().toString());

        if ("Laki-Laki".equalsIgnoreCase(kelamin.getText().toString())) {
            if (tb >= 160)
                bbideal = 90 * (tb - 100) / 100;
            else bbideal = tb - 100;
        } else if ("Perempuan".equalsIgnoreCase(kelamin.getText().toString())) {
            if (tb >= 150)
                bbideal = 90 * (tb - 100) / 100;
            else
                bbideal = tb - 100;
        }
        beratIdeal.setText(String.valueOf(bbideal));
    }

    //IMT
    private void imtExe() {
        double imt=0.0, bb,tb;
        bb = Double.valueOf(berat.getText().toString());
        tb = Double.valueOf(tinggi.getText().toString())/100;

        imt = bb/(tb*tb);

//
//        if (imt < 18.5) "Kurus (underweight)";
//        else if (18.5 <= imt < 25.0) "Normal (ideal)";
//        else if (25.0 <= imt < 30.0) "Kegemukan (overweight – Pre Obese)";
//        else if (30.0 <= imt < 35.0) "Obesitas tingkat 1";
//        else if (35.0 <= imt < 40.0) "Obesitas tingkat 2";
//        else if (imt >= 40.0) "Obesitas tingkat 3";
    }


    //KADAR LEMAK
    private void lemakNBC(){


        /*
        class :ringan, normal, tinggi, sangat tinggi
         */
        //count ringan
        ref.child("DataKasus").orderByChild("kadarLemak").equalTo("RINGAN").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nR = Double.longBitsToDouble(dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.child("DataKasus").orderByChild("kadarLemak").equalTo("NORMAL").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nN = Double.longBitsToDouble(dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.child("DataKasus").orderByChild("kadarLemak").equalTo("TINGGI").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nT = Double.longBitsToDouble(dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.child("DataKasus").orderByChild("kadarLemak").equalTo("SANGAT TINGGI").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nST = Double.longBitsToDouble(dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    //Expandable Menu Makanan

    /*public void tampilMakanMalam(View view) {
        tampilMakanMalam = (ExpandableRelativeLayout) findViewById(R.id.expandMakanMalam);
        tampilMakanMalam.expand();
    }

    public void tampilMakanSiang(View view) {
        tampilMakanSiang = (ExpandableRelativeLayout) findViewById(R.id.expandMakanSiang);
        tampilMakanSiang.expand();
    }

    public void tampilSarapan(View view) {
        tampilSarapan = (ExpandableRelativeLayout) findViewById(R.id.expandSarapan);
        tampilSarapan.expand();
    }*/
}
