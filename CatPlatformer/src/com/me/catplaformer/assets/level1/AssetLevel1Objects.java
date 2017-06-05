package com.me.catplaformer.assets.level1;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

public class AssetLevel1Objects {
	
	public final Animation waterAnimation;

	public AssetLevel1Objects(TextureAtlas atlasMapObjects2){

		Array<AtlasRegion> regions = new Array<AtlasRegion>();
		
		regions.clear();
		regions.add(atlasMapObjects2.findRegion("water1"));
		regions.add(atlasMapObjects2.findRegion("water2"));
		waterAnimation = new Animation(0.2f, regions);
	}
}
