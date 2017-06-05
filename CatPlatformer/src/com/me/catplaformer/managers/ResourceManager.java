package com.me.catplaformer.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.me.catplaformer.assets.AssetCat;
import com.me.catplaformer.assets.AssetFonts;
import com.me.catplaformer.assets.AssetLevelCommom;
import com.me.catplaformer.assets.AssetMusic;
import com.me.catplaformer.assets.AssetsLevel1and2;
import com.me.catplaformer.assets.level1.AssetEnemysLevel1;
import com.me.catplaformer.assets.level1.AssetLevel1Objects;
import com.me.catplaformer.assets.level2.AssetLevel2Objects;
import com.me.catplaformer.utils.Constants;

public class ResourceManager implements Disposable {

	public static final ResourceManager instance = new ResourceManager();
	private AssetManager assetManager;
	
	public AssetCat catAssets;
	public AssetFonts assetFonts;
	public AssetMusic music;
	
	//Level commom objects
	public AssetLevelCommom assetLevelCommom;
	
	//First levels falling platforms
	public AssetsLevel1and2 assetsLevel1and2;
	
	//Level 1
	public AssetLevel1Objects assetLevel1Objects;
	public AssetEnemysLevel1 assetEnemysLevel1;
	
	//Level 2
	public AssetLevel2Objects assetLevel2Objects;

	
	public ResourceManager() {
		assetManager = new AssetManager();
	}
	
	public void init () {
		assetManager = new AssetManager();
		assetFonts = new AssetFonts();
		
		assetManager.load(Constants.CAT_ATLAS, TextureAtlas.class);
		assetManager.finishLoading();
		
		TextureAtlas  atlasCat = assetManager.get(Constants.CAT_ATLAS);
		
		for (Texture t : atlasCat.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		
		catAssets = new AssetCat(atlasCat);	
	}
	
	public void loadMusicResources(){
		assetManager.load("Music/MenuTheme.mp3", Music.class);
		assetManager.load("Music/level1.mp3", Music.class);
		assetManager.finishLoading();
		music = new AssetMusic(assetManager);
	}
	
	public void createCommomResources(){
		TextureAtlas  commomObjects = assetManager.get(Constants.LEVEL_COMMOM_OBJECTS);
		TextureAtlas  waypointsAtlas  = assetManager.get(Constants.LEVEL_WAYPOINTS);
		
		for (Texture t : commomObjects.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		
		for (Texture t : waypointsAtlas.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		
		assetLevelCommom = new AssetLevelCommom(commomObjects,waypointsAtlas);
	}
	
	
	public void createLevel1and2FallingPlatforms(){
		TextureAtlas  level1and2Atlas = assetManager.get(Constants.LEVEL_1_2_OBJECTS);
		
		for (Texture t : level1and2Atlas.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		
		assetsLevel1and2 = new AssetsLevel1and2(level1and2Atlas);
	}
	
	public void createLevel1Resources(){
		
		TextureAtlas  level1Objects1 = assetManager.get(Constants.LEVEL1_OBJECTS);
	
		
		for (Texture t : level1Objects1.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}

		assetEnemysLevel1 = new AssetEnemysLevel1(level1Objects1);	
		assetLevel1Objects = new AssetLevel1Objects(level1Objects1);
		
		
	}
	
	public void createLevel2Resources(){
//		TextureAtlas  level2Objects1 = assetManager.get(Constants.LEVEL2_OBJECTS1);
//		
//		assetLevel2Objects = new AssetLevel2Objects(level2Objects1);
		
	}
	
	public AssetManager getAssetManager() {
		return assetManager;
	}
		
	@Override
	public void dispose() {
		assetManager.dispose();	
		assetManager = null;
	}

}
