package com.example.homelibrary;

import java.io.IOException;

public class Main {

    String isbn;

    public Main() {
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        //0764555898 php
        //9780061438295 body
        //9781943494040 species
        ConnectionPort connect = new ConnectionPort("9781943494040");
    }

    public void setIsbn(String isbn) { this.isbn = isbn; }
}
