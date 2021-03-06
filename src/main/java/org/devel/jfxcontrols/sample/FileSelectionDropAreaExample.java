package org.devel.jfxcontrols.sample;

import javafx.fxml.FXML;
import javafx.stage.FileChooser.ExtensionFilter;
import org.devel.jfxcontrols.scene.control.FileSelectionDropArea;

public class FileSelectionDropAreaExample {

  @FXML
  public FileSelectionDropArea fdaZipFile;
  @FXML
  public FileSelectionDropArea fdaDirectory;

  @FXML
  public void initialize() {
    fdaZipFile.getExtensionFilters().addAll(new ExtensionFilter("Archives", "*.zip", "*.7z", "*.zip64"));
  }
}
