package com.me.catplaformer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.me.catplaformer.managers.AudioManager;
import com.me.catplaformer.managers.GameManager;
import com.me.catplaformer.managers.ScreenManager;
import com.me.catplaformer.screens.transitions.ScreenTransition;
import com.me.catplaformer.screens.transitions.ScreenTransitionFade;
import com.me.catplaformer.utils.Constants;
import com.me.catplaformer.utils.GamePreferences;

public class OptionsScreen extends ScreenManager{

	private Stage optionsScreen;
	private Skin optionsScreenSkin;
	
	private Slider optionsSoundSlider;
	private Slider optionsMusicSlider;
	
	private CheckBox chkIntroAnimation;
	private CheckBox chkFPScounter;
	
	private Button optionsMenuMusicButton;
	private Button optionsMenuSoundButton;
	private Button optionsMenuMusicButtonOff;
	private Button optionsMenuSoundButtonOff;
	
	private Button backButton;
	
	private Image optionsScreenBackground;
	
	public OptionsScreen(GameManager game) {
		super(game);
	}
	
	@Override
	public void show() {
		Gdx.input.setCatchBackKey(true);
		
		optionsScreen = new Stage();
		optionsScreen.setViewport(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT, true);
		optionsScreen.getCamera().translate(-optionsScreen.getGutterWidth(), -optionsScreen.getGutterHeight(), 0);
		
		buildOptionsScreen();
	}

	private void buildOptionsScreen() {
		optionsScreenSkin = new Skin(Gdx.files.internal(Constants.SKIN_OPTIONS_MENU), 
				new TextureAtlas(Constants.TEXTURE_OPTIONS_MENU_ATLAS));
		
		buildBackground();
		switch(Constants.DISPLAY_RESOLUTION){
		case FHD: buildFHDMenu();
				  break;
		case HD:  buildHDMenu();	
		  		  break;
		case LD:  buildLDMenu();	
		  		  break;
		}
		
		loadSettings();
		createOptionsMenuListeners();
		
	}

	private void buildFHDMenu() {
		optionsSoundSlider = new Slider(0.0f, 1.0f, 0.1f, false,optionsScreenSkin);
		optionsMusicSlider = new Slider(0.0f, 1.0f, 0.1f, false,optionsScreenSkin);

		Table wrapper = new Table();
		wrapper.add(optionsSoundSlider).width(215);
		wrapper.row();
		wrapper.add(optionsMusicSlider).width(215).padTop(118.65f);
		wrapper.setTransform(true);
		wrapper.setScale(0.0108f,0.0108f);
		wrapper.setPosition(8.7f, 4.65f);
		
		optionsMenuSoundButton = buildSoundMenuButton(6.3f,4.7f,"sound");
		optionsMenuSoundButton.setSize(1f, 1f);
		optionsMenuSoundButtonOff = buildSoundMenuButton(6.3f,4.7f,"soundoff");
		optionsMenuSoundButtonOff.setSize(1f, 1f);
		optionsMenuMusicButton = buildSoundMenuButton(6.5f,6.9f,"music");
		optionsMenuMusicButton.setSize(1f,1f);
		optionsMenuMusicButtonOff = buildSoundMenuButton(6.5f,6.9f,"musicOff");
		optionsMenuMusicButtonOff.setSize(1f,1f);
		backButton = buildSoundMenuButton(2f,8.5f,"backbutton");
		backButton.setSize(2f,1f);

		chkIntroAnimation = new CheckBox("",optionsScreenSkin);
		
		Table chkTable = new Table();
		chkTable.add(chkIntroAnimation).width(215);
		chkTable.setTransform(true);
		chkTable.setScale(0.0108f,0.0108f);
		chkTable.setPosition(12f, 4.9f);
		
		chkFPScounter = new CheckBox("",optionsScreenSkin);
		Table chkFPSTable = new Table();
		chkFPSTable.add(chkFPScounter).width(215);
		chkFPSTable.setTransform(true);
		chkFPSTable.setScale(0.0108f,0.0108f);
		chkFPSTable.setPosition(5f, 4.9f);
	
		optionsScreen.addActor(wrapper);
		optionsScreen.addActor(optionsMenuSoundButton);
		optionsScreen.addActor(optionsMenuMusicButton);
		optionsScreen.addActor(optionsMenuSoundButtonOff);
		optionsScreen.addActor(optionsMenuMusicButtonOff);
		optionsScreen.addActor(chkTable);
		optionsScreen.addActor(chkFPSTable);
		optionsScreen.addActor(backButton);
		
	}
	
	private void buildHDMenu() {
		optionsSoundSlider = new Slider(0.0f, 1.0f, 0.1f, false,optionsScreenSkin);
		optionsMusicSlider = new Slider(0.0f, 1.0f, 0.1f, false,optionsScreenSkin);

		Table wrapper = new Table();
		wrapper.add(optionsSoundSlider).width(160);
		wrapper.row();
		wrapper.add(optionsMusicSlider).width(160).padTop(85);
		wrapper.setTransform(true);
		wrapper.setScale(0.015f,0.012f);
		wrapper.setPosition(8.7f, 4.65f);
		
		optionsMenuSoundButton = buildSoundMenuButton(6.2f,4.9f,"sound");
		optionsMenuSoundButton.setSize(1f, 1.2f);
		optionsMenuSoundButtonOff = buildSoundMenuButton(6.2f,4.9f,"soundoff");
		optionsMenuSoundButtonOff.setSize(1f, 1.2f);
		optionsMenuMusicButton = buildSoundMenuButton(6.3f,7f,"music");
		optionsMenuMusicButton.setSize(1f, 1.2f);
		optionsMenuMusicButtonOff = buildSoundMenuButton(6.3f,7f,"musicOff");
		optionsMenuMusicButtonOff.setSize(1f, 1.2f);
		backButton = buildSoundMenuButton(2f,8.5f,"backbutton");
		backButton.setSize(2.5f, 1.2f);

		chkIntroAnimation = new CheckBox("",optionsScreenSkin);
		
		Table chkTable = new Table();
		chkTable.add(chkIntroAnimation).width(220);
		chkTable.setTransform(true);
		chkTable.setScale(0.0108f,0.0108f);
		chkTable.setPosition(11.8f, 4.9f);
		
		chkFPScounter = new CheckBox("",optionsScreenSkin);
		Table chkFPSTable = new Table();
		chkFPSTable.add(chkFPScounter).width(220);
		chkFPSTable.setTransform(true);
		chkFPSTable.setScale(0.0108f,0.0108f);
		chkFPSTable.setPosition(5.1f, 4.95f);
	
		optionsScreen.addActor(wrapper);
		optionsScreen.addActor(optionsMenuSoundButton);
		optionsScreen.addActor(optionsMenuMusicButton);
		optionsScreen.addActor(optionsMenuSoundButtonOff);
		optionsScreen.addActor(optionsMenuMusicButtonOff);
		optionsScreen.addActor(chkTable);
		optionsScreen.addActor(chkFPSTable);
		optionsScreen.addActor(backButton);
		
	}
	
	private void buildLDMenu() {
		optionsSoundSlider = new Slider(0.0f, 1.0f, 0.1f, false,optionsScreenSkin);
		optionsMusicSlider = new Slider(0.0f, 1.0f, 0.1f, false,optionsScreenSkin);
		
		Table wrapper = new Table();
		wrapper.add(optionsSoundSlider);
		wrapper.row();
		wrapper.add(optionsMusicSlider).padTop(58.6f);
		wrapper.setTransform(true);
		wrapper.setScale(0.025f,0.02f);
		wrapper.setPosition(9f, 4.45f);
		
		optionsMenuSoundButton = buildSoundMenuButton(6f,4.9f,"sound");
		optionsMenuSoundButton.setSize(1f, 1f);
		optionsMenuSoundButtonOff = buildSoundMenuButton(6f,4.9f,"soundoff");
		optionsMenuSoundButtonOff.setSize(1f, 1f);
		optionsMenuMusicButton = buildSoundMenuButton(6.1f,7.1f,"music");
		optionsMenuMusicButton.setSize(1f, 1f);
		optionsMenuMusicButtonOff = buildSoundMenuButton(6.1f,7.1f,"musicOff");
		optionsMenuMusicButtonOff.setSize(1f, 1f);
		backButton = buildSoundMenuButton(2f,8.5f,"backbutton");
		backButton.setSize(2f, 1f);
		

		chkIntroAnimation = new CheckBox("",optionsScreenSkin);
		
		Table chkTable = new Table();
		chkTable.add(chkIntroAnimation);
		chkTable.setTransform(true);
		chkTable.setScale(0.02f,0.02f);
		chkTable.setPosition(11.5f, 4.7f);
		
		chkFPScounter = new CheckBox("",optionsScreenSkin);
		Table chkFPSTable = new Table();
		chkFPSTable.add(chkFPScounter);
		chkFPSTable.setTransform(true);
		chkFPSTable.setScale(0.02f,0.02f);
		chkFPSTable.setPosition(5.2f, 4.7f);
		
		optionsScreen.addActor(wrapper);
		optionsScreen.addActor(optionsMenuSoundButton);
		optionsScreen.addActor(optionsMenuMusicButton);
		optionsScreen.addActor(optionsMenuSoundButtonOff);
		optionsScreen.addActor(optionsMenuMusicButtonOff);
		optionsScreen.addActor(chkTable);
		optionsScreen.addActor(chkFPSTable);
		optionsScreen.addActor(backButton);
		
	}

	private void createOptionsMenuListeners() {
		optionsMusicSlider.addListener( new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				saveMusic();
				AudioManager.instance.onSettingsUpdated();
				
			}
        });
		
		optionsSoundSlider.addListener( new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				saveMusic();
				AudioManager.instance.onSettingsUpdated();
				super.clicked(event, x, y);
			}

        } );
		
		optionsMenuSoundButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				optionsMenuSoundButton.setVisible(false);
				optionsMenuSoundButtonOff.setVisible(true);
				saveMusic();
				AudioManager.instance.onSettingsUpdated();
				super.clicked(event, x, y);
			}

		});
		
		optionsMenuMusicButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				optionsMenuMusicButton.setVisible(false);
				optionsMenuMusicButtonOff.setVisible(true);
				saveMusic();
				AudioManager.instance.onSettingsUpdated();
				super.clicked(event, x, y);
			}

		});
		
		optionsMenuSoundButtonOff.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				optionsMenuSoundButton.setVisible(true);
				optionsMenuSoundButtonOff.setVisible(false);
				saveMusic();
				AudioManager.instance.onSettingsUpdated();
				super.clicked(event, x, y);
			}

		});
		
		optionsMenuMusicButtonOff.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				optionsMenuMusicButton.setVisible(true);
				optionsMenuMusicButtonOff.setVisible(false);
				saveMusic();
				AudioManager.instance.onSettingsUpdated();
				super.clicked(event, x, y);
			}

		});
		
		chkIntroAnimation.addListener(new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				saveSettings();
				super.clicked(event, x, y);
			}
			
		});
		
		chkFPScounter.addListener(new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				saveSettings();
				super.clicked(event, x, y);
			}
			
		});
		
		backButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent arg0, Actor arg1) {				
				ScreenTransition transition = ScreenTransitionFade.init(0.45f);
				game.setScreen(new MenuScreen(game), transition);
			}

		});
		
	}

	private void buildBackground() {
		optionsScreenBackground = new Image(optionsScreenSkin, "optionsbackground");
		Stack background = new Stack();
		background.setSize(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT);
		background.addActor(optionsScreenBackground);
		
		optionsScreen.addActor(background);
	}

	private Button buildSoundMenuButton(float x,float y,String asset) {
		Button button = new Button(optionsScreenSkin,asset);
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
		
		optionsScreen.act(deltaTime);
		optionsScreen.draw();
	}
	
	private void loadSettings() {
		GamePreferences prefs = GamePreferences.instance;
		prefs.loadMusic();
		optionsSoundSlider.setValue(prefs.volSound);
		optionsMusicSlider.setValue(prefs.volMusic);
		
		if(prefs.music){
			optionsMenuMusicButton.setVisible(true);
			optionsMenuMusicButtonOff.setVisible(false);
		}else{
			optionsMenuMusicButton.setVisible(false);
			optionsMenuMusicButtonOff.setVisible(true);
		}
		
		if(prefs.sound){
			optionsMenuSoundButton.setVisible(true);
			optionsMenuSoundButtonOff.setVisible(false);
		}else{
			optionsMenuSoundButton.setVisible(false);
			optionsMenuSoundButtonOff.setVisible(true);
		}
		
		prefs.loadSettings();
		
		chkIntroAnimation.setChecked(prefs.skipIntro);
		chkFPScounter.setChecked(prefs.showFPS);
		
	}
	
	private void saveMusic() {
		GamePreferences prefs = GamePreferences.instance;
		prefs.sound = optionsMenuSoundButton.isVisible();
		prefs.volSound = optionsSoundSlider.getValue();
		prefs.music = optionsMenuMusicButton.isVisible();
		prefs.volMusic = optionsMusicSlider.getValue();
		prefs.saveMusic();
		
	}
	
	private void saveSettings(){
		GamePreferences prefs = GamePreferences.instance;
		prefs.skipIntro = chkIntroAnimation.isChecked();
		prefs.showFPS = chkFPScounter.isChecked();
		prefs.saveSettings();
	}

	@Override
	public void resize(int width, int height) {
		
	}
	
	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public InputProcessor getInputProcessor() {
		return optionsScreen;
	}

}
