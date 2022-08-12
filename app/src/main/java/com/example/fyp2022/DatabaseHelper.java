package com.example.fyp2022;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

// handles all the database operations
public class DatabaseHelper extends SQLiteOpenHelper {
    SQLiteDatabase db;
    String queryString;
    Cursor cursor;
    public static final String DATABASE_NAME = "ControlSpendingApp2022.db";
    public static final int DATABASE_VERSION = 1;
    // User Table
    public static final String USER_TABLE = "User";
    public static final String COL_USER_ID = "UserID";
    public static final String COL_USER_DATE = "UserDate";
    public static final String COL_BUDGET_TYPE = "BudgetType";
    public static final String COL_BUDGET_SET = "BudgetSet";
    public static final String COL_BUDGET_PERCENT = "BudgetPercent";
    // BudgetDetail Table
    public static final String BUDGETDETAIL_TABLE = "BudgetDetail";
    public static final String COL_BUDGET_ID = "BudgetID";
    public static final String COL_BUDGET_USED = "BudgetUsed";
    public static final String COL_BUDGET_DATE = "BudgetDate";
    // Otp Table
    public static final String OTP_TABLE = "Otp";
    public static final String COL_OTP_ID = "OtpID";
    public static final String COL_OTP = "Otp";
    public static final String COL_OTP_GEN_DATETIME = "OtpGen_DateTime";
    // Friction Table
    public static final String FRICTION_TABLE = "Friction";
    public static final String COL_FRICTION_ID = "FrictionID";
    public static final String COL_FRICTION_TYPE = "FrictionType";
    public static final String COL_FRICTION_DESC = "FrictionDesc";
    public static final String COL_FRICTION_VALIDATION = "FrictionValidation";
    // Budget_Friction Table
    public static final String BUDGET_FRICTION_TABLE = "Budget_Friction";
    public static final String COL_BUDGET_FRICTION_ID = "BudgetFrictionID";
    public static final String COL_BUDGET_FRICTION_VALIDATION = "BudgetFrictionValidation";
    public static final String COL_BUDGET_FRICTION_DATE = "BudgetFrictionDate";
    // Awareness Table
    public static final String AWARENESS_TABLE = "Awareness";
    public static final String COL_AWARE_ID = "AwareID";
    public static final String COL_AWARE_TAG = "AwareTag";
    public static final String COL_AWARE_DATE = "AwareDate";
    public static final String COL_IMAGE_URL = "ImageUrl";
    public static final String COL_AWARE_TITLE = "AwareTitle";
    public static final String COL_AWARE_DESC = "AwareDesc";
    public static final String COL_AWARE_LINK = "AwareLink";


    public DatabaseHelper(@Nullable Context context) {
        // hardcoded value
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    // called first time when database is created. should contain create table code
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createUserTable = "CREATE TABLE  IF NOT EXISTS " + USER_TABLE + " (" +
                COL_USER_ID + " INTEGER, " +
                COL_USER_DATE + " TEXT, " +
                COL_BUDGET_TYPE + " TEXT, " +
                COL_BUDGET_SET + " NUMERIC(16,2), " +
                COL_BUDGET_PERCENT + " REAL, " +
                " PRIMARY KEY ( " + COL_USER_ID + " , " + COL_USER_DATE + " )) ";

        String createBudgetDetailTable = "CREATE TABLE  IF NOT EXISTS " + BUDGETDETAIL_TABLE + " (" +
                COL_BUDGET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_ID + " INTEGER, " +
                COL_USER_DATE + " TEXT, " +
                COL_BUDGET_USED + " NUMERIC(16,2), " +
                COL_BUDGET_DATE + " TEXT, " +
                "FOREIGN KEY ("+ COL_USER_ID +"," +COL_USER_DATE+") REFERENCES "+USER_TABLE+"("+COL_USER_ID+","+COL_USER_DATE+")) ";

        String createOtpTable = "CREATE TABLE IF NOT EXISTS " + OTP_TABLE + " ("+
                COL_OTP_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COL_BUDGET_ID + " INTEGER, "+
                COL_OTP + " TEXT, "+
                COL_OTP_GEN_DATETIME + " TEXT, " +
                "FOREIGN KEY ("+COL_BUDGET_ID +") REFERENCES "+BUDGETDETAIL_TABLE + "("+COL_BUDGET_ID+")) ";

        String createFrictionTable = "CREATE TABLE IF NOT EXISTS " + FRICTION_TABLE + " ("
                + COL_FRICTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_FRICTION_TYPE + " TEXT, "
                + COL_FRICTION_DESC + " TEXT, "
                + COL_FRICTION_VALIDATION + " TEXT ) ";

        String createBudgetFrictionTable = "CREATE TABLE IF NOT EXISTS " + BUDGET_FRICTION_TABLE + " ("
                + COL_BUDGET_FRICTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_BUDGET_ID+ " INTEGER, "
                + COL_FRICTION_ID+ " INTEGER, "
                + COL_BUDGET_FRICTION_VALIDATION + " INTEGER, "
                + COL_BUDGET_FRICTION_DATE + " TEXT, "
                + "FOREIGN KEY("+COL_BUDGET_ID +") REFERENCES " + BUDGETDETAIL_TABLE +"("+ COL_BUDGET_ID +"), "
                + "FOREIGN KEY("+COL_FRICTION_ID +") REFERENCES "+ FRICTION_TABLE + "("+ COL_FRICTION_ID +")) ";

        String createAwarenessTable =" CREATE TABLE IF NOT EXISTS " + AWARENESS_TABLE + " ("
                + COL_AWARE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_AWARE_TAG + " TEXT, "
                + COL_AWARE_DATE + " TEXT, "
                + COL_IMAGE_URL + " TEXT, "
                + COL_AWARE_TITLE + " TEXT, "
                + COL_AWARE_DESC + " TEXT, "
                + COL_AWARE_LINK + " TEXT) ";

        sqLiteDatabase.execSQL(createUserTable);
        sqLiteDatabase.execSQL(createBudgetDetailTable);
        sqLiteDatabase.execSQL(createOtpTable);
        sqLiteDatabase.execSQL(createFrictionTable);
        sqLiteDatabase.execSQL(createBudgetFrictionTable);
        sqLiteDatabase.execSQL(createAwarenessTable);
    }

    // if database version number changes. modify schema if got changes to the database. prevent crashes.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String dropUserTable = "DROP TABLE IF EXISTS " + USER_TABLE ;
        String dropBudgetDetailTable = "DROP TABLE IF EXISTS " + BUDGETDETAIL_TABLE ;
        String dropOtpTable = "DROP TABLE IF EXISTS " + OTP_TABLE ;
        String dropBudgetFrictionTable = "DROP TABLE IF EXISTS " + BUDGET_FRICTION_TABLE ;
        String dropFrictionTable = "DROP TABLE IF EXISTS " + FRICTION_TABLE ;
        String dropAwarenessTable = "DROP TABLE IF EXISTS " + AWARENESS_TABLE ;

        sqLiteDatabase.execSQL(dropUserTable);
        sqLiteDatabase.execSQL(dropBudgetDetailTable);
        sqLiteDatabase.execSQL(dropOtpTable);
        sqLiteDatabase.execSQL(dropFrictionTable);
        sqLiteDatabase.execSQL(dropBudgetFrictionTable);
        sqLiteDatabase.execSQL(dropAwarenessTable);
        onCreate(sqLiteDatabase);
    }

    public boolean addUserBudget(UserModal userModal) {
        // getWritableDatabase -> insert actions
        // get ReadableDatabase -> select (read) actions
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USER_ID, userModal.getUserID());
        cv.put(COL_USER_DATE,userModal.getUserDate());
        cv.put(COL_BUDGET_TYPE,userModal.getBudgetType());
        cv.put(COL_BUDGET_SET, userModal.getBudgetSet());
        cv.put(COL_BUDGET_PERCENT,userModal.getBudgetPercent());

        long insert = db.insert(USER_TABLE, null, cv);
        // fail insert will produce a -1 error
        if (insert == -1 ){
            return false;
        } else {
            return true;
        }
    }

    public int getNextBudgetID() {
        int nextID = 0;
        db=this.getReadableDatabase();
        queryString = "SELECT MAX(" + COL_BUDGET_ID + ") FROM "+BUDGETDETAIL_TABLE +";";
        cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst()) {
            nextID = cursor.getInt(0);
        }
        nextID++;
        cursor.close();
        db.close();

        return nextID++;
    }

    public boolean addBudgetDetail(BudgetModal budgetModal, int userID, String userDate) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USER_ID, userID);
        cv.put(COL_USER_DATE, userDate);
        cv.put(COL_BUDGET_USED,budgetModal.getBudgetUsed());
        cv.put(COL_BUDGET_DATE, budgetModal.getBudgetDate());

        long insert = db.insert(BUDGETDETAIL_TABLE, null, cv);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    //check if user has set budget for the month
    public boolean isBudgetSet(String month){
        boolean result = false;
        db = this.getReadableDatabase();
        queryString = "SELECT EXISTS ( SELECT 1 FROM "+USER_TABLE+
                " WHERE strftime('%m', "+COL_USER_DATE+") = '"+month+"');";
        cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst()) {
            result = cursor.getInt(0) == 1 ? true : false;
        }
        cursor.close();
        db.close();
        return result;
    }

    public double getBudgetSet(String month) {
        double budgetSet = 0;
        db = this.getReadableDatabase();
        queryString = "SELECT " + COL_BUDGET_SET + " FROM " + USER_TABLE +
                " WHERE strftime('%m', "+COL_USER_DATE+") = '"+month+"'; ";
        cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()) {
            budgetSet = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return budgetSet;
    }

    public double getTotalSpendings(String month){
        double totalSpendings = 0;
        db = this.getReadableDatabase();
        queryString = "SELECT SUM(" + COL_BUDGET_USED + ") FROM "+BUDGETDETAIL_TABLE +
                        " WHERE strftime('%m', "+COL_BUDGET_DATE+") = '"+month+"'; ";
        cursor = db.rawQuery(queryString,null);
        if (cursor.moveToFirst()) {
            totalSpendings = round(cursor.getDouble(0),2);
            //totalSpendings = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return totalSpendings;
    }

    public double getHighestSpending(String month) {
        double highestAmtSpent = 0;
        db = this.getReadableDatabase();
        queryString = "SELECT MAX(" + COL_BUDGET_USED + ") FROM "+BUDGETDETAIL_TABLE +
                " WHERE strftime('%m', "+COL_USER_DATE+") = '"+month+"'; ";
        cursor = db.rawQuery(queryString,null);
        if (cursor.moveToFirst()) {
            highestAmtSpent = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return highestAmtSpent;
    }

    public int getTotalTransactions(String month) {
        int numberOfTransactions = 0;
        db = this.getReadableDatabase();
        queryString = "SELECT COUNT(" + COL_BUDGET_USED + ") FROM "+BUDGETDETAIL_TABLE +
                " WHERE strftime('%m', "+COL_USER_DATE+") = '"+month+"'; ";
        cursor = db.rawQuery(queryString,null);
        if (cursor.moveToFirst()) {
            numberOfTransactions = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return numberOfTransactions;
    }


    public String getLatestUserDate() {
        String userDate=null;
        db = this.getReadableDatabase();
        queryString = "SELECT "+ COL_USER_DATE + " FROM " +USER_TABLE + " ORDER BY " + COL_USER_DATE + " DESC;";
        Cursor cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst()) {
            userDate = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return userDate;
    }

    public List<BudgetModal> getBudgetDetails(String month) {
        List<BudgetModal> budgetDetails = new ArrayList<>();
        db = this.getReadableDatabase();
        queryString = "SELECT "+ COL_BUDGET_ID + " , " +
                COL_BUDGET_USED + " , " +
                COL_BUDGET_DATE + " FROM "
                +BUDGETDETAIL_TABLE+
                " WHERE strftime('%m', "+COL_BUDGET_DATE+") = '"+month+"'; ";
        Cursor cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst()) {
            do {
                int budgetID = cursor.getInt(0);
                double budgetUsed = cursor.getDouble(1);
                String budgetDate = cursor.getString(2);
                BudgetModal getBudgetDetail = new BudgetModal(budgetID,budgetUsed,budgetDate);
                budgetDetails.add(getBudgetDetail);
            } while(cursor.moveToNext());
        } else {}
        cursor.close();
        db.close();
        return budgetDetails;
    }

    public List<UserModal> getUserDetails() {
        List<UserModal> userDetails = new ArrayList<>();
        queryString = "SELECT * FROM " + USER_TABLE;
        db = this.getReadableDatabase();
        cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()) {
            do {
                int userID = cursor.getInt(0);
                String userDate = cursor.getString(1);
                String budgetType = cursor.getString(2);
                double budgetSet = cursor.getDouble(3);
                double budgetPercent = cursor.getDouble(4);
                UserModal getUser = new UserModal(userID, userDate, budgetType, budgetSet, budgetPercent);
                userDetails.add(getUser);
            } while (cursor.moveToNext());
        } else { }
        cursor.close();
        db.close();
        return userDetails;
    }

    public int getNextOtpID() {
        int nextID = 0;
        db=this.getReadableDatabase();
        queryString = "SELECT MAX(" + COL_OTP_ID + ") FROM "+OTP_TABLE +";";
        cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst()) {
            nextID = cursor.getInt(0);
        }
        nextID++;
        cursor.close();
        db.close();
        return nextID++;
    }

    public boolean addOtpDetails(OtpModal otpModal, int budgetID) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_BUDGET_ID, budgetID);
        cv.put(COL_OTP, otpModal.getOtp());
        cv.put(COL_OTP_GEN_DATETIME,otpModal.getOtpGen_DateTime());

        long insert = db.insert(OTP_TABLE, null, cv);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public String getOtpGenerated() {
        String genOtp = null;
        db = this.getReadableDatabase();
        queryString = "SELECT "+COL_OTP+ " FROM "+OTP_TABLE +
                        " ORDER BY "+COL_OTP_ID+ " DESC LIMIT 1;";
        cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst()) {
            genOtp = cursor.getString(0);
        }
        return genOtp;
    }

    public int getNextAwarenessID() {
        int nextID = 0;
        db=this.getReadableDatabase();
        queryString = "SELECT MAX(" + COL_AWARE_ID + ") FROM "+AWARENESS_TABLE +";";
        cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst()) {
            nextID = cursor.getInt(0);
        }
        nextID++;
        cursor.close();
        db.close();

        return nextID++;
    }

    public boolean addAwarenessDetail(AwarenessModal awarenessModal) {
        // getWritableDatabase -> insert actions
        // get ReadableDatabase -> select (read) actions
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_AWARE_ID, awarenessModal.getAwareID());
        cv.put(COL_AWARE_TAG,awarenessModal.getAwareTag());
        cv.put(COL_AWARE_DATE,awarenessModal.getAwareDate());
        cv.put(COL_IMAGE_URL, awarenessModal.getImageUrl());
        cv.put(COL_AWARE_TITLE,awarenessModal.getAwareTitle());
        cv.put(COL_AWARE_DESC,awarenessModal.getAwareDesc());
        cv.put(COL_AWARE_LINK, awarenessModal.getAwareLink());

        long insert = db.insert(AWARENESS_TABLE, null, cv);
        // fail insert will produce a -1 error
        if (insert == -1 ){
            return false;
        } else {
            return true;
        }
    }
    public List<AwarenessModal> getAwarenessDetailInDesc () {
        List<AwarenessModal> awarenessDetail = new ArrayList<>();
        db = this.getReadableDatabase();
        queryString = "SELECT * FROM "+AWARENESS_TABLE+ " ORDER BY "+COL_AWARE_DATE+" DESC;";
        Cursor cursor = db.rawQuery(queryString,null);
        if (cursor.moveToFirst()) {
            do {
                int awareID = cursor.getInt(0);
                String awareTag = cursor.getString(1);
                String awareDate = cursor.getString(2);
                String imageUrl = cursor.getString(3);
                String awareTitle = cursor.getString(4);
                String awareDesc = cursor.getString(5);
                String awareLink = cursor.getString(6);
                AwarenessModal getAwarenessDetail = new AwarenessModal(awareID,awareTag,awareDate,imageUrl, awareTitle,awareDesc,awareLink);
                awarenessDetail.add(getAwarenessDetail);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return awarenessDetail;
    }

    public boolean isAwarenessDetailCreated(){
        boolean result = false;
        db = this.getReadableDatabase();
        queryString = "SELECT EXISTS ( SELECT 1 FROM "+AWARENESS_TABLE + ");";
        cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst()) {
            result = cursor.getInt(0) == 1 ? true : false;
        }
        cursor.close();
        db.close();
        return result;
    }

    public boolean addFrictionDetail(FrictionModal frictionModal) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_FRICTION_ID, frictionModal.getFrictionID());
        cv.put(COL_FRICTION_TYPE,frictionModal.getFrictionType());
        cv.put(COL_FRICTION_DESC,frictionModal.getFrictionDesc());
        cv.put(COL_FRICTION_VALIDATION, frictionModal.getFrictionValidation());

        long insert = db.insert(FRICTION_TABLE, null, cv);
        // fail insert will produce a -1 error
        if (insert == -1 ){
            return false;
        } else {
            return true;
        }
    }

    public boolean isFrictionDetailsCreated(){
        boolean result = false;
        db = this.getReadableDatabase();
        queryString = "SELECT EXISTS ( SELECT 1 FROM "+FRICTION_TABLE + ");";
        cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst()) {
            result = cursor.getInt(0) == 1 ? true : false;
        }
        cursor.close();
        db.close();
        return result;
    }

    public List<FrictionModal> getFrictionDetailsWithSpecificType (String type) {
        List<FrictionModal> frictionDetails = new ArrayList<>();
        db = this.getReadableDatabase();
        queryString = "SELECT * FROM "+FRICTION_TABLE+ " WHERE "+COL_FRICTION_TYPE + " = '" + type+"';";
        Cursor cursor = db.rawQuery(queryString,null);
        if (cursor.moveToFirst()) {
            do {
                int frictionID = cursor.getInt(0);
                String frictionType = cursor.getString(1);
                String frictionDesc = cursor.getString(2);
                String frictionValidation = cursor.getString(3);
                FrictionModal getFrictionDetail = new FrictionModal(frictionID,frictionType,frictionDesc,frictionValidation);
                frictionDetails.add(getFrictionDetail);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return frictionDetails;
    }

    public int getNextBudgetFrictionID() {
        int nextID = 0;
        db=this.getReadableDatabase();
        queryString = "SELECT MAX(" + COL_BUDGET_FRICTION_ID + ") FROM "+BUDGET_FRICTION_TABLE +";";
        cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst()) {
            nextID = cursor.getInt(0);
        }
        nextID++;
        cursor.close();
        db.close();

        return nextID++;
    }

    public boolean addBudgetFrictionDetail(BudgetFrictionModal budgetFrictionModal) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_BUDGET_ID, budgetFrictionModal.getBudgetID());
        cv.put(COL_FRICTION_ID, budgetFrictionModal.getFrictionID());
        cv.put(COL_BUDGET_FRICTION_VALIDATION,budgetFrictionModal.getBudgetFrictionValidation());
        cv.put(COL_BUDGET_FRICTION_DATE,budgetFrictionModal.getBudgetFrictionDate());

        long insert = db.insert(BUDGET_FRICTION_TABLE, null, cv);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isBudgetFrictionDataCreated(String month){
        boolean result = false;
        db = this.getReadableDatabase();
        queryString = "SELECT EXISTS ( SELECT 1 FROM "+BUDGET_FRICTION_TABLE +
                        " WHERE strftime('%m', "+COL_BUDGET_FRICTION_DATE+") = '"+month+"'););";
        cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst()) {
            result = cursor.getInt(0) == 1 ? true : false;
        }
        cursor.close();
        db.close();
        return result;
    }

    public int countTimesFrictionPointsTriggered(String month) {
        int count = 0;
        db = this.getReadableDatabase();
        queryString = "SELECT COUNT (DISTINCT "+COL_BUDGET_ID + " ) FROM " +BUDGET_FRICTION_TABLE +
                " WHERE strftime('%m', "+COL_BUDGET_FRICTION_DATE+") = '"+month+"' ;";
        Cursor cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    // method obtained from https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
    public double round(double value, int decimalPlaces) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
