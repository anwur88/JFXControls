package org.devel.jfxcontrols.scene.control;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;

/**
 * This {@link ReflectableTreeItem} gets grounded by a special type (i.e. an
 * instance of {@link Class}). It may also create an arbitrary amount of
 * instances of the type using the default constructor.
 * 
 * @author stefan.illgen
 * 
 */
public class ReflectableTreeItem<T extends Node> extends TreeItem<String> {

	private Class<T> ground;

	public ReflectableTreeItem(String value, Node graphic, Class<T> clazz) {
		super(value, graphic);
		this.ground = clazz;
	}

	public Class<T> getGroundClass() {
		return ground;
	}

	public T createGround() throws InstantiationException,
			IllegalAccessException {
		return ground.newInstance();
	}
}
