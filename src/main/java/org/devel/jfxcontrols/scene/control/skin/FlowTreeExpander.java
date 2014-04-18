package org.devel.jfxcontrols.scene.control.skin;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;

import org.devel.jfxcontrols.scene.control.skin.animation.SingularExpander;

/**
 * @author stefan.illgen
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class FlowTreeExpander<M, I extends IndexedCell<M>> extends
		FlowExtension<M, I> {

	// private TreeItem<M> expandedItem;
	private ObjectProperty<TreeItem<M>> root;
	private SimpleBooleanProperty singular;
	private SingularExpander<M, I> expander;

	private ObjectProperty<TreeItem<M>> expandedItem;
	private SimpleObjectProperty<FlowAdjuster<M, I>> adjuster;

	private boolean dragging;
	private TreeItemHolder treeItemHolder;

	public FlowTreeExpander(ExtendableFlow<M, I> extensibleFlow,
			TreeItemHolder treeItemHolder, FlowAdjuster<M, I> adjuster) {

		super(extensibleFlow);

		this.treeItemHolder = treeItemHolder;

		rowCountProperty().bind(new DoubleBinding() {
			{
				super.bind(rootProperty(), expandedItemProperty());
			}

			@Override
			protected double computeValue() {
				return (getRoot() == null) ? 0
						: countExpandedItems(getRoot()) - 1;
			}
		});

		initEventHandling();

		// setAdjuster(adjuster);
		// fixedCellSizeProperty().bindBidirectional(
		// adjuster.fixedCellSizeProperty());
		// adjuster.rowCountProperty().bindBidirectional(rowCountProperty());
		// selectionModelProperty().bind(adjuster.selectionModelProperty());
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

	// private double computeExpansionSpace(TreeItem<M> treeItem) {
	// if (treeItem.isExpanded()) {
	// return getFixedCellSize() * (treeItem.getChildren().size() + 1);
	// } else {
	// return getFixedCellSize();
	// }
	// }

	public TreeItem<M> getRoot() {
		return rootProperty().get();
	}

	public void setRoot(TreeItem<M> root) {
		rootProperty().set(root);
	}

	public ObjectProperty<TreeItem<M>> rootProperty() {
		if (root == null) {
			root = new SimpleObjectProperty<TreeItem<M>>();
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
				put(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {
						// event.consume();
					}
				});
				put(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {
						event.consume();
					}
				});

				put(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {

						// getSelectionModel().clearSelection();

						int selectedIndex = getSelectionModel()
								.getSelectedIndex();

						if (getAdjuster().isAdjusting()
								|| event.getClickCount() != 1) {
							return;
						}

						I cell = (I) event.getSource();
						final TreeItem<M> treeItem = treeItemHolder
								.getTreeItem(cell);
						cell.needsLayoutProperty().addListener(
								new InvalidationListener() {
									@Override
									public void invalidated(
											Observable observable) {
										observable.removeListener(this);
										System.out.println("aha");
										// if (treeItem.isExpanded())
										getAdjuster()
												.adjustDiff(
														computeCellPositionDiff(
																cell, treeItem,
																event), false);
									}
								});

						if (treeItem.getChildren().size() != 0) {
							treeItem.setExpanded(!treeItem.isExpanded());
							setExpandedItem(treeItem.isExpanded() ? treeItem
									: null);

							// event.consume();
						}
					}

					private double computeCellPositionDiff(I cell,
							TreeItem<M> treeItem, MouseEvent event) {

						double targetSize = (getExpandedItemCount() - 1)
								* getFixedCellSize();
						double visibleSize = getVisibleSize();
						// double cellPosition =
						// getExtensibleFlow().getPosition(
						// cell);
						// double cellIndex = cell.getIndex();
						// double layoutY = + getExtensibleFlow().getHeight()
						cell.getIndex();
						double cellPosition =
						// getExtensibleFlow().getPosition()
						// * targetSize;
						// cellPosition -= cellPosition % getFixedCellSize();
						// cellPosition +=
						getExtensibleFlow().getPosition(cell);
						int index = ((I) cell.getChildrenUnmodifiable().get(1))
								.getIndex();

						cellPosition -= getExtensibleFlow().getLayoutY();
						cellPosition -= (cellPosition % getFixedCellSize());
						double layoutY = getExtensibleFlow().getLayoutY();
						double minCellPosition = visibleSize - targetSize;
						minCellPosition = minCellPosition
								- (minCellPosition % getFixedCellSize());
						// no fit size
						if (minCellPosition <= 0) {
							return cellPosition;
						} else
						// fit size but must scroll up
						if (cellPosition + getFixedCellSize() > minCellPosition) {
							return getFixedCellSize() + cellPosition
									- minCellPosition;
						}
						// fit size with no need to scroll up
						return 0;
					}
				});

				// put(MouseEvent.MOUSE_CLICKED, (event) -> {
				// event.consume();
				// });
				// put(MouseEvent.MOUSE_MOVED, (event) -> {
				// event.consume();
				// });
				// put(MouseEvent.MOUSE_ENTERED, (event) -> {
				// event.consume();
				// });
				// put(MouseEvent.MOUSE_EXITED, (event) -> {
				// event.consume();
				// });
				// put(MouseEvent.MOUSE_ENTERED_TARGET, (event) -> {
				// event.consume();
				// });
				// put(MouseEvent.MOUSE_EXITED_TARGET, (event) -> {
				// event.consume();
				// });
				// put(ScrollEvent.ANY, (event) -> {
				// event.consume();
				// });
				// put(KeyEvent.ANY, (event) -> {
				// event.consume();
				// });
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

	public ObjectProperty<TreeItem<M>> expandedItemProperty() {
		if (expandedItem == null)
			expandedItem = new SimpleObjectProperty<TreeItem<M>>();
		return expandedItem;
	}

	public TreeItem<M> getExpandedItem() {
		return expandedItemProperty().get();
	}

	public void setExpandedItem(TreeItem<M> expandedItem) {
		expandedItemProperty().set(expandedItem);
	}

	/**
	 * <p>
	 * Represents the number of tree nodes presently able to be visible in the
	 * TreeTableView. This is essentially the count of all expanded tree items,
	 * and their children.
	 *
	 * <p>
	 * For example, if just the root node is visible, the expandedItemCount will
	 * be one. If the root had three children and the root was expanded, the
	 * value will be four.
	 */
	private IntegerProperty expandedItemCount = new SimpleIntegerProperty(this,
			"expandedItemCount", 0);

	public final IntegerProperty expandedItemCountProperty() {
		return expandedItemCount;
	}

	public void setExpandedItemCount(int value) {
		expandedItemCount.set(value);
	}

	public final int getExpandedItemCount() {
		return expandedItemCount.get();
	}

	private DoubleProperty visibleSize;

	public DoubleProperty visibleSizeProperty() {
		if (visibleSize == null)
			visibleSize = new SimpleDoubleProperty(0);
		return visibleSize;
	}

	public double getVisibleSize() {
		return visibleSizeProperty().get();
	}

	public void setVisibleSize(double visibleSize) {
		this.visibleSizeProperty().set(visibleSize);
	}

}
