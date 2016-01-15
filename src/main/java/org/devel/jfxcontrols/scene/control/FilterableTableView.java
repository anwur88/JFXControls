/*
 * This document contains trade secret data which is the property of
 * IAV GmbH. Information contained herein may not be used,
 * copied or disclosed in whole or part except as permitted by written
 * agreement from IAV GmbH.
 *
 * Copyright (C) IAV GmbH / Gifhorn / Germany
 */
package org.devel.jfxcontrols.scene.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TableView;

public class FilterableTableView<S> extends TableView<S> {

  // filtered list are unmodifiable
  private final FilteredList<S> filteredItems = getItems().filtered(s -> true);

  public FilterableTableView() {
    this(FXCollections.<S>observableArrayList());
  }

  public FilterableTableView(final ObservableList<S> items) {
    super(items);
  }

  public FilteredList<S> getFilteredItems() {
    return filteredItems;
  }

  @Override
  public String getUserAgentStylesheet() {
    return getClass().getResource("/org/devel/jfxcontrols/scene/control/"
        + getClass().getSimpleName() + ".css").toExternalForm();
  }
}
