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

public class SignUpActivity extends AppCompatActivity {
    String username;
    String password;
    String confirmPassword;
    Repository repository;
    EditText editUsername;
    EditText editPassword;
    EditText editConfirmPassword;
    TextView editSignUpError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        editUsername = findViewById(R.id.signUpUsernameText);
        editPassword = findViewById(R.id.signUpPasswordText);
        editConfirmPassword = findViewById(R.id.confirmPasswordText);
        editSignUpError = findViewById(R.id.signUpError);
        repository = new Repository(getApplication());

        Button signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = editUsername.getText().toString();
                password = editPassword.getText().toString();
                confirmPassword = editConfirmPassword.getText().toString();

                try{
                    CryptoUtil cryptoUtil = new CryptoUtil(sharedPreferences);
                    if(ValidationUtil.validateUsername(username) && ValidationUtil.validatePassword(password)
                            && ValidationUtil.validatePassword(confirmPassword) && password.equals(confirmPassword)){
                        User user = new User(repository.getAllExcursions().size() + 1, username, cryptoUtil.encrypt(password));
                        repository.insert(user);
                        Intent intent = new Intent(SignUpActivity.this, VacationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                            editSignUpError.setText("Please enter a username, password, and confirm the password.");
                        } else if (!ValidationUtil.validateUsername(username)) {
                            editSignUpError.setText("The username must contain 1 capital and be at least 6 characters long.");
                        } else if (!password.equals(confirmPassword)) {
                            editSignUpError.setText("The passwords entered do not match.");
                        } else {
                            editSignUpError.setText("The password must be 8 characters long, contain at least 1 capital, 1 number," +
                                    " and have no spaces.");
                        }
                    }
                } catch (Exception e){
                    throw new RuntimeException(e);
                }

            }
        });
    }
}
