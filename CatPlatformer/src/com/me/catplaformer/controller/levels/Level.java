package com.me.catplaformer.controller.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TmxMapLoader.Parameters;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.me.catplaformer.controller.tmxToBox2d.LevelMapInformation;
import com.me.catplaformer.controller.tmxToBox2d.TmxToBox2d;
import com.me.catplaformer.objects.Cat;
import com.me.catplaformer.objects.Waypoint;
import com.me.catplaformer.objects.collectItems.Diamond;
import com.me.catplaformer.objects.collectItems.PuzzlePiece;
import com.me.catplaformer.shaders.VignetteShader;
import com.me.catplaformer.utils.Constants;

public abstract class Level implements Disposable{
	
	public TiledMap map;
	public TmxToBox2d tmxToBox2d;
	
	public OrthographicCamera backgroundCamera;
	public OrthogonalTiledMapRenderer renderer;
	
	public Texture backGroundTexture;
	public Sprite backGroundSprite;
	
	public int mScore;
	
	public World levelWorld;
	public Cat levelCat;
	
	public boolean mIsGameOver;
	public boolean puzzlePieceCollected;
	
	public boolean endGame;
	public float startEndGameTimer;
	
	public Array<Diamond> diamonds;
	public Array<Waypoint> waypoints;
	
	public PuzzlePiece puzzlePiece;
	
	public float unitScale;
	
	public LevelMapInformation levelMapCommonPhysics;
	
	public Level() {
		unitScale = 108f;
	}
	
	public void loadMap(int levelNumber){
		TmxMapLoader.Parameters parameters = new TmxMapLoader.Parameters();
		parameters.textureMinFilter = TextureFilter.Linear;
		parameters.textureMagFilter = TextureFilter.Linear;
		
		switch(levelNumber){
			case 1: loadLevel1Map(parameters); break;
			case 2: loadLevel2Map(parameters); break;
		}
				
		tmxToBox2d.loadLines(true,true);
		
		backGroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		renderer = new OrthogonalTiledMapRenderer(map,1/unitScale);
		
		backGroundSprite = new Sprite(backGroundTexture);
		backGroundSprite.setPosition(0, 0);
		
	}
	
	public boolean isGameOver() {
		return mIsGameOver;
	}
	
	public void setGameOver(boolean isGameOver) {
		mIsGameOver = isGameOver;
	}

	public abstract void update(float deltaTime);
	public abstract void render(SpriteBatch batch, OrthographicCamera camera, VignetteShader shader);
	public abstract int getScore();
	public abstract void resetLevel();
	public abstract void endGame();
	public abstract void dispose();
	
	private void loadLevel1Map(Parameters parameters) {
		map = new TmxMapLoader().load(Constants.LEVEL_01,parameters); 
		tmxToBox2d = new TmxToBox2d(Constants.LEVEL_01,levelWorld,unitScale);
		backGroundTexture = new Texture(Gdx.files.internal("levels/world1/level1/map/background.jpg"));
		
	}
	
	private void loadLevel2Map(Parameters parameters) {
		map = new TmxMapLoader().load(Constants.LEVEL_02,parameters); 
		tmxToBox2d = new TmxToBox2d(Constants.LEVEL_02,levelWorld,unitScale);
		backGroundTexture = new Texture(Gdx.files.internal("levels/world1/level2/map/background.jpg"));
		
	}
	
	private void loadLevel3Map(Parameters parameters) {
//		map = new TmxMapLoader().load(Constants.LEVEL_01,parameters); 
//		tmxToBox2d = new TmxToBox2d(Constants.LEVEL_01,levelWorld,unitScale);
//		backGroundTexture = new Texture(Gdx.files.internal("levels/world1/level1/Map/background.jpg"));
		
	}
	
	private void loadLevel4Map(Parameters parameters) {
//		map = new TmxMapLoader().load(Constants.LEVEL_01,parameters); 
//		tmxToBox2d = new TmxToBox2d(Constants.LEVEL_01,levelWorld,unitScale);
//		backGroundTexture = new Texture(Gdx.files.internal("levels/world1/level1/Map/background.jpg"));
		
	}
	
	private void loadLevel5Map(Parameters parameters) {
//		map = new TmxMapLoader().load(Constants.LEVEL_01,parameters); 
//		tmxToBox2d = new TmxToBox2d(Constants.LEVEL_01,levelWorld,unitScale);
//		backGroundTexture = new Texture(Gdx.files.internal("levels/world1/level1/Map/background.jpg"));
		
	}
	
	private void loadLevel6Map(Parameters parameters) {
//		map = new TmxMapLoader().load(Constants.LEVEL_01,parameters); 
//		tmxToBox2d = new TmxToBox2d(Constants.LEVEL_01,levelWorld,unitScale);
//		backGroundTexture = new Texture(Gdx.files.internal("levels/world1/level1/Map/background.jpg"));
		
	}
	
	private void loadLevel7Map(Parameters parameters) {
//		map = new TmxMapLoader().load(Constants.LEVEL_01,parameters); 
//		tmxToBox2d = new TmxToBox2d(Constants.LEVEL_01,levelWorld,unitScale);
//		backGroundTexture = new Texture(Gdx.files.internal("levels/world1/level1/Map/background.jpg"));
		
	}
	
	private void loadLevel8Map(Parameters parameters) {
//		map = new TmxMapLoader().load(Constants.LEVEL_01,parameters); 
//		tmxToBox2d = new TmxToBox2d(Constants.LEVEL_01,levelWorld,unitScale);
//		backGroundTexture = new Texture(Gdx.files.internal("levels/world1/level1/Map/background.jpg"));
		
	}
	
	private void loadLevel9Map(Parameters parameters) {
//		map = new TmxMapLoader().load(Constants.LEVEL_01,parameters); 
//		tmxToBox2d = new TmxToBox2d(Constants.LEVEL_01,levelWorld,unitScale);
//		backGroundTexture = new Texture(Gdx.files.internal("levels/world1/level1/Map/background.jpg"));
		
	}
	
	private void loadLevel10Map(Parameters parameters) {
//		map = new TmxMapLoader().load(Constants.LEVEL_01,parameters); 
//		tmxToBox2d = new TmxToBox2d(Constants.LEVEL_01,levelWorld,unitScale);
//		backGroundTexture = new Texture(Gdx.files.internal("levels/world1/level1/Map/background.jpg"));
		
	}
	
	private void loadLevel11Map(Parameters parameters) {
//		map = new TmxMapLoader().load(Constants.LEVEL_01,parameters); 
//		tmxToBox2d = new TmxToBox2d(Constants.LEVEL_01,levelWorld,unitScale);
//		backGroundTexture = new Texture(Gdx.files.internal("levels/world1/level1/Map/background.jpg"));
		
	}
	
	private void loadLevel12Map(Parameters parameters) {
//		map = new TmxMapLoader().load(Constants.LEVEL_01,parameters); 
//		tmxToBox2d = new TmxToBox2d(Constants.LEVEL_01,levelWorld,unitScale);
//		backGroundTexture = new Texture(Gdx.files.internal("levels/world1/level1/Map/background.jpg"));
		
	}
	
	private void loadLevel13Map(Parameters parameters) {
//		map = new TmxMapLoader().load(Constants.LEVEL_01,parameters); 
//		tmxToBox2d = new TmxToBox2d(Constants.LEVEL_01,levelWorld,unitScale);
//		backGroundTexture = new Texture(Gdx.files.internal("levels/world1/level1/Map/background.jpg"));
		
	}
	
	private void loadLevel14Map(Parameters parameters) {
//		map = new TmxMapLoader().load(Constants.LEVEL_01,parameters); 
//		tmxToBox2d = new TmxToBox2d(Constants.LEVEL_01,levelWorld,unitScale);
//		backGroundTexture = new Texture(Gdx.files.internal("levels/world1/level1/Map/background.jpg"));
		
	}
	
	private void loadLevel15Map(Parameters parameters) {
//		map = new TmxMapLoader().load(Constants.LEVEL_01,parameters); 
//		tmxToBox2d = new TmxToBox2d(Constants.LEVEL_01,levelWorld,unitScale);
//		backGroundTexture = new Texture(Gdx.files.internal("levels/world1/level1/Map/background.jpg"));
		
	}
	
	private void loadLevel16Map(Parameters parameters) {
//		map = new TmxMapLoader().load(Constants.LEVEL_01,parameters); 
//		tmxToBox2d = new TmxToBox2d(Constants.LEVEL_01,levelWorld,unitScale);
//		backGroundTexture = new Texture(Gdx.files.internal("levels/world1/level1/Map/background.jpg"));
		
	}
	
	private void loadLevel17Map(Parameters parameters) {
//		map = new TmxMapLoader().load(Constants.LEVEL_01,parameters); 
//		tmxToBox2d = new TmxToBox2d(Constants.LEVEL_01,levelWorld,unitScale);
//		backGroundTexture = new Texture(Gdx.files.internal("levels/world1/level1/Map/background.jpg"));
		
	}
	
	private void loadLevel18Map(Parameters parameters) {
//		map = new TmxMapLoader().load(Constants.LEVEL_01,parameters); 
//		tmxToBox2d = new TmxToBox2d(Constants.LEVEL_01,levelWorld,unitScale);
//		backGroundTexture = new Texture(Gdx.files.internal("levels/world1/level1/Map/background.jpg"));
		
	}
	
	private void loadLevel19Map(Parameters parameters) {
//		map = new TmxMapLoader().load(Constants.LEVEL_01,parameters); 
//		tmxToBox2d = new TmxToBox2d(Constants.LEVEL_01,levelWorld,unitScale);
//		backGroundTexture = new Texture(Gdx.files.internal("levels/world1/level1/Map/background.jpg"));
		
	}
	
	private void loadLevel20Map(Parameters parameters) {
//		map = new TmxMapLoader().load(Constants.LEVEL_01,parameters); 
//		tmxToBox2d = new TmxToBox2d(Constants.LEVEL_01,levelWorld,unitScale);
//		backGroundTexture = new Texture(Gdx.files.internal("levels/world1/level1/Map/background.jpg"));
		
	}

}
