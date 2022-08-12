
package com.example.fyp2022;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FrictionPopUp extends AppCompatActivity implements PassDataInterface {
    boolean success;
    ConstraintLayout layout_popup;
    DatabaseHelper dbHelper;
    Random random;
    Fragment fragment;
    FragmentManager manager;
    FragmentTransaction ft;
    String month;
    List<Integer> successID = new ArrayList<>(), failureID = new ArrayList<>();
    TextView tv_success;
    int successCount, timesToPass, timesFrictionPointTriggered;
    String setSucessCountMsg;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        FrictionPopUp.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // hide action bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_friction_pop_up);

        // take window size as width and height
        //create new display metric object
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        // *0.85 for 85% of screen size
        getWindow().setLayout((int)(width*0.85),(int)(height*0.5));

        LocalDate localDate = LocalDate.now();

        //testing
//        LocalDate testDate = LocalDate.now().withMonth(10).withDayOfMonth(1);
//        localDate = testDate;

        int monthInNumeric = localDate.getMonthValue();
        if (monthInNumeric < 10) {
            month = "0" + monthInNumeric;
        } else {
            month = Integer.toString(monthInNumeric);
        }
        dbHelper = new DatabaseHelper(FrictionPopUp.this);
        tv_success = findViewById(R.id.tv_success);
        timesFrictionPointTriggered = 1;
        if (dbHelper.isBudgetFrictionDataCreated(month)) {
            timesFrictionPointTriggered = dbHelper.countTimesFrictionPointsTriggered(month);
            timesFrictionPointTriggered += 1;
        }
        timesToPass = timesFrictionPointTriggered*2;

        successCount = 0;
        setSucessCountMsg = successCount + "/" + timesToPass;
        tv_success.setText(setSucessCountMsg);
        layout_popup = findViewById(R.id.layout_popup);

        fragment = null;
        random = new Random();
        switch (random.nextInt(3)) {
            case 0:
            case 1:
                fragment = new frictionText(FrictionPopUp.this);
                break;

            case 2:
                fragment = new frictionClick(FrictionPopUp.this);
                break;
        }
        manager = getSupportFragmentManager();
        ft = manager.beginTransaction();
        ft.replace(R.id.layout_popup, fragment);
        ft.commit();
    }

    @Override
    public void onDataReceived(boolean data, int passID, int failID) {
        if (passID != 0 ) {
            successID.add(passID);
            successCount++;
        }
        if (failID != 0){
            failureID.add(failID);
        }
        setSucessCountMsg = successCount + "/" + timesToPass;
        tv_success.setText(setSucessCountMsg);
        success = data;
        if(success && successID.size() >= timesToPass) {
            // pass success to GenOtpHome
            Intent intent = new Intent();
            intent.putExtra("success", success);
            intent.putIntegerArrayListExtra("successID", (ArrayList<Integer>) successID);
            intent.putIntegerArrayListExtra("failureID", (ArrayList<Integer>) failureID);
            setResult(Activity.RESULT_OK,intent);
            finish();
        } else {
            int num = random.nextInt(3);
            switch (num) {
                case 0:
                case 1:
                    fragment = new frictionText(FrictionPopUp.this);
                    break;
                case 2:
                    fragment = new frictionClick(FrictionPopUp.this);
                    break;
            }
            if (fragment != null) {
                ft = manager.beginTransaction();
                ft.replace(R.id.layout_popup,fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        }
    }
}

