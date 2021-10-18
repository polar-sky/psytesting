 package ru.vlsu.registration;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

 public class MainActivity extends AppCompatActivity {

    EditText username, namee, password, repeatpswd;
    Button register, login;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText) findViewById(R.id.username);
        namee = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        repeatpswd = (EditText) findViewById(R.id.repeatpswd);
        register = (Button) findViewById(R.id.register);
        login = (Button) findViewById(R.id.login);
        db = new Database(this);

        register.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String user = username.getText().toString();
                String name  = namee.getText().toString();
                String pswd = password.getText().toString();
                String repeatPswd = repeatpswd.getText().toString();

                if (user.equals("") || pswd.equals("") || repeatPswd.equals(""))
                    Toast.makeText(MainActivity.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                else {
                    if (pswd.equals(repeatPswd)) {
                        Boolean checkEmail = db.checkUser(user);
                        if (!checkEmail) {
                            Boolean insert = db.insertData(user, name, pswd);
                            if (insert) {
                                Toast.makeText(MainActivity.this, "Регистрация прошла успешно", Toast.LENGTH_SHORT).show();
                                //переходим на главную страницу приложения при успешной регистрации
                                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this, "Ошибка регистрации", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Пользователь уже зарегистрирован", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
    }
}