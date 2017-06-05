package com.me.catplaformer.assets.level1;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class AssetEnemysLevel1 {
	
	public final AtlasRegion whiteDogDead;
	public final Animation whiteDogAnimWalking;
	public final Animation whiteDogAnimDead;
	
	public final AtlasRegion brownDogDead;
	public final Animation brownDogAnimWalking;
	public final Animation brownDogAnimDead;
	
	public final AtlasRegion redDogDead;
	public final Animation redDogAnimWalking;
	public final Animation redDogAnimBone;
	public final Animation redDogAnimDead;
	
	public final AtlasRegion dogBone;
	

	
	public AssetEnemysLevel1(TextureAtlas atlasMapObjects1) {
		Array<TextureRegion> regions = new Array<TextureRegion>();	
		
		regions.add(atlasMapObjects1.findRegion("whitedog1"));
		regions.add(atlasMapObjects1.findRegion("whitedog2"));
		regions.add(atlasMapObjects1.findRegion("whitedog3"));
		whiteDogAnimWalking = new Animation(0.3f, regions);	
		
		regions.clear();
		regions.add(atlasMapObjects1.findRegion("whitedog4"));
		regions.add(atlasMapObjects1.findRegion("whitedog5"));			
		whiteDogAnimDead = new Animation(0.6f, regions);
		whiteDogDead = atlasMapObjects1.findRegion("whitedog5");
		
		regions.clear();
		regions.add(atlasMapObjects1.findRegion("browndog1"));
		regions.add(atlasMapObjects1.findRegion("browndog2"));
		regions.add(atlasMapObjects1.findRegion("browndog3"));
		brownDogAnimWalking = new Animation(0.3f, regions);
		
		regions.clear();
		regions.add(atlasMapObjects1.findRegion("browndog4"));
		regions.add(atlasMapObjects1.findRegion("browndog5"));
		brownDogAnimDead = new Animation(0.6f, regions);
		
		brownDogDead = atlasMapObjects1.findRegion("browndog5");
		
		regions.clear();
		regions.add(atlasMapObjects1.findRegion("reddog1"));
		regions.add(atlasMapObjects1.findRegion("reddog2"));
		regions.add(atlasMapObjects1.findRegion("reddog3"));
		redDogAnimWalking = new Animation(0.3f, regions);
		
		regions.clear();
		regions.add(atlasMapObjects1.findRegion("reddog4"));
		regions.add(atlasMapObjects1.findRegion("reddog5"));
		redDogAnimDead = new Animation(0.6f, regions);
		
		regions.clear();
		regions.add(atlasMapObjects1.findRegion("reddog6"));
		regions.add(atlasMapObjects1.findRegion("reddog7"));
		redDogAnimBone = new Animation(0.3f, regions);
		redDogDead = atlasMapObjects1.findRegion("reddog5");
		
		dogBone = atlasMapObjects1.findRegion("bone");
		
		

	}
}
