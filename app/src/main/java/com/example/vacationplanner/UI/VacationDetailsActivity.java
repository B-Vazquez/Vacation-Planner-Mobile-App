package com.example.vacationplanner.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.vacationplanner.utilities.VacationDeleteUtil;
import com.example.vacationplanner.utilities.VacationSaveUtil;
import com.example.vacationplanner.utilities.VacationShareUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            transport = getIntent().getSerializableExtra("transport", Transportation.class);
        } else {
            transport = (Transportation) getIntent().getSerializableExtra("transport");
        }
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
        floatButton.setOnClickListener(view -> {
            Intent intent = new Intent(VacationDetailsActivity.this, ExcursionActivity.class);
            intent.putExtra("vacationID", vacationID);
            startActivity(intent);
        });

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(view -> {
            Vacation vacation;
            String title = editTitle.getText().toString();
            String hotel = editHotel.getText().toString();
            String start = editStart.getText().toString();
            String end = editEnd.getText().toString();
            Transportation transport = (Transportation) editTransport.getSelectedItem();
            try {
                if (VacationSaveUtil.validateVacationToSave(VacationDetailsActivity.this, title, hotel, start, end)) {
                    vacation = new Vacation(vacationID, title, hotel, start, end, transport);
                    VacationSaveUtil.saveVacationToRepository(vacation, repository);
                    finish();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });


        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(view -> {
            Vacation vacation;
            String title = editTitle.getText().toString();
            String hotel = editHotel.getText().toString();
            String start = editStart.getText().toString();
            String end = editEnd.getText().toString();
            Transportation transport = (Transportation) editTransport.getSelectedItem();
            vacation = new Vacation(vacationID, title, hotel, start, end, transport);
            if(VacationDeleteUtil.deleteVacation(VacationDetailsActivity.this, vacation, repository)){
               finish();
            }

        });

        Button startDateButton = findViewById(R.id.startdatebutton);
        startDateButton.setOnClickListener(view -> {
            String dateToLoad;
            String testDate = editStart.getText().toString();
            if (testDate.isEmpty()){
                dateToLoad = sdf.format(new Date());
            } else dateToLoad = testDate;
            try{
                startCalender.setTime(Objects.requireNonNull(sdf.parse(dateToLoad)));
            } catch (ParseException e){
                e.printStackTrace();
            }
            new DatePickerDialog(VacationDetailsActivity.this, startDate, startCalender.get(Calendar.YEAR),
                    startCalender.get(Calendar.MONTH), startCalender.get(Calendar.DAY_OF_MONTH)).show();
        });

        startDate = (datePicker, year, month, day) -> {
            startCalender.set(Calendar.YEAR, year);
            startCalender.set(Calendar.MONTH, month);
            startCalender.set(Calendar.DAY_OF_MONTH, day);
            editStart.setText(sdf.format(startCalender.getTime()));
        };

        Button endDateButton = findViewById(R.id.enddatebutton);
        endDateButton.setOnClickListener(view -> {
            String dateToLoad;
            String testDate = editEnd.getText().toString();
            if (testDate.isEmpty()){
                dateToLoad = sdf.format(new Date());
            } else dateToLoad = testDate;
            try{
                endCalender.setTime(Objects.requireNonNull(sdf.parse(dateToLoad)));
            } catch (ParseException e){
                e.printStackTrace();
            }
            new DatePickerDialog(VacationDetailsActivity.this, endDate, endCalender.get(Calendar.YEAR),
                    endCalender.get(Calendar.MONTH), endCalender.get(Calendar.DAY_OF_MONTH)).show();
        });

        endDate = (datePicker, year, month, day) -> {
            endCalender.set(Calendar.YEAR, year);
            endCalender.set(Calendar.MONTH, month);
            endCalender.set(Calendar.DAY_OF_MONTH, day);
            editEnd.setText(sdf.format(endCalender.getTime()));
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
        String title = editTitle.getText().toString();
        String hotel = editHotel.getText().toString();
        String start = editStart.getText().toString();
        String end = editEnd.getText().toString();
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        if(item.getItemId() == R.id.share){
            StringBuilder shareText = VacationShareUtil.shareVacationDetails(VacationDetailsActivity.this,
                    title, hotel, start, end, repository.getAllExcursions());
            if (shareText == null){
                return true;
            }
            Intent sentIntent = new Intent();
            sentIntent.setAction(Intent.ACTION_SEND);
            sentIntent.putExtra(Intent.EXTRA_TEXT, shareText.toString());
            sentIntent.putExtra(Intent.EXTRA_TITLE, title);
            sentIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sentIntent, null);
            startActivity(shareIntent);

            return true;
        }
        if(item.getItemId() == R.id.startnotify){
            if(!start.isEmpty() && !title.isEmpty() && !end.isEmpty()){
                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                Date startDate;
                try{
                    startDate = sdf.parse(start);
                    long triggerStart = Objects.requireNonNull(startDate).getTime();
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
            if(!start.isEmpty() && !title.isEmpty() && !end.isEmpty()){
                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                Date endDate;
                try{
                    endDate = sdf.parse(end);
                    long triggerEnd = Objects.requireNonNull(endDate).getTime();
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