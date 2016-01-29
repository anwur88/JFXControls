/*
 * This document contains trade secret data which is the property of
 * IAV GmbH. Information contained herein may not be used,
 * copied or disclosed in whole or part except as permitted by written
 * agreement from IAV GmbH.
 *
 * Copyright (C) IAV GmbH / Gifhorn / Germany
 */
package org.devel.jfxcontrols.scene.control.skin;

import com.google.common.io.Files;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Tooltip;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.devel.jfxcontrols.scene.control.FileSelectionDropArea;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * A skin for {@link FileSelectionDropArea} using a file chooser as alternative file selection method.
 */
public class FileSelectionDropAreaSkin extends SkinBase<FileSelectionDropArea> {

  private final List<Predicate<Path>> validators;

  public FileSelectionDropAreaSkin(final FileSelectionDropArea control) {
    super(control);
    validators = control.getValidators();
    initialize();
  }

  private void initialize() {
    final Button searchButton = new Button();
    final Label unselectedFileName = new Label();
    unselectedFileName.setGraphic(getFileGlyph());
    final Parent selectedFileName = createSelectedFileNameLabel();
    final VBox root = new VBox(
        getSkinnable().getSelectedFile() == null ? unselectedFileName : selectedFileName,
        searchButton);
    root.setId("root-container");
    root.setAlignment(Pos.CENTER);
    root.setSpacing(10);
    getChildren().add(root);

    root.prefWidthProperty().bind(getSkinnable().prefWidthProperty());
    root.prefHeightProperty().bind(getSkinnable().prefHeightProperty());
    root.paddingProperty().bind(getSkinnable().paddingProperty());
    getSkinnable().setOnDragOver(this::highlight);
    getSkinnable().setOnDragExited(this::reset);
    getSkinnable().setOnDragDropped(this::selectFile);

    searchButton.textProperty().bind(getSkinnable().buttonTextProperty());
    searchButton.setOnAction(this::openSelectionDialog);

    getSkinnable().selectedFileProperty().addListener(
        new ChangeListener<Path>() {
          private boolean mustSwitchToSelectedFileName = getSkinnable().getSelectedFile() == null;

          @Override
          public void changed(final ObservableValue<? extends Path> observable, final Path oldValue,
                              final Path newValue) {
            if (newValue == null) {
              root.getChildren().remove(selectedFileName);
              root.getChildren().add(0, unselectedFileName);
              mustSwitchToSelectedFileName = true;
            } else if (mustSwitchToSelectedFileName) {
              root.getChildren().remove(unselectedFileName);
              root.getChildren().add(0, selectedFileName);
              mustSwitchToSelectedFileName = false;
            }
          }
        });
  }

  private Parent createSelectedFileNameLabel() {
    final Label selectedFileName = new Label();
    selectedFileName.setId("selected-file-name");
    selectedFileName.setMaxHeight(40);
    selectedFileName.setWrapText(true);
    selectedFileName.setTextOverrun(OverrunStyle.CENTER_ELLIPSIS);

    if (getSkinnable().isDirectorySelection()) {
      //in case we deal with directories the label should show the whole path as the directory name is not enough
      selectedFileName.textProperty().bind(getSkinnable().selectedFileProperty().asString());
    } else {
      selectedFileName.textProperty().bind(getSkinnable().selectedFileNameProperty());
    }
    initFileErrorHighlighting(selectedFileName);

    final HBox selectedFileNameContainer = new HBox(selectedFileName);
    selectedFileNameContainer.setPadding(new Insets(0, 10, 0, 10));
    selectedFileNameContainer.setAlignment(Pos.CENTER);
    selectedFileNameContainer.setPrefWidth(20);

    return selectedFileNameContainer;
  }

  private void initFileErrorHighlighting(final Label selectedFileName) {
    getSkinnable().fileErrorProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        selectedFileName.getStyleClass().add("file-selection-drop-area-label-error");
        final Tooltip tooltip = new Tooltip("");
        tooltip.setId("file-selection-drop-area-tooltip");
        tooltip.setWrapText(true);
        tooltip.setMaxWidth(400);
        tooltip.textProperty().bind(getSkinnable().fileErrorTooltipTextProperty());
        selectedFileName.setTooltip(tooltip);
      } else {
        selectedFileName.getStyleClass().remove("file-selection-drop-area-label-error");
        selectedFileName.setTooltip(null);
      }
    });
  }

  private Node getFileGlyph() {
    final FontAwesomeIconView fileGlyph = new FontAwesomeIconView(FontAwesomeIcon.FILE);
    fileGlyph.setId("fileGlyph");
    /* TODO: define size of glyph via css */
    fileGlyph.setGlyphSize(30.0);
    return fileGlyph;
  }

  private void openSelectionDialog(final ActionEvent event) {
    if (getSkinnable().isDirectorySelection()) {
      openDirectorySelectionDialog();
    } else {
      openFileSelectionDialog();
    }
    event.consume();
  }

  private void openDirectorySelectionDialog() {
    final DirectoryChooser dirChooser = new DirectoryChooser();
    dirChooser.setTitle(getSkinnable().getDialogTitle());
    dirChooser.setInitialDirectory(computeCachedDirectory(getSkinnable().getCachedDirectory()).toFile());
    Optional.ofNullable(dirChooser.showDialog(getSkinnable().getScene().getWindow())).ifPresent(
        (selectedFile) -> {
          if (fileIsValid(selectedFile.toPath())) {
            getSkinnable().setSelectedFile(selectedFile.toPath());
          }
        });
  }

  private void openFileSelectionDialog() {
    final FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(getSkinnable().getDialogTitle());
    fileChooser.getExtensionFilters().addAll(getSkinnable().getExtensionFilters());
    fileChooser.setInitialDirectory(computeCachedDirectory(getSkinnable().getCachedDirectory()).toFile());
    Optional.ofNullable(fileChooser.showOpenDialog(getSkinnable().getScene().getWindow())).ifPresent(
        (selectedFile) -> {
          if (fileIsValid(selectedFile.toPath())) {
            getSkinnable().setSelectedFile(selectedFile.toPath());
          }
        });
  }

  private boolean fileIsValid(final Path selectedFile) {
    return validators.stream().allMatch(p -> p.test(selectedFile));
  }

  private void highlight(final DragEvent event) {
    if (isValidSelection(event.getDragboard())) {
      event.acceptTransferModes(TransferMode.LINK);
      getSkinnable().setHighlighted(true);
    }
    event.consume();
  }

  private void reset(final DragEvent event) {
    getSkinnable().setHighlighted(false);
    event.consume();
  }

  private boolean isValidSelection(final Dragboard db) {
    return db.getFiles().size() == 1 && isValidExtension(db.getFiles().get(0).toPath()) && fileIsValid(
        db.getFiles().get(0).toPath());
  }

  private boolean isValidExtension(final Path file) {
    if (getSkinnable().isDirectorySelection()) {
      return java.nio.file.Files.isDirectory(file);
    }
    return java.nio.file.Files.isRegularFile(file) && getSkinnable().getExtensionFilters().stream().anyMatch(
        extensionFilter -> extensionFilter.getExtensions().stream().anyMatch(extension -> Files.getFileExtension(
            file.toString()).equals(extension.substring(2))));
  }

  private void selectFile(final DragEvent event) {
    final Dragboard db = event.getDragboard();
    boolean success = false;
    if (isValidSelection(db)) {
      success = true;
      getSkinnable().setSelectedFile(db.getFiles().get(0).toPath());
    }
    event.setDropCompleted(success);
    event.consume();
  }

  private Path computeCachedDirectory(final Path result) {
    return java.nio.file.Files.exists(result) ? result : computeCachedDirectory(result.getParent());
  }
}
