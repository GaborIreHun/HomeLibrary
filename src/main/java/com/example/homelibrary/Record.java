package com.example.homelibrary;

import javafx.beans.property.SimpleStringProperty;

public class Record {

    public SimpleStringProperty title;
    public SimpleStringProperty authors;
    public SimpleStringProperty publisher;
    public SimpleStringProperty publishedDate;
    public SimpleStringProperty pages;
    public SimpleStringProperty language;


    public Record(String title, String authors, String publisher, String publishedDate, String pages, String language) {
        this.title = new SimpleStringProperty(title);
        this.authors = new SimpleStringProperty(authors);
        this.publisher = new SimpleStringProperty(publisher);
        this.publishedDate = new SimpleStringProperty(publishedDate);
        this.pages = new SimpleStringProperty(pages);
        this.language = new SimpleStringProperty(language);
    }

    /**
    @Override
    public String toString() {
        return System.out.println(this.getTitle(), );
    }
    */


    public String getTitle() { return title.get(); }

    public void setTitle(String title) { this.title.set(title); }

    public String getAuthors() {
        return authors.get();
    }

    public void setAuthors(String authors) {
        this.authors.set(authors);
    }

    public String getPublisher() {
        return publisher.get();
    }

    public void setPublisher(String publisher) {
        this.publisher.set(publisher);
    }

    public String getPublishedDate() {
        return publishedDate.get();
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate.set(publishedDate);
    }

    public String getPages() {
        return pages.get();
    }

    public void setPages(String pages) {
        this.pages.set(pages);
    }

    public String getLanguage() {
        return language.get();
    }

    public void setLanguage(String language) {
        this.language.set(language);
    }
}
