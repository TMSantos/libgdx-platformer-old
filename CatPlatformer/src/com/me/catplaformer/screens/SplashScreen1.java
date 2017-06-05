package com.me.catplaformer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.me.catplaformer.managers.GameManager;
import com.me.catplaformer.managers.ScreenManager;
import com.me.catplaformer.screens.transitions.ScreenTransition;
import com.me.catplaformer.screens.transitions.ScreenTransitionFade;
import com.me.catplaformer.utils.Constants;

public class SplashScreen1 extends ScreenManager{

	private Stage blacksmithLogoScreen;
	
	private Image blacksmithLogoImage;
	
	private Texture blacksmithLogoTexture;
	
	private float animationTime;
	private boolean splashScreenCasted;
	
	public SplashScreen1(GameManager game) { super(game); }

	@Override
	public void show() {
		Gdx.input.setCatchBackKey(true);
		
		blacksmithLogoScreen = new Stage();
		blacksmithLogoScreen.setViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		
		switch(Constants.DISPLAY_RESOLUTION){
		case FHD: blacksmithLogoTexture = new Texture(Gdx.files.internal("SplashScreen1/Blacksmith-hd.png"));
				  break;
		case HD:  blacksmithLogoTexture = new Texture(Gdx.files.internal("SplashScreen1/Blacksmith-md.png"));
		  		  break;
		case LD:  blacksmithLogoTexture = new Texture(Gdx.files.internal("SplashScreen1/Blacksmith-ld.png"));
		  		  break;
		}
		
		
		blacksmithLogoImage = new Image(blacksmithLogoTexture);
		
		Stack stack = new Stack();
		stack.setSize(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		stack.add(blacksmithLogoImage);
		
		animationTime = 0;
		splashScreenCasted = false;
		
		blacksmithLogoScreen.addActor(stack);	
	}
	
	@Override
	public void render(float deltaTime) {
		if(animationTime > 3){
			animationTime = 0;
			splashScreenCasted = true;
			startSplashScreen2();
		}		
		
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
		blacksmithLogoScreen.act(deltaTime);
		blacksmithLogoScreen.draw();

		if(!splashScreenCasted) animationTime += deltaTime;
	}

	private void startSplashScreen2() {
		ScreenTransition transition = ScreenTransitionFade.init(2f);
		game.setScreen(new SplashScreen2(game), transition);
		
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void hide() {
		blacksmithLogoScreen.dispose();
		blacksmithLogoTexture.dispose();
	}
	
	@Override
	public void dispose() {
		blacksmithLogoScreen.dispose();
		blacksmithLogoTexture.dispose();
	}

	@Override
	public void pause() {
		
	}

	@Override
	public InputProcessor getInputProcessor() {
		return null;
	}

}
