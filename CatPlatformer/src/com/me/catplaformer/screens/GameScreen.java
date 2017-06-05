package com.me.catplaformer.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.me.catplaformer.controller.WorldController;
import com.me.catplaformer.managers.AudioManager;
import com.me.catplaformer.managers.GameManager;
import com.me.catplaformer.managers.ResourceManager;
import com.me.catplaformer.managers.ScreenManager;
import com.me.catplaformer.screens.transitions.ScreenTransition;
import com.me.catplaformer.screens.transitions.ScreenTransitionFade;
import com.me.catplaformer.utils.Constants;
import com.me.catplaformer.utils.GamePreferences;

public class GameScreen extends ScreenManager{

	private Stage HUDScreen;
	private Stage DeadMenu;
	private Stage SoundMenu;
	
	private Skin HUDskin;
	private Skin DeadScreenSkin;
	private Skin PauseMenuSkin;
	private Skin SoundMenuSkin;
	
	private Button HUDleftControl;
	private Button HUDrightControl;
	private Button HUDjumpControl;
	private Button HUDfishControl;
	private Button HUDclawControl;
	
	private Button DeadScreenHomeButton;
	private Button DeadScreenRestartButton;
	
	private Button SoundMenuMusicButton;
	private Button SoundMenuSoundButton;
	private Button SoundMenuMusicButtonOff;
	private Button SoundMenuSoundButtonOff;
	
	private Slider soundSlider;
	private Slider musicSlider;
	
	private Image HUDfishQuantity;
	private Image DeadScreenBackground;
	private Image SoundMenuBackground;
	private Image PauseMenuFlag;
	
	private WorldController worldController;
	
	private float deadScreenWaitTime;
	
	private boolean isDeadMenuCasted;
	private boolean isSoundMenuCasted;
	
	private int selectedLevel;
	
	private float HUDControlMediumCoordinate;
	private float HUDControlSecondMediumCoordinate;
	private float HUDControlsJumpCoordinate;
	private float HUDControlsAfterJumpCoordinate;
	private float HUDControlsWeaponCoordinate;
	
	public GameScreen(GameManager game, int levelNumber) {
		super(game);
		selectedLevel = levelNumber;		
	}
	
	@Override
	public void show() {
		worldController = new WorldController(game,selectedLevel);
		GamePreferences prefs = GamePreferences.instance;
		prefs.skipIntro = true;
		prefs.saveSettings();
		
		HUDScreen = new Stage();
		HUDScreen.setViewport(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT, false);
		
		DeadMenu = new Stage();
		DeadMenu.setViewport(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT, false);
		
		SoundMenu = new Stage();
		SoundMenu.setViewport(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT, false);
	
		isDeadMenuCasted = false;
		isSoundMenuCasted = false;
		
		HUDControlMediumCoordinate = Gdx.graphics.getWidth() / 2;
		HUDControlSecondMediumCoordinate = HUDControlMediumCoordinate / 2;
		HUDControlsJumpCoordinate = HUDControlMediumCoordinate*1.25f;
		HUDControlsAfterJumpCoordinate = HUDControlMediumCoordinate*1.7f;
		HUDControlsWeaponCoordinate = Gdx.graphics.getHeight()/1.6f;
		
		buildHUDscreen();
		buildDeadMenu();
		buildSoundMenu();
		
//		AudioManager.instance.onSettingsUpdated();
//		AudioManager.instance.stopMusic();
//		AudioManager.instance.play(ResourceManager.instance.music.level1);
		
		if(!Constants.isDesktop) buildHUDAndroidcontrols(); 
		GamePreferences gameProgress = GamePreferences.instance;
		gameProgress.loadGameProgress();
	}

	private void buildSoundMenu() {
		SoundMenuSkin = new Skin(Gdx.files.internal(Constants.SKIN_SOUND_MENU), 
				new TextureAtlas(Constants.TEXTURE_SOUND_MENU_ATLAS));
		
		Table layerBackground = buildSoundMenuBackground();

		Stack stack = new Stack();
		SoundMenu.addActor(stack);
		stack.setSize(6.11f, 3.92f);
		stack.setPosition(5.1f, 3.1f);
		stack.add(layerBackground);
		
		soundSlider = new Slider(0.0f, 1.0f, 0.1f, false,SoundMenuSkin);
		musicSlider = new Slider(0.0f, 1.0f, 0.1f, false,SoundMenuSkin);	
		
		switch(Constants.DISPLAY_RESOLUTION){
			case FHD: buildSoundMenuFHD();
		      	   	  break;
			case HD: buildSoundMenuHD();
			 	      break;
			case LD: buildSoundMenuHD();
			 	      break;
			default: break;
		}
		
		loadSettings();
		createSoundMenuListeners();
		
		SoundMenu.addActor(SoundMenuSoundButton);
		SoundMenu.addActor(SoundMenuMusicButton);
		SoundMenu.addActor(SoundMenuSoundButtonOff);
		SoundMenu.addActor(SoundMenuMusicButtonOff);
		
	}

	private void buildSoundMenuHD() {

		float resizeWidth = (float) ((2.781*1920)/(17.8f));
		float padTop = (float) ((0.464*1920)/(17.8f));
		
		Table wrapper = new Table();
		wrapper.add(soundSlider).width(resizeWidth);
		wrapper.row();
		wrapper.add(musicSlider).width(resizeWidth).padTop(padTop);
		wrapper.setTransform(true);
		wrapper.setScale(0.0108f,0.0108f);
		wrapper.setPosition(8.6f, 5f);
		
		SoundMenuSoundButton = buildSoundMenuButton(5.9f,4.73f,"sound");
		SoundMenuSoundButton.setSize(1f, 1f);
		SoundMenuSoundButtonOff = buildSoundMenuButton(5.9f,4.73f,"soundoff");
		SoundMenuSoundButtonOff.setSize(1f, 1f);
		SoundMenuMusicButton = buildSoundMenuButton(5.9f,6.12f,"music");
		SoundMenuMusicButton.setSize(1f, 1f);
		SoundMenuMusicButtonOff = buildSoundMenuButton(5.9f,6.12f,"musicOff");
		SoundMenuMusicButtonOff.setSize(1f, 1f);
		
		SoundMenu.addActor(wrapper);
	}

	private void buildSoundMenuFHD() {
//		float resizeWidth = (float) ((2.781*1920)/(17.8f));
		float resizeWidth = 280f;
		float padTop = (float) ((0.464*1920)/(17.8f));
		
		Table wrapper = new Table();
		wrapper.add(soundSlider).width(resizeWidth);
		wrapper.row();
		wrapper.add(musicSlider).width(resizeWidth).padTop(padTop);
		wrapper.setTransform(true);
		wrapper.setScale(0.0108f,0.0108f);
		wrapper.setPosition(8.5f, 5f);
		
		SoundMenuSoundButton = buildSoundMenuButton(5.9f,4.73f,"sound");
		SoundMenuSoundButton.setSize(1f, 1f);
		SoundMenuSoundButtonOff = buildSoundMenuButton(5.9f,4.73f,"soundoff");
		SoundMenuSoundButtonOff.setSize(1f, 1f);
		SoundMenuMusicButton = buildSoundMenuButton(5.9f,6.12f,"music");
		SoundMenuMusicButton.setSize(1f, 1f);
		SoundMenuMusicButtonOff = buildSoundMenuButton(5.9f,6.12f,"musicOff");
		SoundMenuMusicButtonOff.setSize(1f, 1f);
		
		SoundMenu.addActor(wrapper);
	}

	private void createSoundMenuListeners() {

		
		SoundMenu.addListener(new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(x < 5.4 || x > 12.4){
					switch(Constants.DISPLAY_RESOLUTION){
						case FHD: PauseMenuFlag.addAction(Actions.moveTo(0.93f, 9f, 1f));
					      	   	  break;
						case HD: PauseMenuFlag.addAction(Actions.moveTo(0.93f, 8.8f));
						 	   break;
						case LD: PauseMenuFlag.addAction(Actions.moveTo(0.93f, 8.8f));
						 	   break;
						default: break;
						}
					worldController.isGamePaused = false;
					isSoundMenuCasted = false;
					worldController.isSoundMenuCasted = false;
				}
				
				if(y < 2.8 || y > 7.2){
					switch(Constants.DISPLAY_RESOLUTION){
						case FHD: PauseMenuFlag.addAction(Actions.moveTo(0.93f, 9f)); 
					      	   	   break;
						case HD: PauseMenuFlag.addAction(Actions.moveTo(0.93f, 8.8f));
						 	       break;
						case LD: PauseMenuFlag.addAction(Actions.moveTo(0.93f, 8.8f));
						 	       break;
						default: break;
					}
					worldController.isGamePaused = false;
					isSoundMenuCasted = false;
					worldController.isSoundMenuCasted = false;
				}
				
				super.clicked(event, x, y);
			}
			
		});
		
		musicSlider.addListener( new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				saveSettings();
				AudioManager.instance.onSettingsUpdated();
				
			}
        });
		
		soundSlider.addListener( new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				saveSettings();
				AudioManager.instance.onSettingsUpdated();
				super.clicked(event, x, y);
			}

        } );
		
		SoundMenuSoundButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				SoundMenuSoundButton.setVisible(false);
				SoundMenuSoundButtonOff.setVisible(true);
				saveSettings();
				AudioManager.instance.onSettingsUpdated();
				super.clicked(event, x, y);
			}

		});
		
		SoundMenuMusicButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				SoundMenuMusicButton.setVisible(false);
				SoundMenuMusicButtonOff.setVisible(true);
				saveSettings();
				AudioManager.instance.onSettingsUpdated();
				super.clicked(event, x, y);
			}

		});
		
		SoundMenuSoundButtonOff.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				SoundMenuSoundButton.setVisible(true);
				SoundMenuSoundButtonOff.setVisible(false);
				saveSettings();
				AudioManager.instance.onSettingsUpdated();
				super.clicked(event, x, y);
			}

		});
		
		SoundMenuMusicButtonOff.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				SoundMenuMusicButton.setVisible(true);
				SoundMenuMusicButtonOff.setVisible(false);
				saveSettings();
				AudioManager.instance.onSettingsUpdated();
				super.clicked(event, x, y);
			}

		});
	}

	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(0,0,0,0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		worldController.update(deltaTime);
		worldController.render();

		handleControls();
	
		if(worldController.backPressed){
			 worldController.isGamePaused = true;
			switch(Constants.DISPLAY_RESOLUTION){
				case FHD:  if(Constants.SCREEN_NATIVE_HUD_HEIGHT > 1199) PauseMenuFlag.addAction(Actions.moveTo(0.93f, 5.8f, 1f));
				  		   else PauseMenuFlag.addAction(Actions.moveTo(0.93f, 5.8f, 1f));
				      	   break;
				case HD: PauseMenuFlag.addAction(Actions.moveTo(0.93f, 3.8f,1f));
					 	   break;
				case LD: PauseMenuFlag.addAction(Actions.moveTo(0.93f, 3.8f,1f));
					 	   break;
				default: break;
			}
		}
		
		if(worldController.currentLevel.isGameOver()){
			if(Gdx.input.getInputProcessor() != DeadMenu) Gdx.input.setInputProcessor(DeadMenu);
			deadScreenWaitTime += deltaTime;
			if(!isDeadMenuCasted) isDeadMenuCasted = true;
		}else{
			if(Gdx.input.getInputProcessor() != HUDScreen){
				Gdx.input.setInputProcessor(HUDScreen);
				worldController.resetAndroidControls();
			}
			
			if(deadScreenWaitTime != 0)deadScreenWaitTime = 0;
			if(isDeadMenuCasted){
				isDeadMenuCasted = false;
				worldController.deadMenuCasted = false;
			}
		}
		
	
		if(isDeadMenuCasted && deadScreenWaitTime > 1){
			if(!worldController.deadMenuCasted ) worldController.deadMenuCasted = true;
			DeadMenu.act(deltaTime);
			DeadMenu.draw();
		}else if(isSoundMenuCasted){
			if(Gdx.input.getInputProcessor() != SoundMenu) Gdx.input.setInputProcessor(SoundMenu);
			SoundMenu.act(deltaTime);
			SoundMenu.draw();
		}else{
			HUDScreen.act(deltaTime);
			HUDScreen.draw();
		}
		
	}
	
	private void handleControls() {
		
		if(!Gdx.input.isTouched(0) && !Gdx.input.isTouched(1)){
			worldController.isRightAndroidButtonPressed = false;
			worldController.isLeftAndroidButtonPressed = false;
			worldController.isJumpAndroidButtonPressed = false;
			worldController.isClawAndroidButtonPressed = false;
			worldController.isFishAndroidButtonPressed = false;
		}

		if((Gdx.input.isTouched(0) && Gdx.input.getX() > 0 && Gdx.input.getX() < HUDControlSecondMediumCoordinate) || 
			(Gdx.input.isTouched(1) && Gdx.input.getX(1) > 0 && Gdx.input.getX(1) < HUDControlSecondMediumCoordinate)){
			worldController.isLeftAndroidButtonPressed = true;
			worldController.isRightAndroidButtonPressed = false;
		}else if((Gdx.input.isTouched(0) && Gdx.input.getX() > HUDControlSecondMediumCoordinate && Gdx.input.getX() < HUDControlMediumCoordinate) || 
				(Gdx.input.isTouched(1) && Gdx.input.getX(1) > HUDControlSecondMediumCoordinate && Gdx.input.getX(1) < HUDControlMediumCoordinate)){
			worldController.isRightAndroidButtonPressed = true;
			worldController.isLeftAndroidButtonPressed = false;
		}else{
			worldController.isRightAndroidButtonPressed = false;
			worldController.isLeftAndroidButtonPressed = false;
		}
			
		if((Gdx.input.isTouched(0) && Gdx.input.getX() > HUDControlsJumpCoordinate && Gdx.input.getX() < HUDControlsAfterJumpCoordinate) || 
		   (Gdx.input.isTouched(1) && Gdx.input.getX(1) > HUDControlsJumpCoordinate && Gdx.input.getX(1) < HUDControlsAfterJumpCoordinate)){
			worldController.isJumpAndroidButtonPressed = true;
		}else{
			worldController.isJumpAndroidButtonPressed = false;
		}
		
		if((Gdx.input.isTouched(0) && Gdx.input.getX() > HUDControlsAfterJumpCoordinate && Gdx.input.getY() > HUDControlsWeaponCoordinate) || 
		   (Gdx.input.isTouched(1) && Gdx.input.getX(1) > HUDControlsAfterJumpCoordinate && Gdx.input.getY(1) > HUDControlsWeaponCoordinate)){
			worldController.isFishAndroidButtonPressed = true;
		}
		else
		{
			worldController.isFishAndroidButtonPressed = false;
		}
		
		if((Gdx.input.isTouched(0) && Gdx.input.getX() > HUDControlsAfterJumpCoordinate && Gdx.input.getY() < HUDControlsWeaponCoordinate) || 
			(Gdx.input.isTouched(1) && Gdx.input.getX(1) > HUDControlsAfterJumpCoordinate && Gdx.input.getY(1) < HUDControlsWeaponCoordinate)){
			worldController.isClawAndroidButtonPressed = true;
		}
		else
		{
			worldController.isClawAndroidButtonPressed = false;
		}
		
	}

	private void buildHUDscreen() {
		HUDskin = new Skin(Gdx.files.internal(Constants.SKIN_HUD_CONTROLS), 
				  new TextureAtlas(Constants.TEXTURE_HUD_CONTROLS_ATLAS));
		
		PauseMenuSkin = new Skin(Gdx.files.internal(Constants.SKIN_PAUSE_MENU), 
				  new TextureAtlas(Constants.TEXTURE_PAUSE_MENU_ATLAS));

		PauseMenuFlag = new Image(PauseMenuSkin,"minimenu");

		
		switch(Constants.DISPLAY_RESOLUTION){
			case FHD: PauseMenuFlag.setSize(1.71f, 4.3f);
					  PauseMenuFlag.setPosition(0.93f,9f);
				      createPauseMenuFHDListener(); 
					  HUDfishQuantity = new Image(HUDskin,"fishsky-fhd");
					  HUDfishQuantity.setSize(0.67f, 0.92f);
					  HUDfishQuantity.setPosition(7f,9f);
					  break;
			case HD: PauseMenuFlag.setSize(2.72f, 6.76f);
					 PauseMenuFlag.setPosition(0.93f, 8.8f);
					 createPauseMenuHDListener();
					 HUDfishQuantity = new Image(HUDskin,"fishsky-hd");
					 HUDfishQuantity.setSize(0.73f,1.02f);
					 HUDfishQuantity.setPosition(7f,8.85f);
					 break;
			case LD: PauseMenuFlag.setSize(2.72f, 6.76f);
					 PauseMenuFlag.setPosition(0.93f, 8.8f);
					 createPauseMenuHDListener();
					 HUDfishQuantity = new Image(HUDskin,"fishsky-hd");
					 HUDfishQuantity.setSize(0.73f,1.02f);
					 HUDfishQuantity.setPosition(7f,9f);
					 break;
			default: break;
		}

		HUDScreen.addActor(PauseMenuFlag);
		HUDScreen.addActor(HUDfishQuantity);
		
	}

	private void createPauseMenuFHDListener() {
		PauseMenuFlag.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if( y < 0.74){
					 if(!worldController.isGamePaused){
						 PauseMenuFlag.addAction(Actions.moveTo(0.93f, 5.8f, 1f));
						 worldController.isGamePaused = true;
					 }else{
						 PauseMenuFlag.addAction(Actions.moveTo(0.93f, 9f, 1f));
						 worldController.isGamePaused = false;
					 }
				}else if( x > 0.3 && x < 1.4){
						
						if(y > 3.15 && y < 4){
							ScreenTransition transition = ScreenTransitionFade.init(0.75f);
							game.setScreen(new LevelSelectorScreen(game), transition);
						}
						
						if(y > 2.3 && y < 3.1){
							Gdx.input.setInputProcessor(SoundMenu);
							loadSettings();
							isSoundMenuCasted = true;
							worldController.isSoundMenuCasted = true;
						}
						
						if(y > 1.5 && y < 2.1){
							worldController.level1.resetLevel();
							PauseMenuFlag.addAction(Actions.moveTo(0.93f, 9f, 1f));
							worldController.isGamePaused = false;
						}
						
						if(y < 1.4){
							PauseMenuFlag.addAction(Actions.moveTo(0.93f, 9f, 1f));
							worldController.isGamePaused = false;
							worldController.backPressed = false;
						}
						
					
					}
				super.clicked(event, x, y);
			}

		});	
		
	}
	
	private void createPauseMenuHDListener() {
		PauseMenuFlag.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				if( y < 1.15f){
					 if(!worldController.isGamePaused){
						 PauseMenuFlag.addAction(Actions.moveTo(0.93f, 3.8f, 1f));
						 worldController.isGamePaused = true;
					 }else{
						 PauseMenuFlag.addAction(Actions.moveTo(0.93f, 8.8f, 1f));
						 worldController.isGamePaused = false;
					 }
				}else if( x > 0.5f && x < 2.3f){
						
						if(y > 4.9f && y < 6.2f){
							ScreenTransition transition = ScreenTransitionFade.init(0.75f);
							game.setScreen(new LevelSelectorScreen(game), transition);
						}
						
						if(y > 3.7 && y < 4.8){
							Gdx.input.setInputProcessor(SoundMenu);
							loadSettings();
							isSoundMenuCasted = true;
							worldController.isSoundMenuCasted = true;
						}
						
						if(y > 2.4 && y < 3.5){
							worldController.level1.resetLevel();
							PauseMenuFlag.addAction(Actions.moveTo(0.93f, 8.8f, 1f));
							worldController.isGamePaused = false;
						}
						
						if(y> 1.2 && y < 2.3){
							PauseMenuFlag.addAction(Actions.moveTo(0.93f, 8.8f, 1f));
							worldController.isGamePaused = false;
							worldController.backPressed = false;
						}
						
					
					}
				super.clicked(event, x, y);
			}

		});	
	}
	
	private void buildDeadMenu() {		
		DeadScreenSkin = new Skin(Gdx.files.internal(Constants.SKIN_DEAD_MENU), 
				new TextureAtlas(Constants.TEXTURE_DEAD_MENU_ATLAS));
		
		Table layerBackground = buildDeadMenuBackground();
		
		Stack stack = new Stack();
		DeadMenu.addActor(stack);
		stack.setSize(4.83f, 3f);
		stack.setPosition(5.8f, 3.5f);
		stack.add(layerBackground);
		
		switch(Constants.DISPLAY_RESOLUTION){
			case FHD: 
					 DeadScreenHomeButton = buildDeadScreenButton(6.55f,5.45f,"deadMenuHouse");
				  	 DeadScreenRestartButton = buildDeadScreenButton(8.42f,5.45f,"deadMenuRestart");
				  	 break;
			case HD: DeadScreenHomeButton = buildDeadScreenButton(6.55f,5.45f,"deadMenuHouse");
				 	 DeadScreenRestartButton = buildDeadScreenButton(8.42f,5.45f,"deadMenuRestart");
				 	 break;
			case LD: DeadScreenHomeButton = buildDeadScreenButton(6.55f,5.45f,"deadMenuHouse");
				 	 DeadScreenRestartButton = buildDeadScreenButton(8.42f,5.45f,"deadMenuRestart"); 
				 	 break;
			}
	
		DeadScreenHomeButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent arg0, Actor arg1) {
				ScreenTransition transition = ScreenTransitionFade.init(0.75f);
				game.setScreen(new LevelSelectorScreen(game), transition);
			}

		});
		
		DeadScreenRestartButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent arg0, Actor arg1) {				
				worldController.level1.resetLevel();
			}

		});
		
		DeadMenu.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
			}

		});
		
		DeadMenu.addActor(DeadScreenHomeButton);
		DeadMenu.addActor(DeadScreenRestartButton);
		isDeadMenuCasted = true;
	}
	
	private Table buildDeadMenuBackground() {
		Table layer = new Table();
		DeadScreenBackground = new Image(DeadScreenSkin, "deadMenu");
		layer.add(DeadScreenBackground);
		return layer;
	}
	
	private Table buildSoundMenuBackground() {
		Table layer = new Table();
		SoundMenuBackground = new Image(SoundMenuSkin, "MiniMenuSound");
		layer.add(SoundMenuBackground);
		return layer;
	}
	
	private Button buildSoundMenuButton(float x,float y,String asset) {
		Button button = new Button(SoundMenuSkin,asset);
//		button.setSize(GameUtils.ResizeImageWidth(button.getWidth()), 
//					  GameUtils.ResizeImageHeigth(button.getHeight()));
		button.setPosition(x, Constants.VIEWPORT_HEIGHT - y);

		return button;
	}
	
	private Button buildDeadScreenButton(float x,float y,String asset) {
		Button button = new Button(DeadScreenSkin,asset);
//		button.setSize(GameUtils.ResizeImageWidth(button.getWidth()), 
//				  GameUtils.ResizeImageHeigth(button.getHeight()));
		button.setPosition(x, Constants.VIEWPORT_HEIGHT - y);

		return button;
	}

	private void buildHUDAndroidcontrols() {

		switch(Constants.DISPLAY_RESOLUTION){
			case FHD: HUDleftControl = buildHUDAndroidButton(-0.5f,-0.35f,"left");
					  HUDleftControl.setSize(4.5f, 4.5f);
			 		  HUDrightControl = buildHUDAndroidButton(3f,-0.35f,"right");
			 		  HUDrightControl.setSize(4.5f, 4.5f);
			 		  HUDclawControl = buildHUDAndroidButton(13f,3.5f,"claw");
			 		  HUDclawControl.setSize(3f,4f);
			 		  HUDjumpControl = buildHUDAndroidButton(9.5f,-0.5f,"up");
			 		  HUDjumpControl.setSize(4.5f, 4.5f);
			 		  HUDfishControl = buildHUDAndroidButton(14.2f,0f,"fish");
			 		  HUDfishControl.setSize(2f, 4f);
			      	  break;
			case HD: HUDleftControl = buildHUDAndroidButton(0.5f,0.5f,"left");
					 HUDleftControl.setSize(1.6f, 1.7f);
				     HUDrightControl = buildHUDAndroidButton(5f,0.5f,"right");
				     HUDrightControl.setSize(1.6f, 1.7f);
				     HUDclawControl = buildHUDAndroidButton(14f,5f,"claw");
				     HUDclawControl.setSize(2.1f,2.1f);
				     HUDjumpControl = buildHUDAndroidButton(11f,0.5f,"up");
				     HUDjumpControl.setSize(1.6f, 1.7f);
				     HUDfishControl = buildHUDAndroidButton(14.5f,1.5f,"fish");
				     HUDfishControl.setSize(1.6f, 1.7f);
				 	 break;
			case LD: HUDleftControl = buildHUDAndroidButton(-0.4f,-0.3f,"left");
					 HUDleftControl.setSize(1.6f, 1.7f);
		     		 HUDrightControl = buildHUDAndroidButton(3f,-0.3f,"right");
		     		 HUDrightControl.setSize(1.6f, 1.7f);
		     		 HUDclawControl = buildHUDAndroidButton(13f,3.5f,"claw");
		     		 HUDclawControl.setSize(2.1f,2.1f);
		     		 HUDjumpControl = buildHUDAndroidButton(9.5f,-0.5f,"up");
		     		 HUDjumpControl.setSize(1.6f, 1.7f);
		     		 HUDfishControl = buildHUDAndroidButton(14.5f,2.5f,"fish");
		     		 HUDfishControl.setSize(1.6f, 1.7f);
				 	 break;
			default: break;
		}
			
			HUDScreen.addActor(HUDleftControl);
			HUDScreen.addActor(HUDrightControl);
			HUDScreen.addActor(HUDjumpControl);
			HUDScreen.addActor(HUDfishControl);
			HUDScreen.addActor(HUDclawControl);
			
	
	}	

	private Button buildHUDAndroidButton(float x, float y, String asset) {
		Button button = new Button(HUDskin,asset);
		button.setPosition(x,y);
		return button;
	}
	
	private void loadSettings() {
		GamePreferences prefs = GamePreferences.instance;
		prefs.loadMusic();
		soundSlider.setValue(prefs.volSound);
		musicSlider.setValue(prefs.volMusic);
		
		if(prefs.music){
			SoundMenuMusicButton.setVisible(true);
			SoundMenuMusicButtonOff.setVisible(false);
		}else{
			SoundMenuMusicButton.setVisible(false);
			SoundMenuMusicButtonOff.setVisible(true);
		}
		
		if(prefs.sound){
			SoundMenuSoundButton.setVisible(true);
			SoundMenuSoundButtonOff.setVisible(false);
		}else{
			SoundMenuSoundButton.setVisible(false);
			SoundMenuSoundButtonOff.setVisible(true);
		}
	}

	private void saveSettings() {
		GamePreferences prefs = GamePreferences.instance;
		prefs.sound = SoundMenuSoundButton.isVisible();
		prefs.volSound = soundSlider.getValue();
		prefs.music = SoundMenuMusicButton.isVisible();
		prefs.volMusic = musicSlider.getValue();
		prefs.saveMusic();
	}

	@Override
	public void dispose() {
		worldController.dispose();
		HUDScreen.dispose();
		DeadMenu.dispose();
		SoundMenu.dispose();
		DeadScreenSkin.dispose();
		HUDskin.dispose();
		SoundMenuSkin.dispose();
		PauseMenuSkin.dispose();
		ResourceManager.instance.dispose();
	}

	@Override
	public void resume() {
		super.resume();
		worldController.isGamePaused = false;
	}
	
	@Override
	public void resize(int width, int height) {
		HUDScreen.setViewport(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT, false);
	}

	@Override
	public void hide() {
		worldController.dispose();
		HUDScreen.dispose();
		DeadMenu.dispose();
		SoundMenu.dispose();
		DeadScreenSkin.dispose();
		HUDskin.dispose();
		SoundMenuSkin.dispose();
		PauseMenuSkin.dispose();
		ResourceManager.instance.dispose();
	}

	@Override
	public void pause() {
		worldController.isGamePaused = true;
	}

	@Override
	public InputProcessor getInputProcessor() {
		return HUDScreen;
	}
}
