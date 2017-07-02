package com.greenorbs.tagassist.simulator.ui;

import com.greenorbs.tagassist.CarriageInfo;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class SimCarriage extends SimItem {
	
	private CarriageInfo _carraiage;
	
	public CarriageInfo getCarraiage() {
		return _carraiage;
	}

	public void setCarraiage(CarriageInfo carraiage) {
		_carraiage = carraiage;
	}
	
	static double initX;
	static double initY;
	static Point2D dragAnchor;

	public SimCarriage() {
		Group g = new Group();
		ImageView truck = new ImageView(new Image(getClass()
				.getResourceAsStream("image/truck.png")));
		g.getChildren().add(truck);
		getChildren().add(g);

		// change a cursor when it is over rect
		setCursor(Cursor.HAND);

		setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				// toFront();
				initX = getTranslateX();
				initY = getTranslateY();
				dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());
			}
		});

		setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent me) {
				double newTranslateX = initX + me.getSceneX()
						- dragAnchor.getX();
				double newTranslateY = initY + me.getSceneY()
						- dragAnchor.getY();
				setTranslateX(newTranslateX);
				setTranslateY(newTranslateY);
			}
		});
	}

	
}
