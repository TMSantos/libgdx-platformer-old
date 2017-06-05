package com.me.catplaformer.controller.levels.world1;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.me.catplaformer.controller.levels.Level;
import com.me.catplaformer.controller.tmxToBox2d.MapObject;
import com.me.catplaformer.objects.Bird;
import com.me.catplaformer.objects.Bird.STATE_BIRD;
import com.me.catplaformer.objects.Cat;
import com.me.catplaformer.objects.Cat.STATE;
import com.me.catplaformer.objects.Cat.VIEW_DIRECTION;
import com.me.catplaformer.objects.ObjectBodyData;
import com.me.catplaformer.objects.Waypoint;
import com.me.catplaformer.objects.collectItems.CollectibleFish;
import com.me.catplaformer.objects.collectItems.Diamond;
import com.me.catplaformer.objects.collectItems.PuzzlePiece;
import com.me.catplaformer.objects.customObjects.CustomMapObject;
import com.me.catplaformer.objects.customObjects.CustomPlatform;
import com.me.catplaformer.objects.dogs.DogEnemy;
import com.me.catplaformer.objects.dogs.DogEnemy.DOG_TYPE;
import com.me.catplaformer.objects.dogs.DogEnemy.STATE_DOG;
import com.me.catplaformer.objects.enemyCats.WhiteEnemy;
import com.me.catplaformer.objects.enemyCats.WhiteEnemy.ENEMY_CAT_STATE;
import com.me.catplaformer.shaders.VignetteShader;
import com.me.catplaformer.utils.GamePreferences;

public class Level1 extends Level {

	private CustomMapObject water1;
	private CustomMapObject water2;
	private CustomMapObject stairs;
	
	private Array<CustomPlatform> fallingPlatforms;
	private Array<CustomPlatform> movingPlatforms;
	
	private WhiteEnemy enemyCat;

	private Array<DogEnemy> dogEnemys;
	private Array<Bird> birds;
	
	private Array<CollectibleFish> collectibleFish;
	
	private Array<Diamond> redDiamonds;
		
	private ObjectBodyData mFishData;

	public Level1(World world,Cat cat){
		super();
		levelCat = cat;
		levelWorld = world;
		loadMap(1);
		levelMapCommonPhysics = tmxToBox2d.getLevelMapCummomPhysics();

		endGame = false;
		startEndGameTimer = 0;
		puzzlePieceCollected = false;
		init();
	}

	private void init() {	
		mIsGameOver = false;		
		puzzlePiece = new PuzzlePiece();
		stairs = new CustomMapObject(levelWorld,163.5f,4.5f,0.5f,2.5f,true);
		createFallingPlatforms();
		createMovingPlatforms();
		createLevelWater();
		createDogEnemys();
		createBirds();
		createDiamonds();
		createRedDiamonds();
		createColletibleFish();
		createWaypoints();
		createEnemyCat();
		createCollisionListener();
	}
	
	private void createEnemyCat() {
		enemyCat = new WhiteEnemy(levelWorld,132f,4f);
	}

	private void createColletibleFish() {
		if(levelMapCommonPhysics.mapCollectibleFish == null || levelMapCommonPhysics.mapCollectibleFish.size == 0) return;
		collectibleFish = new Array<CollectibleFish>();
		
		for(MapObject fish : levelMapCommonPhysics.mapCollectibleFish){
			collectibleFish.add(new CollectibleFish(levelWorld,fish.positionX,fish.positionY));
		}
	}

	private void createDiamonds() {
		if(levelMapCommonPhysics.mapDiamonds == null) return;
		
		diamonds = new Array<Diamond>();
		for(MapObject object : levelMapCommonPhysics.mapDiamonds){
			diamonds.add(new Diamond(levelWorld,object.positionX,object.positionY));
		}
		
	}
	
	private void createRedDiamonds() {
		redDiamonds = new Array<Diamond>();
		redDiamonds.add(new Diamond());
		redDiamonds.add(new Diamond());
		redDiamonds.add(new Diamond());
		redDiamonds.add(new Diamond());
		redDiamonds.add(new Diamond());
		redDiamonds.add(new Diamond());
		redDiamonds.add(new Diamond());
		redDiamonds.add(new Diamond());
		redDiamonds.add(new Diamond());

	}

	private void createDogEnemys() {
		
		dogEnemys = new Array<DogEnemy>();

		dogEnemys.add(new DogEnemy(levelWorld,38f,4f,4f,DOG_TYPE.BROWN));
		dogEnemys.add(new DogEnemy(levelWorld,81f,3f,2.8f,DOG_TYPE.BROWN));
		dogEnemys.add(new DogEnemy(levelWorld,117f,4f,1.8f,DOG_TYPE.BROWN));

		dogEnemys.add(new DogEnemy(levelWorld,158.5f,4f,3f,DOG_TYPE.WHITE));
		dogEnemys.add(new DogEnemy(levelWorld,182f,4f,3f,DOG_TYPE.WHITE));
		dogEnemys.add(new DogEnemy(levelWorld,190f,4f,3f,DOG_TYPE.WHITE));
		
		dogEnemys.add(new DogEnemy(levelWorld,312f,4f,1.8f,DOG_TYPE.RED));
		
	}
	
	private void createBirds() {
		birds = new Array<Bird>();
		birds.add(new Bird(levelWorld,2f,12f,4.5f));
		birds.add(new Bird(levelWorld,2f,38f,6f));
		birds.add(new Bird(levelWorld,2f,72f,6.5f));
		birds.add(new Bird(levelWorld,2f,103f,8.5f));
		birds.add(new Bird(levelWorld,2f,113.5f,8.3f));
		birds.add(new Bird(levelWorld,2f,173f,7f));
		birds.add(new Bird(levelWorld,2f,273.5f,8f));
		birds.add(new Bird(levelWorld,2f,293f,6.5f));
		birds.add(new Bird(levelWorld,2f,331f,5.5f));
	}
	
	@Override
	public void update(float deltaTime) {
		updateLevelLogic(deltaTime);	
	}

	private void updateLevelLogic(float deltaTime){	
		
		if(levelCat.body.getPosition().y < 0){
			mIsGameOver = true;
		}
		
		levelCat.update(deltaTime);		
		
		enemyCat.update(deltaTime);
		
		for(CustomPlatform platform : fallingPlatforms){
			platform.update(deltaTime);
		}
		
		for(CustomPlatform platform : movingPlatforms){
			platform.update(deltaTime);
		}
		
		water1.update(deltaTime);
		water2.update(deltaTime);
		
		for(Diamond diamond : diamonds){
			diamond.update(deltaTime);
		}
		
		for(Diamond diamond : redDiamonds){
			diamond.update(deltaTime);
		}
		
		for(CollectibleFish collectFish : collectibleFish){
			collectFish.update(deltaTime);
		}
		
		for(DogEnemy dog : dogEnemys){
			dog.update(deltaTime);					
		}
		
		for(Bird bird : birds){			
			bird.update(deltaTime);
		}
		
		for(Waypoint waypoint : waypoints){
			waypoint.update(deltaTime);
		}
		
		enemyCatLogicUpdate();
		
		if(enemyCat.mIsDestroyed){
			if(!puzzlePiece.isCasted()){
				puzzlePiece.start(levelWorld,enemyCat.body.getPosition().x, enemyCat.body.getPosition().y);
			}
		}
		
		if(puzzlePiece.isCasted()) puzzlePiece.update(deltaTime);
	
		if(endGame) startEndGameTimer += deltaTime;
		
		System.out.println(movingPlatforms.get(0).body.getPosition().x);
		
	}

	private void enemyCatLogicUpdate() {
		if(levelCat.body.getPosition().x > enemyCat.body.getPosition().x - 4f
				&& levelCat.body.getPosition().x < enemyCat.body.getPosition().x){
				if(enemyCat.enemyCatState != ENEMY_CAT_STATE.Dying) enemyCat.moveLeft();
			}else if(levelCat.body.getPosition().x < enemyCat.body.getPosition().x + 4f
					 && levelCat.body.getPosition().x > enemyCat.body.getPosition().x){
				if(enemyCat.enemyCatState != ENEMY_CAT_STATE.Dying) enemyCat.moveRight();
			}else{
				if(enemyCat.enemyCatState != ENEMY_CAT_STATE.Dying) enemyCat.normalMovement();
			}
		
	}

	@Override
	public void render(SpriteBatch batch, OrthographicCamera camera,VignetteShader shader) {
		//Draw background
		batch.setProjectionMatrix(backgroundCamera.combined);
		batch.begin();
		backGroundSprite.draw(batch);
		batch.end();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		water1.render(batch);
		water2.render(batch);
		
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
		
		//Draw MapObjects
		batch.begin();
		for(CustomPlatform platform : fallingPlatforms){
			platform.render(batch);		
		}
		
		for(CustomPlatform platform : movingPlatforms){
			platform.render(batch);		
		}	
		
		for(Diamond diamond : diamonds){
			diamond.render(batch);
		}
	
		for(Diamond diamond : redDiamonds){
			if(diamond.isCasted()) diamond.render(batch);
		}
		
		for(CollectibleFish collectFish : collectibleFish){
			collectFish.render(batch);
		}

		enemyCat.render(batch);
	
		for(DogEnemy dog : dogEnemys){
			dog.render(batch);
		}
		
		for(Bird bird : birds){
			bird.render(batch);
		}

		for(Waypoint waypoint : waypoints){
			waypoint.render(batch);
		}
		
		levelCat.render(batch);
		
		if(puzzlePiece.isCasted()) puzzlePiece.render(batch);
		
		batch.end();
		
	}
	
	private void createLevelWater() {	
		water1 = new CustomMapObject(levelWorld,247.8f,1f,25f,5f);
		water2 = new CustomMapObject(levelWorld,291.5f,1f,45f,5f);
			
	}
	
	private void createWaypoints() {
		if(levelMapCommonPhysics.mapWaypoints == null || levelMapCommonPhysics.mapWaypoints.size == 0) return;
		
		waypoints = new Array<Waypoint>();
		int number = 1;
		for(MapObject wp : levelMapCommonPhysics.mapWaypoints){
			waypoints.add(new Waypoint(levelWorld,number,wp.positionX,wp.positionY));
			number++;
		}
	}
	
	private void createFallingPlatforms() {
		
		fallingPlatforms = new Array<CustomPlatform>();
	
		//First sequence
		fallingPlatforms.add(new CustomPlatform(levelWorld,92.7f,3.7f,3.5f,1,0,0,2,0,1));
		fallingPlatforms.add(new CustomPlatform(levelWorld,95.6f,5f,3f,2,0,0,2,0,1));
		fallingPlatforms.add(new CustomPlatform(levelWorld,98.4f,5.3f,3f,3,0,0,2,0,1));
		fallingPlatforms.add(new CustomPlatform(levelWorld,100.5f,5.9f,3.2f,4,0,0,2,0,1));
		fallingPlatforms.add(new CustomPlatform(levelWorld,101.7f,4.4f,3f,2,0,0,2,0,1));
		fallingPlatforms.add(new CustomPlatform(levelWorld,103.6f,5f,3.5f,1,0,0,2,0,1));
		fallingPlatforms.add(new CustomPlatform(levelWorld,105.4f,5.9f,3f,3,0,0,2,0,1));
		fallingPlatforms.add(new CustomPlatform(levelWorld,107.2f,4.7f,3.2f,4,0,0,2,0,1));
		fallingPlatforms.add(new CustomPlatform(levelWorld,110.8f,5.9f,3f,2,0,0,2,0,1));
		
		//End of first sequence
		fallingPlatforms.add(new CustomPlatform(levelWorld,197.1f,3.4f,3f,3,0,0,2,0,1));
		
		fallingPlatforms.add(new CustomPlatform(levelWorld,247f,3.4f,3f,3,0,0,2,0,1));
		
		fallingPlatforms.add(new CustomPlatform(levelWorld,267f,3.6f,3f,2,0,0,2,0,1));
		fallingPlatforms.add(new CustomPlatform(levelWorld,269.2f,4.6f,3.2f,4,0,0,2,0,1));
		
		//Water sequence
		fallingPlatforms.add(new CustomPlatform(levelWorld,284,2.8f,3.2f,4,0,0,2,0,1));
		fallingPlatforms.add(new CustomPlatform(levelWorld,286.35f,3.75f,3f,3,0,0,2,0,1));
		fallingPlatforms.add(new CustomPlatform(levelWorld,288.8f,2.8f,3f,2,0,0,2,0,1));
		fallingPlatforms.add(new CustomPlatform(levelWorld,290.7f,3.75f,3.5f,1,0,0,2,0,1));
		fallingPlatforms.add(new CustomPlatform(levelWorld,292.8f,2.8f,3f,3,0,0,2,0,1));
		fallingPlatforms.add(new CustomPlatform(levelWorld,294.5f,3.85f,3f,2,0,0,2,0,1));
		fallingPlatforms.add(new CustomPlatform(levelWorld,296.4f,2.8f,3.5f,1,0,0,2,0,1));
		fallingPlatforms.add(new CustomPlatform(levelWorld,298.7f,3.75f,3f,3,0,0,2,0,1));
		//End of water sequence
	}
	
	private void createMovingPlatforms() {
		movingPlatforms = new Array<CustomPlatform>();
		movingPlatforms.add(new CustomPlatform(levelWorld,171f,2f,3.5f,4,3.5f,0,2,0,2));
		movingPlatforms.add(new CustomPlatform(levelWorld,223f,1.7f,3f,2,2.3f,2,0,0,2));
		movingPlatforms.add(new CustomPlatform(levelWorld,233f,1.7f,3f,3,4.3f,2,0,0,2));
		
	}
	
	private void verifyFishColision(Fixture fixtureA,Fixture fixtureB) {
		
			 if(fixtureA.getUserData() != null
				&& fixtureA.getUserData().equals("fish")
				&& fixtureB.getBody() != levelCat.body
				&& fixtureB.getBody().getUserData() != "smallDiamond"
				&& fixtureB.isSensor() == false){
				 
			
			mFishData = (ObjectBodyData) fixtureA.getBody().getUserData();
			mFishData.active = false;
			fixtureA.getBody().setUserData(mFishData);
			
			if(fixtureB.getBody() == enemyCat.body){
				enemyCat.takeLifeAwayWithFish();
				return;
			}
			 
			if(fixtureB.getUserData() != null 
			   && fixtureB.getUserData().equals("dogBody")){
				for(DogEnemy dog : dogEnemys){
					if(fixtureB.getBody() == dog.body){
						dog.setStateDog(STATE_DOG.Dead);
						return;
					}
				}
			}
		}
		
		if(fixtureB.getUserData() != null
				&& fixtureB.getUserData().equals("fish")
				&& fixtureA.getBody() != levelCat.body
				&& fixtureA.getBody().getUserData() != "smallDiamond"
				&& fixtureA.isSensor() == false){
			
			mFishData = (ObjectBodyData) fixtureB.getBody().getUserData();
			mFishData.active = false;
			fixtureB.getBody().setUserData(mFishData);
			
			if(fixtureA.getBody() == enemyCat.body){
				enemyCat.takeLifeAwayWithFish();
				return;
			}
			
			if(fixtureA.getUserData() != null 
					   && fixtureA.getUserData().equals("dogBody")){
				for(DogEnemy dog : dogEnemys){
					if(fixtureA.getBody() == dog.body){
						dog.setStateDog(STATE_DOG.Dead);
						return;
					}
				}
			}
		}
	}
	
	private void verifyCollectItemsContacts(Fixture fixtureA,Fixture fixtureB) {
		
		if(fixtureA.getUserData() == null || fixtureB.getUserData() == null) return;
		
		if((fixtureA.getUserData() == "catBody" ||
				fixtureB.getUserData() == "catBody" )) {

			if(fixtureA.getUserData() != null && (fixtureA.getUserData() == "smallDiamond")){
				for(Diamond diamond : diamonds){
					if(diamond.body == fixtureA.getBody()){
						mScore += 10;
						diamond.setIsCollected(true);
					}
				}	
			}
			
			if(fixtureA.getUserData() != null && (fixtureA.getUserData() == "redDiamond")){
				for(Diamond redDiamond : redDiamonds){
					if(redDiamond.body == fixtureA.getBody()){
						mScore += 20;
						redDiamond.setIsCollected(true);
					}
				}
			}
			
			if(fixtureA.getUserData() != null && (fixtureA.getUserData() == "collectibleFish")){
				for(CollectibleFish collectedFish : collectibleFish){
					if(collectedFish.body == fixtureA.getBody()){
						if(collectedFish.isIsCollected()){
							return;
						}else{
							levelCat.setFishQuantity(levelCat.getFishQuantity()+1);
							collectedFish.setIsCollected(true);
						}
					}
				}
			}
			
			if(fixtureA.getUserData() != null && (fixtureA.getUserData() == "puzzlePiece")){
				puzzlePiece.setIsCollected(true);
				puzzlePieceCollected = true;
			}
	
			if(fixtureB.getUserData() != null && (fixtureB.getUserData() == "smallDiamond")){
				for(Diamond diamond : diamonds){
					if(diamond.body == fixtureB.getBody()){
							mScore += 10;
							diamond.setIsCollected(true);
					}
				}
			}
			
			if(fixtureB.getUserData() != null && (fixtureB.getUserData() == "redDiamond")){
				for(Diamond redDiamond : redDiamonds){
					if(redDiamond.body == fixtureB.getBody()){
						mScore += 20;
						redDiamond.setIsCollected(true);
					}
				}
			}
			
			if(fixtureB.getUserData() != null && (fixtureB.getUserData() == "collectibleFish")){
				for(CollectibleFish collectedFish : collectibleFish){
					if(collectedFish.body == fixtureB.getBody()){
						if(collectedFish.isIsCollected()){
							return;
						}else{
							levelCat.setFishQuantity(levelCat.getFishQuantity()+1);
							collectedFish.setIsCollected(true);
						}
					}
				}
			}
			
			if(fixtureB.getUserData() != null && (fixtureB.getUserData() == "puzzlePiece")){
				puzzlePiece.setIsCollected(true);
				puzzlePieceCollected = true;
			}
			
		}
		
	}

	private void verifyWaterContact(Fixture fixtureA,Fixture fixtureB) {
		
		if((fixtureA.getUserData() != null && fixtureA.getUserData().equals("water") ||
				fixtureB.getUserData() != null && fixtureB.getUserData().equals("water") )) {
					
			if(fixtureA.getBody() != null && (fixtureA.getBody() == levelCat.body)){
				levelCat.state = STATE.Dying;
				mIsGameOver = true;
				levelCat.shouldRender = false;
			}
	
			if(fixtureB.getBody() != null && (fixtureB.getBody() == levelCat.body )){
				levelCat.state = STATE.Dying;
				mIsGameOver = true;
				levelCat.shouldRender = false;
			}
			
			if(fixtureA.getUserData() != null && (fixtureA.getUserData().equals("fallingPlatform"))){
				for(CustomPlatform fallingPlatform : fallingPlatforms){
					if(fallingPlatform.body == fixtureA.getBody()){
						fallingPlatform.shouldRender = false;
						fallingPlatform.update = false;
					}
				}
			}
			
			if(fixtureB.getUserData() != null && (fixtureB.getUserData().equals("fallingPlatform"))){
				for(CustomPlatform fallingPlatform : fallingPlatforms){
					if(fallingPlatform.body == fixtureB.getBody()){
						fallingPlatform.shouldRender = false;
						fallingPlatform.update = false;
					}
				}
			}
		}
		
	}
	
	private void verifyEnemyContact(Fixture fixtureA,Fixture fixtureB) {
		
		if(fixtureA.getUserData() == null || fixtureB.getUserData() == null) return;
		
		if(fixtureA.getUserData().equals("catBody") || fixtureA.getUserData().equals("catLegs")
			|| fixtureB.getUserData().equals("catBody") || fixtureB.getUserData().equals("catLegs")) {
					
			if((fixtureA.getUserData().equals("dogBody")
			   || fixtureA.getUserData().equals("bone")
			   || fixtureA.getUserData().equals("enemyFish")
			   || fixtureA.getUserData().equals("enemyCatBody"))){
				
				levelCat.state = STATE.Dying;
				mIsGameOver = true;
				return;
			}
	
			if((fixtureB.getUserData().equals("dogBody")
			    || fixtureB.getUserData().equals("bone")
				|| fixtureB.getUserData().equals("enemyFish")
				|| fixtureB.getUserData().equals("enemyCatBody"))){
				
				levelCat.state = STATE.Dying;
				mIsGameOver = true;
				return;
			}
		}	
		
	}
	
	private void verifyDynamicPlatformsContacts(Fixture fixtureA,Fixture fixtureB) {
		for(CustomPlatform platform : fallingPlatforms){
			
			if((fixtureA == platform.body.getFixtureList().get(0)
					|| fixtureB == platform.body.getFixtureList().get(0))){
			
				if(fixtureA.getUserData() != null && (fixtureA.getUserData().equals("ground"))){
					fixtureB.setRestitution(0);
				}
			
				if(fixtureB.getUserData() != null && (fixtureB.getUserData().equals("ground"))){
					fixtureA.setRestitution(0);
				}
			
				if(fixtureA.getUserData() != null && (fixtureA.getUserData().equals("catLegs"))) {
					if(!platform.isPlatformGoingToFall){
						platform.startFallingTimer();
						platform.isPlatformGoingToFall = true;
					}
				}
			
				if(fixtureB.getUserData() != null && (fixtureB.getUserData().equals("catLegs"))) {
					if(!platform.isPlatformGoingToFall){
						platform.startFallingTimer();
						platform.isPlatformGoingToFall = true;
					}	
				}
			}
		}
		
	}
	
	private void verifyScratchContact(Fixture fixtureA,Fixture fixtureB) {
		
		if(fixtureA.getUserData() == null || fixtureB.getUserData() == null) return;
		
		if((((fixtureA.getUserData() == "clawLeft" && levelCat.viewDirection == VIEW_DIRECTION.LEFT) || (fixtureB.getUserData() == "clawLeft" && levelCat.viewDirection == VIEW_DIRECTION.LEFT))
			|| 
		   ((fixtureA.getUserData() == "clawRight" && levelCat.viewDirection == VIEW_DIRECTION.RIGHT) || (fixtureB.getUserData() == "clawRight" && levelCat.viewDirection == VIEW_DIRECTION.RIGHT)))) {
			
			if(fixtureA.getUserData() != null && (fixtureA.getUserData() == "dogBody")){
				for(DogEnemy dog : dogEnemys){
					if(dog.body == fixtureA.getBody()) dog.setStateDog(STATE_DOG.Dead);
				}
			}
	
			if(fixtureB.getUserData() != null && (fixtureB.getUserData() == "dogBody")){
				for(DogEnemy dog : dogEnemys){
					if(dog.body == fixtureB.getBody()) dog.setStateDog(STATE_DOG.Dead);
				}
			}
			
			if(fixtureA.getUserData() != null && (fixtureA.getUserData() == "bird")){
				for(int i = 0; i < birds.size ; i++){
					if(birds.get(i).body == fixtureA.getBody()){
						birds.get(i).setStateBird(STATE_BIRD.Dead);
//						redDiamonds.get(i).startRedDiamond(levelWorld, birds.get(i).getPosition().x,birds.get(i).getPosition().y);
					}
				}
			}
	
			if(fixtureB.getUserData() != null && (fixtureB.getUserData() == "bird")){
				for(int i = 0; i < birds.size ; i++){
					if(birds.get(i).body == fixtureB.getBody()){
						birds.get(i).setStateBird(STATE_BIRD.Dead);
//						redDiamonds.get(i).startRedDiamond(levelWorld, birds.get(i).getPosition().x,birds.get(i).getPosition().y);
					}
				}
			}
			
			if(fixtureA.getUserData() != null && (fixtureA.getUserData() == "enemyCatBody")){
				enemyCat.takeLifeAwayWithClaw();
			}
	
			if(fixtureB.getUserData() != null && (fixtureB.getUserData() == "enemyCatBody")){
				enemyCat.takeLifeAwayWithClaw();
			}
			
		}
		
	}
	
	private void verifyWayPoints(Fixture fixtureA,Fixture fixtureB){

		if((fixtureA.getBody() != null && fixtureA.getBody() == levelCat.body) 
			|| (fixtureB.getBody() != null && fixtureB.getBody() == levelCat.body)){
			
			if(fixtureA.getUserData() != null && fixtureA.getUserData().equals("wayPointSensor1") || fixtureB.getUserData() != null && fixtureB.getUserData().equals("wayPointSensor1")) {
				saveData(levelCat.getPosition().x);
				waypoints.get(0).onContact();
			}
			
			if((fixtureA.getUserData() != null && fixtureA.getUserData().equals("wayPointSensor2") || fixtureB.getUserData() != null && fixtureB.getUserData().equals("wayPointSensor2"))) {
				saveData(levelCat.getPosition().x);
				waypoints.get(1).onContact();
			}
			
			if((fixtureA.getUserData() != null && fixtureA.getUserData().equals("wayPointSensor3") || fixtureB.getUserData() != null && fixtureB.getUserData().equals("wayPointSensor3"))) {
				saveData(levelCat.getPosition().x);
				waypoints.get(2).onContact();
				
			}
			
			if((fixtureA.getUserData() != null && fixtureA.getUserData().equals("wayPointSensor4") || fixtureB.getUserData() != null && fixtureB.getUserData().equals("wayPointSensor4"))) {
				saveData(levelCat.getPosition().x);
				waypoints.get(3).onContact();
			}
			
			if((fixtureA.getUserData() != null && fixtureA.getUserData().equals("wayPointSensor5") || fixtureB.getUserData() != null && fixtureB.getUserData().equals("wayPointSensor5"))) {
				waypoints.get(4).onContact();
				endGame();
			}
			
		}
	}
	
	private void invalidateOldBones(Contact contact){
		if(contact.isTouching() && (contact.getFixtureA().getUserData() == "bone" ||
				   contact.getFixtureB().getUserData() == "bone" )) {
			
			if(contact.getFixtureA().getUserData() != null && (contact.getFixtureA().getUserData() == "ground")){
				contact.getFixtureB().setUserData("deadBone");
			}
	
			if(contact.getFixtureB().getUserData() != null && (contact.getFixtureB().getUserData() == "ground")){
				contact.getFixtureA().setUserData("deadBone");
			}
		}
	}
		
	private void saveData(float i) {
		GamePreferences gameProgress = GamePreferences.instance;
		gameProgress.levelProgress = i;
		gameProgress.saveGameProgress();
	}
	
	@Override
	public void resetLevel(){
		
		for(CustomPlatform fallingPlatform : fallingPlatforms){
			levelWorld.destroyBody(fallingPlatform.body);
		}
		

		fallingPlatforms.clear();
		fallingPlatforms = null;
		createFallingPlatforms();
		levelCat.reset();
		mIsGameOver = false;
		levelCat.shouldRender = true;
	}

	@Override
	public int getScore() {
		return mScore;
	}

	public void setScore(int score) {
		mScore = score;
	}
	
	@Override
	public void dispose() {
//		AudioManager.instance.stopMusic();
		
		disposeAllTextures();
		if(levelWorld != null) levelWorld.dispose();
		renderer.dispose();
		map.dispose();
		backGroundTexture.dispose();
		
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
	
	public boolean isCatOnMovingPlatform(Array<Contact> contactList) {	
		for(Contact contact: contactList){
			Fixture fixtureA = contact.getFixtureA();
            Fixture fixtureB = contact.getFixtureB(); 
			
			if(fixtureA == levelCat.body.getFixtureList().get(1) ||
					fixtureB == levelCat.body.getFixtureList().get(1)) {				

					if(fixtureA.getUserData() != null && (fixtureA.getUserData().equals("movingPlatform"))) {
						return true;
					}
 
					if(fixtureB.getUserData() != null && (fixtureB.getUserData().equals("movingPlatform"))) {
						return true;
					}
					return false;
			}
		}
			return false;
	}
	
	public void verifyStairsContact(Fixture fixtureA,Fixture fixtureB,boolean onStairs){
        if(fixtureA.getUserData() == null || fixtureB.getUserData() == null) return;
        
		if(fixtureA.getUserData().equals("legSensor") || fixtureB.getUserData().equals("legSensor")) {				

		   if(fixtureA.getUserData() != null && (fixtureA.getUserData().equals("stairs"))) {
			   if(onStairs) levelCat.stairsContacts++;
			   else levelCat.stairsContacts--;
			}
 
			if(fixtureB.getUserData() != null && (fixtureB.getUserData().equals("stairs"))) {
				if(onStairs) levelCat.stairsContacts++;
				else levelCat.stairsContacts--;
			}

		}

	}
	
	private void createCollisionListener() {
		levelWorld.setContactListener(new ContactListener(){

			@Override
			public void beginContact(Contact contact) {
				Fixture fixtureA = contact.getFixtureA();
	            Fixture fixtureB = contact.getFixtureB(); 
	            
	            if(fixtureA == null || fixtureB == null) return;
	            
	            verifyJumpState(fixtureA,fixtureB,true);
	            verifyStairsContact(fixtureA,fixtureB,true);
				verifyWaterContact(fixtureA,fixtureB);
				verifyEnemyContact(fixtureA,fixtureB);
				if(levelCat.state == STATE.Scratch) verifyScratchContact(fixtureA,fixtureB);
			//	invalidateOldBones(fixtureA,fixtureB);
				verifyWayPoints(fixtureA,fixtureB);
				verifyDynamicPlatformsContacts(fixtureA,fixtureB);
				verifyCollectItemsContacts(fixtureA,fixtureB);
				verifyFishColision(fixtureA,fixtureB);
				
//				levelCat.setIsOnMovingPlatform(isCatOnMovingPlatform(mContactList));
			}

			@Override
			public void endContact(Contact contact) {
				Fixture fixtureA = contact.getFixtureA();
	            Fixture fixtureB = contact.getFixtureB(); 
	            if(fixtureA == null || fixtureB == null) return;
	            verifyJumpState(fixtureA,fixtureB,false);
	            verifyStairsContact(fixtureA,fixtureB,false);		
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
	}
	
	@Override
	public void endGame() {		
		saveData(5);
		endGame = true;	
		
	}
	
	private void disposeAllTextures() {
		
		levelCat.dispose();	
		enemyCat.dispose();
		
		for(CustomPlatform platform : fallingPlatforms){
			platform.dispose();
		}
		
		for(CustomPlatform platform : movingPlatforms){
			platform.dispose();
		}
		
		water1.dispose();
		water2.dispose();
		
		for(Diamond diamond : diamonds){
			diamond.dispose();
		}
		
		for(Diamond diamond : redDiamonds){
			diamond.dispose();
		}
		
		for(CollectibleFish collectFish : collectibleFish){
			collectFish.dispose();
		}
		
		for(DogEnemy dog : dogEnemys){
			dog.dispose();					
		}
		
		for(Bird bird : birds){			
			bird.dispose();
		}
		
		for(Waypoint waypoint : waypoints){
			waypoint.dispose();
		}
		
		puzzlePiece.dispose();
		
	}
}
