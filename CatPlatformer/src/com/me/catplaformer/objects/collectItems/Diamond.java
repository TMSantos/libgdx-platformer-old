package com.me.catplaformer.objects.collectItems;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.me.catplaformer.managers.ResourceManager;
import com.me.catplaformer.objects.CatPlatformerGameObject;
import com.me.catplaformer.utils.Constants;

public class Diamond extends CatPlatformerGameObject{
	
	private boolean mIsCollected;
	private boolean mIsDestroyed;
	private Animation diamondAnimation;
	private int type;
	private boolean casted;
	
	public Diamond(){
		
		switch(Constants.DISPLAY_RESOLUTION){
			case LD: dimension.set(2.3f,2.3f);break;
			case HD: dimension.set(2.3f,2.3f); break;
			case FHD: dimension.set(2.3f,2.3f); break;
			default:break;
		}
		
		
		origin.set(dimension.x / 2, dimension.y / 2);
		diamondAnimation = ResourceManager.instance.assetLevelCommom.redDiamondAnimation;
		type = 2;
		casted = false;
	}
	
	public Diamond(World world,float x,float y){
		init(world,x,y);
	}

	public void init(World world,float x,float y){
		type = 1;
		casted = false;
		
		switch(Constants.DISPLAY_RESOLUTION){
			case LD: dimension.set(2.3f,2.3f);break;
			case HD: dimension.set(2.3f,2.3f);break;
			case FHD: dimension.set(2.3f,2.3f);break;
			default:break;
		}
		
		origin.set(dimension.x / 2, dimension.y / 2);
		diamondAnimation = ResourceManager.instance.assetLevelCommom.diamondAnimation;
		body = createSmallDiamond(world);
		setPosition(new Vector2(x,y));
		body.setTransform(getPosition(), 0);
		setAnimation(diamondAnimation);
	}
	
	public void startRedDiamond(World world,float x,float y){
		body = createSmallDiamond(world);
		setPosition(new Vector2(x,y));
		body.setTransform(getPosition(), 0);
		body.applyLinearImpulse(new Vector2(0f,7f), body.getPosition(), true);
		setAnimation(diamondAnimation);
		casted = true;
	}
	
	private Body createSmallDiamond(World world) {
		BodyDef def = new BodyDef();
		if(type == 1 )def.type = BodyType.StaticBody;
		if(type == 2 )def.type = BodyType.DynamicBody;
		def.fixedRotation = true;
		
		Body diamondBody = world.createBody(def);
		
		CircleShape diamondShape = new CircleShape();
		diamondShape.setRadius(0.15f);
		FixtureDef diamondShapeFixture = new FixtureDef();
		diamondShapeFixture.isSensor = true;
		diamondShapeFixture.shape = diamondShape;
		diamondBody.createFixture(diamondShapeFixture);
		diamondShape.dispose();	

		if(type == 1) diamondBody.getFixtureList().get(0).setUserData("smallDiamond");
		if(type == 2) diamondBody.getFixtureList().get(0).setUserData("redDiamond");
		
		return diamondBody;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if(mIsDestroyed) return;
		if(mIsCollected){
			body.getWorld().destroyBody(body);
			mIsDestroyed=true;
		}
		
		
	}
	
	@Override
	public void render(SpriteBatch batch) {
		if(!shouldRender) return;
		if(mIsCollected) return;
		
		TextureRegion reg = null;

		if(animation != null){
			reg = animation.getKeyFrame(stateTime,true);
		}

		batch.draw(reg.getTexture(), body.getPosition().x - origin.x , body.getPosition().y - origin.y, origin.x, origin.y, dimension.x, dimension.y,
				scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false,
				false);		
		
	}
	
	public boolean isCasted() {
		return casted;
	}

	public void setCasted(boolean casted) {
		this.casted = casted;
	}
	
	public int getType() {
		return type;
	}
	
	public boolean isIsCollected() {
		return mIsCollected;
	}

	public void setIsCollected(boolean IsCollected) {
		mIsCollected = IsCollected;
	}
	
	public void dispose(){
		for(TextureRegion region : diamondAnimation.getKeyFrames()){
			region.getTexture().dispose();
		}
	}
}
