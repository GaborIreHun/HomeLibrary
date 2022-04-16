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

/**
 *
 */
public class SearchController {

    /**
     *
     */
    @FXML
    public TableView<Record> collection_info;

    /**
     *
     */
    @FXML
    private TableColumn<Record, String> clmnAuthor;

    /**
     *
     */
    @FXML
    private TableColumn<Record, String> clmnDate;

    /**
     *
     */
    @FXML
    private TableColumn<Record, String> clmnLanguage;

    /**
     *
     */
    @FXML
    private TableColumn<Record, String> clmnPages;

    /**
     *
     */
    @FXML
    private TableColumn<Record, String> clmnPublisher;

    /**
     *
     */
    @FXML
    private TableColumn<Record, String> clmnTitle;

    /**
     *
     */
    @FXML
    private AnchorPane searchPane;

    /**
     *
     */
    @FXML
    private TextField searchDynamic;

    /**
     *
     */
    @FXML
    private Button btnRemove;


    /**
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void handleBackAction(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }

    /**
     *
     * @param event
     */
    @FXML
    void handleSaveAsAction(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();

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
        catch(Exception exception){}

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
        } catch (IOException e) { e.printStackTrace(); }
    }


    /**
     *
     * @param event
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
     *
     * @param event
     */
    @FXML
    void handleResetAction(ActionEvent event) { searchDynamic.clear(); }


    /**
     *
     */
    public ObservableList<Record> booksToLook
            = FXCollections.observableArrayList();

    /**
     *
     */
    List<String[]> books = new ArrayList<>();

    /**
     *
     */
    public void initialize() {

        loadCSV();

        // Initial filtered list
        FilteredList<Record> filteredBooks = new FilteredList<>(booksToLook, b -> true);

        searchDynamic.textProperty().addListener((observable, oldValue, newValue) ->
                filteredBooks.setPredicate(bookMatches -> {

                    // All records are displayed if no search value given
                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }

                    String searchKeyword = newValue.toLowerCase();

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

        //
        SortedList<Record> sortedBooks = new SortedList<>(filteredBooks);

        // Binding sorted results with the table view
        sortedBooks.comparatorProperty().bind(collection_info.comparatorProperty());

        // Applying filtered and sorted information to the table view
        collection_info.setItems(sortedBooks);

        // Credit to: tookootek@YouTube: https://www.youtube.com/watch?v=2M0L6w3tMOY
    }


    /**
     *
     * @param file
     */
    private void SaveFile(File file){

        List<String[]> lineInArray = new ArrayList<>();
        String[] book;
        String content = null;
        String bookCollection = null;

        try (CSVReader reader = new CSVReader(new FileReader("books.csv"))) {
            while ((book = reader.readNext()) != null) {
                lineInArray.add(book);
            }
        } catch (FileNotFoundException e) { e.printStackTrace(); }
        catch (IOException | CsvValidationException e) { e.printStackTrace(); }

        for (String[] singleBook: lineInArray){
            for (int i=0; i < singleBook.length; i++){
                content += singleBook[i] + ",";
            }
            bookCollection += content + "\n";
            content = "";
        }

        FileWriter fileWriter;

        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(bookCollection);
            fileWriter.close();
        } catch (IOException ex) {
            //TODO
        }
    }


    /**
     *
     * @throws IOException
     */
    public void updateCSV() throws IOException {


        CSVWriter writer = null;


        try {
            // Creating CSVWriter with FileWriter object with setting to overwrite any existing copy
            writer = new CSVWriter(new FileWriter("books.csv", false),
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.RFC4180_LINE_END);

            for (String[] book : books) {

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
                else if (book[0] == null) { System.out.println("issue");}
                else {
                    writer.writeNext(
                            new String[] { book[0], book[1], book[2], book[3], book[4], book[5]});
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        // Using finally to close writer whether write is failing or not
        finally { writer.close(); }
    }


    /**
     *
     * @return
     */
    public void loadCSV() {

        final ObservableList<Record> books
                = FXCollections.observableArrayList();

        String CsvFile = "books.csv";
        String FieldDelimiter = ",";

        BufferedReader br;

        Record record = null;
        Boolean bookIsEmpty = true;

        try {

            br = new BufferedReader(new FileReader(CsvFile));

            String line;

            if ((br.readLine()) == null) {
                JOptionPane
                    .showMessageDialog(null, "There are no books in your collection!!");
            }
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

        if (bookIsEmpty == false) {
        clmnTitle.setCellValueFactory(new PropertyValueFactory<Record, String>("title"));
        clmnAuthor.setCellValueFactory(new PropertyValueFactory<Record, String>("authors"));
        clmnPublisher.setCellValueFactory(new PropertyValueFactory<Record, String>("publisher"));
        clmnDate.setCellValueFactory(new PropertyValueFactory<Record, String>("publishedDate"));
        clmnPages.setCellValueFactory(new PropertyValueFactory<Record, String>("pages"));
        clmnLanguage.setCellValueFactory(new PropertyValueFactory<Record, String>("language"));

        collection_info.setItems(books);

        booksToLook = books;
        }
    }

    //Method to tests whether a Record matches the search text




    /**
     * Method to loop through a list of Records and performs this test on each Record
     * : List of Records that match for criteria
     * : The text field that contains the filter
     * @return: Returns observableList with Records that match for the search
     */


}
