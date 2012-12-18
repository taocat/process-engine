package it.unitn.disi.peng.process.engine.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;

public class SubProcess {
	public String name;
	private List <Task> tasks;
	
	public SubProcess() {
		this.tasks= new ArrayList <Task> (); 
	}
	
	public SubProcess(String name) {
		this.tasks= new ArrayList <Task> (); 
		this.name = name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void addTask(Task task) {
		this.tasks.add(task);
	}
	
	public void execute(Activity activity) {
		Iterator<Task> iterator = this.tasks.iterator();
		while (iterator.hasNext()) {
			Task task = iterator.next();
			task.execute(activity);
		}
	}
}
