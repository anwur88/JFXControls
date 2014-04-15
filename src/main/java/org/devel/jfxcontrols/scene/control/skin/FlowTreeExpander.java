/**
 * 
 */
package org.devel.jfxcontrols.scene.control.skin;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import org.devel.jfxcontrols.scene.control.skin.animation.SingularExpander;

/**
 * @author stefan.illgen
 *
 */
@SuppressWarnings({
	"unchecked", "rawtypes"
})
public class FlowTreeExpander<M, I extends IndexedCell<M>> extends FlowExtension<M, I> {

	private TreeItem<M> expandedItem;
	private ObjectProperty<TreeItem<M>> root;
	private SimpleBooleanProperty singular;
	private SingularExpander<M, I> expander;
	private SimpleObjectProperty<FlowAdjuster<M, I>> adjuster;
	private boolean dragging;
	private TreeItemHolder treeItemHolder;

	public FlowTreeExpander(ExtensibleFlow<M, I> extensibleFlow,
		TreeItemHolder treeItemHolder,
		FlowAdjuster<M, I> adjuster) {

		super(extensibleFlow);

		this.treeItemHolder = treeItemHolder;

		rootProperty().addListener(new ChangeListener<TreeItem<M>>() {

			@Override
			public void changed(ObservableValue<? extends TreeItem<M>> observable,
								TreeItem<M> oldValue,
								TreeItem<M> newValue) {
				setRowCount(countExpandedItems(newValue) - 1);
			}
		});

		initEventHandling();

		setAdjuster(adjuster);
		fixedCellSizeProperty().bindBidirectional(adjuster.fixedCellSizeProperty());
		adjuster.rowCountProperty().bindBidirectional(rowCountProperty());
	}

	private double computeExpansionSpace(TreeItem<M> treeItem) {
		if (treeItem.isExpanded()) {
			return getFixedCellSize() * (treeItem.getChildren().size() + 1);
		} else {
			return getFixedCellSize();
		}
	}

	private int countExpandedItems(TreeItem<M> treeItem) {
		int numExpandedItems = 0;
		if (treeItem != null) {
			numExpandedItems = 1;
			if (!treeItem.isLeaf() && treeItem.isExpanded()) {
				// expandedTreeItem = treeItem;
				ObservableList<TreeItem<M>> children = treeItem.getChildren();
				for (TreeItem<M> child : children) {
					numExpandedItems += countExpandedItems(child);
				}
			}
		}
		return numExpandedItems;
	}

	public TreeItem<M> getRoot() {
		return rootProperty().get();
	}

	public void setRoot(TreeItem<M> root) {
		rootProperty().set(root);
	}

	public ObjectProperty<TreeItem<M>> rootProperty() {
		if (root == null) {
			root = new SimpleObjectProperty<>();
		}
		return root;
	}

	public BooleanProperty singularProperty() {
		if (singular == null)
			singular = new SimpleBooleanProperty(true);
		return singular;
	}

	public boolean isSingular() {
		return singularProperty().get();
	}

	public void setSingular(boolean flingable) {
		singularProperty().set(flingable);
	}

	public SingularExpander<M, I> getExpander() {
		if (expander == null)
			expander = new SingularExpander<>();
		return expander;
	}

	public void setExpander(SingularExpander<M, I> expander) {
		this.expander = expander;
	}

	@Override
	public Map<EventType, EventHandler> createTypedEventHandlers() {
		return new HashMap<EventType, EventHandler>() {
			{

			}
		};
	}

	@Override
	public Map<EventType, EventHandler> createTypedEventFilters() {
		return new HashMap<EventType, EventHandler>() {
			private static final long serialVersionUID = 6803899089411940673L;
			{
				put(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {
						setDragging(true);
					}

				});

				put(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {

						if (getAdjuster().getYVelocity() == 0) {
							setDragging(false);
						}

						if (isDragging()) {
							return;
						}

						if (event.getClickCount() == 1) {

							I cell = (I) event.getSource();
							TreeItem<M> treeItem = treeItemHolder.getTreeItem(cell);

							if (treeItem.getChildren().size() != 0) {

								if (expandedItem != null
									&& !expandedItem.equals(treeItem)) {
									expandedItem.setExpanded(false);
									expandedItem = null;
								}

								treeItem.setExpanded(!treeItem.isExpanded());
								setRowCount(countExpandedItems(getRoot()) - 1);

								if (treeItem.isExpanded()) {
									expandedItem = treeItem;
									// getAdjuster().adjustDiff(computeDiff2FirstVisibleCell(cell),
									// true);
									// getExtensibleFlow().showAsFirst(cell);
								} else {
									expandedItem = null;
									// getAdjuster().adjustDiff(computeDiff2EntireRow(),
									// true);
								}
							}
							event.consume();
						}
					}
				});

				put(MouseEvent.MOUSE_MOVED, (event) -> {
					event.consume();
				});
				put(MouseEvent.MOUSE_ENTERED, (event) -> {
					event.consume();
				});
				put(MouseEvent.MOUSE_EXITED, (event) -> {
					event.consume();
				});
				put(MouseEvent.MOUSE_ENTERED_TARGET, (event) -> {
					event.consume();
				});
				put(MouseEvent.MOUSE_EXITED_TARGET, (event) -> {
					event.consume();
				});
				put(ScrollEvent.ANY, (event) -> {
					event.consume();
				});
				put(KeyEvent.ANY, (event) -> {
					event.consume();
				});
			}
		};
	}

	public ObjectProperty<FlowAdjuster<M, I>> adjusterProperty() {
		if (adjuster == null)
			adjuster = new SimpleObjectProperty<FlowAdjuster<M, I>>();
		return adjuster;
	}

	public FlowAdjuster<M, I> getAdjuster() {
		return adjusterProperty().get();
	}

	public void setAdjuster(FlowAdjuster<M, I> adjuster) {
		adjusterProperty().set(adjuster);
	}

	private boolean isDragging() {
		return dragging;
	}

	private void setDragging(boolean b) {
		dragging = b;
	}

}
