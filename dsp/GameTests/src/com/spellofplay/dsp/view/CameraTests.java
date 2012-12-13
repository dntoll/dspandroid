package com.spellofplay.dsp.view;

import static org.junit.Assert.*;

import org.junit.Test;

import com.spellofplay.dsp.model.ModelPosition;

public class CameraTests {

	@Test
	public void testToViewPos() {
		Camera cam = new Camera();
		float scale = cam.getScale();
		
		
		ModelPosition input = new ModelPosition(7, 2);
		ViewPosition expected = new ViewPosition(cam.getDisplacement().m_x + 7 * scale, cam.getDisplacement().m_y + 2 *scale);
		
		ViewPosition actual = cam.toViewPos(input);
		assertEquals(expected.m_x, actual.m_x, 0);
		assertEquals(expected.m_y, actual.m_y, 0);
	}

	@Test
	public void testToViewScale() {
		Camera cam = new Camera();
		
		
		float input = 4.3f;
		float expected = cam.getScale()  * input;
		
		
		float actual = cam.toViewScale(input);
		
		assertEquals(expected, actual, 0);
	}

}
