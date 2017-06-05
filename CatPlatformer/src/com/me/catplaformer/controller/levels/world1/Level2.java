package com.me.catplaformer.controller.levels.world1;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;
import com.me.catplaformer.controller.levels.Level;
import com.me.catplaformer.objects.Cat;
import com.me.catplaformer.objects.customObjects.CustomPlatform;
import com.me.catplaformer.objects.customObjects.RectangleMapObject;
import com.me.catplaformer.shaders.VignetteShader;

public class Level2 extends Level{
	
	private Array<CustomPlatform> movingPlatforms;
	private RectangleMapObject rectangleMapObject;

	private Array<Body> birdgeBodys;

	public Level2(World world, Cat cat) {
		super();
		levelCat = cat;
		levelWorld = world;
		loadMap(2);
		birdgeBodys = new Array<Body>();
		init();
		createCollisionListener();
	}

	private void init() {
		createFallingPlatforms();
		rectangleMapObject = new RectangleMapObject(levelWorld,24.3f,5f,1.4f,2f);
		createBridge(32.25f,4.63f,0.3f,0.1f,29);
	}
	
	private void createFallingPlatforms() {
		movingPlatforms = new Array<CustomPlatform>();
		
		//First sequence
		movingPlatforms.add(new CustomPlatform(levelWorld,8f,4f,3.5f,1,3f,3f,2f,2f,3));
		
//		movingPlatforms.add(new CustomPlatform(levelWorld,12f,5f,3.5f,1,0.0f,2f,0,6f,2));
//		movingPlatforms.add(new CustomPlatform(levelWorld,16f,5f,3.5f,1,0.0f,2f,0,7f,2));
//		movingPlatforms.add(new CustomPlatform(levelWorld,19.5f,5f,3.5f,1,0.0f,2f,0,9f,2));
		
		
		
	}
	
	private void createBridge(float x, float y, float width,float heigth, int amount) {
		PolygonShape rect = new PolygonShape();
		rect.setAsBox(width,heigth);
		
		FixtureDef fixture = new FixtureDef();
		fixture.shape = rect;
		fixture.density = 1f;
		fixture.friction = 1.0f;
		fixture.restitution = 0.3f;
		
		Body firstBody = createCircle(x,y,0.3f,0.1f);
		Body finalBody = createCircle(x+10.5f,y,0.3f,0.1f);
		
		Body prevBody = firstBody;
		
		for(int i = 0; i < amount-1 ; i++){
			float newX = x + (i*width);;
			float newY = y ;
			
			BodyDef bodydef = new BodyDef();
			bodydef.type = BodyType.DynamicBody;
			bodydef.position.set(newX,newY);
			bodydef.allowSleep = true;
			
			Body body = levelWorld.createBody(bodydef);
			
			body.createFixture(fixture);
			
			RevoluteJointDef jointDef = new RevoluteJointDef();
			jointDef.bodyA = prevBody;
			jointDef.bodyB = body;
			jointDef.localAnchorA.x = 0.2f;
			jointDef.localAnchorA.y = 0f;
			jointDef.localAnchorB.x = -0.2f;
			jointDef.localAnchorB.y = 0f;

			levelWorld.createJoint(jointDef);
		
			prevBody = body;
		}
		
		RevoluteJointDef jointDef = new RevoluteJointDef();
		jointDef.bodyA = prevBody;
		jointDef.bodyB = finalBody;
		jointDef.localAnchorA.x = 0.2f;
		jointDef.localAnchorA.y = 0f;
		jointDef.localAnchorB.x = -0.2f;
		jointDef.localAnchorB.y = 0f;
		
		levelWorld.createJoint(jointDef);
		
		
	}

	@Override
	public void update(float deltaTime) {
		
		for(CustomPlatform fallingPlat : movingPlatforms){
			fallingPlat.update(deltaTime);
		}
		
		rectangleMapObject.update();
		
		levelCat.update(deltaTime);
	}

	@Override
	public void render(SpriteBatch batch, OrthographicCamera camera,VignetteShader shader) {
		batch.setProjectionMatrix(backgroundCamera.combined);
		batch.begin();
		backGroundSprite.draw(batch);
		batch.end();

		//Draw Map
		if(shader != null){
			renderer.getSpriteBatch().setShader(shader.getVignetteShader());
			shader.render();
		}else{
			renderer.getSpriteBatch().setShader(null);
		}
		renderer.setView(camera);
		renderer.render();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		 
		for(CustomPlatform fallingPlat : movingPlatforms){
			 fallingPlat.render(batch);
		 }
		 
		levelCat.render(batch);
		batch.end();
	}
	
	private void createCollisionListener() {
		levelWorld.setContactListener(new ContactListener(){

			@Override
			public void beginContact(Contact contact) {
				Fixture fixtureA = contact.getFixtureA();
	            Fixture fixtureB = contact.getFixtureB(); 
	            
	            if(fixtureA == null || fixtureB == null) return;
	            verifyJumpState(fixtureA,fixtureB,true);
	            if((fixtureA.getBody() == levelCat.body && fixtureB.getBody() == rectangleMapObject.body)
	            	|| (fixtureA.getBody() == rectangleMapObject.body && fixtureB.getBody() == levelCat.body)){
	            	
	            	rectangleMapObject.setTransform(true);
	            }
				
			}

			@Override
			public void endContact(Contact contact) {
					Fixture fixtureA = contact.getFixtureA();
		            Fixture fixtureB = contact.getFixtureB(); 
		            
		            if(fixtureA == null || fixtureB == null) return;
		            
				verifyJumpState(fixtureA,fixtureB,false);
				
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse contactImpulse) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void preSolve(Contact contact, Manifold contactManifold) {
				// TODO Auto-generated method stub
				
			}
			
			
			
		});
	}
		
	public void verifyJumpState(Fixture fixtureA,Fixture fixtureB,boolean ground) {	  
			if(fixtureA.getUserData() == null || fixtureB.getUserData() == null) return;
			
			if(fixtureA.getUserData().equals("legSensor") || fixtureB.getUserData().equals("legSensor")) {				

				if(fixtureA.getUserData().equals("ground") || fixtureA.getUserData().equals("movingPlatform") || fixtureA.getUserData().equals("fallingPlatform")) {
					if(ground)levelCat.footContacts++;
					else levelCat.footContacts--;
				}
 
				if(fixtureB.getUserData().equals("ground") || fixtureB.getUserData().equals("movingPlatform") || fixtureB.getUserData().equals("fallingPlatform")) {
					if(ground) levelCat.footContacts ++;
					else levelCat.footContacts--;
				}
			}
		
	}
	
	private Body createCircle(float x, float y, float width,float heigth){
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		def.position.set(x,y);
		def.fixedRotation = true;
		
		Body recBody = levelWorld.createBody(def);
 
		PolygonShape rect = new PolygonShape();
		rect.setAsBox(width, heigth);
		
		FixtureDef rectBodyFixture = new FixtureDef();
		
		rectBodyFixture.density = 1f;
		rectBodyFixture.shape = rect;
		rectBodyFixture.friction = 0.1f;
		
		recBody.createFixture(rectBodyFixture);
		
		rect.dispose();
		
		return recBody;
	}
	
	@Override
	public int getScore() {
		return 0;
	}

	@Override
	public void resetLevel() {
		
	}

	@Override
	public void endGame() {
		
	}

	@Override
	public void dispose() {
		
	}

}
