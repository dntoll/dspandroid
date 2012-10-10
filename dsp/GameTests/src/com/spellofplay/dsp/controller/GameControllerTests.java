package com.spellofplay.dsp.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.spellofplay.dsp.model.IModel;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.Soldier;
import com.spellofplay.dsp.view.IGameView;
import com.spellofplay.dsp.view.IInput;

public class GameControllerTests {

	Soldier selectedSoldier = new Soldier(new ModelPosition(0,0));
	ModelPosition destination = new ModelPosition();
	IModel mockedModel;
	IGameView mockedView;
	IInput input;
	GameController sut;
	
	@Before
	public void setUp()  {
		mockedModel = mock(IModel.class);
		mockedView = mock(IGameView.class);
		sut = new GameController();
	}
	
	@Test
	public void testUpdatesPlayerIfHasUnfinishedActions() {
		when(mockedModel.hasUnfinishedActions()).thenReturn(true);
		when(mockedModel.isEnemyTime()).thenReturn(false);
		
		sut.update(null, mockedModel, mockedView, null, 0.0f);
		
		verify(mockedModel).updatePlayers();
	}
	
	@Test
	public void testUpdatesEnemiesIfItsTheirTurn() {
		when(mockedModel.isEnemyTime()).thenReturn(true);
		
		sut.update(null, mockedModel, mockedView, null, 0.0f);
		
		verify(mockedModel).updateEnemies();
	}
	
	@Test
	public void testSoldierGetsNewDestination() {
		when(mockedModel.isEnemyTime()).thenReturn(false);
		when(mockedView.getSelectedSoldier(input, mockedModel)).thenReturn(selectedSoldier);
		when(mockedView.getDestination(input)).thenReturn(destination);
		
		sut.update(null, mockedModel, mockedView, null, 0.0f);
		
		verify(mockedModel).doMoveTo(selectedSoldier, destination);
	}
	
	@Test
	public void testSoldierGetsNoDestination() {
		
		when(mockedModel.isEnemyTime()).thenReturn(false);
		when(mockedView.getSelectedSoldier(input, mockedModel)).thenReturn(selectedSoldier);
		when(mockedView.getDestination(input)).thenReturn(null);
		
		sut.update(null, mockedModel, mockedView, null, 0.0f);
		
		verify(mockedModel, never()).doMoveTo(selectedSoldier, null);
	}
	
	@Test
	public void testNoSelectedSoldierGetsNoDestination() {
		when(mockedModel.isEnemyTime()).thenReturn(false);
		when(mockedView.getSelectedSoldier(input, mockedModel)).thenReturn(null);
		
		sut.update(null, mockedModel, mockedView, null, 0.0f);
		
		verify(mockedView, never()).getDestination(input);
	}

	@Test
	public void testDrawGame() {
		fail("Not yet implemented");
	}

}
