package com.example.fyp2022;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;

public class SetBudget extends AppCompatActivity {
    Button btn_confirm;
    RadioGroup radioGrp_setBudget;
    RadioButton radioBtn_Budget, radioBtn_Income;
    EditText et_income;
    TextView tv_budgetSetAmt;
    String budgetSet;
    double percentage;
    String currentDate;
    ActivityResultLauncher activityResultLauncher;

    // dropdown
    AutoCompleteTextView actv_budgetAmt, actv_budgetPercent;
    TextInputLayout dd_budget,dd_budgetPercent;
    ArrayAdapter<String> adapterItems;
    String[] fixedBudget, fixedPercent;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide action bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_set_budget);

        LocalDate localDate = LocalDate.now();
        currentDate = localDate.toString();

        // testing
//        LocalDate testDate = LocalDate.now().withMonth(10).withDayOfMonth(1);
//        localDate = testDate;

        //dropdown
        dd_budget = findViewById(R.id.dd_budget);
        actv_budgetAmt = findViewById(R.id.actv_budgetAmt);
        fixedBudget = getResources().getStringArray(R.array.fixedBudget);
        adapterItems = new ArrayAdapter<String>(this,R.layout.list_item,fixedBudget);
        actv_budgetAmt.setAdapter(adapterItems);

        dd_budgetPercent = findViewById(R.id.dd_budgetPercent);
        actv_budgetPercent = findViewById(R.id.actv_budgetPercent);
        fixedPercent = getResources().getStringArray(R.array.fixedPercent);
        adapterItems = new ArrayAdapter<String>(this,R.layout.list_item, fixedPercent);
        actv_budgetPercent.setAdapter(adapterItems);
        actv_budgetPercent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String item = parent.getItemAtPosition(position).toString();
                    percentage = Double.parseDouble(item) / 100;
                    calcBudget();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Please set a percentage!" , Toast.LENGTH_SHORT).show();
                }
            }
        });


        et_income = findViewById(R.id.et_income);
        tv_budgetSetAmt = findViewById(R.id.tv_budgetSetAmt);
        tv_budgetSetAmt.setText("0");
        et_income.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    calcBudget();
                } catch (Exception e) { }
            }
        });

        //Radio btn
        radioBtn_Budget = findViewById(R.id.radioBtn_Budget);
        radioBtn_Income = findViewById(R.id.radioBtn_Income);
        radioGrp_setBudget = (RadioGroup) findViewById(R.id.radioGrp_setBudget);

        btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open popup
                Intent popupIntent = new Intent(SetBudget.this, PopUp.class);
                String message = "Are you sure? Once set, the budget cannot be changed until next month.";
                popupIntent.putExtra("message",message);
                activityResultLauncher.launch(popupIntent);
            }
        });

        try {
            // get data from PopUp class
            activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                UserModal newBudget = null;
                                try {
                                    if (radioBtn_Budget.isChecked()) {
                                        newBudget = new UserModal(1,currentDate,"budget"
                                                                ,Double.parseDouble(actv_budgetAmt.getText().toString())
                                                                ,0.0);
                                        budgetSet = actv_budgetAmt.getText().toString();
                                        Intent intent = new Intent(SetBudget.this, Budget.class);
                                        startActivity(intent);
                                    }
                                    if (radioBtn_Income.isChecked()) {
                                        if (!(tv_budgetSetAmt.getText().toString().equalsIgnoreCase("0"))) {
                                            budgetSet = tv_budgetSetAmt.getText().toString();
                                            newBudget = new UserModal(1,currentDate,"income",Double.parseDouble(budgetSet),percentage);
                                            Intent intent = new Intent(SetBudget.this, Budget.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(SetBudget.this, "Budget cannot be 0",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(SetBudget.this, "Error Setting Budget",Toast.LENGTH_SHORT).show();
                                }
                                // initialize databasehelper
                                try {
                                    DatabaseHelper databaseHelper = new DatabaseHelper(SetBudget.this);
                                    boolean success = databaseHelper.addUserBudget(newBudget);
                                } catch (Exception e) { }
                            }
                        }
                    });
        } catch(Exception e) { }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_budget);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_budget:
                        return true;
                    case R.id.menu_genOtp:
                        return false;
                    case R.id.menu_summary:
                        startActivity(new Intent(SetBudget.this, Summary.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_awareness:
                        startActivity(new Intent(SetBudget.this, Awareness.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    public void onCheckedButton(View view) {
        // get id of the radio item checked,
        switch(view.getId()) {
            case R.id.radioBtn_Budget:
                dd_budget.setEnabled(true);
                et_income.setEnabled(false);
                dd_budgetPercent.setEnabled(false);
                break;
            case R.id.radioBtn_Income:
                dd_budget.setEnabled(false);
                et_income.setEnabled(true);
                dd_budgetPercent.setEnabled(true);
                break;
        }
    }

    public void calcBudget() {
        double calculate = Double.parseDouble(et_income.getText().toString()) * percentage;
        tv_budgetSetAmt.setText(Double.toString(calculate));
    }
}
