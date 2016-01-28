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
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TableView;

import java.util.function.Predicate;

public class FilterableTableView<S> extends TableView<S> {

  private final ObservableList<StringProperty> filters = FXCollections.observableArrayList();
  private final ObservableList<ObjectBinding<Predicate<S>>> filterPredicates = FXCollections.observableArrayList();
  private final Predicate<S> filterPredicate = s -> true;

  private final ReadOnlyObjectWrapper<Predicate<S>> predicate = new ReadOnlyObjectWrapper<>(filterPredicate);
  // filtered list is already unmodifiable
  private FilteredList<S> filteredItems;

  public FilterableTableView() {
    this(FXCollections.<S>observableArrayList());
  }

  public FilterableTableView(final ObservableList<S> items) {
    super(items);

    // replace item list by filtered item list every time item list is reset
    itemsProperty().addListener(((itemsValue, oldItems, newItems) -> {
      if (newItems != null) {
        filteredItems = newItems.filtered(filterPredicate);
        // rebind predicates (AND)
        filteredItems.predicateProperty().bind(Bindings.createObjectBinding(() -> s -> {
          final long result = filterPredicates.stream()
              .filter(predicateProperty -> predicateProperty.get().test(s))
              .count();
          return result == filterPredicates.size();
        },
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

  public void addFilterPredicate(ObjectBinding<Predicate<S>> predicate) {
    filterPredicates.add(predicate);
  }

  public void addFilterPredicate(final Predicate<S> predicate, final ReadOnlyStringProperty property) {
    filterPredicates.add(Bindings.createObjectBinding(() -> predicate, property));
  }

  @Override
  public String getUserAgentStylesheet() {
    return getClass().getResource("/org/devel/jfxcontrols/scene/control/"
        + getClass().getSimpleName() + ".css").toExternalForm();
  }
}
