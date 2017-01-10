package com.example.testpocketsphinx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;




import com.example.testpocketsphinx.swig.Config;
import com.example.testpocketsphinx.swig.Decoder;
import com.example.testpocketsphinx.swig.Hypothesis;




public class MainActivity extends Activity implements OnClickListener {

	private Button mButtonStart = null;
	private Button mButtonStop = null;

	static {
		System.loadLibrary("pocketsphinx_jni");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mButtonStart = (Button) this.findViewById(R.id.btn_start);
		mButtonStop = (Button) this.findViewById(R.id.btn_stop);
		mButtonStart.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_start:
			onStartRecord();
			break;
		default:
			break;
		}
	}
	private void onStartRecord() {
		Config c = Decoder.defaultConfig();
        c.setString("-hmm", "/mnt/sdcard/lml/en-us");
        c.setString("-lm", "/mnt/sdcard/lml/en-us.lm.bin");
        c.setString("-dict", "/mnt/sdcard/lml/cmudict-en-us.dict");
        Decoder d = new Decoder(c);
		

        @SuppressWarnings("resource")
		FileInputStream ais = null;
		try {
			ais = new FileInputStream(new File("/mnt/sdcard/lml/goforward.raw"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Decoder d = new Decoder();
        d.startUtt();
        d.setRawdataSize(300000);
        byte[] b = new byte[4096];
        int nbytes;
        try {
			while ((nbytes = ais.read(b)) >= 0) {
			    ByteBuffer bb = ByteBuffer.wrap(b, 0, nbytes);
			    bb.order(ByteOrder.LITTLE_ENDIAN);
			    short[] s = new short[nbytes/2];
			    bb.asShortBuffer().get(s);
			    //System.out.println(s);
			    d.processRaw(s, nbytes/2, false, false);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        d.endUtt();
        System.out.println(d.hyp().getHypstr());
	
	}
}
/*
*	private void onStartRecord() {
		
*		// TODO Auto-generated method stub
*		int mresult = 0;
*		String[] args;
*		// String inFile;
*		// String otFiel;

*		Test tinyalsa 
*		// inFile = "/mnt/sdcard/tinyalsa/test.wav";
*		// otFiel = "/mnt/sdcard/alizedemo/alize/data/train/prm/S01-01.prm";
*		// System.out.println("----inFile = "+inFile);
*		// System.out.println("----otFile = "+otFiel);
*		// args = new
*		// String[]{"sfbcep","-F","PCM16","-p","19","-e","-D","-A",inFile,otFiel};
*		
*		Log.e("midea","+++INFO: mresult = " + mresult);
*		System.out.println("+++INFO: mresult = " + mresult);
*		args = new String[] { "tinyalsa"};
*		//mresult = tinyalsa.tinyalsa(args);
*		// mresult = Feature.read_paras(args);
*		Log.e("midea","+++INFO2:mresult = " + mresult);
*		//System.out.println("+++INFO2:mresult = " + mresult);
*	}
*/


