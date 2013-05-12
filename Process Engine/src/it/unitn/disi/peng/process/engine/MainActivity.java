package it.unitn.disi.peng.process.engine;

import it.unitn.disi.peng.process.engine.parser.BpmnParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		parseBpmn();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private void parseBpmn() {
		File SDCardRoot = Environment.getExternalStorageDirectory();
		File file = new File(SDCardRoot, "healthcare.bpmn");
//		File file = new File(SDCardRoot, "MDO_control_annotated.bpmn");
//		File file = new File(SDCardRoot, "MDO_control_simplest_annotated.bpmn");
//		File file = new File("MDO control_annotated.bpmn");
		InputStream in = null;

		BpmnParser parser = new BpmnParser(this);

		try {
			in = new FileInputStream(file);
			parser.parse(in);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
