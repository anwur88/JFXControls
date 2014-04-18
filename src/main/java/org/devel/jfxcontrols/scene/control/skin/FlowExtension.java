/**
 * 
 */
package org.devel.jfxcontrols.scene.control.skin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Region;

/**
 * @author stefan.illgen
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class FlowExtension<M, I extends IndexedCell<M>> {

	private ExtendableFlow<M, I> extensibleFlow;
	private ObjectProperty<SelectionModel<TreeItem<M>>> selectionModel;
	private SimpleDoubleProperty fixedCellSize;
	private SimpleIntegerProperty rowCount;
	private List<FlowExtension<M, I>> children;

	public FlowExtension(ExtendableFlow<M, I> extensibleFlow) {
		this.extensibleFlow = extensibleFlow;
	}

	public ExtendableFlow<M, I> getExtensibleFlow() {
		return extensibleFlow;
	}

	public void setExtensibleFlow(ExtendableFlow<M, I> extensibleFlow) {
		this.extensibleFlow = extensibleFlow;
	}

	public ObjectProperty<SelectionModel<TreeItem<M>>> selectionModelProperty() {
		if (selectionModel == null)
			selectionModel = new SimpleObjectProperty<SelectionModel<TreeItem<M>>>();
		return selectionModel;
	}

	public SelectionModel<TreeItem<M>> getSelectionModel() {
		return selectionModelProperty().get();
	}

	public void setSelectionModel(SelectionModel<TreeItem<M>> selectionModel) {
		this.selectionModel.set(selectionModel);
	}

	public double getFixedCellSize() {
		return fixedCellSizeProperty().get();
	}

	public void setFixedCellSize(double fixedCellSize) {
		fixedCellSizeProperty().set(fixedCellSize);
	}

	public DoubleProperty fixedCellSizeProperty() {
		if (fixedCellSize == null)
			fixedCellSize = new SimpleDoubleProperty(0);
		return fixedCellSize;
	}

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

	public double getMaxViewHeight() {
		return getRowCount() * getFixedCellSize();
	}

	public List<FlowExtension<M, I>> getChildren() {
		if (children == null)
			children = new ArrayList<FlowExtension<M, I>>();
		return children;
	}

	public void addChildren(FlowExtension<M, I> child) {
		getChildren().add(child);
	}

	public void removeChildren(FlowExtension<M, I> child) {
		getChildren().remove(child);
	}

	public <E extends Event> void setEventHandling(Region region) {
		// handler
		Map<EventType<E>, EventHandler<E>> typedEventHandlers = createTypedEventHandlers();
		for (EventType eventType : typedEventHandlers.keySet()) {
			region.addEventHandler(eventType, typedEventHandlers.get(eventType));
		}
		// filter
		Map<EventType<E>, EventHandler<E>> typedEventFilters = createTypedEventFilters();
		for (EventType eventType : typedEventFilters.keySet()) {
			region.addEventFilter(eventType, typedEventFilters.get(eventType));
		}
	}

	protected final void initEventHandling() {
		// ########### accessing row example ###########
		// final Callback<VirtualFlow, I> createCell = getExtensibleFlow()
		// .getCreateCell();
		// getExtensibleFlow().setCreateCell(new Callback<VirtualFlow, I>() {
		// @Override
		// public I call(VirtualFlow flow) {
		// I cell = createCell.call(getExtensibleFlow());
		// setEventHandling(cell);
		// return cell;
		// }
		// });

	}

	protected final double computeDiff2FirstVisibleCell(I selectedCell) {
		I firstVisibleCell = getExtensibleFlow()
				.getFirstVisibleCellWithinViewPort();

		// System.out.println("BRRR: "
		// + Double.valueOf(-selectedCell.getLayoutY() +
		// firstVisibleCell.getLayoutY()
		// + getFixedCellSize()).toString());
		//
		// return -selectedCell.getLayoutY();

		return -((selectedCell.getIndex() - firstVisibleCell.getIndex())
				* getFixedCellSize() + firstVisibleCell.getLayoutY());// : //
																		// getFixedCellSize()
		// +
		// firstVisibleCell.getLayoutY()
		// -selectedCell.getLayoutY() + firstVisibleCell.getLayoutY();
		// + getFixedCellSize();
	}

	protected final double computeDiff2LastVisibleCell(I selectedCell) {
		I lastVisibleCell = getExtensibleFlow()
				.getLastVisibleCellWithinViewPort();

		// System.out.println("BRRR: "
		// + Double.valueOf(-selectedCell.getLayoutY() +
		// firstVisibleCell.getLayoutY()
		// + getFixedCellSize()).toString());
		//
		// return -selectedCell.getLayoutY();

		return getExtensibleFlow().getPosition(selectedCell);
		// - selectedCell.getIndex() * getFixedCellSize();//
		// (lastVisibleCell.getLayoutY());
		// + getExtensibleFlow().getHeight();// - getFixedCellSize();
		// //
		// getFixedCellSize()
		// +
		// firstVisibleCell.getLayoutY()
		// -selectedCell.getLayoutY() + firstVisibleCell.getLayoutY();
		// + getFixedCellSize();
	}

	protected final double computeDiff2EntireFirstCell() {
		I firstVisibleCell = getExtensibleFlow()
				.getFirstVisibleCellWithinViewPort();
		return (Math.abs(firstVisibleCell.getLayoutY()) < getFixedCellSize() / 2) ? -firstVisibleCell
				.getLayoutY() : -getFixedCellSize()
				- firstVisibleCell.getLayoutY();
	}

	protected final double computeDiff2EntireRow() {
		I firstVisibleCell = getExtensibleFlow()
				.getFirstVisibleCellWithinViewPort();
		return (Math.abs(firstVisibleCell.getLayoutY()) < getFixedCellSize()) ? -firstVisibleCell
				.getLayoutY() : -getFixedCellSize()
				- firstVisibleCell.getLayoutY();
	}

	abstract <E extends Event> Map<EventType<E>, EventHandler<E>> createTypedEventHandlers();

	abstract <E extends Event> Map<EventType<E>, EventHandler<E>> createTypedEventFilters();

	// Map<EventType<E>, EventHandler<E>> createTypedEventHandlers();

	// void extend(ExtensibleFlow<M, I> extensibleFlow);

}
