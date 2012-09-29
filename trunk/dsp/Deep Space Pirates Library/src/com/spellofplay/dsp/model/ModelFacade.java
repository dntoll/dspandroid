package com.spellofplay.dsp.model;

public class ModelFacade {
	
	Game m_game = new Game();
	Level m_level = new Level();
	
	public String getGameTitle() {
		return "Deep Space Pirates " + lastTime;
	}

	float lastTime=0;
	public void update(float elapsedTimeSeconds) {
		lastTime = 1.0f / elapsedTimeSeconds;
		
		m_game.update();
	}
	public Level getLevel() {

		return m_level;
	}
}
