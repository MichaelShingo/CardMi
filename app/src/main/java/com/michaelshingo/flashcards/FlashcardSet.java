package com.michaelshingo.flashcards;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FlashcardSet implements Serializable {
    private ArrayList<Flashcard> flashcardList = new ArrayList<Flashcard>();
    private ArrayList<Flashcard> studiedFlashcards = new ArrayList<>();
    private ArrayList<Flashcard> recycledFlashcards = new ArrayList<>();
    private String name;


    public ArrayList<Flashcard> getRecycledFlashcardList(){
        return recycledFlashcards;
    }


    public void permamentlyDelete(int i){
        recycledFlashcards.remove(i);
    }

    public void restore(int i){
        flashcardList.add(recycledFlashcards.get(i));
        recycledFlashcards.remove(i);
    }

    public void sortDesc(){
        if (!flashcardList.isEmpty()) {
            Collections.sort(flashcardList, new Comparator<Flashcard>() {
                @Override
                public int compare(final Flashcard f1, final Flashcard f2) {
                    return f2.getTerm().compareToIgnoreCase(f1.getTerm());
                }
            });
        }
    }

    public ArrayList<Flashcard> getStudiedFlashcardList(){
        return studiedFlashcards;
    }

    public void deleteStudied(int i){
        recycledFlashcards.add(studiedFlashcards.get(i));
        studiedFlashcards.remove(i);
    }

    public int getStudiedLength(){
        return studiedFlashcards.size();
    }

    public void sortAsc(){
        if (!flashcardList.isEmpty()) {
            Collections.sort(flashcardList, new Comparator<Flashcard>() {
                @Override
                public int compare(final Flashcard f1, final Flashcard f2) {
                    return f1.getTerm().compareToIgnoreCase(f2.getTerm());
                }
            });
        }
        else{
        }
    }

    public void shuffle(){
        Collections.shuffle(flashcardList);
    }
    public void add(Flashcard flashcard){
        flashcardList.add(flashcard);
    }

    public void markAsStudied(int i){
        studiedFlashcards.add(flashcardList.get(i));
        //flashcardList.remove(i); don't add this here because it's done in textAfterDelete
    }

    public void remove(int i){ //removes flashcards from list without putting it in the recycle bin
        flashcardList.remove(i);

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

    public void recycle(int index){
        recycledFlashcards.add(flashcardList.get(index));
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
