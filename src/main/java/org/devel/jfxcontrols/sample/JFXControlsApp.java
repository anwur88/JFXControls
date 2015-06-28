/**
 *
 */
package org.devel.jfxcontrols.sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.devel.jfxcontrols.conf.Properties;
import org.devel.jfxcontrols.resource.ImageRegistry;

/**
 * @author stefan.illgen
 */
public class JFXControlsApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        setup(stage);

        Scene scene = new Scene(getParent());
        scene.getStylesheets().add("/org/devel/jfxcontrols/sample/JFXControlsApp.css");
        stage.setScene(scene);
        stage.setTitle(getClass().getName());

        stage.show();

    }

    private void setup(Stage stage) {

        // conf
        Properties.init();

        // image registry
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (event.getEventType().equals(
                        WindowEvent.WINDOW_CLOSE_REQUEST)
                        && event.getTarget().equals(stage)) {
                    stage.removeEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST,
                            this);
                    ImageRegistry.instance().dispose();
                }

            }
        });
    }

    private Parent getParent() {
        return new JFXShowCase();
    }

}
