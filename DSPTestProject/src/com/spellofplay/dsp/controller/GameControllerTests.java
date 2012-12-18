package com.spellofplay.dsp.controller;

import junit.framework.TestCase;

public class GameControllerTests extends TestCase {

	/*com.spellofplay.dsp.model.ICharacter selectedSoldier;
	ModelPosition destination = new ModelPosition();
	IModel mockedModel;
	GameView mockedView;
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
*/
}
