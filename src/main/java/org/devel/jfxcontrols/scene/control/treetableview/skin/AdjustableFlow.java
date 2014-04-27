/**
 * 
 */
package org.devel.jfxcontrols.scene.control.treetableview.skin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.TreeTableRow;

import org.devel.jfxcontrols.lang.UnsupportedValueException;
import org.devel.jfxcontrols.scene.control.treetableview.command.Adjustable;
import org.devel.jfxcontrols.scene.control.treetableview.command.RowAdjust;

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
	private final ReadOnlyDoubleWrapper entireCellDelta = new ReadOnlyDoubleWrapper(0);
	private final ReadOnlyDoubleWrapper maxPosition = new ReadOnlyDoubleWrapper(0);
	private final ReadOnlyDoubleWrapper visibleHeight = new ReadOnlyDoubleWrapper(0);
	private final ReadOnlyIntegerWrapper totalHeight = new ReadOnlyIntegerWrapper(0);
	private final ReadOnlyListWrapper<I> visibleCells = new ReadOnlyListWrapper<I>(FXCollections.observableArrayList(new ArrayList<I>()));
	private final ReadOnlyIntegerWrapper totalCellCount = new ReadOnlyIntegerWrapper(0);
	private RowAdjust<String, TreeTableRow<String>> rowAdjust;
	private int selectedIndex = -1;

	public AdjustableFlow(ReadOnlyIntegerProperty totalCellCount,
		int visibleCellCount,
		double fixedCellLength) {
		super();
		if (visibleCellCount < 0)
			throw new UnsupportedValueException("The parameter visibleCellCount must be a non negative value.");
		if (fixedCellLength < 0)
			throw new UnsupportedValueException("The parameter fixedCellLength must be a non negative value.");
		setVisibleCellCount(visibleCellCount);
		setFixedCellLength(fixedCellLength);
		bind(totalCellCount);

		final ChangeListener<Boolean> cl = new ChangeListener<Boolean>() {
			private boolean adjusted;

			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
								Boolean oldValue,
								Boolean newValue) {

				if (selectedIndex != -1) {
					Platform.runLater(() -> {
						if (selectedIndex != -1) {
							int cache = selectedIndex;
							selectedIndex = -1;
							adjustIndexFirst(cache);
						}
						requestCellLayout();
					});
				}
			}
		};
		needsLayoutProperty().addListener(cl);
	}

	private void bind(ReadOnlyIntegerProperty totalCellCount2) {

		totalCellCount.bind(totalCellCount2);
		totalHeight.bind(totalCellCount.multiply(fixedCellLengthProperty()));
		visibleHeight.bind(visibleCellCountProperty().multiply(fixedCellLengthProperty())
													 .add(getHbar().heightProperty()));
		prefHeightProperty().bind(visibleHeight);
		maxPosition.bind(totalHeightProperty().subtract(visibleCellCountProperty().multiply(fixedCellLengthProperty())));
		entireCellDelta.bind(new DoubleBinding() {
			{
				super.bind(absPositionProperty(),
						   fixedCellLengthProperty(),
						   visibleCellCountProperty());
			}

			@Override
			protected double computeValue() {
				double maxDelta = getFixedCellLength();
				double delta = getAbsPosition() % maxDelta;
				return delta > 0 ? (Math.abs(delta) < maxDelta / 2 ? -delta : maxDelta
					- delta) : (Math.abs(delta) < maxDelta / 2
					? -delta
					: -(maxDelta + delta));
			}
		});
	}

	/*
	 * Overridden to set a fix height for this VirtualFlow.
	 * @see javafx.scene.layout.Region#resize(double, double)
	 */
	@Override
	public void resize(double width, double height) {
		super.resize(width, getVisibleHeight());
	}

	@Override
	public void setPosition(double newPosition) {
		super.setPosition(newPosition);
		System.out.println("set position...");
		setAbsPosition(makePositionAbsolute(newPosition));
	}

	private double makePositionAbsolute(double newPosition) {
		double absPosition = newPosition
			* (getTotalCellCount() * getFixedCellLength() - getVisibleHeight());
		// double correction = absPosition % getFixedCellLength();
		// absPosition = (correction < (getFixedCellLength() / 2)) ? absPosition
		// - correction : absPosition + (getFixedCellLength() - correction);
		return absPosition;
	}

	// public double adjustPixels(final double delta) {
	// double result = 0.0d;
	// result = (delta < -getAbsPosition())
	// ? super.adjustPixels(-getAbsPosition())
	// : super.adjustPixels(delta);
	// setAbsPosition(getAbsPosition() + result);
	// return result;
	// }

	public double adjustIndexFirst(final int index) {
		int currentIndex = getCurrentIndex();
		System.out.println("absPosition: " + getAbsPosition());
		System.out.println("curindex: " + currentIndex);
		System.out.println("index: " + index);
		System.out.println(getLayoutY());
		System.out.println(getCell(index).getLayoutY());
		System.out.println(getDeltaY(currentIndex, getCells()));

		double result = ((index - currentIndex) * getFixedCellLength())
			- (makePositionAbsolute(getPosition()) % getFixedCellLength());
		// + getDeltaY(currentIndex, getCells()
		System.out.println("result: " + result);
		return adjustPixels(result);
	}

	private double getDeltaY(int currentIndex, List<? extends IndexedCell<?>> cells) {
		for (IndexedCell<?> cell : cells) {
			if (cell.getIndex() == currentIndex)
				return cell.getLayoutY();

			return getDeltaY(currentIndex,
							 cell.getChildrenUnmodifiable()
								 .filtered(new Predicate<Node>() {
									 @Override
									 public boolean test(Node t) {
										 return t instanceof IndexedCell<?>;
									 }
								 })
								 .stream()
								 .map(new Function<Node, IndexedCell<?>>() {

									 @Override
									 public IndexedCell<?> apply(Node t) {
										 return (IndexedCell<?>) t;
									 }
								 })
								 .collect(Collectors.toList()));
		}
		return 0.0;
	}

	private int getCurrentIndex() {

		System.out.println("P(abs): " + getAbsPosition());
		System.out.println("P(max): " + getMaxPosition());
		System.out.println("n(Zelle): " + getTotalCellCount());

		// setAbsPosition(makePositionAbsolute(getPosition()));

		double makePositionAbsolute = makePositionAbsolute(getPosition());
		System.out.println("makePositionAbsolute: " + makePositionAbsolute);

		int result = (int) (makePositionAbsolute(getPosition()) / getFixedCellLength());
		// getMaxPosition()
		// *

		return result;

	}

	@Override
	public double adjustEntireCellDelta() {

		System.out.println("###P(abs): " + getAbsPosition());
		System.out.println("###Cell Delta" + getEntireCellDelta());

		double absPositionError = (getAbsPosition() + getEntireCellDelta())
			% getFixedCellLength();
		double absDelta = getEntireCellDelta() - absPositionError;
		double result = adjustPixels(absDelta);
		return result;
	}

	// ########## properties #############

	@Override
	public boolean getVertical() {
		return verticalProperty().get();
	}

	@Override
	public DoubleProperty absPositionProperty() {
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
		return absPositionProperty().get();
	}

	@Override
	public void setAbsPosition(double absPosition) {
		absPositionProperty().set(absPosition);
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

	public ReadOnlyDoubleProperty entireCellDeltaProperty() {
		return entireCellDelta.getReadOnlyProperty();
	}

	public double getEntireCellDelta() {
		return entireCellDeltaProperty().get();
	}

	@Override
	public ReadOnlyListProperty<I> visibleCellsProperty() {
		return visibleCells.getReadOnlyProperty();
	}

	@Override
	public List<I> getVisibleCells() {
		return Collections.unmodifiableList(visibleCellsProperty().get());
	}

	@Override
	public void layoutAdjustPixels(final int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

}
