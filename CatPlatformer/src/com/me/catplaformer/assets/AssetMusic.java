package com.me.catplaformer.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

public class AssetMusic {
	
	public final Music MenuTheme;
	public final Music level1;

	public AssetMusic (AssetManager am) {
		MenuTheme = am.get("Music/MenuTheme.mp3", Music.class);
		level1 = am.get("Music/level1.mp3", Music.class);
	}
	
	public void dispose(){
		MenuTheme.dispose();
		level1.dispose();
	}
}
