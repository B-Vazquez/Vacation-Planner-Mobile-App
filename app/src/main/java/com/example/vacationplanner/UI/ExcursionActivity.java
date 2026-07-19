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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vacationplanner.R;
import com.example.vacationplanner.database.Repository;
import com.example.vacationplanner.entities.Excursion;
import com.example.vacationplanner.entities.Vacation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExcursionActivity extends AppCompatActivity {
    String title;
    String date;
    int excursionID;
    int vacationID;
    EditText editTitle;
    EditText editDate;
    Repository repository;
    DatePickerDialog.OnDateSetListener excursionDate;
    final Calendar excursionCalender = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_excursion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editTitle = findViewById(R.id.excursiontitle);
        editDate = findViewById(R.id.excursiondate);
        title = getIntent().getStringExtra("name");
        excursionID = getIntent().getIntExtra("id", -1);
        vacationID = getIntent().getIntExtra("vacationID", -1);
        date = getIntent().getStringExtra("date");
        editTitle.setText(title);
        editDate.setText(date);
        editDate.setEnabled(false);
        repository = new Repository(getApplication());

        Button saveButton = findViewById(R.id.excursionSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vacationID != -1){
                    try{
                        String date = editDate.getText().toString();
                        String title = editTitle.getText().toString();
                        if(title.isEmpty() || date.isEmpty()){
                            Toast.makeText(ExcursionActivity.this, "All fields must be filled to save excursion.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        DateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
                        Date excurDate = dateFormat.parse(date);
                        List<Vacation> vacations = repository.getAllVacations();
                        String start = "", end = "";
                        for (Vacation v : vacations){
                            if (v.getVacationID() == vacationID) { start = v.getStart_date(); end = v.getEnd_date(); }
                        }
                        Date vacationStart = dateFormat.parse(start);
                        Date vacationEnd = dateFormat.parse(end);
                        if(excurDate.before(vacationStart) || excurDate.after(vacationEnd)){
                            Toast.makeText(ExcursionActivity.this, "Excursion date must be between the start and end date of it's vacation.", Toast.LENGTH_LONG).show();
                        }
                        else{
                            if(excursionID == -1){
                                if(repository.getAllExcursions().size() == 0) excursionID = 1;
                                else excursionID = repository.getAllExcursions().get(repository.getAllExcursions().size() - 1).getExcursionID() + 1;
                                Excursion excursion = new Excursion(excursionID, title, vacationID, date);
                                repository.insert(excursion);
                            }
                            else{
                                Excursion excursion = new Excursion(excursionID, title, vacationID, date);
                                repository.update(excursion);
                            }
                            finish();
                        }
                    } catch (ParseException e){
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(ExcursionActivity.this, "Vacation must be saved before adding excursions.", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button deleteButton = findViewById(R.id.excursionDeleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(excursionID != -1){
                    String title = editTitle.getText().toString();
                    String date = editDate.getText().toString();
                    Excursion excursion = new Excursion(excursionID, title, vacationID, date);
                    repository.delete(excursion);
                }
                finish();
            }
        });

        Button dateButton = findViewById(R.id.excursiondatebutton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info;
                String test = editDate.getText().toString();
                if(test.isEmpty()){
                    info = sdf.format(new Date());
                } else info = test;
                try{
                    excursionCalender.setTime(sdf.parse(info));
                } catch (ParseException e){
                    e.printStackTrace();
                }
                new DatePickerDialog(ExcursionActivity.this, excursionDate, excursionCalender.get(Calendar.YEAR),
                        excursionCalender.get(Calendar.MONTH), excursionCalender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        excursionDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                excursionCalender.set(Calendar.YEAR, year);
                excursionCalender.set(Calendar.MONTH, month);
                excursionCalender.set(Calendar.DAY_OF_MONTH, day);
                editDate.setText(sdf.format(excursionCalender.getTime()));
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.excursion_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        if(item.getItemId() == R.id.excursionNotify){
            String title = editTitle.getText().toString();
            String date = editDate.getText().toString();
            if(!title.isEmpty() || !date.isEmpty()){
                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                Date myDate = null;
                try {
                    myDate = sdf.parse(date);
                    DateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
                    List<Vacation> vacations = repository.getAllVacations();
                    String start = "", end = "";
                    for (Vacation v : vacations){
                        if (v.getVacationID() == vacationID) { start = v.getStart_date(); end = v.getEnd_date(); }
                    }
                    Date vacationStart = dateFormat.parse(start);
                    Date vacationEnd = dateFormat.parse(end);
                    if(myDate.before(vacationStart) || myDate.after(vacationEnd)){
                        Toast.makeText(ExcursionActivity.this, "Excursion date must be between the start and end date of it's vacation.", Toast.LENGTH_LONG).show();
                        return true;
                    }
                    Long trigger = myDate.getTime();
                    Intent intent = new Intent(ExcursionActivity.this, MyReceiver.class);
                    intent.putExtra("message", "The start of your " + title + " excursion is today.");
                    PendingIntent sender = PendingIntent.getBroadcast(ExcursionActivity.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                } catch (ParseException e){
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(ExcursionActivity.this, "All fields must be filled to set an alert.", Toast.LENGTH_LONG).show();
            }
        }
        return true;
    }
}