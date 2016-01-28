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
import javafx.scene.control.ComboBox;
import org.devel.jfxcontrols.scene.control.FilterableTableColumn;
import org.devel.jfxcontrols.scene.control.FilterableTableView;
import org.devel.jfxcontrols.scene.control.FilterableTableView.PredicateJoinPolicies;

public class FilterableTableViewExample {

  @FXML
  public FilterableTableColumn<Person, String> ftcColumnX;
  @FXML
  public FilterableTableColumn<Person, String> ftcColumnY;
  @FXML
  public FilterableTableColumn<Person, String> ftcColumnZ;
  @FXML
  public ComboBox<String> cbPredicateJoinPolicy;
  @FXML
  private FilterableTableView<Person> fttvTable;

  @FXML
  private void initialize() {
    ftcColumnX.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
    ftcColumnY.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getAge()));
    ftcColumnZ.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPlace()));

    fttvTable.addFilterPredicate((Person s) ->
            // TODO Move empty evaluation into table column, because this is default behavior!
            ftcColumnX.getFilterText().trim().isEmpty()
                || ftcColumnX.getCellData(s).contains(ftcColumnX.getFilterText().trim()),
        ftcColumnX.filterTextProperty());
    fttvTable.addFilterPredicate((Person s) ->
            ftcColumnX.getFilterText().trim().isEmpty()
                || ftcColumnY.getCellData(s).contains(ftcColumnY.getFilterText().trim()),
        ftcColumnY.filterTextProperty());
    fttvTable.addFilterPredicate((Person s) ->
            ftcColumnX.getFilterText().trim().isEmpty()
                || ftcColumnZ.getCellData(s).contains(ftcColumnZ.getFilterText().trim()),
        ftcColumnZ.filterTextProperty());

    fttvTable.setItems(FXCollections.observableArrayList(
        new Person("stefan", "33", "dresden"),
        new Person("henk", "35", "ebenheit"),
        new Person("karin", "60", "ebenheit")));

    cbPredicateJoinPolicy.setItems(FXCollections.observableArrayList(
        PredicateJoinPolicies.ALL_MATCH.toString(),
        PredicateJoinPolicies.ANY_MATCH.toString()));
    cbPredicateJoinPolicy.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) ->
        fttvTable.setPredicateJoinPolicy(PredicateJoinPolicies.valueOf(newValue))));
    cbPredicateJoinPolicy.getSelectionModel().select(0);
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
