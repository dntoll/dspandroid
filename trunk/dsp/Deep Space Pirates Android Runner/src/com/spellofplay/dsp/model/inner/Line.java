package com.spellofplay.dsp.model.inner;

import com.spellofplay.dsp.model.Vector2;

class Line {
	
	private Vector2 m_pos1;
	private Vector2 m_pos2;
	private float m_lineMag;

	public Line(Vector2 pos1, Vector2 pos2) {
		m_pos1 = pos1;
		m_pos2 = pos2;
		m_lineMag = (m_pos1.sub(m_pos2)).length();
		
				
	}

	float distance(Vector2 position) {
		return pointToLineDistance(m_pos1, m_pos2, position);
	}
	
	private float pointToLineDistance(Vector2 A, Vector2 B, Vector2 P)
	{
		
		float u = ((P.x - A.x) * (B.x - A.x) + 
				   (P.y - A.y) * (B.y - A.y)) / ( m_lineMag * m_lineMag);
		
		if( u < 0.0f || u > 1.0f) {
			return Float.MAX_VALUE;
		}
		
		float ix = A.x + u * (B.x - A.x);
		float iy = A.y + u * (B.y - A.y);
		Vector2 intersect = new Vector2(ix, iy);
	
		
		return P.sub(intersect).length();
	}
	
	
	public static boolean test() {
		Line l = new Line(new Vector2(4, 4), new Vector2(4, 6));
		if (l.distance(new Vector2(4, 5)) != 0) {
			
			return false;
		}
		
		if (l.distance(new Vector2(3, 5)) != 1.0f) {
			return false;
		}
		
		if (l.distance(new Vector2(5, 5)) != 1.0f)
		{
			return false;
		}
		
		
		
		return true;
	}

}
