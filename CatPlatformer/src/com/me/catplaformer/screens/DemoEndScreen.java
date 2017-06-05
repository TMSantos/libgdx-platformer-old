package com.me.catplaformer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.me.catplaformer.managers.AudioManager;
import com.me.catplaformer.managers.GameManager;
import com.me.catplaformer.managers.ScreenManager;
import com.me.catplaformer.screens.transitions.ScreenTransition;
import com.me.catplaformer.screens.transitions.ScreenTransitionFade;
import com.me.catplaformer.utils.Constants;

public class DemoEndScreen extends ScreenManager{
	
	private Stage demoEndScreen;
	private Image demoEndScreenImage;
	
	private float screenTime;

	public DemoEndScreen(GameManager game) {
		super(game);
	}
	
	@Override
	public void show() {
		AudioManager.instance.stopMusic();
		Gdx.input.setCatchBackKey(true);
		
		demoEndScreen = new Stage();
		demoEndScreen.setViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT,true);
		
		Texture texture = null;
		
		switch(Constants.DISPLAY_RESOLUTION){
			case FHD: texture = new Texture(Gdx.files.internal("DemoEndScreen/EndScreen-fhd.png")); break;
			case HD: texture = new Texture(Gdx.files.internal("DemoEndScreen/EndScreen-hd.png")); break;
			case LD: texture = new Texture(Gdx.files.internal("DemoEndScreen/EndScreen-ld.png")); break;
		}
		
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		demoEndScreenImage = new Image(texture);
		
		Stack stack = new Stack();
		stack.setSize(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		stack.add(demoEndScreenImage);
		
		demoEndScreen.addActor(stack);
		
		createListener();
	}

	private void createListener() {
		demoEndScreen.addListener(new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(screenTime > 2){
					ScreenTransition transition = ScreenTransitionFade.init(2f);
					game.setScreen(new MenuScreen(game), transition);
				}
				super.clicked(event, x, y);
			}
			
		});
		
	}

	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
		demoEndScreen.act(deltaTime);
		demoEndScreen.draw();
		
		screenTime += deltaTime;
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void hide() {
		demoEndScreen.dispose();
	}
	
	@Override
	public void dispose() {
		demoEndScreen.dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public InputProcessor getInputProcessor() {
		return demoEndScreen;
	}

}
