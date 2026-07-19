package com.example.vacationplanner.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacationplanner.R;
import com.example.vacationplanner.database.Repository;
import com.example.vacationplanner.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class VacationActivity extends AppCompatActivity {
    private Repository repository;
    private VacationAdapter vacationAdapter;
    private List<Vacation> allVacations;
    private RecyclerView recyclerView;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FloatingActionButton floatButton = findViewById(R.id.floatingActionButton);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationActivity.this, VacationDetailsActivity.class);
                startActivity(intent);
            }
        });

        Button reportButton = findViewById(R.id.reportButton);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationActivity.this, ReportActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recyclerview);
        repository = new Repository(getApplication());
        allVacations = repository.getAllVacations();
        vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);
        toast = Toast.makeText(VacationActivity.this, "No matches found.", Toast.LENGTH_SHORT);


        Spinner searchChoice = findViewById(R.id.searchChoice);
        ArrayAdapter<CharSequence> searchChoiceAdapter = ArrayAdapter.createFromResource(this,
                R.array.search_choices,
                android.R.layout.simple_spinner_dropdown_item
        );
        searchChoiceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchChoice.setAdapter(searchChoiceAdapter);

        SearchView searchBar = findViewById(R.id.searchView);
        searchBar.setQueryHint("Search for Title, Hotel, or Transportation");
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Vacation> vacations = new ArrayList<>();
                newText = newText.toLowerCase();

                // Gets the position of the selected search option
                int pos = searchChoice.getSelectedItemPosition();

                // Searches vacations based on the selected option
                switch (pos){
                    case (0):
                        for(Vacation i : allVacations){
                            if(i.getTitle().toLowerCase().contains(newText)){
                                vacations.add(i);
                            }
                        }
                        break;
                    case (1):
                        for(Vacation i : allVacations){
                            if(i.getHotel().toLowerCase().contains(newText)){
                                vacations.add(i);
                            }
                        }
                        break;
                    case (2):
                        for(Vacation i : allVacations){
                            if(i.getTransport().toString().toLowerCase().contains(newText)){
                                vacations.add(i);
                            }
                        }
                        break;
                }

                if(vacations.isEmpty()){
                    vacations = allVacations;
                    toast.show();
                }
                vacationAdapter.setVacations(vacations);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home){
            this.finish();
            return true;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        allVacations = repository.getAllVacations();
        recyclerView = findViewById(R.id.recyclerview);
        vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);
    }
}