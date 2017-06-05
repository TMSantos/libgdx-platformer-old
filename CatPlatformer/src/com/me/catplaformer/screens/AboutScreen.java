package com.me.catplaformer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.me.catplaformer.managers.GameManager;
import com.me.catplaformer.managers.ScreenManager;
import com.me.catplaformer.screens.transitions.ScreenTransition;
import com.me.catplaformer.screens.transitions.ScreenTransitionFade;
import com.me.catplaformer.utils.Constants;

public class AboutScreen extends ScreenManager{

	private Stage aboutScreen;
	
	private Image aboutScreen1;
	private Image aboutScreen2;
	
	public AboutScreen(GameManager game) {
		super(game);
	}
	
	@Override
	public void show() {
		Gdx.input.setCatchBackKey(true);
		
		aboutScreen = new Stage();
		aboutScreen.setViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT,true);
		aboutScreen.getCamera().translate(-aboutScreen.getGutterWidth(), -aboutScreen.getGutterHeight(), 0);
		
		buildAboutScreen();
		
	}
	
	private void buildAboutScreen() {

		switch(Constants.DISPLAY_RESOLUTION){
			case FHD: aboutScreen1 = new Image(new Texture(Gdx.files.internal("AboutScreen/AboutImage1-fhd.png")));
				  	  aboutScreen2 = new Image(new Texture(Gdx.files.internal("AboutScreen/AboutImage2-fhd.png"))); 
				  	  break;
			case HD: aboutScreen1 = new Image(new Texture(Gdx.files.internal("AboutScreen/AboutImage1-hd.png")));
					 aboutScreen2 = new Image(new Texture(Gdx.files.internal("AboutScreen/AboutImage2-hd.png"))); 
					 break;
			case LD: aboutScreen1 = new Image(new Texture(Gdx.files.internal("AboutScreen/AboutImage1-ld.png")));
		  		 	 aboutScreen2 = new Image(new Texture(Gdx.files.internal("AboutScreen/AboutImage2-ld.png"))); 
		  		 	 break;
			}
		
		
		
		Stack stack = new Stack();
		stack.setSize(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		stack.add(aboutScreen1);
		stack.add(aboutScreen2);
		
		aboutScreen2.setVisible(false);
		
		aboutScreen1.addListener( new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				aboutScreen2.setVisible(true);
				super.clicked(event, x, y);
			}
			
		});
		
		aboutScreen2.addListener( new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				ScreenTransition transition = ScreenTransitionFade.init(0.75f);
				game.setScreen(new MenuScreen(game), transition);
				super.clicked(event, x, y);
			}
			
		});
		
		
		aboutScreen.addActor(stack);
		
	}

	
	@Override
	public void render(float deltaTime) {
		if(Gdx.input.isKeyPressed(Keys.BACK)){
			ScreenTransition transition = ScreenTransitionFade.init(0.75f);
			game.setScreen(new MenuScreen(game), transition);
		}
		
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		aboutScreen.act(deltaTime);
		aboutScreen.draw();
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void hide() {
		aboutScreen.dispose();	
	}
	
	@Override
	public void dispose() {
		aboutScreen.dispose();	
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InputProcessor getInputProcessor() {
		return aboutScreen;
	}

}
