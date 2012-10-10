package com.spellofplay.dsp.view;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import android.graphics.PointF;

import com.spellofplay.dsp.model.IModel;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.Soldier;

public class GameViewTest {

	IModel mockedModel;
	IInput mockedInput;
	GameView sut;
	private ModelPosition farAway = new ModelPosition(18, 19);
	private ModelPosition soldierPos = new ModelPosition(2, 3);
	private ViewPosition  soldierViewPos = new ViewPosition(2 * GameView.m_scale, 3 * GameView.m_scale);
	private ViewPosition  destinationPosition = new ViewPosition(0, 1 * GameView.m_scale);
	private ViewPosition  farAwayPosition = new ViewPosition(18* GameView.m_scale, 19* GameView.m_scale);
	private ModelPosition destinationModelPosition = new ModelPosition(0, 1);
	
	
	
	Soldier expected = new Soldier(soldierPos);
	List<Soldier> arrayOfSoldiers = new ArrayList<Soldier>();
	
	@Before
	public void setUp()  {
		mockedModel = mock(IModel.class);
		mockedInput = mock(IInput.class);
		sut = new GameView(null);
		
		
		arrayOfSoldiers.add(expected);
		arrayOfSoldiers.add(new Soldier(farAway ));
	}
	
	@Test
	public void testGetSelectedSoldier() {
		
		
		when(mockedModel.getAliveSoldiers()).thenReturn(arrayOfSoldiers);
		when(mockedInput.IsMouseClicked()).thenReturn(true);
		when(mockedInput.getClickPosition()).thenReturn(soldierViewPos);
		sut.setupInput(mockedInput, mockedModel);
		Soldier actual = sut.getSelectedSoldier(mockedInput, mockedModel);
		
		assertSame(expected, actual);
	}
	
	@Test
	public void testGetSelectedSoldierToFarAway() {
		when(mockedModel.getAliveSoldiers()).thenReturn(arrayOfSoldiers);
		when(mockedInput.IsMouseClicked()).thenReturn(true);
		when(mockedInput.getClickPosition()).thenReturn(destinationPosition);
		sut.setupInput(mockedInput, mockedModel);
		Soldier actual = sut.getSelectedSoldier(mockedInput, mockedModel);
		
		assertNull(actual);

	}

	@Test
	public void testGetDestination() {
		when(mockedModel.getAliveSoldiers()).thenReturn(arrayOfSoldiers);
		when(mockedInput.IsMouseClicked()).thenReturn(true);
		when(mockedInput.getClickPosition()).thenReturn(destinationPosition);
		
		sut.setupInput(mockedInput, mockedModel);
		ModelPosition expected = destinationModelPosition;
		ModelPosition actual = sut.getDestination(mockedInput);
		
		assertEquals(expected.m_x, actual.m_x, 0);
		assertEquals(expected.m_y, actual.m_y, 0);
	}
	
	@Test
	public void testGetDestinationOnOtherSoldier() {
		when(mockedModel.getAliveSoldiers()).thenReturn(arrayOfSoldiers);
		when(mockedInput.IsMouseClicked()).thenReturn(true);
		when(mockedInput.getClickPosition()).thenReturn(farAwayPosition);
		
		sut.setupInput(mockedInput, mockedModel);
		ModelPosition actual = sut.getDestination(mockedInput);
		
		assertEquals(null, actual);
		
	}

}
