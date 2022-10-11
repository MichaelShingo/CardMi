package com.michaelshingo.flashcards;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        //INSTANTIATION

        FloatingActionButton btn_add = findViewById(R.id.addSet);
        FloatingActionButton tempNav = findViewById(R.id.tempNav);


        //ON CLICK LISTENERS
        
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create alert and prompt user for name of set
                AlertDialog.Builder alert = new AlertDialog.Builder(SelectActivity.this);
                alert.setTitle("Name your flashcard set:");
                final EditText nameText = new EditText(SelectActivity.this);
                nameText.setHint("Name your flashcard set");
                alert.setView(nameText);
                alert.show();




                String name = "";
                FlashcardSet flashcardSet = new FlashcardSet(name);
                //creating database here
                // you need to associate each parameter with a value in the FlashcardSet and see if you can add an arraylist or not....
                DatabaseHelper databaseHelper = new DatabaseHelper(SelectActivity.this);
                boolean success = databaseHelper.addOne(flashcardSet);
                Toast.makeText(SelectActivity.this, "Success = " + success, Toast.LENGTH_SHORT).show();*/
            }
        });

        tempNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SelectActivity.this, MainActivity.class));
            }
        });

        }
    }
