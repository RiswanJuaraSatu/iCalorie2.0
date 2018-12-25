package com.apps.riswanda.icalorie;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    // ExpandableRelativeLayout tampilSarapan, tampilMakanSiang, tampilMakanMalam;

    Double tb, bbideal = 0.0;
    Double totalSangatTinggi = 0.0, totalTinggi = 0.0, totalNormal = 0.0, totalRendah = 0.0;
    Double sumTBLST = 0.0, sumTBT = 0.0, sumTBS = 0.0, sumTBR = 0.0;
    Double sumBBLST = 0.0, sumBBLT = 0.0, sumBBLN = 0.0, sumBBLR = 0.0;
    Double sumLPLST = 0.0, sumLPLT = 0.0, sumLPLN = 0.0, sumLPLR = 0.0;
    Double sumLKLST = 0.0, sumLKLT = 0.0, sumLKLN = 0.0, sumLKLR = 0.0;
    Double sumPRLST = 0.0, sumPRLT = 0.0, sumPRLN = 0.0, sumPRLR = 0.0;
    Double meanTBST = 0.0, meanTBT = 0.0, meanTBN = 0.0, meanTBR = 0.0;
    Double meanBBST = 0.0, meanBBT = 0.0, meanBBN = 0.0, meanBBR = 0.0;
    Double meanLPST = 0.0, meanLPT = 0.0, meanLPN = 0.0, meanLPR = 0.0;


    Double userTinggi, userBerat, userPerut;
    Double ngTinggiST, ngTinggiT, ngTinggiN, ngTinggiR;
    Double ngBeratST, ngBeratT, ngBeratN, ngBeratR;
    Double ngPerutST, ngPerutT, ngPerutN, ngPerutR;
    Double probLakiST, probLakiT, probLakiN, probLakiR;
    Double probPerST, probPerT, probPerN, probPerR;
    String userKelamin;

    Double sdTinggiSangatTinggi = 0.0, sdTinggiTinggi = 0.0, sdTinggiNormal = 0.0, sdTinggiRendah = 0.0;
    Double sdBeratSangatTinggi = 0.0, sdBeratTinggi = 0.0, sdBeratNormal = 0.0, sdBeratRendah = 0.0;
    Double sdPerutSangatTinggi = 0.0, sdPerutTinggi = 0.0, sdPerutNormal = 0.0, sdPerutRendah = 0.0;

//    Double likelihoodSanggatTinggi, likelihoodTinggi, likelihoodNormal, likelihoodRendah;


    ArrayList<String> tinggiBadanSangatTingi, tinggiBadanTinggi, tinggiBadanNormal, tinggiBadanRendah = new ArrayList<String>();
    ArrayList<String> beratBadanSangatTinggi, beratBadanTinggi, beratBadanNormal, beratBadanRendah = new ArrayList<String>();
    ArrayList<String> lingkarPinggangSangatTinggi, lingkarPinggangTinggi, lingkarPinggangNormal, lingkarPinggangRendah = new ArrayList<String>();
    ArrayList<String> jenisKelaminAL = new ArrayList<String>();

    private static final String TAG = "HomeActivity/";
    FirebaseAuth mAuth;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();

    private TextView nama, umur, tinggi, berat, kelamin, pinggang, aktivitas, beratIdeal, kadarLemakUser;

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
    private void lemakNBC() {

        userBerat = Double.parseDouble(berat.getText().toString());
        userTinggi = Double.parseDouble(tinggi.getText().toString());
        userPerut = Double.parseDouble(pinggang.getText().toString());
        userKelamin = kelamin.getText().toString();


        // class Tinggi badan
        ref.child("DataKasus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot kasus : dataSnapshot.getChildren()) {
                    String kadarLemak = kasus.child("kadarLemak").getValue().toString();
                    String tbS = kasus.child("tb").getValue().toString();
                    String bbS = kasus.child("bb").getValue().toString();
                    String lpS = kasus.child("lp").getValue().toString();
                    String jk = kasus.child("kategori").getValue().toString();


                    //  Perhitungan data kuantitatif


                    if (kadarLemak.equals("SANGAT TINGGI")) {
                        tinggiBadanSangatTingi.add(tbS);
                        beratBadanSangatTinggi.add(bbS);
                        lingkarPinggangSangatTinggi.add(lpS);
                        sumTBLST += Double.parseDouble(tbS);
                        sumBBLST += Double.parseDouble(bbS);
                        sumLPLST += Double.parseDouble(lpS);
                        totalSangatTinggi += 1;

                    } else if (kadarLemak.equals("TINGGI")) {
                        tinggiBadanTinggi.add(tbS);
                        beratBadanTinggi.add(bbS);
                        lingkarPinggangTinggi.add(lpS);
                        sumTBT += Double.parseDouble(tbS);
                        sumBBLT += Double.parseDouble(bbS);
                        sumLPLT += Double.parseDouble(lpS);
                        totalTinggi += 1;

                    } else if (kadarLemak.equals("NORMAL")) {
                        tinggiBadanNormal.add(tbS);
                        beratBadanNormal.add(bbS);
                        lingkarPinggangNormal.add(lpS);
                        sumTBS += Double.parseDouble(tbS);
                        sumBBLN += Double.parseDouble(bbS);
                        sumLPLN += Double.parseDouble(lpS);
                        totalNormal += 1;

                    } else if (kadarLemak.equals("RENDAH")) {
                        tinggiBadanRendah.add(tbS);
                        beratBadanRendah.add(bbS);
                        lingkarPinggangRendah.add(lpS);
                        sumTBR += Double.parseDouble(tbS);
                        sumBBLR += Double.parseDouble(bbS);
                        sumLPLR += Double.parseDouble(lpS);
                        totalRendah += 1;

                    }

                    // Perhitungan data non kuantitatif

                    if (kadarLemak.equals("SANGAT TINGGI") && jk.equals("Laki-laki")) {
                        sumLKLST += 1;
                    } else if (kadarLemak.equals("TINGGI") && jk.equals("Laki-laki")) {
                        sumLKLT += 1;
                    } else if (kadarLemak.equals("NORMAL") && jk.equals("Laki-laki")) {
                        sumLKLN += 1;
                    } else if (kadarLemak.equals("RENDAH") && jk.equals("Laki-laki")) {
                        sumLKLR += 1;
                    } else if (kadarLemak.equals("SANGAT TINGGI") && jk.equals("Perempuan")) {
                        sumPRLST += 1;
                    } else if (kadarLemak.equals("TINGGI") && jk.equals("Perempuan")) {
                        sumPRLT += 1;
                    } else if (kadarLemak.equals("NORMAL") && jk.equals("Perempuan")) {
                        sumPRLN += 1;
                    } else if (kadarLemak.equals("RENDAH") && jk.equals("Perempuan")) {
                        sumPRLR += 1;
                    }
                }//end for

                // tinggi badan
                meanTBST = sumTBLST / totalSangatTinggi;
                meanTBT = sumTBT / totalTinggi;
                meanTBN = sumTBS / totalNormal;
                meanTBR = sumTBR / totalRendah;
                //Berat Badan
                meanBBST = sumBBLST / totalSangatTinggi;
                meanBBT = sumBBLT / totalTinggi;
                meanBBN = sumBBLN / totalNormal;
                meanBBR = sumBBLR / totalRendah;
                //LINGKAR PERUT
                meanLPST = sumLPLST / totalSangatTinggi;
                meanLPT = sumLPLT / totalTinggi;
                meanLPN = sumLPLN / totalNormal;
                meanLPR = sumLPLR / totalRendah;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Double totalKasus = totalSangatTinggi + totalTinggi + totalNormal + totalRendah;

        for (String tbstMasuk : tinggiBadanSangatTingi) {
            Double tmp;
            tmp = (Double.parseDouble(tbstMasuk) - meanTBST);
            sdTinggiSangatTinggi += Math.pow(tmp, 2.0);
        }
        sdTinggiSangatTinggi = sdTinggiSangatTinggi / (tinggiBadanSangatTingi.size() - 1);
        sdTinggiSangatTinggi = Math.sqrt(sdTinggiSangatTinggi);

        for (String tbtMasuk : tinggiBadanTinggi) {
            Double tmp;
            tmp = (Double.parseDouble(tbtMasuk) - meanTBT);
            sdTinggiTinggi += Math.pow(tmp, 2.0);
        }
        sdTinggiTinggi = sdTinggiTinggi / (tinggiBadanTinggi.size() - 1);
        sdTinggiTinggi = Math.sqrt(sdTinggiTinggi);
        for (String tbnMasuk : tinggiBadanNormal) {
            Double tmp;
            tmp = (Double.parseDouble(tbnMasuk) - meanTBN);
            sdTinggiNormal += Math.pow(tmp, 2.0);
        }
        sdTinggiNormal = sdTinggiNormal / (tinggiBadanNormal.size() - 1);
        sdTinggiNormal = Math.sqrt(sdTinggiNormal);
        for (String tbrMasuk : tinggiBadanRendah) {
            Double tmp;
            tmp = (Double.parseDouble(tbrMasuk) - meanTBR);
            sdTinggiRendah += Math.pow(tmp, 2.0);
        }
        sdTinggiRendah = sdTinggiRendah / (tinggiBadanRendah.size() - 1);
        sdTinggiRendah = Math.sqrt(sdTinggiRendah);

        //berat standar deviasi
        for (String bstMasuk : beratBadanSangatTinggi) {
            Double tmp;
            tmp = (Double.parseDouble(bstMasuk) - meanBBST);
            sdBeratSangatTinggi += Math.pow(tmp, 2.0);
        }
        sdBeratSangatTinggi = sdBeratSangatTinggi / (beratBadanSangatTinggi.size() - 1);
        sdBeratSangatTinggi = Math.sqrt(sdBeratSangatTinggi);

        for (String tbtMasuk : beratBadanTinggi) {
            Double tmp;
            tmp = (Double.parseDouble(tbtMasuk) - meanBBT);
            sdTinggiTinggi += Math.pow(tmp, 2.0);
        }
        sdBeratTinggi = sdBeratTinggi / (beratBadanTinggi.size() - 1);
        sdBeratTinggi = Math.sqrt(sdBeratTinggi);
        for (String tbnMasuk : beratBadanNormal) {
            Double tmp;
            tmp = (Double.parseDouble(tbnMasuk) - meanBBN);
            sdBeratNormal += Math.pow(tmp, 2.0);
        }
        sdBeratNormal = sdBeratNormal / (beratBadanNormal.size() - 1);
        sdBeratNormal = Math.sqrt(sdBeratNormal);
        for (String tbrMasuk : beratBadanRendah) {
            Double tmp;
            tmp = (Double.parseDouble(tbrMasuk) - meanBBR);
            sdBeratRendah += Math.pow(tmp, 2.0);
        }
        sdBeratRendah = sdBeratRendah / (beratBadanRendah.size() - 1);
        sdBeratRendah = Math.sqrt(sdBeratRendah);

        //perut standar deviasi
        for (String lpMasuk :
                lingkarPinggangSangatTinggi) {
            Double tmp;
            tmp = (Double.parseDouble(lpMasuk) - meanLPST);
            sdPerutSangatTinggi += Math.pow(tmp, 2.0);
        }
        sdPerutSangatTinggi = sdPerutSangatTinggi / (lingkarPinggangSangatTinggi.size() - 1);
        sdPerutSangatTinggi = Math.sqrt(sdBeratSangatTinggi);

        for (String lpMasuk :
                lingkarPinggangTinggi) {
            Double tmp;
            tmp = (Double.parseDouble(lpMasuk) - meanLPT);
            sdPerutTinggi += Math.pow(tmp, 2.0);
        }
        sdPerutTinggi = sdPerutTinggi / (lingkarPinggangTinggi.size() - 1);
        sdPerutTinggi = Math.sqrt(sdPerutTinggi);

        for (String lpMasuk :
                lingkarPinggangNormal) {
            Double tmp;
            tmp = (Double.parseDouble(lpMasuk) - meanLPN);
            sdPerutNormal += Math.pow(tmp, 2.0);
        }
        sdPerutNormal = sdPerutNormal / (lingkarPinggangNormal.size() - 1);
        sdPerutNormal = Math.sqrt(sdPerutNormal);

        for (String lpMasuk :
                lingkarPinggangRendah) {
            Double tmp;
            tmp = (Double.parseDouble(lpMasuk) - meanLPR);
            sdPerutRendah += Math.pow(tmp, 2.0);
        }
        sdPerutRendah = sdPerutRendah / (lingkarPinggangRendah.size() - 1);
        sdPerutRendah = Math.sqrt(sdPerutRendah);

        // pencocokan data
        //tinggi badan
        ngTinggiST = normalGaussian(userTinggi, meanTBST, sdTinggiSangatTinggi);
        ngTinggiT = normalGaussian(userTinggi, meanTBT, sdTinggiTinggi);
        ngTinggiN = normalGaussian(userTinggi, meanTBN, sdTinggiNormal);
        ngTinggiR = normalGaussian(userTinggi, meanTBR, sdTinggiRendah);

        //berat badan
        ngBeratST = normalGaussian(userBerat, meanBBST, sdBeratSangatTinggi);
        ngBeratT = normalGaussian(userBerat, meanBBT, sdBeratTinggi);
        ngBeratN = normalGaussian(userBerat, meanBBN, sdBeratNormal);
        ngBeratR = normalGaussian(userBerat, meanBBR, sdBeratRendah);

        //lingkar perut
        ngPerutST = normalGaussian(userPerut, meanLPST, sdPerutSangatTinggi);
        ngPerutT = normalGaussian(userPerut, meanLPT, sdPerutTinggi);
        ngPerutN = normalGaussian(userPerut, meanLPN, sdPerutNormal);
        ngPerutR = normalGaussian(userPerut, meanLPR, sdPerutRendah);

        //probabilitas kelamin
        probLakiST = sumLKLST / totalSangatTinggi;
        probLakiT = sumLKLT / totalTinggi;
        probLakiN = sumLKLN / totalNormal;
        probLakiR = sumLKLR / totalRendah;

        probPerST = sumPRLST / totalSangatTinggi;
        probPerT = sumPRLT / totalTinggi;
        probPerN = sumPRLN / totalNormal;
        probPerR = sumPRLR / totalRendah;

        //prob class
        Double probSangatTinggi = totalSangatTinggi / totalKasus;
        Double probTinggi = totalTinggi / totalKasus;
        Double probNormal = totalNormal / totalKasus;
        Double probRendah = totalRendah / totalKasus;

        Double likelihoodSanggatTinggi = ngTinggiST * ngBeratST * ngPerutST * probSangatTinggi;
        Double likelihoodTinggi = ngTinggiT * ngBeratT * ngPerutT * probTinggi;
        Double likelihoodNormal = ngTinggiN * ngBeratN * ngPerutN * probNormal;
        Double likelihoodRendah = ngTinggiR * ngBeratR * ngPerutR * probRendah;

        switch (userKelamin) {
            case "Laki-laki":
                likelihoodSanggatTinggi *= probLakiST;
                likelihoodTinggi *= probLakiT;
                likelihoodNormal *= probLakiN;
                likelihoodRendah *= probLakiR;
                break;
            case "Perempuan":
                likelihoodSanggatTinggi *= probPerST;
                likelihoodTinggi *= probPerT;
                likelihoodNormal *= probPerN;
                likelihoodRendah *= probPerR;
        }

        Double fixSangatTinggi = likelihoodSanggatTinggi / (likelihoodSanggatTinggi + likelihoodTinggi + likelihoodNormal + likelihoodRendah);
        Double fixTinggi = likelihoodTinggi / (likelihoodSanggatTinggi + likelihoodTinggi + likelihoodNormal + likelihoodRendah);
        Double fixNormal = likelihoodNormal / (likelihoodSanggatTinggi + likelihoodTinggi + likelihoodNormal + likelihoodRendah);
        Double fixRendah = likelihoodRendah / (likelihoodSanggatTinggi + likelihoodTinggi + likelihoodNormal + likelihoodRendah);

        if (fixSangatTinggi > fixTinggi && fixSangatTinggi > fixNormal && fixSangatTinggi > fixRendah) {
            kadarLemakUser.setText("SANGAT TINGGI");
        } else if (fixTinggi > fixSangatTinggi && fixTinggi > fixNormal && fixTinggi > fixRendah) {
            kadarLemakUser.setText("TINGGI");
        } else if (fixNormal > fixTinggi && fixNormal > fixSangatTinggi && fixNormal > fixRendah) {
            kadarLemakUser.setText("NORMAL");
        } else if (fixRendah > fixSangatTinggi && fixRendah > fixTinggi && fixRendah > fixNormal) {
            kadarLemakUser.setText("RENDAH");
        }
    }

    private Double normalGaussian(Double userInfo, Double rata, Double standarDeviasi) {
        Double bawah, expo;
        expo = (Math.pow(userInfo - rata, 2) * -1) / (Math.pow(standarDeviasi, 2) * 2);
        bawah = Math.sqrt((2 * Math.PI * standarDeviasi));

        return 1 * Math.pow(Math.E, expo) / bawah;
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
