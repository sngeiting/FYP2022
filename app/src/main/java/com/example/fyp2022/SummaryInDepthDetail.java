package com.example.fyp2022;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class SummaryInDepthDetail extends AppCompatActivity {

    ImageView iv_SID_backBtn;
    TextView tv_SID_backBtn;
    private RecyclerView recyclerView;
    private SummaryDetailRecycleViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    String month;
    DatabaseHelper dbHelper;
    List<BudgetModal> budgetDetails;
    ConstraintLayout layout_indepthSummaryDetails;
    TextView tv_replaceTxt, tv_goodJobTxt;

    public SummaryInDepthDetail() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        // set animation
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        setContentView(R.layout.activity_summary_in_depth_detail);

        iv_SID_backBtn = findViewById(R.id.iv_SID_backBtn);
        tv_SID_backBtn = findViewById(R.id.tv_SID_backBtn);

        tv_SID_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SummaryInDepthDetail.this,Summary.class);
                startActivity(intent);
            }
        });
        iv_SID_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SummaryInDepthDetail.this,Summary.class);
                startActivity(intent);
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            month = extras.getString("monthSelected");
        }

        layout_indepthSummaryDetails = findViewById(R.id.layout_indepthSummaryDetails);
        tv_replaceTxt = findViewById(R.id.tv_replaceTxt);
        tv_goodJobTxt = findViewById(R.id.tv_goodJobTxt);

        dbHelper = new DatabaseHelper(SummaryInDepthDetail.this);
        budgetDetails = dbHelper.getBudgetDetails(month);

        if (budgetDetails.size() !=0) {
            layout_indepthSummaryDetails.setVisibility(View.VISIBLE);
            tv_replaceTxt.setVisibility(View.GONE);
            tv_goodJobTxt.setVisibility(View.GONE);
            recyclerView = findViewById(R.id.lv_summaryDetails);
            recyclerView.hasFixedSize();

            // using linear layout manager
            layoutManager = new LinearLayoutManager(SummaryInDepthDetail.this);
            recyclerView.setLayoutManager(layoutManager);
            mAdapter = new SummaryDetailRecycleViewAdapter(budgetDetails,SummaryInDepthDetail.this );
            recyclerView.setAdapter(mAdapter);
        } else {
            layout_indepthSummaryDetails.setVisibility(View.INVISIBLE);
            tv_replaceTxt.setVisibility(View.VISIBLE);
            tv_replaceTxt.setText("No spendings this month.");
            tv_goodJobTxt.setText("Good Job! ");
        }

        // btm nav bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_summary);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_budget:
                        startActivity(new Intent(SummaryInDepthDetail.this, Budget.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_genOtp:
                        startActivity(new Intent(SummaryInDepthDetail.this, GenOtpHome.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_summary:
                        return true;
                    case R.id.menu_awareness:
                        startActivity(new Intent(SummaryInDepthDetail.this, Awareness.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}