package org.devel.jfxcontrols.scene.control;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.loadui.testfx.Assertions.verifyThat;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import org.devel.jfxcontrols.conf.Properties;
import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

public class RotatableCheckBoxTest extends GuiTest {

	private AnchorPane root;
	
	@FXML
	private RotatableCheckBox myrotcheckbox;

	public RotatableCheckBoxTest() {
		super();
	}

	@Override
	protected Parent getRootNode() {
		
		URL url = getClass().getResource(getClass().getSimpleName() + ".fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(url);

		try {
			root = fxmlLoader.<AnchorPane>load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		
		return root;
	}

	@Before
	public void setup() {
		new Properties().load();
		try {
			super.setupStage();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * Does not cover setting correct view port on target property
	 */
	@Test
	public void rotationAnimationTest() {
		
		// Pre
		RotatableCheckBox rcheckbox = find("#myrotcheckbox", root);
		verifyThat(rcheckbox, notNullValue(RotatableCheckBox.class));
		
		// Action
		for (double i = 0.0; i < 360.0; i = i + 1.0) {
			final double degree = 0.0 + i;
			Platform.runLater(() -> {
				rcheckbox.setStyle("-dev-rotate1: "+ degree +";");
				rcheckbox.requestLayout();
			});
			waitUntil(rcheckbox.isNeedsLayout(), is(false), 30);
			// Post
			verifyThat(rcheckbox.getRotate1(), is(degree));
			verifyThat(rcheckbox.getRotate(), is(degree));
		}
	}

}
