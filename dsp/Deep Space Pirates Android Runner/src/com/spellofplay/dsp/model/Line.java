package com.spellofplay.dsp.model;

public class Line {
	
	Vector2 m_pos1;
	Vector2 m_pos2;
	float m_lineMag;

	public Line(Vector2 pos1, Vector2 pos2) {
		m_pos1 = pos1;
		m_pos2 = pos2;
		m_lineMag = (m_pos1.sub(m_pos2)).length();
		
				
	}

	public float distance(Vector2 position) {
		return pointToLineDistance(m_pos1, m_pos2, position);
	}
	
	private float pointToLineDistance(Vector2 A, Vector2 B, Vector2 P)
	{
		
		float u = ((P.m_x - A.m_x) * (B.m_x - A.m_x) + 
				   (P.m_y - A.m_y) * (B.m_y - A.m_y)) / ( m_lineMag * m_lineMag);
		
		if( u < 0.0f || u > 1.0f) {
			return Float.MAX_VALUE;
		}
		
		float ix = A.m_x + u * (B.m_x - A.m_x);
		float iy = A.m_y + u * (B.m_y - A.m_y);
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
