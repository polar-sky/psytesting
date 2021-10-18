package ru.vlsu.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText username, password;
    Button login;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

         //инициализируем элементы
        username = (EditText) findViewById(R.id.usernameLogin);
        password = (EditText) findViewById(R.id.passwordLogin);
        login = (Button) findViewById(R.id.loginButton);

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                String user = username.getText().toString();
                String pswd = password.getText().toString();

                if(user.equals("")|| pswd.equals("")){
                    Toast.makeText(Login.this, "Заполниет все полян", Toast.LENGTH_SHORT).show();
                }else {
                    Boolean checkEmail = db.checkUserPassword(user, pswd);
                    if(checkEmail==true){
                        Toast.makeText(Login.this, "Добро пожаловать", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), HomePage.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(Login.this, "Неверные данные", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


}