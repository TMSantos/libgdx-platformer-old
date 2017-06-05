package com.me.catplaformer.objects.customObjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.me.catplaformer.managers.ResourceManager;
import com.me.catplaformer.objects.CatPlatformerGameObject;

public class CustomPlatform extends CatPlatformerGameObject{

	public TextureRegion platformReg;
	public PlatformType platformType;
	public boolean isPlatformGoingToFall;
	private Vector2 patrolRange;
	
	private Sprite movingPlatformSprite;
	private float fallingTime;
	private boolean startTiming;
	
	private float degreesPerSecond = 90f;
	 
	private Vector2 direction = new Vector2(1.0f, 0.0f);
	private Vector2 zero = new Vector2(0.0f, 0.0f);
	private Vector2 tmp = new Vector2();

	public CustomPlatform(World world,float x,float y,float width,int platformNumber,float patrolRangeX,float patrolRangeY,float velocityX,float velocityY,int type) {
		init(world,x,y,width,platformNumber,patrolRangeX,patrolRangeY,velocityX,velocityY,type);
	}

	private void init(World world,float x,float y, float width,int platformNumber,float patrolRangeX,float patrolRangeY,float velocityX,float velocityY,int type) {
		dimension.set((float) (width/2), 0.7f);
		
		origin.set(dimension.x / 2, dimension.y / 2);
		position.x = x;
		position.y = y;
		velocity.x = velocityX;
		velocity.y = velocityY;
		patrolRange = new Vector2(patrolRangeX,patrolRangeY);
		
		setPlatformReg(platformNumber);
		
		fallingTime = 0;
		
		shouldRender = true;
		startTiming = false;
		isPlatformGoingToFall = false;
		
		if(type == 1) platformType = PlatformType.FALL;
		else if(type == 2) platformType = PlatformType.MOVING;
		else platformType = PlatformType.CIRCLE;
		
		setPosition(position);
		body = createDynamicPlatformPhysics(world,width,0.7f,type);
		body.setTransform(getPosition(), 0);
		
	}
	
	public void setPlatformReg(int platformNumber) {
		switch(platformNumber){
		case 1: platformReg = ResourceManager.instance.assetsLevel1and2.movingPlatform1;
				initSprite();
				break;
		case 2: platformReg = ResourceManager.instance.assetsLevel1and2.movingPlatform2;
				initSprite();
				break;
		case 3: platformReg = ResourceManager.instance.assetsLevel1and2.movingPlatform3;
				initSprite();
				break;
		case 4: platformReg = ResourceManager.instance.assetsLevel1and2.movingPlatform4;
				initSprite();
				break;
		}
	}

	private void initSprite() {
		movingPlatformSprite = new Sprite(platformReg);
		movingPlatformSprite.setSize(dimension.x, dimension.y);
		movingPlatformSprite.setOrigin(origin.x, origin.y);
		movingPlatformSprite.setPosition(position.x-origin.x, position.y-origin.y);
	}

	public enum PlatformType{
		FALL,MOVING,CIRCLE
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		switch(platformType){
			case MOVING: move(); 
						break;
			case FALL: 	fall(deltaTime);	
						break;
			case CIRCLE: moveInCircle(deltaTime);
						break;
			
			default: break;
		}
	}

	private void fall(float deltaTime) {		
		if(startTiming) fallingTime += deltaTime;
	
		if(fallingTime > 0.9f){
			body.getFixtureList().get(0).setRestitution(0.5f);
			body.setType(BodyType.DynamicBody); 
			startTiming = false;
			fallingTime = 0;
		}
		
	}
	
	private void moveInCircle(float deltaTime) {
		degreesPerSecond = 360f;
		direction.rotate(degreesPerSecond * deltaTime);
		velocity.x = 8f;
		velocity.y = 8f;
        body.setLinearVelocity(tmp.set(direction).scl(velocity));
	}

	private void move() {
		if(patrolRange.y == 0.0f){ 
			if(body.getPosition().x > position.x + patrolRange.x){
				body.setLinearVelocity(velocity.x * -1 , velocity.y);							
			}else if(body.getPosition().x < position.x - patrolRange.x){
				body.setLinearVelocity(velocity);
			}
		 }else{
			 if(body.getPosition().y >  position.y + patrolRange.y){
					body.setLinearVelocity(velocity.x,velocity.y *-1 );							
				}else if(body.getPosition().y  < position.y - patrolRange.y){
					body.setLinearVelocity(velocity);
				}
		 }		
	}

	@Override
	public void render(SpriteBatch batch) {
		if(!shouldRender) return;
		movingPlatformSprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
		movingPlatformSprite.setPosition(body.getPosition().x - origin.x, body.getPosition().y - origin.y);
		movingPlatformSprite.draw(batch);
	}

	public void startFallingTimer() {
		startTiming = true;
	}
	
	private Body createDynamicPlatformPhysics(World world,float width, float height,int type) {
		BodyDef def = new BodyDef();
		def.type = BodyType.KinematicBody;
		
		Body platformBody = world.createBody(def);

		PolygonShape movingPlatformShape = new PolygonShape();
		movingPlatformShape.setAsBox(width/4,height/4,new Vector2(0,-0.1f),0);
		
		FixtureDef fixturePlatform = new FixtureDef();
		
		fixturePlatform.density = 1f;
		fixturePlatform.shape = movingPlatformShape;
		
		
		if(type == 1){
			fixturePlatform.friction = 0.3f;
			platformBody.createFixture(fixturePlatform);
			platformBody.getFixtureList().get(0).setUserData("fallingPlatform");
		}else{
			fixturePlatform.friction = 80f;
			platformBody.createFixture(fixturePlatform);
			platformBody.setLinearVelocity(velocity.x,velocity.y);
			platformBody.getFixtureList().get(0).setUserData("movingPlatform");
		}
		
		
		movingPlatformShape.dispose();
		
		return platformBody;
	}
	
	public void dispose(){
		platformReg.getTexture().dispose();
	}

}
