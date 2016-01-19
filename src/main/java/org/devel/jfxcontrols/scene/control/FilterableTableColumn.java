/*
 * This document contains trade secret data which is the property of
 * IAV GmbH. Information contained herein may not be used,
 * copied or disclosed in whole or part except as permitted by written
 * agreement from IAV GmbH.
 *
 * Copyright (C) IAV GmbH / Gifhorn / Germany
 */
package org.devel.jfxcontrols.scene.control;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

/**
 * A column for a TreeTable that has a filter text field on top that allows filtering of its content.
 *
 * @param <S>
 * @param <T>
 */
public class FilterableTableColumn<S, T> extends TableColumn<S, T> {

  public static final int TOP_RIGHT_BOTTOM_LEFT = 5;

  private final TextField filterField;

  public FilterableTableColumn() {
    getStyleClass().add("sx-filterable-table-column");

    filterField = new FilterTextField();
    final StackPane container = new StackPane(filterField);
    container.setPadding(new Insets(TOP_RIGHT_BOTTOM_LEFT));
    setGraphic(container);
    setSortable(false);
    final DoubleBinding width = widthProperty().subtract(TOP_RIGHT_BOTTOM_LEFT * 2);
    container.minWidthProperty().bind(width);
    container.prefWidthProperty().bind(width);
    container.maxWidthProperty().bind(width);
    filterField.minWidthProperty().bind(width);
    filterField.prefWidthProperty().bind(width);
    filterField.maxWidthProperty().bind(width);

    tableViewProperty().addListener(new ChangeListener<TableView<S>>() {
      @Override
      public void changed(ObservableValue<? extends TableView<S>> observable, TableView<S> oldValue, TableView<S> newValue) {
        if (newValue != null) {
          tableViewProperty().removeListener(this);
          // TODO Must make assumptions on table used? Better define the predicate outside of column?
          final FilterableTableView<S> tableView = (FilterableTableView) getTableView();
          tableView.addFilter(filterTextProperty());
        }
      }
    });


  }

  public StringProperty filterTextProperty() {
    return filterField.textProperty();
  }

  public TextField getFilterField() {
    return filterField;
  }


}
