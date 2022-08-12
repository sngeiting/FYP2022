package com.example.fyp2022;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class Budget extends AppCompatActivity {
    TextView tv_Bud_budgetSet, tv_Bud_budgetLeft,tv_Bud_budgetLeftSign;
    DatabaseHelper dbHelper;
    String month;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide action bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_budget);

        LocalDate localDate = LocalDate.now();
        //test only
//        LocalDate testDate = LocalDate.now().withMonth(10).withDayOfMonth(1);
//        localDate = testDate;

        int monthInNumeric = localDate.getMonthValue();
        if (monthInNumeric < 10) {
            month = "0" + monthInNumeric;
        } else {
            month = Integer.toString(monthInNumeric);
        }

        tv_Bud_budgetLeft = findViewById(R.id.tv_Bud_budgetLeft);
        tv_Bud_budgetSet = findViewById(R.id.tv_Bud_budgetSet);
        tv_Bud_budgetLeftSign = findViewById(R.id.tv_Bud_budgetLeftSign);

        dbHelper = new DatabaseHelper(Budget.this);
        double budgetSet = dbHelper.getBudgetSet(month);
        tv_Bud_budgetSet.setText(Double.toString(budgetSet));
        double totalSpendings = dbHelper.getTotalSpendings(month);
        double budgetLeft = round(budgetSet - totalSpendings,2);

        if (totalSpendings == 0 ) {
            tv_Bud_budgetLeft.setText(Double.toString(budgetLeft));

        } else {
            tv_Bud_budgetLeft.setText(Double.toString(budgetLeft));
        }
        if(budgetLeft < 0) {
            tv_Bud_budgetLeft.setTextColor(getResources().getColor(R.color.red_error));
            tv_Bud_budgetLeftSign.setTextColor(getResources().getColor(R.color.red_error));
        }

        boolean isBudgetSet = dbHelper.isBudgetSet(month);
        Intent intent;
        if(!isBudgetSet){
            // if user have not set a budget for the month
            startActivity(new Intent(Budget.this, SetBudget.class));
        }

        // Btm Nav bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_budget);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_budget:
                        return true;
                    case R.id.menu_genOtp:
                        startActivity(new Intent(Budget.this, GenOtpHome.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_summary:
                        startActivity(new Intent(Budget.this, Summary.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_awareness:
                        startActivity(new Intent(Budget.this, Awareness.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }
    // method obtained from https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
    public double round(double value, int decimalPlaces) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}