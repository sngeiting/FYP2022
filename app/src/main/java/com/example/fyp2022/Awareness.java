package com.example.fyp2022;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class Awareness extends AppCompatActivity {

    DatabaseHelper dbHelper;
    List<AwarenessModal> awarenessList;

    private RecyclerView recyclerView;
    private AwarenessRecycleViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_awareness);

        dbHelper = new DatabaseHelper(Awareness.this);
        awarenessList = dbHelper.getAwarenessDetailInDesc();
        recyclerView = findViewById(R.id.lv_awarenessDetail);
        recyclerView.hasFixedSize();

        // using linear layout manager
        layoutManager = new LinearLayoutManager(Awareness.this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new AwarenessRecycleViewAdapter(awarenessList,Awareness.this );
        recyclerView.setAdapter(mAdapter);

        // btm nav bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_awareness);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_budget:
                        startActivity(new Intent(Awareness.this, Budget.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_genOtp:
                        startActivity(new Intent(Awareness.this, GenOtpHome.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_summary:
                        startActivity(new Intent(Awareness.this, Summary.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_awareness:
                        return true;
                }
                return false;
            }
        });
    }
}
