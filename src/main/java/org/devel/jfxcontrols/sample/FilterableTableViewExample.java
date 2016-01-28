/*
 * This document contains trade secret data which is the property of
 * IAV GmbH. Information contained herein may not be used,
 * copied or disclosed in whole or part except as permitted by written
 * agreement from IAV GmbH.
 *
 * Copyright (C) IAV GmbH / Gifhorn / Germany
 */
package org.devel.jfxcontrols.sample;

import com.google.common.base.MoreObjects;
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

    fttvTable.addFilterPredicate((Person s) ->
        ftcColumnX.getCellData(s).contains(ftcColumnX.getFilterText()), ftcColumnX.filterTextProperty());
    fttvTable.addFilterPredicate((Person s) ->
        ftcColumnY.getCellData(s).contains(ftcColumnY.getFilterText()), ftcColumnY.filterTextProperty());
    fttvTable.addFilterPredicate((Person s) ->
        ftcColumnZ.getCellData(s).contains(ftcColumnZ.getFilterText()), ftcColumnZ.filterTextProperty());

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

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(this)
          .add("name", name)
          .add("age", age)
          .add("place", place)
          .toString();
    }
  }
}
