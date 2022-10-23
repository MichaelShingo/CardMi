package com.michaelshingo.flashcards;


import java.io.Serializable;
import java.util.ArrayList;

public class FlashcardSet implements Serializable {
    private ArrayList<Flashcard> flashcardList = new ArrayList<Flashcard>();
    private ArrayList<Flashcard> studiedFlashcards = new ArrayList<>();
    private String name;

    public void add(Flashcard flashcard){
        flashcardList.add(flashcard);
    }

    public void markAsStudied(int i){
        studiedFlashcards.add(flashcardList.get(i));
        flashcardList.remove(i);
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

    public void update(int i, Flashcard flashcard){
        flashcardList.set(i, flashcard);
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
