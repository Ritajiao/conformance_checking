package object;

import java.util.ArrayList;

//记录弧的源点、终点和方向
public class PNArc {
	private boolean PT = true;
	private boolean TP = false;

	private boolean direction;
	
	private PNNode source;
	private PNNode target;

	//该弧的转折点,
	private ArrayList<TurnPoint> turnPoints;

	public ArrayList<TurnPoint> getTurnPoints() {
		return turnPoints;
	}

	public void setTurnPoints(ArrayList<TurnPoint> turnPoints) {
		this.turnPoints = turnPoints;
	}

	/**
	 * Constructor creates an edge from the given place to the given transition.
	 * @param p the place to be connected to this arc
	 * @param t the transition to be connected to this arc
	 */
	public PNArc(Place p, Transition t) {
		source=p;
		target=t;
		direction = PT;
	}
	
	/**
	 * Constructor creates an edge from the given transition to the given place.
	 * @param t the transition to be connected to this arc
	 * @param p the place to be connected to this arc
	 */
	public PNArc(Transition t, Place p) {
		source=t;
		target=p;
		direction = TP;
	}

	public boolean isPT() {
		return PT;
	}

	public boolean isTP() {
		return TP;
	}

	public boolean getDirection() {
		return direction;
	}

	public PNNode getSource() {
		return source;
	}

	public PNNode getTarget() {
		return target;
	}

}
