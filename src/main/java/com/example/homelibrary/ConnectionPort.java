package com.example.homelibrary;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ConnectionPort{

    /**
     *
     */
    String[] book = new String[6];

    /**
     *
     */
    Record record = new Record(book[0],book[1],book[2],book[3],book[4],book[5]);



    /**
     * Constructor to instantiate ConnectionPort object.
     *
     * @throws IOException if underlying service fails.
     * @throws InterruptedException if underlying service fails.
     * @param request String value with address.
     */
    public ConnectionPort(String request) throws IOException, InterruptedException {
        /**
         * Request for HTTP connection.
         */
        makeRequest(request);
    }


    /**
     * Default constructor.
     */
    public ConnectionPort() {
    }


    /**
     * Method makes request to access data. After response is received it will map required
     * information the AssetQuote class instance fields.
     *
     * @param requestString address with information.
     * @throws IOException thrown if wrong data is entered.
     * @throws InterruptedException thrown when a thread is interrupted while it's waiting, sleeping,
     *                              or otherwise occupied.
     */
    private void makeRequest(String requestString) throws IOException, InterruptedException {

        // Code reference - yahoo finance api tutorial. Line 55 - 61, creating user request
        // with appropriate URI, API key and type of request.
        /**
         * API key of the user.
         */
        String key = "AIzaSyAyzBOao9W5uizzQkV2ifZ96HyhOvPA1pE";
        HttpRequest userRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://www.googleapis.com/books/v1/volumes?q=isbn:" + requestString))
                .header("x-api-key", key)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        // Storing response from API.
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(userRequest, HttpResponse.BodyHandlers.ofString());

        // call mapping method with received data.
        listInfo(response.body());
        //System.out.println(response.body());
    }


    /**
     *
     * @param responseBody
     */
    private void listInfo(String responseBody) {


        String title = null;
        List<String> authors = new ArrayList<>();
        String publisher = null;
        String publishedDate = null;
        int pages = 0;
        String language = null;


        Container fullJsonObject = new Gson().fromJson(responseBody, Container.class);

        for (Item i : fullJsonObject.items) {
            title = i.volumeInfo.title;
            authors.add(i.volumeInfo.authors[0]);
            publisher = i.volumeInfo.publisher;
            publishedDate = i.volumeInfo.publishedDate;
            pages = i.volumeInfo.pageCount;
            language = i.volumeInfo.language;

            System.out.println(i.volumeInfo.authors[0]);
            System.out.println(i.volumeInfo.title);
            System.out.println(i.volumeInfo.publisher);
            System.out.println(i.volumeInfo.publishedDate);
            System.out.println(i.volumeInfo.pageCount);
            System.out.println(i.volumeInfo.language);
        }

        
        String[] array = authors.toArray(new String[0]);

        book[0] = title;
        book[1] = array[0];
        book[2] = publisher;
        book[3] = publishedDate;
        if(pages == 0) { book[4] = null; }
        else { book[4] = Integer.toString(pages); }
        book[5] = language;

        for (int i = 0; i < book.length; i++) {
            if (book[i] == null) {
                book[i] = "No Information";
            }
        }
    }


    /**
     *
     * @return
     */
    public String getTitle() { return record.getTitle(); }

    /**
     *
     * @return
     */
    public String getAuthor() { return record.getAuthors(); }

    /**
     *
     * @return
     */
    public String getPublisher() { return record.getPublisher(); }

    /**
     *
     * @return
     */
    public String getPublishedDate() { return record.getPublishedDate(); }

    /**
     *
     * @return
     */
    public String getPages() { return record.getPages(); }

    /**
     *
     * @return
     */
    public String getLanguage() { return record.getLanguage(); }


    /**
     *
     */
    public class Container {

        public String kind;
        public int totalItems;
        public Item[] items;

        public String getKind() { return kind; }

        public void setKind(String kind) { this.kind = kind; }

        public int getTotalItems () { return totalItems; }

        public void setTotalItems (int totalItems) { this.totalItems = totalItems; }

        public Item[] getItem () { return items; }

        public void setItems (Item[] item) { this.items = items; }
    }

    /**
     *
     */
    public class Item {

        public VolumeInfo volumeInfo;

        public VolumeInfo getVolumeInfo() { return volumeInfo; }

        public void setVolumeInfo(VolumeInfo volumeInfo) { this.volumeInfo = volumeInfo; }
    }

    /**
     *
     */
    public class VolumeInfo {

        public String title;
        public String[] authors;
        public String publisher;
        public String publishedDate;
        public int pageCount;
        public String language;

        public String getTitle() { return title; }

        public void setTitle(String title) { this.title = title; }

        public String[] getAuthors() { return authors; }

        public void setAuthors(String[] authors) { this.authors = authors; }

        public String getPublisher() { return publisher; }

        public void setPublisher(String publisher) { this.publisher = publisher; }

        public String getPublishedDate() { return publishedDate; }

        public void setPublishedDate(String publishedDate) { this.publishedDate = publishedDate; }

        public int getPageCount() { return pageCount; }

        public void setPageCount(int pageCount) { this.pageCount = pageCount; }

        public String getLanguage() { return language; }

        public void setLanguage(String language) { this.language = language; }
    }

}




