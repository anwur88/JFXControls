/**
 * 
 */
package org.devel.jfxcontrols.scene.control.skin;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.IntegerProperty;
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
public class ExtensibleFlow<M, I extends IndexedCell<M>> extends VirtualFlow<I> {

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

	private SimpleIntegerProperty rowCount;

	public IntegerProperty rowCountProperty() {
		if (rowCount == null)
			rowCount = new SimpleIntegerProperty(0);
		return rowCount;
	}

	public int getRowCount() {
		return rowCountProperty().get();
	}

	public void setRowCount(int rowCount) {
		rowCountProperty().set(rowCount);
	}

	public double getPosition(I selectedCell) {
		return getCellPosition(selectedCell);
	}

}
