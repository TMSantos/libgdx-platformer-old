package com.me.catplaformer.objects.enemyCats;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.me.catplaformer.managers.ResourceManager;
import com.me.catplaformer.objects.CatPlatformerGameObject;
import com.me.catplaformer.objects.Fish;
import com.me.catplaformer.objects.ObjectBodyData;

public class WhiteEnemy extends CatPlatformerGameObject{

	private Animation walk;
	private Animation psychoWalk;
	private Animation dead;
	
	private TextureRegion regNormal;
	
	public ENEMY_CAT_VIEW_DIRECTION enemyCatViewDirection;
	public ENEMY_CAT_STATE enemyCatState;
	
	private float deadAnimationTime;
	private float patrolRange;
	
	private float fastCastRate;

	public ObjectBodyData fishBodyEnemyCatData;
	
	private final Array<Fish> activeEnemyFish = new Array<Fish>();
	
	private int mLives;
	
	private float maxVelocity;
	private float castTimer;
	private float canLoseLife;
	private float woundedTime;
	
	private boolean mIsDead;
	private boolean isWounded;

	public boolean mIsDestroyed;
	
	public WhiteEnemy(World b2world,float x,float y){
		init(b2world,x,y);
	}
	
	private void init(World b2world, float x, float y) {
		body = createEnemyCat(b2world);
		body.setTransform(x,y,0);
		enemyCatViewDirection = ENEMY_CAT_VIEW_DIRECTION.LEFT;
		enemyCatState = ENEMY_CAT_STATE.Walking;
		patrolRange = 7f;
		position.x = x;
		position.y = y;
		velocity.x = 1f;
		velocity.y = 0f;
		maxVelocity = 2f;
		fastCastRate = 0f;
		canLoseLife = 0f;
		castTimer = 0f;
		dimension.set(2f, 3.3f);
		origin.set(dimension.x / 2, dimension.y / 2);
		mLives = 2;
		woundedTime = 0;
		isWounded = false;
		createAnimations();
		setAnimation(walk);
	}

	private void createAnimations() {
		walk = ResourceManager.instance.assetsLevel1and2.enemyCatWalk;
		psychoWalk = ResourceManager.instance.assetsLevel1and2.enemyCatPsychoWalk;
		dead = ResourceManager.instance.assetsLevel1and2.enemyCatDying;
		
	}

	public enum ENEMY_CAT_VIEW_DIRECTION {
		LEFT, RIGHT
	}
	
	public enum ENEMY_CAT_STATE {
		Standing,
		Walking,
		Jumping,
		Dying,
		psychoWalk,
		CastFish,
		Climbing
	}
	
	private final Pool<Fish> fishEnemyPool = new Pool<Fish>() {
        @Override
        protected Fish newObject() {
        	switch(enemyCatViewDirection){
				case RIGHT: return new Fish(body.getWorld(),body,1,2);
				case LEFT:	return new Fish(body.getWorld(),body,-1,2);
        	}
        	return null;
        }     
    };
    
    public void normalMovement(){
    	if(animation == null) setAnimation(null);
    	else if(animation != walk) setAnimation(walk);
    	
    	if(enemyCatState != ENEMY_CAT_STATE.Walking) enemyCatState = ENEMY_CAT_STATE.Walking;
    	
    	if(Math.abs(body.getLinearVelocity().x) > maxVelocity) {			
			velocity.x = Math.signum(body.getLinearVelocity().x) * maxVelocity;
			body.setLinearVelocity(velocity.x, velocity.y);
		}
    	
    	switch(enemyCatViewDirection){
		case LEFT: 	if(body.getLinearVelocity().x > -maxVelocity){								
	    				body.applyLinearImpulse(-0.2f,0,body.getPosition().x,body.getPosition().y, true);
					}
					break;
		case RIGHT: if(body.getLinearVelocity().x < maxVelocity){
						body.applyLinearImpulse(0.2f,0,body.getPosition().x,body.getPosition().y, true);
					}
    				break;
		}
		
    }
	
    public void moveLeft(){
    	if(animation == null) setAnimation(null);
    	else if(animation != psychoWalk) setAnimation(psychoWalk);
    	
    	enemyCatState = ENEMY_CAT_STATE.Walking;
    	enemyCatViewDirection = ENEMY_CAT_VIEW_DIRECTION.LEFT;
    	
    	if(Math.abs(body.getLinearVelocity().x) > maxVelocity) {			
			velocity.x = Math.signum(body.getLinearVelocity().x) * maxVelocity;
			body.setLinearVelocity(velocity.x, velocity.y);
		}
    	
    	if(body.getLinearVelocity().x > -maxVelocity && body.getPosition().x > position.x - patrolRange){
    		body.applyLinearImpulse(-0.2f,0,body.getPosition().x,body.getPosition().y, true);
    	}
    }
    
    public void moveRight(){
    	if(animation == null) setAnimation(null);
    	else if(animation != psychoWalk) setAnimation(psychoWalk);
    	
    	enemyCatState = ENEMY_CAT_STATE.Walking;
    	enemyCatViewDirection = ENEMY_CAT_VIEW_DIRECTION.RIGHT;
    	
    	if(Math.abs(body.getLinearVelocity().x) > maxVelocity) {			
			velocity.x = Math.signum(body.getLinearVelocity().x) * maxVelocity;
			body.setLinearVelocity(velocity.x, velocity.y);
		}
    	
    	if(body.getLinearVelocity().x < maxVelocity && body.getPosition().x < position.x + patrolRange){
    		body.applyLinearImpulse(0.2f,0,body.getPosition().x,body.getPosition().y, true);
    	}
    }
    
    private void castFish(int direction){
    	if(fastCastRate > 0.3f){
			fastCastRate = 0;
    		enemyCatState = ENEMY_CAT_STATE.CastFish;
			Fish item = fishEnemyPool.obtain();
			item.reSend(1*direction);
			activeEnemyFish.add(item);
    	}
    }
    
    private void releaseFishPool(){
    	Fish itemRelease;
        int len = activeEnemyFish.size;
        for (int i = len; --i >= 0;) {
        	itemRelease = activeEnemyFish.get(i);
        	fishBodyEnemyCatData = (ObjectBodyData) itemRelease.body.getUserData();
            if (fishBodyEnemyCatData.active == false) {
            	activeEnemyFish.removeIndex(i);
            	fishEnemyPool.free(itemRelease);
            }
        }
    }

	@Override
	public void update(float deltaTime){
		if(mIsDestroyed) return;
		super.update(deltaTime);
		
		if(mLives <= 0){
			isDead(deltaTime);
			return;
		}
		
		if(isWounded){
			woundedTime += deltaTime;
			if(woundedTime > 0.3f){
				woundedTime = 0;
				isWounded = false;
				setAnimation(walk);
			}
		}
		
		for(Fish fish : activeEnemyFish){
			fish.update(deltaTime);
		}
		
		castTimer += deltaTime;
		fastCastRate += deltaTime;
		canLoseLife += deltaTime;
		releaseFishPool();
		
		if(castTimer > 4f){
			switch(enemyCatViewDirection){
				case LEFT: castFish(-1); break;
				case RIGHT: castFish(1); break;
			}
			castTimer = 0;
		}
		
		if(body.getPosition().x > position.x + patrolRange){
			enemyCatViewDirection = ENEMY_CAT_VIEW_DIRECTION.LEFT;
		}else if(body.getPosition().x < position.x - patrolRange){
			enemyCatViewDirection = ENEMY_CAT_VIEW_DIRECTION.RIGHT;
		}
				
	}
	
	private void isDead(float deltaTime) {

		if(animation != dead){
			enemyCatState = ENEMY_CAT_STATE.Dying;
			setAnimation(dead);
			body.getFixtureList().get(0).setUserData("enemyCatDead");
			body.getFixtureList().get(1).setUserData("enemyCatDead");
		}
		
		deadAnimationTime += deltaTime;
		
		if(deadAnimationTime > 1.2f){
			destroy();
		}
		
	}

	private void destroy() {
		mIsDestroyed = true;
		mIsDead = false;
		body.getWorld().destroyBody(body);
		
	}

	public int getLives() {
		return mLives;
	}

	public void setLives(int lives) {
		mLives = lives;
	}
	
	public void takeLifeAwayWithClaw(){
		if(canLoseLife > 0.2){
			mLives -= 1;
			canLoseLife = 0;
			regNormal = ResourceManager.instance.assetsLevel1and2.enemyCatWounded;
			setAnimation(null);
			isWounded = true;
		}
	}
	
	public void takeLifeAwayWithFish(){
		mLives -= 1;
		regNormal = ResourceManager.instance.assetsLevel1and2.enemyCatWounded;
		setAnimation(null);
		isWounded = true;
	}
	
	public boolean isDead() {
		return mIsDead;
	}

	public void setIsDead(boolean isDead) {
		mIsDead = isDead;
	}
	
	@Override
	public void render(SpriteBatch batch) {	
		if(!shouldRender) return;
		if(mIsDestroyed) return;
		
		TextureRegion reg = null;
		
		if(animation != null){
			reg = animation.getKeyFrame(stateTime,true);
		}else{
			reg = regNormal;
		}
		

		batch.draw(reg.getTexture(), body.getPosition().x - origin.x , body.getPosition().y - origin.y - 0.05f, origin.x, origin.y, dimension.x, dimension.y,
					scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), enemyCatViewDirection == ENEMY_CAT_VIEW_DIRECTION.RIGHT,
					false);
		
		for(Fish fish : activeEnemyFish){
			fish.render(batch);
		}
		
	}
	
	private Body createEnemyCat(World b2world) {
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.fixedRotation = true;
		def.allowSleep = false;
		
		Body enemyCatBody = b2world.createBody(def);
	
		PolygonShape enemyCatBodyShape = new PolygonShape();
		enemyCatBodyShape.setAsBox(0.2f, 0.6f);
		FixtureDef enemyCatPhysicsFixture = new FixtureDef();
		enemyCatPhysicsFixture.density = 2f;
		enemyCatPhysicsFixture.friction = 0f;
		enemyCatPhysicsFixture.shape = enemyCatBodyShape;
		enemyCatBody.createFixture(enemyCatPhysicsFixture);
		enemyCatBodyShape.dispose();	
		
		CircleShape enemyCatLegsShape = new CircleShape();
		enemyCatLegsShape.setRadius(0.2f);
		enemyCatLegsShape.setPosition(new Vector2(0f,-0.6f));
		FixtureDef enemyCatLegsFixture= new FixtureDef();
		enemyCatLegsFixture.density = 0f;
		enemyCatLegsFixture.friction = 1f;
		enemyCatLegsFixture.restitution = 0f;
		enemyCatLegsFixture.shape = enemyCatLegsShape;
		enemyCatBody.createFixture(enemyCatLegsFixture);
		enemyCatLegsShape.dispose();
	
		enemyCatBody.getFixtureList().get(0).setUserData("enemyCatBody");
		enemyCatBody.getFixtureList().get(1).setUserData("enemyCatLegs");
		
		enemyCatBody.setBullet(true);
		
		return enemyCatBody;
		
	}
	
	public void dispose(){
		if(regNormal != null) regNormal.getTexture().dispose();
		
		for(TextureRegion region : walk.getKeyFrames()){
			region.getTexture().dispose();
		}
		
		for(TextureRegion region : psychoWalk.getKeyFrames()){
			region.getTexture().dispose();
		}
		
		for(TextureRegion region : dead.getKeyFrames()){
			region.getTexture().dispose();
		}
	}
}
