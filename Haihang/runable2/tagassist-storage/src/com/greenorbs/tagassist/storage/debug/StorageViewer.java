package com.greenorbs.tagassist.storage.debug;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.ref.WeakReference;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.apache.log4j.xml.DOMConfigurator;

import com.greenorbs.tagassist.storage.query.IQuery;
import com.greenorbs.tagassist.storage.query.IQueryResult;
import com.greenorbs.tagassist.storage.query.StorageFactory;

public class StorageViewer extends JFrame {

	private static final long serialVersionUID = 1L;

	public StorageViewer() {
		JTabbedPane main = new JTabbedPane();

		JPanel bagTab = new JPanel();
		bagTab = new JPanel();
		bagTab.setLayout(new GridLayout(1, 0));
		QueryViewer queryViewer = new QueryViewer(
				StorageFactory.getBaggageStorage());
		ObjectViewer objectViewer = new ObjectViewer(
				StorageFactory.getBaggageStorage());
	
		
		bagTab.add(queryViewer);
		bagTab.add(objectViewer);		
		main.add(bagTab,"Baggage Storage");
		
		
		this.getContentPane().add(main);

		this.setSize(500,500);
	}

	public static void main(String[] args) {
		
		DOMConfigurator.configure("log4j.xml");

		JFrame frame = new StorageViewer();
		frame.setSize(500, 500);
		frame.setVisible(true);

	}
}
