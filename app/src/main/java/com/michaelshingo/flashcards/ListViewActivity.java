package com.michaelshingo.flashcards;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.recycle_menu, menu);
//    }

    public void updateListView(){
        ArrayList<Flashcard> listFlashcardList = flashcardSet.getFlashcardList();
        ArrayList<String> listViewTermsList = new ArrayList<>();

        for (Flashcard card:listFlashcardList){
            listViewTermsList.add(card.getTerm());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ListViewActivity.this, android.R.layout.simple_list_item_1, listViewTermsList);
        listViewListView.setAdapter(arrayAdapter);

//        registerForContextMenu(recycleListView);

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

//    @Override
//    public boolean onContextItemSelected(@NonNull MenuItem item) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        int listPosition = info.position;
//
//        switch (item.getItemId()) {
//            case R.id.btn_permanently_delete:
//                flashcardSet.permamentlyDelete(listPosition);
//                updateListView();
//                return true;
//            case R.id.btn_restore:
//                flashcardSet.restore(listPosition);
//                updateListView();
//                return true;
//            default:
//                return true;
//        }
//    }

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

        updateListView();

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
}
