/**
 * 
 */
package org.devel.jfxcontrols.scene.control.compass;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.transform.Rotate;

/**
 * @author stefan.illgen
 */
public class TappasCompassSkin extends SkinBase<TappasCompass> {

    // private static final Logger LOGGER =
    // LoggerFactory.getLogger(TappasCompassSkin.class);
    private static final URL FXML_RESOURCE_PATH = TappasCompass.class.getResource(TappasCompass.class.getSimpleName()
        + ".fxml");

    private SubScene compassScene;

    /**
     * @param control
     */
    public TappasCompassSkin(TappasCompass control) {
        super(control);
        try {
            getSkinnable().setBackground(null);
            createScene(loadFXML());
        } catch (IOException
                 |
                 IllegalStateException e) {
            // LOGGER.error("Failed to create skin", e);
        }
        getSkinnable().requestLayout();
    }

    protected StackPane loadFXML() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FXML_RESOURCE_PATH);
        fxmlLoader.setController(new TappasCompassController());
        return fxmlLoader.load();
    }

    protected void createScene(StackPane root) {
        compassScene = new SubScene(root,
                                    root.prefWidth(-1),
                                    root.prefHeight(-1),
                                    true,
                                    SceneAntialiasing.BALANCED);
        compassScene.heightProperty()
                    .bind(getSkinnable().scaleProperty()
                                        .multiply(compassScene.getHeight()));
        compassScene.widthProperty()
                    .bind(getSkinnable().scaleProperty()
                                        .multiply(compassScene.getWidth()));
        getChildren().add(compassScene);
    }

    @Override
    protected void layoutChildren(double contentX,
                                  double contentY,
                                  double contentWidth,
                                  double contentHeight) {
        super.layoutChildren(contentX, contentY, contentWidth, contentHeight);

        layoutInArea(compassScene,
                     0,
                     0,
                     getSkinnable().prefWidth(-1),
                     getSkinnable().prefHeight(-1),
                     -1,
                     getSkinnable().getAlignment().getHpos(),
                     getSkinnable().getAlignment().getVpos());

    }

    /**
     * @author stefan.illgen
     */
    public class TappasCompassController implements Initializable {

        @FXML
        private StackPane compassRoot;

        @FXML
        private Group north;

        @FXML
        private PhongMaterial phongMaterial;

        @FXML
        private Group needle;

        @Override
        public void initialize(URL location, ResourceBundle resources) {

            compassRoot.setBackground(null);

            // scale
            compassRoot.scaleXProperty().bind(getSkinnable().scaleProperty());
            compassRoot.scaleYProperty().bind(getSkinnable().scaleProperty());
            compassRoot.scaleZProperty().bind(getSkinnable().scaleProperty());

            // tilt
            Rotate tilt = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
            tilt.angleProperty().bind(getSkinnable().tiltProperty().multiply(-1));
            tilt.pivotYProperty().bind(compassRoot.heightProperty().divide(2.0));
            compassRoot.getTransforms().add(tilt);

            // orientation
            final Rotate orientation = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
            orientation.angleProperty().bind(getSkinnable().orientationProperty()
                                                           .multiply(-1));
            needle.getTransforms().add(orientation);
        }
    }
}
