package com.greenorbs.tagassist.simulator.ui;

import com.greenorbs.tagassist.simulator.device.SimPeripheralServer;
import com.greenorbs.tagassist.simulator.device.SimReaderServer;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class SimCheckTunnel extends SimItem {

	private SimReaderServer _readerServer;
	
	private SimPeripheralServer _peripheralServer;
	
	
	
//	static double initX;
//	static double initY;
//	static Point2D dragAnchor;
//
//	static{
//		
//		//please read this static variables from the config file.
//	}
//
//	public SimCheckTunnel(Config.Direction dir) {
//		Group g = new Group();
//		AnchorPane ap = new AnchorPane();
//		g.getChildren().add(ap);
//
//		if (dir == Config.Direction.Vertical) {
//			ap.setPrefHeight(Config.CONVEYOR_ITSELF_WIDTH + 100);
//			ap.setPrefWidth(77.0);
//
//			ImageView top = new ImageView(new Image(getClass()
//					.getResourceAsStream("image/check_vertical_top.png")));
//			ImageView bottom = new ImageView(new Image(getClass()
//					.getResourceAsStream("image/check_vertical_bottom.png")));
//			ap.getChildren().addAll(top, bottom);
//			ap.setTopAnchor(top, 0.0);
//			ap.setBottomAnchor(bottom, 0.0);
//		} else {
//			ap.setPrefHeight(77.0);
//			ap.setPrefWidth(Config.CONVEYOR_ITSELF_WIDTH + 100);
//
//			ImageView left = new ImageView(new Image(getClass()
//					.getResourceAsStream("image/check_horizontal_left.png")));
//			ImageView right = new ImageView(new Image(getClass()
//					.getResourceAsStream("image/check_horizontal_right.png")));
//			ap.getChildren().addAll(left, right);
//			ap.setLeftAnchor(left, 0.0);
//			ap.setRightAnchor(right, 0.0);
//		}
//		getChildren().add(g);
//
//		// change a cursor when it is over rect
//		setCursor(Cursor.HAND);
//
//		setOnMousePressed(new EventHandler<MouseEvent>() {
//			public void handle(MouseEvent me) {
//				// toFront();
//				initX = getTranslateX();
//				initY = getTranslateY();
//				dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());
//			}
//		});
//
//		setOnMouseDragged(new EventHandler<MouseEvent>() {
//			public void handle(MouseEvent me) {
//				double newTranslateX = initX + me.getSceneX()
//						- dragAnchor.getX();
//				double newTranslateY = initY + me.getSceneY()
//						- dragAnchor.getY();
//				setTranslateX(newTranslateX);
//				setTranslateY(newTranslateY);
//			}
//		});
//	}
}
