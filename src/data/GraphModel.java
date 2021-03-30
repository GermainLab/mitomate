package data;

import java.util.ArrayList;

import fiji.plugin.trackmate.Spot;
import graph.CompleteGraphAnalyzer;
import graph.ConnectedGraphAnalyzer;

public class GraphModel {

	//complete graph model
	private CompleteGraphAnalyzer completeGraph;
	
	
	//contain the junctions of the graph
	ArrayList<Spot> junctions;
	//contains the ends of the graph
	ArrayList<Spot> ends;
	
	//constructor
	public GraphModel(CompleteGraphAnalyzer cga){
		this.completeGraph=cga;
	}
	
	public ArrayList<ConnectedGraphAnalyzer> getListOfSubgraph(){
		return  completeGraph.getSeparetedGraphList();
	}

	public CompleteGraphAnalyzer getCompleteGraph() {
		return completeGraph;
	}

	public void setCompleteGraph(CompleteGraphAnalyzer completeGraph) {
		this.completeGraph = completeGraph;
	}

	public ArrayList<Spot> getJunctions() {
		return junctions;
	}

	public void setJunctions(ArrayList<Spot> junctions) {
		this.junctions = junctions;
	}

	public ArrayList<Spot> getEnds() {
		return ends;
	}

	public void setEnds(ArrayList<Spot> ends) {
		this.ends = ends;
	}
	
}
