package com.spellofplay.dsp.view;

import junit.framework.TestCase;

import com.spellofplay.dsp.model.IModel;
import com.spellofplay.dsp.model.inner.ModelFacade;
import com.spellofplay.dsp.model.inner.PersistanceStub;


public class VisibilityViewTests extends TestCase {

	public void testLoad() {
		VisibilityView pre = new VisibilityView();
		PersistanceStub persistence = new PersistanceStub();
		IModel modelStub = new ModelFacade();
		pre.recalculateVisibility(modelStub);
		while (pre.doneUpdating() == false) {
			pre.updateVisibility(modelStub);
		}
		pre.Save(persistence);
		
		
		VisibilityView sut = new VisibilityView();
		sut.Load(persistence);
		
		boolean isSame = sut.hasSameExploredVisibility(pre);
		assertFalse(isSame);
		
		isSame = pre.hasSameExploredVisibility(pre);
		assertTrue(isSame);
		
	}
	
	
	public void testVisibility() {
		assertTrue(VisibilityView.Visibility.NeverSeen.hasSameExploredVisibility( VisibilityView.Visibility.NeverSeen));
		assertFalse(VisibilityView.Visibility.NotSeen.hasSameExploredVisibility( VisibilityView.Visibility.NeverSeen));
		assertFalse(VisibilityView.Visibility.NeverSeen.hasSameExploredVisibility( VisibilityView.Visibility.SeenByAll));
		assertFalse(VisibilityView.Visibility.SeenByAll.hasSameExploredVisibility( VisibilityView.Visibility.NotSeen));
	}
	
	
}
