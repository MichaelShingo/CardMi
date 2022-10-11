package com.michaelshingo.flashcards;

public class Flashcard {

    private String term;
    private String definition;
    private int id;

    @Override
    public String toString() {
        return "Flashcard{" +
                "term='" + term + '\'' +
                ", definition='" + definition + '\'' +
                ", id=" + id +
                '}';
    }

    public Flashcard(String term, String definition, int id) {
        this.term = term;
        this.definition = definition;
        this.id = id;
    }


    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
