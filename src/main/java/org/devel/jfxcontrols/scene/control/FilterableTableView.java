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
import javafx.scene.control.TableView;

public class FilterableTableView<S> extends TableView<S> {

  public FilterableTableView() {
    this(FXCollections.<S>observableArrayList());
  }

  public FilterableTableView(ObservableList<S> items) {
    super(items);
  }

  @Override
  public String getUserAgentStylesheet() {
    return getClass().getResource("/org/devel/jfxcontrols/scene/control/"
        + getClass().getSimpleName() + ".css").toExternalForm();
  }
}
