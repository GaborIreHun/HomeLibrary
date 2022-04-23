package com.example.homelibrary;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.Window;
import javax.swing.*;

import static javafx.application.Platform.exit;

/**
 * This class represents the search page
 */
public class SearchController {

    /**
     * The tableview for collection information
     */
    @FXML
    public TableView<Record> collection_info;

    /**
     * The author's table column
     */
    @FXML
    private TableColumn<Record, String> clmnAuthor;

    /**
     * The published date's table column
     */
    @FXML
    private TableColumn<Record, String> clmnDate;

    /**
     * The language's table column
     */
    @FXML
    private TableColumn<Record, String> clmnLanguage;

    /**
     * The page number's table column
     */
    @FXML
    private TableColumn<Record, String> clmnPages;

    /**
     * The publisher's table column
     */
    @FXML
    private TableColumn<Record, String> clmnPublisher;

    /**
     * The title's table column
     */
    @FXML
    private TableColumn<Record, String> clmnTitle;

    /**
     * The area of the search page/pane
     */
    @FXML
    private AnchorPane searchPane;

    /**
     * Text field for search word input
     */
    @FXML
    private TextField searchDynamic;

    /**
     * The remove button
     */
    @FXML
    private Button btnRemove;


    /**
     * Action handler for back button click event
     * @param event: Click action event
     * @throws IOException: Exception for IO operation fail
     */
    @FXML
    void handleBackAction(ActionEvent event) throws IOException {
        // Initializing FXMLLoader with the appropriate fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
        // Creating a new window
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        // Loading setup to a new Scene object
        Scene scene = new Scene(loader.load());
        // Setting scene with set properties
        stage.setScene(scene);
    }

    /**
     * Action handler for save as button click event
     * @param event: Button click event
     */
    @FXML
    void handleSaveAsAction(ActionEvent event) {

        // Creating FileChooser object
        FileChooser fileChooser = new FileChooser();

        // Setting current window as a target to display the file chooser
        Window stage = searchPane.getScene().getWindow();

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
                SaveFile(file);
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
     * Method to handle print action
     * @param event: Button click event
     */
    @FXML
    void handlePrintAction(ActionEvent event) {

        // Setting the file to be printed
        File file = new File("books.csv");
        // Try-Catch for error handling
        try {
            // Setting up lunch for associated applications on native desktop
            Desktop desktop = Desktop.getDesktop();

            // Printing file with the native desktop printing facility,
            // using the associated application's print command.
            desktop.print(file);
        } catch (IOException e) {
            // Opening confirmation window with warning message
            JOptionPane
                    .showMessageDialog(null, "There was an error while printing!");
            e.printStackTrace();
        }
    }


    /**
     * Action handler for remove button click event
     * @param event: Button click event
     */
    @FXML
    void handleRemoveAction(ActionEvent event) throws IOException {

        //Action when the button is pressed
        btnRemove.setOnAction(e -> {
            // Assigning index of selected row/object
            Record selectedItem = collection_info.getSelectionModel().getSelectedItem();
            // Removing selected row from tableView
            booksToLook.remove(selectedItem);
            // Clearing datastructures for new set of records
            books.clear();
            // Reading remained records and storing them in datastructures
            for (Record book : booksToLook){
                books.add(new String[]{book.getTitle(),
                        book.getAuthors(),
                        book.getPublisher(),
                        book.getPublishedDate(),
                        book.getPages(),
                        book.getLanguage()});
            }
            try { updateCSV(); }
            catch (IOException ex) {
                // Message box to advise of the error
                JOptionPane
                        .showMessageDialog(null, "There was an error while updating the collection!");
                ex.printStackTrace(); }
        });
    }


    /**
     * Action handler for reset button click event
     * @param event: Button click event
     */
    @FXML
    void handleResetAction(ActionEvent event) { searchDynamic.clear(); }


    /**
     * Action handler for menu exit button click event
     * @param event: Button click event
     */
    @FXML
    void handleExit(ActionEvent event) {
        exit();
    }


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
     * ObservableList to store information for the search function
     */
    public ObservableList<Record> booksToLook
            = FXCollections.observableArrayList();

    /**
     * Datastructures to hold information of books
     */
    List<String[]> books = new ArrayList<>();

    /**
     * Initializer for page start up
     */
    public void initialize() {
        // Calling method to load CSV data to TableView
        loadCSV();

        // Initial filtered list
        FilteredList<Record> filteredBooks = new FilteredList<>(booksToLook, b -> true);

        // Adding listener to searchDynamic text field changes and adding appropriate
        // actions to aid the dynamic search of the TableView items
        searchDynamic.textProperty().addListener((observable, oldValue, newValue) ->
                filteredBooks.setPredicate(bookMatches -> {

                    // All records are displayed if no search value given
                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }
                    // Lowering characters of the search words to enhance search
                    String searchKeyword = newValue.toLowerCase();

                    // Checking if the search phrase matches the information in any of the columns
                    if ( bookMatches.getTitle().toLowerCase().indexOf(searchKeyword) > -1 ) {
                        // Match is found
                        return true; }
                    else if ( bookMatches.getAuthors().toLowerCase().indexOf(searchKeyword) > -1 ) {
                        // Match is found
                        return true; }
                    else if ( bookMatches.getPublisher().toLowerCase().indexOf(searchKeyword) > -1 ) {
                        // Match is found
                        return true; }
                    else if ( bookMatches.getPublishedDate().toLowerCase().indexOf(searchKeyword) > -1 ) {
                        // Match is found
                        return true; }
                    else if ( bookMatches.getPages().toLowerCase().indexOf(searchKeyword) > -1 ) {
                        // Match is found
                        return true; }
                    else if ( bookMatches.getLanguage().toLowerCase().indexOf(searchKeyword) > -1 ) {
                        // Match is found
                        return true; }
                    else
                        return false; })
        );

        // Creating datastructures for the matching records for the search phrase
        SortedList<Record> sortedBooks = new SortedList<>(filteredBooks);

        // Binding sorted results with the table view
        sortedBooks.comparatorProperty().bind(collection_info.comparatorProperty());

        // Applying filtered and sorted information to the table view
        collection_info.setItems(sortedBooks);

        // Credit to: tookootek@YouTube: https://www.youtube.com/watch?v=2M0L6w3tMOY
    }


    /**
     * Method to save collection
     * @param file: target file
     */
    static void SaveFile(File file){
        // Initializing variables and datastructures for try and for blocks
        List<String[]> lineInArray = new ArrayList<>();
        String[] book;
        String content = null;
        String bookCollection = null;

        // Reading values from CSV to datastructures
        try (CSVReader reader = new CSVReader(new FileReader("books.csv"))) {
            while ((book = reader.readNext()) != null) {
                lineInArray.add(book);
            }
        } catch (FileNotFoundException e) {
            // Message box to advise of the error
            JOptionPane
                    .showMessageDialog(null, "File to read not found!");
            e.printStackTrace();
        }
        catch (IOException | CsvValidationException e) {
            // Message box to advise of the error
            JOptionPane
                    .showMessageDialog(null, "There was an error while reading the collection!");
            e.printStackTrace();
        }

        // Writing the data from datastructures to a String variable
        for (String[] singleBook: lineInArray){
            for (int i=0; i < singleBook.length; i++){
                content += singleBook[i] + ",";
            }
            bookCollection += content + "\n";
            content = "";
        }

        // Declaring FileWriter object for the try block
        FileWriter fileWriter;

        // Writing CSV from String variable
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(bookCollection);
            fileWriter.close();
        } catch (IOException ex) {
            // Message box to advise of the error
            JOptionPane
                    .showMessageDialog(null, "There was an error while writing the collection!");
        }
    }


    /**
     * Method to update default collection if item was removed by user
     * @throws IOException: Exception for IO operation fail
     */
    public void updateCSV() throws IOException {

        // Initializing CSVWriter object to null for the try block
        CSVWriter writer = null;


        try {
            // Creating CSVWriter with FileWriter object with setting to overwrite any existing copy
            writer = new CSVWriter(new FileWriter("books.csv", false),
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.RFC4180_LINE_END);

            // Looping through datastructures that holds book information
            for (String[] book : books) {
                // Preventing additionally generated quotation marks
                if (book[0] != "" && book[0].charAt(0) == '"') {
                    // Appending books.csv with book record info to next new line
                    writer.writeNext(
                            new String[] {
                                    book[0].substring(1, book[0].length() - 1),
                                    book[1].substring(1, book[1].length() - 1),
                                    book[2].substring(1, book[2].length() - 1),
                                    book[3].substring(1, book[3].length() - 1),
                                    book[4].substring(1, book[4].length() - 1),
                                    book[5].substring(1, book[5].length() - 1)
                            });
                }
                // Preventing null values to be witten to file
                else if (book[0] == null) {
                    // Message box to advise of the error
                    JOptionPane
                            .showMessageDialog(null, "There was an error while updating the collection!");
                    }
                // Writing information into CSV
                else {
                    writer.writeNext(
                            new String[] { book[0], book[1], book[2], book[3], book[4], book[5]});
                }
            }
        } catch (IOException e) {
            // Message box to advise of the error
            JOptionPane
                    .showMessageDialog(null, "There was an error while updating the collection!");
            e.printStackTrace(); }
        // Using finally to close writer whether write is failing or not
        finally { writer.close(); }
    }


    /**
     * Method to loop through the default CSV and display the found information in the TableView
     */
    public void loadCSV() {
        // ObservableList object to store temporary information
        final ObservableList<Record> books
                = FXCollections.observableArrayList();

        // Variable for default CSV location
        String CsvFile = "books.csv";
        // Variable for delimiter
        String FieldDelimiter = ",";

        // Declaring BufferReader object
        BufferedReader br;

        // Initializing record object to null
        Record record = null;
        // Initializing boolean that refers to available info
        Boolean bookIsEmpty = true;

        try {
            // Initializing BufferReader with the default CSV
            br = new BufferedReader(new FileReader(CsvFile));

            // Declaring variable that represents a line in the CSV
            String line;

            // Checking if there is any data in the CSV
            // If not, warning user with messagebox
            if ((br.readLine()) == null) {
                JOptionPane
                    .showMessageDialog(null, "There are no books in your collection!!");
            }
            // If data present loop through the CSV, create record objects from the information
            // and add those to the temporary ObservableList
            else {
                bookIsEmpty = false;
                while ((line = br.readLine()) != null) {
                  String[] book = line.split(FieldDelimiter, -1);

                  record = new Record(book[0], book[1], book[2], book[3], book[4], book[5]);
                  books.add(record);
                }
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "File Not Found!!");
            ex.printStackTrace();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "I/O operation interrupted!!");
            ex.printStackTrace();
        }

        // If there is any data in the CSV utilize the temporary ObservableList to display data appropriately
        if (bookIsEmpty == false) {
            // Assign the appropriate info to the relevant column
            clmnTitle.setCellValueFactory(new PropertyValueFactory<Record, String>("title"));
            clmnAuthor.setCellValueFactory(new PropertyValueFactory<Record, String>("authors"));
            clmnPublisher.setCellValueFactory(new PropertyValueFactory<Record, String>("publisher"));
            clmnDate.setCellValueFactory(new PropertyValueFactory<Record, String>("publishedDate"));
            clmnPages.setCellValueFactory(new PropertyValueFactory<Record, String>("pages"));
            clmnLanguage.setCellValueFactory(new PropertyValueFactory<Record, String>("language"));
            // Loading tableview with created ObservableList<Record>
            collection_info.setItems(books);
            // Assign the value of the temporary ObservableList to the primary one
            booksToLook = books;
        }
    }

    public void handleGuide(ActionEvent actionEvent) {
    }
}
