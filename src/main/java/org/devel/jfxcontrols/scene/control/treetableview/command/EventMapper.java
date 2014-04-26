/**
 * 
 */
package org.devel.jfxcontrols.scene.control.treetableview.command;

import java.util.Map;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 * 
 * @author stefan.illgen
 *
 * @param <C>
 * @param <A>
 * @param <R>
 */
@SuppressWarnings({
	"unchecked", "rawtypes"
})
public abstract class EventMapper<C extends Command<A, R>, A extends Command.Action<A>, R extends Receiver> {

	private C command;
	private Control control;
	private Map<EventType, EventHandler> all;

	public EventMapper(Control control, C command) {
		this.control = control;
		this.command = command;
	}

	public Node getSource() {
		return control;
	}

	protected <E extends MouseEvent> void addMouseFilter(Map<EventType<E>, A> filter) {
		for (EventType<E> eventType : filter.keySet()) {
			control.addEventFilter(eventType, (E event) -> {
				if (!command.execute(buildMouseAction(filter.get(eventType), event))) {
					event.consume();
				}
			});
		}
	}

	protected <E extends MouseEvent> void addMouseHandler(Map<EventType<E>, A> handler) {
		for (EventType<E> eventType : handler.keySet()) {
			control.addEventHandler(eventType, (E event) -> {
				if (!command.execute(buildMouseAction(handler.get(eventType), event))) {
					event.consume();
				}
			});
		}
	}

	protected void addScrollFilter(Map<EventType<ScrollEvent>, A> filter) {
		for (EventType<ScrollEvent> eventType : filter.keySet()) {
			control.addEventFilter(eventType, (ScrollEvent event) -> {
				if (!command.execute(buildScrollAction(filter.get(eventType), event))) {
					event.consume();
				}
			});
		}
	}

	protected void addScrollHandler(Map<EventType<ScrollEvent>, A> handler) {
		for (EventType<ScrollEvent> eventType : handler.keySet()) {
			control.addEventHandler(eventType, (ScrollEvent event) -> {
				if (!command.execute(buildScrollAction(handler.get(eventType), event))) {
					event.consume();
				}
			});
		}
	}

	protected void addKeyFilter(Map<EventType<KeyEvent>, A> keyFilter) {
		for (EventType<KeyEvent> eventType : keyFilter.keySet()) {
			control.addEventFilter(eventType, (KeyEvent event) -> {
				if (!command.execute(buildKeyAction(keyFilter.get(eventType), event))) {
					event.consume();
				}
			});
		}
	}

	protected void addKeyHandler(Map<EventType<KeyEvent>, A> handler) {
		for (EventType<KeyEvent> eventType : handler.keySet()) {
			control.addEventHandler(eventType, (KeyEvent event) -> {
				if (!command.execute(buildKeyAction(handler.get(eventType), event))) {
					event.consume();
				}
			});
		}
	}

	public void unregisterAll() {
		for (EventType eventType : all.keySet()) {
			control.removeEventFilter(eventType, all.get(eventType));
		}
	}

	public abstract <E extends MouseEvent> A buildMouseAction(A action, E event);

	public abstract A buildScrollAction(A action, ScrollEvent event);

	public abstract A buildKeyAction(A action, KeyEvent event);
}
