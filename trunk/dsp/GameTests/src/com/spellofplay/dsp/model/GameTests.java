package com.spellofplay.dsp.model;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.spellofplay.dsp.model.inner.Game;


public class GameTests {
	
	Game sut;
	
	@Before
	public void setUp() {
		sut = new Game();
		
	}
	
	
	@Test
	public void testGetAliveSoldiers() {
		Assert.assertEquals(3, sut.getAliveSoldiers().size());
	}
	
	
	@Test
	public void testStartNewGame() {
		
		sut.startLevel(0);
		
		//Assert.assertEquals(sut.m_soldiers[0].m_position, l.getStartLocation(0));
	}
}
