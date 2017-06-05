package com.me.catplaformer.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.me.catplaformer.managers.ResourceManager;

public class Fish extends CatPlatformerGameObject implements Poolable{
	
	private Body mEmitterBody;
	private int mDirection;
	private TextureRegion fish;
	private Sprite fishSprite;
	
	public ObjectBodyData fishBodyData;
	
	private float maxTimeAlive;
	private int mFaction; //to know if it is casted by enemy or player
	
	public Fish(World b2world,Body emitterBody,int direction,int faction){
		mEmitterBody = emitterBody;
		mDirection = direction;
		init(b2world,faction);
		
	}
	
	private void init(World b2world,int faction){
		mFaction = faction;
		velocity.x = 0.5f;
		velocity.y = 0.5f;
		dimension.set(0.5f, 0.3f);
		origin.set(dimension.x / 2, dimension.y / 2);
		maxTimeAlive = 0;
		
		if(mFaction == 1 ) fish = ResourceManager.instance.assetLevelCommom.bulletFish;
		if(mFaction == 2 ) fish = ResourceManager.instance.assetsLevel1and2.enemyBulletFish;
		fishBodyData = new ObjectBodyData();
		body = createFish(b2world);
		fishSprite = new Sprite(fish);
		fishSprite.setSize(dimension.x, dimension.y);
		fishSprite.setOrigin(origin.x, origin.y);
		fishSprite.setPosition(body.getPosition().x-origin.x, body.getPosition().y-origin.y);
		if(mDirection == -1) fishSprite.flip(true, false);

	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		maxTimeAlive += deltaTime;
		if(body.getPosition().y < 0 
		|| body.getPosition().y > 10 
		|| body.getPosition().x > mEmitterBody.getPosition().x + 8f
		|| maxTimeAlive > 2f){
			maxTimeAlive = 0;
			fishBodyData.active = false;
			body.setUserData(fishBodyData);
		}
		
		if(!fishBodyData.active){
//			body.setTransform(-200f,-200f, 0);
			body.getFixtureList().get(0).setUserData("inactiveFish");
			body.setActive(false);
		}
		
	}
	
	private Body createFish(World world){
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.fixedRotation = true;
		def.allowSleep = true;
		def.bullet = true;
		
		Body fishBody = world.createBody(def);
		fishBody.setGravityScale(0);
		CircleShape fishShape = new CircleShape();
		fishShape.setRadius(0.15f);
		
		fishBody.createFixture(fishShape,1);
		fishShape.dispose();
		
		if(mFaction == 1) fishBody.getFixtureList().get(0).setUserData("fish");
		else if(mFaction == 2) fishBody.getFixtureList().get(0).setUserData("enemyFish");
		
		fishBodyData.active = true;
		fishBody.setUserData(fishBodyData);
		fishBody.setTransform(new Vector2(mEmitterBody.getPosition().x+(0.2f*mDirection),mEmitterBody.getPosition().y), 0);
		fishBody.setLinearVelocity(velocity);
		fishBody.applyLinearImpulse(new Vector2(0.6f*mDirection,0), mEmitterBody.getWorldCenter(),true);
		
		return fishBody;
	}
	
	
	@Override
	public void render(SpriteBatch batch) {
		if(!shouldRender) return;
		if(mDirection == -1){
			if(!fishSprite.isFlipX()) fishSprite.flip(true, false);
		}else{
			if(fishSprite.isFlipX()) fishSprite.flip(true, false);
		}
		
		fishSprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
		fishSprite.setPosition(body.getPosition().x - origin.x, body.getPosition().y - origin.y);
		fishSprite.draw(batch);	

	}

	public void reSend(int direction){
		body.setActive(true);
		mDirection = direction;
		body.setTransform(new Vector2(mEmitterBody.getPosition().x+(0.2f*direction),mEmitterBody.getPosition().y), 0);
		body.setLinearVelocity(velocity);
		body.applyLinearImpulse(new Vector2(0.6f*direction,0), mEmitterBody.getWorldCenter(),true);
		fishBodyData.active = true;
		if(mFaction == 1) body.getFixtureList().get(0).setUserData("fish");
		else if(mFaction == 2) body.getFixtureList().get(0).setUserData("enemyFish");
		body.setUserData(fishBodyData);
		maxTimeAlive = 0;
		
	}
	
	@Override
	public void reset() {
		maxTimeAlive = 0;
		fishBodyData.active = false;
		body.getFixtureList().get(0).setUserData("inactiveFish");
		body.setUserData(fishBodyData);
	}
	
	public void dispose(){
		fish.getTexture().dispose();
	}
	
}

