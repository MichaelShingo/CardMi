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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;


public class RecycleActivity extends AppCompatActivity {
    private ListView recycleListView;
    private FlashcardSet flashcardSet;
    private FloatingActionButton btn_recycle_back;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.recycle_menu, menu);
    }

    public void updateListView(){
        ArrayList<Flashcard> recycledFlashcardList = flashcardSet.getRecycledFlashcardList();
        ArrayList<String> recycledTermList = new ArrayList<>();
        ArrayList<String> recycledDefList = new ArrayList<>();

        for (Flashcard card:recycledFlashcardList){
            recycledTermList.add(card.getTerm());
        }
        for (Flashcard card:recycledFlashcardList){
            recycledDefList.add(card.getDefinition());
        }


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(RecycleActivity.this, android.R.layout.simple_list_item_1, recycledTermList);
        recycleListView.setAdapter(arrayAdapter);

        registerForContextMenu(recycleListView);

        recycleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                recycleListView.showContextMenuForChild(view);
            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int listPosition = info.position;

        switch (item.getItemId()) {
            case R.id.btn_permanently_delete:
                flashcardSet.permamentlyDelete(listPosition);
                updateListView();
                return true;
            case R.id.btn_restore:
                flashcardSet.restore(listPosition);
                updateListView();
                return true;
            default:
                return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);

        Bundle bundle = getIntent().getExtras();
        int listID = bundle.getInt("id");
        String encodedFlashcardSet = bundle.getString("set");
        try {
            flashcardSet = (FlashcardSet) FlashcardSetEncoder.fromString(encodedFlashcardSet);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //INSTANTIATION
        btn_recycle_back = findViewById(R.id.btn_recycle_back);
        recycleListView = findViewById(R.id.recycleListView);

        btn_recycle_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle recycleBundle = new Bundle();
                Intent recycleIntent = new Intent(RecycleActivity.this, MainActivity.class);
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
            }
        });
        updateListView();
    }
}
