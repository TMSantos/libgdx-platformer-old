package com.me.catplaformer.utils;

/**
 * Created by Tiago Santos on 05/06/2013.
 */
public class Constants {
	
	//Screen constants
	public static float SCREEN_NATIVE_HUD;
	public static float SCREEN_NATIVE_HUD_HEIGHT;
	
	public static float VIEWPORT_GUI_WIDTH;
	public static float VIEWPORT_GUI_HEIGHT;
	
	public static float CAMERA_POSITION;
	
	public static float VIEWPORT_WIDTH = 16f;
	public static final float VIEWPORT_HEIGHT = 10.0f;

	//Texture Atlas directories
	public static String TEXTURE_MAIN_MENU_ATLAS;
	public static final String TEXTURE_WORLD_SELECTOR_SCREEN_ATLAS = "WorldSelectorScreen/worldselectorscreen-fhd.atlas";
	public static final String TEXTURE_LEVEL_SELECTOR_ATLAS = "LevelSelectorScreen/levelselectorscreen-fhd.atlas" ;
	public static final String TEXTURE_DEAD_MENU_ATLAS = "DeadMenu/deadmenu.atlas";
	public static String TEXTURE_PAUSE_MENU_ATLAS;
	public static final String TEXTURE_SOUND_MENU_ATLAS = "SoundMenu/soundmenu.atlas";;
	public static String TEXTURE_OPTIONS_MENU_ATLAS;
	public static final String TEXTURE_HUD_CONTROLS_ATLAS = "HUD/Controls/hud-controls.atlas";
	public static final String TEXTURE_ABOUT_MENU_ATLAS = "AboutScreen/aboutscreen-fhd.atlas";
	public static final String TEXTURE_LOADING = "LoadingScreen/loading-fhd.atlas";

	
	//LEVEL MAPS
	public static final String LEVEL_01 = "levels/world1/level1/map/level1map.tmx";
	public static final String LEVEL_02 = "levels/world1/level2/map/level2map.tmx";
	public static final String LEVEL_03 = "levels/world1/level3/map/level3map.tmx";
	public static final String LEVEL_04 = "levels/world1/level4/map/level4map.tmx";
	public static final String LEVEL_05 = "levels/world1/level5/map/level5map.tmx";
	public static final String LEVEL_06 = "levels/world2/level6/map/level6map.tmx";
	public static final String LEVEL_07 = "levels/world2/level7/map/level7map.tmx";
	public static final String LEVEL_08 = "levels/world2/level8/map/level8map.tmx";
	public static final String LEVEL_09 = "levels/world2/level9/map/level9map.tmx";
	public static final String LEVEL_10 = "levels/world2/level10/map/level10map.tmx";
	public static final String LEVEL_11 = "levels/world3/level11/map/level11map.tmx";
	public static final String LEVEL_12 = "levels/world3/level12/map/level12map.tmx";
	public static final String LEVEL_13 = "levels/world3/level13/map/level13map.tmx";
	public static final String LEVEL_14 = "levels/world3/level14/map/level14map.tmx";
	public static final String LEVEL_15 = "levels/world3/level15/map/level15map.tmx";
	public static final String LEVEL_16 = "levels/world4/level16/map/level16map.tmx";
	public static final String LEVEL_17 = "levels/world4/level17/map/level17map.tmx";
	public static final String LEVEL_18 = "levels/world4/level18/map/level18map.tmx";
	public static final String LEVEL_19 = "levels/world4/level19/map/level19map.tmx";
	public static final String LEVEL_20 = "levels/world4/level20/map/level20map.tmx";
	
	
	//LEVEL commom
	public static final String LEVEL_COMMOM_OBJECTS = "levels/levelCommom/levelCommom.atlas";
	public static final String LEVEL_WAYPOINTS = "levels/levelCommom/waypoints.atlas";
	public static final String LEVEL_1_2_OBJECTS = "levels/world1/level1and2/level1and2.atlas";
	public static final String CAT_ATLAS = "levels/catmale.atlas";
	
	//LEVEL 1 Objects
	public static final String LEVEL1_OBJECTS = "levels/world1/level1/objects/level1objects.atlas";
	
	//LEVEL 2 Objects
	public static final String LEVEL2_OBJECTS1 = "levels/world1/level2/objects/level2objects.atlas";
	
	//Skins directories
	public static final String SKIN_HUD_CONTROLS = "HUD/Controls/hud-controls.json";
	public static final String SKIN_MAINMENU = "MainMenuScreen/mainmenu.json";
	public static final String SKIN_WORLD_SELECTOR = "WorldSelectorScreen/worldselectorscreen.json";
	public static final String SKIN_LEVEL_SELECTOR = "LevelSelectorScreen/levelselectorscreen.json";
	public static final String SKIN_DEAD_MENU = "DeadMenu/deadmenu.json";
	public static final String SKIN_PAUSE_MENU = "PauseMenu/pausemenu.json";
	public static final String SKIN_SOUND_MENU = "SoundMenu/soundmenu.json";
	public static final String SKIN_OPTIONS_MENU = "OptionsScreen/optionsscreen.json";
	public static final String SKIN_ABOUT_MENU = "AboutScreen/aboutscreen.json";
	
	//FONTS
	public static final String CAT_FONT = "Fonts/kittenrush.fnt";
	
	//Others
	public static final String GAME_PROGRESS = "gameprogress.prefs";
	public static boolean isDesktop;
	public static DISPLAY_RES DISPLAY_RESOLUTION;
	
	public static enum DISPLAY_RES {
		FHD, HD,LD
	}
		
}
