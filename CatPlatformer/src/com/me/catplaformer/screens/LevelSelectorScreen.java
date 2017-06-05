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
import com.me.catplaformer.CatPlatformer;
import com.me.catplaformer.managers.GameManager;
import com.me.catplaformer.managers.ScreenManager;
import com.me.catplaformer.screens.transitions.ScreenTransition;
import com.me.catplaformer.screens.transitions.ScreenTransitionFade;
import com.me.catplaformer.utils.Constants;
import com.me.catplaformer.utils.GamePreferences;

public class LevelSelectorScreen extends ScreenManager{
	
	private Stage levelSelectorStage;
	private Skin levelSelectorSkin;
	
	private Image imgLevelSelectorBackground;
	
	private Button levelOneButton;
	private Button levelTwoButton;
	private Button levelThreeButton;
	private Button levelFourButton;
	private Button levelFiveButton;
	
	private Button backButton;
	
	private boolean isLevelTwoUnlocked = false;
	private boolean isLevelThreeUnlocked = false;
	private boolean isLevelFourUnlocked = false;
	private boolean isLevelFiveUnlocked = false;
	

	public LevelSelectorScreen(GameManager game) {
		super(game);
	}
	
	@Override
	public void show() {
		Gdx.input.setCatchBackKey(true);
		
		levelSelectorStage = new Stage();
		levelSelectorStage.setViewport(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT, true);
		levelSelectorStage.getCamera().translate(-levelSelectorStage.getGutterWidth(), -levelSelectorStage.getGutterHeight(), 0);
		buildLevelSelectorMenu();
		
//		if(AudioManager.instance.getPlayingMusic() != ResourceManager.instance.music.MenuTheme){
//			AudioManager.instance.onSettingsUpdated();
//			AudioManager.instance.stopMusic();
//			AudioManager.instance.play(ResourceManager.instance.music.MenuTheme);
//		}
		
	}

	@Override
	public void dispose() {
		levelSelectorStage.dispose();
		levelSelectorSkin.dispose();
	}

	@Override
	public void resume() {}

	@Override
	public void render(float deltaTime) {
		
		if(Gdx.input.isKeyPressed(Keys.BACK)){
			backButton.toggle();
		}
		
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		levelSelectorStage.act(deltaTime);
		levelSelectorStage.draw();
		
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void hide() {
		levelSelectorStage.dispose();
		levelSelectorSkin.dispose();
	}

	@Override
	public void pause() {}

	@Override
	public InputProcessor getInputProcessor() {
		return levelSelectorStage;
	}
	
	private void buildLevelSelectorMenu() {
		levelSelectorSkin = new Skin(Gdx.files.internal(Constants.SKIN_LEVEL_SELECTOR), 
				new TextureAtlas(Constants.TEXTURE_LEVEL_SELECTOR_ATLAS));

		Table layerBackground = buildBackgroundLayer();
		
		Stack stack = new Stack();
		levelSelectorStage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		stack.add(layerBackground);
		
		build();
		
		levelOneButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent arg0, Actor arg1) {
				onLevelOneSelected();
			}

		});
		
		backButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent arg0, Actor arg1) {				
				ScreenTransition transition = ScreenTransitionFade.init(0.45f);
				game.setScreen(new WorldSelectorScreen(game), transition);
			}

		});
		
		levelSelectorStage.addActor(levelOneButton);
		levelSelectorStage.addActor(levelTwoButton);
		levelSelectorStage.addActor(levelThreeButton);
		levelSelectorStage.addActor(levelFourButton);
		levelSelectorStage.addActor(levelFiveButton);
		levelSelectorStage.addActor(backButton);
		
	}

	private void build() {
		levelOneButton = buildLevelButton(3.15f,6.15f,"level1");
		levelOneButton.setSize(1.9f, 1.7f);
		levelTwoButton = buildLevelButton(5.1f,6.15f,isLevelTwoUnlocked ? "level2" : "levellock");
		levelTwoButton.setSize(1.9f, 1.7f);
		levelThreeButton = buildLevelButton(7.05f,6.15f,isLevelThreeUnlocked ? "level3" : "levellock");
		levelThreeButton.setSize(1.9f, 1.7f);
		levelFourButton = buildLevelButton(9f,6.15f,isLevelFourUnlocked ? "level4" : "levellock");
		levelFourButton.setSize(1.9f, 1.7f);
		levelFiveButton = buildLevelButton(11f,6.15f,isLevelFiveUnlocked ? "level5" : "levellock");
		levelFiveButton.setSize(1.9f, 1.7f);
		backButton = buildLevelButton(1.854f,8.34f,"Arrow");
		backButton.setSize(2f, 1f);
		
	}

	private Table buildBackgroundLayer() {
		Table layer = new Table();
		imgLevelSelectorBackground = new Image(levelSelectorSkin, "levelSelectorBackground");
		layer.add(imgLevelSelectorBackground);
		return layer;
	}

	private Button buildLevelButton(float x,float y,String asset) {
		Button button = new Button(levelSelectorSkin,asset);
//		button.setSize(GameUtils.ResizeImageWidth(button.getWidth()),
//		GameUtils.ResizeImageHeigth(button.getHeight()));

		button.setPosition(x, Constants.VIEWPORT_HEIGHT - y);

		return button;
	}
	
	private void onLevelOneSelected() {
		GamePreferences prefs = GamePreferences.instance;
		prefs.loadSettings();
		
		if(prefs.skipIntro){
			game.setScreen(new LoadScreen(game,1), null);
		}else{
			CatPlatformer.castVideo();
		}
		
	}
	
}
