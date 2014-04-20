/**
 * 
 */
package org.devel.jfxcontrols.scene.control.tv;

import java.util.HashMap;
import java.util.Map;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import org.devel.jfxcontrols.animation.Expand;
import org.devel.jfxcontrols.animation.Fling;
import org.devel.jfxcontrols.animation.RowAdjust;
import org.devel.jfxcontrols.animation.View;

import com.sun.javafx.scene.control.skin.VirtualFlow;

/**
 * @author stefan.illgen
 *
 */
public class TreeTableViewSkin<T>
	extends
		com.sun.javafx.scene.control.skin.TreeTableViewSkin<T> implements View<Event> {

	private AdjustableFlow<T, TreeTableRow<T>> adjustableFlow;

	public TreeTableViewSkin(final TreeTableView<T> treeTableView) {
		super(treeTableView);
		initialize();
	}

	/*
	 * Create a custom flow.
	 */
	@Override
	protected VirtualFlow<TreeTableRow<T>> createVirtualFlow() {
		adjustableFlow = new AdjustableFlow<T, TreeTableRow<T>>(getSkinnable().expandedItemCountProperty(),
																15,
																50);
		return adjustableFlow;
	}

	/*
	 * Remove discolure arrow.
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

	// ############### View ##########

	// private List<Command<? extends Command.Action<?>, ? extends Receiver>>
	// commands;

	private RowAdjust<T, TreeTableRow<T>> rowAdjust;
	private Fling fling;
	private Expand<T> expand;

	private void initialize() {
		// commands
		rowAdjust = new RowAdjust<T, TreeTableRow<T>>(adjustableFlow);
		// fling = new Fling();
		// expand = new Expand<T>();
		// event handler + filter
		addEventFilter(null);
		addEventHandler(null);
	}

	@Override
	public void addEventHandler(Map<EventType<Event>, EventHandler<Event>> eventHandler) {

	}

	@Override
	@SuppressWarnings({
		"rawtypes", "unchecked"
	})
	public void addEventFilter(Map<EventType<Event>, EventHandler<Event>> eventHandler) {

		TreeTableView<T> ttv = getSkinnable();

		HashMap<EventType<? extends Event>, EventHandler<? extends Event>> filters = new HashMap<EventType<? extends Event>, EventHandler<? extends Event>>() {
			private static final long serialVersionUID = -3419029735075202493L;
			{
				put(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if (!rowAdjust.execute(RowAdjust.Action.PRESS.mouseY(event.getY())
																	 .animate(true))) {
							event.consume();
						}
					}
				});
				put(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {
						if (!rowAdjust.execute(RowAdjust.Action.DRAG.mouseY(event.getY()))) {
							event.consume();
						}
					}
				});

				put(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if (!rowAdjust.execute(RowAdjust.Action.RELEASE.mouseY(event.getY())
																	   .animate(true))) {
							event.consume();
						}
					}
				});

				put(MouseEvent.MOUSE_CLICKED, (event) -> {
					event.consume();
				});
				put(MouseEvent.MOUSE_MOVED, (event) -> {
					event.consume();
				});
				put(MouseEvent.MOUSE_ENTERED, (event) -> {
					event.consume();
				});
				put(MouseEvent.MOUSE_EXITED, (event) -> {
					event.consume();
				});
				put(MouseEvent.MOUSE_ENTERED_TARGET, (event) -> {
					event.consume();
				});
				put(MouseEvent.MOUSE_EXITED_TARGET, (event) -> {
					event.consume();
				});
				put(ScrollEvent.ANY, (event) -> {
					event.consume();
				});
				put(KeyEvent.ANY, (event) -> {
					event.consume();
				});
			}
		};

		for (EventType eventType : filters.keySet()) {
			ttv.addEventFilter(eventType, filters.get(eventType));
		}
	}

}
