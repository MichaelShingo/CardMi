package com.michaelshingo.flashcards;

import androidx.annotation.NonNull;
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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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
    private ImageButton btn_studied;
    private FlashcardSet flashcardSet = new FlashcardSet("");
    private int i;
    private int DURATION1000 = 1000;
    private int DURATIONFLIP = 250;
    private int DURATION100 = 100;
    private int DURATION500 = 500;
    private ImageButton btn_overflow;
    private PopupWindow studiedPopupWindow;
    private ListView studiedListView;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //is this even being triggered when you click the listeview item???
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        System.out.println(v.getId());
        System.out.println("Some kind of contextmenuregistered item was selected..................");


        if (v.getId() == R.id.btn_overflow) {
            menuInflater.inflate(R.menu.overflow_menu_main, menu); //provide menu from the method parameters
        }
        else if (v.getId() == studiedListView.getItemIdAtPosition(0)) { //it's just a matter of finding the ID of the listview item.....
            System.out.println("triggered studiedListView contextMenu if statement");
            System.out.println(studiedListView.getItemIdAtPosition(0));
            menuInflater.inflate(R.menu.studied_menu, menu);
        }
    }


    public void setTextAfterDelete(int index) {
        if (flashcardSet.length() == 0) {
            Toast.makeText(MainActivity.this, "Create a flashcard first.", Toast.LENGTH_SHORT).show();
        } else {
            flashcardSet.remove(i);

            if (flashcardSet.length() == 0) {
                i = 0;
                flashcardText.setText("Click the + icon to add a flashcard.");
            } else if (i > flashcardSet.length() - 1) {
                i--;
                flashcardText.setText(flashcardSet.get(i).getTerm());
            } else {
                flashcardText.setText(flashcardSet.get(i).getTerm());
            }
        }
    }



    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item){
        //TODO make sure this protects against null values and empty lists
        //TODO is the database updating when you add things to studied?
        switch (item.getItemId()){
            case R.id.btn_list:
                Toast.makeText(MainActivity.this, "Show as list", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.btn_studied:
                //bundle the flashcardset item
                Bundle bundle = new Bundle();
                Intent intent = new Intent(MainActivity.this, StudiedActivity.class);
                String currentEncodedSet = null;
                try {
                    currentEncodedSet = FlashcardSetEncoder.toString(flashcardSet);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                bundle.putString("set", currentEncodedSet);
                intent.putExtras(bundle);
                startActivity(intent);

                //TODO remember you DON"T use onClick listeners for context menu items
                return true;

            case R.id.btn_recycle_bin:
                Toast.makeText(MainActivity.this, "recycle", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.btn_sort_asc:
                YoYo.with(Techniques.Pulse).duration(DURATION500).playOn(flashcard);
                YoYo.with(Techniques.Pulse).duration(DURATION500).playOn(flashcardText);
                flashcardSet.sortAsc();
                i = 0;
                flashcardText.setText(flashcardSet.get(i).getTerm());
                //sort the list according to term
                //reset i to 0
                //set flashcardText
                return true;
            case R.id.btn_sort_desc:
                YoYo.with(Techniques.Pulse).duration(DURATION500).playOn(flashcard);
                YoYo.with(Techniques.Pulse).duration(DURATION500).playOn(flashcardText);
                flashcardSet.sortDesc();
                i = 0;
                flashcardText.setText(flashcardSet.get(i).getTerm());
                return true;
            case R.id.btn_shuffle:
                YoYo.with(Techniques.Pulse).duration(DURATION500).playOn(flashcard);
                YoYo.with(Techniques.Pulse).duration(DURATION500).playOn(flashcardText);
                flashcardSet.shuffle();
                i = 0;
                flashcardText.setText(flashcardSet.get(i).getTerm());
                Toast.makeText(MainActivity.this, "shuffle", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return true;
        }
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
        btn_studied = findViewById(R.id.studied);
        flashcard = findViewById(R.id.flashcard);
        btn_overflow = findViewById(R.id.btn_overflow);

        i = 0;
        RelativeLayout outerLayout = findViewById(R.id.outerLayout);

        YoYo.with(Techniques.FadeInLeft).duration(DURATION1000).playOn(btn_back);
        YoYo.with(Techniques.FadeInLeft).duration(DURATION1000).playOn(btn_add);
        YoYo.with(Techniques.FadeInLeft).duration(DURATION1000).playOn(btn_edit);
        YoYo.with(Techniques.FadeInLeft).duration(DURATION1000).playOn(btn_delete);
        YoYo.with(Techniques.RotateIn).duration(250).playOn(flashcard);
        YoYo.with(Techniques.RotateIn).duration(250).playOn(flashcardText);


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
        try {
            if (flashcardSet.length() != 0) {
                flashcardText.setText(flashcardSet.get(0).getTerm()); //shows first term
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }



        //ON CLICK LISTENERS

        registerForContextMenu(btn_overflow);
        btn_overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.openContextMenu(btn_overflow);

            }
        });

        btn_studied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flashcardSet.length() == 0){
                    Toast.makeText(MainActivity.this, R.string.create_first, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, flashcardSet.get(i).getTerm() + " marked as studied.", Toast.LENGTH_SHORT).show();
                    flashcardSet.markAsStudied(i);
                    setTextAfterDelete(i);
                }

            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        flashcardText.setText(flashcardSet.get(i).getTerm()); //shows last added term
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
                databaseHelper.update(listID, updatedEncodedSet);
                startActivity(new Intent(MainActivity.this, SelectActivity.class));
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextAfterDelete(i);
                }
            });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                YoYo.with(Techniques.SlideOutUp).duration(DURATIONFLIP).playOn(flashcard);
                YoYo.with(Techniques.SlideOutUp).duration(DURATIONFLIP).playOn(flashcardText);
                YoYo.with(Techniques.SlideInUp).duration(DURATIONFLIP).delay(DURATIONFLIP).playOn(flashcard);
                YoYo.with(Techniques.SlideInUp).duration(DURATIONFLIP).delay(DURATIONFLIP).playOn(flashcardText);
                flashcardText.setText(flashcardSet.get(i).getDefinition());

            }
            public void onSwipeRight(){

                i--;
                if (i < 0){
                    i = flashcardSet.length() - 1;
                }

                YoYo.with(Techniques.SlideOutLeft).duration(DURATIONFLIP).playOn(flashcard);
                YoYo.with(Techniques.SlideOutLeft).duration(DURATIONFLIP).playOn(flashcardText);
                YoYo.with(Techniques.SlideInLeft).duration(DURATIONFLIP).playOn(flashcard);
                YoYo.with(Techniques.SlideInLeft).duration(DURATIONFLIP).playOn(flashcardText);
                flashcardText.setText(flashcardSet.get(i).getTerm());
            }
            public void onSwipeLeft() {
                i++;
                if (i > flashcardSet.length() - 1){
                    i = 0;
                }

                YoYo.with(Techniques.SlideOutRight).duration(DURATIONFLIP).playOn(flashcard);
                YoYo.with(Techniques.SlideOutRight).duration(DURATIONFLIP).playOn(flashcardText);
                YoYo.with(Techniques.SlideInRight).duration(DURATIONFLIP).playOn(flashcard);
                YoYo.with(Techniques.SlideInRight).duration(DURATIONFLIP).playOn(flashcardText);
                flashcardText.setText(flashcardSet.get(i).getTerm());
            }
            public void onSwipeBottom() {
                YoYo.with(Techniques.SlideOutDown).duration(DURATIONFLIP).playOn(flashcard);
                YoYo.with(Techniques.SlideOutDown).duration(DURATIONFLIP).playOn(flashcardText);
                YoYo.with(Techniques.SlideInDown).duration(DURATIONFLIP).playOn(flashcard);
                YoYo.with(Techniques.SlideInDown).duration(DURATIONFLIP).playOn(flashcardText);
                flashcardText.setText(flashcardSet.get(i).getTerm());

            }
            public boolean performClick() {
                Toast.makeText(MainActivity.this, "Swipe on the flashcard.", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}