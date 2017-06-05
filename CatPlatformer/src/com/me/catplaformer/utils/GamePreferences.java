package com.me.catplaformer.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Tiago Santos on 05/06/2013.
 */
public class GamePreferences {

	public static final GamePreferences instance = new GamePreferences();
	
	public Preferences gamePreferences;
	public float levelProgress;
	
	public boolean sound;
	public boolean music;
	public float volSound;
	public float volMusic;
	
	public boolean skipIntro;
	public boolean showFPS;
	
	private GamePreferences() {
		gamePreferences = Gdx.app.getPreferences(Constants.GAME_PROGRESS);
	}
	
	public void loadMusic(){
		sound = gamePreferences.getBoolean("sound", true);
		music = gamePreferences.getBoolean("music", true);
		volSound = MathUtils.clamp(gamePreferences.getFloat("volSound", 0.5f), 0.0f, 1.0f);
		volMusic = MathUtils.clamp(gamePreferences.getFloat("volMusic", 0.5f), 0.0f, 1.0f);
	}
	
	public void loadGameProgress(){
		levelProgress = gamePreferences.getFloat("gameProgress");
	}
	
	public void loadSettings(){
		skipIntro = gamePreferences.getBoolean("playIntro", false);
		showFPS = gamePreferences.getBoolean("showFPS", true);
	}
	
	public void saveSettings(){
		gamePreferences.putBoolean("playIntro", skipIntro);
		gamePreferences.putBoolean("showFPS", showFPS);
		gamePreferences.flush();
	}
	
	public void saveGameProgress(){
		gamePreferences.putFloat("gameProgress", levelProgress);
		gamePreferences.flush();
	}
	
	public void saveMusic(){
		gamePreferences.putBoolean("sound", sound);
		gamePreferences.putBoolean("music", music);
		gamePreferences.putFloat("volSound", volSound);
		gamePreferences.putFloat("volMusic", volMusic);
		gamePreferences.flush();
	}
	
}
