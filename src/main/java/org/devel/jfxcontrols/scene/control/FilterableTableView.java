/*
 * This document contains trade secret data which is the property of
 * IAV GmbH. Information contained herein may not be used,
 * copied or disclosed in whole or part except as permitted by written
 * agreement from IAV GmbH.
 *
 * Copyright (C) IAV GmbH / Gifhorn / Germany
 */
package org.devel.jfxcontrols.scene.control;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TableView;

import java.util.function.Predicate;

public class FilterableTableView<S> extends TableView<S> {

  private final ObservableList<ObjectBinding<Predicate<S>>> filterPredicates = FXCollections.observableArrayList();
  private final Predicate<S> filterPredicate = s -> true;

  private FilteredList<S> filteredItems;

  public FilterableTableView() {
    this(FXCollections.<S>observableArrayList());
  }

  public FilterableTableView(final ObservableList<S> items) {
    super(items);

    itemsProperty().addListener(((itemsValue, oldItems, newItems) -> {
      if (newItems != null) {
        filteredItems = newItems.filtered(filterPredicate);
        filteredItems.predicateProperty().bind(Bindings.createObjectBinding(() -> s -> filterPredicates.stream()
                .map(ObjectBinding::get)
                .allMatch(p -> p.test(s)),
            filterPredicates.toArray(new ObjectBinding[filterPredicates.size()])));
        setItems(filteredItems);
      } else {
        filteredItems = null;
      }
    }));
  }

  public FilteredList<S> getFilteredItems() {
    return filteredItems;
  }

  public void addFilterPredicate(final Predicate<S> predicate, final ReadOnlyStringProperty textProperty) {
    filterPredicates.add(Bindings.createObjectBinding(() ->
            ((Predicate<S>) s -> textProperty.get().trim().isEmpty()).or(predicate),
        textProperty));
  }

  @Override
  public String getUserAgentStylesheet() {
    return getClass().getResource("/org/devel/jfxcontrols/scene/control/"
        + getClass().getSimpleName() + ".css").toExternalForm();
  }
}
