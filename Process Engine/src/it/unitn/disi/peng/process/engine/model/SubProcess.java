package it.unitn.disi.peng.process.engine.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.util.Log;

public class SubProcess {
	public String name;
	private List <BpmnElement> elements;
	private BpmnElement start;
	private BpmnElement end;
	private boolean flows[][];
	private int currentIndex;
	public static final int BEFORE_EXECUTION = -1;
	public static final int AFTER_EXECUTION = -2;
	private HashMap<String, String> variables;
	
	public SubProcess() {
		this.elements= new ArrayList <BpmnElement> (); 
		this.currentIndex = BEFORE_EXECUTION;
	}
	
	public SubProcess(String name) {
		this.elements= new ArrayList <BpmnElement> (); 
		this.currentIndex = BEFORE_EXECUTION;
		this.name = name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setStart(BpmnElement element) {
		this.start = element;
	}
	
	public void setEnd(BpmnElement element) {
		this.end = element;
	}
	
	public void addElement(BpmnElement element) {
		this.elements.add(element);
	}
	
	public void addFlow(String sourceRef, String targetRef) {
		// initialize the matrix if not yet done
		if (flows == null) {
			flows = new boolean [elements.size()] [elements.size()];
		}
		
		int sourceIndex = elementId2Index(sourceRef);
		int targetIndex = elementId2Index(targetRef);
		if (sourceIndex >= 0 && targetIndex >= 0) {
			//if both source and target id found
			flows[sourceIndex] [targetIndex] = true;
		}
	}
	
	public int elementId2Index(String id) {
		int i = 0;
		Iterator<BpmnElement> iterator = this.elements.iterator();
		while (iterator.hasNext()) {
			BpmnElement element = iterator.next();
			if (element.getId().equals(id)) {
				return i;
			}
			i++;
		}
		return -1;
	}
	
	public void execute(Activity activity) {
		if (start != null) {
			BpmnElement currentElement = start;
			int currentIndex = elements.indexOf(start);
			while (currentElement != null) {
				Log.i(this.getClass().getName(), currentElement.getId() + ":" + currentElement.getName());
				
				currentIndex = getNextIndex(currentIndex);
				if (currentIndex >= 0) {
					currentElement = elements.get(currentIndex);
				}
				else {
					// after executing one element, the next element is by default set to null, until there is actually a next element
					currentElement = null;
				}
			}
		}
	}
	
	public void executeNext(Activity activity) {
		BpmnElement currentElement;
		Log.i(this.getClass().getName(), "Current Index 1:" + currentIndex);
		currentIndex = getNextIndex(currentIndex);
		Log.i(this.getClass().getName(), "Current Index 2:" + currentIndex);
		if (currentIndex >= 0) {
			currentElement = elements.get(currentIndex);
			Log.i(this.getClass().getName(), "executeNext 1:" + currentIndex + ":" + currentElement.getName());
			if (currentElement instanceof Task) {
				Log.i(this.getClass().getName(), "executeNext 2:" + currentElement.getName());
				Task currentTask = (Task) currentElement;
				currentTask.execute(activity, this);
				variables = currentTask.getVariables();
			}
		}
	}
	
	public int getNextIndex(int ci) {
		
		if (ci == BEFORE_EXECUTION) {
			Log.i(this.getClass().getName(), "start index is:" + elements.indexOf(start));
			return elements.indexOf(start);
		}
		
		// try to find the next element
		for (int j = 0; j < elements.size(); j++) {
			if ( flows[ci][j] ) {
				// TODO
				// gateway
				// move to the next element
				return j;
			}
		}
		
		return AFTER_EXECUTION;
	}
	
	public void print() {
		Iterator<BpmnElement> iterator = this.elements.iterator();
		while (iterator.hasNext()) {
			BpmnElement element = iterator.next();
			Log.i(this.getClass().getName(), element.getId() + ":" + element.getName());
		}
		
		for (int i = 0; i < elements.size(); i++) {
			for (int j = 0; j < elements.size(); j++) {
				if (flows[i][j]) {
					Log.i(this.getClass().getName(), "" + i + "." + elements.get(i).getName() + "->" + j + "." + elements.get(j).getName());
				}
			}
		}
	}
	
	public HashMap<String, String> getVariables() {
		return variables;
	}
}
