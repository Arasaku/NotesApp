package com.example.main.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.main.R;
import com.example.main.db.UserDao;
import com.example.main.model.User;
import com.example.main.util.AppExecutors;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

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
        MaterialButton btnLogin = findViewById(R.id.btnLogin);
        MaterialButton btnGoRegister = findViewById(R.id.btnGoRegister);

        btnLogin.setOnClickListener(v -> attemptLogin());
        btnGoRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void attemptLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        executors.diskIO().execute(() -> {
            User user = userDao.loginUser(username, password);
            executors.mainThread().execute(() -> {
                if (user != null) {
                    Intent intent = new Intent(this, NotesListActivity.class);
                    intent.putExtra("user_id", user.getId());
                    intent.putExtra("username", user.getUsername());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Неверный логин или пароль",
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}