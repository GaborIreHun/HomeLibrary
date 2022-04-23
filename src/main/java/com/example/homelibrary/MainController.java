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
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.awt.*;
import java.io.*;
import javax.swing.*;

import static javafx.application.Platform.exit;


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
     * The save as menu button
     */
    @FXML
    private MenuItem btnMenuSaveAs;

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


    /**
     * Initializer for page start up
     */
    public void initialize() { counter(); }


    /**
     * Action handler for menu about button click event
     * @param event: Button click event
     */
    @FXML
    void handleAbout(ActionEvent event) {
        // Opening message window with app info
        JOptionPane
                .showMessageDialog(null, "Home Library v1.0\n" +
                        "Create your home library record easy\n" +
                        "Author: Gabor Sebestyen\n" +
                        "2022");
    }


    /**
     * Action handler for menu technical documentation button click event
     * @param event: Button click event
     */
    @FXML
    void handleTechnical(ActionEvent event) throws IOException {
        File file = new java.io.File("src/javadocs/index.html").getAbsoluteFile();
        Desktop.getDesktop().open(file);
    }


    /**
     * Action handler for menu exit button click event
     * @param event: Button click event
     */
    @FXML
    void handleExit(ActionEvent event) {
        exit();
    }


    /**
     * Action handler for save as button click event
     * @param event: Button click event
     */
    @FXML
    void handleSaveAs(ActionEvent event) {
        // Creating FileChooser object
        FileChooser fileChooser = new FileChooser();

        // Setting current window as a target to display the file chooser
        Window stage = mainPane.getScene().getWindow();

        // Creating fileChooser title
        fileChooser.setTitle("Save file As");
        // Setting proposed file name
        fileChooser.setInitialFileName("New books");
        // Setting available file extensions
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("CSV","*.csv"));

        try{
            // Open the fileChooser
            File file = fileChooser.showSaveDialog(stage);
            // Save to the chosen directory
            fileChooser.setInitialDirectory(file.getParentFile());

            if (file != null){
                SearchController.SaveFile(file);
            }
        }
        catch(Exception exception){
            // Opening confirmation window with warning message
            JOptionPane
                    .showMessageDialog(null, "Problem occurred while saving the file!");
        }

        // Credit to: Noble Code Monkeys@YouTube: https://www.youtube.com/watch?v=7lnVelyHxrc
    }


    /**
     * Action handler for add button click event
     * @param event: Click action event
     */
    @FXML
    void handleAddAction(ActionEvent event) throws IOException {
        // Calling function to add book to collection
        addBook();
    }


    /**
     * Action handler for find button click event
     * @param event: Click action event
     * @throws IOException: Exception for IO operation fail
     * @throws InterruptedException: Exception for thread interruption
     */
    @FXML
    void handleFindAction(ActionEvent event) throws IOException, InterruptedException {

        // Checking for missing input for ISBN
        if (txtISBN.getText().length() == 0 || txtISBN.getText() == "") {
            // Opening confirmation window with warning message
            JOptionPane
                    .showMessageDialog(null, "Enter th ISBN number!");
        }
        // Checking if ISBN input contains non-numeric characters
        else if (!txtISBN.getText().matches("[0-9]+")) {
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
        } catch (IOException e) {
            // Opening confirmation window with warning message
            JOptionPane
                    .showMessageDialog(null, "Problem occurred with the printing!");
            // Printing stack trace to console for debugging purposes
            e.printStackTrace();
        }
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

                // Message box to confirm the addition
                JOptionPane
                        .showMessageDialog(null, "The book has been added!");
                // Clearing ISBN text field
                txtISBN.clear();
                // Clearing items from tableview
                book_info.getItems().clear();
            }
            // Catch for Input Output exception
            catch (IOException e) {
                JOptionPane
                        .showMessageDialog(null, "An error has occurred while adding the file!");
                e.printStackTrace(); }
            // Using finally to close writer whether write is failing or not
            finally { writer.close(); }
        }
        // Calling counter() method to rectify item counter
        counter();
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
        catch (FileNotFoundException ex) {
            // Opening confirmation window with warning message
            JOptionPane
                    .showMessageDialog(null, "Problem occurred while handling the file!");
            ex.printStackTrace();
        }
        catch (IOException ex) {
            // Opening confirmation window with warning message
            JOptionPane
                    .showMessageDialog(null, "I/O problem occurred!");
            ex.printStackTrace();
        }

        // Assigning counted item value to instance variable
        numberOfBooks = counter;
        // Setting item counter text field with appropriate value
        txtCount.setText(Integer.toString(numberOfBooks));
        // Refreshing text field of the item counter
        txtCount.redo();
    }

    public void handleGuide(ActionEvent actionEvent) {
    }
}