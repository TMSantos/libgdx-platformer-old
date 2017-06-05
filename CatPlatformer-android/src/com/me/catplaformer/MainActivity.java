package com.me.catplaformer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

/**
 * Created by Tiago Santos on 05/06/2013.
 */
@Override
public class MainActivity extends AndroidApplication{
	
	private Context mContext;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mContext = this;
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        
        CatPlatformer gameInstance = new CatPlatformer();
        gameInstance.setGameListener(gameListener);
        gameInstance.afterIntro = false;
        
        initialize(gameInstance, cfg);
    }
    
	private CatPlatformer.GameListener gameListener = new CatPlatformer.GameListener() {
		
		@Override
		public void openURL() {
			  Intent myIntent = new Intent(mContext, VideoActivity.class);
			  startActivity(myIntent);
		}

		@Override
		public void finnishActivity() {
			finish();
		}
	};
}