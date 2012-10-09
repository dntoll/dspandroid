package com.spellofplay.dsp.view;

import static org.junit.Assert.*;

import org.junit.Test;

import android.graphics.PointF;

import com.spellofplay.dsp.model.ModelPosition;

public class CameraTests {

	@Test
	public void testToViewPos() {
		Camera cam = new Camera();
		cam.m_scale = 2;
		cam.m_displacement = new ViewPosition(16, 16);
		
		ModelPosition input = new ModelPosition(7, 2);
		ViewPosition expected = new ViewPosition(16 + 7 * 2, 16 + 2 *2);
		
		ViewPosition actual = cam.toViewPos(input);
		assertEquals(expected.m_x, actual.m_x, 0);
		assertEquals(expected.m_y, actual.m_y, 0);
	}

	@Test
	public void testToViewScale() {
		Camera cam = new Camera();
		cam.m_scale = 2.33f;
		
		float input = 4.3f;
		float expected = cam.m_scale  * input;;
		
		
		float actual = cam.toViewScale(input);
		
		assertEquals(expected, actual, 0);
	}

}
