package com.michaelshingo.flashcards;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;


public class StudiedActivity extends AppCompatActivity {
    private ListView studiedListView;
    private FlashcardSet flashcardSet;
    private FloatingActionButton btn_studied_back;
    private int listID;
    private TextView activityTitleText;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.studied_menu, menu);
    }


    public void updateListView(){
        ArrayList<Flashcard> studiedFlashcardList = flashcardSet.getStudiedFlashcardList();
        ArrayList<String> studiedTermList = new ArrayList<>();
        ArrayList<String> studiedDefList = new ArrayList<>();

        for (Flashcard card:studiedFlashcardList){
            studiedTermList.add(card.getTerm());
        }
        for (Flashcard card:studiedFlashcardList){
            studiedDefList.add(card.getDefinition());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(StudiedActivity.this, android.R.layout.simple_list_item_1, studiedTermList);
        studiedListView.setAdapter(arrayAdapter);

        registerForContextMenu(studiedListView);

        studiedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                studiedListView.showContextMenuForChild(view);
            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int listPosition = info.position;

        switch (item.getItemId()) {
            case R.id.btn_mark_for_study:
                flashcardSet.markToStudy(listPosition);
                updateListView();
                return true;
            case R.id.btn_delete_studied:
                flashcardSet.deleteStudied(listPosition);
                updateListView();
                return true;
            default:
                return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studied);

        Bundle bundle = getIntent().getExtras();
        listID = bundle.getInt("id");
        String encodedFlashcardSet = bundle.getString("set");
        try {
            flashcardSet = (FlashcardSet) FlashcardSetEncoder.fromString(encodedFlashcardSet);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //INSTANTIATION
        btn_studied_back = findViewById(R.id.btn_studied_back);
        studiedListView = findViewById(R.id.studiedListView);
        activityTitleText = findViewById(R.id.action_bar_title);

        activityTitleText.setText(R.string.studied_cards);

        btn_studied_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle studiedBundle = new Bundle();
                Intent studiedIntent = new Intent(StudiedActivity.this, MainActivity.class);
                String studiedEncodedSet = null;
                try {
                    studiedEncodedSet = FlashcardSetEncoder.toString(flashcardSet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                studiedBundle.putString("set", studiedEncodedSet);
                studiedBundle.putInt("id", listID);
                studiedIntent.putExtras(studiedBundle);
                startActivity(studiedIntent);
            }
        });
        updateListView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DatabaseHelper databaseHelper = new DatabaseHelper(StudiedActivity.this);
        String encodedFlashcardSet = null;
        try {
            encodedFlashcardSet = FlashcardSetEncoder.toString(flashcardSet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        databaseHelper.update(listID, encodedFlashcardSet);
    }
}
