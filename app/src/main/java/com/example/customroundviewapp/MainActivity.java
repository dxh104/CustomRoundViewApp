package com.example.customroundviewapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customroundviewapp.widget.CustomRoundView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button btnStart;
    private Button btnPause;
    private Button btnResume;
    private Button btnStop;
    private CustomRoundView customRoundView;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;
    private TextView tv7;
    private TextView tv8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
        tv5.setOnClickListener(this);
        tv6.setOnClickListener(this);
        tv7.setOnClickListener(this);
        tv8.setOnClickListener(this);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customRoundView.startAnimal(1000 * 10, true);
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customRoundView.pauseAnimal();
            }
        });
        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customRoundView.resumeAnimal();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customRoundView.stopAnimal();
            }
        });
//        customRoundView.startAnimal(1000 * 10, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        customRoundView.pauseAnimal();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        customRoundView.resumeAnimal();
    }

    private void initView() {

        btnStart = (Button) findViewById(R.id.btn_start);
        btnPause = (Button) findViewById(R.id.btn_pause);
        btnResume = (Button) findViewById(R.id.btn_resume);
        btnStop = (Button) findViewById(R.id.btn_stop);
        customRoundView = (CustomRoundView) findViewById(R.id.customRoundView);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        tv5 = (TextView) findViewById(R.id.tv5);
        tv6 = (TextView) findViewById(R.id.tv6);
        tv7 = (TextView) findViewById(R.id.tv7);
        tv8 = (TextView) findViewById(R.id.tv8);
    }

    @Override
    public void onClick(View v) {
        String s = ((TextView) v).getText().toString();
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

}
