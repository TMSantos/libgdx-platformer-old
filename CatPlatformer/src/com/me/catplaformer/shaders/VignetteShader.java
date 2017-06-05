package com.me.catplaformer.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class VignetteShader {

	private ShaderProgram vignetteShader;
	
	public VignetteShader(){
		vignetteShader = new ShaderProgram(Gdx.files.internal("graphics/vignette.vsh"), Gdx.files.internal("graphics/vignette.fsh"));
	}
	
	public void render(){
		vignetteShader.begin();
		vignetteShader.setUniformf("u_resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		float transition = 0.7f;		
		
		vignetteShader.setUniformf("tint", 1.0f, 0.7f + transition * 0.3f, 0.7f + transition * 0.3f, 1.0f);
		vignetteShader.setUniformf("innerRadius", 0.02f);
		vignetteShader.setUniformf("outerRadius", 0.4f + transition * 0.5f);
		vignetteShader.setUniformf("intensity", 0.99f);
		vignetteShader.setUniformf("noise", 1.0f - transition);
		vignetteShader.end();
	}
	
	public ShaderProgram getVignetteShader() {
		return vignetteShader;
	}
}
