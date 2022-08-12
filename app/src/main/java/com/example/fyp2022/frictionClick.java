
package com.example.fyp2022;

import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import java.util.Random;

public class frictionClick extends Fragment {

    private int userClicks;
    private int frictionID;
    TextView tv_requiredClicks, tv_userClicks, tv_clickTitle;
    ConstraintLayout layout_frictionCLick;
    List<FrictionModal> frictionList;
    boolean success;
    DatabaseHelper dbHelper;
    Runnable r;
    Handler handler;
    PassDataInterface passDataInterface;
    int successIDs, failureIDs;

    public frictionClick(PassDataInterface passDataInterface) {
        this.passDataInterface = passDataInterface;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.fragment_friction_click, container, false);

        successIDs = 0;
        failureIDs = 0;
        userClicks = 0;
        tv_clickTitle = view.findViewById(R.id.tv_clickTitle);
        tv_requiredClicks = view.findViewById(R.id.tv_requiredClicks);
        tv_userClicks = view.findViewById(R.id.tv_userClicks);
        layout_frictionCLick = view.findViewById(R.id.layout_frictionClick);

        // set required clicks
        Random random = new Random();
        dbHelper = new DatabaseHelper(getActivity());
        String frictionTypeToGet = "Click";
        frictionList = dbHelper.getFrictionDetailsWithSpecificType(frictionTypeToGet);

        // generate a random int from 0 to list size.
        int num = random.nextInt(frictionList.size());
        frictionID = frictionList.get(num).getFrictionID();
        String titleSet = frictionList.get(num).getFrictionDesc();
        int getNumOfClicks = Integer.parseInt(frictionList.get(num).getFrictionValidation());

        tv_requiredClicks.setText(Integer.toString((getNumOfClicks)));
        tv_clickTitle.setText(titleSet);
        r = new Runnable() {
            @Override
            public void run() {
                // close fragment. return success or fail depending if got hit until the required clicks.
                if (userClicks >= getNumOfClicks){
                    success = true;
                    successIDs = frictionID;
                } else {
                    success = false;
                    failureIDs = frictionID;
                    Toast.makeText(getActivity(), "You failed! ",Toast.LENGTH_SHORT).show();
                }
                // parse success to activity to go ahead and generate otp
                passDataInterface.onDataReceived(success, successIDs, failureIDs);
            }
        };

        layout_frictionCLick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userClicks++;
                tv_userClicks.setText(Integer.toString(userClicks));
            }
        });
        handler = new Handler();
        handler.postDelayed(r,10000);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // prevent null activity
        handler.removeCallbacks(r);

    }
}

