package com.michaelshingo.flashcards;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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


//Features to add: sorting, deleting, recycle bin for deleted flashcards
public class SelectActivity extends AppCompatActivity {

    private FloatingActionButton btn_add;
    private FloatingActionButton tempNav;
    private ListView listView;
    private ArrayList<FlashcardSet> flashcardSetArray;

    private void updateListView(){
        DatabaseHelper dataBaseHelper = new DatabaseHelper(SelectActivity.this);
        ArrayList<String> encodedFlashcardSets = dataBaseHelper.getAll();
        ArrayList<FlashcardSet> decodedFlashcardSets = new ArrayList<>();

        for (String encodedStr:encodedFlashcardSets){
            FlashcardSet decodedFlashcardSet = null;
            try {
                decodedFlashcardSet = (FlashcardSet) FlashcardSetEncoder.fromString(encodedStr);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            decodedFlashcardSets.add(decodedFlashcardSet);
        }

        ArrayList<String> allNames = new ArrayList<>();

        //TODO this is no longer retrieving anything from the database??

        for (FlashcardSet flashcardSet: decodedFlashcardSets){
            if (flashcardSet != null) {
                allNames.add(flashcardSet.getName());
            }
        }

        listView = findViewById(R.id.flashcardSetList);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplication(), android.
                R.layout.simple_list_item_1, allNames);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(i);
                //Toast.makeText(SelectActivity.this, decodedFlashcardSets.get(i).getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SelectActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                String currentEncodedSet = null;
                try {
                    currentEncodedSet = FlashcardSetEncoder.toString(decodedFlashcardSets.get(i));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bundle.putString("set", currentEncodedSet);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        //INSTANTIATION

        FloatingActionButton btn_add = findViewById(R.id.addSet);
        FloatingActionButton tempNav = findViewById(R.id.tempNav);


        //POPULATE THE LISTVIEW

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
                        } catch (IOException e) {
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
