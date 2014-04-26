package org.devel.jfxcontrols.scene.control.treetableview.command;

import javafx.scene.control.TreeItem;

/**
 * 
 * @author stefan.illgen
 *
 */
public interface Expandable<S> extends Receiver {

	TreeItem<S> expand();

}
