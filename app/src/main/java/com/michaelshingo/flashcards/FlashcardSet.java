package com.michaelshingo.flashcards;


import java.io.Serializable;
import java.util.ArrayList;

public class FlashcardSet implements Serializable {
    private ArrayList<Flashcard> flashcardList = new ArrayList<Flashcard>();
    private String name;

    public void add(Flashcard flashcard){
        flashcardList.add(flashcard);
    }

    public Flashcard get(int index){
        return flashcardList.get(index);
    }

    public void remove(int index){
        flashcardList.remove(index);
    }

    public int length(){
        return flashcardList.size();
    }



    public FlashcardSet(String name) {
        this.name = name;
    }


    public ArrayList<Flashcard> getFlashcardList() {
        return flashcardList;
    }

    public void setFlashcardList(ArrayList<Flashcard> flashcardList) {
        this.flashcardList = flashcardList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
