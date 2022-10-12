package com.michaelshingo.flashcards;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class SelectActivity extends AppCompatActivity {
    private SharedPreferences sp; //but this should be accessible from other classes in this app
    private FloatingActionButton btn_add;
    private FloatingActionButton tempNav;
    private ListView flashcardSetList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        //INSTANTIATION

        FloatingActionButton btn_add = findViewById(R.id.addSet);
        FloatingActionButton tempNav = findViewById(R.id.tempNav);
        flashcardSetList = findViewById(R.id.flashcardSetList);
        //create a list of the names of all the sharedpreferences

        ArrayList<String> list = new ArrayList<String>();
        list.add("Item 1");
        list.add("Item 2");
        list.add("Item 3");

        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplication(), android.
                R.layout.simple_list_item_1, list);

        flashcardSetList.setAdapter(arrayAdapter);









        //ON CLICK LISTENERS
        
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(SelectActivity.this);
                final EditText nameText = new EditText(SelectActivity.this);
                nameText.setHint("Name your flashcard set");
                alert.setView(nameText);
                alert.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sp = getSharedPreferences(nameText.getText().toString(), Context.MODE_PRIVATE); //creates the shared preferences (set of flashcards)
                        Toast.makeText(SelectActivity.this, "Created new flashcard set.", Toast.LENGTH_SHORT);
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Do nothing
                            }
                        });
                alert.show();




                String name = "";
                FlashcardSet flashcardSet = new FlashcardSet(name);


                /*
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
