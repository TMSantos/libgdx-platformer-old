package com.me.catplaformer.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.me.catplaformer.managers.ResourceManager;
import com.me.catplaformer.utils.GamePreferences;

public class Cat extends CatPlatformerGameObject{

	private Animation stand;
	private Animation walk;
	private Animation dying;
	private Animation claw;
	
	private TextureRegion regNormal;
	
	private int mFishQuantity;
	
	public VIEW_DIRECTION viewDirection;
	public STATE state;
	
	private boolean mIsOnMovingPlatform;
	
	private float deadAnimationTime;
	private float fastCastRate;
	private boolean mCanCastFish;
	private float clawFastCastRate;
	public boolean shouldRender;
	
	public int footContacts;
	public int stairsContacts;
	
	public ObjectBodyData fishBodyCatData;
	
	private final Array<Fish> activeFish = new Array<Fish>();
	
	final static float MAX_VELOCITY = 4.5f;
	
	public enum VIEW_DIRECTION {
		LEFT, RIGHT
	}
	
	public enum STATE {
		Standing,
		Walking,
		Jumping,
		Dying,
		Scratch,
		CastFish,
		Climbing
	}
	
	public Cat(World world) {
		init(world);
	}

	public void init(World world) {
		body = createCat(world);
		shouldRender = true;
		loadgame();
		createAnimations();	
		body.setTransform(getPosition(), 0);
		dimension.set(2.8f, 1.5f);
		
		deadAnimationTime = 0;
		setAnimation(stand);
		fastCastRate = 0;
		mFishQuantity = 0;
		
		mCanCastFish = true;
		state = STATE.Standing;
		viewDirection = VIEW_DIRECTION.RIGHT;
		origin.set(dimension.x / 2, dimension.y / 2);
	}
	
	private void loadgame() {
		 GamePreferences gameProgress = GamePreferences.instance;
		 gameProgress.loadGameProgress();
		 if(gameProgress.levelProgress > 5f) setPosition(new Vector2(gameProgress.levelProgress,8f));
		 else setPosition(new Vector2(5f,8f));		
		 setPosition(new Vector2(5f,6f));
	}
	
	public void reset(){
		loadgame();
		body.setTransform(getPosition(), 0);
		state = STATE.Standing;
		viewDirection = VIEW_DIRECTION.RIGHT;
		deadAnimationTime = 0;
		setAnimation(stand);
	}
	
	private void createAnimations() {
		regNormal = ResourceManager.instance.catAssets.cat;
		stand = ResourceManager.instance.catAssets.animStanding;
		walk = ResourceManager.instance.catAssets.animWalking;
		dying = ResourceManager.instance.catAssets.animDead;
		claw = ResourceManager.instance.catAssets.animClaw;		
	}
	 
	private final Pool<Fish> fishPool = new Pool<Fish>() {
	        @Override
	        protected Fish newObject() {
	        	switch(viewDirection){
					case RIGHT: return new Fish(body.getWorld(),body,1,1);
					case LEFT:	return new Fish(body.getWorld(),body,-1,1);
	        	}
	        	return null;
	        }     
	    };
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		for(Fish fish : activeFish){
			fish.update(deltaTime);
		}
		
		releasePool();
        clawFastCastRate += deltaTime;
		fastCastRate+=deltaTime;
		
		switch(state){
			case Standing: if(animation != stand){
								setAnimation(stand);
							}
						  break;
			case Walking: if(animation != walk){
							setAnimation(walk);
						  }
						  break;
			case Jumping: regNormal = ResourceManager.instance.catAssets.cat;
						  if(animation != null ) setAnimation(null);
						  break;
			case Dying: deadAnimationTime += deltaTime;
						dying();
						break;	
			case Scratch: if(animation != claw){
								setAnimation(claw);
						  		}
	 		  			   break;
			case CastFish: shootFish();
						   break;
			case Climbing: regNormal = ResourceManager.instance.catAssets.climbing;
						   if(animation != null ) setAnimation(null);
						   break;
		}
		
	}
	
	private void dying() {
		if(animation != dying ){
			setAnimation(dying);			
		}

		if(deadAnimationTime > 1.9f){
			regNormal = ResourceManager.instance.catAssets.dead;
			setAnimation(null);
		}	
	}

	private void shootFish(){
		if(fastCastRate > 0.3 && mCanCastFish){
			fastCastRate = 0;
			mCanCastFish = false;
			Fish item = fishPool.obtain();
			switch(viewDirection){
				case RIGHT: item.reSend(1);
							activeFish.add(item);
							mFishQuantity--;
							break;
				case LEFT:	item.reSend(-1);
							activeFish.add(item);
							mFishQuantity--;
							break;	
			}
		}
	}
	
	private void releasePool(){
		Fish itemRelease;
        int len = activeFish.size;
        for (int i = len; --i >= 0;) {
        	itemRelease = activeFish.get(i);
        	fishBodyCatData = (ObjectBodyData) itemRelease.body.getUserData();
            if (fishBodyCatData.active == false) {
            	activeFish.removeIndex(i);
                fishPool.free(itemRelease);
            }
        }
	}
	
	public boolean canUseClaw(){
		if(clawFastCastRate > 0.1){
			clawFastCastRate = 0;
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public void render(SpriteBatch batch) {
		if(!shouldRender) return;
		
		TextureRegion reg = null;

		if(animation != null){
			reg = animation.getKeyFrame(stateTime,true);
		}else{
			reg = regNormal;
		}

		if(animation == stand){
			batch.draw(reg.getTexture(), body.getPosition().x - origin.x , body.getPosition().y - origin.y, origin.x, origin.y, dimension.x, dimension.y,
					scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false,
					false);
		}
		else
		{
			batch.draw(reg.getTexture(), body.getPosition().x - origin.x , body.getPosition().y - origin.y, origin.x, origin.y, dimension.x, dimension.y,
			scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), viewDirection == VIEW_DIRECTION.LEFT,
			false);
		}
			
		
		for(Fish fish : activeFish){
			fish.render(batch);
		}
	}
	
	public boolean isCanCastFish() {
		return mCanCastFish;
	}

	public void canCastFish(boolean canCastFish) {
		mCanCastFish = canCastFish;
	}

	public int getFishQuantity() {
		return mFishQuantity;
	}

	public void setFishQuantity(int FishQuantity) {
		mFishQuantity = FishQuantity;
	}
	
	public boolean isIsOnMovingPlatform() {
		return mIsOnMovingPlatform;
	}

	public void setIsOnMovingPlatform(boolean IsOnMovingPlatform) {
		mIsOnMovingPlatform = IsOnMovingPlatform;
	}
	
	private Body createCat(World b2world) {
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.fixedRotation = true;
		def.allowSleep = false;
		def.gravityScale = 0f;
		
		Body catBody = b2world.createBody(def);
	
		PolygonShape catBodyShape = new PolygonShape();
		catBodyShape.setAsBox(0.15f, 0.20f);
		FixtureDef playerPhysicsFixture = new FixtureDef();
		playerPhysicsFixture.density = 2f;
		playerPhysicsFixture.friction = 0f;
		playerPhysicsFixture.shape = catBodyShape;
		catBody.createFixture(playerPhysicsFixture);
		catBodyShape.dispose();	
		
		CircleShape catLegsShape = new CircleShape();
		catLegsShape.setRadius(0.153f);
		catLegsShape.setPosition(new Vector2(0f,-0.2f));
		FixtureDef catLegsFixture= new FixtureDef();
		catLegsFixture.density = 0f;
		catLegsFixture.friction = 0.5f;
		catLegsFixture.restitution = 0f;
		catLegsFixture.shape = catLegsShape;
		catBody.createFixture(catLegsFixture);
		catLegsShape.dispose();
		
		PolygonShape catLeftClawShape = new PolygonShape();
		catLeftClawShape.setAsBox(0.325f, 0.3f, new Vector2(-0.32f,0f),0);
		FixtureDef catLeftClawFixture = new FixtureDef();
		catLeftClawFixture.isSensor = true;
		catLeftClawFixture.shape = catLeftClawShape;
		catBody.createFixture(catLeftClawFixture);
		catLeftClawShape.dispose();
		
		PolygonShape catRightClawShape = new PolygonShape();
		catRightClawShape.setAsBox(0.325f, 0.3f, new Vector2(0.32f,0f),0);
		FixtureDef catRightClawFixture = new FixtureDef();
		catRightClawFixture.isSensor = true;
		catRightClawFixture.shape = catRightClawShape;
		catBody.createFixture(catRightClawFixture);
		catRightClawShape.dispose();
		
		PolygonShape legsSensor = new PolygonShape();
		legsSensor.setAsBox(0.15f, 0.1f, new Vector2(0f,-0.35f),0);
		FixtureDef legSensorFixture = new FixtureDef();
		legSensorFixture.isSensor = true;
		legSensorFixture.shape = legsSensor;
		catBody.createFixture(legSensorFixture);
		legsSensor.dispose();
		
		catBody.getFixtureList().get(0).setUserData("catBody");
		catBody.getFixtureList().get(1).setUserData("catLegs");
		catBody.getFixtureList().get(2).setUserData("clawLeft");
		catBody.getFixtureList().get(3).setUserData("clawRight");
		catBody.getFixtureList().get(4).setUserData("legSensor");
		
		catBody.setBullet(true);
		
		return catBody;
		
	}
	
	public void capMaxVelocity(){
		if(Math.abs(velocity.x) > MAX_VELOCITY) {			
			velocity.x = Math.signum(velocity.x) * MAX_VELOCITY;
			body.setLinearVelocity(velocity.x, velocity.y);
		}
	}
	
	public boolean isMaxVelocity(int i){
		switch(i){
			case 1: return velocity.x < i*MAX_VELOCITY; 
			case -1: return velocity.x > i*MAX_VELOCITY;	
		}
		return false;
	}

	public void stand(){
		body.setLinearVelocity(velocity.x * 0.5f, velocity.y);
		if(state != STATE.Standing) state = STATE.Standing;
	}
	
	public void moveLeft(){
		if(!isIsOnMovingPlatform()){
			body.applyLinearImpulse(-0.3f, 0, position.x, position.y,true);
		}else{
			body.applyLinearImpulse(-1f, 0, position.x, position.y,true);
		}
		if(state != STATE.Walking) state = STATE.Walking;
		viewDirection = VIEW_DIRECTION.LEFT;
	}
	
	public void moveRight(){
		if(!isIsOnMovingPlatform()){
			body.applyLinearImpulse(0.3f, 0, position.x, position.y,true);
		}else{
			body.applyLinearImpulse(1f, 0, position.x, position.y,true);
		}
		if(state != STATE.Walking) state = STATE.Walking;
		viewDirection = VIEW_DIRECTION.RIGHT;
	}
	
	public void Scratch(){
		if(state != STATE.Scratch) state = STATE.Scratch;
	}
	
	public void shoot(){
		if(state != STATE.CastFish && getFishQuantity() > 0){
			state = STATE.CastFish;
		}
	}
	
	public void jump(){
		if(state != STATE.Jumping) state = STATE.Jumping;
		
		
		if(stairsContacts >= 1){
			
			body.setLinearVelocity(velocity.x, 2);
			body.applyLinearImpulse(0, 0.2f, position.x, position.y,true);	
			if(state != STATE.Climbing) state = STATE.Climbing;
		}
		
		if(footContacts < 1 ) return;
		
		if(footContacts >= 1){
			body.setLinearVelocity(velocity.x, 0);
			body.setTransform(position.x, position.y + 0.01f, 0);
			body.applyLinearImpulse(0, 1.35f, position.x, position.y,true);
		}


	}
	
	public void dispose(){
		for(TextureRegion region : stand.getKeyFrames()){
			region.getTexture().dispose();
		}
		
		for(TextureRegion region : walk.getKeyFrames()){
			region.getTexture().dispose();
		}
		
		for(TextureRegion region : dying.getKeyFrames()){
			region.getTexture().dispose();
		}
		
		for(TextureRegion region : claw.getKeyFrames()){
			region.getTexture().dispose();
		}
		
		regNormal.getTexture().dispose();
	}
}
