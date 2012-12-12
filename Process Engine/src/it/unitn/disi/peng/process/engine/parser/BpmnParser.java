package it.unitn.disi.peng.process.engine.parser;

import it.unitn.disi.peng.process.engine.model.SubProcess;
import it.unitn.disi.peng.process.engine.model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

public class BpmnParser {
	private static final String ns = null;

	public void parse(InputStream in) throws XmlPullParserException, IOException {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			readDefinitions(parser);
		} finally {
			in.close();
		}
	}
	
	private void readDefinitions(XmlPullParser parser) throws XmlPullParserException, IOException {
	    Log.i(this.getClass().getName(), "definitions parsing...");
	    parser.require(XmlPullParser.START_TAG, ns, "definitions");

	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        
	        String name = parser.getName();
	        if (name.equals("process")) {
	        	readProcess(parser);
	        } else {
	            skip(parser);
	        }
	    }
	}
	
	private List readProcess(XmlPullParser parser) throws XmlPullParserException, IOException {	    
	    List subProcesses = new ArrayList();
	    SubProcess subProcess;

	    Log.i(this.getClass().getName(), "process parsing...");
	    parser.require(XmlPullParser.START_TAG, ns, "process");
	    
	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        
	        String name = parser.getName();
	        if (name.equals("subProcess")) {
	        	subProcess = readSubProcess(parser);
	        	subProcesses.add(subProcess);
	        } else {
	            skip(parser);
	        }
	    }
	    return subProcesses;
	}
	
	private SubProcess readSubProcess(XmlPullParser parser) throws XmlPullParserException, IOException {
	    SubProcess subProcess;
	    Task task;
	    
	    Log.i(this.getClass().getName(), "subProcess parsing...");
	    parser.require(XmlPullParser.START_TAG, ns, "subProcess");
	    String nameAttribute = parser.getAttributeValue(ns, "name");
	    subProcess = new SubProcess();
	    subProcess.setName(nameAttribute);
	    Log.i(this.getClass().getName(), subProcess.name);
	    
	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        
	        String name = parser.getName();
	        if (name.equals("task")) {
	            task = readTask(parser);
	            subProcess.addTask(task);
	        } else {
	            skip(parser);
	        }
	    }
	    return subProcess;
	}
	
	private Task readTask(XmlPullParser parser) throws XmlPullParserException, IOException {
		Task task;
		
	    Log.i(this.getClass().getName(), "task parsing...");
	    parser.require(XmlPullParser.START_TAG, ns, "task");
	    String nameAttribute = parser.getAttributeValue(ns, "name");
	    String classNameAttribute = parser.getAttributeValue(ns, "class");
	    Log.i(this.getClass().getName(), "Parsing task: " + nameAttribute + " " + classNameAttribute);
	    task = new Task();
	    task.setName(nameAttribute);
	    task.setClassName(classNameAttribute);

	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        
	        String name = parser.getName();
	        // Starts by looking for the entry tag
//	        if (name.equals("process")) {
//	        	readProcess(parser);
//	        } else {
//	            skip(parser);
//	        }
            skip(parser);
	    }
	    return task;
	}
	
	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
	    if (parser.getEventType() != XmlPullParser.START_TAG) {
	        throw new IllegalStateException();
	    }
	    int depth = 1;
	    while (depth != 0) {
	        switch (parser.next()) {
	        case XmlPullParser.END_TAG:
	            depth--;
	            break;
	        case XmlPullParser.START_TAG:
	            depth++;
	            break;
	        }
	    }
	 }
}
