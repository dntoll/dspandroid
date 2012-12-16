package com.spellofplay.dsp.model.inner;

import com.spellofplay.dsp.model.Preferences;

import junit.framework.TestCase;


public class GameTests extends TestCase {

	public void testLoad() throws Exception {
		Game pre = new Game();
		pre.startLevel(5);
		IPersistance persistance = new PersistanceStub();
		CharacterCollection<Enemy> expectedEnemies = pre.getAliveEnemies();
		pre.Save(persistance);
		
		
		
		Game sut = new Game();
		sut.Load(persistance);

		assertEquals(pre.getCurrentLevel(), sut.getCurrentLevel());
		
		CharacterCollection<Enemy> actualEnemies = sut.getAliveEnemies();
		
		assertTrue(expectedEnemies.equals(actualEnemies));
		assertTrue(pre.getAliveSoldiers().equals(sut.getAliveSoldiers()));
		
		
		compareLevel(pre, sut);
	}
	
	public void testLoadWhenNoProfileExists() {
		IPersistance persistance = new PersistanceStub();
		
		Game sut = new Game();
		try {
			sut.Load(persistance);
			fail();
		} catch (Exception e) {
			//ok
		}
	}

	

	private void compareLevel(Game pre, Game sut) {
		for (int x = 0; x < Preferences.WIDTH; x++) {
			for (int y = 0; y < Preferences.HEIGHT; y++) {
				assertEquals(pre.getTile(x, y), sut.getTile(x, y));		
			}
		}
	}



	

}
