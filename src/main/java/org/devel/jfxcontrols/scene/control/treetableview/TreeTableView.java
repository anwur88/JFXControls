/**
 * 
 */
package org.devel.jfxcontrols.scene.control.treetableview;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableRow;
import javafx.util.Callback;

import org.devel.jfxcontrols.scene.control.treetableview.command.Adjustable;
import org.devel.jfxcontrols.scene.control.treetableview.command.Command;
import org.devel.jfxcontrols.scene.control.treetableview.command.RowAdjust;
import org.devel.jfxcontrols.scene.control.treetableview.command.RowAdjust.Action;

/**
 * @author stefan.illgen
 *
 */
public class TreeTableView<T, A extends Adjustable<T, TreeTableRow<T>>>
	extends
		javafx.scene.control.TreeTableView<T> {

	ObjectProperty<Callback<A, Command<RowAdjust.Action, A>>> commandFactory;

	public TreeTableView() {
		this(null);
	}

	public TreeTableView(TreeItem<T> root) {
		super(root);
	}

	public ObjectProperty<Callback<A, Command<RowAdjust.Action, A>>>
		commandFactoryProperty() {
		if (commandFactory == null) {
			commandFactory = new SimpleObjectProperty<Callback<A, Command<Action, A>>>(null);
		}
		return commandFactory;
	}

	public Callback<A, Command<Action, A>> getCommandFactory() {
		return commandFactoryProperty().get();
	}

	public void setCommandFactory(Callback<A, Command<Action, A>> commandFactory) {
		this.commandFactoryProperty().set(commandFactory);
	}

}
