package com.example.vacationplanner.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacationplanner.R;
import com.example.vacationplanner.database.Repository;
import com.example.vacationplanner.entities.Excursion;
import com.example.vacationplanner.entities.Transportation;
import com.example.vacationplanner.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationDetailsActivity extends AppCompatActivity {
    String title;
    String hotel;
    String start_date;
    String end_date;
    int vacationID;
    EditText editTitle;
    EditText editHotel;
    EditText editStart;
    EditText editEnd;
    Spinner editTransport;
    ArrayAdapter<Transportation> transportAdapter;
    Repository repository;
    DatePickerDialog.OnDateSetListener startDate;
    DatePickerDialog.OnDateSetListener endDate;
    final Calendar startCalender = Calendar.getInstance();
    final Calendar endCalender = Calendar.getInstance();
    Transportation transport;
    Transportation[] transportMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        transportMethods = Transportation.values();
        transportAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, transportMethods);
        editTransport = findViewById(R.id.transport);
        editTransport.setAdapter(transportAdapter);
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editTitle = findViewById(R.id.titletext);
        editHotel = findViewById(R.id.hoteltext);
        editStart = findViewById(R.id.startdate);
        editEnd = findViewById(R.id.enddate);
        vacationID = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        hotel = getIntent().getStringExtra("hotel");
        start_date = getIntent().getStringExtra("start");
        end_date = getIntent().getStringExtra("end");
        transport = (Transportation) getIntent().getSerializableExtra("transport");
        editTitle.setText(title);
        editHotel.setText(hotel);
        editStart.setText(start_date);
        editEnd.setText(end_date);
        for (int i = 0; i < transportMethods.length; i++){
            if(transportMethods[i].equals(transport)){
                editTransport.setSelection(i);
                break;
            }
        }
        editStart.setEnabled(false);
        editEnd.setEnabled(false);

        FloatingActionButton floatButton = findViewById(R.id.floatingActionButton2);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationDetailsActivity.this, ExcursionActivity.class);
                intent.putExtra("vacationID", vacationID);
                startActivity(intent);
            }
        });

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Vacation vacation;
                String title = editTitle.getText().toString();
                String hotel = editHotel.getText().toString();
                String start = editStart.getText().toString();
                String end = editEnd.getText().toString();
                Transportation transport = (Transportation) editTransport.getSelectedItem();
                DateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
                if (title.isEmpty() || hotel.isEmpty() || start.isEmpty() || end.isEmpty()){
                    Toast.makeText(VacationDetailsActivity.this, "All fields must be filled.", Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        Date dateStart = dateFormat.parse(start);
                        Date dateEnd = dateFormat.parse(end);
                        if (dateEnd.before(dateStart)) {
                            Toast.makeText(VacationDetailsActivity.this, "End date must come after start date", Toast.LENGTH_LONG).show();
                        } else {
                            if (vacationID == -1) {
                                if (repository.getAllVacations().size() == 0) vacationID = 1;
                                else
                                    vacationID = repository.getAllVacations().get(repository.getAllVacations().size() - 1).getVacationID() + 1;
                                vacation = new Vacation(vacationID, title, hotel, start, end, transport);
                                repository.insert(vacation);
                            } else {
                                vacation = new Vacation(vacationID, title, hotel, start, end, transport);
                                repository.update(vacation);
                            }
                            finish();
                        }
                    } catch (ParseException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });


        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vacation vacation;
                String title = editTitle.getText().toString();
                String hotel = editHotel.getText().toString();
                String start = editStart.getText().toString();
                String end = editEnd.getText().toString();
                Transportation transport = (Transportation) editTransport.getSelectedItem();
                if(vacationID != -1){
                    vacation = new Vacation(vacationID, title, hotel, start, end, transport);
                    List<Excursion> excursions = repository.getAssociatedExcursions(vacationID);
                    if(!excursions.isEmpty()){
                        Toast.makeText(VacationDetailsActivity.this, "Unable to delete a product with excursions", Toast.LENGTH_LONG).show();
                        return;
                    }
                    else{
                        repository.delete(vacation);
                    }
                }
                finish();
            }
        });

        Button startDateButton = findViewById(R.id.startdatebutton);
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info;
                String test = editStart.getText().toString();;
                if (test.isEmpty()){
                    info = sdf.format(new Date());
                } else info = test;
                try{
                    startCalender.setTime(sdf.parse(info));
                } catch (ParseException e){
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetailsActivity.this, startDate, startCalender.get(Calendar.YEAR),
                        startCalender.get(Calendar.MONTH), startCalender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                startCalender.set(Calendar.YEAR, year);
                startCalender.set(Calendar.MONTH, month);
                startCalender.set(Calendar.DAY_OF_MONTH, day);
                editStart.setText(sdf.format(startCalender.getTime()));
            }
        };

        Button endDateButton = findViewById(R.id.enddatebutton);
        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info;
                String test = editEnd.getText().toString();;
                if (test.isEmpty()){
                    info = sdf.format(new Date());
                } else info = test;
                try{
                    endCalender.setTime(sdf.parse(info));
                } catch (ParseException e){
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetailsActivity.this, endDate, endCalender.get(Calendar.YEAR),
                        endCalender.get(Calendar.MONTH), endCalender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        endDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                endCalender.set(Calendar.YEAR, year);
                endCalender.set(Calendar.MONTH, month);
                endCalender.set(Calendar.DAY_OF_MONTH, day);
                editEnd.setText(sdf.format(endCalender.getTime()));
            }
        };

        RecyclerView recyclerView = findViewById(R.id.excursionRecyclerView);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for(Excursion excursion : repository.getAllExcursions()){
            if (excursion.getVacationID() == vacationID) filteredExcursions.add(excursion);
        }
        excursionAdapter.setExcursions(filteredExcursions);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.vacation_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        if(item.getItemId() == R.id.share){
            String title = editTitle.getText().toString();
            String hotel = editHotel.getText().toString();
            String start = editStart.getText().toString();
            String end = editEnd.getText().toString();
            StringBuilder shareText = new StringBuilder();
            if(!title.isEmpty() && !hotel.isEmpty() && !start.isEmpty() && !end.isEmpty()){
                shareText.append("Title: ").append(title);
                shareText.append("\nHotel: ").append(hotel);
                shareText.append("\nStart Date: ").append(start);
                shareText.append("\nEnd Date: ").append(end);
                List<Excursion> excursions = repository.getAssociatedExcursions(vacationID);
                if(excursions != null && !excursions.isEmpty()){
                    shareText.append("\n\nExcursions:");
                    for(Excursion excursion : excursions){
                        shareText.append("\nTitle: ").append(excursion.getExcursionName());
                        shareText.append("\nDate: ").append(excursion.getDate());
                    }
                }
                Intent sentIntent = new Intent();
                sentIntent.setAction(Intent.ACTION_SEND);
                sentIntent.putExtra(Intent.EXTRA_TEXT, shareText.toString());
                sentIntent.putExtra(Intent.EXTRA_TITLE, title);
                sentIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sentIntent, null);
                startActivity(shareIntent);
            }
            else{
                Toast.makeText(VacationDetailsActivity.this, "All fields must be filled to share.", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        if(item.getItemId() == R.id.startnotify){
            String title = editTitle.getText().toString();
            String start = editStart.getText().toString();
            String end = editEnd.getText().toString();
            if(!start.isEmpty() && !title.isEmpty() && !end.isEmpty()){
                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                Date startDate = null;
                try{
                    startDate = sdf.parse(start);
                    Long triggerStart = startDate.getTime();
                    Intent startIntent = new Intent(VacationDetailsActivity.this, MyReceiver.class);
                    startIntent.putExtra("message", "The start of your " + title + " vacation is today.");
                    PendingIntent startSender = PendingIntent.getBroadcast(VacationDetailsActivity.this, ++MainActivity.numAlert, startIntent, PendingIntent.FLAG_IMMUTABLE);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, triggerStart, startSender);
                } catch (ParseException e){
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(VacationDetailsActivity.this, "All fields must be filled to set an alert.", Toast.LENGTH_LONG).show();
            }
        }
        if(item.getItemId() == R.id.endnotify){
            String title = editTitle.getText().toString();
            String start = editStart.getText().toString();
            String end = editEnd.getText().toString();
            if(!start.isEmpty() && !title.isEmpty() && !end.isEmpty()){
                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                Date endDate = null;
                try{
                    endDate = sdf.parse(end);
                    Long triggerEnd = endDate.getTime();
                    Intent endIntent = new Intent(VacationDetailsActivity.this, MyReceiver.class);
                    endIntent.putExtra("message", "The end of your " + title + " vacation is today.");
                    PendingIntent endSender = PendingIntent.getBroadcast(VacationDetailsActivity.this, ++MainActivity.numAlert, endIntent, PendingIntent.FLAG_IMMUTABLE);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, triggerEnd, endSender);
                } catch (ParseException e){
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(VacationDetailsActivity.this, "All fields must be filled to set an alert.", Toast.LENGTH_LONG).show();
            }
        }
        return true;
    }
    @Override
    protected void onResume(){
        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.excursionRecyclerView);
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion excursion : repository.getAllExcursions()){
            if(excursion.getVacationID() == vacationID) filteredExcursions.add(excursion);
        }
        excursionAdapter.setExcursions(filteredExcursions);
    }
}