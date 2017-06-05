package com.me.catplaformer.objects.collectItems;

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

public class CollectibleFish extends CatPlatformerGameObject{

	private boolean mIsCollected;
	private boolean mIsDestroyed;
	private TextureRegion regCollectibleFish;
	
	public CollectibleFish(World world,float x,float y){
		init(world,x,y);
	}
	
	public void init(World world,float x,float y){
		regCollectibleFish = ResourceManager.instance.assetLevelCommom.collectibleFish;		
		body = createCollectibleFish(world);
		setPosition(new Vector2(x,y));
		body.setTransform(getPosition(), 0);
		dimension.set(0.8f,1.2f);
		origin.set(dimension.x / 2, dimension.y / 2);
	}
	
	private Body createCollectibleFish(World world) {
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		def.fixedRotation = true;
		
		Body collectibleFishBody = world.createBody(def);
		
		CircleShape collectibleFishShape = new CircleShape();
		collectibleFishShape.setRadius(0.15f);
		FixtureDef collectibleFishFixture = new FixtureDef();
		collectibleFishFixture.isSensor = true;
		collectibleFishFixture.shape = collectibleFishShape;
		collectibleFishBody.createFixture(collectibleFishFixture);
		collectibleFishShape.dispose();	

		collectibleFishBody.getFixtureList().get(0).setUserData("collectibleFish");
		
		return collectibleFishBody;
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

		batch.draw(regCollectibleFish.getTexture(), body.getPosition().x - origin.x , body.getPosition().y - origin.y, origin.x, origin.y, dimension.x, dimension.y,
				scale.x, scale.y, rotation, regCollectibleFish.getRegionX(), regCollectibleFish.getRegionY(), regCollectibleFish.getRegionWidth(), regCollectibleFish.getRegionHeight(), false,
				false);		
		
	}
	
	public boolean isIsCollected() {
		return mIsCollected;
	}

	public void setIsCollected(boolean IsCollected) {
		mIsCollected = IsCollected;
	}

	public void dispose(){
		regCollectibleFish.getTexture().dispose();
	}
}
