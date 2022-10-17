package com.michaelshingo.flashcards;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
//import com.michaelshingo.flashcards.OnSwipeTouchListener;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private CardView flashcard;
    private TextView flashcardText;
    private RelativeLayout outerLayout;
    private FloatingActionButton btn_add, btn_edit, btn_delete, btn_back;
    private FlashcardSet flashcardSet = new FlashcardSet("");
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //INSTANTIATION

        btn_add = findViewById(R.id.btn_add);
        btn_edit = findViewById(R.id.btn_edit);
        btn_delete = findViewById(R.id.btn_delete);
        btn_back = findViewById(R.id.btn_back);
        flashcardText = findViewById(R.id.flashcardText);
        flashcard = findViewById(R.id.flashcard);
        i = 0;
        RelativeLayout outerLayout = findViewById(R.id.outerLayout);

        //RETRIEVES FLASHCARD SET FORM SELECTACIVITY
        Bundle bundle = getIntent().getExtras();
        String encodedFlashcardSet = bundle.getString("set");
        int listID = bundle.getInt("id");
        try {
            flashcardSet = (FlashcardSet) FlashcardSetEncoder.fromString(encodedFlashcardSet);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //DISPLAY THE FIRST FLASHCARD IF NOT EMPTY
        if (flashcardSet.length() != 0) {
            flashcardText.setText(flashcardSet.get(0).getTerm()); //shows first term
        }


        //ON CLICK LISTENERS
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO when adding flashcard, add it to the database
                //EDIT BUTTON POP UP
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

                alert.setTitle("New Flashcard");
                LinearLayout addLayout = new LinearLayout(MainActivity.this);
                addLayout.setOrientation(LinearLayout.VERTICAL);
                final EditText term = new EditText(MainActivity.this);
                term.setHint("Term");
                final EditText definition = new EditText(MainActivity.this);
                definition.setHint("Definition");
                addLayout.addView(term);
                addLayout.addView(definition);
                alert.setView(addLayout);

                //CREATES NEW FLASHCARD
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {
                        Flashcard flashcard = new Flashcard(term.getText().toString(), definition.getText().toString(), -1);
                        flashcardSet.add(flashcard);
                        i = flashcardSet.length() - 1;
                        System.out.println("new flashcard, i is now: " + i);
                        flashcardText.setText(flashcardSet.get(i).getTerm()); //shows last added term
                        Toast.makeText(MainActivity.this, "new flashhcard i = " + i, Toast.LENGTH_SHORT).show();
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {

                    }
                });
                alert.show();

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedEncodedSet = null;
                try {
                    updatedEncodedSet = FlashcardSetEncoder.toString(flashcardSet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                System.out.println("updating id in database" + listID);
                databaseHelper.update(listID, updatedEncodedSet); //fix this!!!!!!!!!!!!! //TODO
                startActivity(new Intent(MainActivity.this, SelectActivity.class));
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flashcardSet.length() == 0){
                    Toast.makeText(MainActivity.this, "Create a flashcard first.", Toast.LENGTH_SHORT).show();
                }
                else{
                    flashcardSet.remove(i);

                    if (flashcardSet.length() == 0){
                        i = 0;
                        flashcardText.setText("Click the + icon to add a flashcard.");
                        //TODO try not to hardcode this string....
                    }
                    else if (i > flashcardSet.length() - 1){
                        i--;
                        flashcardText.setText(flashcardSet.get(i).getTerm());
                    }
                    else {
                        flashcardText.setText(flashcardSet.get(i).getTerm());
                    }
                }
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO what to do when there are no flashcards yet

                if (flashcardSet.length() == 0){
                    Toast.makeText(MainActivity.this, "Create a flashcard first.", Toast.LENGTH_SHORT).show();

                }
                else{
                    Flashcard currentFlashcard = flashcardSet.get(i);

                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

                    alert.setTitle("Edit Flashcard");
                    LinearLayout editLayout = new LinearLayout(MainActivity.this);
                    editLayout.setOrientation(LinearLayout.VERTICAL);
                    final EditText term = new EditText(MainActivity.this);
                    term.setText(currentFlashcard.getTerm());
                    final EditText definition = new EditText(MainActivity.this);
                    definition.setText(currentFlashcard.getDefinition());
                    editLayout.addView(term);
                    editLayout.addView(definition);
                    alert.setView(editLayout);

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int j) {
                            currentFlashcard.setTerm(term.getText().toString());
                            currentFlashcard.setDefinition(definition.getText().toString());
                            flashcardSet.update(i, currentFlashcard);
                            flashcardText.setText(currentFlashcard.getTerm().toString());
                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int j) {

                        }
                    });
                    alert.show();
                }
            }
        });

        //SWIPING FUNCTIONALITY

        flashcard.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop(){
                if (i < 0){
                    i = flashcardSet.length() - 1;
                }
                System.out.println("%%%%%%%%%%%% i = %%%%%%%%% " + i);
                flashcardText.setText(flashcardSet.get(i).getDefinition());
            }
            public void onSwipeRight(){
                i--;
                if (i < 0){
                    i = flashcardSet.length() - 1;
                }
                flashcardText.setText(flashcardSet.get(i).getTerm());
            }
            public void onSwipeLeft() {
                i++;
                if (i > flashcardSet.length() - 1){
                    i = 0;
                }
                flashcardText.setText(flashcardSet.get(i).getTerm());
            }
            public void onSwipeBottom() {
                flashcardText.setText(flashcardSet.get(i).getTerm());
            }
            public boolean performClick() {
                Toast.makeText(MainActivity.this, "Swipe on the flashcard.", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}