package com.spellofplay.dsp.model;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

public class PointTest extends TestCase {

	
	public void testSub() {
		ModelPosition sut = new ModelPosition(2, 3);
		
		ModelPosition other = new ModelPosition(3, -5);
		
		Vector2 actual = sut.sub(other);
		
		Assert.assertEquals(-1, actual.x, 0.01f);
		Assert.assertEquals(8, actual.y, 0.01f);
	}
	
	
	
	public void testLength() {
		Vector2 sut = new Vector2(2, 3);
		
		float actual = sut.length();
		
		float expected = (float) Math.sqrt(2.0f * 2.0f + 3.0f * 3.0f);
		
		Assert.assertEquals(expected, actual, 0.01f );
	}

}
