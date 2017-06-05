package com.me.catplaformer;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * Created by Tiago Santos on 05/06/2013.
 */
public class Main {
	
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "CatPlatformer";
		cfg.width = 480;
		cfg.height = 320;
		
		new LwjglApplication(new CatPlatformer(), cfg);
	}
}
