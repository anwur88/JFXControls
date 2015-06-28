package org.devel.jfxcontrols.scene.control.compass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 * @author stefan.illgen
 */
public class TappasCompass extends Control {

    private static final String DEFAULT_STYLE_CLASS = "tappas-compass";

    private static final double DFEAULT_SCALE = 8.0d;
    private static final double DEFAULT_ORIENTATION = 0.0d;
    private static final double DEFAULT_TILT = 0.0d;
    private static final Pos DEFAULT_ALIGNMENT = Pos.CENTER;

    public TappasCompass() {
        getStyleClass().add(DEFAULT_STYLE_CLASS);
    }

    @Override
    public String getUserAgentStylesheet() {
        return getClass().getResource(this.getClass().getSimpleName() + ".css")
                         .toExternalForm();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TappasCompassSkin(this);
    }

    /**
     * The default alignment of children as within the compass control's width
     * and height.
     */
    public final ObjectProperty<Pos> alignmentProperty() {
        if (alignment == null) {
            alignment = new StyleableObjectProperty<Pos>(DEFAULT_ALIGNMENT) {
                @Override
                public void invalidated() {
                    requestLayout();
                }

                @Override
                public CssMetaData<TappasCompass, Pos> getCssMetaData() {
                    return StyleableProperties.ALIGNMENT;
                }

                @Override
                public Object getBean() {
                    return TappasCompass.this;
                }

                @Override
                public String getName() {
                    return "alignment";
                }
            };
        }
        return alignment;
    }

    private ObjectProperty<Pos> alignment;

    public final void setAlignment(Pos value) {
        alignmentProperty().set(value);
    }

    public final Pos getAlignment() {
        return alignment == null ? DEFAULT_ALIGNMENT : alignment.get();
    }

    /**
     * @return
     */
    public final DoubleProperty orientationProperty() {
        if (orientation == null) {
            orientation = new StyleableDoubleProperty(DEFAULT_ORIENTATION) {

                @Override
                public void invalidated() {
                    requestLayout();
                }

                @Override
                public String getName() {
                    return "orientation";
                }

                @Override
                public Object getBean() {
                    return TappasCompass.this;
                }

                @Override
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.ORIENTATION;
                }
            };
        }
        return orientation;
    }

    public double getOrientation() {
        return orientation == null ? 0.0d : orientation.get();
    }

    private DoubleProperty orientation;

    public void setOrientation(double orientation) {
        this.orientationProperty().set(orientation);
    }

    /**
     * @return
     */
    public final DoubleProperty tiltProperty() {
        if (tilt == null) {
            tilt = new StyleableDoubleProperty(DEFAULT_TILT) {

                @Override
                public void invalidated() {
                    requestLayout();
                }

                @Override
                public String getName() {
                    return "tilt";
                }

                @Override
                public Object getBean() {
                    return TappasCompass.this;
                }

                @Override
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.TILT;
                }
            };
        }
        return tilt;
    }

    private DoubleProperty tilt;

    public void setTilt(double tilt) {
        this.tiltProperty().set(tilt);
    }

    public double getTilt() {
        return tilt == null ? DEFAULT_TILT : tilt.get();
    }

    /**
     * @return
     */
    public final DoubleProperty scaleProperty() {
        if (scale == null) {
            scale = new StyleableDoubleProperty(DFEAULT_SCALE) {

                @Override
                public void invalidated() {
                    requestLayout();
                }

                @Override
                public String getName() {
                    return "scale";
                }

                @Override
                public Object getBean() {
                    return TappasCompass.this;
                }

                @Override
                public CssMetaData<? extends Styleable, Number> getCssMetaData() {
                    return StyleableProperties.SCALE;
                }
            };
        }
        return scale;
    }

    private DoubleProperty scale;

    public double getScale() {
        return scale == null ? DFEAULT_SCALE : scale.get();
    }

    public void setScale(double scale) {
        this.scaleProperty().set(scale);
    }

    /***************************************************************************
     * * Stylesheet Handling * *
     **************************************************************************/

    /**
     * Super-lazy instantiation pattern from Bill Pugh.
     * 
     * @treatAsPrivate implementation detail
     */
    @SuppressWarnings("unchecked")
    private static class StyleableProperties {

        protected static final CssMetaData<TappasCompass, Number> ORIENTATION =
            new CssMetaData<TappasCompass, Number>("-tappas-compass-orientation",
                                                   StyleConverter.getSizeConverter(),
                                                   0.0d) {

                @Override
                public boolean isSettable(TappasCompass node) {
                    return node.orientation == null || !node.orientation.isBound();
                }

                @Override
                public StyleableProperty<Number> getStyleableProperty(TappasCompass node) {
                    return (StyleableProperty<Number>) node.orientationProperty();
                }
            };

        protected static final CssMetaData<TappasCompass, Number> TILT =
            new CssMetaData<TappasCompass, Number>("-tappas-compass-tilt",
                                                   StyleConverter.getSizeConverter(),
                                                   DEFAULT_TILT) {

                @Override
                public boolean isSettable(TappasCompass node) {
                    return node.tilt == null || !node.tilt.isBound();
                }

                @Override
                public StyleableProperty<Number> getStyleableProperty(TappasCompass node) {
                    return (StyleableProperty<Number>) node.tiltProperty();
                }
            };

        protected static final CssMetaData<TappasCompass, Number> SCALE =
            new CssMetaData<TappasCompass, Number>("-tappas-compass-scale",
                                                   StyleConverter.getSizeConverter(),
                                                   DFEAULT_SCALE) {

                @Override
                public boolean isSettable(TappasCompass node) {
                    return node.scale == null || !node.scale.isBound();
                }

                @Override
                public StyleableProperty<Number> getStyleableProperty(TappasCompass node) {
                    return (StyleableProperty<Number>) node.scaleProperty();
                }
            };

        private static final CssMetaData<TappasCompass, Pos> ALIGNMENT =
            new CssMetaData<TappasCompass, Pos>("-tappas-compass-alignment",
                                                (StyleConverter<?, Pos>) StyleConverter.<Pos> getEnumConverter(Pos.class),
                                                DEFAULT_ALIGNMENT) {

                @Override
                public boolean isSettable(TappasCompass node) {
                    return node.alignment == null || !node.alignment.isBound();
                }

                @Override
                public StyleableProperty<Pos> getStyleableProperty(TappasCompass node) {
                    return (StyleableProperty<Pos>) node.alignmentProperty();
                }
            };

        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables =
                new ArrayList<CssMetaData<? extends Styleable, ?>>(Control.getClassCssMetaData());
            styleables.add(ALIGNMENT);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    /**
     * @return The CssMetaData associated with this class, which may include the
     *         CssMetaData of its super classes.
     * @since JavaFX 8.0
     */
    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

}
