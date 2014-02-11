/**
 * 
 */
package org.devel.jfxcontrols.sample;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Callback;

import org.devel.jfxcontrols.resource.ImageRegistry;
import org.devel.jfxcontrols.resource.Images;
import org.devel.jfxcontrols.scene.layout.SearchRoutePane;

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
	public void initialize(URL location, ResourceBundle resources) {

		// root
		TreeItem<String> root = new TreeItem<String>("All Controls",
				ImageRegistry.getImageView(Images.LOGO_16));
		masterTreeView.setRoot(root);
		root.getChildren().addAll(getAllControls());

		// m2d: selection binding
		masterTreeView
				.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {

					@Override
					public TreeCell<String> call(TreeView<String> tv) {

						final TreeCell<String> treeCell = new TextFieldTreeCell<String>();
						treeCell.setOnMouseClicked(new EventHandler<MouseEvent>() {
							@SuppressWarnings("unchecked")
							@Override
							public void handle(MouseEvent event) {
								TreeItem<String> item = ((TreeCell<String>) event
										.getSource()).getTreeItem();
								if (item instanceof ReflectiveTreeItem) {
									try {
										loadDetails(((ReflectiveTreeItem<? extends Node>) item)
												.createGround());
									} catch (InstantiationException | IllegalAccessException e) {
										e.printStackTrace();
									}
								}
							}
						});

						return treeCell;
					}
				});

	}

	private List<TreeItem<String>> getAllControls() {

		List<TreeItem<String>> result = new ArrayList<TreeItem<String>>();

		// return new Aggregator();
		// return new ImageCropper()

		ReflectiveTreeItem<SearchRoutePane> mapViewMenuItem = new ReflectiveTreeItem<SearchRoutePane>(
				"Map View", ImageRegistry.getImageView(Images.LOGO_16),
				SearchRoutePane.class);
		result.add(mapViewMenuItem);

		result.add(new TreeItem<String>("Image Cropper", ImageRegistry
				.getImageView(Images.LOGO_16)));
		result.add(new TreeItem<String>("Button Aggregator", ImageRegistry
				.getImageView(Images.LOGO_16)));

		return result;
	}

	public class ReflectiveTreeItem<T extends Node> extends TreeItem<String> {

		private Class<T> ground;

		public ReflectiveTreeItem(String value, Node graphic, Class<T> clazz) {
			super(value, graphic);
			this.ground = clazz;
		}

		public Class<T> getGroundClass() {
			return ground;
		}
		
		public T createGround() throws InstantiationException, IllegalAccessException {
			return ground.newInstance();
		}
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

}
