package com.example.fyp2022;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;

public class Summary extends AppCompatActivity {

    AutoCompleteTextView actv_month;
    TextInputLayout dd_month;
    ArrayAdapter<String> adapterItems;
    String[] months;
    int monthInNumeric;
    String month, getMonthSelected, userSelectedMonth;
    Button btn_details;
    Intent intent;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_summary);

        // call fragment_summary_detail
        summaryDetail summaryDetail = new summaryDetail();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.layout_summaryDetails, summaryDetail).commit();

        actv_month = findViewById(R.id.actv_month);
        dd_month = findViewById(R.id.dd_month);
        months = getResources().getStringArray(R.array.months);
        adapterItems = new ArrayAdapter<String>(this,R.layout.list_item,months);
        actv_month.setAdapter(adapterItems);

        LocalDate localDate = LocalDate.now();
        actv_month.setText(localDate.getMonth().toString(),false);
        monthInNumeric = localDate.getMonthValue();

        //test only
//        LocalDate testDate = LocalDate.now().withMonth(10).withDayOfMonth(1);
//        localDate = testDate;

        // send in numeric month to SummaryInDepthDetail and summaryDetail fragment.
        Bundle bundle = new Bundle();
        if(monthInNumeric < 10) {
            month ="0" + monthInNumeric;
            bundle.putString("monthSelected",month);
            userSelectedMonth = month;
        } else {
            bundle.putString("monthSelected",Integer.toString(monthInNumeric));
            userSelectedMonth = Integer.toString(monthInNumeric);
        }
        summaryDetail.setArguments(bundle);

        actv_month.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getMonthSelected = parent.getItemAtPosition(position).toString();
                monthInNumeric = position+1;
                try {
                    // create new fragment, replace existing fragment with new fragment.
                    summaryDetail newMonthSelected = new summaryDetail();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.layout_summaryDetails, newMonthSelected);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();
                    if(monthInNumeric < 10) {
                        month ="0" + monthInNumeric;
                        bundle.putString("monthSelected",month);
                        userSelectedMonth = month;
                    } else {
                        bundle.putString("monthSelected",Integer.toString(monthInNumeric));
                        userSelectedMonth = Integer.toString(monthInNumeric);

                    }
                    // parse in to fragment to generate data.
                    newMonthSelected.setArguments(bundle);
                } catch (Exception e) { }
            }
        });

        btn_details = findViewById(R.id.btn_details);
        btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Summary.this, SummaryInDepthDetail.class);
                intent.putExtra("monthSelected", userSelectedMonth);
                startActivity(intent);
            }
        });

        // btm nav bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_summary);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_budget:
                        startActivity(new Intent(Summary.this, Budget.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_genOtp:
                        startActivity(new Intent(Summary.this, GenOtpHome.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_summary:
                        return true;
                    case R.id.menu_awareness:
                        startActivity(new Intent(Summary.this, Awareness.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}