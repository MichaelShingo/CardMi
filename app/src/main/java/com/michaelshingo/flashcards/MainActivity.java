package com.michaelshingo.flashcards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
//import com.michaelshingo.flashcards.OnSwipeTouchListener;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {
    private CardView flashcard;
    private TextView flashcardText;
    private RelativeLayout outerLayout;
    private FloatingActionButton btn_add, btn_edit, btn_delete, btn_back;
    private ImageButton btn_studied;
    private FlashcardSet flashcardSet;
    private int i;
    private int listID;
    private int DURATION1000 = 1000;
    private int DURATIONFLIP = 250;
    private int DURATION100 = 100;
    private int DURATION500 = 500;
    private ImageButton btn_overflow;
    private PopupWindow studiedPopupWindow;
    private ListView studiedListView;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();

        if (v.getId() == R.id.btn_overflow) {
            menuInflater.inflate(R.menu.overflow_menu_main, menu); //provide menu from the method parameters
        }
        else if (v.getId() == studiedListView.getItemIdAtPosition(0)) {
            menuInflater.inflate(R.menu.studied_menu, menu);
        }
    }


    public void setTextAfterDelete(int index) {
        if (flashcardSet.length() == 0) {
            Toast.makeText(MainActivity.this, R.string.create_first, Toast.LENGTH_SHORT).show();
        } else {
            flashcardSet.recycle(i);

            if (flashcardSet.length() == 0) {
                i = 0;
                flashcardText.setText(R.string.add_flashcard);
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
        switch (item.getItemId()){
            case R.id.btn_list:
                Bundle listBundle = new Bundle();
                Intent listIntent = new Intent(MainActivity.this, ListViewActivity.class);
                String listEncodedSet = null;
                try {
                    listEncodedSet = FlashcardSetEncoder.toString(flashcardSet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                listBundle.putString("set", listEncodedSet);
                listBundle.putInt("id", listID);
                listIntent.putExtras(listBundle);
                startActivity(listIntent);
                return true;
            case R.id.btn_studied:
                Bundle bundle = new Bundle();
                Intent intent = new Intent(MainActivity.this, StudiedActivity.class);
                String currentEncodedSet = null;
                try {
                    currentEncodedSet = FlashcardSetEncoder.toString(flashcardSet);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                bundle.putString("set", currentEncodedSet);
                bundle.putInt("id", listID);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;

            case R.id.btn_recycle_bin:
                Bundle recycleBundle = new Bundle();
                Intent recycleIntent = new Intent(MainActivity.this, RecycleActivity.class);
                String recycleEncodedSet = null;
                try {
                    recycleEncodedSet = FlashcardSetEncoder.toString(flashcardSet);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                recycleBundle.putString("set", recycleEncodedSet);
                recycleBundle.putInt("id", listID);
                recycleIntent.putExtras(recycleBundle);
                startActivity(recycleIntent);

                return true;

            case R.id.btn_sort_asc:
                if (flashcardSet.length() == 0){
                    Toast.makeText(MainActivity.this, R.string.add_flashcard, Toast.LENGTH_SHORT).show();
                }
                else{
                    YoYo.with(Techniques.Pulse).duration(DURATION500).playOn(flashcard);
                    YoYo.with(Techniques.Pulse).duration(DURATION500).playOn(flashcardText);
                    flashcardSet.sortAsc();
                    i = 0;
                    flashcardText.setText(flashcardSet.get(i).getTerm());
                }
                return true;
            case R.id.btn_sort_desc:
                if (flashcardSet.length() == 0){
                    Toast.makeText(MainActivity.this, R.string.add_flashcard, Toast.LENGTH_SHORT).show();
                }
                else{
                    YoYo.with(Techniques.Pulse).duration(DURATION500).playOn(flashcard);
                    YoYo.with(Techniques.Pulse).duration(DURATION500).playOn(flashcardText);
                    flashcardSet.sortDesc();
                    i = 0;
                    flashcardText.setText(flashcardSet.get(i).getTerm());
                }
                return true;
            case R.id.btn_shuffle:
                if (flashcardSet.length() == 0){
                    Toast.makeText(MainActivity.this, R.string.add_flashcard, Toast.LENGTH_SHORT).show();
                }
                else{
                    YoYo.with(Techniques.Pulse).duration(DURATION500).playOn(flashcard);
                    YoYo.with(Techniques.Pulse).duration(DURATION500).playOn(flashcardText);
                    flashcardSet.shuffle();
                    i = 0;
                    flashcardText.setText(flashcardSet.get(i).getTerm());
                }
                return true;
            default:
                return true;
        }
    }

    public void flipFlashcard() {
        if (flashcardSet.length() == 0){
        }
        else if (flashcardText.getText().toString().equals(flashcardSet.get(i).getDefinition())){
            YoYo.with(Techniques.SlideOutDown).duration(DURATIONFLIP).playOn(flashcard);
            YoYo.with(Techniques.SlideOutDown).duration(DURATIONFLIP).playOn(flashcardText);
            YoYo.with(Techniques.SlideInDown).duration(DURATIONFLIP).playOn(flashcard);
            YoYo.with(Techniques.SlideInDown).duration(DURATIONFLIP).playOn(flashcardText);
            flashcardText.setText(flashcardSet.get(i).getTerm());

        }
        else if (flashcardText.getText().toString().equals(flashcardSet.get(i).getTerm())){
            if (i < 0){
                i = flashcardSet.length() - 1;
            }
            YoYo.with(Techniques.SlideOutUp).duration(DURATIONFLIP).playOn(flashcard);
            YoYo.with(Techniques.SlideOutUp).duration(DURATIONFLIP).playOn(flashcardText);
            YoYo.with(Techniques.SlideInUp).duration(DURATIONFLIP).playOn(flashcard);
            YoYo.with(Techniques.SlideInUp).duration(DURATIONFLIP).playOn(flashcardText);
            flashcardText.setText(flashcardSet.get(i).getDefinition());
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
        listID = bundle.getInt("id");
        int selectedListPosition = bundle.getInt("listPosition");
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
                if (selectedListPosition >= 0) {
                    flashcardText.setText(flashcardSet.get(selectedListPosition).getTerm());//shows term selected from listviewActivity
                    i = selectedListPosition;
                }
                else{
                    flashcardText.setText(flashcardSet.get(0).getTerm()); //shows first term
                    i = 0;
                }
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
                    YoYo.with(Techniques.Pulse).duration(DURATION500).playOn(flashcard);
                    YoYo.with(Techniques.Pulse).duration(DURATION500).playOn(flashcardText);
                    Toast.makeText(MainActivity.this, flashcardSet.get(i).getTerm() + " " + getResources().getString(R.string.marked_as_studied), Toast.LENGTH_SHORT).show();
                    flashcardSet.markAsStudied(i);
                    flashcardSet.remove(i);

                    if (flashcardSet.length() == 0) {
                        i = 0;
                        flashcardText.setText(R.string.add_flashcard);
                    } else if (i > flashcardSet.length() - 1) {
                        i--;
                        flashcardText.setText(flashcardSet.get(i).getTerm());
                    } else {
                        flashcardText.setText(flashcardSet.get(i).getTerm());
                    }
                }

            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle(R.string.new_flashcard);
                LinearLayout addLayout = new LinearLayout(MainActivity.this);
                addLayout.setOrientation(LinearLayout.VERTICAL);
                final EditText term = new EditText(MainActivity.this);
                term.setHint(R.string.term);
                final EditText definition = new EditText(MainActivity.this);
                definition.setHint(R.string.definition);
                addLayout.addView(term);
                addLayout.addView(definition);
                alert.setView(addLayout);

                //CREATES NEW FLASHCARD
                alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {
                        Flashcard flashcard = new Flashcard(term.getText().toString(), definition.getText().toString(), -1);
                        flashcardSet.add(flashcard);
                        i = flashcardSet.length() - 1;
                        flashcardText.setText(flashcardSet.get(i).getTerm()); //shows last added term
                    }
                });
                alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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
                FlashcardSet testDecodedSet1 = null;
                try {
                    updatedEncodedSet = FlashcardSetEncoder.toString(flashcardSet);
                    testDecodedSet1 = (FlashcardSet)FlashcardSetEncoder.fromString(updatedEncodedSet);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                databaseHelper.update(listID, updatedEncodedSet);

                String testEncodedSet = databaseHelper.getAll().get(0);
                FlashcardSet testDecodedSet = null;
                try {
                    testDecodedSet = (FlashcardSet) FlashcardSetEncoder.fromString(testEncodedSet);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
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
                editFlashcard();
            }
        });

        //SWIPING FUNCTIONALITY

//        flashcardText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                flipFlashcard();
//            }
//        });
//
//        flashcard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                flipFlashcard();
//            }
//        });

//        flashcardText.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
//            public void onSwipeTop(){
//                flipFlashcard();
//            }
//            public void onSwipeBottom() {
//                flipFlashcard();
//            }
//            public void onSwipeRight(){
//                previousFlashcard();
//            }
//            public void onSwipeLeft() {
//                nextFlashcard();
//            }
//
//        });

        flashcard.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop(){
                flipFlashcard();
            }
            public void onSwipeRight(){
                previousFlashcard();
            }
            public void onSwipeLeft() {
                nextFlashcard();
            }
            public void onSwipeBottom() {
                flipFlashcard();
            }
            public void onClick(){
                flipFlashcard();
            }
            public void onLongClick(){
                editFlashcard();
            }
        });
    }

    private void editFlashcard() {
        if (flashcardSet.length() == 0){
            Toast.makeText(MainActivity.this, R.string.create_first, Toast.LENGTH_SHORT).show();
        }
        else{
            Flashcard currentFlashcard = flashcardSet.get(i);
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle(R.string.edit_flashcard);
            LinearLayout editLayout = new LinearLayout(MainActivity.this);
            editLayout.setOrientation(LinearLayout.VERTICAL);
            final EditText term = new EditText(MainActivity.this);
            term.setText(currentFlashcard.getTerm());
            final EditText definition = new EditText(MainActivity.this);
            definition.setText(currentFlashcard.getDefinition());
            editLayout.addView(term);
            editLayout.addView(definition);
            alert.setView(editLayout);
            alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int j) {
                    currentFlashcard.setTerm(term.getText().toString());
                    currentFlashcard.setDefinition(definition.getText().toString());
                    flashcardSet.update(i, currentFlashcard);
                    flashcardText.setText(currentFlashcard.getTerm().toString());
                }
            });
            alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int j) {
                }
            });
            alert.show();
        }
    }

    private void previousFlashcard() {
        if (flashcardSet.length() == 0){
        }
        else{
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
    }

    private void nextFlashcard() {
        if (flashcardSet.length() == 0){
        }
        else{
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

    }

    @Override
    protected void onPause() {
        super.onPause();
        DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
        String encodedFlashcardSet = null;
        try {
            encodedFlashcardSet = FlashcardSetEncoder.toString(flashcardSet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        databaseHelper.update(listID, encodedFlashcardSet);

    }
}