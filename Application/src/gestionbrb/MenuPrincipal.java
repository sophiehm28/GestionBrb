package gestionbrb;

import java.io.IOException;
import java.sql.SQLException;

import gestionbrb.vue.MenuPrincipalControleur;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MenuPrincipal extends Application {
	public static String[] arg;
	private Stage primaryStage;

	public MenuPrincipal() {
	}

	@Override
	public void start(Stage primaryStage){
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Menu Principal");
		afficheTable();
		
	}

	public void afficheTable() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MenuPrincipal.class.getResource("vue/MenuPrincipal.fxml"));
			AnchorPane tablesOverview = (AnchorPane) loader.load();
			Scene scene = new Scene(tablesOverview);
			primaryStage.setScene(scene);
			primaryStage.show();

			
			 MenuPrincipalControleur controller = loader.getController();
			 controller.setMainApp(this);
			 

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		arg = args;
		launch(args);
		
	}
}
