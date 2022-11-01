package com.michaelshingo.flashcards;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;


public class StudiedActivity extends AppCompatActivity {
    ListView studiedListView;
    FlashcardSet flashcardSet;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();



    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        //TODO make sure this protects against null values and empty lists
        //TODO is the database updating when you add things to studied?
        switch (item.getItemId()) {
            case R.id.btn_list:
                Toast.makeText(StudiedActivity.this, "Show as list", Toast.LENGTH_SHORT).show();
                return true;
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studied);

        Bundle bundle = getIntent().getExtras();
        String encodedFlashcardSet = bundle.getString("set");
        try {
            flashcardSet = (FlashcardSet) FlashcardSetEncoder.fromString(encodedFlashcardSet);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(flashcardSet);
        //INSTANTIATION

        studiedListView = findViewById(R.id.studiedListView);

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


    }
}
