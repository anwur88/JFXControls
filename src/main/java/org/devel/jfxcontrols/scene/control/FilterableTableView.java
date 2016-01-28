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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TableView;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class FilterableTableView<S> extends TableView<S> {

  public enum PredicateJoinPolicies {ALL_MATCH, ANY_MATCH}
  public static final PredicateJoinPolicies DEFAULT_PREDICATE_JOIN_POLICY = PredicateJoinPolicies.ALL_MATCH;

  private final ObjectProperty<PredicateJoinPolicies> predicateJoinPolicy =
      // TODO add pseudo class state
      new SimpleObjectProperty<>(DEFAULT_PREDICATE_JOIN_POLICY);

  private final ObservableList<ObjectBinding<Predicate<S>>> filterPredicates = FXCollections.observableArrayList();
  private final Predicate<S> filterPredicate = s -> true;

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
        final ObjectBinding<Predicate<? super S>> filterPredicatesBinding = Bindings.createObjectBinding(() -> s -> {
          final Stream<Predicate<S>> predicateStream = filterPredicates.stream()
              .map(ObjectBinding::get);
          final Predicate<Predicate<S>> predicate = p -> p.test(s);

          switch (getPredicateJoinPolicy()) {
            case ANY_MATCH:
              return predicateStream.anyMatch(predicate);
            case ALL_MATCH:
            default:
              return predicateStream.allMatch(predicate);
          }
        }, filterPredicates.toArray(new ObjectBinding[filterPredicates.size()]));
        filteredItems.predicateProperty().bind(filterPredicatesBinding);
        // TODO Is this the right way to let the predicate join policy influence the filtered items?
        predicateJoinPolicyProperty().addListener((observable, oldValue, newValue) ->
            filterPredicatesBinding.invalidate());

        setItems(filteredItems);
      } else {
        filteredItems = null;
      }
    }));
  }

  public final ObjectProperty<PredicateJoinPolicies> predicateJoinPolicyProperty() {
    return predicateJoinPolicy;
  }

  public final void setPredicateJoinPolicy(final PredicateJoinPolicies predicateJoinPolicy) {
    predicateJoinPolicyProperty().set(predicateJoinPolicy);
  }

  public final PredicateJoinPolicies getPredicateJoinPolicy() {
    return predicateJoinPolicy == null ? DEFAULT_PREDICATE_JOIN_POLICY : predicateJoinPolicy.get();
  }

  public FilteredList<S> getFilteredItems() {
    return filteredItems;
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
