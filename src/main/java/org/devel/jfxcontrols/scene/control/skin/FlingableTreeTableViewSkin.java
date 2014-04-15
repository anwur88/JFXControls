/**
 * 
 */
package org.devel.jfxcontrols.scene.control.skin;

import java.util.ArrayList;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;

import com.sun.javafx.scene.control.skin.TreeTableViewSkin;
import com.sun.javafx.scene.control.skin.VirtualFlow;

/**
 * @author stefan.illgen
 *
 */
public class FlingableTreeTableViewSkin<S> extends TreeTableViewSkin<S>
	implements
		TreeItemHolder<S, TreeTableRow<S>> {

	private ExtensibleFlow<S, TreeTableRow<S>> extensibleFlow;

	private SimpleBooleanProperty flingable;

	// private SimpleBooleanProperty singular;

	public FlingableTreeTableViewSkin(TreeTableView<S> treeTableView) {
		super(treeTableView);
		extensibleFlow.addAllExtensions(new ArrayList<FlowExtension<S, TreeTableRow<S>>>() {
			private static final long serialVersionUID = -3874572374641334976L;
			{
				// obligatory entire ro adjustment
				FlowAdjuster<S, TreeTableRow<S>> adjuster = new FlowAdjuster<S, TreeTableRow<S>>(extensibleFlow);
				adjuster.selectionModelProperty()
						.bind(getSkinnable().selectionModelProperty());
				adjuster.fixedCellSizeProperty()
						.bindBidirectional(getSkinnable().fixedCellSizeProperty());

				// // obligatory expansion
				// FlowTreeExpander<S, TreeTableRow<S>> flowTreeExpander = new
				// FlowTreeExpander<S, TreeTableRow<S>>(extensibleFlow,
				// FlingableTreeTableViewSkin.this,
				// adjuster);
				// //
				// flowTreeExpander.singularProperty().bindBidirectional(singularProperty());
				// flowTreeExpander.rootProperty()
				// .bindBidirectional(getSkinnable().rootProperty());
				//
				// if (isFlingable()) {
				// FlowFlinger<S, TreeTableRow<S>> flowFlinger = new
				// FlowFlinger<S, TreeTableRow<S>>(extensibleFlow,
				// adjuster);
				// flowFlinger.setEventHandling(getSkinnable());
				// }
				adjuster.setEventHandling(getSkinnable());
			}
		});
	}

	/*
	 * Create a custom flow.
	 */
	@Override
	protected VirtualFlow<TreeTableRow<S>> createVirtualFlow() {
		extensibleFlow = new ExtensibleFlow<S, TreeTableRow<S>>();

		return extensibleFlow;
	}

	/*
	 * Remove discolure arrow.
	 */
	@Override
	public TreeTableRow<S> createCell() {
		TreeTableRow<S> cell;

		if (getSkinnable().getRowFactory() != null) {
			cell = getSkinnable().getRowFactory().call(getSkinnable());
		} else {
			cell = new TreeTableRow<S>();
		}

		cell.updateTreeTableView(getSkinnable());
		return cell;
	}

	public BooleanProperty flingableProperty() {
		if (flingable == null)
			flingable = new SimpleBooleanProperty(false);
		return flingable;
	}

	public boolean isFlingable() {
		return flingableProperty().get();
	}

	public void setFlingable(boolean flingable) {
		flingableProperty().set(flingable);
	}

	// public BooleanProperty singularProperty() {
	// if (singular == null)
	// singular = new SimpleBooleanProperty(true);
	// return singular;
	// }
	//
	// public boolean isSingular() {
	// return singularProperty().get();
	// }
	//
	// public void setSingular(boolean flingable) {
	// singularProperty().set(flingable);
	// }

	@Override
	public TreeItem<S> getTreeItem(TreeTableRow<S> cell) {
		return cell.getTreeItem();
	}

}
