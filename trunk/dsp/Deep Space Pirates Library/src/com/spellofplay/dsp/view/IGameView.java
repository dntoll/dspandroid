package com.spellofplay.dsp.view;

import com.spellofplay.dsp.model.IModel;
import com.spellofplay.dsp.model.ModelPosition;
import com.spellofplay.dsp.model.Soldier;

public interface IGameView {

	public abstract void drawGame(IDraw drawable, IModel a_model);

	public abstract Soldier getSelectedSoldier(IInput a_input, IModel a_model);

	

	public abstract ModelPosition getDestination(IInput a_input);

	public abstract void setupInput(IInput a_input, IModel a_model);

}