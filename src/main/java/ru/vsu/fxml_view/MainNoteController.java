package ru.vsu.fxml_view;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.vsu.FXEngine;
import ru.vsu.jpa.domain.UserNote;
import ru.vsu.services.note.UserNoteService;
import ru.vsu.utils.TosterKeeperUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ivan Rovenskiy
 * 01 March 2020
 */
@Component
@FxmlView("main_note_screen.fxml")
public class MainNoteController {
    @FXML
    private TextArea statusArea;
    @FXML
    private TableView<UserNote> noteTable;
    @FXML
    private TableColumn<UserNote, String> userNameColumn;
    @FXML
    private TableColumn<UserNote, Date> noteCreationDateColumn;
    @FXML
    private TableColumn<UserNote, String> noteTitleColumn;
    @FXML
    private TextArea noteTitleArea;
    @FXML
    private TextArea noteBodyArea;

    @FXML
    void initialize() {
        initTable();
    }

    private final UserNoteService userNoteService;

    public MainNoteController(UserNoteService userNoteService) {
        this.userNoteService = userNoteService;
    }

    private void initTable() {
        updateTableItems();

        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        noteCreationDateColumn.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        noteTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        noteTable.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                noteTitleArea.setText(newValue.getTitle());
                noteBodyArea.setText(newValue.getBody());
            }
        }));
    }

    private void updateTableItems() {
        final List<UserNote> allNoteList = userNoteService.findAllAndDecryptUserNoteList(TosterKeeperUtils.getCurrentUserId());

        final List<UserNote> activeNoteList = allNoteList.stream()
                .filter(UserNote::isActive)
                .collect(Collectors.toList());

        noteTable.setItems(FXCollections.observableArrayList(activeNoteList));
    }

    @FXML
    void addNewNoteAction(ActionEvent event) {
        noteTable.getSelectionModel().clearSelection();
        appendStatusText("Please, enter title and body. Then click save button.");
    }

    @FXML
    void deleteSelectedNoteAction(ActionEvent event) {
        final UserNote selectedNote = noteTable.getSelectionModel().getSelectedItem();

        if (selectedNote == null) {
            appendStatusText("Please select note and then delete it.");
        } else {
            userNoteService.saveAndCryptUserNote(UserNote.deactivate(selectedNote), TosterKeeperUtils.getCurrentUserId());
        }

        updateTableItems();
    }

    @FXML
    void saveSelectedNoteAction(ActionEvent event) {
        final UserNote selectedNote = noteTable.getSelectionModel().getSelectedItem();

        if (selectedNote == null) {
            if (StringUtils.isBlank(noteTitleArea.getText())) {
                appendStatusText("Can't save note with empty title");
                return;
            }

            if (StringUtils.isBlank(noteBodyArea.getText())) {
                appendStatusText("Can't save note with empty body");
                return;
            }

            userNoteService.saveAndCryptUserNote(
                    UserNote.newNote(
                            TosterKeeperUtils.getCurrentUserId(),
                            new Date(),
                            noteTitleArea.getText(),
                            noteBodyArea.getText()),
                    TosterKeeperUtils.getCurrentUserId());

            appendStatusText("New note saved successfully");
        } else {
            userNoteService.saveAndCryptUserNote(
                    UserNote.fromExistWithUpdates(
                            selectedNote,
                            noteTitleArea.getText(),
                            noteBodyArea.getText()),
                    TosterKeeperUtils.getCurrentUserId());

            appendStatusText("Exist note saved successfully");
        }

        updateTableItems();
    }

    @FXML
    void goBackToMainScreenAction(ActionEvent event) {
        FXEngine.showStartStage();
    }

    private void appendStatusText(String message) {
        statusArea.appendText("\n");
        statusArea.appendText(message);
    }
}