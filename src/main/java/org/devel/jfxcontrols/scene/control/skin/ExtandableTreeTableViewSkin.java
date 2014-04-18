/**
 * 
 */
package org.devel.jfxcontrols.scene.control.skin;

import java.util.ArrayList;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;

import com.sun.javafx.scene.control.skin.TreeTableViewSkin;
import com.sun.javafx.scene.control.skin.VirtualFlow;

/**
 * @author stefan.illgen
 *
 */
public class ExtandableTreeTableViewSkin<S> extends TreeTableViewSkin<S>
		implements TreeItemHolder<S, TreeTableRow<S>> {

	private ExtendableFlow<S, TreeTableRow<S>> extensibleFlow;

	public ExtandableTreeTableViewSkin(TreeTableView<S> treeTableView) {
		super(treeTableView);
	}

	/*
	 * Create a custom flow.
	 */
	@Override
	protected VirtualFlow<TreeTableRow<S>> createVirtualFlow() {
		extensibleFlow = new ExtendableFlow<S, TreeTableRow<S>>();
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

	@Override
	public TreeItem<S> getTreeItem(TreeTableRow<S> cell) {
		return cell.getTreeItem();
	}

	public ExtendableFlow<S, TreeTableRow<S>> getExtendableFlow() {
		return extensibleFlow;
	}

	public void addExtensions(
			ArrayList<FlowExtension<S, TreeTableRow<S>>> extensions) {
		extensibleFlow.addAllExtensions(extensions);
	}

}
