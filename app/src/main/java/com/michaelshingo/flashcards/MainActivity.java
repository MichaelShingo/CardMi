package com.michaelshingo.flashcards;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
//import com.michaelshingo.flashcards.OnSwipeTouchListener;


import android.app.Activity;
import android.app.AlertDialog;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private CardView flashcard;
    private EditText flashcardText;
    private int i;
    private RelativeLayout outerLayout;
    private FloatingActionButton btn_add, btn_edit, btn_delete, btn_back;

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

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
        RelativeLayout outerLayout = findViewById(R.id.outerLayout);

        i = 0;










        //ON CLICK LISTENERS
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //it should show a popup where the user can enter term and definition in separate fields
                //then field.getText.toString() and add it to the database....

                //EDIT BUTTON POP UP
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

                alert.setTitle("New Flashcard");
                alert.setMessage("Enter term and definition.");
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
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Flashcard flashcard = new Flashcard(term.getText().toString(), definition.getText().toString(), -1);


                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Toast.makeText(MainActivity.this, "cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.show();

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SelectActivity.class));
                //Toast.makeText(MainActivity.this, "Back Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Delete Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashcardText.setShowSoftInputOnFocus(true);
                flashcardText.requestFocus(2);
                //Toast.makeText(MainActivity.this, "Edit icon clicked.", Toast.LENGTH_SHORT).show();

            }
        });






                //USER DATA

                ArrayList < ArrayList < String >> musicTheoryData = new ArrayList<>();
        ArrayList<String> termDef = new ArrayList<>(
                Arrays.asList("cadential 6/4", "2nd inversion I chord used before V"));

        musicTheoryData.add(termDef);

        termDef = new ArrayList<>(
                Arrays.asList("modulation", "a change in key"));
        musicTheoryData.add(termDef);

        termDef = new ArrayList<>(
                Arrays.asList("hexachordal combinatoriality", "When two hexachords used in a piece create a complete set of twelve pitches."));

        musicTheoryData.add(termDef);


        flashcardText.setText(musicTheoryData.get(i).get(i)); //shows first term


        //SWIPING FUNCTIONALITY

        flashcard.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {

            public void onSwipeTop(){
                flashcardText.setText(musicTheoryData.get(i).get(1));
                Toast.makeText(MainActivity.this, "Swipe Top", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeRight(){

                i--;
                if (i < 0){
                    i = musicTheoryData.size() - 1;
                }
                flashcardText.setText(musicTheoryData.get(i).get(0));
                Toast.makeText(MainActivity.this, "Swipe Right", Toast.LENGTH_SHORT).show();

            }

            public void onSwipeLeft() {
                i++;
                if (i > musicTheoryData.size() - 1){
                    i = 0;
                }
                flashcardText.setText(musicTheoryData.get(i).get(0));
                Toast.makeText(MainActivity.this, "Swipe Left", Toast.LENGTH_SHORT).show();

            }

            public void onSwipeBottom() {
                flashcardText.setText(musicTheoryData.get(i).get(0));
                Toast.makeText(MainActivity.this, "Swipe Bottom", Toast.LENGTH_SHORT).show();

            }

            public boolean performClick() {
                Toast.makeText(MainActivity.this, "Swipe on the flashcard.", Toast.LENGTH_SHORT).show();
                return true;
            }


        });


        flashcardText.setShowSoftInputOnFocus((false));
        outerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flashcardText.getShowSoftInputOnFocus()){
                    flashcardText.setShowSoftInputOnFocus(false);
                    hideKeyboard(MainActivity.this);
                }
            }
        });

    }
}