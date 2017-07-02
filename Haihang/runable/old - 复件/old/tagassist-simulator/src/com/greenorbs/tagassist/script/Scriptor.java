package com.greenorbs.tagassist.script;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import com.greenorbs.tagassist.simulator.util.SortedList;

public class Scriptor implements Iterable<Script> {

	public static final int ELEMENT_BAGGAGE = 0;
	public static final int ELEMENT_CARRIAGE = 1;
	public static final int ELEMENT_CHECK_TUNNEL = 2;
	public static final int ELEMENT_TRACK_TUNNEL = 3;
	public static final int ELEMENT_CONVEYOR = 4;
	public static final int ELEMENT_SORTPOOL = 5;

	public static final int ACTION_ADD = 10;
	public static final int ACTION_DELETE = 11;
	public static final int ACTION_MOVE = 12;
	public static final int ACTION_EDIT = 13;

	private List<Script> _scripts;

	public Scriptor() {
		this._scripts = new SortedList<Script>(new Comparator<Script>() {
			@Override
			public int compare(Script s1, Script s2) {
				if (s1 == null) {
					return 1;
				} else {
					long r = s1.getTime() - s2.getTime();
					if (r == 0) {
						return 0;
					} else {
						return r > 0 ? 1 : -1;

					}
				}
			}

		});
	}

	public static Scriptor load(String filename) {
		// TOD load the script from the file.
		return null;
	}

	public void save(String filename) {
		// TOD save the script into file.
	}

	public void add(Script script) {

		this._scripts.add(script);
	}

	@Override
	public Iterator<Script> iterator() {
		return this._scripts.iterator();
	}

}
