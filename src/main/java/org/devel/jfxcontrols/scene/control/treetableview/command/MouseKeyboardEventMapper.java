package org.devel.jfxcontrols.scene.control.treetableview.command;

import java.util.HashMap;
import java.util.Map;

import javafx.event.EventType;
import javafx.scene.control.Control;
import javafx.scene.control.IndexedCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import org.devel.jfxcontrols.scene.control.treetableview.command.RowAdjust.Action;

public class MouseKeyboardEventMapper {

	/**
	 * 
	 * @author stefan.illgen
	 *
	 * @param <T>
	 * @param <I>
	 */
	public static class RowAdjustEventMapper<T, I extends IndexedCell<T>>
		extends
			EventMapper<RowAdjust<T, I>, RowAdjust.Action, Adjustable<T, I>> {

		public Map<EventType<MouseEvent>, RowAdjust.Action> mouseFilter = new HashMap<EventType<MouseEvent>, RowAdjust.Action>() {
			private static final long serialVersionUID = -9005856313771120088L;
			{
				put(MouseEvent.MOUSE_CLICKED, RowAdjust.Action.CONSUME_CLICKED);
				// put(MouseEvent.MOUSE_MOVED, RowAdjust.Action.CONSUME);
				// put(MouseEvent.MOUSE_ENTERED, RowAdjust.Action.CONSUME);
				// put(MouseEvent.MOUSE_EXITED, RowAdjust.Action.CONSUME);
				// put(MouseEvent.MOUSE_ENTERED_TARGET,
				// RowAdjust.Action.CONSUME);
				// put(MouseEvent.MOUSE_EXITED_TARGET,
				// RowAdjust.Action.CONSUME);
			}
		};

		public Map<EventType<MouseEvent>, RowAdjust.Action> mouseHandler = new HashMap<EventType<MouseEvent>, RowAdjust.Action>() {
			private static final long serialVersionUID = -9005856313771120088L;
			{
				put(MouseEvent.MOUSE_PRESSED, RowAdjust.Action.INIT);
				put(MouseEvent.MOUSE_DRAGGED, RowAdjust.Action.DRAG);
				put(MouseEvent.MOUSE_RELEASED, RowAdjust.Action.ADJUST_AFTER_DRAG);
			}
		};

		public Map<EventType<ScrollEvent>, Expand.Action> scrollFilter = new HashMap<EventType<ScrollEvent>, Expand.Action>() {
			private static final long serialVersionUID = -5431997953022846187L;
			{
				// put(ScrollEvent.ANY, Expand.Action.CONSUME);
			}
		};

		public Map<EventType<KeyEvent>, Expand.Action> keyFilter = new HashMap<EventType<KeyEvent>, Expand.Action>() {
			private static final long serialVersionUID = 660491684221683820L;
			{
				// put(KeyEvent.ANY, Expand.Action.CONSUME);
			}
		};

		public static <TS, IS extends IndexedCell<TS>>
			RowAdjustEventMapper<TS, IS>
			create(Control source, RowAdjust<TS, IS> target) {
			return new RowAdjustEventMapper<TS, IS>(source, target);
		}

		private RowAdjustEventMapper(Control source, RowAdjust<T, I> target) {
			super(source, target);
			addMouseFilter(mouseFilter);
			addMouseHandler(mouseHandler);
		}

		@Override
		public <E extends MouseEvent> Action buildMouseAction(Action action, E event) {
			return action.y(event.getY()).animate(true);
		}

		@Override
		public Action buildScrollAction(Action action, ScrollEvent event) {
			return action.y(event.getY()).animate(true);
		}

		@Override
		public Action buildKeyAction(Action action, KeyEvent event) {
			return action.animate(true);
		}

	}

	/**
	 * 
	 * @author stefan.illgen
	 *
	 * @param <T>
	 * @param <A>
	 */
	public static class ExpandEventMapper<T, A extends IndexedCell<T>>
		extends
			EventMapper<Expand<T, A>, Expand.Action, Expandable<T>> {

		public Map<EventType<MouseEvent>, Expand.Action> mouseFilter = new HashMap<EventType<MouseEvent>, Expand.Action>() {
			private static final long serialVersionUID = -9005856313771120088L;
			{
				// put(MouseEvent.MOUSE_PRESSED, Expand.Action.NONE);
				// put(MouseEvent.MOUSE_DRAGGED, Expand.Action.NONE);
				// put(MouseEvent.MOUSE_RELEASED, Expand.Action.NONE);
				// put(MouseEvent.MOUSE_CLICKED, Expand.Action.NONE);
				// put(MouseEvent.MOUSE_MOVED, Expand.Action.NONE);
				// put(MouseEvent.MOUSE_ENTERED, Expand.Action.NONE);
				// put(MouseEvent.MOUSE_EXITED, Expand.Action.NONE);
				// put(MouseEvent.MOUSE_ENTERED_TARGET, Expand.Action.NONE);
				// put(MouseEvent.MOUSE_EXITED_TARGET, Expand.Action.NONE);
			}
		};

		public Map<EventType<ScrollEvent>, Expand.Action> scrollFilter = new HashMap<EventType<ScrollEvent>, Expand.Action>() {
			private static final long serialVersionUID = -5431997953022846187L;
			{
				// put(ScrollEvent.ANY, Expand.Action.NONE);
			}
		};

		public Map<EventType<KeyEvent>, Expand.Action> keyFilter = new HashMap<EventType<KeyEvent>, Expand.Action>() {
			private static final long serialVersionUID = 660491684221683820L;
			{
				// put(KeyEvent.ANY, Expand.Action.NONE);
			}
		};

		public Map<EventType<MouseEvent>, Expand.Action> mouseHandler = new HashMap<EventType<MouseEvent>, Expand.Action>() {
			private static final long serialVersionUID = -9005856313771120088L;
			{
				// put(MouseEvent.MOUSE_PRESSED, Expand.Action.NONE);
				// put(MouseEvent.MOUSE_DRAGGED, Expand.Action.NONE);
				// put(MouseEvent.MOUSE_RELEASED, Expand.Action.NONE);
				put(MouseEvent.MOUSE_CLICKED, Expand.Action.EXPAND);
				// put(MouseEvent.MOUSE_MOVED, Expand.Action.NONE);
				// put(MouseEvent.MOUSE_ENTERED, Expand.Action.NONE);
				// put(MouseEvent.MOUSE_EXITED, Expand.Action.NONE);
				// put(MouseEvent.MOUSE_ENTERED_TARGET, Expand.Action.NONE);
				// put(MouseEvent.MOUSE_EXITED_TARGET, Expand.Action.NONE);
			}
		};

		public Map<EventType<ScrollEvent>, Expand.Action> scrollHandler = new HashMap<EventType<ScrollEvent>, Expand.Action>() {
			private static final long serialVersionUID = -5431997953022846187L;
			{
				// put(ScrollEvent.ANY, Expand.Action.NONE);
			}
		};

		public Map<EventType<KeyEvent>, Expand.Action> keyHandler = new HashMap<EventType<KeyEvent>, Expand.Action>() {
			private static final long serialVersionUID = 660491684221683820L;
			{
				// put(KeyEvent.ANY, Expand.Action.NONE);
			}
		};

		public static <TS, AS extends IndexedCell<TS>>
			ExpandEventMapper<TS, AS>
			create(Control source, Expand<TS, AS> target) {
			return new ExpandEventMapper<TS, AS>(source, target);
		}

		private ExpandEventMapper(Control source, Expand<T, A> target) {
			super(source, target);
			addMouseFilter(mouseFilter);
			addScrollFilter(scrollFilter);
			addKeyFilter(keyFilter);
			addMouseHandler(mouseHandler);
			addScrollHandler(scrollHandler);
			addKeyHandler(keyHandler);
		}

		@Override
		public <E extends MouseEvent>
			Expand.Action
			buildMouseAction(Expand.Action action, E event) {
			return action.y(event.getY())
						 .source(event.getSource())
						 .target(event.getTarget())
						 .count(event.getClickCount())
						 .animate(true);
		}

		@Override
		public Expand.Action buildScrollAction(Expand.Action action, ScrollEvent event) {
			return action.y(event.getY())
						 .source(event.getSource())
						 .target(event.getTarget())
						 .animate(true);
		}

		@Override
		public Expand.Action buildKeyAction(Expand.Action action, KeyEvent event) {
			return action.source(event.getSource())
						 .target(event.getTarget())
						 .animate(true);
		}

	}

}
