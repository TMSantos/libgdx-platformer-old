package com.me.catplaformer.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.me.catplaformer.utils.Constants;

public class AssetFonts {

	public BitmapFont defaultSmall;
	public BitmapFont defaultNormal;
	public BitmapFont defaultBig;
	
	public AssetFonts(){
		defaultSmall = new BitmapFont(Gdx.files.internal(Constants.CAT_FONT), true);
		defaultNormal = new BitmapFont(Gdx.files.internal(Constants.CAT_FONT), true);
		defaultBig = new BitmapFont(Gdx.files.internal(Constants.CAT_FONT), true);
		
		defaultSmall.setScale(0.75f);
		defaultNormal.setScale(1.0f);
		defaultBig.setScale(1.5f);
		
		defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		defaultBig.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	public void dispose(){
		defaultSmall.dispose();
		defaultNormal.dispose();
		defaultBig.dispose();
	}
}
