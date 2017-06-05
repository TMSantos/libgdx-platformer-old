package com.me.catplaformer.controller.tmxToBox2d;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.me.catplaformer.utils.Constants;

public class TmxToBox2d {

	public final static String PHYSICS_ATTRIBUTE = "object";
	public final static String PHYSICS_POSITION_X_ATTRIBUTE = "x";
	public final static String PHYSICS_POSITION_Y_ATTRIBUTE = "y";
	public final static String PHYSICS_DIMENSION_WIDTH_ATTRIBUTE = "width";
	public final static String PHYSICS_DIMENSION_HEIGHT_ATTRIBUTE = "height";
	
	private final String PHYSICS_LAYER = "objectgroup";
	private final String PHYSICS_RECTANGLE_NAME = "Rectangles";
	private final String PHYSICS_LINE_GROUND = "Ground";
	private final String PHYSICS_LINE_NO_GROUND = "NoGround";
	private final String PHYSICS_POLY_LINE_ATTRIBUTE = "polyline";
	private final String PHYSICS_POLY_LINE_POINTS_ATTRIBUTE = "points";
	
	private final String PHYSICS_DIAMONDS_NAME = "Diamonds";
	private final String PHYSICS_FALLING_WAYPOINT_NAME = "Waypoints";
	private final String PHYSICS_COLLECTIBLE_FISH_NAME = "CollectibleFish";
	
	private static final String MAP_HEIGHT = "height";
	private static final String MAP_TILE_HEIGHT = "tileheight";
	
	private World mWorld;
	private String mTmxMap;
	private float mPixelPerMeter;
	
	public int mapHeight;

	private String mapNumberOfHeightTiles;
	private String mapTileHeight;
	
	private Array<Element> mPhysicsLayers;
	private Array<Element> mRectangleLayers;
	
	private Array<Element> mDiamondList;
	private Array<Element> mWaypoints;
	private Array<Element> mCollectibleFish;
	
	private Array<Element> mPolyLineGroundPoints;
	private Array<Element> mPolyLineNoGroundPoints;
	
	private Element root = null;
	
	public TmxToBox2d(String tmxmap,World world,float pixelPerMeter){
		mWorld = world;
		mTmxMap = tmxmap;
		mPixelPerMeter = pixelPerMeter;
		
		init();	
	}

	private void init() {
		XmlReader reader = new XmlReader();
		FileHandle fh = Gdx.files.internal(mTmxMap);
			
		try {
			root = reader.parse(fh);
		}catch (IOException e) {
			e.printStackTrace();
		}
	
		mapNumberOfHeightTiles = root.get(MAP_HEIGHT);
		mapTileHeight = root.get(MAP_TILE_HEIGHT);
		mapHeight = Integer.parseInt(mapNumberOfHeightTiles) * Integer.parseInt(mapTileHeight);
		
		mPhysicsLayers = root.getChildrenByName(PHYSICS_LAYER);
		
		
		for(Element item : mPhysicsLayers){			
			if(item.getAttribute("name").equals(PHYSICS_LINE_GROUND)){
				mPolyLineGroundPoints = item.getChildrenByName(PHYSICS_ATTRIBUTE);
			}else if(item.getAttribute("name").equals(PHYSICS_LINE_NO_GROUND)){
				mPolyLineNoGroundPoints = item.getChildrenByName(PHYSICS_ATTRIBUTE);
			}else if(item.getAttribute("name").equals(PHYSICS_RECTANGLE_NAME)){
				mRectangleLayers = item.getChildrenByName(PHYSICS_ATTRIBUTE);
			}else if(item.getAttribute("name").equals(PHYSICS_DIAMONDS_NAME)){
				mDiamondList = item.getChildrenByName(PHYSICS_ATTRIBUTE);
			}else if(item.getAttribute("name").equals(PHYSICS_FALLING_WAYPOINT_NAME)){
				mWaypoints = item.getChildrenByName(PHYSICS_ATTRIBUTE);
			}else if(item.getAttribute("name").equals(PHYSICS_COLLECTIBLE_FISH_NAME)){
				mCollectibleFish = item.getChildrenByName(PHYSICS_ATTRIBUTE);
			}
		}
	}
	
	public void loadLines(boolean ground,boolean noGround){
		
		if(!ground && !noGround){
			return;
		}
		
		if(ground && mPolyLineGroundPoints == null || noGround && mPolyLineNoGroundPoints == null){
			return;
		}
		
		ArrayList<TmxToBox2dPolyLine> lineShapesGround = null;
		ArrayList<TmxToBox2dPolyLine> lineShapesNoGround = null;
		
		
		lineShapesGround = getPolyLines(mPolyLineGroundPoints);
		
		lineShapesNoGround = getPolyLines(mPolyLineNoGroundPoints);
		
		
		if(ground){
			for(TmxToBox2dPolyLine line : lineShapesGround){
			
				for(int i = 0; i < line.getPolyPoints().length-1 ; i++){
					BodyDef groundBodyDef = new BodyDef();
					groundBodyDef.type = BodyDef.BodyType.StaticBody;
					Body groundBody = mWorld.createBody(groundBodyDef);
			
					EdgeShape environmentShape = new EdgeShape();				
				
					environmentShape.set((line.getPosition().x/mPixelPerMeter + (line.getPolyPoints()[i*1].x/mPixelPerMeter)),
							(mapHeight/mPixelPerMeter - (line.getPosition().y/mPixelPerMeter) - (line.getPolyPoints()[i*1].y/mPixelPerMeter)),
							((line.getPosition().x/mPixelPerMeter) + (line.getPolyPoints()[i+1].x/mPixelPerMeter)),
							(mapHeight/mPixelPerMeter - (line.getPosition().y/mPixelPerMeter) - (line.getPolyPoints()[i+1].y/mPixelPerMeter)));
					
					FixtureDef groundFixture = new FixtureDef();
					groundFixture.shape = environmentShape;
					groundFixture.density = 0f;
					groundFixture.friction = 0.5f;
					groundBody.createFixture(groundFixture);
					groundBody.getFixtureList().get(0).setUserData("ground");
					environmentShape.dispose();
					}
			}
		}
		
		if(noGround){
			for(TmxToBox2dPolyLine line : lineShapesNoGround){
				
				for(int i = 0; i < line.getPolyPoints().length-1 ; i++){
					BodyDef groundBodyDef = new BodyDef();
					groundBodyDef.type = BodyDef.BodyType.StaticBody;
					Body groundBody = mWorld.createBody(groundBodyDef);
			
					EdgeShape environmentShape = new EdgeShape();				
				
					environmentShape.set((line.getPosition().x/mPixelPerMeter + (line.getPolyPoints()[i*1].x/mPixelPerMeter)),
							(mapHeight/mPixelPerMeter - (line.getPosition().y/mPixelPerMeter) - (line.getPolyPoints()[i*1].y/mPixelPerMeter)),
							((line.getPosition().x/mPixelPerMeter) + (line.getPolyPoints()[i+1].x/mPixelPerMeter)),
							(mapHeight/mPixelPerMeter - (line.getPosition().y/mPixelPerMeter) - (line.getPolyPoints()[i+1].y/mPixelPerMeter)));
			
					FixtureDef fixture = new FixtureDef();
					fixture.friction = 0f;
					fixture.density = 0f;
					fixture.shape = environmentShape;
					groundBody.createFixture(fixture);
					//groundBody.createFixture(environmentShape, 0);
					environmentShape.dispose();
					}
		
			}	
		}
	}

	public void loadRectangles(){
		
		if(mRectangleLayers == null){
			return;
		}
		
		ArrayList<TmxToBox2dRectangle> rectangleShapes = getPhysicObjects(mRectangleLayers);
		
		for(TmxToBox2dRectangle rectangle : rectangleShapes){
			
			createBox(BodyType.StaticBody, 
					(float)rectangle.getWidth()/2, 
					(float)rectangle.getHeight()/2,
					1, 
					rectangle.getPosition().x + ((float)rectangle.getWidth()/2),
					(float)(mapHeight - (rectangle.getPosition().y + ((float)rectangle.getHeight()/2)))
					);
				

		}
	}
	
	private ArrayList<TmxToBox2dPolyLine> getPolyLines(Array<Element> polyLines) {
		ArrayList<TmxToBox2dPolyLine> mPolyLines = new ArrayList<TmxToBox2dPolyLine>();
		
		for(Element item : polyLines){
			TmxToBox2dPolyLine mPolyLine = new TmxToBox2dPolyLine();
			Vector2 polyLinePosition = new Vector2();
			polyLinePosition.x = Integer.parseInt(item.getAttribute(PHYSICS_POSITION_X_ATTRIBUTE));
			polyLinePosition.y = Integer.parseInt(item.getAttribute(PHYSICS_POSITION_Y_ATTRIBUTE));
			mPolyLine.setPosition(polyLinePosition);		
			String[] polyPointsCoordinates = item.getChildByName(PHYSICS_POLY_LINE_ATTRIBUTE).getAttribute(PHYSICS_POLY_LINE_POINTS_ATTRIBUTE).split("\\s");
			Vector2[] coordinates = new Vector2[polyPointsCoordinates.length];
			
			for(int i = 0 ; i < polyPointsCoordinates.length ; i++){
				String[] coordinate = polyPointsCoordinates[i].split(",");
				//one item from polyPointsCoordinates corresponds to one par of coordinates
				coordinates[i] = new Vector2();
				coordinates[i].x = Integer.parseInt(coordinate[0]);
				coordinates[i].y =  Integer.parseInt(coordinate[1]);			
			}
			
			mPolyLine.setPolyPoints(coordinates);
			mPolyLines.add(mPolyLine);
			
		}
		return mPolyLines;
	}

	private ArrayList<TmxToBox2dRectangle> getPhysicObjects(Array<Element> rectangleLayer) {
		
		ArrayList<TmxToBox2dRectangle> mRectangleObjects = new ArrayList<TmxToBox2dRectangle>();
		
		for(Element item : rectangleLayer){
			TmxToBox2dRectangle mRectangleObject = new TmxToBox2dRectangle();
			Vector2 rectanglePosition = new Vector2();
			rectanglePosition.x = Integer.parseInt(item.getAttribute(PHYSICS_POSITION_X_ATTRIBUTE));
			rectanglePosition.y = Integer.parseInt(item.getAttribute(PHYSICS_POSITION_Y_ATTRIBUTE));
			mRectangleObject.setPosition(rectanglePosition);
			mRectangleObject.setWidth(Integer.parseInt(item.getAttribute(PHYSICS_DIMENSION_WIDTH_ATTRIBUTE)));
			mRectangleObject.setHeight(Integer.parseInt(item.getAttribute(PHYSICS_DIMENSION_HEIGHT_ATTRIBUTE)));
			
			mRectangleObjects.add(mRectangleObject);
		}
		return mRectangleObjects;
	}
	
	public LevelMapInformation getLevelMapCummomPhysics(){
		LevelMapInformation level = new LevelMapInformation();

		level.mapCollectibleFish =  getCollectibleFish();
		level.mapDiamonds = getDiamonds();
		level.mapWaypoints = getMapWaypoints();
		
		return level;
	}
	
	public Array<MapObject> getCollectibleFish(){
		if(mCollectibleFish == null) return null;
		Array<MapObject> collectibleFishs = new Array<MapObject>();
		
		for(Element item : mCollectibleFish){
			MapObject fish = new MapObject();
			fish.positionX = (float)((Integer.parseInt(item.getAttribute(TmxToBox2d.PHYSICS_POSITION_X_ATTRIBUTE))/mPixelPerMeter));
			fish.positionY = (float) ((Constants.VIEWPORT_HEIGHT) - (Integer.parseInt(item.getAttribute(TmxToBox2d.PHYSICS_POSITION_Y_ATTRIBUTE))/mPixelPerMeter));
		
			collectibleFishs.add(fish);
		}
		return collectibleFishs;
	}
	
	public Array<MapObject> getDiamonds(){
		if(mDiamondList == null) return null;
		
		Array<MapObject> mDiamonds = new Array<MapObject>();
		
		for(Element item : mDiamondList){
			MapObject diamond = new MapObject();
			diamond.positionX = (float)(Integer.parseInt(item.getAttribute(TmxToBox2d.PHYSICS_POSITION_X_ATTRIBUTE))/mPixelPerMeter);
			diamond.positionY = (float) (Constants.VIEWPORT_HEIGHT) - (Integer.parseInt(item.getAttribute(TmxToBox2d.PHYSICS_POSITION_Y_ATTRIBUTE))/mPixelPerMeter);
			
			mDiamonds.add(diamond);
		}
		return mDiamonds;
	}
	
	public Array<MapObject> getMapWaypoints(){
		if(mWaypoints == null) return null;
		Array<MapObject> waypoints = new Array<MapObject>();
		
		for(Element item : mWaypoints){
			MapObject wp = new MapObject();
			wp.positionX = (float)((Integer.parseInt(item.getAttribute(TmxToBox2d.PHYSICS_POSITION_X_ATTRIBUTE))/mPixelPerMeter));
			wp.positionY = (float) ((Constants.VIEWPORT_HEIGHT) - (Integer.parseInt(item.getAttribute(TmxToBox2d.PHYSICS_POSITION_Y_ATTRIBUTE))/mPixelPerMeter));
		
			waypoints.add(wp);
		}
		return waypoints;
	}
	
	private void createBox(BodyType type, float width, float height, float density,float x,float y) {
		
		BodyDef def = new BodyDef();
		def.type = type;
		def.position.set(x/mPixelPerMeter, y/mPixelPerMeter);
		Body box = mWorld.createBody(def);
 
		PolygonShape poly = new PolygonShape();
		poly.setAsBox(width/mPixelPerMeter, height/mPixelPerMeter);
		box.createFixture(poly, density);
		box.getFixtureList().get(0).setUserData("rectangle");
		poly.dispose();
	
	}
}
