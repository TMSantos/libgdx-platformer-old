package com.me.catplaformer.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.me.catplaformer.managers.ResourceManager;

public class Waypoint extends CatPlatformerGameObject{

	private Animation waypoint;
	private TextureRegion regNormal;
	
	private boolean startAnimationTimer;
	private float animationTimer;
	private float endAnimationTimer;
	private int waypointNumber;
	
	public Waypoint(World levelWorld,int number,float x, float y){
		createWaypointSensor(levelWorld,number,x);
		dimension.set(7f, 3.5f);
		origin.set(dimension.x/2, dimension.y/2);
		position.x = x-3.2f;
		position.y = y-1.6f;
		waypointNumber = number;
		animationTimer = 0;
		endAnimationTimer = 0;
		init(levelWorld);
	}
	
	private void init(World levelWorld) {
		switch(waypointNumber){
		 
			case 1 : 	waypoint = ResourceManager.instance.assetLevelCommom.waypoint1;
						regNormal = ResourceManager.instance.assetLevelCommom.waypoint1first;
						break;
			case 2 : 	waypoint = ResourceManager.instance.assetLevelCommom.waypoint2;
						regNormal = ResourceManager.instance.assetLevelCommom.waypoint2first;
						break;
			case 3 : 	waypoint = ResourceManager.instance.assetLevelCommom.waypoint3;
						regNormal = ResourceManager.instance.assetLevelCommom.waypoint3first;
						break;
			case 4 : 	waypoint = ResourceManager.instance.assetLevelCommom.waypoint4;
						regNormal = ResourceManager.instance.assetLevelCommom.waypoint4first;
						break;
			case 5 : 	waypoint = ResourceManager.instance.assetLevelCommom.endWaypointBegin;
						regNormal = ResourceManager.instance.assetLevelCommom.endWaypointFirst;
						break;
		}	
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if(startAnimationTimer){
			animationTimer+=deltaTime;
			
			if(waypointNumber == 5 && animationTimer > 0.75f ){
				endAnimationTimer += deltaTime;
				if(waypoint != ResourceManager.instance.assetLevelCommom.endWaypointExplosion){
					waypoint = ResourceManager.instance.assetLevelCommom.endWaypointExplosion;
					setAnimation(waypoint);
				}
				
				if(endAnimationTimer > 0.95f){
					setAnimation(null);
					regNormal = ResourceManager.instance.assetLevelCommom.endWaypointFinal;
				}
				
			}
			
			
			if(animationTimer > 1.2f && waypointNumber != 2 && waypointNumber !=3 && waypointNumber != 5 && waypointNumber != 4){
				setAnimation(null);
				switch(waypointNumber){
					case 1: regNormal = ResourceManager.instance.assetLevelCommom.waypoint1final; break;
					
				}		
				
			}else if(animationTimer > 0.9f && (waypointNumber == 2 || waypointNumber == 4 || waypointNumber == 3)){
				setAnimation(null);
				switch(waypointNumber){
					case 2: regNormal = ResourceManager.instance.assetLevelCommom.waypoint2final; break;
					case 3: regNormal = ResourceManager.instance.assetLevelCommom.waypoint3final; break;
					case 4: regNormal = ResourceManager.instance.assetLevelCommom.waypoint4final; break;
			}	
			}
		}
	}
	
	public void onContact(){
		setAnimation(waypoint);
		startAnimationTimer = true;
		
	}
	
	@Override
	public void render(SpriteBatch batch) {
//		if(!shouldRender) return;
		TextureRegion reg = null;
		
		if(animation != null){
			reg = animation.getKeyFrame(stateTime,true);
		}else{
			reg = regNormal;
		}


		batch.draw(reg.getTexture(), position.x , position.y, origin.x, origin.y, dimension.x, dimension.y,
			scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false,
			false);

			
	}
	
	private void createWaypointSensor(World world,int number,float position) {
		BodyDef waypointDef = new BodyDef();
		waypointDef.type = BodyDef.BodyType.StaticBody;
		
		body = world.createBody(waypointDef);
		EdgeShape waypointShape = new EdgeShape();				
	
		waypointShape.set(position,2f,position,10f);
		
		FixtureDef waypointFixture = new FixtureDef();
		waypointFixture.shape = waypointShape;
		waypointFixture.isSensor = true;
		body.createFixture(waypointFixture);
		body.getFixtureList().get(0).setUserData("wayPointSensor"+number);
		waypointShape.dispose();
		
	}
	
	public void dispose(){
		regNormal.getTexture().dispose();
		
		for(TextureRegion region : waypoint.getKeyFrames()){
			region.getTexture().dispose();
		}
		
	}

}
