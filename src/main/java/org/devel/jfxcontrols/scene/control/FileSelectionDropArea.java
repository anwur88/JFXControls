/*
 * This document contains trade secret data which is the property of
 * IAV GmbH. Information contained herein may not be used,
 * copied or disclosed in whole or part except as permitted by written
 * agreement from IAV GmbH.
 *
 * Copyright (C) IAV GmbH / Gifhorn / Germany
 */
package org.devel.jfxcontrols.scene.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.stage.FileChooser.ExtensionFilter;
import org.devel.jfxcontrols.scene.control.skin.FileSelectionDropAreaSkin;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import static javafx.beans.binding.Bindings.createObjectBinding;
import static javafx.beans.binding.Bindings.createStringBinding;

/**
 * A control for dropping files for selection providing an alternative way to trigger the selection using a button.
 */
public class FileSelectionDropArea extends Control {

  public static final String INITIAL_DIRECTORY = "user.home";
  public static final String DEFAULT_SEARCH_BUTTON_TEXT = "Search";
  public static final String DEFAULT_FILE_DIALOG_TITLE = "Open";
  private static final String DEFAULT_STYLE_CLASS = "file-selection-drop-area";
  private static final PseudoClass PSEUDO_CLASS_HIGHLIGHTED = PseudoClass.getPseudoClass("highlighted");

  private final BooleanProperty directorySelection = new SimpleBooleanProperty();

  private final ObjectProperty<Path> selectedFile = new SimpleObjectProperty<>();
  private final ReadOnlyObjectWrapper<Path> cachedDirectory = new ReadOnlyObjectWrapper<>();
  private final ReadOnlyStringWrapper selectedFileName = new ReadOnlyStringWrapper();

  private final StringProperty buttonText = new SimpleStringProperty(DEFAULT_SEARCH_BUTTON_TEXT);
  private final StringProperty dialogTitle = new SimpleStringProperty(DEFAULT_FILE_DIALOG_TITLE);

  private final BooleanProperty fileError = new SimpleBooleanProperty();

  private final ObservableList<ExtensionFilter> extensionFilters = FXCollections.<ExtensionFilter>observableArrayList();

  private final List<Predicate<Path>> validators = new LinkedList<>();
  /**
   * Indicates whether this drop area is highlighted. This can be manipulated programmatically.
   */
  private BooleanProperty highlighted;
  private final StringProperty fileErrorTooltipText = new SimpleStringProperty("");

  public FileSelectionDropArea() {
    initialize();
  }

  private void initialize() {
    getStyleClass().setAll(DEFAULT_STYLE_CLASS);
    cachedDirectory.bind(createObjectBinding(() ->
        selectedFile.get() == null || selectedFile.get().getParent() == null || !Files.exists(
            selectedFile.get().getParent())
            ? Paths.get(System.getProperty(INITIAL_DIRECTORY))
            : selectedFile.get().getParent(), selectedFile));
    selectedFileName.bind(
        createStringBinding(() -> selectedFile.get() == null ? null : selectedFile.get().getFileName().toString(),
            selectedFile));
  }

  public ReadOnlyStringProperty selectedFileNameProperty() {
    return selectedFileName.getReadOnlyProperty();
  }

  public String getSelectedFileName() {
    return selectedFileName.get();
  }

  public StringProperty buttonTextProperty() {
    return buttonText;
  }

  public String getButtonText() {
    return buttonText.get();
  }

  public void setButtonText(final String buttonText) {
    this.buttonText.set(buttonText);
  }

  public ReadOnlyObjectProperty<Path> cachedDirectoryProperty() {
    return cachedDirectory.getReadOnlyProperty();
  }

  public Path getCachedDirectory() {
    return cachedDirectory.get();
  }

  public ObjectProperty<Path> selectedFileProperty() {
    return selectedFile;
  }

  public Path getSelectedFile() {
    return selectedFile.get();
  }

  public void setSelectedFile(final Path selectedFile) {
    // TE-708: set selected file to null when same file is loaded to trigger automatic data format determination
    if (selectedFile != null && selectedFile.equals(this.selectedFile.get())) {
      this.selectedFile.set(null);
    }
    this.selectedFile.set(selectedFile);
  }

  public StringProperty dialogTitleProperty() {
    return dialogTitle;
  }

  public String getDialogTitle() {
    return dialogTitle.get();
  }

  public void setDialogTitle(final String dialogTitle) {
    this.dialogTitle.set(dialogTitle);
  }

  public boolean isFileError() {
    return fileError.get();
  }

  public BooleanProperty fileErrorProperty() {
    return fileError;
  }

  public void setFileError(final boolean fileError) {
    this.fileError.set(fileError);
  }

  public ObservableList<ExtensionFilter> getExtensionFilters() {
    return extensionFilters;
  }

  public String getFileErrorTooltipText() {
    return fileErrorTooltipText.get();
  }

  public StringProperty fileErrorTooltipTextProperty() {
    return fileErrorTooltipText;
  }

  public void setFileErrorTooltipText(final String fileErrorTooltipText) {
    this.fileErrorTooltipText.set(fileErrorTooltipText);
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new FileSelectionDropAreaSkin(this);
  }

  /**
   * Adds a new validator that checks whether a choosen file is a valid.
   *
   * @param validator predicate to check the file
   */
  public void addValidator(final Predicate<Path> validator) {
    validators.add(validator);
  }

  /**
   * All validators that are checked pior to accept a selected file.
   *
   * @return file validators
   */
  public List<Predicate<Path>> getValidators() {
    return validators;
  }

  public final boolean isHighlighted() {
    return highlighted != null && highlighted.get();
  }

  public void setHighlighted(final boolean highlighted) {
    highlightedProperty().set(highlighted);
  }

  public final BooleanProperty highlightedProperty() {
    if (highlighted == null) {
      highlighted = new BooleanPropertyBase() {

        @Override
        protected void invalidated() {
          pseudoClassStateChanged(PSEUDO_CLASS_HIGHLIGHTED, get());
        }

        @Override
        public Object getBean() {
          return FileSelectionDropArea.this;
        }

        @Override
        public String getName() {
          return "highlighted";
        }
      };
    }
    return highlighted;
  }

  public boolean isDirectorySelection() {
    return directorySelection.get();
  }

  public BooleanProperty directorySelectionProperty() {
    return directorySelection;
  }

  public void setDirectorySelection(final boolean directorySelection) {
    this.directorySelection.set(directorySelection);
  }

  @Override
  public String getUserAgentStylesheet() {
    return getClass().getResource("/org/devel/jfxcontrols/scene/control/"
        + getClass().getSimpleName() + ".css").toExternalForm();
  }
}
