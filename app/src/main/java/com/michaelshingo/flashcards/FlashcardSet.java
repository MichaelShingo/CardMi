package com.michaelshingo.flashcards;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FlashcardSet implements Serializable {
    private ArrayList<Flashcard> flashcardList = new ArrayList<Flashcard>();
    private ArrayList<Flashcard> studiedFlashcards = new ArrayList<>();
    private String name;

    public void sortAsc(){
        Collections.sort(flashcardList, new Comparator<Flashcard>(){
            @Override
            public int compare(Flashcard f1, Flashcard f2) {
                return f2.getTerm().compareTo(f1.getTerm());
            }
        });
    }
    public void add(Flashcard flashcard){
        flashcardList.add(flashcard);
    }

    public void markAsStudied(int i){
        studiedFlashcards.add(flashcardList.get(i));
    }

    public void markToStudy(int i){
        flashcardList.add(studiedFlashcards.get(i));
        studiedFlashcards.remove(i);
    }

    public Flashcard getStudied(int index){
        return studiedFlashcards.get(index);
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
