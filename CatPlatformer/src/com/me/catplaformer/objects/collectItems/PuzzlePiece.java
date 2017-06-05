package com.me.catplaformer.objects.collectItems;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.me.catplaformer.managers.ResourceManager;
import com.me.catplaformer.objects.CatPlatformerGameObject;

public class PuzzlePiece extends CatPlatformerGameObject{

	private boolean mIsCollected;
	private boolean mIsDestroyed;
	private boolean isCasted;
	
	private TextureRegion puzzlePiece;
	private Sprite puzzlePieceSprite;
	
	public PuzzlePiece(){
		init();
	}
	
	public void init(){
		dimension.set(0.4f,0.4f);
		origin.set(dimension.x / 2, dimension.y / 2);
		puzzlePiece = ResourceManager.instance.assetLevelCommom.PuzzlePiece;
		puzzlePieceSprite = new Sprite(puzzlePiece);
		isCasted = false;
	}
	
	public void start(World world,float x,float y){
		isCasted = true;
		body = createPuzzlePieceBody(world);
		setPosition(new Vector2(x,y));
		body.setTransform(getPosition(), 0);
		puzzlePieceSprite.setSize(dimension.x, dimension.y);
		puzzlePieceSprite.setOrigin(origin.x, origin.y);
		puzzlePieceSprite.setPosition(body.getPosition().x-origin.x, body.getPosition().y-origin.y);
		
		body.setAngularVelocity(5f);
		body.applyLinearImpulse(0,10f, body.getPosition().x,body.getPosition().y, true);
		body.applyAngularImpulse(5f, true);
	}
	
	private Body createPuzzlePieceBody(World world) {
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.fixedRotation = false;
		
		Body puzzlePieceBody = world.createBody(def);
		
		CircleShape puzzlePieceShape = new CircleShape();
		puzzlePieceShape.setRadius(0.15f);
		FixtureDef puzzlePieceShapeFixture = new FixtureDef();
		puzzlePieceShapeFixture.shape = puzzlePieceShape;
		puzzlePieceBody.createFixture(puzzlePieceShapeFixture);
		puzzlePieceShape.dispose();	

		puzzlePieceBody.getFixtureList().get(0).setUserData("puzzlePiece");
		
		return puzzlePieceBody;
	}
	
	@Override
	public void update(float deltaTime) {
		if(mIsDestroyed) return;
		if(mIsCollected){
			body.getWorld().destroyBody(body);
			mIsDestroyed=true;
		}

		super.update(deltaTime);
	}
	
	@Override
	public void render(SpriteBatch batch) {
		if(mIsCollected) return;
		
		puzzlePieceSprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
		puzzlePieceSprite.setPosition(body.getPosition().x - origin.x, body.getPosition().y - origin.y);
		puzzlePieceSprite.draw(batch);	

		
	}
	
	public boolean isCasted() {
		return isCasted;
	}

	public void setCasted(boolean isCasted) {
		this.isCasted = isCasted;
	}
	
	public boolean isCollected() {
		return mIsCollected;
	}

	public void setIsCollected(boolean IsCollected) {
		mIsCollected = IsCollected;
	}
	
	public void dispose(){
		puzzlePiece.getTexture().dispose();
	}
}
