package com.me.catplaformer.objects.customObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.me.catplaformer.managers.ResourceManager;
import com.me.catplaformer.objects.CatPlatformerGameObject;

public class CustomMapObject extends CatPlatformerGameObject{

	public TextureRegion objectRegion;
	
	private Animation waterAnimation;
	
	public Body stairsBody;
	public Body stairsFinnishBody;
	
	public CustomMapObject(World world,float x,float y,float width,float height){
		init(world,x,y,width,height);
	}
	
	public CustomMapObject(World world,float x,float y,float width,float height,boolean stairs){
		createStairsPhysics(world,x,y,width,height);
	}
	
	public void init(World world,float x, float y, float width, float height){
		dimension.set(width, height);
		origin.set(dimension.x / 2, dimension.y / 2);
		position = new Vector2(x,y);
		
		waterAnimation = ResourceManager.instance.assetLevel1Objects.waterAnimation;
		setAnimation(waterAnimation);
		body = createWater(world);

	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
	}
	
	@Override
	public void render(SpriteBatch batch) {
		if(!shouldRender) return;
		TextureRegion reg = null;

		if(waterAnimation != null){
			reg = waterAnimation.getKeyFrame(stateTime,true);
		}else{
			reg = objectRegion;
		}

		batch.draw(reg.getTexture(),position.x - origin.x ,position.y - origin.y, origin.x, origin.y, dimension.x, dimension.y,
				scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false,
				false);
	}
	
	private Body createWater(World world) {
		BodyDef def = new BodyDef();
		def.type = BodyType.KinematicBody;
		
		Body waterBody = world.createBody(def);

		PolygonShape waterShape = new PolygonShape();
		waterShape.setAsBox(dimension.x/4,dimension.y/4);
		
		FixtureDef waterFixture = new FixtureDef();
		
		waterFixture.density = 1f;
		waterFixture.shape = waterShape;
		waterFixture.friction = 5f;
		waterFixture.isSensor = true;
		waterBody.createFixture(waterFixture);
		waterBody.getFixtureList().get(0).setUserData("water");
	
		waterShape.dispose();
		
		waterBody.setTransform(new Vector2(position.x,position.y),0);
		
		return waterBody;
		
	}

	private void createStairsPhysics(World world,float x,float y,float width,float height) {
		BodyDef def = new BodyDef();
		def.type = BodyType.KinematicBody;
		
		stairsBody = world.createBody(def);

		PolygonShape stairsShape = new PolygonShape();
		stairsShape.setAsBox((float) (width/2), height/2);
		
		FixtureDef stairsFixture = new FixtureDef();
		
		stairsFixture.density = 1f;
		stairsFixture.shape = stairsShape;
		stairsFixture.friction = 5f;
		stairsFixture.isSensor = true;
		stairsBody.createFixture(stairsFixture);
		stairsBody.getFixtureList().get(0).setUserData("stairs");
	
		stairsShape.dispose();
		
		stairsBody.setTransform(new Vector2(x,y),0);
		
		BodyDef stairsFinnishDef = new BodyDef();
		stairsFinnishDef.type = BodyType.KinematicBody;
		
		stairsFinnishBody = world.createBody(stairsFinnishDef);

		EdgeShape stairsFinnishShape = new EdgeShape();
		stairsFinnishShape.set(x-(width/2), y+(height/2), x+(width/2), y+(height/2));
		
		FixtureDef stairsFinnishFixture = new FixtureDef();
		
		stairsFinnishFixture.friction = 0f;
		stairsFinnishFixture.density = 0f;
		stairsFinnishFixture.shape = stairsFinnishShape;
		stairsFinnishFixture.isSensor = true;
		stairsFinnishBody.createFixture(stairsFinnishFixture);
		stairsFinnishBody.getFixtureList().get(0).setUserData("stairsFinnish");
	
		stairsFinnishShape.dispose();
		
		
	}
	
	public void dispose(){
		if(objectRegion != null ) objectRegion.getTexture().dispose();
		
		for(TextureRegion region : waterAnimation.getKeyFrames()){
			region.getTexture().dispose();
		}
	}
}
