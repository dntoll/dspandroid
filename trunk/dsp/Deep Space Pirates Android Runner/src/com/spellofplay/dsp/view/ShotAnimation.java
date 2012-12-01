package com.spellofplay.dsp.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.graphics.Color;
import com.spellofplay.dsp.model.Vector2;

public class ShotAnimation {

	class Shot {

		Vector2 attackerModelPos;
		Vector2 fireTargetModelPos;
		boolean hit;
		static final float shotspeed = 20.0f;
		static final float blastAnimationTime = 0.3f;
		float time = 0;
		private float x;
		private float y;
		
		
		public Shot(Vector2 attacker, Vector2 fireTarget, boolean hit) {
			this.attackerModelPos = moveShotToOutsideAttacker(attacker, fireTarget);
			
			
			this.fireTargetModelPos = fireTarget;
			this.hit = hit;
			
			if (hit == false)
				this.fireTargetModelPos = displaceTarget(fireTarget);
			
		}


		public void update(float a_elapsedTime) {
			time += a_elapsedTime;
		}
		
		public boolean isActive() {
			return time < blastAnimationTime + timeToTravel();
		}
		
		private float timeToTravel() {
			return getDistance() / shotspeed;
		}


		private float getDistance() {
			return attackerModelPos.sub(fireTargetModelPos).length();
		}


		public void draw(AndroidDraw drawable, Camera cam) {
			
			ViewPosition fireTargetPos = cam.toViewPos(fireTargetModelPos);
			ViewPosition attackerPos = cam.toViewPos(attackerModelPos);
			
			Vector2 direction = fireTargetModelPos.sub(attackerModelPos);
			direction.normalize();
			
			int Trace = Color.argb(64, 255, 0, 0);
			 
			if (time < timeToTravel()) {
				drawShotInAir(drawable, cam, fireTargetPos, attackerPos,
						direction, Trace);
			} else {
				
				float blastTime = time - timeToTravel();
				
				float percent = blastTime / blastAnimationTime;
				int blastSize = (int)(percent * 0.3f * cam.getScale());
				drawable.drawCircle(fireTargetPos, blastSize, Trace);
			}
			
		}


		public void drawShotInAir(AndroidDraw drawable, Camera cam,
				ViewPosition fireTargetPos, ViewPosition attackerPos,
				Vector2 direction, int Trace) {
			float percent = time / timeToTravel();
			float shotLen = 0.5f;
			
			Vector2 from = attackerModelPos.add(direction.mul((getDistance()-shotLen) * percent));
			Vector2 to = from.add(direction.mul(shotLen));
			
			ViewPosition viewFrom = cam.toViewPos(from);
			ViewPosition viewTo = cam.toViewPos(to);
			drawable.drawLine(viewFrom, viewTo, Trace);
			drawable.drawCircle(viewTo, 5, Trace);
			
			drawable.drawLine(attackerPos, fireTargetPos, Trace);
		}


		


		private Vector2 moveShotToOutsideAttacker(Vector2 attackerPos, Vector2 fireTargetPos) {
			Vector2 dir = fireTargetPos.sub(attackerPos);
			dir.normalize();
			
			attackerPos = attackerPos.add(dir.mul(0.5f));
			return attackerPos;
		}


		private Vector2 displaceTarget(Vector2 fireTargetPos) {
			Random rand = new Random();
			this.x = rand.nextInt(1000) - 500;
			this.y = rand.nextInt(1000) - 500;
			
			Vector2 displacement = new Vector2(x, y);
			displacement.normalize();
			displacement = displacement.mul(0.5f);
			//fireTargetPos = fireTargetPos.sub(displacement);
			return fireTargetPos.add(displacement);
		}
		
	}
	
	List<Shot> activeShots = new ArrayList<Shot>();
	
	
	
	public void draw(AndroidDraw drawable, Camera cam) {
		for(Shot s : activeShots) {
			if (s.isActive()) {
				s.draw(drawable, cam);
			}
		}
	}

	public void addHit(Vector2 attacker, Vector2 fireTarget) {
		
		/*Vector2 attackerPos = attacker.getVisualPosition(cam).toVector2();
		Vector2 fireTargetPos = fireTarget.getVisualPosition(cam).toVector2();
		attackerPos = moveShotToOutsideAttacker(cam, attackerPos, fireTargetPos);
		
		if (hit == false) {
			fireTargetPos = displaceTarget(cam, fireTargetPos);
		}*/
		
		activeShots.add(new Shot(attacker, fireTarget, true) );
	}

	public void addMiss(Vector2 attacker, Vector2 fireTarget) {
		activeShots.add(new Shot(attacker, fireTarget, false) );
	}

	public void update(float a_elapsedTime) {
		
		for(Shot s : activeShots) {
			s.update(a_elapsedTime);
		}
	}
	
	public boolean isActive() {
		for(Shot s : activeShots) {
			if (s.isActive()) {
				return true;
			}
		}
		
		activeShots.clear();
		return false;
	}

}
