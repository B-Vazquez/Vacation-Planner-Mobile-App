package com.example.vacationplanner.UI;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacationplanner.R;
import com.example.vacationplanner.database.Repository;
import com.example.vacationplanner.viewmodel.VacationViewModel;

public class ReportActivity extends AppCompatActivity {
    private ReportAdapter reportAdapter;
    private RecyclerView reportRecyclerView;
    private VacationViewModel vacationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.report);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        reportRecyclerView = findViewById(R.id.reportRecyclerView);
        vacationViewModel = new ViewModelProvider(this).get(VacationViewModel.class);
        reportAdapter = new ReportAdapter(this);
        reportRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reportRecyclerView.setAdapter(reportAdapter);

        vacationViewModel.getAllVacations().observe(this, vacations -> {
            reportAdapter.setReportVacations(vacations);
        });
    }


}