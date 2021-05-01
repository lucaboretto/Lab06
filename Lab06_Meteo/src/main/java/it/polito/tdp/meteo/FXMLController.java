/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.meteo;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import it.polito.tdp.meteo.model.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	Model model;
	ObservableList<String> mesi;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxMese"
    private ChoiceBox<String> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="btnUmidita"
    private Button btnUmidita; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalcola"
    private Button btnCalcola; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaSequenza(ActionEvent event) {
    	String meseString = this.boxMese.getValue();
    	int mese = -1;
    	for(int i=0; i<mesi.size(); i++) {
    		if(mesi.get(i).equals(meseString))
    			mese = i+1;
    	}
    	this.txtResult.appendText(this.model.trovaSequenza(mese).toString());
    }

    @FXML
    void doCalcolaUmidita(ActionEvent event) {
    	String meseString = this.boxMese.getValue();
    	int mese = -1;
    	for(int i=0; i<mesi.size(); i++) {
    		if(mesi.get(i).equals(meseString))
    			mese = i+1;
    	}
    	this.txtResult.setText(this.model.getUmiditaMedia(mese));
    }

    void setModel(Model model) {
    	this.model = model;
    	ArrayList<String> m = new ArrayList<>();
    	m.add("gennaio");
    	m.add("febbraio");
    	m.add("marzo");
    	m.add("aprile");
    	m.add("maggio");
    	m.add("giugno");
    	m.add("luglio");
    	m.add("agosto");
    	m.add("settembre");
    	m.add("ottobre");
    	m.add("novembre");
    	m.add("dicembre");
    	
    	mesi = FXCollections.observableArrayList(m);
    	
    	this.boxMese.setItems(mesi);
    }
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnUmidita != null : "fx:id=\"btnUmidita\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
}

