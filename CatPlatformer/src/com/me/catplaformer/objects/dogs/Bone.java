package com.me.catplaformer.objects.dogs;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.me.catplaformer.managers.ResourceManager;
import com.me.catplaformer.objects.CatPlatformerGameObject;
import com.me.catplaformer.objects.ObjectBodyData;

public class Bone extends CatPlatformerGameObject implements Poolable{

	private Body mEmitterBody;
	private int mDirection;
	private TextureRegion bone;
	private Sprite boneSprite;
	private float maxTimeAlive;
	
	public ObjectBodyData boneBodyData;
	
	public Bone(World b2world,Body emitterBody,int direction){
		mEmitterBody = emitterBody;
		mDirection = direction;
		maxTimeAlive = 0;
		init(b2world);
		
	}
	
	private void init(World b2world){
		velocity.x = 0.5f;
		velocity.y = 0.5f;
		dimension.set(0.3f, 0.3f);
		origin.set(dimension.x / 2, dimension.y / 2);
		boneBodyData = new ObjectBodyData();
		bone = ResourceManager.instance.assetEnemysLevel1.dogBone;
		body = createBone(b2world);
		boneSprite = new Sprite(bone);
		boneSprite.setSize(dimension.x, dimension.y);
		boneSprite.setOrigin(origin.x, origin.y);
		boneSprite.setPosition(body.getPosition().x-origin.x, body.getPosition().y-origin.y);
		
	}
	
	private Body createBone(World world){
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.fixedRotation = false;
		def.allowSleep = true;
		def.bullet = true;
		
		Body boneBody = world.createBody(def);
		
		CircleShape boneShape = new CircleShape();
		boneShape.setRadius(0.15f);
		
		boneBody.createFixture(boneShape,1);
		boneShape.dispose();
		
		boneBodyData.active = true;
		boneBody.setUserData(boneBodyData);
		boneBody.getFixtureList().get(0).setUserData("bone");
		boneBody.setTransform(new Vector2(mEmitterBody.getPosition().x+(0.2f*mDirection),mEmitterBody.getPosition().y), 0);
		boneBody.setLinearVelocity(velocity);
		boneBody.applyLinearImpulse(new Vector2(0.5f*mDirection,0.05f), mEmitterBody.getWorldCenter(),true);
		
		return boneBody;
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		maxTimeAlive += deltaTime;
		
		if(maxTimeAlive > 1){
			maxTimeAlive = 0;
			boneBodyData.active = false;
			body.setUserData(boneBodyData);
		}
		
		if(!boneBodyData.active){
			body.setTransform(-10f,-10f, 0);
		}
		
		
	}
	
	
	public void reSend(int direction){
		body.setTransform(new Vector2(mEmitterBody.getPosition().x+(0.2f*direction),mEmitterBody.getPosition().y), 0);
		body.setLinearVelocity(velocity);
		body.applyLinearImpulse(new Vector2(0.5f*direction,0.05f), mEmitterBody.getWorldCenter(),true);
		boneBodyData.active = true;
		body.setUserData(boneBodyData);
		maxTimeAlive = 0;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		if(!shouldRender) return;
		boneSprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
		boneSprite.setPosition(body.getPosition().x - origin.x, body.getPosition().y - origin.y);
		boneSprite.draw(batch);		
	}

	@Override
	public void reset() {
		boneBodyData.active = false;
		body.setUserData(boneBodyData);
		
	}
	
	public void dispose(){
		bone.getTexture().dispose();
	}

}
