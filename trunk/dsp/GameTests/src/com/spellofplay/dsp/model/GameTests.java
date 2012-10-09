package com.spellofplay.dsp.model;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


public class GameTests {
	
	Game sut;
	
	@Before
	public void setUp() {
		sut = new Game();
		sut.m_soldiers[0] = new Soldier(null);
		sut.m_soldiers[1] = new Soldier(null);
		sut.m_soldiers[2] = new Soldier(null);
	}
	
	
	@Test
	public void testGetAliveSoldiers() {
		Assert.assertEquals(3, sut.getAliveSoldiers().size());
	}
	
	
	@Test
	public void testStartNewGame() {
		
		Level l = new Level();
		l.loadLevel(0);
		sut.startLevel(0);
		
		Assert.assertEquals(sut.m_soldiers[0].m_position, l.getStartLocation(0));
	}
}
