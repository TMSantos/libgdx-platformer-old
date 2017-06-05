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

public class SplashScreen2 extends ScreenManager{
	
	private Stage splashScreen;
	
	private Image splashImage;
	
	private Texture splashTexture;
	
	private float animationTime;
	private boolean mainMenuCasted;

	public SplashScreen2(GameManager game) {
		super(game);
	}

	@Override
	public void show() {
		Gdx.input.setCatchBackKey(true);
		
		splashScreen = new Stage();
		splashScreen.setViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		
		switch(Constants.DISPLAY_RESOLUTION){
		case FHD: splashTexture = new Texture(Gdx.files.internal("SplashScreen2/kittenrush-hd.png"));
				  break;
		case HD:  splashTexture = new Texture(Gdx.files.internal("SplashScreen2/kittenrush-hd.png"));
		  		  break;
		case LD:  splashTexture = new Texture(Gdx.files.internal("SplashScreen2/kittenrush-ld.png"));
		  		  break;
		}
		
		splashImage = new Image(splashTexture);
		
		Stack stack = new Stack();
		stack.setSize(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		stack.add(splashImage);
		
		animationTime = 0;
		mainMenuCasted = false;
		
		splashScreen.addActor(stack);		
	}
	
	@Override
	public void render(float deltaTime) {
		if(animationTime > 3){
			animationTime = 0;
			mainMenuCasted = true;
			startMenu();
		}		
		
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
		splashScreen.act(deltaTime);
		splashScreen.draw();

		if(!mainMenuCasted) animationTime += deltaTime;
	}
	
	private void startMenu(){
			ScreenTransition transition = ScreenTransitionFade.init(2f);
			game.setScreen(new MenuScreen(game), transition);
	}
	@Override
	public void resize(int width, int height) {}
	
	@Override
	public void hide() {
		splashScreen.dispose();
		splashTexture.dispose();
	}
	
	@Override
	public void dispose() {
		splashScreen.dispose();
		splashTexture.dispose();
	}

	@Override
	public void pause() {}

	@Override
	public InputProcessor getInputProcessor() {
		return splashScreen;
	}

}
