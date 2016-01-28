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
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

/**
 * A column for a TreeTable that has a filter text field on top that allows filtering of its content.
 *
 * @see TableColumn
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
  }

  public StringProperty filterTextProperty() {
    return filterField.textProperty();
  }

  public String getFilterText() {
    return filterField.getText();
  }
}
