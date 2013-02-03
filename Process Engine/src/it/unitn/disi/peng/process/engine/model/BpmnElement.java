package it.unitn.disi.peng.process.engine.model;

public class BpmnElement {
	
	private String id;
	private String name;
	
	BpmnElement(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
