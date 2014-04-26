/**
 * 
 */
package org.devel.jfxcontrols.scene.control.treetableview.command;

import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.scene.control.IndexedCell;

/**
 * @author stefan.illgen
 *
 */
public interface Adjustable<T, I extends IndexedCell<T>> extends Receiver {

	/**
	 * Indicates whether this {@link Adjustable} is has a dynamic vertical
	 * length (height) or a variable horizontal length (width).
	 * 
	 * @return True, if this {@link Adjustable} is in vertical mode, otherwise
	 *         false (horizontal mode).
	 */
	BooleanProperty verticalProperty();

	boolean getVertical();

	void setVertical(boolean vertical);

	/**
	 * Current position in pixels.
	 * <ul>
	 * <li>Minimum (beginning i.e. top / left): 0</li>
	 * <li>Maximum (end i.e. bottom / right): maximum position</li>
	 * </ul>
	 * 
	 * @return the current position in pixels
	 */
	DoubleProperty absPositionProperty();

	double getAbsPosition();

	void setAbsPosition(double position);

	/**
	 * Maximum position (bottom / right) calculated by subtracting visible
	 * height from total height.
	 * 
	 * @return the maximum position (i.e. bottom or right end of the
	 *         {@link Adjustable})
	 */
	ReadOnlyDoubleProperty maxPositionProperty();

	double getMaxPosition();

	/**
	 * The fixed length defines the length depending on this {@link Adjustable}s
	 * {@link #verticalProperty()}. In vertical mode the length equals the
	 * height of a (row) cell. In horizontal mode the fixed cell length means
	 * the width of the (column) cell.
	 * 
	 * @return
	 */
	DoubleProperty fixedCellLengthProperty();

	double getFixedCellLength();

	void setFixedCellLength(double fixedCellLength);

	/**
	 * Exact amount of visible cells (i.e. rows or columns).
	 * 
	 * @return
	 */
	IntegerProperty visibleCellCountProperty();

	int getVisibleCellCount();

	void setVisibleCellCount(int visibleCellCount);

	/**
	 * Total amount of cells (i.e. rows or columns) including visible and
	 * invisible ones.
	 * 
	 * @return
	 */
	ReadOnlyIntegerProperty totalCellCountProperty();

	int getTotalCellCount();

	/**
	 * Visible height of the {@link Adjustable}.
	 * 
	 * <strong>Note:</strong> This property is calculated multiplying the cell
	 * length and the amount of visible cells.
	 * 
	 * @return
	 */
	ReadOnlyDoubleProperty visibleHeightProperty();

	double getVisibleHeight();

	/**
	 * Total height of cells (i.e. rows or columns) including visible and
	 * invisible ones.
	 * 
	 * @return
	 */
	ReadOnlyIntegerProperty totalHeightProperty();

	int getTotalHeight();

	/**
	 * ...
	 * 
	 * @return
	 */
	public ReadOnlyDoubleProperty entireCellDeltaProperty();

	public double getEntireCellDelta();

	/**
	 * Indexed List of visible rows.
	 * 
	 * @return
	 */
	ReadOnlyListProperty<I> visibleCellsProperty();

	List<I> getVisibleCells();

	/**
	 * Adjusts the {@link Adjustable} bei changing the position by the given
	 * delta value.
	 * 
	 * @param deltaY
	 * @return
	 */
	double adjustPixels(double delta);

	/**
	 * 
	 * @return
	 */
	public double adjustEntireCellDelta();

	/**
	 * Adjusts Pixels awaiting for the next layout pass.
	 * 
	 * @param readOnlyBooleanProperty
	 * @param intialFlowPosition
	 */
	void layoutAdjustPixels(int selectedIndex);

}
