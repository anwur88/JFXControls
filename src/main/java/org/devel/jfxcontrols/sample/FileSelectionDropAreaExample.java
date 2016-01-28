/*
 * This document contains trade secret data which is the property of
 * IAV GmbH. Information contained herein may not be used,
 * copied or disclosed in whole or part except as permitted by written
 * agreement from IAV GmbH.
 *
 * Copyright (C) IAV GmbH / Gifhorn / Germany
 */
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
