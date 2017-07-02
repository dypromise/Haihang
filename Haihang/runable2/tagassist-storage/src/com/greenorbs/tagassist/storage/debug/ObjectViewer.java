package com.greenorbs.tagassist.storage.debug;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;

import com.greenorbs.tagassist.storage.IStorage;

public class ObjectViewer extends JPanel implements ActionListener {

	private JList _list;

	private JButton _refresh;

	private IStorage _storage;

	public ObjectViewer(IStorage storage) {

		this._list = new JList();
		this._refresh = new JButton("Refresh objects");

		this._refresh.addActionListener(this);

		this._storage = storage;
		
		this.setLayout(new BorderLayout());
		this.add(this._refresh, BorderLayout.NORTH);
		this.add(this._list, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		DefaultListModel<String> model = (DefaultListModel)this._list.getModel();

		if (_storage == null) {
			return;
		}

		Iterator<?> it = this._storage.iterator();
		while (it.hasNext()) {
			Object o = it.next();
			model.addElement("[" + o.getClass() + "]:[" + o.toString() + "]");
		}

	}
}
