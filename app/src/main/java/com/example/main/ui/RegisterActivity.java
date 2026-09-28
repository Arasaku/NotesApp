package com.example.main.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.main.R;
import com.example.main.db.UserDao;
import com.example.main.util.AppExecutors;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etUsername, etPassword;
    private UserDao userDao;
    private AppExecutors executors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userDao = new UserDao(this);
        executors = AppExecutors.getInstance();

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        MaterialButton btnRegister = findViewById(R.id.btnGoRegister);

        btnRegister.setOnClickListener(v -> attemptRegister());
    }

    private void attemptRegister() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        executors.diskIO().execute(() -> {
            long id = userDao.registerUser(username, password);
            executors.mainThread().execute(() -> {
                if (id > 0) {
                    Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Ошибка: имя занято", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
