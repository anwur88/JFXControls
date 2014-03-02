/**
 * 
 */
package org.devel.jfxcontrols.sample;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import org.devel.jfxcontrols.resource.ImageRegistry;
import org.devel.jfxcontrols.resource.Images;
import org.devel.jfxcontrols.scene.control.Aggregator;
import org.devel.jfxcontrols.scene.control.CircleCheckBox;
import org.devel.jfxcontrols.scene.control.CircleCheckBoxSkin;
import org.devel.jfxcontrols.scene.control.ImageCropper;
import org.devel.jfxcontrols.scene.control.ReflectableTreeItem;
import org.devel.jfxcontrols.scene.control.RotatableCheckBox;
import org.devel.jfxcontrols.scene.control.skin.RotatableCheckBoxSkin;
import org.devel.jfxcontrols.scene.layout.Router;

/**
 * @author stefan.illgen
 * 
 */
public class JFXShowCase extends AnchorPane implements Initializable {

	@FXML
	private ImageView logoImageView;

	@FXML
	private Text logoText;

	@FXML
	private Accordion detailsAccordeon;

	@FXML
	private TitledPane exampleTitledPane;

	@FXML
	private TreeView<String> masterTreeView;

	@FXML
	private TitledPane scTitledPane;

	@FXML
	private AnchorPane exampleContainer;

	@FXML
	private TextArea scTextArea;

	public JFXShowCase() {
		loadFXML();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void initialize(URL location, ResourceBundle resources) {

		// root
		TreeItem<String> root = new TreeItem<String>("All Controls",
				ImageRegistry.getImageView(Images.LOGO_16));
		masterTreeView.setRoot(root);
		root.getChildren().addAll(getAllControls());

		// master 2 details: selection binding
		masterTreeView.setCellFactory(treeView -> {
			// on click on the master
				final TreeCell<String> treeCell = new TextFieldTreeCell<String>();
				treeCell.setOnMouseClicked(event -> {
					// get tree item and load details view with created
					// grounding
					try {
						ReflectableTreeItem<? extends Node> item = (ReflectableTreeItem<? extends Node>) ((TreeCell<String>) event
								.getSource()).getTreeItem();

						if (item.getGroundClass() == CircleCheckBox.class)
							loadDetails(createCircleCheckBox());
						else if (item.getGroundClass() == RotatableCheckBox.class)
							loadDetails(createRotatableCheckBox());
						else
							loadDetails(item.createGround());
					} catch (InstantiationException | IllegalAccessException
							| ClassCastException e) {
						e.printStackTrace();
					}
				});

				return treeCell;
			});

	}

	private List<TreeItem<String>> getAllControls() {
		return new ArrayList<TreeItem<String>>() {
			private static final long serialVersionUID = -9213576968159746189L;
			{
				add(createTreeItem(Router.class));
				add(createTreeItem(ImageCropper.class));
				add(createTreeItem(Aggregator.class));
				add(createTreeItem(CircleCheckBox.class));
				add(createTreeItem(RotatableCheckBox.class));
			}
		};
	}

	private <T extends Node> TreeItem<String> createTreeItem(Class<T> clazz) {
		return new ReflectableTreeItem<T>(clazz.getSimpleName(),
				ImageRegistry.getImageView(Images.LOGO_16), clazz);
	}

	private void loadFXML() {

		URL url = getClass().getResource(getClass().getSimpleName() + ".fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(url);

		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	private void loadDetails(Node node) {

		// load example
		exampleContainer.getChildren().clear();
		exampleContainer.getChildren().add(node);
		AnchorPane.clearConstraints(node);
		AnchorPane.setTopAnchor(node, 0.0);
		AnchorPane.setRightAnchor(node, 0.0);
		AnchorPane.setLeftAnchor(node, 0.0);
		AnchorPane.setBottomAnchor(node, 0.0);
		exampleTitledPane.setExpanded(true);

		// TODO stefan - load example instantiation code
		// // reflect byte code
		// String code = "";
		// try {
		// String clazzName = node.getClass().getSimpleName();
		// URI fileURI = node.getClass().getResource("./" + clazzName +
		// ".class").toURI();
		// code = FileUtils.readFileToString(FileUtils.getFile(new
		// File(fileURI)),
		// Charset.forName("UTF-8"));
		// } catch (IOException | URISyntaxException e) {
		// e.printStackTrace();
		// code = e.getMessage();
		// }
		// scTextArea.setText(code);
	}

	private CircleCheckBox createCircleCheckBox() {

		CircleCheckBox ccb = new CircleCheckBox();
		ccb.setSkin(new CircleCheckBoxSkin(ccb));

		return ccb;
	}

	private RotatableCheckBox createRotatableCheckBox() {
		
		RotatableCheckBox rcb = new RotatableCheckBox();
		rcb.setSkin(new RotatableCheckBoxSkin(rcb));
		
		return rcb;
	}
}
