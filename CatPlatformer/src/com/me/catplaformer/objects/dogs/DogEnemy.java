package com.me.catplaformer.objects.dogs;

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
import com.me.catplaformer.objects.ObjectBodyData;

public class DogEnemy extends CatPlatformerGameObject{

	private Animation walk;
	private Animation dead;
	private Animation bone;
	
	private float mPatrolRange;

	private VIEW_DIRECTION_DOG viewDirectionDog;
	private STATE_DOG stateDog;

	private DOG_TYPE mDogType;
	
	private float boneTimer;
	private boolean isBoneAnimationFinished;
	private float boneAnimationTime;
	private float deadAnimationTime;
	private boolean mIsDead;
	public boolean mIsDestroyed;
	private float maxVelocity;
	
	public ObjectBodyData boneBodyDogData;
	
	private final Array<Bone> activeBone = new Array<Bone>();
 
	public DogEnemy(World world,float x,float y,float patrolRange,DOG_TYPE dogType){
		velocity.x = 1f;
		velocity.y = 0f;
		dimension.set(2.2f, 2.5f);
		origin.set(dimension.x / 2, dimension.y / 2);
		boneTimer = 0;
		deadAnimationTime = 0;
		isBoneAnimationFinished = true;
		boneAnimationTime = 0;
		mIsDead = false;
		mIsDestroyed = false;
		init(world,dogType,patrolRange,x,y);
	}
	
	private void init(World world, DOG_TYPE dogType,float patrolRange, float x, float y) {
		body = createEnemyDog(world);
		setPosition(new Vector2(x,y));
		body.setTransform(getPosition(), 0);
		stateDog = STATE_DOG.Walking;
		viewDirectionDog = VIEW_DIRECTION_DOG.LEFT;
		mPatrolRange = patrolRange;
		mDogType = dogType;
		createAnimations(mDogType);
		setAnimation(walk);
		
	}
	
	private final Pool<Bone> bonePool = new Pool<Bone>() {

		@Override
		protected Bone newObject() {
		        	switch(viewDirectionDog){
		        		case RIGHT: return new Bone(body.getWorld(),body,1);
						case LEFT:	return new Bone(body.getWorld(),body,-1);
		        	}
		        	return null;
		        }    
      
    };
    
	private void createAnimations(DOG_TYPE dogType) {
		switch(dogType){
			case WHITE: walk = ResourceManager.instance.assetEnemysLevel1.whiteDogAnimWalking;
						dead = ResourceManager.instance.assetEnemysLevel1.whiteDogAnimDead;
						maxVelocity = 2f;
						break;
			case BROWN: walk = ResourceManager.instance.assetEnemysLevel1.brownDogAnimWalking;
						dead = ResourceManager.instance.assetEnemysLevel1.brownDogAnimDead;
						maxVelocity = 1f;
						break;
			case RED: 	walk = ResourceManager.instance.assetEnemysLevel1.redDogAnimWalking;
					  	dead = ResourceManager.instance.assetEnemysLevel1.redDogAnimDead;
					  	bone = ResourceManager.instance.assetEnemysLevel1.redDogAnimBone;
					  	maxVelocity = 1f;				  	
					  	break;
		}
		
	}
	
	public enum DOG_TYPE{
		WHITE,BROWN,RED
	}

	public enum VIEW_DIRECTION_DOG {
		LEFT, RIGHT
	}
	
	public enum STATE_DOG {
		Walking,
		Dead,
		CastingBone
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if(mIsDestroyed) return;
		
		if(isDead()) destroy();
		
		if(stateDog == STATE_DOG.Dead){
			updateDeadState(deltaTime);
			return;
		}
		
		if(Math.abs(body.getLinearVelocity().x) > maxVelocity) {			
			velocity.x = Math.signum(body.getLinearVelocity().x) * maxVelocity;
			body.setLinearVelocity(velocity.x, velocity.y);
		}
		
		if(mDogType == DOG_TYPE.RED) redDogUpdate(deltaTime);
		updateDefaultDog();
	}

	private void updateDefaultDog() {
		switch(viewDirectionDog){
				case LEFT:  moveLeft();
							break;
				case RIGHT: moveRight();
			        		break;
			}
	
		if(body.getPosition().x > position.x + mPatrolRange){
			viewDirectionDog = VIEW_DIRECTION_DOG.LEFT;
		}else if(body.getPosition().x < position.x - mPatrolRange){
			viewDirectionDog = VIEW_DIRECTION_DOG.RIGHT;
		}	
	}

	private void moveRight() {
		if(body.getLinearVelocity().x < maxVelocity){
			body.applyLinearImpulse(0.1f,0,body.getPosition().x,body.getPosition().y, true);
		}

	}

	private void moveLeft() {
		if(body.getLinearVelocity().x > -maxVelocity){
			body.applyLinearImpulse(-0.1f,0,body.getPosition().x,body.getPosition().y, true);
		}
	}

	private void redDogUpdate(float deltaTime) {
		boneTimer+=deltaTime;
		
		for(Bone bone : activeBone){
			bone.update(deltaTime);
		}
		
		Bone itemRelease;
        int len = activeBone.size;
        for (int i = len; --i >= 0;) {
        	itemRelease = activeBone.get(i);
        	boneBodyDogData = (ObjectBodyData) itemRelease.body.getUserData();
            if (boneBodyDogData.active == false) {
            	activeBone.removeIndex(i);
                bonePool.free(itemRelease);
            }
        }
		
		if(stateDog != STATE_DOG.Walking && isBoneAnimationFinished ){
			stateDog = STATE_DOG.Walking;
			setAnimation(walk);
		}
		
		if(boneTimer > 2 || !isBoneAnimationFinished){
			boneAnimationTime += deltaTime;
			if(stateDog != STATE_DOG.CastingBone){
				stateDog = STATE_DOG.CastingBone;
				setAnimation(bone);	
				
			}

			if(boneAnimationTime > 0.6f){
				shootBone();
				isBoneAnimationFinished = true;
				boneAnimationTime = 0;
				boneTimer = 0;
			}else{
				isBoneAnimationFinished = false;
			}
		}
		
	}

	private void updateDeadState(float deltaTime) {
			if(animation != dead){
				setAnimation(dead);
				body.getFixtureList().get(0).setUserData("dogDead");
				body.getFixtureList().get(1).setUserData("dogDead");
			}
			
			deadAnimationTime += deltaTime;
			
//			if(deadAnimationTime > 	0.6f && deadAnimationTime < 1.2f){
//				dimension.set(1.6f, 1.1f);
//			}
			
			if(deadAnimationTime > 1.2f){
				mIsDead = true;		
			}					
	}

	public void shootBone(){
		Bone item = bonePool.obtain();
		
		switch(viewDirectionDog){
			case RIGHT: item.reSend(1);
					    activeBone.add(item);
						break;
			case LEFT:  item.reSend(-1);
						activeBone.add(item);
						break;
		}
	}
	
	public void destroy(){
		if(mDogType == DOG_TYPE.RED){
			activeBone.clear();
			bonePool.clear();
		}
		mIsDestroyed = true;
		mIsDead = false;
		body.getWorld().destroyBody(body);
	}

	@Override
	public void render(SpriteBatch batch) {
		if(!shouldRender) return;
		if(mIsDestroyed) return;
		
		TextureRegion reg = animation.getKeyFrame(stateTime,true);

		batch.draw(reg.getTexture(), body.getPosition().x - origin.x, body.getPosition().y - origin.y, origin.x, origin.y, dimension.x, dimension.y,
				scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), viewDirectionDog == VIEW_DIRECTION_DOG.RIGHT,
				false);
		
		for(Bone bone : activeBone){
			bone.render(batch);
		}
				
	}
	
	public STATE_DOG getStateDog() {
		return stateDog;
	}

	public void setStateDog(STATE_DOG stateDog) {
		this.stateDog = stateDog;
	}

	private Body createEnemyDog(World b2world) {
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.fixedRotation = true;
		def.allowSleep = false;
		
		Body dogBody = b2world.createBody(def);
		
		PolygonShape dogBodyShape = new PolygonShape();
		dogBodyShape.setAsBox(0.25f, 0.3f);
		FixtureDef dogPhysicsFixture = new FixtureDef();
		dogPhysicsFixture.density = 2f;
		dogPhysicsFixture.friction = 0f;
		dogPhysicsFixture.shape = dogBodyShape;
		dogBody.createFixture(dogPhysicsFixture);
		dogBodyShape.dispose();	
		
		CircleShape dogLegsShape = new CircleShape();
		dogLegsShape.setRadius(0.25f);
		dogLegsShape.setPosition(new Vector2(0f,-0.3f));
		FixtureDef dogLegsFixture= new FixtureDef();
		dogLegsFixture.density = 0f;
		dogLegsFixture.friction = 0f;
		dogLegsFixture.restitution = 0f;
		dogLegsFixture.shape = dogLegsShape;
		dogBody.createFixture(dogLegsFixture);
		dogLegsShape.dispose();

		dogBody.getFixtureList().get(0).setUserData("dogBody");
		dogBody.getFixtureList().get(1).setUserData("dogLegs");
		
		dogBody.setBullet(true);
		
		return dogBody;
		
	}
	
	public boolean isDead() {
		return mIsDead;
	}
	
	public void dispose(){
		for(TextureRegion region : walk.getKeyFrames()){
			region.getTexture().dispose();
		}
		
		for(TextureRegion region : dead.getKeyFrames()){
			region.getTexture().dispose();
		}
		
		if(bone != null){
			for(TextureRegion region : bone.getKeyFrames()){
				region.getTexture().dispose();
			}
		}
	}
}
