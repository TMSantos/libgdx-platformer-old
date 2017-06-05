package com.me.catplaformer.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.me.catplaformer.controller.levels.Level;
import com.me.catplaformer.controller.levels.world1.Level1;
import com.me.catplaformer.controller.levels.world1.Level2;
import com.me.catplaformer.managers.GameManager;
import com.me.catplaformer.managers.ResourceManager;
import com.me.catplaformer.objects.Cat;
import com.me.catplaformer.screens.DemoEndScreen;
import com.me.catplaformer.screens.transitions.ScreenTransition;
import com.me.catplaformer.screens.transitions.ScreenTransitionFade;
import com.me.catplaformer.shaders.VignetteShader;
import com.me.catplaformer.utils.CameraHelper;
import com.me.catplaformer.utils.Constants;
import com.me.catplaformer.utils.GamePreferences;

public class WorldController extends InputAdapter implements Disposable{
	
	public GameManager game;
	
	public CameraHelper cameraHelper;
	
	public OrthographicCamera cameraHUDinformation;
	private OrthographicCamera camera;
	
	private SpriteBatch batch;
	
	private World levelWorld;
	public Cat cat;
	
	public Level currentLevel;
	
	public Level1 level1;
	public Level2 level2;
	
	private int levelNumber;
	
	public boolean backPressed;
	public boolean isGamePaused;
	
	public boolean deadMenuCasted;
	public boolean isSoundMenuCasted;
	
	private BitmapFont fpsFont;
	private BitmapFont mScore;
	private BitmapFont mFishQuantity;

	private Box2DDebugRenderer PhysicsRenderer;

	private boolean showFPS;
	
	public boolean isRightAndroidButtonPressed = false;
	public boolean isLeftAndroidButtonPressed = false;
	public boolean isJumpAndroidButtonPressed = false;
	public boolean isClawAndroidButtonPressed = false;
	public boolean isFishAndroidButtonPressed = false;
	
	private VignetteShader vignetteShader;
	
	public WorldController (GameManager game, int level) {
		this.game = game;
		Gdx.input.setCatchBackKey(true);
		
		batch = new SpriteBatch();
		
		cameraHUDinformation = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		cameraHUDinformation.position.set(0, 0, 0);
		cameraHUDinformation.setToOrtho(true); 
		cameraHUDinformation.update();
		
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT);
		camera.position.set(0,0,0);
		camera.update();
		
		ShaderProgram.pedantic = false;
		vignetteShader = new VignetteShader();

		levelNumber = level;
		
		createHUD();
		
		GamePreferences prefs = GamePreferences.instance;
		prefs.loadSettings();
		showFPS = prefs.showFPS;
		
		PhysicsRenderer = new Box2DDebugRenderer();	
		
		initLevel();
	}
	
	private void initLevel(){
		cameraHelper = new CameraHelper();
		levelWorld = new World(new Vector2(0, -9.8f), true);
		initCat();
		cameraHelper.setTarget(cat);
		
		switch(levelNumber){
			case 1:loadLevel1(); break;
			case 2:loadLevel2(); break; 
		}
		
		currentLevel.backgroundCamera = cameraHUDinformation;
	}	
	
	public void loadLevel1(){
		isGamePaused = false;
		backPressed = false;
		level1 = new Level1(levelWorld,cat);
		currentLevel = level1;
	}
	
	public void loadLevel2(){
		isGamePaused = false;
		backPressed = false;
		level2 = new Level2(levelWorld,cat);
		currentLevel = level2;
	}
	
	private void initCat() {
		cat = new Cat(levelWorld);
	}
	
	public void update (float deltaTime) {
		
		if(currentLevel.startEndGameTimer > 3f){
			currentLevel.endGame = false;
			
			currentLevel.startEndGameTimer = 0;
			ScreenTransition transition = ScreenTransitionFade.init(2f);
			game.setScreen(new DemoEndScreen(game), transition);
		}
		
		handleDebugInput(deltaTime);
		if(isGamePaused){return;}
		levelWorld.step(deltaTime, 8, 3);
		
		if(!currentLevel.isGameOver()){
			handleCatControl(deltaTime);
			if(!cameraHelper.shouldUpdate) cameraHelper.shouldUpdate = true;
		}else{
			if(cameraHelper.shouldUpdate) cameraHelper.shouldUpdate = false;
		}
		
		currentLevel.update(deltaTime);
		
		cameraHelper.update(deltaTime);
	}
	
	public void render() {	
		if(deadMenuCasted || isSoundMenuCasted){
			batch.setShader(vignetteShader.getVignetteShader());
			vignetteShader.render();
			renderWorld(batch,vignetteShader);
		}else{
			batch.setShader(null);
			renderWorld(batch,null);
		}

		
		renderGui(batch);
	}	
	
	public Level getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(Level currentLevel) {
		this.currentLevel = currentLevel;
	}

	private void handleDebugInput(float deltaTime) {
		
		float camMoveSpeed = 5 * deltaTime;
		
		if (Gdx.input.isKeyPressed(Keys.LEFT)) moveCamera(-camMoveSpeed, 0);
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) moveCamera(camMoveSpeed, 0);
		if (Gdx.input.isKeyPressed(Keys.UP)) moveCamera(0, camMoveSpeed);
		if (Gdx.input.isKeyPressed(Keys.DOWN)) moveCamera(0, -camMoveSpeed);
		
		// Camera Controls (zoom)
		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camZoomSpeed *= camZoomSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Keys.PLUS)) cameraHelper.addZoom(camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.MINUS)) cameraHelper.addZoom(-camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.SLASH)) cameraHelper.setZoom(1);
		
	
	}
	
	private void moveCamera (float x, float y) {
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}
	
	public void resetAndroidControls(){
		isRightAndroidButtonPressed = false;
		isLeftAndroidButtonPressed = false;
		isJumpAndroidButtonPressed = false;
		isClawAndroidButtonPressed = false;
		isFishAndroidButtonPressed = false;
	}
	
	private void handleCatControl(float deltaTime) {
		cat.velocity = cat.body.getLinearVelocity();
		cat.position = cat.body.getPosition();
		
		cat.capMaxVelocity();
 
		if((!isLeftAndroidButtonPressed && !Gdx.input.isKeyPressed(Keys.A)) && 
		   (!isRightAndroidButtonPressed && !Gdx.input.isKeyPressed(Keys.D))) {			
			cat.stand();
		}
	
		if( (isLeftAndroidButtonPressed || Gdx.input.isKeyPressed(Keys.A)) && cat.isMaxVelocity(-1)) {
			cat.moveLeft();
		}

		if((isRightAndroidButtonPressed || Gdx.input.isKeyPressed(Keys.D)) && cat.isMaxVelocity(1)) {
			cat.moveRight();
		}

		
		if(isClawAndroidButtonPressed || Gdx.input.isKeyPressed(Keys.K)){
			cat.Scratch();			
		}
		
		if(isFishAndroidButtonPressed || Gdx.input.isKeyPressed(Keys.J)){
			cat.shoot();
		}
		
		if(Gdx.input.isKeyPressed(Keys.BACK)){
			if(!backPressed) backPressed = true;
		}
		
		if(!isFishAndroidButtonPressed && !Gdx.input.isKeyPressed(Keys.J)){
			cat.canCastFish(true);
		}
		
		if(isJumpAndroidButtonPressed || Gdx.input.isKeyPressed(Keys.SPACE)){
			cat.jump();
		}
	}
	
	private void createHUD() {
		fpsFont = ResourceManager.instance.assetFonts.defaultNormal;
		
		if(Constants.SCREEN_NATIVE_HUD >= 1280){
			mScore = ResourceManager.instance.assetFonts.defaultBig;
			mFishQuantity = ResourceManager.instance.assetFonts.defaultBig;
		}else if(Constants.SCREEN_NATIVE_HUD >= 800 && Constants.SCREEN_NATIVE_HUD < 1280){
			mScore = ResourceManager.instance.assetFonts.defaultNormal;
			mFishQuantity = ResourceManager.instance.assetFonts.defaultNormal;
		}else if(Constants.SCREEN_NATIVE_HUD < 800){
			mScore = ResourceManager.instance.assetFonts.defaultSmall;
			mFishQuantity = ResourceManager.instance.assetFonts.defaultSmall;
		}
	}
	
	private void renderWorld(SpriteBatch batch, VignetteShader shader) {	
		cameraHelper.applyTo(camera);
//		currentLevel.render(batch, camera,shader);
		PhysicsRenderer.render(currentLevel.levelWorld, camera.combined);	
	}
	
	private void renderGui(SpriteBatch batch) {
		batch.setProjectionMatrix(cameraHUDinformation.combined);
		batch.begin();
		renderGuiFpsCounter(batch);
		batch.end();
	}
	
	private void renderGuiFpsCounter (SpriteBatch batch) {
		float fpsX = 35;
		float scoreX = (float) ((12.5 * Constants.SCREEN_NATIVE_HUD) /( Constants.VIEWPORT_WIDTH)) ;
		float quantityX = (float) ((8 * Constants.SCREEN_NATIVE_HUD) /( Constants.VIEWPORT_WIDTH)) ;
		float y = cameraHUDinformation.viewportHeight - 50;
		
		int fps = Gdx.graphics.getFramesPerSecond();
		
		if (fps >= 45) {
			fpsFont.setColor(0, 1, 0, 1);
		} else if (fps >= 30) {
			fpsFont.setColor(1, 1, 0, 1);
		} else {
			fpsFont.setColor(1, 0, 0, 1);
		}

		
		if(showFPS){
			fpsFont.draw(batch, "fps: " + fps, fpsX, y);
			fpsFont.setColor(1, 1, 1, 1);
		}

		mScore.setColor(1, 1, 1, 1); 
		mScore.draw(batch, "score: " + currentLevel.getScore(), scoreX, 15);
		mFishQuantity.setColor(0, 0, 0, 1);
		mFishQuantity.draw(batch, "x "+ cat.getFishQuantity(), quantityX, 15);
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		currentLevel.dispose();
		fpsFont.dispose();
		mFishQuantity.dispose();
		mScore.dispose();
	}

}
