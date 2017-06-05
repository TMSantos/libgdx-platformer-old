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

public class WorldSelectorScreen extends ScreenManager{
	
	private Stage worldSelectorStage;
	private Skin worldSelectorSkin;
	
	private Image imgWorldSelectorBackground;
	
	private Button worldOneButton;
	private Button worldTwoButton;
	private Button worldThreeButton;
	private Button worldFourButton;
	private Button backButton;
	
	private boolean isWorldTwoUnlocked = false;
	private boolean isWorldThreeUnlocked = false;
	private boolean isWorldFourUnlocked = false;
	
	public WorldSelectorScreen(GameManager game) {
		super(game);
	}

	@Override
	public void show() {
		Gdx.input.setCatchBackKey(true);
		
		worldSelectorStage = new Stage();
		worldSelectorStage.setViewport(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT, true);
		worldSelectorStage.getCamera().translate(-worldSelectorStage.getGutterWidth(), -worldSelectorStage.getGutterHeight(), 0);
		buildWorldSelectorMenu();
	}
	
	private void buildWorldSelectorMenu() {
		worldSelectorSkin = new Skin(Gdx.files.internal(Constants.SKIN_WORLD_SELECTOR), 
							new TextureAtlas(Constants.TEXTURE_WORLD_SELECTOR_SCREEN_ATLAS));
		
		Table layerBackground = buildBackgroundLayer();
		
		switch(Constants.DISPLAY_RESOLUTION){
			case FHD: buildFHD(); break;
			case HD: buildMD(); break;
			case LD: buildMD(); break;
		}
		
		
		Stack stack = new Stack();
		worldSelectorStage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		stack.add(layerBackground);
		
		worldOneButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent arg0, Actor arg1) {
				onWorldOneSelected();
			}

		});
		
		backButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent arg0, Actor arg1) {
				ScreenTransition transition = ScreenTransitionFade.init(0.45f);
				game.setScreen(new MenuScreen(game), transition);
			}

		});
		
		worldSelectorStage.addActor(worldOneButton);
		worldSelectorStage.addActor(worldTwoButton);
		worldSelectorStage.addActor(worldThreeButton);
		worldSelectorStage.addActor(worldFourButton);
		worldSelectorStage.addActor(backButton);
		
	}

	private void buildFHD() {
		worldOneButton = buildWorldButton(3.3f,6.2f,"world1");
		worldOneButton.setSize(2.3f, 2.1f);
		worldTwoButton = buildWorldButton(5.67f,6.2f,isWorldTwoUnlocked ? "world2" : "worldlock");
		worldTwoButton.setSize(2.3f, 2.1f);
		worldThreeButton = buildWorldButton(8.1f,6.2f,isWorldThreeUnlocked ? "world3" : "worldlock");
		worldThreeButton.setSize(2.3f, 2.1f);
		worldFourButton = buildWorldButton(10.5f,6.2f,isWorldFourUnlocked ? "world4" : "worldlock");
		worldFourButton.setSize(2.3f, 2.1f);
		backButton = buildWorldButton(1.9f,8.4f,"Arrow");
		backButton.setSize(2f,1f);
		
	}
	
	private void buildMD() {
		worldOneButton = buildWorldButton(3.25f,6.2f,"world1");
		worldOneButton.setSize(2.3f, 2.1f);
		worldTwoButton = buildWorldButton(5.65f,6.2f,isWorldTwoUnlocked ? "world2" : "worldlock");
		worldTwoButton.setSize(2.3f, 2.1f);
		worldThreeButton = buildWorldButton(8.05f,6.2f,isWorldThreeUnlocked ? "world3" : "worldlock");
		worldThreeButton.setSize(2.3f, 2.1f);
		worldFourButton = buildWorldButton(10.45f,6.2f,isWorldFourUnlocked ? "world4" : "worldlock");
		worldFourButton.setSize(2.3f, 2.1f);
		backButton = buildWorldButton(1.9f,8.4f,"Arrow");
		backButton.setSize(2f,1f);
		
	}
	
	private Table buildBackgroundLayer() {
		Table layer = new Table();
		imgWorldSelectorBackground = new Image(worldSelectorSkin, "WorldSelectorScreenBackground");
		layer.add(imgWorldSelectorBackground);
		return layer;
	}
	
	private Button buildWorldButton(float x,float y, String asset) {
		Button button = new Button(worldSelectorSkin,asset);
		button.setPosition(x, Constants.VIEWPORT_HEIGHT - y);

		return button;
	}

	@Override
	public void render(float deltaTime) {
		
		if(Gdx.input.isKeyPressed(Keys.BACK)){
			backButton.toggle();
		}
		
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		worldSelectorStage.act(deltaTime);
		worldSelectorStage.draw();
		
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void dispose() {
		worldSelectorStage.dispose();
		worldSelectorSkin.dispose();
	}

	@Override
	public void resume() {}

	@Override
	public void hide() {
		worldSelectorStage.dispose();
		worldSelectorSkin.dispose();
	}

	@Override
	public void pause() {}

	@Override
	public InputProcessor getInputProcessor() {
		return worldSelectorStage;
	}
	
	private void onWorldOneSelected() {
		ScreenTransition transition = ScreenTransitionFade.init(0.45f);
		game.setScreen(new LevelSelectorScreen(game), transition);
		}


}
