package com.me.catplaformer;

import com.badlogic.gdx.Gdx;
import com.me.catplaformer.managers.GameManager;
import com.me.catplaformer.screens.LoadScreen;
import com.me.catplaformer.screens.SplashScreen1;
import com.me.catplaformer.screens.transitions.ScreenTransition;
import com.me.catplaformer.screens.transitions.ScreenTransitionFade;
import com.me.catplaformer.utils.Constants;

/**
 * Created by Tiago Santos on 05/06/2013.
 */

public class CatPlatformer extends GameManager{

	public static GameListener gameListener;
	public boolean afterIntro;
	
	@Override
	public void create() {	
		initGame();	
		setScreen(new LoadScreen(this,2), null);	
//		if(afterIntro){
//			gameListener.finnishActivity();
//			setScreen(new LoadScreen(this,1), null);			
//		}else{
//			ScreenTransition transition = ScreenTransitionFade.init(2f);
//			setScreen(new SplashScreen1(this), transition);
//		}	
	}

	private void initGame() {
		Constants.SCREEN_NATIVE_HUD = Gdx.graphics.getWidth();
		Constants.SCREEN_NATIVE_HUD_HEIGHT = Gdx.graphics.getHeight(); 
		
		if(Gdx.graphics.getWidth() >= 1500){
			Constants.TEXTURE_MAIN_MENU_ATLAS = "MainMenuScreen/mainmenu-fhd.atlas";
			Constants.TEXTURE_OPTIONS_MENU_ATLAS = "OptionsScreen/optionsscreen-fhd.atlas";
			Constants.TEXTURE_PAUSE_MENU_ATLAS = "PauseMenu/pausemenu-fhd.atlas";
			Constants.VIEWPORT_GUI_WIDTH  = 1920;
			Constants.VIEWPORT_GUI_HEIGHT  = 1200;
			
			Constants.DISPLAY_RESOLUTION = Constants.DISPLAY_RES.FHD; 
		}else if(Gdx.graphics.getWidth() >= 600 && Gdx.graphics.getWidth() < 1500 ){
			Constants.TEXTURE_MAIN_MENU_ATLAS = "MainMenuScreen/mainmenu-md.atlas";
			Constants.TEXTURE_OPTIONS_MENU_ATLAS = "OptionsScreen/optionsscreen-fhd.atlas";
			Constants.TEXTURE_PAUSE_MENU_ATLAS = "PauseMenu/pausemenu-hd.atlas";

			Constants.VIEWPORT_GUI_WIDTH  = 1280;
			Constants.VIEWPORT_GUI_HEIGHT  = 800;
			
			Constants.DISPLAY_RESOLUTION = Constants.DISPLAY_RES.HD; 
		}else{
			Constants.TEXTURE_MAIN_MENU_ATLAS = "MainMenuScreen/mainmenu-ld.atlas";
			Constants.TEXTURE_OPTIONS_MENU_ATLAS = "OptionsScreen/optionsscreen-ld.atlas";
			Constants.TEXTURE_PAUSE_MENU_ATLAS = "PauseMenu/pausemenu-hd.atlas";
			
			Constants.VIEWPORT_GUI_WIDTH  = 1280;
			Constants.VIEWPORT_GUI_HEIGHT  = 800;
			
			Constants.DISPLAY_RESOLUTION = Constants.DISPLAY_RES.LD; 

		}

		switch(Gdx.app.getType()){
			case Android:
				Constants.isDesktop = false;
				break;
			case Desktop:
				Constants.isDesktop = true;
				break;
			default:break;
		}	
		
	}

	public static void castVideo(){
		gameListener.openURL();
	}

	public GameListener getGameListener() {
		return gameListener;
	}


	public void setGameListener(GameListener gameListener) {
		CatPlatformer.gameListener = gameListener;
	}


	public interface GameListener{
		public void openURL();
		public void finnishActivity();
	}
	
}
