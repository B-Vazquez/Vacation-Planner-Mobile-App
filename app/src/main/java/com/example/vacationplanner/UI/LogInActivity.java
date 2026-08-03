package com.example.vacationplanner.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vacationplanner.R;
import com.example.vacationplanner.database.Repository;
import com.example.vacationplanner.entities.User;
import com.example.vacationplanner.utilities.CryptoUtil;
import com.example.vacationplanner.utilities.ValidationUtil;


public class LogInActivity extends AppCompatActivity {
    String username;
    String password;
    Repository repository;
    EditText editUsername;
    EditText editPassword;
    TextView editLogInError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.log_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        editUsername = findViewById(R.id.logInUsernameText);
        editPassword = findViewById(R.id.logInPasswordText);
        editLogInError = findViewById(R.id.logInError);
        repository = new Repository(getApplication());

        Button logInButton = findViewById(R.id.logInButton);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    CryptoUtil cryptoUtil = new CryptoUtil(sharedPreferences);
                    username = editUsername.getText().toString();
                    password = editPassword.getText().toString();

                    ValidationUtil.ValidationResult result = ValidationUtil.validateLogin(username, password);
                    if (result.isValid) {
                        User user = repository.getAllUsers().get(0);
                        if (user.getUsername().equals(username) && cryptoUtil.compare(user.getPassword(), password)) {
                            Intent intent = new Intent(LogInActivity.this, VacationActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } else {
                            editLogInError.setText("The entered username/password does not match stored credentials.");
                        }
                    } else {
                        editLogInError.setText(result.errorMessage);
                    }
                } catch (Exception e){
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
