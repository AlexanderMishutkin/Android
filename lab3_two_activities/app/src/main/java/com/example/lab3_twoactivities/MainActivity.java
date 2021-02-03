package com.example.lab3_twoactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public static String text2remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editText = findViewById(R.id.editText);
        Button button = findViewById(R.id.button);

        editText.setText(text2remember);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text2remember = editText.getText().toString();
                Intent intent = new Intent(MainActivity.this,
                        Main2Activity.class);
                startActivity(intent);
            }
        });
    }
}
