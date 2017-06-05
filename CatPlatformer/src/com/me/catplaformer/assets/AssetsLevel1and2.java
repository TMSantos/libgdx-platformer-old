package com.me.catplaformer.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

public class AssetsLevel1and2 {
	
		public final AtlasRegion movingPlatform1;
		public final AtlasRegion movingPlatform2;
		public final AtlasRegion movingPlatform3;
		public final AtlasRegion movingPlatform4;
		
		public final Animation enemyCatWalk;
		public final Animation enemyCatPsychoWalk;
		public final Animation enemyCatDying;

		public final AtlasRegion enemyCatWounded;
		
		public final AtlasRegion enemyBulletFish;
		
		
		public AssetsLevel1and2(TextureAtlas level1and2){
			
			movingPlatform1 = level1and2.findRegion("FallingPlatform1");
			movingPlatform2 = level1and2.findRegion("FallingPlatform2");
			movingPlatform3 = level1and2.findRegion("FallingPlatform3");
			movingPlatform4 = level1and2.findRegion("FallingPlatform4");
			
			enemyBulletFish = level1and2.findRegion("MeanFish");
			
			Array<TextureRegion> regions = new Array<TextureRegion>();	
			
			regions.add(level1and2.findRegion("EnemyCat1"));
			regions.add(level1and2.findRegion("EnemyCat2"));
			regions.add(level1and2.findRegion("EnemyCat1"));
			regions.add(level1and2.findRegion("EnemyCat3"));
			enemyCatWalk = new Animation(0.3f, regions);
			
			regions.clear();
			regions.add(level1and2.findRegion("EnemyCat4"));
			regions.add(level1and2.findRegion("EnemyCat5"));
			regions.add(level1and2.findRegion("EnemyCat4"));
			regions.add(level1and2.findRegion("EnemyCat6"));
			enemyCatPsychoWalk = new Animation(0.3f, regions);
			
			regions.clear();
			regions.add(level1and2.findRegion("EnemyCat7"));
			regions.add(level1and2.findRegion("EnemyCat8"));
			regions.add(level1and2.findRegion("EnemyCat9"));
			regions.add(level1and2.findRegion("EnemyCat10"));
			enemyCatDying = new Animation(0.3f, regions);
			
			enemyCatWounded = level1and2.findRegion("EnemyCat11");
			
		}
}
