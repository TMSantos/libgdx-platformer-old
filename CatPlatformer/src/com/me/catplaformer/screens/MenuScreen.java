package com.me.catplaformer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.me.catplaformer.managers.GameManager;
import com.me.catplaformer.managers.ScreenManager;
import com.me.catplaformer.screens.transitions.ScreenTransition;
import com.me.catplaformer.screens.transitions.ScreenTransitionFade;
import com.me.catplaformer.utils.Constants;

public class MenuScreen extends ScreenManager{

	private Stage mainMenuStage;
	private Skin mainMenuSkin;

	private Image imgMainMenuBackground;
	
	private Button playButton;
	private Button optionsButton;
	private Button aboutButton;
	private Button facebookButton;
	private Button exitButton;	
	
	public MenuScreen(GameManager game) {
		super(game);
		
	}
	
	@Override
	public void show() {
		Gdx.input.setCatchBackKey(true);
		
		mainMenuStage = new Stage();
		mainMenuStage.setViewport(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT, true);
		mainMenuStage.getCamera().translate(-mainMenuStage.getGutterWidth(), -mainMenuStage.getGutterHeight(), 0);
		buildMainMenu();
		
//		GamePreferences.instance.loadMusic();
//		AudioManager.instance.play(ResourceManager.instance.music.MenuTheme);
	
	}

	@Override
	public void render(float deltaTime) {
		
		if(Gdx.input.isKeyPressed(Keys.BACK)){
			exitButton.toggle();
		}
		
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		mainMenuStage.act(deltaTime);
		mainMenuStage.draw();
		
	}

	private void buildMainMenu() {
		mainMenuSkin = new Skin(Gdx.files.internal(Constants.SKIN_MAINMENU), 
				new TextureAtlas(Constants.TEXTURE_MAIN_MENU_ATLAS));
		
		//MainMenu Layers
		Table layerBackground = buildBackgroundLayer();

		Stack stack = new Stack();
		stack.setSize(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		mainMenuStage.addActor(stack);
		stack.add(layerBackground);
		
		
		switch(Constants.DISPLAY_RESOLUTION){
			case FHD: buildFHDassets(); break;
			case HD: buildHDassets(); break;
			case LD: buildLDassets(); break;
		}
		
		playButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent arg0, Actor arg1) {
				onPlayClicked();
			}

		});
		
		optionsButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent arg0, Actor arg1) {
				onOptionsClicked();
			}
		});
		
		aboutButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent arg0, Actor arg1) {
				onAboutClicked();
			}
		});
		
		exitButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent arg0, Actor arg1) {
				onExitClicked();
			}

		});
		
		
		mainMenuStage.addActor(playButton);
		mainMenuStage.addActor(optionsButton);
		mainMenuStage.addActor(aboutButton);
		mainMenuStage.addActor(facebookButton);
		mainMenuStage.addActor(exitButton);		
	}
	
	private void buildFHDassets() {
		playButton = buildMainMenuButton(6.17f,3.45f,"Play");
		playButton.setSize(3.6f,1f);
		optionsButton = buildMainMenuButton(6.17f,4.74f,"Options");
		optionsButton.setSize(3.6f,1f);
		aboutButton = buildMainMenuButton(6.16f,5.91f,"About");
		aboutButton.setSize(3.6f,1f);
		facebookButton = buildMainMenuButton(11.15f,8.2f,"Face");
		facebookButton.setSize(1f,1f);
		exitButton = buildMainMenuButton(12.3f,8.2f,"Exit");
		exitButton.setSize(2f, 1f);
	}
	
	private void buildHDassets() {
		playButton = buildMainMenuButton(5.75f,4.6f,"Play");
		playButton.setSize(4.5f, 1.3f);
		optionsButton = buildMainMenuButton(5.75f,6.25f,"Options");
		optionsButton.setSize(4.5f, 1.3f);
		aboutButton = buildMainMenuButton(5.75f,7.7f,"About");
		aboutButton.setSize(4.5f, 1.3f);
		facebookButton = buildMainMenuButton(12.9f,6.75f,"Face");
		facebookButton.setSize(1.5f, 1.5f);
		exitButton = buildMainMenuButton(12.2f,8.5f,"Exit");
		exitButton.setSize(2.5f, 1.5f);
	}
	
	private void buildLDassets() {
		playButton = buildMainMenuButton(3.95f,4.5f,"Play");
		playButton.setSize(8f, 2f);
		optionsButton = buildMainMenuButton(3.95f,6.6f,"Options");
		optionsButton.setSize(8f, 2f);
		aboutButton = buildMainMenuButton(3.9f,8.45f,"About");
		aboutButton.setSize(8f, 2f);
		facebookButton = buildMainMenuButton(13.1f,7.4f,"Face");
		facebookButton.setSize(1.6f, 1.6f);
		exitButton = buildMainMenuButton(12.3f,8.7f,"Exit");
		exitButton.setSize(2.5f, 1.5f);
	}

	private Button buildMainMenuButton(float x,float y,String asset){
		Button button = new Button(mainMenuSkin,asset);
		button.setPosition(x, Constants.VIEWPORT_HEIGHT - y);
		return button;
	}


	private Table buildBackgroundLayer() {
		Table layer = new Table();
		imgMainMenuBackground = new Image(mainMenuSkin, "Background");
		layer.add(imgMainMenuBackground);
		return layer;
	}
	
	private void onPlayClicked(){
		ScreenTransition transition = ScreenTransitionFade.init(0.45f);
		game.setScreen(new WorldSelectorScreen(game), transition);
	}
	
	private void onOptionsClicked() {
		ScreenTransition transition = ScreenTransitionFade.init(0.45f);
		game.setScreen(new OptionsScreen(game), transition);
	}
	
	private void onAboutClicked() {
		ScreenTransition transition = ScreenTransitionFade.init(0.45f);
		game.setScreen(new AboutScreen(game), transition);
		
	}
	
	private void onExitClicked() {
		Gdx.app.exit();
	}

	@Override
	public void dispose() {
		mainMenuStage.dispose();
		mainMenuSkin.dispose();		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void resize(int width, int height) {
//		mainMenuStage.setViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT, true);
	}
	
	@Override
	public InputProcessor getInputProcessor () {
		return mainMenuStage;
	}

	@Override
	public void hide() {
		mainMenuStage.dispose();
		mainMenuSkin.dispose();			
	}

	@Override
	public void pause() {
	}

}
