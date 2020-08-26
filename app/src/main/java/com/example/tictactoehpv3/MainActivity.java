package com.example.tictactoehpv3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button twoPlayerButton = (Button) findViewById(R.id.twoPlayerButton);
        twoPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent spIntent = new Intent(getApplicationContext(), twoPlayerActivity.class);
                startActivity(spIntent);
            }
        });



        Button hiddenGame = (Button) findViewById(R.id.hiddenGame);
        hiddenGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent hgIntent = new Intent(getApplicationContext(), hiddenGameActivity.class);
                startActivity(hgIntent);
            }
        });


        Button compGame = (Button) findViewById(R.id.compPlayerButton);
        compGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent hgIntent = new Intent(getApplicationContext(), compActivity.class);
                startActivity(hgIntent);
            }
        });
    }
}
