/**
 * 
 */
package org.devel.jfxcontrols.scene.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Labeled;

import com.sun.javafx.css.converters.SizeConverter;

/**
 * Example 4 the definition of stylable properties using the JavafX 8 CSS API.
 * 
 * @author stefan.illgen
 * 
 */
public class RotatableCheckBox extends CheckBox {

	public RotatableCheckBox() {
		super();
		initiate();		
	}

	public RotatableCheckBox(String text) {
		super(text);
		initiate();
	}

	private void initiate() {
		setupCSS();
		bind();
		setNeedsLayout(true);
	}

	// ### Property Configuration

	private void bind() {
		 rotateProperty().bindBidirectional(rotate1Property());
	}

	private DoubleProperty rotate1;

	public DoubleProperty rotate1Property() {
		if (rotate1 == null) {
			rotate1 = new StyleableDoubleProperty(0d) {

				@Override
				public CssMetaData<? extends Styleable, Number> getCssMetaData() {
					return StyleableProperties.ROTATE;
				}

				@Override
				public Object getBean() {
					return this;
				}

				@Override
				public String getName() {
					return "rotate1";
				}

			};
		}
		return rotate1;
	}

	public Double getRotate1() {
		return rotate1Property().get();
	}

	public void setRotate1(Double rotate1) {
		this.rotate1.add(rotate1);
	}

	public boolean canSetRotate1() {
		return (rotate1 == null) || !rotate1.isBound();
	}

	// ### CSS Configuration

	/*
	 * Set up style class and publish Skin.
	 */

	private static final String DEFAULT_STYLE_CLASS = "rotatable-check-box";

	private void setupCSS() {
		/*
		 * Do not set but add this style class in order to reuse given css
		 * constraints for check-box.
		 */
		getStyleClass().addAll(DEFAULT_STYLE_CLASS);
	}

	// @Override
	// protected Skin<?> createDefaultSkin() {
	// return new RotatableCheckBoxSkin(this);
	// }

	@Override
	public String getUserAgentStylesheet() {
		return getClass().getResource(this.getClass().getSimpleName() + ".css")
				.toExternalForm();
	}

	/*
	 * Define and publish CSS styleable properties (i.e. CSS meta meta data).
	 */

	private static final List<CssMetaData<? extends Styleable, ?>> CSS_META_DATA;
	// static initiation
	static {
		final List<CssMetaData<? extends Styleable, ?>> metaData = new ArrayList<CssMetaData<? extends Styleable, ?>>(
				Labeled.getClassCssMetaData());
		Collections.addAll(metaData, StyleableProperties.ROTATE);
		CSS_META_DATA = Collections.unmodifiableList(metaData);
	}

	private static class StyleableProperties {

		// ROTATE
		private static final CssMetaData<RotatableCheckBox, Number> ROTATE = new CssMetaData<RotatableCheckBox, Number>(
				"-dev-rotate1", SizeConverter.getInstance(), 0.0) {

			@Override
			public boolean isSettable(RotatableCheckBox node) {
				return node.canSetRotate1();
			}

			@SuppressWarnings("unchecked")
			@Override
			public StyleableProperty<Number> getStyleableProperty(
					RotatableCheckBox node) {
				return (StyleableProperty<Number>) node.rotate1Property();
			}
		};

	}

	public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
		return CSS_META_DATA;
	}

	/*
	 * Main method: Define and publish CSS styleable properties (i.e. CSS meta
	 * meta data).
	 * 
	 * @see javafx.scene.control.Labeled#getControlCssMetaData()
	 */
	@Override
	public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
		return getClassCssMetaData();
	}

}
