/**
 * 
 */
package org.devel.jfxcontrols.scene.control.treetableview.skin;

import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableRow;

import org.devel.jfxcontrols.scene.control.treetableview.ExpandableTreeTableCell;
import org.devel.jfxcontrols.scene.control.treetableview.command.Expand;
import org.devel.jfxcontrols.scene.control.treetableview.command.Expandable;
import org.devel.jfxcontrols.scene.control.treetableview.command.MouseKeyboardEventMapper;
import org.devel.jfxcontrols.scene.control.treetableview.command.MouseKeyboardEventMapper.ExpandEventMapper;

import com.sun.javafx.scene.control.skin.TreeTableCellSkin;

/**
 * @author stefan.illgen
 *
 */
public class ExpandableTreeTableCellSkin<S, T> extends TreeTableCellSkin<S, T>
	implements
		Expandable<S> {

	private static final String INPUT_MODE = "INPUT_MODE";
	private static final String EXPAND = "EXPAND";
	private ExpandEventMapper<S, T, TreeTableCell<S, T>, TreeTableRow<T>> expandEventMapper;

	public ExpandableTreeTableCellSkin(ExpandableTreeTableCell<S, T> treeTableCell) {
		super(treeTableCell);
		registerChangeListener(treeTableCell.inputModeProperty(), INPUT_MODE);
		registerChangeListener(treeTableCell.inputModeProperty(), EXPAND);
		initExpand();
	}

	@Override
	protected void handleControlPropertyChanged(String p) {
		super.handleControlPropertyChanged(p);

		if (INPUT_MODE.equals(p)) {
			reloadEventMapping();
		} else if (EXPAND.equals(p)) {
			initExpand();
		}
	}

	private void initExpand() {
		Expand<S, T, TreeTableCell<S, T>, TreeTableRow<T>> expand = getSkinnableC().getExpand();
		if (expand != null) {
			expand.setReceiver(getSkinnableC().getTreeTableView());
		}
		reloadEventMapping();
	}

	private void reloadEventMapping() {

		if (expandEventMapper != null) {
			expandEventMapper.unregisterAll();
		}

		if (getSkinnableC().getInputMode() != null && getSkinnableC().getExpand() != null) {

			switch (getSkinnableC().getInputMode()) {
			case MOUSE_KEYBOARD:
				expandEventMapper = MouseKeyboardEventMapper.ExpandEventMapper.<S, T, TreeTableCell<S, T>, TreeTableRow<T>> create(getSkinnableC().getTreeTableView(),
																																   getSkinnableC().getExpand());
				break;
			case TOUCH:
				// expandEventMapper =
				// TouchKeyboardEventMapper.expandEventMapper.<T,
				// TreeTableRow<T>> create(getSkinnableC(),
				// expand);
				break;
			default:
				break;
			}
		}
	}

	private ExpandableTreeTableCell<S, T> getSkinnableC() {
		return (ExpandableTreeTableCell<S, T>) getSkinnable();
	}

	@Override
	public int expand() {
		// TODO Auto-generated method stub
		return 0;
	}

	// @Override
	// public void expand() {
	// TreeItem<S> selected = getSkinnable().getTreeTableRow().getTreeItem();
	// //
	// getSkinnable().getTreeTableView().getSelectionModel().getSelectedItem();
	// getSkinnable().getTreeTableView().
	//
	// }
	// @Override
	// public void expand() {
	//
	// getSkinnableC().getTreeTableView()
	// .getSelectionModel()
	// .selectedItemProperty()
	//
	//
	// // TreeItem<S> treeItem =
	// // getSkinnableC().getTreeTableRow().getTreeItem();
	// // if (expandedTreeItem == null) {
	// // System.out.println("nothing expanbded");
	// // expandedTreeItem = treeItem;
	// // treeItem.setExpanded(true);
	// // } else if (expandedTreeItem != null &&
	// // !expandedTreeItem.equals(treeItem)) {
	// // System.out.println("expanded but other item");
	// // expandedTreeItem.setExpanded(false);
	// // treeItem.setExpanded(true);
	// // expandedTreeItem = treeItem;
	// // } else if (expandedTreeItem.equals(treeItem)) {
	// // System.out.println("expanded and same item");
	// // treeItem.setExpanded(false);
	// // expandedTreeItem = null;
	// // }
	// //
	// // return expandedTreeItem;
	// }

}
