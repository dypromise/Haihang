package com.greenorbs.tagassist.storage.debug;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;

import com.greenorbs.tagassist.storage.AbstractStorage;
import com.greenorbs.tagassist.storage.query.IQuery;
import com.greenorbs.tagassist.storage.query.IQueryResult;

public class QueryViewer extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JButton _refresh;

	private JList<String> _list;

	private AbstractStorage _storage;

	public QueryViewer(AbstractStorage storage) {

		_refresh = new JButton("referesh query");

		_refresh.addActionListener(this);

		this._storage = storage;

		this._list = new JList<String>();

		this.setLayout(new BorderLayout());

		this.add(this._refresh, BorderLayout.NORTH);

		this.add(this._list);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (this._storage != null) {

			Vector<String> queries = new Vector<String>();
			Iterator it = this._storage.queries().keySet().iterator();
			while (it.hasNext()) {

				IQuery query = (IQuery) it.next();
				WeakReference<IQueryResult> result = (WeakReference<IQueryResult>) this._storage
						.queries().get(query);

				queries.add("[" + query + ":" + result.get() + "]");
			}

			this._list = new JList<String>(queries);
		}
	}

}
