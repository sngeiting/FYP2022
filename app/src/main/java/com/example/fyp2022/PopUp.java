package com.example.fyp2022;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class PopUp extends AppCompatActivity {
    Button btn_popupConfirm;
    TextView tv_displayTxt;
    String displayTxt;
    String extraTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        PopUp.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // hide action bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_pop_up);

        // take window size as width and height
        //create new display metric object
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        // *0.85 for 85% of screen size
        getWindow().setLayout((int)(width*0.85),(int)(height*0.45));
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            displayTxt = extras.getString("message","");
            extraTxt = extras.getString("overshotTimes","");
        }
        btn_popupConfirm = findViewById(R.id.btn_popupConfirm);
        tv_displayTxt = findViewById(R.id.tv_displayTxt);
        if (extraTxt.equals("")) {
            tv_displayTxt.setText(displayTxt);
        } else {
            tv_displayTxt.setText(displayTxt + "\n\n"+extraTxt);
        }

        btn_popupConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });


    }
}