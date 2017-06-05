package com.me.catplaformer;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

/**
 * Created by Tiago Santos on 21/06/2013.
 */
public class AfterIntroActivity extends AndroidApplication{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		
		CatPlatformer newCatPlatformerInstance = new CatPlatformer();
		newCatPlatformerInstance.afterIntro = true;
		
		initialize(newCatPlatformerInstance, cfg);
    
	}
}
