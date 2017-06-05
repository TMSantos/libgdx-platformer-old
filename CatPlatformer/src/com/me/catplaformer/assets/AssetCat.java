package com.me.catplaformer.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

public class AssetCat {
	
	public final AtlasRegion cat;
	public final AtlasRegion dead;
	public final AtlasRegion climbing;
	
	public final Animation animStanding;
	public final Animation animWalking;
	public final Animation animDead;
	public final Animation animClaw;

	public AssetCat(TextureAtlas atlas) {
		cat = atlas.findRegion("jump");
		dead = atlas.findRegion("dead2");
		climbing = atlas.findRegion("back");

		Array<AtlasRegion> regions = null;
		
		regions = new Array<AtlasRegion>();
		regions.add(atlas.findRegion("stand1"));
		regions.add(atlas.findRegion("stand2"));
		animStanding = new Animation(3f, regions);
				
		regions = new Array<AtlasRegion>();
		regions.add(atlas.findRegion("walk1"));
		regions.add(atlas.findRegion("walk2"));
		animWalking = new Animation(0.3f, regions);		
		
		regions = new Array<AtlasRegion>();
		regions.add(atlas.findRegion("dead1"));
		regions.add(atlas.findRegion("dead2"));
		animDead = new Animation(1f, regions);
		
		regions = new Array<AtlasRegion>();
		regions.add(atlas.findRegion("claw1"));
		regions.add(atlas.findRegion("claw2"));
		regions.add(atlas.findRegion("claw3"));
		regions.add(atlas.findRegion("claw4"));
		animClaw = new Animation(0.1f, regions);
	}
}
