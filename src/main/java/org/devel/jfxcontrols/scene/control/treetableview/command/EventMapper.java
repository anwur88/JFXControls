/**
 * 
 */
package org.devel.jfxcontrols.scene.control.treetableview.command;

import java.util.HashMap;
import java.util.Map;

import javafx.event.EventHandler;
import javafx.event.EventType;
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
public class EventMapper<C extends Command<A, R>, A extends Command.Action<A>, R extends Receiver> {

	private C command;
	private Control control;

	public EventMapper(Control control, C command) {
		this.control = control;
		this.command = command;
	}

	public Map<EventType<? extends ScrollEvent>, EventHandler<? extends ScrollEvent>> scrollFilters = new HashMap<EventType<? extends ScrollEvent>, EventHandler<? extends ScrollEvent>>() {
		private static final long serialVersionUID = -5431997953022846187L;
		{
			put(ScrollEvent.ANY, (event) -> {
				event.consume();
			});
		}
	};

	public Map<EventType<? extends KeyEvent>, EventHandler<? extends KeyEvent>> KeyFilters = new HashMap<EventType<? extends KeyEvent>, EventHandler<? extends KeyEvent>>() {
		private static final long serialVersionUID = 660491684221683820L;
		{
			put(KeyEvent.ANY, (event) -> {
				event.consume();
			});
		}
	};

	public <E extends MouseEvent> void addMouseFilters(Map<EventType<E>, A> filters) {
		for (EventType<E> eventType : filters.keySet()) {
			addMouseEventFilter(eventType, filters.get(eventType));
		}
	}

	public <E extends MouseEvent> void addMouseEventFilter(EventType<E> eventType,
														   A action) {
		control.addEventFilter(eventType, (event) -> {
			if (!command.execute(action.y(event.getY()))) {
				event.consume();
			}
		});
	}
}
