package com.michaelshingo.flashcards;


import java.util.ArrayList;

public class FlashcardSet {
    private ArrayList<Flashcard> flashcardSet;
    private String name;

    public FlashcardSet(String name) {
        this.name = name;
    }


    public ArrayList<Flashcard> getFlashcardSet() {
        return flashcardSet;
    }

    public void setFlashcardSet(ArrayList<Flashcard> flashcardSet) {
        this.flashcardSet = flashcardSet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
