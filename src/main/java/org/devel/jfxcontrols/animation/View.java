package org.devel.jfxcontrols.animation;

import java.util.Map;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;

public interface View<T extends Event> {

	void addEventHandler(Map<EventType<T>, EventHandler<T>> eventHandler);

	void addEventFilter(Map<EventType<T>, EventHandler<T>> eventHandler);

}
