package com.example.homelibrary;

import javafx.beans.property.SimpleStringProperty;

/**
 * This class represents a record
 */
public class Record {

    /**
     * The title of the book
     */
    public SimpleStringProperty title;

    /**
     * The authors of the book
     */
    public SimpleStringProperty authors;

    /**
     * The publisher of the book
     */
    public SimpleStringProperty publisher;

    /**
     * The publishing date of the book
     */
    public SimpleStringProperty publishedDate;

    /**
     * The number of pages in the book
     */
    public SimpleStringProperty pages;

    /**
     * The language of the book
     */
    public SimpleStringProperty language;


    /**
     * The main constructor that is being used
     * @param title: The title of the book
     * @param authors: The authors of the book
     * @param publisher: The publisher of the book
     * @param publishedDate: The publishing date of the book
     * @param pages: The number of pages in the book
     * @param language: The language of the book
     */
    public Record(String title, String authors, String publisher, String publishedDate, String pages, String language) {
        this.title = new SimpleStringProperty(title);
        this.authors = new SimpleStringProperty(authors);
        this.publisher = new SimpleStringProperty(publisher);
        this.publishedDate = new SimpleStringProperty(publishedDate);
        this.pages = new SimpleStringProperty(pages);
        this.language = new SimpleStringProperty(language);
    }


    /**
     * Default constructor
     */
    public Record(){}


    /**
     * Getter for the title
     * @return the title of the book
     */
    public String getTitle() { return title.get(); }

    /**
     * The setter of the title
     * @param title: the appropriate title of the book
     */
    public void setTitle(String title) { this.title.set(title); }

    /**
     * Getter for the authors
     * @return the authors
     */
    public String getAuthors() { return authors.get(); }

    /**
     * The setter of the authors
     * @param authors: the appropriate authors
     */
    public void setAuthors(String authors) { this.authors.set(authors); }

    /**
     * Getter for the publisher
     * @return the publisher
     */
    public String getPublisher() { return publisher.get(); }

    /**
     * The setter of the publisher
     * @param publisher: the appropriate publisher
     */
    public void setPublisher(String publisher) { this.publisher.set(publisher); }

    /**
     * Getter for the published date
     * @return the published date
     */
    public String getPublishedDate() { return publishedDate.get(); }

    /**
     * The setter of the published date
     * @param publishedDate: the appropriate published date
     */
    public void setPublishedDate(String publishedDate) { this.publishedDate.set(publishedDate); }

    /**
     * Getter for the number of pages
     * @return the number of pages
     */
    public String getPages() { return pages.get(); }

    /**
     * The setter of the number of pages
     * @param pages: the appropriate number of pages
     */
    public void setPages(String pages) { this.pages.set(pages); }

    /**
     * Getter for the language
     * @return the language
     */
    public String getLanguage() { return language.get(); }

    /**
     * The setter for the language
     * @param language: the appropriate language
     */
    public void setLanguage(String language) { this.language.set(language); }
}
