package com.spellofplay.dsp.model;

public class ModelFacade {
	
	Game m_game = new Game();
	
	public String getGameTitle() {
		return "Deep Space Pirates " + lastTime;
	}

	float lastTime=0;
	public void update(float elapsedTimeSeconds) {
		lastTime = elapsedTimeSeconds;
		
		m_game.update();
	}
	public Level getLevel() {
		// TODO Auto-generated method stub
		return null;
	}
}
