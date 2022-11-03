package com.michaelshingo.flashcards;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;


public class ListViewActivity extends AppCompatActivity {
    private ListView listViewListView;
    private FlashcardSet flashcardSet;
    private FloatingActionButton btn_list_back;
    private int listID;
    private TextView activityTitleText;

    public void updateListView(){
        ArrayList<Flashcard> listFlashcardList = flashcardSet.getFlashcardList();
        ArrayList<String> listViewTermsList = new ArrayList<>();

        for (Flashcard card:listFlashcardList){
            listViewTermsList.add(card.getTerm());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ListViewActivity.this, android.R.layout.simple_list_item_1, listViewTermsList);
        listViewListView.setAdapter(arrayAdapter);

        listViewListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Bundle listBundle = new Bundle();
                Intent listIntent = new Intent(ListViewActivity.this, MainActivity.class);
                String listEncodedSet = null;
                try {
                    listEncodedSet = FlashcardSetEncoder.toString(flashcardSet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                listBundle.putString("set", listEncodedSet);
                listBundle.putInt("id", listID);
                listBundle.putInt("listPosition", i);
                listIntent.putExtras(listBundle);
                startActivity(listIntent);

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

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
        btn_list_back = findViewById(R.id.btn_list_back);
        listViewListView = findViewById(R.id.listListView);
        activityTitleText = findViewById(R.id.action_bar_title);

        updateListView();
        activityTitleText.setText(R.string.list_view);

        btn_list_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle listBundle = new Bundle();
                Intent listIntent = new Intent(ListViewActivity.this, MainActivity.class);
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
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        DatabaseHelper databaseHelper = new DatabaseHelper(ListViewActivity.this);
        String encodedFlashcardSet = null;
        try {
            encodedFlashcardSet = FlashcardSetEncoder.toString(flashcardSet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        databaseHelper.update(listID, encodedFlashcardSet);
    }
}
