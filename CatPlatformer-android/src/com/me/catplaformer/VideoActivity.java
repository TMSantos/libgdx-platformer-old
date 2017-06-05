package com.me.catplaformer;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

public class VideoActivity extends Activity implements OnCompletionListener{

/**
 * Created by Tiago Santos on 21/06/2013.
 */
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.videoview);
		
		String fileName = "android.resource://" + getPackageName() + "/raw/introanimation";
		
		VideoView vv = (VideoView) this.findViewById(R.id.surface);
		vv.setVideoURI(Uri.parse(fileName));
		vv.setOnCompletionListener(this);
		vv.start();
}

@Override
public void onCompletion(MediaPlayer arg0) {
		Intent intent = new Intent(this, AfterIntroActivity.class);
		startActivity(intent);      
		finish();
}

@Override
public void onBackPressed() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);      
		finish();
}

}
