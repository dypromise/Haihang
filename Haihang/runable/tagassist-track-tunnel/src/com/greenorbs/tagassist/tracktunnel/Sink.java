/*
 * Copyright (C) 2012 GreenOrbs
 *
 * This file is part of TagAssist System.
 * 
 * All rights reserved.
 *
 * Lei Yang, 2012-3-4
 */

package com.greenorbs.tagassist.tracktunnel;

import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.greenorbs.tagassist.device.HardwareException;
import com.greenorbs.tagassist.device.IHardware;
import com.impinj.octane.ImpinjReader;
import com.impinj.octane.OctaneSdkException;

import javassist.expr.NewArray;

public class Sink implements ActionListener {
	
	public static void main(String[] args) throws OctaneSdkException {
		DOMConfigurator.configure("log4j.xml");
		
		FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext("bean_test.xml");
		Sink sink = (Sink) ctx.getBean("sink");
		sink.start();
		ctx.close();
		
	}
	
	private static Logger _logger = Logger.getLogger(Sink.class);

	private final String COMMAND_START = "start";
	private final String COMMAND_PAUSE = "pause";
	private final String COMMAND_STOP = "stop";
	private final String COMMAND_EXIT = "exit";
	
	private PopupMenu _popup;
	
	private List<TrackTunnel_dingyangadd> _TrackTunnel_dingyangadds = new ArrayList<TrackTunnel_dingyangadd>(); 
	private Map<String, TrackTunnel_dingyangadd> _TrackTunnel_dingyangaddMap = new HashMap<String, TrackTunnel_dingyangadd>();
	
	public List<TrackTunnel_dingyangadd> getTrackTunnel_dingyangadds() {
		return _TrackTunnel_dingyangadds;
	}

	public void setTrackTunnel_dingyangadds(List<TrackTunnel_dingyangadd> __TrackTunnel_dingyangadds) {
		this._TrackTunnel_dingyangadds = __TrackTunnel_dingyangadds;
	}

	public void start() throws OctaneSdkException {
		this.startTrackTunnel_dingyangadds();
		this.buildSystemTray();
	}
	
	private void startTrackTunnel_dingyangadds() throws OctaneSdkException {
		for (TrackTunnel_dingyangadd TrackTunnel_dingyangadd : _TrackTunnel_dingyangadds) {
			try {
				TrackTunnel_dingyangadd.startup();
				this._TrackTunnel_dingyangaddMap.put(TrackTunnel_dingyangadd.getUUID(), TrackTunnel_dingyangadd);
			} catch (HardwareException e) {
				_logger.error("Failed to start up the TrackTunnel_dingyangadd: " + TrackTunnel_dingyangadd);
				try {
					TrackTunnel_dingyangadd.shutdown();
				} catch (HardwareException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
		
	private void buildSystemTray() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception ex) {
			_logger.error(ex);
		}
		
		if (SystemTray.isSupported()) {
			try {
				SystemTray tray = SystemTray.getSystemTray();
				
				this._popup = new PopupMenu();

				for (TrackTunnel_dingyangadd t : _TrackTunnel_dingyangadds) {
					Menu trackMenu = new Menu(t.getName());
					trackMenu.setActionCommand(t.getUUID());
					trackMenu.addActionListener(this);

					MenuItem play = new MenuItem("Start");
					play.setActionCommand(COMMAND_START);
					play.addActionListener(this);
					MenuItem pause = new MenuItem("Pause");
					pause.setActionCommand(COMMAND_PAUSE);
					pause.addActionListener(this);
					MenuItem stop = new MenuItem("Stop");
					stop.setActionCommand(COMMAND_STOP);
					stop.addActionListener(this);

					trackMenu.add(play);
					trackMenu.add(pause);
					trackMenu.add(stop);

					this._popup.add(trackMenu);
				}

				this._popup.addSeparator();

				MenuItem exitItem = new MenuItem("Exit");
				exitItem.setActionCommand(COMMAND_EXIT);
				exitItem.addActionListener(this);
				this._popup.add(exitItem);

				TrayIcon trayIcon = new TrayIcon(getImage("TrackTunnel_dingyangadd.png"),
						"Track Tunnels Administrator", this._popup);
				trayIcon.setActionCommand("tray");
				trayIcon.addActionListener(this);
				trayIcon.setPopupMenu(this._popup);
				trayIcon.addMouseListener(new MouseAdapter() {

					@Override
					public void mouseReleased(MouseEvent e) {
						updateMenu();
					}

				});

				tray.add(trayIcon);
			} catch (Exception e) {
				_logger.error(e);
			}
		}
	}
	
	private Image getImage(String name) throws IOException {

		URL url = Sink.class.getClassLoader().getResource(
				"com/greenorbs/tagassist/TrackTunnel_dingyangadd/images/" + name);

		return new ImageIcon(url).getImage();
	}

	private void updateMenu() {
		for (int i = 0; i < this._popup.getItemCount(); i++) {
			MenuItem item = this._popup.getItem(i);
			String uuid = item.getActionCommand();
			TrackTunnel_dingyangadd TrackTunnel_dingyangadd = this._TrackTunnel_dingyangaddMap.get(uuid);
			if (TrackTunnel_dingyangadd != null) {
				Menu menu = (Menu) item;
				Map<String, MenuItem> items = new HashMap<String, MenuItem>();
				for (int j = 0; j < menu.getItemCount(); j++) {
					items.put(menu.getItem(j).getActionCommand(),
							menu.getItem(j));
				}

				if (TrackTunnel_dingyangadd.getStatus() == IHardware.STATUS_ON) {
					items.get(COMMAND_START).setEnabled(false);
					items.get(COMMAND_PAUSE).setEnabled(true);
					items.get(COMMAND_STOP).setEnabled(true);
				} else if (TrackTunnel_dingyangadd.getStatus() == IHardware.STATUS_PAUSE) {
					items.get(COMMAND_START).setEnabled(true);
					items.get(COMMAND_PAUSE).setEnabled(false);
					items.get(COMMAND_STOP).setEnabled(true);
				} else if (TrackTunnel_dingyangadd.getStatus() == IHardware.STATUS_OFF) {
					items.get(COMMAND_START).setEnabled(true);
					items.get(COMMAND_PAUSE).setEnabled(false);
					items.get(COMMAND_STOP).setEnabled(false);
				}
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		
		switch (actionCommand) {
		case COMMAND_START:
		{
			MenuItem item = (MenuItem) e.getSource();
			String uuid = ((Menu) item.getParent()).getActionCommand();
			TrackTunnel_dingyangadd TrackTunnel_dingyangadd = this._TrackTunnel_dingyangaddMap.get(uuid);
			if (TrackTunnel_dingyangadd != null && TrackTunnel_dingyangadd.getStatus() != IHardware.STATUS_ON) {
				try {
					try {
						TrackTunnel_dingyangadd.startup();
					} catch (OctaneSdkException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (HardwareException e1) {
					_logger.error("Failed to start up the TrackTunnel_dingyangadd: " + TrackTunnel_dingyangadd.getUUID());
				}
			}
			break;
		}
		case COMMAND_PAUSE:
		{
			MenuItem item = (MenuItem) e.getSource();
			String uuid = ((Menu) item.getParent()).getActionCommand();
			TrackTunnel_dingyangadd TrackTunnel_dingyangadd = this._TrackTunnel_dingyangaddMap.get(uuid);
			if (TrackTunnel_dingyangadd != null && TrackTunnel_dingyangadd.getStatus() != IHardware.STATUS_PAUSE
					&& TrackTunnel_dingyangadd.getStatus() != IHardware.STATUS_OFF) {
				try {
					TrackTunnel_dingyangadd.pause();
				} catch (HardwareException e1) {
					_logger.error("Failed to pause the TrackTunnel_dingyangadd: " + TrackTunnel_dingyangadd);
				}
			}
			break;
		}
		case COMMAND_STOP:
		{
			MenuItem item = (MenuItem) e.getSource();
			String uuid = ((Menu) item.getParent()).getActionCommand();
			TrackTunnel_dingyangadd TrackTunnel_dingyangadd = this._TrackTunnel_dingyangaddMap.get(uuid);
			if (TrackTunnel_dingyangadd != null && TrackTunnel_dingyangadd.getStatus() != IHardware.STATUS_OFF) {
				try {
					TrackTunnel_dingyangadd.shutdown();
				} catch (HardwareException e1) {
					_logger.error("Failed to shut down the TrackTunnel_dingyangadd: " + TrackTunnel_dingyangadd);
				}
			}
			break;
		}
		case COMMAND_EXIT:
		{
			for (TrackTunnel_dingyangadd TrackTunnel_dingyangadd : _TrackTunnel_dingyangadds) {
				try {
					TrackTunnel_dingyangadd.shutdown();
				} catch (HardwareException e1) {
					_logger.error("Failed to shut down the tracktunel: " + TrackTunnel_dingyangadd);
				}
			}
			System.exit(0);
			break;
		}
		default:
			break;
		}
	}
	
}
