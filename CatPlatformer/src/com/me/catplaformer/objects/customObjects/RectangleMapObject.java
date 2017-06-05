package com.me.catplaformer.objects.customObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.me.catplaformer.objects.CatPlatformerGameObject;

public class RectangleMapObject extends CatPlatformerGameObject{

	public TextureRegion objectRegion;
	
	private boolean transform;

	public RectangleMapObject(World world,float x,float y,float width,float height){
		position.set(x, y);
		dimension.set(width, height);
		objectWorld = world;
		body = createBody();
		transform = false;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		
	}
	
	public void update(){
		if(transform) {
			if(body.getType() != BodyType.DynamicBody) body.setType(BodyType.DynamicBody);
		}
	}
	

	public void setTransform(boolean m_transform) {
		transform = m_transform;
	}
	
	private Body createBody(){
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;		
		def.position.set(position);
		
		Body rectangleBody = objectWorld.createBody(def);
 
		PolygonShape rectangleShape = new PolygonShape();
		rectangleShape.setAsBox(dimension.x/2,dimension.y/2);
		
		FixtureDef rectangleBodyFixture = new FixtureDef();
		
		rectangleBodyFixture.density = 10f;
		rectangleBodyFixture.shape = rectangleShape;
		rectangleBodyFixture.friction = 1f;
		
		rectangleBody.createFixture(rectangleBodyFixture);
		
		rectangleBody.getFixtureList().get(0).setUserData("rectangle");
		rectangleShape.dispose();
		
		return rectangleBody;
	}

}
