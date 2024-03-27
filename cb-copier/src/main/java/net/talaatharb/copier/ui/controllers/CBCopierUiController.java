package net.talaatharb.copier.ui.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.talaatharb.copier.config.HelperBeans;
import net.talaatharb.copier.facade.CBCopierFacade;

@Slf4j
@RequiredArgsConstructor
public class CBCopierUiController implements Initializable {

	private static final String EDIT_WINDOW_FXML = "../EditWindow.fxml";

	@FXML
	@Setter(value = AccessLevel.PACKAGE)
	private Button connectButton;

	@FXML
	@Setter(value = AccessLevel.PACKAGE)
	private Label connectionStatus;


	private final CBCopierFacade copierFacade;

	@FXML
	@Setter(value = AccessLevel.PACKAGE)
	private Button deleteButton;

	@FXML
	@Setter(value = AccessLevel.PACKAGE)
	private Button editButton;

	@FXML
	@Setter(value = AccessLevel.PACKAGE)
	private Button fetchButton;

	@FXML
	@Setter(value = AccessLevel.PACKAGE)
	private Button insertButton;

	private final ObjectMapper objectMapper;

	@FXML
	@Setter(value = AccessLevel.PACKAGE)
	private TextArea queryTextArea;

	@FXML
	@Setter(value = AccessLevel.PACKAGE)
	private TextArea resultTextArea;

	@FXML
	@Setter(value = AccessLevel.PACKAGE)
	private TextField filterTextField;

	@FXML
	@Setter(value = AccessLevel.PACKAGE)
	private Button upsertButton;

	private JsonNode result;

	public CBCopierUiController() {
		objectMapper = HelperBeans.buildObjectMapper();
		copierFacade = HelperBeans.buildCopierFacade(HelperBeans.buildConnectionService(),
				HelperBeans.buildCopierService());
	}

	@FXML
	void connect() {
		final boolean connectionResult = copierFacade.connect();
		if (connectionResult) {
			log.info("Connection Successful...");

			connectionStatus.setStyle("-fx-background-color: #00ff00");

			connectButton.setDisable(true);
			editButton.setDisable(true);

			deleteButton.setDisable(false);
			fetchButton.setDisable(false);
			insertButton.setDisable(false);
			upsertButton.setDisable(false);
		} else {
			log.info("Connection Failed...");
			connectionStatus.setStyle("-fx-background-color: #ff0000");
		}
	}

	@FXML
	void deleteWithFilter() {
		final String filter = filterTextField.getText();
		log.info("Delete data from destination DB using filter: " + filter);
		copierFacade.deleteWithFilter(filter);
	}

	@FXML
	void editConnection() {
		log.info("Edit connection");
		final FXMLLoader loader = new FXMLLoader(getClass().getResource(EDIT_WINDOW_FXML));
		Scene newScene;
		try {
			final Parent root = loader.load();
			newScene = new Scene(root);
		} catch (final IOException ex) {
			log.error(ex.getMessage());
			return;
		}

		final Stage editConnectionWindow = new Stage();
		editConnectionWindow.initOwner(null);
		editConnectionWindow.setScene(newScene);
		editConnectionWindow.showAndWait();
	}

	@FXML
	void fetchUsingQuery() throws IOException {
		log.info("Fetch data using the query");
		final String resultString = copierFacade.fetchUsingQuery(queryTextArea.getText());
		result = objectMapper
				.readTree(new ByteArrayInputStream(resultString.getBytes(StandardCharsets.UTF_8)));
		resultTextArea.setText(objectMapper.writeValueAsString(result));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		log.info("Initializing UI application Main window controller...");
	}

	@FXML
	void insert() {
		log.info("Inserting into destniation DB");
		copierFacade.insert(result);
	}

	@FXML
	void upsert() {
		log.info("Upsert data into destination DB");
		copierFacade.upsert(result);
	}

}
