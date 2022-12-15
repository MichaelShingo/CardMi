package com.michaelshingo.flashcards;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;


public class SelectActivity extends AppCompatActivity {
    //TODO reordering and sorting the list??
    //TODO searching the lisT?
    //TODO different color themes in settings

    private FloatingActionButton btn_add;
    private ListView listView;
    private ArrayList<FlashcardSet> flashcardSetArray;
    private FloatingActionButton btn_deleteSet;
    private ArrayList<String> encodedFlashcardSets;
    private ArrayList<FlashcardSet> decodedFlashcardSets;
    private int MAX_CHAR_COUNT = 40;
    Toolbar toolbar;

    ArrayList<Integer> idArray;

    private void updateListView(){
        DatabaseHelper databaseHelper = new DatabaseHelper(SelectActivity.this);
        encodedFlashcardSets = databaseHelper.getAll();
        idArray = databaseHelper.getIDs();
        decodedFlashcardSets = new ArrayList<>();

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
        System.out.println(allNames);
        for (FlashcardSet flashcardSet: decodedFlashcardSets){
            if (flashcardSet != null) {
                allNames.add(flashcardSet.getName());
            }
        }
        listView = findViewById(R.id.flashcardSetList);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplication(), R.layout.centered_list, allNames);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int currentID = idArray.get(i);
                Intent intent = new Intent(SelectActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                String currentEncodedSet = null;
                try {
                    currentEncodedSet = FlashcardSetEncoder.toString(decodedFlashcardSets.get(i));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bundle.putString("set", currentEncodedSet);
                bundle.putInt("id", currentID); //this should put the id number of the clicked item
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.floating_menu, menu); //provide menu from the method parameters
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        DatabaseHelper databaseHelper = new DatabaseHelper(SelectActivity.this);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int listPosition = info.position;
        int currentID = idArray.get(listPosition);

        switch(item.getItemId()){
            case R.id.editName:
                //show alert, populate textView with name, edit name, update name in FlashcardSet and add to database, update listview
                AlertDialog.Builder alert =  new AlertDialog.Builder(SelectActivity.this);
                EditText editSetName = new EditText(SelectActivity.this);
                editSetName.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                TextView charCount = new TextView(SelectActivity.this);
                LinearLayout editNameLayout = new LinearLayout(SelectActivity.this);
                editNameLayout.setOrientation(LinearLayout.VERTICAL);
                editNameLayout.addView(editSetName);
                editNameLayout.addView(charCount);
                FlashcardSet currentFlashcardSet = decodedFlashcardSets.get(listPosition);
                editSetName.setText(currentFlashcardSet.getName());
                charCount.setText(editSetName.getText().toString().length() + "/" + MAX_CHAR_COUNT);

                TextWatcher editNameCounter = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        int currentLength = editSetName.length(); //charSequence.length();
                        charCount.setText(currentLength + "/" + MAX_CHAR_COUNT);
                        if (currentLength > MAX_CHAR_COUNT){
                            charCount.setTextColor(getResources().getColor(R.color.red));
                        }
                        else{
                            charCount.setTextColor(getResources().getColor(R.color.black));
                        }
                    }
                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                };

                editSetName.addTextChangedListener(editNameCounter);

                alert.setView(editNameLayout);
                alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (editSetName.length() == 0){
                            currentFlashcardSet.setName(getResources().getString(R.string.untitled));
                        }
                        else if (editSetName.length() > 40){
                            currentFlashcardSet.setName(editSetName.getText().toString().substring(0,MAX_CHAR_COUNT));
                        }
                        else {
                            currentFlashcardSet.setName(editSetName.getText().toString());
                        }

                        String currentEncodedFlashcardSet = null;
                        try {
                            currentEncodedFlashcardSet = FlashcardSetEncoder.toString(currentFlashcardSet);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        databaseHelper.update(currentID, currentEncodedFlashcardSet);
                        updateListView();
                    }
                });
                alert.show();
                return true;

            case R.id.deleteSet:
                String currentEncodedSet = encodedFlashcardSets.get(listPosition);
                databaseHelper.delete(currentID);
                updateListView();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        //INSTANTIATION

        FloatingActionButton btn_add = findViewById(R.id.addSet); //name this variable better...
        updateListView();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(SelectActivity.this, R.animator.property_animator1);
                set.setTarget(btn_add);
                set.start();
                btn_add.animate().scaleX(0).scaleY(0);
                btn_add.hide();
                AlertDialog.Builder alert = new AlertDialog.Builder(SelectActivity.this);
                final EditText nameText = new EditText(SelectActivity.this);
                nameText.setHint(R.string.name_flashcard);
                TextView charCount = new TextView(SelectActivity.this);
                LinearLayout addFlashcardLayout = new LinearLayout(SelectActivity.this);
                addFlashcardLayout.setOrientation(LinearLayout.VERTICAL);
                addFlashcardLayout.addView(nameText);
                addFlashcardLayout.addView(charCount);
                charCount.setText(nameText.length() + "/" + MAX_CHAR_COUNT);

                TextWatcher addNameCounter = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        int currentLength = nameText.length();
                        charCount.setText(currentLength + "/" + MAX_CHAR_COUNT);
                        if (currentLength > MAX_CHAR_COUNT){
                            charCount.setTextColor(getResources().getColor(R.color.red));
                        }
                        else {
                            charCount.setTextColor(getResources().getColor(R.color.black));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                };

                nameText.addTextChangedListener(addNameCounter);


                alert.setView(addFlashcardLayout);
                alert.setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FlashcardSet flashcardSet = new FlashcardSet("");
                        String userInput = nameText.getText().toString();

                        if (userInput.length() == 0) {
                            flashcardSet.setName(getResources().getString(R.string.untitled));
                        }
                        else if (userInput.length() > 40) {
                            flashcardSet.setName(nameText.getText().toString().substring(0, MAX_CHAR_COUNT));
                        }
                        else {
                            flashcardSet.setName(nameText.getText().toString());
                        }
                        String encodedFlashcardSet = "Untitled"; //R.string.untitled;
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

                alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        btn_add.animate().scaleX(1f).scaleY(1f);
                        btn_add.show();
                    }
                });
                alert.show();
                String name = "";
                FlashcardSet flashcardSet = new FlashcardSet(name);
            }
        });
        }

    @Override
    protected void onResume() {
        super.onResume();
        updateListView();
    }
}

