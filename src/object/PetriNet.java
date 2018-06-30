package object;

import java.util.ArrayList;

//一个petri网由变迁、库所、弧组成
public class PetriNet {
	/* PN={p,t,f} */
	private ArrayList<Transition> transitions; 		//transition t
	private ArrayList<Place> places;	//place p
	private ArrayList<PNArc> pnArcs;

	public PetriNet(ArrayList<Transition> transitions,ArrayList<Place> places,ArrayList<PNArc> pnArcs) {
		this.transitions=transitions;
		this.places=places;
		this.pnArcs=pnArcs;
	}
	/**
	 * Gets the <code>Transition</code> nodes contained in this Petri net.
	 *获得PetriNet中的transition节点
	 * @return a list of all transitions contained
	 */
	public ArrayList<Transition> getTransitions() {
		return transitions;
	}
	/**
	 * Gets the <code>Place</code> nodes contained in this Petri net.
	 *
	 * @return a list of all places contained
	 */
	public ArrayList<Place> getPlaces() {
		return places;
	}

	public ArrayList<PNArc> getPnArcs() {
		return pnArcs;
	}


}
