/**
 * 
 */
package org.devel.jfxcontrols.scene.control.treetableview.skin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

	public double adjustPixels(final double delta) {
		double result = super.adjustPixels(delta);
		setAbsPosition(getAbsPosition() + delta);
		return result;
	}

	@Override
	public double adjustEntireCellDelta() {
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
	public void setPosition(double position) {
		super.setPosition(position);
		setAbsPosition(position * getMaxPosition());
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
	protected void layoutChildren() {

		double absPosition = getAbsPosition();
		int totalHeight = getTotalHeight();
		System.out.println("Before: ");
		System.out.println("Position: " + absPosition + " px");
		System.out.println("Total Height: " + totalHeight + " px");

		super.layoutChildren();

		double positionDelta = getAbsPosition() - absPosition;
		double heightDelta = getTotalHeight() - totalHeight;

		// setPosition(getMaxPosition() / absPosition);

		System.out.println("After: ");
		System.out.println("Position: " + absPosition + " px");
		System.out.println("Total Height: " + totalHeight + " px");
	}

	@Override
	public void layoutAdjustPixels(double intialFlowPosition) {
		needsLayoutProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> obs,
								Boolean oldV,
								Boolean newV) {
				if (newV != null && !newV) {
					obs.removeListener(this);
					adjustPixels(getAbsPosition() - intialFlowPosition);
				}
			}
		});
	}
}
