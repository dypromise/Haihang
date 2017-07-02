package com.greenorbs.tagassist.script;

public class Script {

	private int _element;

	private int _action;

	private long _time;

	private Object[] _parameters;

	public Script(int element, int action, Object... parameters) {

		this.setElement(element);
		this.setAction(action);
		this.setParameters(parameters);
	}

	public int getElement() {
		return _element;
	}

	public void setElement(int element) {
		_element = element;
	}

	public int getAction() {
		return _action;
	}

	public void setAction(int action) {
		_action = action;
	}

	public long getTime() {
		return _time;
	}

	public void setTime(long time) {
		_time = time;
	}

	public Object[] getParameters() {
		return _parameters;
	}

	public void setParameters(Object[] parameters) {
		_parameters = parameters;
	}


}
