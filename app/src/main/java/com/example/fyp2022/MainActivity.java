
package com.example.fyp2022;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Button btn_go;
    DatabaseHelper dbHelper;
    List<AwarenessModal> awarenessList = new ArrayList<>();
    List<FrictionModal> frictionList = new ArrayList<>();
    List<String> tipList = new ArrayList<>();
    TextView tv_tipOfTheDay;
    boolean isBudgetSet;
    String month;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        tv_tipOfTheDay = findViewById(R.id.tv_tipOfTheDay);
        dbHelper = new DatabaseHelper(MainActivity.this);
        isBudgetSet = dbHelper.isBudgetSet(month);

        btn_go = findViewById(R.id.btn_go);
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (!isBudgetSet) {
                    // if user have not set a budget for the month
                    intent = new Intent(MainActivity.this, SetBudget.class);
                } else {
                    // budget is set
                    intent = new Intent(MainActivity.this, Budget.class);
                }
                startActivity(intent);
            }
        });

        dbHelper = new DatabaseHelper(MainActivity.this);
        if (awarenessList.size() == 0) {
            fillAwareData();
        }
        if (frictionList.size() == 0) {
            fillFrictionData();
        }
        if(tipList.size() == 0 ) {
            fillTipData();
        }

        // generate a random int from 0 to list size.
        Random random = new Random();
        int num = random.nextInt(tipList.size());
        String tipOfTheDay = tipList.get(num);
        tv_tipOfTheDay.setText(tipOfTheDay);
    }

    private void fillAwareData() {
        // int awareID = dbHelper.getNextAwarenessID();
        AwarenessModal aware0 = new AwarenessModal(1, "News", "2022-02-08",
                "https://media.asiaone.com/sites/default/files/styles/article_main_image/public/original_images/Feb2022/20220208_girl_pixabay.jpg?itok=4-WE_i3Y",
                "Dad gets $20k reimbursed after 18-year-old daughter Genshin Impact gacha spree",
                "Back in 2021, Mr Lim Cheng Mong, a product manager, was confused when he found himself with $20,000 in overdue credit card debt linked to " +
                        "89 mysterious transactions.",
                "https://www.asiaone.com/singapore/dad-gets-20k-reimbursed-after-18-year-old-daughters-genshin-impact-gacha-spree?utm_" +
                        "medium=Social&utm_source=Facebook&fbclid=IwAR3Kd3epangwP2CDp8P3TpWja26hF2Js8ctqNXBLxm3xeSU64QOorTrr8WY#Echobox=1644289862");
        AwarenessModal aware1 = new AwarenessModal(2, "News", "2021-06-20",
                "https://onecms-res.cloudinary.com/image/upload/s--yqJwvhqN--/c_fill%2Cg_auto%2Ch_468%2Cw_830/f_auto%2Cq_auto/ncpg-loot-boxes-and-" +
                        "gambling-main.jpg?itok=M-KYqKsJ",
                "I spent $20,000 of my parents’ money on mystery boxes’: When lines between gaming and gambling are blurred",
                "SINGAPORE: You’re a teenager with S$50 to spare, and rather than clothes or gadgets for yourself, you decide to spend on your " +
                        "favourite online game — specifically, your character or avatar.",
                "https://www.channelnewsasia.com/cnainsider/spent-parents-money-mystery-loot-boxes-gaming-problem-gambling-2050696");
        AwarenessModal aware2 = new AwarenessModal(3, "News", "2019-11-25",
                "https://static1.makeuseofimages.com/wordpress/wp-content/uploads/2019/11/video-games-trick.jpg?q=50&fit=contain&w=1500&h=750&dpr=1.5",
                "6 Ways Video Games Trick You Into Spending Money",
                "Buying a game doesn't mean the end of you spending money on that title. Instead, gamers are being pressured into spending " +
                        "more money on games they have already paid to play.",
                "https://www.makeuseof.com/tag/video-games-trick-spending-money/");
        AwarenessModal aware3 = new AwarenessModal(4, "Tips", "2021-08-18",
                "https://www.ispcc.ie/wp-content/uploads/2021/11/gaming-1024x683-1.jpg",
                "Online video games and children: How to control their spending",
                "When it comes to entertaining children of any age, particularly during the school holidays, the pastime of choice for " +
                        "many children and teenagers is playing video games. " +
                        "Online gaming allows people to play in real-time with like-minded individuals across the world through a computer, " +
                        "games console, tablet or smartphone connected to the internet. ",
                "https://www.ispcc.ie/online-video-games-and-children-how-to-control-their-spending/");

        awarenessList.addAll(Arrays.asList(aware0, aware1, aware2, aware3));

        if (!dbHelper.isAwarenessDetailCreated()) {
            for (AwarenessModal item : awarenessList) {
                // insert into database
                dbHelper.addAwarenessDetail(item);
            }
        }
    }

    private void fillFrictionData() {
        FrictionModal f0 = new FrictionModal(1, "Text",
                "Are you sure you want to spend?",
                "Pr0c33d With Paym3nt");
        FrictionModal f1 = new FrictionModal(2, "Text",
                "Did you think this through? ",
                "Yes I Have Thought This Through");
        FrictionModal f2 = new FrictionModal(3, "Text",
                "Are you sure you want to continue?",
                "cOnT1Nu3");
        FrictionModal f3 = new FrictionModal(4, "Text",
                "Do you think it is worth to spend?",
                "Yes. It IS S0 W0RTH To SpEnD!");
        FrictionModal f4 = new FrictionModal(5, "Text",
                "Are you really really sure?",
                "Yes. I AM really ReAlLy REALLY sure.");
        FrictionModal f5 = new FrictionModal(6, "Text",
                "Are you really really REALLY sure?",
                "Y3S. I Am R3a11y R3A11y R3A11Y sure.");
        FrictionModal f6 = new FrictionModal(7, "Text",
                "Reconsider again?",
                "No. JuSt. GiVe. ME. tHe. oTp. AlReAdY!");
        FrictionModal f7 = new FrictionModal(8, "Click",
                "Click Click Click! 75  times!",
                "75");

        frictionList.addAll(Arrays.asList(f0, f1, f2, f3, f4, f5, f6,f7));
        if (!dbHelper.isFrictionDetailsCreated()) {
            for (FrictionModal item : frictionList) {
                // insert into database
                dbHelper.addFrictionDetail(item);
            }
        }
    }

    private void fillTipData() {
        tipList = Arrays.asList("Just taking an extra day to think about your purchase is all it takes to reduce spending?",
                "Before making a purchase, always ask yourself. Is it a need or is it a want?",
                "Spending on real life items are much more worth it than virtual assets.");
    }
}

