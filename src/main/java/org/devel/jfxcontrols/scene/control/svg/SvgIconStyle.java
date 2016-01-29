package org.devel.jfxcontrols.scene.control.svg;

import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableStringProperty;
import javafx.css.StyleConverter;
import javafx.css.StyleableProperty;
import javafx.css.StyleableStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Labeled;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

/**
 * Styles a {@link Labeled} with an svg icon. The icon can be specified via css option -sx-svgpath.
 * <p>
 * A new Class that should be styled with this icon has to construct an object of this type and add the result of {@link
 * SvgIconStyle#cssMetaData} to its {@link javafx.scene.control.Control#getControlCssMetaData()}.
 */
public class SvgIconStyle {

  private static final String SX_SVG_PATH = "-sx-svgpath";

  private final StyleableStringProperty svgIconPath;
  private final CssMetaData<Labeled, String> cssMetaData;
  private final Consumer<Node> propConsumer;

  public SvgIconStyle(final Consumer<Node> propConsumer) {
    this.propConsumer = propConsumer;
    cssMetaData = createCssMetaData();
    svgIconPath = new SimpleStyleableStringProperty(cssMetaData);
    svgIconPath.addListener((observable, oldValue, newValue) -> loadIconFxml(newValue));
  }

  private CssMetaData<Labeled, String> createCssMetaData() {
    return new CssMetaData<Labeled, String>(
        SX_SVG_PATH, StyleConverter.getStringConverter()) {
      @Override
      public boolean isSettable(final Labeled styleable) {
        return svgIconPath == null || !svgIconPath.isBound();
      }

      @Override
      public StyleableProperty<String> getStyleableProperty(final Labeled styleable) {
        return svgIconPath;
      }
    };
  }

  private void loadIconFxml(final String iconFxmlPath) {
    if (iconFxmlPath != null) {
      final URL location = SvgIconStyle.class.getResource(iconFxmlPath);

      final FXMLLoader fxmlLoader = new FXMLLoader(location);
      try {
        propConsumer.accept(fxmlLoader.load());
      } catch (final IOException ioEx) {
        throw new RuntimeException("Could not load svg icons from path: " + svgIconPath, ioEx);
      }
    }
  }

  public CssMetaData<Labeled, String> getCssMetaData() {
    return cssMetaData;
  }
}
