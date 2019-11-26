package gestionbrb.controleur;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import gestionbrb.IngredientsProduits;
import gestionbrb.model.Produit;
import gestionbrb.util.bddUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
 * @author Leo
 */



public class ModifierProduitsControleur {
	@FXML
	private TextField chNomProduit;
	@FXML
	private TextField chPrixProduit;
	@FXML
	private TextField chQuantiteProduit;
	@FXML
	private TextArea chDescription;
	@FXML
	private ObservableList<String> listeType = FXCollections.observableArrayList();
	@FXML
	private ChoiceBox<String> chChoixType;
	@FXML
	private VBox vb;
	private ArrayList<RadioButton> listebouton = new ArrayList<>();
	private ArrayList<String> listeNomIngredient = new ArrayList<>();
	private Produit produit;
	private Stage dialogStage;
	private boolean okClicked = false;
	IngredientsProduits mainApp;
	
	@FXML
	private void initialize() {
		try {
		Connection conn = bddUtil.dbConnect();
		ResultSet typeDB = conn.createStatement().executeQuery("select idType, nom from type_produit");
		ResultSet ingredientDB = conn.createStatement().executeQuery("select nomIngredient from ingredients");
		while (typeDB.next()) {
			listeType.add("ID: "+typeDB.getInt("idType")+" -> "+typeDB.getString("nom"));
		}
		chChoixType.setItems(listeType);
		while(ingredientDB.next()) {
			String nomIngredient = ingredientDB.getString("nomIngredient");
			listeNomIngredient.add(nomIngredient);
		}
		for (int i = 0; i < listeNomIngredient.size(); i++) {
			RadioButton radiobtn = new RadioButton(listeNomIngredient.get(i));
			listebouton.add(radiobtn);
			}
			vb.getChildren().addAll(listebouton);
		} catch (Exception e) {
			FonctionsControleurs.alerteErreur("Erreur!", "Erreur d'�xecution", "D�tails: " + e);
			e.printStackTrace();
		}
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	public void setMainApp(IngredientsProduits mainApp) {
		this.mainApp = mainApp;
	}
	
	public void setProduit(Produit produit) throws SQLException, ClassNotFoundException {
		this.produit = produit;
		chNomProduit.setText(produit.getNomProduit());
		chPrixProduit.setText(Float.toString(produit.getPrixProduit()));
		chQuantiteProduit.setText(Integer.toString(produit.getQuantiteProduit()));
		chDescription.setText(produit.getDescriptionProduit());
		chChoixType.setValue(produit.getType());
		
	}
	public boolean isOkClicked() {
		return okClicked;
	}
	
	List<String> liste = new ArrayList<>();
	@FXML
	public void actionValider() {
	String ingredients = "";
		try {
			for (int j = 0; j < listebouton.size(); j++) {
				if (listebouton.get(j).isSelected()) {
					String listeSelection = listebouton.get(j).toString(); // recupere la valeur du bouton
					String ingSplit[] = listeSelection.split("'"); // extrait le nom de l'ingredient
					liste.add(ingSplit[1]); // rajoute cet ingredient � une liste
					ingredients = String.join(", ", liste); // stocke dans une variable la liste des ingredients s�par� par une virgule
				}
			}
			if (estValide()) {
				produit.setNomProduit(chNomProduit.getText());
				produit.setPrixProduit(Float.parseFloat(chPrixProduit.getText()));
				produit.setQuantiteProduit(Integer.parseInt(chQuantiteProduit.getText()));
				produit.setDescriptionProduit(chDescription.getText());
				produit.setType(chChoixType.getValue());
				produit.setIngredients(ingredients);
				okClicked = true;
				dialogStage.close();
			}
		} catch (Exception e) {
			FonctionsControleurs.alerteErreur("Erreur!", "Erreur d'�xecution", "D�tails: "+e);
			e.printStackTrace();
		}
	}
	@FXML
	private void actionAnnuler() {
		dialogStage.close();
	}
	
	public boolean estValide() {
		String erreurMsg = "";

		if (chQuantiteProduit.getText() == null || chQuantiteProduit.getText().length() == 0) {
			erreurMsg += "Veuillez remplir la quantit� du produit\n";
		} else {
			try {
				Integer.parseInt(chQuantiteProduit.getText());
			} catch (NumberFormatException e) {
				erreurMsg += "Erreur! Le champ Quantit� n'accepte que les nombres\n";
			}
		}
		
		if (chPrixProduit.getText() == null || chPrixProduit.getText().length() == 0) {
			erreurMsg += "Veuillez remplir le prix du produit\n";
		} else {
			try {
				Float.parseFloat(chPrixProduit.getText());
			} catch (NumberFormatException e) {
				erreurMsg += "Erreur! Le champ prix n'accepte que les nombres\n";
			}
		}
		if (chNomProduit.getText() == null || chNomProduit.getText().length() == 0) {
			erreurMsg += "Veuillez remplir le nom du produit\n";
			}
		
		if (chChoixType.getValue() == null) {
			erreurMsg += "Veuillez saisir le type du produit\n";
			}
		
		if (chDescription.getText() == null || chDescription.getText().length() == 0) {
			erreurMsg += "Veuillez d�crire le produit\n";
			}
		if (erreurMsg.length() == 0) {
			return true;
		} else {
			// Affiche un message d'erreur
			FonctionsControleurs.alerteErreur("Entr�e incorrecte", "Corrigez les erreurs suivantes pour pouvoir modifier les informations",
					erreurMsg);

			return false;
		}
	}
}