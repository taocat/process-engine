package it.unitn.disi.peng.process.engine.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SubProcess {
	public String name;
	private List <Task> tasks;
	
	public SubProcess() {
		this.tasks= new ArrayList <Task> (); 
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void addTask(Task task) {
		this.tasks.add(task);
	}
	
	public void execute() {
		Iterator<Task> iterator = this.tasks.iterator();
		while (iterator.hasNext()) {
			Task task = iterator.next();
			task.execute();
		}
	}
}
