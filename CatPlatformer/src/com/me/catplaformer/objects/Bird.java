package com.me.catplaformer.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.me.catplaformer.managers.ResourceManager;

public class Bird extends CatPlatformerGameObject{

	private Animation walk;
	
	private TextureRegion regDeadBird;
	
	private float mPatrolRange;
	
	private STATE_BIRD stateBird;

	private VIEW_DIRECTION_BIRD viewDirectionBird;
	
	private float birdDeadAnimationTime;
	private boolean mIsBirdDead;
	public boolean mIsBirdDestroyed;
	
	private boolean alreadyDead;
	
	public Bird(World world,float patrolRange,float x,float y){
		init(world,patrolRange,x,y);
	}
	
	private void init(World world,float patrolRange,float x,float y) {
		body = createBird(world);
		dimension.set(1.5f, 1.4f);
		velocity.x = 1f;
		velocity.y = 0f;
		viewDirectionBird = VIEW_DIRECTION_BIRD.RIGHT;
		stateBird = STATE_BIRD.Walking;
		origin.set(dimension.x / 2, dimension.y / 2);
		birdDeadAnimationTime = 0;
		mIsBirdDead = false;
		mIsBirdDestroyed = false;
		alreadyDead = false;
		mPatrolRange = patrolRange;
		createAnimations();
		setPosition(new Vector2(x,y));
		body.setTransform(getPosition(), 0);
	}

	private void createAnimations() {
		walk = ResourceManager.instance.assetLevelCommom.birdAnimWalking;
		regDeadBird = ResourceManager.instance.assetLevelCommom.birdDead;
		setAnimation(walk);
		
	}

	public enum STATE_BIRD {
		Walking,
		Dead
	
	}
	
	public enum VIEW_DIRECTION_BIRD {
		LEFT, RIGHT
	}
	
	public void destroy(){
		mIsBirdDestroyed = true;
		mIsBirdDead = false;
		body.getWorld().destroyBody(body);
	}
	
	private Body createBird(World b2world) {
		BodyDef def = new BodyDef();
		def.type = BodyType.KinematicBody;
		def.fixedRotation = true;
		
		Body birdBody = b2world.createBody(def);
		
		PolygonShape birdShape = new PolygonShape();
		birdShape.setAsBox(0.2f, 0.1f,new Vector2(0,-0.1f),0);
		FixtureDef birdFixture = new FixtureDef();
		birdFixture.density = 1f;
		birdFixture.friction = 0.3f;
		birdFixture.shape = birdShape;
		birdBody.createFixture(birdFixture);
		birdShape.dispose();	

		birdBody.getFixtureList().get(0).setUserData("bird");
		
		birdBody.setBullet(true);
		
		return birdBody;
		
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		
		if(mIsBirdDestroyed) return;
		
		if(mIsBirdDead) destroy();
		
		if(checkDeadState(deltaTime)) return;
		
		switch(viewDirectionBird){
		case LEFT:  body.setLinearVelocity(velocity.x * -1 ,0);
					break;
		case RIGHT: body.setLinearVelocity(velocity.x ,0);
	        		break;
	}

		
		if(body.getPosition().x > position.x + mPatrolRange){
			viewDirectionBird = VIEW_DIRECTION_BIRD.LEFT;
		}else if(body.getPosition().x < position.x - mPatrolRange){
			viewDirectionBird = VIEW_DIRECTION_BIRD.RIGHT;
		}
		
	}
	
	private boolean checkDeadState(float deltaTime) {
		if(stateBird == STATE_BIRD.Dead){
			if(!alreadyDead){
				animation = null;		
				alreadyDead = true;
				body.setLinearVelocity(0, 0);
				body.setType(BodyType.DynamicBody);
				body.getFixtureList().get(0).setUserData("birdDead");
			}
			birdDeadAnimationTime += deltaTime;
			
			if(birdDeadAnimationTime > 	1f){
				mIsBirdDead = true;
				return true;
			}
			
			return true;
		}else
			return false;
	}

	@Override
	public void render(SpriteBatch batch) {
		if(!shouldRender) return;
		if(mIsBirdDestroyed) return;
		
		TextureRegion reg = null;

		if(animation != null){
			reg = animation.getKeyFrame(stateTime,true);
		}else{
			reg = regDeadBird;
		}

		batch.draw(reg.getTexture(), body.getPosition().x - origin.x , body.getPosition().y - origin.y - 0.05f, origin.x, origin.y, dimension.x, dimension.y,
				scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(),viewDirectionBird == VIEW_DIRECTION_BIRD.RIGHT,
				false);
				
	}
	
	public STATE_BIRD getStateBird() {
		return stateBird;
	}

	public void setStateBird(STATE_BIRD stateBird) {
		this.stateBird = stateBird;
	}
	
	public boolean isIsBirdDead() {
		return mIsBirdDead;
	}

	public void setIsBirdDead(boolean IsBirdDead) {
		mIsBirdDead = IsBirdDead;
	}
	
	public boolean isIsBirdDestroyed() {
		return mIsBirdDestroyed;
	}

	public void dispose(){
		regDeadBird.getTexture().dispose();
		for(TextureRegion region : walk.getKeyFrames()){
			region.getTexture().dispose();
		}
	}
	
	public void setIsBirdDestroyed(boolean IsBirdDestroyed) {
		mIsBirdDestroyed = IsBirdDestroyed;
	}
}
