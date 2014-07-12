/**
 * 
 */
package org.devel.jfxcontrols.scene.control.treetableview.skin;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.IndexedCell;

import org.devel.jfxcontrols.scene.control.treetableview.command.Adjustable;

import com.sun.javafx.scene.control.skin.VirtualContainerBase;
import com.sun.javafx.scene.control.skin.VirtualFlow;

/**
 * @author stefan.illgen
 *
 */
public class AdjustableFlow<T, I extends IndexedCell<T>> extends VirtualFlow<I>
	implements
		Adjustable<T, I> {

	private DoubleProperty absPosition;
	private IntegerProperty visibleCellCount;
	private SimpleDoubleProperty fixedCellLength;
	private final ReadOnlyDoubleWrapper maxPosition = new ReadOnlyDoubleWrapper(0);
	private final ReadOnlyDoubleWrapper visibleHeight = new ReadOnlyDoubleWrapper(0);
	private final ReadOnlyIntegerWrapper totalHeight = new ReadOnlyIntegerWrapper(0);
	private final ReadOnlyListWrapper<I> visibleCells = new ReadOnlyListWrapper<I>(FXCollections.observableArrayList(new ArrayList<I>()));
	private final ReadOnlyIntegerWrapper totalCellCount = new ReadOnlyIntegerWrapper(0);
	private int selectedIndex = -1;
	private SimpleIntegerProperty firstCellIndex;
	private ObjectProperty<I> firstVisibleCell;
	private BooleanProperty cellCountChanged;
	private int lastCellCount;
	private double selectedItemCount = -1;
	private double lastCellPosition;

	private final ChangeListener<Boolean> needsLayoutListener = (obs, oldV, newV) -> {
		System.out.println("needs layout change listener called ..");
		if (oldV && !newV && selectedIndex != -1 && getVisibleCell(selectedIndex) != null) {

			System.out.println("selectedIndex: " + selectedIndex);
			System.out.println("selectedItemCount: " + selectedItemCount);
			System.out.println("getFixedCellLength(): " + getFixedCellLength());
			System.out.println("getCellPosition(getVisibleCell(selectedIndex)): "
				+ getCellPosition(getVisibleCell(selectedIndex)));

			show(selectedIndex,
				 selectedItemCount * getFixedCellLength(),
				 getCellPosition(getVisibleCell(selectedIndex)));

			lastCellPosition = -1;
			selectedIndex = -1;
			selectedItemCount = -1;
		}
	};

	@Override
	protected void layoutChildren() {

		super.layoutChildren();

		// if (lastCellCount != getCellCount()) {
		// lastCellPosition = getCellPosition(getVisibleCell(selectedIndex));
		// lastCellCount = getCellCount();
		// } else if (selectedIndex != -1 && lastCellCount == getCellCount()
		// && getVisibleCell(selectedIndex) != null) {
		// show(selectedIndex,
		// selectedItemCount * getFixedCellLength(),
		// lastCellPosition);
		// lastCellPosition = -1;
		// selectedIndex = -1;
		// selectedItemCount = -1;
		// }

		// if (selectedIndex != -1) {
		//
		// double selectedCellPositionOld =
		// getCellPosition(getVisibleCell(selectedIndex));
		// super.layoutChildren();
		// double selectedCellPositionNew =
		// getCellPosition(getVisibleCell(selectedIndex));
		// double selectedCellPositionDelta = selectedCellPositionNew
		// - selectedCellPositionOld;
		//
		// System.out.println("selectedCellPositionDelta: " +
		// selectedCellPositionDelta);
		//
		// lastCellCount = getCellCount();
		//
		// } else {
		// super.layoutChildren();
		// }

		// final double oldAbsPosition = getAbsPosition();
		// System.out.println("old abs pos: " + oldAbsPosition);

		// final double newAbsPosition = getAbsPosition();
		// System.out.println("new abs pos: " + newAbsPosition);

		// if (lastCellCount != getCellCount() && selectedIndex != -1) {
		// double selectedCellPositionNew =
		// getCellPosition(getVisibleCell(selectedIndex));
		// }
		//
		// if (lastCellCount != getCellCount() && lastSelectedIndex !=
		// selectedIndex) {
		// // adjust
		// // adjustPixels(newAbsPosition - oldAbsPosition);
		// if (lastSelectedIndex != selectedIndex) {
		// adjustFirst(selectedIndex, selectedItemCount);
		// lastSelectedIndex = selectedIndex;
		// }
		// // cache
		// lastCellCount = getCellCount();
		// }
		// else
		// if (lastCellCount != getCellCount() && lastSelectedIndex ==
		// selectedIndex) {
		// // adjust
		// adjustPixels(newAbsPosition - oldAbsPosition);
		// // adjustFirst(selectedIndex);
		// // cache
		// lastCellCount = getCellCount();
		// // lastSelectedIndex = selectedIndex;
		// }

		// if (selectedIndex != -1) {
		// adjustFirst(selectedIndex);
		// }

		// if (getCellCountChanged()) {
		// setCellCountChanged(false);

		// } else {
		//
		// }
	}

	private T selectedItem;

	public AdjustableFlow(ReadOnlyBooleanProperty needsLayout,
		ReadOnlyIntegerProperty totalCellCount,
		int visibleCellCount,
		double fixedCellLength) {
		super();

		setSnapToPixel(true);

		// needsLayout.addListener(needsLayoutListener);

		if (visibleCellCount < 0)
			throw new IllegalArgumentException("The parameter visibleCellCount must be a non negative value.");
		if (fixedCellLength < 0)
			throw new IllegalArgumentException("The parameter fixedCellLength must be a non negative value.");
		setVisibleCellCount(visibleCellCount);
		setFixedCellLength(fixedCellLength);
		bind(totalCellCount);

	}

	private void bind(ReadOnlyIntegerProperty totalCellCount2) {

		totalCellCount2.addListener((obs) -> {
			setCellCountChanged(true);
		});

		totalCellCount.bind(totalCellCount2);
		totalHeight.bind(totalCellCount.multiply(fixedCellLengthProperty()));
		visibleHeight.bind(visibleCellCountProperty().multiply(fixedCellLengthProperty())
													 .add(getHbar().heightProperty()));
		prefHeightProperty().bind(visibleHeight);
		maxPosition.bind(totalHeightProperty().subtract(visibleCellCountProperty().multiply(fixedCellLengthProperty())));
		// entireCellDelta.bind(new DoubleBinding() {
		// {
		// super.bind(positionProperty(),
		// fixedCellLengthProperty(),
		// visibleCellCountProperty());
		// }
		//
		// @Override
		// protected double computeValue() {
		// return computeEntireCellDelta();
		// }
		//
		// });

	}

	@Override
	public double computeEntireCellDelta() {
		double maxDelta = getFixedCellLength();
		double delta = convertToAbsolutePosition(getPosition()) % maxDelta;
		double result = delta > 0 ? (Math.abs(delta) < maxDelta / 2 ? -delta : maxDelta
			- delta) : (Math.abs(delta) < maxDelta / 2 ? -delta : -(maxDelta + delta));
		return result;
	}

	/*
	 * Overridden to set a fix height for this VirtualFlow.
	 * @see javafx.scene.layout.Region#resize(double, double)
	 */
	@Override
	public void resize(double width, double height) {
		super.resize(width, getVisibleHeight());
	}

	private double convertToAbsolutePosition(double relPosition) {
		return snapPosition(relPosition
			* ((getCellCount() * getFixedCellLength()) - getViewportLength()));
	}

	private double convertToRelativePosition(double absPosition) {
		return snapPosition(absPosition
			/ (getTotalCellCount() * getFixedCellLength() - getVisibleHeight()));
	}

	public double adjustFirst(final int index, double selectedItemCount2) {

		double result = 0.0d;
		setFirstVisibleCellIndex(index);
		I visibleCell = getVisibleCell(index);
		return result;
	}

	public void show(final int selectedIndex, final double length, double topCellIndex) {

		// Platform.runLater(() -> {

		System.out.println("index: " + selectedIndex);
		System.out.println("getCellPosition(getVisibleCell(index)): "
			+ getCellPosition(getVisibleCell(selectedIndex)));

		// final double start = getCellPosition(getCell(selectedItem));
		final double start = getCellPosition(getCell(selectedIndex));
		final double end = start + length;
		final double viewportLength = getViewportLength();
		final double delta;

		if (start < 0) {
			delta = start;
		} else if (end > viewportLength) {
			delta = end - viewportLength;
		} else {
			delta = 0.0d;
		}

		System.out.println("aha");

		adjustPixels(delta);
		// });
	}

	public void show(final int index, final double length, int topCellIndex) {

		show(index, length, getCellPosition(getCell(topCellIndex)));
	}

	private I getCell(T item) {
		for (int index = 0; index < getCellCount(); index++) {
			I visibleCell = getVisibleCell(index);
			if (visibleCell != null && visibleCell.getItem().equals(item))
				return visibleCell;
		}
		return null;
	}

	public I getFirstVisibleCell() {
		if (getCells().isEmpty() || getViewportLength() <= 0)
			return null;
		I cell = getCell(getFirstVisibleCellIndex());
		return cell.isEmpty() ? null : cell;
	}

	public int getFirstVisibleCellIndex() {
		return firstVisibleCellIndexProperty().get();
	}

	public void setFirstVisibleCellIndex(int index) {
		firstVisibleCellIndexProperty().set(index);
	}

	private IntegerProperty firstVisibleCellIndexProperty() {
		if (firstCellIndex == null) {
			firstCellIndex = new SimpleIntegerProperty(0);
		}
		return firstCellIndex;
	}

	@Override
	public double adjustEntireCellDelta() {

		System.out.println("###P(abs): " + getAbsPosition());
		System.out.println("###Cell Delta" + computeEntireCellDelta());

		double absPositionError = (getAbsPosition() + computeEntireCellDelta())
			% getFixedCellLength();
		double absDelta = computeEntireCellDelta() - absPositionError;
		double result = adjustPixels(absDelta);
		return result;
	}

	// ########## properties #############

	@Override
	public boolean getVertical() {
		return verticalProperty().get();
	}

	@Override
	public DoubleProperty positionProperty() {
		if (absPosition == null) {
			absPosition = new SimpleDoubleProperty(0);
			absPosition.addListener(new InvalidationListener() {
				@Override
				public void invalidated(Observable observable) {
					// update visible cells
					double indexL = Math.round(getAbsPosition() / getFixedCellLength());
					int index = (int) Math.max(Math.min(getTotalCellCount(), indexL), 0);
					List<I> visibleCells = new ArrayList<I>();
					for (I rowCell : getCells()) {
						if (index <= indexL) {
							for (Node node : rowCell.getChildrenUnmodifiable()) {
								if (node instanceof IndexedCell<?>) {
									IndexedCell<?> cell = (IndexedCell<?>) node;
									if (cell.getIndex() == index) {
										visibleCells.add(rowCell);
										index++;
										break;
									}
								}
							}
						}
					}
					AdjustableFlow.this.visibleCells.setAll(visibleCells);
				}
			});
		}
		return absPosition;
	}

	@Override
	public double getAbsPosition() {
		return positionProperty().get();
	}

	@Override
	public void setAbsPosition(double absPosition) {
		System.out.println("abs pos: " + absPosition);
		positionProperty().set(absPosition);
	}

	@Override
	public ReadOnlyDoubleProperty maxPositionProperty() {
		return maxPosition.getReadOnlyProperty();
	}

	@Override
	public double getMaxPosition() {
		return maxPositionProperty().get();
	}

	@Override
	public DoubleProperty fixedCellLengthProperty() {
		if (fixedCellLength == null) {
			fixedCellLength = new SimpleDoubleProperty(0L);
			fixedCellLength.addListener(new InvalidationListener() {
				@Override
				public void invalidated(Observable observable) {
					AdjustableFlow.super.setFixedCellSize(getFixedCellLength());
				}
			});
		}
		return fixedCellLength;
	}

	@Override
	public double getFixedCellLength() {
		return fixedCellLengthProperty().get();
	}

	@Override
	public void setFixedCellLength(double fixedCellLength) {
		fixedCellLengthProperty().set(fixedCellLength);
	}

	@Override
	public void setFixedCellSize(double value) {
		setFixedCellLength(Math.round(value));
	}

	@Override
	public IntegerProperty visibleCellCountProperty() {
		if (visibleCellCount == null) {
			visibleCellCount = new SimpleIntegerProperty(0);
		}
		return visibleCellCount;
	}

	@Override
	public int getVisibleCellCount() {
		return visibleCellCountProperty().get();
	}

	@Override
	public void setVisibleCellCount(int visibleCellCount) {
		visibleCellCountProperty().set(visibleCellCount);
	}

	@Override
	public ReadOnlyIntegerProperty totalCellCountProperty() {
		return totalCellCount.getReadOnlyProperty();
	}

	@Override
	public int getTotalCellCount() {
		return totalCellCountProperty().get();
	}

	@Override
	public ReadOnlyDoubleProperty visibleHeightProperty() {
		return visibleHeight.getReadOnlyProperty();
	}

	@Override
	public double getVisibleHeight() {
		return visibleHeightProperty().get();
	}

	@Override
	public ReadOnlyIntegerProperty totalHeightProperty() {
		return totalHeight.getReadOnlyProperty();
	}

	@Override
	public int getTotalHeight() {
		return totalHeightProperty().get();
	}

	// public ReadOnlyDoubleProperty entireCellDeltaProperty() {
	// return entireCellDelta.getReadOnlyProperty();
	// }
	//
	// public double getEntireCellDelta() {
	// return entireCellDeltaProperty().get();
	// }

	// @Override
	// public ReadOnlyListProperty<I> visibleCellsProperty() {
	// return visibleCells.getReadOnlyProperty();
	// }
	//
	// @Override
	// public List<I> getVisibleCells() {
	// return Collections.unmodifiableList(visibleCellsProperty().get());
	// }

	public BooleanProperty cellCountChangedProperty() {
		if (cellCountChanged == null) {
			cellCountChanged = new SimpleBooleanProperty(false);
		}
		return cellCountChanged;
	}

	public boolean getCellCountChanged() {
		return cellCountChangedProperty().get();
	}

	public void setCellCountChanged(boolean value) {
		cellCountChangedProperty().set(value);
	}

	@Override
	public void layoutAdjustPixels(final int selectedIndex, double selectedItemCount) {
		lastCellCount = getCellCount();
		selectedItem = getVisibleCell(selectedIndex).getItem();
		lastCellPosition = getCellPosition(getCell(selectedItem));
		this.selectedIndex = selectedIndex;
		this.selectedItemCount = selectedItemCount;
	}

	public void dispose() {
		// needsLayoutProperty().removeListener(needsLayoutListener);
	}

	// protected double snapSize(double value) {
	// return isSnapToPixel() ? Math.round(value) : value;
	// }

	protected void positionCell(I cell, double position) {
		if (isVertical()) {
			cell.setLayoutX(0);
			cell.setLayoutY(snapPosition(position));
		} else {
			cell.setLayoutX(snapPosition(position));
			cell.setLayoutY(0);
		}
	}

}
