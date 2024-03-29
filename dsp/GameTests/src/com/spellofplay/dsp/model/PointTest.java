package com.spellofplay.dsp.model;

import junit.framework.Assert;

import org.junit.Test;

public class PointTest {

	@Test
	public void testSub() {
		ModelPosition sut = new ModelPosition(2, 3);
		
		ModelPosition other = new ModelPosition(3, -5);
		
		Vector2 actual = sut.sub(other);
		
		Assert.assertEquals(-1, actual.x);
		Assert.assertEquals(8, actual.y);
	}
	
	
	@Test
	public void testLength() {
		Vector2 sut = new Vector2(2, 3);
		
		float actual = sut.length();
		
		float expected = (float) Math.sqrt(2 * 2 + 3 * 3);
		
		Assert.assertEquals(expected, actual );
	}

}
