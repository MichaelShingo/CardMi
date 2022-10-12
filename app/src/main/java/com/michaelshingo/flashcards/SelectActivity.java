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

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class SelectActivity extends AppCompatActivity {
    private SharedPreferences sp; //but this should be accessible from other classes in this app
    private FloatingActionButton btn_add;
    private FloatingActionButton tempNav;
    private ListView flashcardSetList;

    private void updateListView(){
        //TODO you need to pull flashcardset names from database, decode them, and put them in the list
        flashcardSetList = findViewById(R.id.flashcardSetList);
        ArrayList<String> list = new ArrayList<String>();
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplication(), android.
                R.layout.simple_list_item_1, list);
        flashcardSetList.setAdapter(arrayAdapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        //INSTANTIATION

        FloatingActionButton btn_add = findViewById(R.id.addSet);
        FloatingActionButton tempNav = findViewById(R.id.tempNav);


        updateListView();

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
                        //TODO add the serialized flashcardSet to the database and have it display in listview
                        //TODO when it is clicked start MainActivity and display that FlashcardSet

                        //NEEDS TO CHECK IF NAME ALREADY EXISTS OR IS EMPTY OR CONTAINS INVALID CHARACTERS
                        FlashcardSet flashcardSet = new FlashcardSet(nameText.getText().toString());
                        String encodedFlashcardSet = new String("Untitled");

                        try {
                            encodedFlashcardSet = FlashcardSetEncoder.toString(flashcardSet);
                            Toast.makeText(SelectActivity.this, "Created new flashcard set.", Toast.LENGTH_SHORT).show();
                            FlashcardSet decodedFlashcardSet = (FlashcardSet)FlashcardSetEncoder.fromString(encodedFlashcardSet);
                        } catch (IOException e) {
                            Toast.makeText(SelectActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            Toast.makeText(SelectActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        DatabaseHelper databaseHelper = new DatabaseHelper(SelectActivity.this);
                        boolean success = databaseHelper.addOne(encodedFlashcardSet);
                        updateListView();

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
