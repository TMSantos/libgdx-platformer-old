package com.me.catplaformer.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

public class AssetLevelCommom {
	
	public final AtlasRegion birdDead;
	public final Animation birdAnimWalking;
	
	public final AtlasRegion bulletFish;
	public final AtlasRegion collectibleFish;
	public final AtlasRegion PuzzlePiece;
	
	public final Animation diamondAnimation;
	public final Animation redDiamondAnimation;
	
	public final Animation waypoint1;
	public final AtlasRegion waypoint1first;
	public final AtlasRegion waypoint1final;
	
	public final Animation waypoint2;
	public final AtlasRegion waypoint2first;
	public final AtlasRegion waypoint2final;
	
	public final Animation waypoint3;
	public final AtlasRegion waypoint3first;
	public final AtlasRegion waypoint3final;
	
	public final Animation waypoint4;
	public final AtlasRegion waypoint4first;
	public final AtlasRegion waypoint4final;
	
	public final Animation endWaypointBegin;
	public final Animation endWaypointExplosion;
	public final AtlasRegion endWaypointFirst;
	public final AtlasRegion endWaypointFinal;
	
	public AssetLevelCommom(TextureAtlas commomObjects, TextureAtlas waypointAtlas){

		bulletFish = commomObjects.findRegion("bulletFish");
		collectibleFish = commomObjects.findRegion("collectFish");
		
		PuzzlePiece = commomObjects.findRegion("PuzzlePiece");
		
		Array<AtlasRegion> regions = new Array<AtlasRegion>();
		regions.add(commomObjects.findRegion("bird1"));
		regions.add(commomObjects.findRegion("bird2"));
		regions.add(commomObjects.findRegion("bird3"));
		regions.add(commomObjects.findRegion("bird4"));
		regions.add(commomObjects.findRegion("bird5"));
		regions.add(commomObjects.findRegion("bird6"));
		
		birdAnimWalking = new Animation(0.2f, regions);
		
		birdDead = commomObjects.findRegion("bird7");
		
		regions.clear();
		regions.add(commomObjects.findRegion("diamond1"));
		regions.add(commomObjects.findRegion("diamond2"));
		regions.add(commomObjects.findRegion("diamond3"));
		diamondAnimation = new Animation(0.3f, regions);
		
		regions.clear();
		regions.add(commomObjects.findRegion("RedDiamond1"));
		regions.add(commomObjects.findRegion("RedDiamond2"));
		regions.add(commomObjects.findRegion("RedDiamond3"));
		redDiamondAnimation = new Animation(0.3f, regions);
		
		regions.clear();
		regions.add(waypointAtlas.findRegion("waypoint1-1"));
		regions.add(waypointAtlas.findRegion("waypoint1-2"));
		regions.add(waypointAtlas.findRegion("waypoint1-3"));
		regions.add(waypointAtlas.findRegion("waypoint1-4"));
		waypoint1 = new Animation(0.3f, regions);
		
		waypoint1first = waypointAtlas.findRegion("waypoint1-1");
		waypoint1final = waypointAtlas.findRegion("waypoint1-4");
		
		regions.clear();
		regions.add(waypointAtlas.findRegion("waypoint1-4"));
		regions.add(waypointAtlas.findRegion("waypoint2-1"));
		regions.add(waypointAtlas.findRegion("waypoint2-2"));
		regions.add(waypointAtlas.findRegion("waypoint2-3"));
		waypoint2 = new Animation(0.3f, regions);
		
		waypoint2first = waypointAtlas.findRegion("waypoint1-4");
		waypoint2final = waypointAtlas.findRegion("waypoint2-3");
		
		regions.clear();
		regions.add(waypointAtlas.findRegion("waypoint2-3"));
		regions.add(waypointAtlas.findRegion("waypoint3-1"));
		regions.add(waypointAtlas.findRegion("waypoint3-2"));
		waypoint3 = new Animation(0.3f, regions);
		
		waypoint3first = waypointAtlas.findRegion("waypoint2-3");
		waypoint3final = waypointAtlas.findRegion("waypoint3-2");
		
		regions.clear();
		regions.add(waypointAtlas.findRegion("waypoint3-2"));
		regions.add(waypointAtlas.findRegion("waypoint4-1"));
		regions.add(waypointAtlas.findRegion("waypoint4-2"));
		waypoint4 = new Animation(0.3f, regions);
		
		waypoint4first = waypointAtlas.findRegion("waypoint3-2");
		waypoint4final = waypointAtlas.findRegion("waypoint4-2");
		
		regions.clear();
		regions.add(waypointAtlas.findRegion("endWaypoint1"));
		regions.add(waypointAtlas.findRegion("endWaypoint2"));
		regions.add(waypointAtlas.findRegion("endWaypoint3"));
		regions.add(waypointAtlas.findRegion("endWaypoint4"));
		regions.add(waypointAtlas.findRegion("endWaypoint5"));
		endWaypointBegin = new Animation(0.15f, regions);
		
		regions.clear();
		regions.add(waypointAtlas.findRegion("endWaypoint6"));
		regions.add(waypointAtlas.findRegion("endWaypoint7"));
		regions.add(waypointAtlas.findRegion("endWaypoint8"));
		regions.add(waypointAtlas.findRegion("endWaypoint9"));
		regions.add(waypointAtlas.findRegion("endWaypoint10"));
		regions.add(waypointAtlas.findRegion("endWaypoint11"));
		regions.add(waypointAtlas.findRegion("endWaypoint12"));
		regions.add(waypointAtlas.findRegion("endWaypoint13"));
		regions.add(waypointAtlas.findRegion("endWaypoint14"));
		regions.add(waypointAtlas.findRegion("endWaypoint15"));
		regions.add(waypointAtlas.findRegion("endWaypoint16"));
		regions.add(waypointAtlas.findRegion("endWaypoint17"));
		regions.add(waypointAtlas.findRegion("endWaypoint18"));
		regions.add(waypointAtlas.findRegion("endWaypoint19"));
		regions.add(waypointAtlas.findRegion("endWaypoint20"));
		regions.add(waypointAtlas.findRegion("endWaypoint21"));
		regions.add(waypointAtlas.findRegion("endWaypoint22"));
		regions.add(waypointAtlas.findRegion("endWaypoint23"));
		regions.add(waypointAtlas.findRegion("endWaypoint24"));
		endWaypointExplosion = new Animation(0.05f, regions);
		
		endWaypointFirst = waypointAtlas.findRegion("waypoint4-2");
		endWaypointFinal = waypointAtlas.findRegion("endWaypoint24");
	}
}
