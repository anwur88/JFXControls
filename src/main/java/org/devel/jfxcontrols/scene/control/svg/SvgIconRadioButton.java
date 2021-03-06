package org.devel.jfxcontrols.scene.control.svg;

import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.scene.control.RadioButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link RadioButton} whose graphic is a svg which has been converted to fxml. When svgIcoPath is set, this button
 * will set its graphic to the specified icons which must be an fxml file.
 * <p>
 * The svgIconPath can be set via css option: -sx-svgpath.
 */
public class SvgIconRadioButton extends RadioButton {

  public static final String DFAULT_STYLE_CLASS = "svg-icon-radio-button";

  private final SvgIconStyle style;

  public SvgIconRadioButton() {
    style = new SvgIconStyle(this::setGraphic);

    getStyleClass().add(DFAULT_STYLE_CLASS);
  }

  @Override
  public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
    final List<CssMetaData<? extends Styleable, ?>> buttonCssMetaData = new ArrayList<>();
    buttonCssMetaData.addAll(super.getControlCssMetaData());
    buttonCssMetaData.add(style.getCssMetaData());
    return buttonCssMetaData;
  }

  @Override
  public String getUserAgentStylesheet() {
    return getClass().getResource("/org/devel/jfxcontrols/scene/control/svg/"
        + getClass().getSimpleName() + ".css").toExternalForm();
  }
}
