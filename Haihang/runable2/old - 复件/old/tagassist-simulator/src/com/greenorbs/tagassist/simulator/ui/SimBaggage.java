package com.greenorbs.tagassist.simulator.ui;

import java.util.Random;

import com.greenorbs.tagassist.BaggageInfo;
import com.greenorbs.tagassist.simulator.device.SimReaderServer;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;

public class SimBaggage extends SimItem {

	private BaggageInfo _baggage;

	public void setBaggage(BaggageInfo baggage) {
		this._baggage = baggage;
	}

	public BaggageInfo getBaggage() {
		return this._baggage;
	}

//	static double initX;
//	static double initY;
//	static Point2D dragAnchor;
//	final Rectangle rect;
//
//	public SimBaggage(double posX, double posY) {
//		Group g = new Group();
//		// Math.abs((new Random()).nextInt())%8
//		Color color = Config.COLORSET[Math.abs((new Random()).nextInt()) % 10];
//		rect = RectangleBuilder
//				.create()
//				.width(40.0)
//				.height(40.0)
//				.fill(new RadialGradient(1, 0, 0.2, 1, 0.9, true,
//						CycleMethod.NO_CYCLE, new Stop[] {
//								new Stop(0, Color.rgb(250, 250, 250)),
//								new Stop(1, color) }))
//				.effect(new InnerShadow(7, color.darker().darker())).x(posX)
//				.y(posY)
//				// .layoutX(posX)
//				// .layoutY(posY)
//				.arcHeight(10).arcWidth(10).build();
//		g.getChildren().add(rect);
//		getChildren().add(g);
//
//		// change a cursor when it is over rect
//		setCursor(Cursor.HAND);
//
//		setOnMousePressed(new EventHandler<MouseEvent>() {
//			public void handle(MouseEvent me) {
//				toFront();
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
//
//				setTranslateX(newTranslateX);
//				setTranslateY(newTranslateY);
//			}
//		});
//
//		setOnMouseReleased(new EventHandler<MouseEvent>() {
//			public void handle(MouseEvent me) {
//
//			}
//		});
//
//		// this.setOnMousePressed(new EventHandler<MouseEvent>(){
//		// public void handle(MouseEvent e){
//		//
//		// }
//		// });
//	}
}
