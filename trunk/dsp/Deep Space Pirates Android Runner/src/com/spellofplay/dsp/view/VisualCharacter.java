package com.spellofplay.dsp.view;

import android.graphics.Color;
import android.graphics.Rect;

import com.spellofplay.dsp.model.Enemy;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.Character;

public class VisualCharacter {
	private static final Rect SOLDIER = new Rect(0, 0, 255, 255);
	private static final Rect ENEMY = new Rect(0, 128, 32, 128 + 32);
	private static final float MOVEMENT_TIME = 0.2f;
	private static final float ROTATION_SPEED = 360.0f;
	private final int TRANSPARENT = Color.argb(128, 255, 255, 255);
	
	private float m_animationTimer = 0.0f;
	private float m_rotation = 0;
	private float m_targetRotation = 0;
		
	
	Character m_modelCharacter;
	ModelPosition m_lastPosition = new ModelPosition();
	
	public VisualCharacter(Character dude) {
		m_modelCharacter = dude;
		m_lastPosition.m_x = dude.getPosition().m_x;
		m_lastPosition.m_y = dude.getPosition().m_y;
	}
	
	void drawEnemy(AndroidDraw drawable, Camera camera, ITexture enemyTexture, boolean isSpotted, ModelPosition lastSeenPosition) {
		boolean drawGui = true;
		//Plats och färg för synliga fiender
		ViewPosition vEpos = getVisualPosition(camera);//camera.toViewPos(m_modelCharacter.getPosition());
		int color = Color.WHITE;
		
		//NOBODY CAN SEE THE ENEMY RIGHT NOW?
		if (isSpotted == false) {
			color =  TRANSPARENT;
			//LAST KNOWN POSITION
			vEpos = camera.toViewPos(lastSeenPosition); 
			drawGui = false;
		} 
		

		drawCharacter(vEpos, drawable, camera, enemyTexture, null, ENEMY, color, drawGui);
	}

	private void drawGUI(AndroidDraw drawable, Rect dst) {
		drawable.drawText("" + m_modelCharacter.getTimeUnits(), dst.left, dst.top, drawable.m_guiText);
		drawable.drawText("" + m_modelCharacter.getHitpoints(), dst.right-16, dst.top, drawable.m_guiText);
	}

	void drawSoldier(AndroidDraw drawable, Camera camera, ITexture player, Enemy target ) {
		
		ViewPosition vpos = getVisualPosition(camera);
		drawCharacter(vpos, drawable, camera, player, target != null ? target.getPosition() : null, SOLDIER, Color.WHITE, true);
	}
	
	private void drawCharacter(ViewPosition vpos, AndroidDraw drawable, Camera camera, ITexture player, ModelPosition targetPosition, Rect source, int color, boolean drawgui) {
		//Did we move?
		if (m_lastPosition.equals(m_modelCharacter.getPosition()) == false) {
			m_targetRotation = m_lastPosition.sub(m_modelCharacter.getPosition()).getRotation();
		} else if (targetPosition != null) {
			//Rotate to face target
			m_targetRotation = m_modelCharacter.getPosition().sub(targetPosition).getRotation();
		}
		//
		 
		
		Rect dst = new Rect((int)vpos.m_x -camera.getHalfScale(), 
				(int)vpos.m_y -camera.getHalfScale(),
				(int)vpos.m_x + camera.getHalfScale(), 
				(int)vpos.m_y + camera.getHalfScale());
		
		
		drawable.drawBitmap(player, source, dst, color, m_rotation);
		if (drawgui) {
			drawGUI(drawable, dst);
		}
	}

	public ViewPosition getVisualPosition(Camera camera) {
		ViewPosition vpos = null;//camera.toViewPos(m_modelCharacter.getPosition());
		
		if (m_animationTimer < MOVEMENT_TIME) {
			//interpolate
			float vmxpos = (float)m_lastPosition.m_x * (1.0f - m_animationTimer/MOVEMENT_TIME) +   
			               (float)m_modelCharacter.getPosition().m_x * m_animationTimer / MOVEMENT_TIME;
			float vmypos = (float)m_lastPosition.m_y * (1.0f - m_animationTimer/MOVEMENT_TIME) +   
            			   (float)m_modelCharacter.getPosition().m_y * m_animationTimer / MOVEMENT_TIME;
			
			vpos = camera.toViewPos(vmxpos, vmypos);
					
		} else {
			vpos = camera.toViewPos(m_modelCharacter.getPosition());
		}
		return vpos;
	}

	public boolean update(float a_elapsedTime) {
		
		//Normalize
		m_rotation = fixAngle(m_rotation);
		m_targetRotation = fixAngle(m_targetRotation);

		//Find direction and rotate
		float difference = getDifference(m_rotation, m_targetRotation);
		if (difference > ROTATION_SPEED * a_elapsedTime) {
			m_rotation += ROTATION_SPEED * a_elapsedTime;
		} else if (difference < -ROTATION_SPEED* a_elapsedTime) {
			m_rotation -= ROTATION_SPEED * a_elapsedTime;
		} else {
			m_rotation = m_targetRotation;
		}
		
		if (m_rotation != m_targetRotation)
			return false;
		
		m_animationTimer += a_elapsedTime;
		if (m_animationTimer >= MOVEMENT_TIME) {
			m_lastPosition = m_modelCharacter.getPosition();
			return true;
		}
		return false;
	}

	private float fixAngle(float a_rotation) {
		while (a_rotation > 360) {
			a_rotation -= 360;
		}
		while (a_rotation < 0) {
			a_rotation += 360;
		}
		
		return a_rotation;
	}

	private float getDifference(float a, float b) {
		float difference = b-a;
		
		if (difference > 180) {
			difference = - (360 - difference);
		}
		if (difference < -180) {
			difference = (360 + difference);
		}
		
		return difference;
	}

	public void moveTo() {
		m_animationTimer = 0.0f;
	}
}

