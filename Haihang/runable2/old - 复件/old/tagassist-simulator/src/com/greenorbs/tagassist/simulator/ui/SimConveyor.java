package com.greenorbs.tagassist.simulator.ui;

import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.StrokeLineCap;

public class SimConveyor extends SimItem {

//	public final Path path;

//	public SimConveyor(Color color, double width) {
//		path = new Path();
//
//		MoveTo moveTold = new MoveTo();
//		moveTold.setX(Config.CONVEYOR_START_X);
//		moveTold.setY(Config.CONVEYOR_START_Y + Config.CONVEYOR_CORNER_RADIUS);
//
//		LineTo lineTold = new LineTo();
//		lineTold.setX(Config.CONVEYOR_START_X);
//		lineTold.setY(Config.CONVEYOR_START_Y + Config.CONVEYOR_WIDTH
//				- Config.CONVEYOR_CORNER_RADIUS);
//
//		MoveTo moveTolb = new MoveTo();
//		moveTolb.setX(Config.CONVEYOR_START_X);
//		moveTolb.setY(Config.CONVEYOR_START_Y + Config.CONVEYOR_WIDTH
//				- Config.CONVEYOR_CORNER_RADIUS);
//
//		ArcTo arcTolb = new ArcTo();
//		arcTolb.setX(Config.CONVEYOR_START_X + Config.CONVEYOR_CORNER_RADIUS);
//		arcTolb.setY(Config.CONVEYOR_START_Y + Config.CONVEYOR_WIDTH);
//		arcTolb.setRadiusX(Config.CONVEYOR_CORNER_RADIUS);
//		arcTolb.setRadiusY(Config.CONVEYOR_CORNER_RADIUS);
//
//		// ////////////////////////////////////////
//
//		MoveTo moveTobr = new MoveTo();
//		moveTobr.setX(Config.CONVEYOR_START_X + Config.CONVEYOR_CORNER_RADIUS);
//		moveTobr.setY(Config.CONVEYOR_START_Y + Config.CONVEYOR_WIDTH);
//
//		LineTo lineTobr = new LineTo();
//		lineTobr.setX(Config.CONVEYOR_START_X + Config.CONVEYOR_LENGTH
//				- Config.CONVEYOR_CORNER_RADIUS);
//		lineTobr.setY(Config.CONVEYOR_START_Y + Config.CONVEYOR_WIDTH);
//
//		MoveTo moveTorb = new MoveTo();
//		moveTorb.setX(Config.CONVEYOR_START_X + Config.CONVEYOR_LENGTH
//				- Config.CONVEYOR_CORNER_RADIUS);
//		moveTorb.setY(Config.CONVEYOR_START_Y + Config.CONVEYOR_WIDTH);
//
//		ArcTo arcTorb = new ArcTo();
//		arcTorb.setX(Config.CONVEYOR_START_X + Config.CONVEYOR_LENGTH);
//		arcTorb.setY(Config.CONVEYOR_START_Y + Config.CONVEYOR_WIDTH
//				- Config.CONVEYOR_CORNER_RADIUS);
//		arcTorb.setRadiusX(Config.CONVEYOR_CORNER_RADIUS);
//		arcTorb.setRadiusY(Config.CONVEYOR_CORNER_RADIUS);
//
//		// ////////////////////////////////////////
//
//		MoveTo moveToru = new MoveTo();
//		moveToru.setX(Config.CONVEYOR_START_X + Config.CONVEYOR_LENGTH);
//		moveToru.setY(Config.CONVEYOR_START_Y + Config.CONVEYOR_WIDTH
//				- Config.CONVEYOR_CORNER_RADIUS);
//
//		LineTo lineToru = new LineTo();
//		lineToru.setX(Config.CONVEYOR_START_X + Config.CONVEYOR_LENGTH);
//		lineToru.setY(Config.CONVEYOR_START_Y + Config.CONVEYOR_CORNER_RADIUS);
//
//		MoveTo moveTort = new MoveTo();
//		moveTort.setX(Config.CONVEYOR_START_X + Config.CONVEYOR_LENGTH);
//		moveTort.setY(Config.CONVEYOR_START_Y + Config.CONVEYOR_CORNER_RADIUS);
//
//		ArcTo arcTort = new ArcTo();
//		arcTort.setX(Config.CONVEYOR_START_X + Config.CONVEYOR_LENGTH
//				- Config.CONVEYOR_CORNER_RADIUS);
//		arcTort.setY(Config.CONVEYOR_START_Y);
//		arcTort.setRadiusX(Config.CONVEYOR_CORNER_RADIUS);
//		arcTort.setRadiusY(Config.CONVEYOR_CORNER_RADIUS);
//
//		// ////////////////////////////////////////////
//
//		MoveTo moveTotl = new MoveTo();
//		moveTotl.setX(Config.CONVEYOR_START_X + Config.CONVEYOR_LENGTH
//				- Config.CONVEYOR_CORNER_RADIUS);
//		moveTotl.setY(Config.CONVEYOR_START_Y);
//
//		LineTo lineTotl = new LineTo();
//		lineTotl.setX(Config.CONVEYOR_START_X + Config.CONVEYOR_CORNER_RADIUS);
//		lineTotl.setY(Config.CONVEYOR_START_Y);
//
//		MoveTo moveTolt = new MoveTo();
//		moveTolt.setX(Config.CONVEYOR_START_X + Config.CONVEYOR_CORNER_RADIUS);
//		moveTolt.setY(Config.CONVEYOR_START_Y);
//
//		ArcTo arcTolt = new ArcTo();
//		arcTolt.setX(Config.CONVEYOR_START_X);
//		arcTolt.setY(Config.CONVEYOR_START_Y + Config.CONVEYOR_CORNER_RADIUS);
//		arcTolt.setRadiusX(Config.CONVEYOR_CORNER_RADIUS);
//		arcTolt.setRadiusY(Config.CONVEYOR_CORNER_RADIUS);
//
//		path.getElements().add(moveTold);
//		path.getElements().add(lineTold);
//		path.getElements().add(moveTolb);
//		path.getElements().add(arcTolb);
//
//		path.getElements().add(moveTobr);
//		path.getElements().add(lineTobr);
//		path.getElements().add(moveTorb);
//		path.getElements().add(arcTorb);
//
//		path.getElements().add(moveToru);
//		path.getElements().add(lineToru);
//		path.getElements().add(moveTort);
//		path.getElements().add(arcTort);
//
//		path.getElements().add(moveTotl);
//		path.getElements().add(lineTotl);
//		path.getElements().add(moveTolt);
//		path.getElements().add(arcTolt);
//
//		path.setStroke(color);
//		path.setStrokeWidth(width);
//		path.setStrokeLineCap(StrokeLineCap.ROUND);
//	}
}
