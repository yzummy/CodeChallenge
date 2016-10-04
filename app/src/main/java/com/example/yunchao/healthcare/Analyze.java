package com.example.yunchao.healthcare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by Yunch on 10/1/2016.
 */

public class Analyze extends Activity{

    private TextView tv1, tv2,tv3,tv4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analyze);
        tv1 = (TextView) findViewById(R.id.totalPatients);
        tv2 = (TextView) findViewById(R.id.gender);
        tv3 = (TextView) findViewById(R.id.aveMedication);
        tv4 = (TextView) findViewById(R.id.avgImmuniation);

        Intent intent = getIntent();
        String totalPatients = intent.getStringExtra("totalPatients");
        tv1.setText(totalPatients);
        String[] gender = intent.getStringArrayExtra("gender");
        String gen = "Male: "+gender[1]+"    "+"Female: "+gender[2]+"    "+"Unspecified: "+gender[0];
        tv2.setText(gen);
        String avgMedication = intent.getStringExtra("AvgMedication");
        tv3.setText(avgMedication);
        String avgImmunization = intent.getStringExtra("AvgImmunization");
        tv4.setText(avgImmunization);

    }
}
