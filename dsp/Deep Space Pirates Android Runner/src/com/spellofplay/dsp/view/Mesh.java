package com.spellofplay.dsp.view;

import android.graphics.Canvas.VertexMode;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;

public class Mesh {

	
	public VertexMode m_mode = VertexMode.TRIANGLES;
	
	private int m_vertices;// = 3 * Map.Width * Map.Height * 2  * 2;
	
	public float[] m_verts;// = new float[2 * m_vertices];
	public float[] m_texs;// = new float[2 * m_vertices];
	public int[] m_colors;// = new int[m_vertices];
	public short[] m_indices;// = new short[m_vertices];
	
	public Mesh(int width, int height) {
		m_vertices = 3 * width * height * 2  * 2;
		m_verts = new float[2 * m_vertices];
		m_texs = new float[2 * m_vertices];
		m_colors = new int[m_vertices];
		m_indices = new short[m_vertices];
		
		clear();
	}
	
	PointF[] m_texcoords = new PointF[4];
	
	private int m_numTriangles = 0;

	public void clear() {
		
		m_numTriangles = 0;
		
		for (int i = 0; i < m_vertices; i++) {
			m_colors[i] = Color.WHITE;
			m_indices[i] = (short)i;
		}
		
		for (int i = 0; i< 4; i++) {
			m_texcoords[i] = new PointF();
		}
	}

	public void AddRectangle(Rect src, Rect dst, Rotation rotation) {
		//Triangle 1
		//0, 1
		//2,
		PointF coords[] = GetRotatedCoords(src, rotation); 
		 
		
		
		int triangleOffset = m_numTriangles * 3 * 2;
		m_verts[triangleOffset]     = dst.left;
		m_verts[triangleOffset + 1] = dst.top;
		
		m_verts[triangleOffset + 2] = dst.right;
		m_verts[triangleOffset + 3] = dst.top;
		
		m_verts[triangleOffset + 4] = dst.left;
		m_verts[triangleOffset + 5] = dst.bottom;
		
		m_texs[triangleOffset] 		= coords[0].x;
		m_texs[triangleOffset + 1]  = coords[0].y;
		
		m_texs[triangleOffset + 2]  = coords[1].x;
		m_texs[triangleOffset + 3]  = coords[1].y;
		
		m_texs[triangleOffset + 4]  = coords[2].x;
		m_texs[triangleOffset + 5]  = coords[2].y;
		
		m_numTriangles++;
		
		//Triangle2
		// , 1
		//2, 3
		triangleOffset = m_numTriangles * 2 * 3;
		m_verts[triangleOffset] 	= dst.left;
		m_verts[triangleOffset + 1] = dst.bottom;
		
		m_verts[triangleOffset + 2] = dst.right;
		m_verts[triangleOffset + 3] = dst.top;
		
		m_verts[triangleOffset + 4] = dst.right;
		m_verts[triangleOffset + 5] = dst.bottom;
		
		m_texs[triangleOffset] 		= coords[2].x;
		m_texs[triangleOffset + 1]  = coords[2].y;
		
		m_texs[triangleOffset + 2]  = coords[1].x;
		m_texs[triangleOffset + 3]  = coords[1].y;
		
		m_texs[triangleOffset + 4]  = coords[3].x;
		m_texs[triangleOffset + 5]  = coords[3].y;
		
		m_numTriangles++;
		
	}

	
	private PointF[] GetRotatedCoords(Rect src, Rotation rotation) {
		switch (rotation) {
			case NONE : 
				m_texcoords[0].set(src.left, src.top);
				m_texcoords[1].set(src.right, src.top);
				m_texcoords[2].set(src.left, src.bottom);
				m_texcoords[3].set(src.right, src.bottom);
				break;
			case NINTY : 
				m_texcoords[0].set(src.right, src.top);
				m_texcoords[1].set(src.right, src.bottom);
				m_texcoords[2].set(src.left, src.top);
				m_texcoords[3].set(src.left, src.bottom);
				break;
			case HUNDREDEIGHTY : 
				m_texcoords[0].set(src.right, src.bottom);
				m_texcoords[1].set(src.left, src.bottom);
				m_texcoords[2].set(src.right, src.top);
				m_texcoords[3].set(src.left, src.top);
				break;
			case TWOSEVENTY : 
				m_texcoords[0].set(src.left, src.bottom);
				m_texcoords[1].set(src.left, src.top);
				m_texcoords[2].set(src.right, src.bottom);
				m_texcoords[3].set(src.right, src.top);
				break;
				
		}
		return m_texcoords;
	}

	public int GetVerticeCount() {
		
		return m_numTriangles * 3 * 2;
	}

	

	public int GetIndexCount() {
		// TODO Auto-generated method stub
		return m_numTriangles * 3;
	} 

}
