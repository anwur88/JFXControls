/**
 * 
 */
package org.devel.jfxcontrols.scene.control.skin;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.IndexedCell;

import com.sun.javafx.scene.control.skin.VirtualFlow;

/**
 *
 * @param <M>
 *            The type of the item stored in the cell
 * @param <I>
 *            The type of cell used by this virtualised control (e.g. TableRow,
 *            TreeTableRow)
 * 
 * @author stefan.illgen
 */
public class ExtendableFlow<M, I extends IndexedCell<M>> extends VirtualFlow<I> {

	public ExtendableFlow() {
		super();
		getHbar().setVisible(false);
		visibleHeightProperty().bind(fixedCellLengthProperty().multiply(visibleCellCountProperty())
															  .add(hBarHeight()));
	}

	@Override
	public void resize(double width, double height) {
		super.resize(width, getVisibleHeight());
	}

	// ###############extensions #################

	private ObservableList<FlowExtension<M, I>> extensions;

	public ObservableList<FlowExtension<M, I>> extensionsProperty() {
		if (extensions == null)
			extensions = FXCollections.observableArrayList(new ArrayList<FlowExtension<M, I>>());
		return extensions;
	}

	public List<FlowExtension<M, I>> getExtensions() {
		return extensionsProperty().subList(0, extensions.size());
	}

	public boolean addAllExtensions(List<FlowExtension<M, I>> extensions) {
		return extensionsProperty().addAll(extensions);
	}

	public boolean addExtension(FlowExtension<M, I> extension) {
		return extensionsProperty().add(extension);
	}

	public boolean removeExtension(FlowExtension<M, I> extension) {
		return extensionsProperty().remove(extension);
	}

	public FlowExtension<M, I> removeExtension(int index) {
		return extensionsProperty().remove(index);
	}

	// PROPS

	private SimpleIntegerProperty visibleCellCount;

	public IntegerProperty visibleCellCountProperty() {
		if (visibleCellCount == null)
			visibleCellCount = new SimpleIntegerProperty(0);
		return visibleCellCount;
	}

	public int getVisibleCellCount() {
		return visibleCellCountProperty().get();
	}

	public void setVisibleCellCount(int rowCount) {
		visibleCellCountProperty().set(rowCount);
	}

	public double getPosition(I selectedCell) {
		return getCellPosition(selectedCell);
	}

	public void positionCell(I cell, double position) {
		super.positionCell(cell, position);
	}

	public ReadOnlyDoubleProperty hBarHeight() {
		return getHbar().heightProperty();
	}

	public ReadOnlyDoubleProperty vBarWidth() {
		return getVbar().widthProperty();
	}

	private IntegerProperty selectedCellIndex;
	private SimpleDoubleProperty fixedCellLength;
	private SimpleDoubleProperty visibleHeight;

	public IntegerProperty firstVisibleCellIndexProperty() {
		if (selectedCellIndex == null)
			new SimpleIntegerProperty(0);
		return selectedCellIndex;
	}

	public int getFirstVisibleCellIndex() {
		return firstVisibleCellIndexProperty().get();
	}

	public void setFirstVisibleCellIndex(int firstVisibleCellIndex) {
		this.selectedCellIndex.set(firstVisibleCellIndex);
	}

	public DoubleProperty fixedCellLengthProperty() {
		if (fixedCellLength != null)
			fixedCellLength = new SimpleDoubleProperty(0);
		return fixedCellLength;
	}

	public double getFixedCellLength() {
		return fixedCellLengthProperty().get();
	}

	public void setFixedCellLength(double fixedCellLength) {
		fixedCellLengthProperty().set(fixedCellLength);
	}

	public DoubleProperty visibleHeightProperty() {
		if (visibleHeight != null)
			visibleHeight = new SimpleDoubleProperty(0);
		return visibleHeight;
	}

	public double getVisibleHeight() {
		return visibleHeightProperty().get();
	}

	public void setVisibleHeight(double visibleHeight) {
		visibleHeightProperty().set(visibleHeight);
	}

}
