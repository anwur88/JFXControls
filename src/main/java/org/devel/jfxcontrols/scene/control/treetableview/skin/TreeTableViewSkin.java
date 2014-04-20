/**
 * 
 */
package org.devel.jfxcontrols.scene.control.treetableview.skin;

import javafx.scene.control.TreeTableRow;

import org.devel.jfxcontrols.scene.control.treetableview.command.Adjustable;

import com.sun.javafx.scene.control.skin.VirtualFlow;

/**
 * @author stefan.illgen
 *
 */
public class TreeTableViewSkin<T>
	extends
		com.sun.javafx.scene.control.skin.TreeTableViewSkin<T> {

	private static final String COMMAND_FACTORY_PROPERTY = "COMMAND_FACTORY";

	private AdjustableFlow<T, TreeTableRow<T>> adjustableFlow;

	public TreeTableViewSkin(final org.devel.jfxcontrols.scene.control.treetableview.TreeTableView<T, Adjustable<T, TreeTableRow<T>>> treeTableView) {
		super(treeTableView);
		registerChangeListener(treeTableView.commandFactoryProperty(),
							   COMMAND_FACTORY_PROPERTY);
	}

	@SuppressWarnings({
		"rawtypes", "unchecked"
	})
	@Override
	protected void handleControlPropertyChanged(String p) {
		super.handleControlPropertyChanged(p);

		if (COMMAND_FACTORY_PROPERTY.equals(p)) {
			((org.devel.jfxcontrols.scene.control.treetableview.TreeTableView) getSkinnable()).getCommandFactory()
																							  .call(adjustableFlow);
		}
	}

	/*
	 * Create a custom flow.
	 */
	@Override
	protected VirtualFlow<TreeTableRow<T>> createVirtualFlow() {
		adjustableFlow = new AdjustableFlow<T, TreeTableRow<T>>(getSkinnable().expandedItemCountProperty(),
																10,
																50);
		return adjustableFlow;
	}

	/*
	 * Remove disclosure arrow.
	 */
	@Override
	public TreeTableRow<T> createCell() {
		TreeTableRow<T> cell;

		if (getSkinnable().getRowFactory() != null) {
			cell = getSkinnable().getRowFactory().call(getSkinnable());
		} else {
			cell = new TreeTableRow<T>();
		}

		cell.updateTreeTableView(getSkinnable());
		return cell;
	}

}
