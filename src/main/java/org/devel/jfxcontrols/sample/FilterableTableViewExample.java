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

public class FilterableTableViewExample {

  @FXML
  public FilterableTableColumn<Person, String> ftcColumnX;
  @FXML
  public FilterableTableColumn<Person, String> ftcColumnY;
  @FXML
  public FilterableTableColumn<Person, String> ftcColumnZ;
  @FXML
  private FilterableTableView<Person> fttvTable;

  @FXML
  private void initialize() {
    ftcColumnX.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
    ftcColumnY.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getAge()));
    ftcColumnZ.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPlace()));

    fttvTable.setItems(FXCollections.observableArrayList(
        new Person("stefan", "33", "dresden"),
        new Person("henk", "35", "ebenheit"),
        new Person("karin", "60", "ebenheit")));
  }

  public class Person {
    private final String name;
    private final String age;
    private final String place;

    public Person(final String name, final String age, final String place) {
      this.name = name;
      this.age = age;
      this.place = place;
    }

    public String getName() {
      return name;
    }

    public String getAge() {
      return age;
    }

    public String getPlace() {
      return place;
    }
  }
}
