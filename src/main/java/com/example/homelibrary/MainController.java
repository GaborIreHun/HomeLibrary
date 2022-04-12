package com.example.homelibrary;

import com.opencsv.CSVWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.awt.*;
import java.io.*;
import javax.swing.*;


/**
 * This class represents the main page
 */
public class MainController {


    /**
     * The sum of book information
     */
    String[] book = new String[6];

    /**
     * The area of the main page/pane
     */
    @FXML
    private AnchorPane mainPane;

    /**
     * The add button
     */
    @FXML
    private Button btnAdd;

    /**
     * The find button
     */
    @FXML
    private Button btnFind;

    /**
     * The reset button
     */
    @FXML
    private Button btnReset;

    /**
     * The tableview for found book information
     */
    @FXML
    private TableView<Record> book_info;

    /**
     * The author's table column
     */
    @FXML
    public TableColumn<Record, String> clmnAuthor;

    /**
     * The published date's table column
     */
    @FXML
    public TableColumn<Record, String> clmnDate;

    /**
     * The language's table column
     */
    @FXML
    public TableColumn<Record, String> clmnLanguage;

    /**
     * The page number's table column
     */
    @FXML
    public TableColumn<Record, String> clmnPages;

    /**
     * The publisher's table column
     */
    @FXML
    public TableColumn<Record, String> clmnPublisher;

    /**
     * The title's table column
     */
    @FXML
    public TableColumn<Record, String> clmnTitle;

    /**
     * Text field of the item counter
     */
    @FXML
    private TextField txtCount;

    /**
     * Text field for ISBN input
     */
    @FXML
    private TextField txtISBN;

    /**
     * Integer for item counter
     */
    int numberOfBooks = 0;

    // open collection -> desktop.browse(URI.create("books.csv"));

    /**
     * Initializer fo page start up
     */
    public void initialize() { counter(); }


    /**
     * Action handler for add button click event
     * @param event: Click action event
     */
    @FXML
    void handleAddAction(ActionEvent event) throws IOException {
        // Calling function to add book to collection
        addBook();
        // Refreshing item counter field
        txtCount.redo();
    }


    /**
     * Action handler for find button click event
     * @param event: Click action event
     * @throws IOException: Exception for IO operation fail
     * @throws InterruptedException: Exception for thread interruption
     */
    @FXML
    void handleFindAction(ActionEvent event) throws IOException, InterruptedException {

        // Checking if ISBN input contains non-numeric characters
        if (!txtISBN.getText().matches("[0-9]+")) {
            // Opening confirmation window with warning message
            JOptionPane
                    .showMessageDialog(null, "ISBN is numeric, no other character allowed!");
        }
        // Checking for appropriate length or ISBN input
        else if (txtISBN.getText().length() != 10 && txtISBN.getText().length() != 0
        && txtISBN.getText().length() != 13) {
            // Opening confirmation window with warning message
            JOptionPane
                    .showMessageDialog(null, "ISBN consists either 10 or 13 characters!");
        }
        // Checking for missing input for ISBN
        else if (txtISBN.getText().length() == 0 || txtISBN.getText() == "") {
            // Opening confirmation window with warning message
            JOptionPane
                    .showMessageDialog(null, "Enter th ISBN number!");
        }
        else {
            // Boolean to determine book info received
            Boolean bookIsEmpty = true;
            // Initializing ConnectionPort object to null
            ConnectionPort connect = null;

            // Error prevention of no returned values
            try {
                // Initializing ConnectionPort object with ISBN parameter
                connect = new ConnectionPort(txtISBN.getText());
                // Changing boolean to state book info is not empty
                bookIsEmpty = false;
            } catch (NullPointerException e) {
                // Opening confirmation window with warning message
                JOptionPane
                        .showMessageDialog(null, "Book not found!");
            }

            // Checking if book info is not empty to load data in tableview
            if (bookIsEmpty == false) {

            // Assigning values to prepare possible add action
            book[0] = connect.book[0];
            book[1] = connect.book[1];
            book[2] = connect.book[2];
            book[3] = connect.book[3];
            book[4] = connect.book[4];
            book[5] = connect.book[5];

            // Loading found information into a Record object
            Record list = new Record(connect.book[0], connect.book[1], connect.book[2], connect.book[3],
                    connect.book[4], connect.book[5]);

            // Initializing ObservableList<Record> with an observableArrayList loaded with previous Record object
            final ObservableList<Record> dataList
                    = FXCollections.observableArrayList(list);

            // Setting tableview cell values with appropriate variable names
            clmnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            clmnAuthor.setCellValueFactory(new PropertyValueFactory<>("authors"));
            clmnPublisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
            clmnDate.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));
            clmnPages.setCellValueFactory(new PropertyValueFactory<>("pages"));
            clmnLanguage.setCellValueFactory(new PropertyValueFactory<>("language"));

            // Loading tableview with created ObservableList<Record>
            book_info.setItems(dataList);
            }
        }
    }


    /**
     * Action handler for print button click event
     * @param event: Click action event
     */
    @FXML
    void handlePrintMainAction(ActionEvent event) {

        // Setting the file to be printed
        File file = new File("books.csv");
        // Try-Catch for error handling
        try {
            // Setting up lunch for associated applications on native desktop
            Desktop desktop = Desktop.getDesktop();

            // Prints a file with the native desktop printing facility,
            // using the associated application's print command.
            desktop.print(file);
        } catch (IOException e) { e.printStackTrace(); }
    }


    /**
     * Action handler for reset button click event
     * @param event: Click action event
     */
    @FXML
    void handleResetMainAction(ActionEvent event) {
        // Clearing ISBN text field
        txtISBN.clear();
        // Clearing items from tableview
        book_info.getItems().clear();
    }


    /**
     * Action handler for search button click event
     * @param event: Click action event
     * @throws IOException: Thrown when a thread is interrupted
     */
    @FXML
    void handleSearchCollectionAction(ActionEvent event) throws IOException {
        // Initializing FXMLLoader with the appropriate fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("searchWindow.fxml"));
        // Creating a new window
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        // Loading setup to a new Scene object
        Scene scene = new Scene(loader.load());
        // Setting scene with set properties
        stage.setScene(scene);
    }


    /**
     * Method for adding book to collection
     * @throws IOException: Thrown when a thread is interrupted
     */
    public void addBook() throws IOException {

        // Creating target file path for write
        final String CSV_FILE_PATH = "books.csv";

        // Checking if there is appropriate data to add
        if (book[0] == "" || txtISBN == null || txtISBN.getText() == "") {
            // Opening confirmation window with warning message
            JOptionPane
                    .showMessageDialog(null, "Nothing to add!");
        }
        else {
            // Initializing CSVWriter object with null
            CSVWriter writer = null;
            // Try-Catch for error handling
            try {

                // Creating CSVWriter with FileWriter object where target can be appended
                writer = new CSVWriter(new FileWriter("books.csv", true));

                // Appending books.csv with book record info to next new line
                writer.writeNext(book);

                // Calling counter() method to
                counter();
                // Refreshing text field of the item counter
                txtCount.redo();
            }
            // Catch for Input Output exception
            catch (IOException e) { e.printStackTrace(); }
            // Using finally to close writer whether write is failing or not
            finally { writer.close(); }
        }

    }


    /**
     * Method for counting items in collection
     */
    public void counter() {

        // Initializing counter
        int counter = 0;
        // String with collection file name and extension
        String CsvFile = "books.csv";

        // Declaring BufferReader
        BufferedReader br;

        // Try-Catch for error handling
        try {
            // Initializing BufferReader with new FileReader object pointing to target file
            br = new BufferedReader(new FileReader(CsvFile));

            // Declaring variable for line in collection
            String line;
            // Reading each line that has data amd incrementing counter at each iteration
            while ((line = br.readLine()) != null) { ++counter; }
            // Refreshing text field of the item counter
            txtCount.redo();
        }
        catch (FileNotFoundException ex) { ex.printStackTrace(); }
        catch (IOException ex) { ex.printStackTrace(); }

        // Assigning counted item value to instance variable
        numberOfBooks = counter;
        // Setting item counter text field with appropriate value
        txtCount.setText(Integer.toString(numberOfBooks));
        // Refreshing text field of the item counter
        txtCount.redo();
    }
}