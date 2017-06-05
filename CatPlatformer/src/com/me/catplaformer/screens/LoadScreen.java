package com.me.catplaformer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.me.catplaformer.managers.GameManager;
import com.me.catplaformer.managers.ResourceManager;
import com.me.catplaformer.managers.ScreenManager;
import com.me.catplaformer.screens.transitions.ScreenTransition;
import com.me.catplaformer.screens.transitions.ScreenTransitionFade;
import com.me.catplaformer.utils.Constants;

public class LoadScreen extends ScreenManager{

	private Stage loadScreen;
	
	private Texture loadingTexture;
	private Texture backgroundTexture;
	
	private Image loadScreenBackground;
	private Image loading;
	
	private int levelNumber;
	
	private boolean finished;
	private boolean castMenu;
	
	public LoadScreen(GameManager game,int level) {
		super(game);
		levelNumber = level;
	}
	
	@Override
	public void show() {
		Gdx.input.setCatchBackKey(true);
		
		if(ResourceManager.instance.getAssetManager() == null ) 
			ResourceManager.instance.init();
		
//		ResourceManager.instance.loadMusicResources();
		
		loadScreen = new Stage();
		loadScreen.setViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		
		backgroundTexture = new Texture(Gdx.files.internal("SplashScreen2/kittenrush-hd.png"));
		
		loadScreenBackground = new Image(backgroundTexture);
		
		Stack stack = new Stack();
		stack.setSize(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		stack.add(loadScreenBackground);
		
		loadingTexture = new Texture(Gdx.files.internal("LoadingScreen/dotsfhd.png")); 
		loading = new Image(loadingTexture);
		loading.setSize(1.5f, 0.2f);
		loading.setPosition(7.5f, 3);
			
		loadScreen.addActor(stack);
		loadScreen.addActor(loading);
		
		finished = false;
		castMenu = false;
		
		switch(levelNumber){
			case 1: loadLevel1and2Platforms();
					loadCommomObjects();
				    loadLevel1(); 
				    break;
			case 2: loadLevel1and2Platforms();
					loadCommomObjects();
					loadLevel2(); 
					break;
		}
		
	}
	
	private void loadLevel1and2Platforms(){
		ResourceManager.instance.getAssetManager().load(Constants.LEVEL_1_2_OBJECTS, TextureAtlas.class);
		ResourceManager.instance.getAssetManager().finishLoading();
	}
	
	private void loadCommomObjects(){
		ResourceManager.instance.getAssetManager().load(Constants.LEVEL_COMMOM_OBJECTS, TextureAtlas.class);
		ResourceManager.instance.getAssetManager().finishLoading();
		ResourceManager.instance.getAssetManager().load(Constants.LEVEL_WAYPOINTS, TextureAtlas.class);
		ResourceManager.instance.getAssetManager().finishLoading();
	}
	
	private void loadLevel1() {
		ResourceManager.instance.getAssetManager().load(Constants.LEVEL1_OBJECTS, TextureAtlas.class);
		ResourceManager.instance.getAssetManager().finishLoading();
		
	}
	
	private void loadLevel2() {
		
	}

	@Override
	public void render(float deltaTime) {
		if(castMenu){
			castMenu = false;
			ScreenTransition transition = ScreenTransitionFade.init(0.75f);
			game.setScreen(new GameScreen(game,levelNumber), transition);
		}
		
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		if(!finished){
			loadScreen.act(deltaTime);
			loadScreen.draw();
		}

		if(ResourceManager.instance.getAssetManager().update()){
			if(!finished){
				finished = true;
				castMenu = true;
				
				switch(levelNumber){
					case 1: createLevel1Resources();  break;
					case 2: createLevel2Resources();  break;
				}
			}			
		}
	}

	private void createLevel1Resources() {
		ResourceManager.instance.createCommomResources();
		ResourceManager.instance.createLevel1and2FallingPlatforms();
		ResourceManager.instance.createLevel1Resources();
		
	}
	
	private void createLevel2Resources() {
		ResourceManager.instance.createCommomResources();
		ResourceManager.instance.createLevel1and2FallingPlatforms();
		ResourceManager.instance.createLevel2Resources();
		
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void hide() {
		loadScreen.dispose();
		loadingTexture.dispose();
		backgroundTexture.dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public InputProcessor getInputProcessor() {
		return null;
	}

}
