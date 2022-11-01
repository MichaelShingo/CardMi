package com.michaelshingo.flashcards;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;


public class StudiedActivity extends AppCompatActivity {
    ListView studiedListView;
    FlashcardSet flashcardSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studied);

        //INSTANTIATION

        studiedListView = findViewById(R.id.studiedListView);

//        //adjust formatting of popupwindow
//        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//        View popupView = inflater.inflate(R.layout.popup_window_main, null);
//        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
//        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
//        boolean focusable = true; //allows taps outside the window to dismiss it
//        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
//        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//        popupWindow.setElevation(35);
//
//        studiedListView = popupView.findViewById(R.id.popup_listview);
//        ArrayList<Flashcard> studiedFlashcardList = flashcardSet.getStudiedFlashcardList();
//        ArrayList<String> studiedTermList = new ArrayList<>();
//        ArrayList<String> studiedDefList = new ArrayList<>();
//        for (Flashcard card:studiedFlashcardList){
//            studiedTermList.add(card.getTerm());
//        }
//        for (Flashcard card:studiedFlashcardList){
//            studiedDefList.add(card.getDefinition());
//        }
//
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, studiedTermList);
//        studiedListView.setAdapter(arrayAdapter);
//
//        popupWindow.showAtLocation(flashcard, Gravity.CENTER, 0, 0);
//        registerForContextMenu(studiedListView);


    }
}
