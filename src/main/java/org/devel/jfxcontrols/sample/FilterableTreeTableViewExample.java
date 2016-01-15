/*
 * This document contains trade secret data which is the property of
 * IAV GmbH. Information contained herein may not be used,
 * copied or disclosed in whole or part except as permitted by written
 * agreement from IAV GmbH.
 *
 * Copyright (C) IAV GmbH / Gifhorn / Germany
 */
package org.devel.jfxcontrols.sample;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import org.devel.jfxcontrols.scene.control.FilterableTableColumn;
import org.devel.jfxcontrols.scene.control.FilterableTableView;

public class FilterableTreeTableViewExample {

  @FXML
  public FilterableTableColumn<String, String> ftcColumnX;
  @FXML
  public FilterableTableColumn<String, String> ftcColumnY;
  @FXML
  public FilterableTableColumn<String, String> ftcColumnZ;
  @FXML
  private FilterableTableView<String> fttvTable;

  @FXML
  private void initialize() {
    ftcColumnX.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()));
    ftcColumnY.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()));
    ftcColumnZ.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()));

    fttvTable.setItems(FXCollections.observableArrayList("eins", "zwei", "drei"));
  }

}
