package com.greenorbs.tagassist.simulator;

import com.greenorbs.tagassist.script.Scriptor;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ToolBar;
import javafx.stage.Stage;

public class Simulator extends Application{

	private Scriptor _history = new Scriptor();
	private Scriptor _script = new Scriptor();
	private ToolBar _toolbar = new ToolBar();

	
	public static void main(String[] args) {
		Application.launch(args);
	}
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		primaryStage.show();
	}
	

}
