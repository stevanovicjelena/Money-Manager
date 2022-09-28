package com.example.moneymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
//import androidx.appcompat.app.AppCompatActivity;


import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

public class DailyAnalyticsActivity extends AppCompatActivity {

    private Toolbar settingsToolbar;

    private FirebaseAuth mAuth;
    private String onlineUserId = "";
    private DatabaseReference expensesRef, personalRef;

    private TextView totalBudgetAmountTextView, analyticsTransportAmount, analyticsFoodAmount, analyticsHouseAmount, analyticsEntertainmentAmount;
    private TextView analyticsEducationAmount, analyticsCharityAmount, analyticsApparelAmount, analyticsHealthAmount, analyticsPersonalAmount, analyticsOtherAmount, monthSpentAmount;

    private RelativeLayout relativeLayoutTransport, relativeLayoutFood, relativeLayoutHouse, relativeLayoutEntertainment, relativeLayoutEducation;
    private RelativeLayout relativeLayoutCharity, relativeLayoutApparel, relativeLayoutHealth, relativeLayoutPersonal, relativeLayoutOther, linearLayoutAnalysis;

    private AnyChartView anyChartView;
    private TextView progress_ratio_transport, progress_ratio_food, progress_ratio_house, progress_ratio_ent, progress_ratio_edu, progress_ratio_cha, progress_ratio_app, progress_ratio_hea, progress_ratio_per, progress_ratio_oth, monthRatioSpending;
    private ImageView status_Image_transport, status_Image_food, status_Image_house, status_Image_entertainment, status_Image_education, status_Image_charity, status_Image_apparel, status_Image_health, status_Image_personal, status_Image_other, monthRatioSpending_image;


   // @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_analytics);

        settingsToolbar = findViewById(R.id.my_Feed_Toolbar);
        setSupportActionBar(settingsToolbar);
        if(getActionBar() != null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Today analytics");

        mAuth = FirebaseAuth.getInstance();
        onlineUserId = mAuth.getCurrentUser().getUid();
        expensesRef = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        personalRef = FirebaseDatabase.getInstance().getReference("personal").child(onlineUserId);

        totalBudgetAmountTextView = findViewById(R.id.totalBudgetAmountTextView);

        //general analytics
        monthSpentAmount = findViewById(R.id.monthSpentAmount);
        linearLayoutAnalysis = findViewById(R.id.linearLayoutAnalysis);
        monthRatioSpending = findViewById(R.id.monthRatioSpending);
        monthRatioSpending_image = findViewById(R.id.monthRatioSpending_image);

        analyticsTransportAmount = findViewById(R.id.analyticsTransportAmount);
        analyticsFoodAmount = findViewById(R.id.analyticsFoodAmount);
        analyticsHouseAmount = findViewById(R.id.analyticsHouseAmount);
        analyticsEntertainmentAmount = findViewById(R.id.analyticsEntertainmentAmount);
        analyticsEducationAmount = findViewById(R.id.analyticsEducationAmount);
        analyticsCharityAmount = findViewById(R.id.analyticsCharityAmount);
        analyticsApparelAmount = findViewById(R.id.analyticsApparelAmount);
        analyticsHealthAmount = findViewById(R.id.analyticsHealthAmount);
        analyticsPersonalAmount = findViewById(R.id.analyticsPersonalAmount);
        analyticsOtherAmount = findViewById(R.id.analyticsOtherAmount);

        //Relative layout views
        relativeLayoutTransport = findViewById(R.id.relativeLayoutTransport);
        relativeLayoutFood = findViewById(R.id.relativeLayoutFood);
        relativeLayoutHouse = findViewById(R.id.relativeLayoutHouse);
        relativeLayoutEntertainment = findViewById(R.id.relativeLayoutEntertainment);
        relativeLayoutEducation = findViewById(R.id.relativeLayoutEducation);
        relativeLayoutCharity = findViewById(R.id.relativeLayoutCharity);
        relativeLayoutApparel = findViewById(R.id.relativeLayoutApparel);
        relativeLayoutHealth = findViewById(R.id.relativeLayoutHealth);
        relativeLayoutPersonal = findViewById(R.id.relativeLayoutPersonal);
        relativeLayoutOther = findViewById(R.id.relativeLayoutOther);

        //TextViews
        progress_ratio_transport = findViewById(R.id.progress_ratio_transport);
        progress_ratio_food = findViewById(R.id.progress_ratio_food);
        progress_ratio_house = findViewById(R.id.progress_ratio_house);
        progress_ratio_ent = findViewById(R.id.progress_ratio_ent);
        progress_ratio_edu = findViewById(R.id.progress_ratio_edu);
        progress_ratio_cha = findViewById(R.id.progress_ratio_cha);
        progress_ratio_app = findViewById(R.id.progress_ratio_app);
        progress_ratio_hea = findViewById(R.id.progress_ratio_hea);
        progress_ratio_per = findViewById(R.id.progress_ratio_per);
        progress_ratio_oth = findViewById(R.id.progress_ratio_oth);

        //Image Views
        status_Image_transport = findViewById(R.id.status_Image_transport);
        status_Image_food = findViewById(R.id.status_Image_food);
        status_Image_house = findViewById(R.id.status_Image_house);
        status_Image_entertainment = findViewById(R.id.status_Image_entertainment);
        status_Image_education = findViewById(R.id.status_Image_education);
        status_Image_charity = findViewById(R.id.status_Image_charity);
        status_Image_apparel = findViewById(R.id.status_Image_apparel);
        status_Image_health = findViewById(R.id.status_Image_health);
        status_Image_personal = findViewById(R.id.status_Image_personal);
        status_Image_other = findViewById(R.id.status_Image_other);

        //AnyChartView
        anyChartView = findViewById(R.id.anyChartView);

        getTotalWeekTransportExpense();
        getTotalWeekFoodExpense();
        getTotalWeekHouseExpense();
        getTotalWeekEntExpense();
        getTotalWeekEduExpense();
        getTotalWeekCharityExpense();
        getTotalWeekAppExpense();
        getTotalWeekHealthExpense();
        getTotalWeekPerExpense();
        getTotalWeekOtherExpense();

        getTotalDaySpending();

        new java.util.Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        loadGraph();
                        setStatusAndImageResource();
                    }
                },
                    2000
                );


    }

    private void getTotalWeekTransportExpense() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set epoch time
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemNday = "Transport"+date;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNday").equalTo(itemNday);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsTransportAmount.setText("Spent: "+totalAmount);
                    }
                    personalRef.child("dayTrans").setValue(totalAmount);
                }else{
                    relativeLayoutTransport.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getTotalWeekFoodExpense() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set epoch time
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemNday = "Food"+date;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNday").equalTo(itemNday);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsFoodAmount.setText("Spent: "+totalAmount);
                    }
                    personalRef.child("dayFood").setValue(totalAmount);
                }else{
                    relativeLayoutFood.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekHouseExpense() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set epoch time
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemNday = "House"+date;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNday").equalTo(itemNday);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsHouseAmount.setText("Spent: "+totalAmount);
                    }
                    personalRef.child("dayHouse").setValue(totalAmount);
                }else{
                    relativeLayoutHouse.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekEntExpense() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set epoch time
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemNday = "Entertainment"+date;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNday").equalTo(itemNday);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsEntertainmentAmount.setText("Spent: "+totalAmount);
                    }
                    personalRef.child("dayEnt").setValue(totalAmount);
                }else{
                    relativeLayoutEntertainment.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekEduExpense() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set epoch time
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemNday = "Education"+date;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNday").equalTo(itemNday);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsEducationAmount.setText("Spent: "+totalAmount);
                    }
                    personalRef.child("dayEdu").setValue(totalAmount);
                }else{
                    relativeLayoutEducation.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekCharityExpense() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set epoch time
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemNday = "Charity"+date;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNday").equalTo(itemNday);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsCharityAmount.setText("Spent: "+totalAmount);
                    }
                    personalRef.child("dayChar").setValue(totalAmount);
                }else{
                    relativeLayoutCharity.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekAppExpense() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set epoch time
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemNday = "Apparel"+date;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNday").equalTo(itemNday);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsApparelAmount.setText("Spent: "+totalAmount);
                    }
                    personalRef.child("dayApp").setValue(totalAmount);
                }else{
                    relativeLayoutApparel.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekHealthExpense() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set epoch time
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemNday = "Health"+date;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNday").equalTo(itemNday);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsHealthAmount.setText("Spent: "+totalAmount);
                    }
                    personalRef.child("dayHealth").setValue(totalAmount);
                }else{
                    relativeLayoutHealth.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekPerExpense() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set epoch time
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemNday = "Personal"+date;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNday").equalTo(itemNday);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsPersonalAmount.setText("Spent: "+totalAmount);
                    }
                    personalRef.child("dayPers").setValue(totalAmount);
                }else{
                    relativeLayoutPersonal.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalWeekOtherExpense() {
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set epoch time
        DateTime now = new DateTime();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());
        String itemNday = "Other"+date;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("itemNday").equalTo(itemNday);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                        analyticsOtherAmount.setText("Spent: "+totalAmount);
                    }
                    personalRef.child("dayOth").setValue(totalAmount);
                }else{
                    relativeLayoutOther.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTotalDaySpending(){

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = dateFormat.format(cal.getTime());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        Query query = reference.orderByChild("date").equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount()>0){
                    int totalAmount = 0;
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        Object total = map.get("amount");
                        int pTotal = Integer.parseInt(String.valueOf(total));
                        totalAmount += pTotal;
                    }
                    totalBudgetAmountTextView.setText("Total day's spending: $" + totalAmount);
                    monthSpentAmount.setText("Total spents: $" +totalAmount);
                }else{
                    totalBudgetAmountTextView.setText("You not spent money today");
                    anyChartView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DailyAnalyticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadGraph(){
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int traTotal;
                    if (snapshot.hasChild("dayTrans")){
                        traTotal = Integer.parseInt(snapshot.child("dayTrans").getValue().toString());
                    }else{
                        traTotal = 0;
                    }

                    int foodTotal;
                    if (snapshot.hasChild("dayFood")){
                        foodTotal = Integer.parseInt(snapshot.child("dayFood").getValue().toString());
                    }else{
                        foodTotal = 0;
                    }

                    int houseTotal;
                    if (snapshot.hasChild("dayHouse")){
                        houseTotal = Integer.parseInt(snapshot.child("dayHouse").getValue().toString());
                    }else{
                        houseTotal = 0;
                    }

                    int entTotal;
                    if (snapshot.hasChild("dayEnt")){
                        entTotal = Integer.parseInt(snapshot.child("dayEnt").getValue().toString());
                    }else{
                        entTotal = 0;
                    }

                    int eduTotal;
                    if (snapshot.hasChild("dayEdu")){
                        eduTotal = Integer.parseInt(snapshot.child("dayEdu").getValue().toString());
                    }else{
                        eduTotal = 0;
                    }

                    int chaTotal;
                    if (snapshot.hasChild("dayChar")){
                        chaTotal = Integer.parseInt(snapshot.child("dayChar").getValue().toString());
                    }else{
                        chaTotal = 0;
                    }

                    int appTotal;
                    if (snapshot.hasChild("dayApp")){
                        appTotal = Integer.parseInt(snapshot.child("dayApp").getValue().toString());
                    }else{
                        appTotal = 0;
                    }

                    int heaTotal;
                    if (snapshot.hasChild("dayHealth")){
                        heaTotal = Integer.parseInt(snapshot.child("dayHealth").getValue().toString());
                    }else{
                        heaTotal = 0;
                    }

                    int perTotal;
                    if (snapshot.hasChild("dayPers")){
                        perTotal = Integer.parseInt(snapshot.child("dayPers").getValue().toString());
                    }else{
                        perTotal = 0;
                    }

                    int othTotal;
                    if (snapshot.hasChild("dayOth")){
                        othTotal = Integer.parseInt(snapshot.child("dayOth").getValue().toString());
                    }else{
                        othTotal = 0;
                    }

                    Pie pie = AnyChart.pie();
                    List<DataEntry> data = new ArrayList<>();
                    data.add(new ValueDataEntry("Transport", traTotal));
                    data.add(new ValueDataEntry("Food", foodTotal));
                    data.add(new ValueDataEntry("House", houseTotal));
                    data.add(new ValueDataEntry("Entertainment", entTotal));
                    data.add(new ValueDataEntry("Education", eduTotal));
                    data.add(new ValueDataEntry("Charity", chaTotal));
                    data.add(new ValueDataEntry("Apparel", appTotal));
                    data.add(new ValueDataEntry("Health", heaTotal));
                    data.add(new ValueDataEntry("Personal", perTotal));
                    data.add(new ValueDataEntry("Other", othTotal));

                    pie.data(data);

                    pie.title("Daily analytics");

                    pie.labels().position("outside");

                    pie.legend().title().enabled(true);
                    pie.legend().title().text("Items spent on").padding(0d,0d,10d,0d);

                    pie.legend().position("center-bottom")
                                .itemsLayout(LegendLayout.HORIZONTAL)
                                .align(Align.CENTER);

                    anyChartView.setChart(pie);
                }
                else{
                    Toast.makeText(DailyAnalyticsActivity.this, "Child does not exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setStatusAndImageResource(){
        personalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int traTotal;
                    if (snapshot.hasChild("dayTrans")){
                        traTotal = Integer.parseInt(snapshot.child("dayTrans").getValue().toString());
                    }else{
                        traTotal = 0;
                    }

                    int foodTotal;
                    if (snapshot.hasChild("dayFood")){
                        foodTotal = Integer.parseInt(snapshot.child("dayFood").getValue().toString());
                    }else{
                        foodTotal = 0;
                    }

                    int houseTotal;
                    if (snapshot.hasChild("dayHouse")){
                        houseTotal = Integer.parseInt(snapshot.child("dayHouse").getValue().toString());
                    }else{
                        houseTotal = 0;
                    }

                    int entTotal;
                    if (snapshot.hasChild("dayEnt")){
                        entTotal = Integer.parseInt(snapshot.child("dayEnt").getValue().toString());
                    }else{
                        entTotal = 0;
                    }

                    int eduTotal;
                    if (snapshot.hasChild("dayEdu")){
                        eduTotal = Integer.parseInt(snapshot.child("dayEdu").getValue().toString());
                    }else{
                        eduTotal = 0;
                    }

                    int chaTotal;
                    if (snapshot.hasChild("dayChar")){
                        chaTotal = Integer.parseInt(snapshot.child("dayChar").getValue().toString());
                    }else{
                        chaTotal = 0;
                    }

                    int appTotal;
                    if (snapshot.hasChild("dayApp")){
                        appTotal = Integer.parseInt(snapshot.child("dayApp").getValue().toString());
                    }else{
                        appTotal = 0;
                    }

                    int heaTotal;
                    if (snapshot.hasChild("dayHealth")){
                        heaTotal = Integer.parseInt(snapshot.child("dayHealth").getValue().toString());
                    }else{
                        heaTotal = 0;
                    }

                    int perTotal;
                    if (snapshot.hasChild("dayPers")){
                        perTotal = Integer.parseInt(snapshot.child("dayPers").getValue().toString());
                    }else{
                        perTotal = 0;
                    }

                    int othTotal;
                    if (snapshot.hasChild("dayOth")){
                        othTotal = Integer.parseInt(snapshot.child("dayOth").getValue().toString());
                    }else{
                        othTotal = 0;
                    }

                   float monthTotalSpentAmount;
                    if (snapshot.hasChild("today")){
                        monthTotalSpentAmount = Integer.parseInt(snapshot.child("today").getValue().toString());
                    }else{
                        monthTotalSpentAmount = 0;
                    }

                    //Getting ratio

                    float traRatio;
                    if (snapshot.hasChild("dayTransRatio")){
                        traRatio = Integer.parseInt(snapshot.child("dayTransRatio").getValue().toString());
                    }else{
                        traRatio = 0;
                    }

                    float foodRatio;
                    if (snapshot.hasChild("dayFoodRatio")){
                        foodRatio = Integer.parseInt(snapshot.child("dayFoodRatio").getValue().toString());
                    }else{
                        foodRatio = 0;
                    }

                    float houseRatio;
                    if (snapshot.hasChild("dayHouseRatio")){
                        houseRatio = Integer.parseInt(snapshot.child("dayHouseRatio").getValue().toString());
                    }else{
                        houseRatio = 0;
                    }

                    float entRatio;
                    if (snapshot.hasChild("dayEntRatio")){
                        entRatio = Integer.parseInt(snapshot.child("dayEntRatio").getValue().toString());
                    }else{
                        entRatio = 0;
                    }

                    float eduRatio;
                    if (snapshot.hasChild("dayEduRatio")){
                        eduRatio = Integer.parseInt(snapshot.child("dayEduRatio").getValue().toString());
                    }else{
                        eduRatio = 0;
                    }

                    float chaRatio;
                    if (snapshot.hasChild("dayCharityRatio")){
                        chaRatio = Integer.parseInt(snapshot.child("dayCharityRatio").getValue().toString());
                    }else{
                        chaRatio = 0;
                    }

                    float appRatio;
                    if (snapshot.hasChild("dayAppRatio")){
                        appRatio = Integer.parseInt(snapshot.child("dayAppRatio").getValue().toString());
                    }else{
                        appRatio = 0;
                    }

                    float heaRatio;
                    if (snapshot.hasChild("dayHealthRatio")){
                        heaRatio = Integer.parseInt(snapshot.child("dayHealthRatio").getValue().toString());
                    }else{
                        heaRatio = 0;
                    }

                    float perRatio;
                    if (snapshot.hasChild("dayPerRatio")){
                        perRatio = Integer.parseInt(snapshot.child("dayPerRatio").getValue().toString());
                    }else{
                        perRatio = 0;
                    }

                    float othRatio;
                    if (snapshot.hasChild("dayOtherRatio")){
                        othRatio = Integer.parseInt(snapshot.child("dayOtherRatio").getValue().toString());
                    }else{
                        othRatio = 0;
                    }

                    float monthTotalSpentAmountRatio;
                    if (snapshot.hasChild("dailyBudget")){
                        monthTotalSpentAmountRatio = Integer.parseInt(snapshot.child("today").getValue().toString());
                    }else{
                        monthTotalSpentAmountRatio = 0;
                    }


                    // Posting green, red or brown image

                    float monthPercent = (monthTotalSpentAmount/monthTotalSpentAmountRatio)*100;
                    if (monthPercent<50){
                        monthRatioSpending.setText(monthPercent + " $" + " used of " + monthTotalSpentAmountRatio + ". Status:");
                        monthRatioSpending_image.setImageResource(R.drawable.green);
                    }else if (monthPercent >= 50 && monthPercent < 100){
                        monthRatioSpending.setText(monthPercent+" $"+" used of "+monthTotalSpentAmountRatio + ". Status:");
                        monthRatioSpending_image.setImageResource(R.drawable.brown);
                    }else{
                        monthRatioSpending.setText(monthPercent+" $"+" used of "+monthTotalSpentAmountRatio + ". Status:");
                        monthRatioSpending_image.setImageResource(R.drawable.red);
                    }

                    float transportPercent = (traTotal/traRatio)*100;
                    if (transportPercent<50){
                        progress_ratio_transport.setText(transportPercent + " $" + " used of " + traRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.green);
                    }else if (transportPercent >= 50 && transportPercent < 100){
                        progress_ratio_transport.setText(transportPercent+" $"+" used of "+traRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.brown);
                    }else{
                        progress_ratio_transport.setText(transportPercent+" $"+" used of "+traRatio + ". Status:");
                        status_Image_transport.setImageResource(R.drawable.red);
                    }

                    float foodPercent = (foodTotal/foodRatio)*100;
                    if (foodPercent<50){
                        progress_ratio_food.setText(foodPercent + " $" + " used of " + foodRatio + ". Status:");
                        status_Image_food.setImageResource(R.drawable.green);
                    }else if (foodPercent >= 50 && foodPercent < 100){
                        progress_ratio_food.setText(foodPercent+" $"+" used of "+foodRatio + ". Status:");
                        status_Image_food.setImageResource(R.drawable.brown);
                    }else{
                        progress_ratio_food.setText(foodPercent+" $"+" used of "+foodRatio + ". Status:");
                        status_Image_food.setImageResource(R.drawable.red);
                    }

                    float housePercent = (houseTotal/houseRatio)*100;
                    if (housePercent<50){
                        progress_ratio_house.setText(housePercent + " $" + " used of " + houseRatio + ". Status:");
                        status_Image_house.setImageResource(R.drawable.green);
                    }else if (housePercent >= 50 && housePercent < 100){
                        progress_ratio_house.setText(housePercent+" $"+" used of "+houseRatio + ". Status:");
                        status_Image_house.setImageResource(R.drawable.brown);
                    }else{
                        progress_ratio_house.setText(housePercent+" $"+" used of "+houseRatio + ". Status:");
                        status_Image_house.setImageResource(R.drawable.red);
                    }

                    float entPercent = (entTotal/entRatio)*100;
                    if (entPercent<50){
                        progress_ratio_ent.setText(entPercent + " $" + " used of " + entRatio + ". Status:");
                        status_Image_entertainment.setImageResource(R.drawable.green);
                    }else if (entPercent >= 50 && entPercent < 100){
                        progress_ratio_ent.setText(entPercent+" $"+" used of "+entRatio + ". Status:");
                        status_Image_entertainment.setImageResource(R.drawable.brown);
                    }else{
                        progress_ratio_ent.setText(entPercent+" $"+" used of "+entRatio + ". Status:");
                        status_Image_entertainment.setImageResource(R.drawable.red);
                    }

                    float eduPercent = (eduTotal/eduRatio)*100;
                    if (eduPercent<50){
                        progress_ratio_edu.setText(eduPercent + " $" + " used of " + eduRatio + ". Status:");
                        status_Image_education.setImageResource(R.drawable.green);
                    }else if (eduPercent >= 50 && eduPercent < 100){
                        progress_ratio_edu.setText(eduPercent+" $"+" used of "+eduRatio + ". Status:");
                        status_Image_education.setImageResource(R.drawable.brown);
                    }else{
                        progress_ratio_edu.setText(eduPercent+" $"+" used of "+eduRatio + ". Status:");
                        status_Image_education.setImageResource(R.drawable.red);
                    }

                    float chaPercent = (chaTotal/chaRatio)*100;
                    if (chaPercent<50){
                        progress_ratio_cha.setText(chaPercent + " $" + " used of " + chaRatio + ". Status:");
                        status_Image_charity.setImageResource(R.drawable.green);
                    }else if (chaPercent >= 50 && chaPercent < 100){
                        progress_ratio_cha.setText(chaPercent+" $"+" used of "+chaRatio + ". Status:");
                        status_Image_charity.setImageResource(R.drawable.brown);
                    }else{
                        progress_ratio_cha.setText(chaPercent+" $"+" used of "+chaRatio + ". Status:");
                        status_Image_charity.setImageResource(R.drawable.red);
                    }

                    float appPercent = (appTotal/appRatio)*100;
                    if (appPercent<50){
                        progress_ratio_app.setText(appPercent + " $" + " used of " + appRatio + ". Status:");
                        status_Image_apparel.setImageResource(R.drawable.green);
                    }else if (appPercent >= 50 && appPercent < 100){
                        progress_ratio_app.setText(appPercent+" $"+" used of "+appRatio + ". Status:");
                        status_Image_apparel.setImageResource(R.drawable.brown);
                    }else{
                        progress_ratio_app.setText(appPercent+" $"+" used of "+appRatio + ". Status:");
                        status_Image_apparel.setImageResource(R.drawable.red);
                    }

                    float heaPercent = (heaTotal/heaRatio)*100;
                    if (heaPercent<50){
                        progress_ratio_hea.setText(heaPercent + " $" + " used of " + heaRatio + ". Status:");
                        status_Image_health.setImageResource(R.drawable.green);
                    }else if (heaPercent >= 50 && heaPercent < 100){
                        progress_ratio_hea.setText(heaPercent+" $"+" used of "+heaRatio + ". Status:");
                        status_Image_health.setImageResource(R.drawable.brown);
                    }else{
                        progress_ratio_hea.setText(heaPercent+" $"+" used of "+heaRatio + ". Status:");
                        status_Image_health.setImageResource(R.drawable.red);
                    }

                    float perPercent = (perTotal/perRatio)*100;
                    if (perPercent<50){
                        progress_ratio_per.setText(perPercent + " $" + " used of " + perRatio + ". Status:");
                        status_Image_personal.setImageResource(R.drawable.green);
                    }else if (perPercent >= 50 && perPercent < 100){
                        progress_ratio_per.setText(perPercent+" $"+" used of "+perRatio + ". Status:");
                        status_Image_personal.setImageResource(R.drawable.brown);
                    }else{
                        progress_ratio_per.setText(perPercent+" $"+" used of "+perRatio + ". Status:");
                        status_Image_personal.setImageResource(R.drawable.red);
                    }

                    float othPercent = (othTotal/othRatio)*100;
                    if (othPercent<50){
                        progress_ratio_oth.setText(othPercent + " $" + " used of " + othRatio + ". Status:");
                        status_Image_other.setImageResource(R.drawable.green);
                    }else if (othPercent >= 50 && othPercent < 100){
                        progress_ratio_oth.setText(othPercent+" $"+" used of "+othRatio + ". Status:");
                        status_Image_other.setImageResource(R.drawable.brown);
                    }else{
                        progress_ratio_oth.setText(othPercent+" $"+" used of "+othRatio + ". Status:");
                        status_Image_other.setImageResource(R.drawable.red);
                    }


                }
                else{
                    Toast.makeText(DailyAnalyticsActivity.this, "Set status and image resource errors", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}