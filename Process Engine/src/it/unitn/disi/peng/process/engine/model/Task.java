package it.unitn.disi.peng.process.engine.model;

import android.util.Log;

public class Task {
	private String name;
	private String className;
	
	public Task() {
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public void execute() {
	    Log.i(this.getClass().getName(), "Executing task: " + name);
	}
}
