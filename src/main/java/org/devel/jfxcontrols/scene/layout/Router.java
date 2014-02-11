package org.devel.jfxcontrols.scene.layout;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.StyleableProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import org.devel.jfxcontrols.scene.control.RouteMapView;
import org.devel.jfxcontrols.scene.control.skin.RouterSkin;

/**
 * Aggregation Example using just one {@link ObjectProperty} type.
 * 
 * + full properties of aggregated controls, without writing much of code
 * 
 * - encapsulation: router makes a lot of assumptions on its contents but does
 * not expose all of its controls and layouts, thus if someone wants 2 adapt
 * this control in another context, he has to check implementation details
 * 
 * - also FXML children will also be not possible
 * 
 * 
 * @author stefan.illgen
 * 
 */
public class Router extends Control {

	private static final String DEFAULT_STYLE_CLASS = "router";

	private ObjectProperty<RouteMapView> routeMapView;
	private StringProperty startLabel;
	private StringProperty startText;
	private StringProperty finishLabel;
	private StringProperty finishText;
	private StringProperty buttonText;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Router() {

		// setStyle(null);
		getStyleClass().setAll(DEFAULT_STYLE_CLASS);

		// focusTraversable is styleable through css. Calling
		// setFocusTraversable
		// makes it look to css like the user set the value and css will not
		// override. Initializing focusTraversable by calling applyStyle with
		// null
		// StyleOrigin ensures that css will be able to override the value.
		((StyleableProperty) focusTraversableProperty()).applyStyle(null,
				Boolean.FALSE);

	}

	public ObjectProperty<RouteMapView> routeMapViewProperty() {
		if(routeMapView == null)
			routeMapView = new SimpleObjectProperty<RouteMapView>();
		return routeMapView;
	}

	public RouteMapView getRouteMapView() {
		return routeMapViewProperty().get();
	}
	
	public void setRouteMapView(RouteMapView routeMapView) {
		routeMapViewProperty().set(routeMapView);
	}

	public StringProperty startLabelProperty() {
		if(startLabel == null)
			startLabel = new SimpleStringProperty();
		return startLabel;
	}

	public String getStartLabel() {
		return startLabelProperty().get();
	}

	public StringProperty startTextProperty() {
		if(startText== null)
			startText = new SimpleStringProperty();
		return startText;
	}

	public String getStartText() {
		return startTextProperty().get();
	}

	public StringProperty finishLabelProperty() {
		if(finishLabel == null)
			finishLabel = new SimpleStringProperty();
		return finishLabel;
	}

	public String getFinishLabel() {
		return finishLabelProperty().get();
	}

	public StringProperty finishTextProperty() {
		if(finishText == null)
			finishText = new SimpleStringProperty();
		return finishText;
	}

	public String getFinishText() {
		return finishTextProperty().get();
	}

	public StringProperty buttonProperty() {
		if(buttonText == null)
			buttonText = new SimpleStringProperty();
		return buttonText;
	}

	public String getButton() {
		return buttonProperty().get();
	}

	@Override
	protected Skin<?> createDefaultSkin() {
		return new RouterSkin(this);
	}

	/**
	 * Most Controls return true for focusTraversable, so Control overrides this
	 * method to return true, but Accordion returns false for focusTraversable's
	 * initial value; hence the override of the override. This method is called
	 * from CSS code to get the correct initial value.
	 * 
	 * @treatAsPrivate implementation detail
	 */
	@Deprecated
	@Override
	protected/* do not make final */Boolean impl_cssGetFocusTraversableInitialValue() {
		return Boolean.FALSE;
	}

}
