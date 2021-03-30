package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import fiji.plugin.trackmate.Spot;
import fiji.plugin.trackmate.SpotCollection;
import graph.CompleteGraphAnalyzer;
import graph.ConnectedGraphAnalyzer;

public class Data {

	// model of graph used to describe subgraphs
	GraphModel graphModel;
	// list of points loaded
	ArrayList<Spot> pointList;
	// List of subgraph
	ArrayList<Subgraph> subgraphList;

	public Data(GraphModel graphModel, ArrayList<Spot> pointList, double maxPointDistance) {
		this.graphModel = graphModel;
		this.pointList = pointList;

		// if there's a graph
		if (this.graphModel != null) {

			subgraphList = new ArrayList<Subgraph>();

			// create the list of subgraph if there's subgraph
			for (ConnectedGraphAnalyzer connectedGraph : graphModel.getListOfSubgraph()) {
				subgraphList.add(new Subgraph(connectedGraph));
			}
			// if there's point to analyze we should add them to the good graph

			if (pointList != null) {

				// the map contain the point and the vector minimum distance,
				// pos[0],pos[1], indexSubgraph }
				HashMap<Spot, double[]> closestDistance = new HashMap<Spot, double[]>();

				int subgraphIndex = 0; // for each subgraph write the neares
				for (Subgraph sub : subgraphList) { // for each point
					for (Spot p : pointList) { // for all element of the network
						for (double[] pos : sub.getsNetwork().getPosList()) { // get
																				// the
																				// distance
																				// between
																				// point
																				// andthe
																				// current
																				// //
																				// subgraph
																				// element
							double distance = Math.sqrt(Math.pow(p.getDoublePosition(0) - pos[0], 2)
									+ Math.pow(p.getDoublePosition(1) - pos[1], 2)); // check
																						// if
																						// it's
																						// the
																						// first
																						// element

							if (closestDistance.containsKey(p)) {
								if (distance < closestDistance.get(p)[0]) {
									closestDistance.put(p, new double[] { distance, pos[0], pos[1], subgraphIndex });
								}
							} else {
								closestDistance.put(p, new double[] { distance, pos[0], pos[1], subgraphIndex });
							}
						}
					}
					subgraphIndex++;
				}

				// get subgraph list
				for (int i = 0; i < subgraphList.size(); i++) { // point list
					for (Spot p : pointList) {
						double[] pdescr = closestDistance.get(p); // check if
																	// it's for
																	// the good
																	// graph
						if (i == pdescr[3]) {
							if (true) {
								subgraphList.get(i).addPoint(new data.Spot(p, pdescr[1], pdescr[2]));

							}
						}
					}
				}
			}
		}

	}

	public Data(SpotCollection spotCol, CompleteGraphAnalyzer cga, double maxPointDistance, boolean usecondition,
			double distanceThreshold, boolean lessThan, boolean conditionJunction, double distanceThresholdJunction,
			boolean lessThanJunction, boolean removePoint, double probabilityRemovePoint) {

		GraphModel graphModel = new GraphModel(cga);
		ArrayList<Spot> pointList = new ArrayList<Spot>();

		for (Spot s : spotCol.iterable(true)) {
			pointList.add(s);
		}

		this.graphModel = graphModel;
		this.pointList = pointList;

		// if there's a graph
		if (this.graphModel != null) {
			subgraphList = new ArrayList<Subgraph>();

			// create the list of subgraph if there's subgraph
			for (ConnectedGraphAnalyzer connectedGraph : graphModel.getListOfSubgraph()) {
				subgraphList.add(new Subgraph(connectedGraph));
			}
			// if there's point to analyze we should add them to the good graph
			if (pointList != null) {

				// the map contain the point and the vector minimum distance,
				// pos[0], pos[1], indexSubgraph }
				HashMap<Spot, double[]> closestDistance = new HashMap<Spot, double[]>();

				int subgraphIndex = 0;
				// for each subgraph write the neares
				for (Subgraph sub : subgraphList) {
					// for each point
					for (Spot p : pointList) {
						// for all element of the network
						for (double[] pos : sub.getsNetwork().getPosList()) {
							// get the distance between point and the current
							// subgraph element
							double distance = Math.sqrt(Math.pow(p.getDoublePosition(0) - pos[0], 2)
									+ Math.pow(p.getDoublePosition(1) - pos[1], 2));
							// check if it's the first element
							if (closestDistance.containsKey(p)) {
								if (distance < closestDistance.get(p)[0]) {
									closestDistance.put(p, new double[] { distance, pos[0], pos[1], subgraphIndex });
								}
							} else {
								closestDistance.put(p, new double[] { distance, pos[0], pos[1], subgraphIndex });
							}
						}
					}
					subgraphIndex++;
				}

				// get subgraph list
				for (int i = 0; i < subgraphList.size(); i++) {

					ArrayList<data.Spot> tempSpot = new ArrayList<data.Spot>();

					// point list
					for (Spot p : pointList) {
						double[] pdescr = closestDistance.get(p);
						// check if it's for the good graph
						if (i == pdescr[3]) {

							if (maxPointDistance > pdescr[0]) {

								double minDist = Double.MAX_VALUE;
								double minDistJunc = Double.MAX_VALUE;

								// check distance to end of the graph
								for (Spot end : subgraphList.get(i).getEnds()) {

									double distance = Math.sqrt(Math.pow(end.getDoublePosition(0) - pdescr[1], 2)
											+ Math.pow(end.getDoublePosition(1) - pdescr[2], 2));
									if (distance < minDist) {
										minDist = distance;
									}
								}

								for (Spot junction : subgraphList.get(i).getJunctions()) {

									double distance = Math.sqrt(Math.pow(junction.getDoublePosition(0) - pdescr[1], 2)
											+ Math.pow(junction.getDoublePosition(1) - pdescr[2], 2));
									if (distance < minDistJunc) {
										minDistJunc = distance;
									}
								}

								boolean toAddEnd = false;
								boolean toAddJunction = false;

								if (usecondition) {

									if (lessThan) {
										if (minDist <= distanceThreshold) {
											toAddEnd = true;
											// subgraphList.get(i).addPoint(new data.Spot(p, pdescr[1], pdescr[2]));
										}
									} else {
										if (minDist >= distanceThreshold) {
											// subgraphList.get(i).addPoint(new data.Spot(p, pdescr[1], pdescr[2]));
											toAddEnd = true;
										}
									}

								} else {

									toAddEnd = true;
									// subgraphList.get(i).addPoint(new data.Spot(p, pdescr[1], pdescr[2]));
								}

								if (conditionJunction) {

									if (lessThan) {
										if (minDistJunc <= distanceThresholdJunction) {
											toAddJunction = true;
											// subgraphList.get(i).addPoint(new data.Spot(p, pdescr[1], pdescr[2]));
										}
									} else {
										if (minDistJunc >= distanceThresholdJunction) {
											// subgraphList.get(i).addPoint(new data.Spot(p, pdescr[1], pdescr[2]));
											toAddJunction = true;
										}
									}

								} else {

									toAddJunction = true;
									// subgraphList.get(i).addPoint(new data.Spot(p, pdescr[1], pdescr[2]));
								}

								if (toAddEnd && toAddJunction) {

									tempSpot.add(new data.Spot(p, pdescr[1], pdescr[2]));

								}
							}
						}
					}

					if (removePoint) {
						Collections.shuffle(tempSpot);
						int ratioToKeep = (int) Math.round(((double) tempSpot.size()) * (1.0 - probabilityRemovePoint));

						for (int k = 0; k < ratioToKeep; k++) {
							subgraphList.get(i).addPoint(tempSpot.get(k));
						}

					} else {
						for (data.Spot s : tempSpot) {
							subgraphList.get(i).addPoint(s);
						}
					}

					// all spot have been visited

				}
			}
		}

	}

	

	public Data(double ratio, CompleteGraphAnalyzer cga, double maxPointDistance) {

		Random rand = RandomSingle.getInstance().getRand();
		GraphModel graphModel = new GraphModel(cga);
		ArrayList<Spot> pointList = new ArrayList<Spot>();

		this.graphModel = graphModel;
		this.pointList = pointList;

		if (this.graphModel != null) {
			subgraphList = new ArrayList<Subgraph>();

			for (ConnectedGraphAnalyzer connectedGraph : graphModel.getListOfSubgraph()) {
				Subgraph sub = new Subgraph(connectedGraph);
				subgraphList.add(sub);

				// int numberPoint =100;
				double[][] poslist = connectedGraph.getDescription().getPosList();

				for (double[] pos : poslist) {
					if (rand.nextDouble() < ratio) {
						Spot p = new data.Spot(pos[0], pos[1], 0.0, 10.0, 1000.0, null);
						sub.addPoint(new data.Spot(p, pos[0], pos[1]));
					}
				}
				/*
				 * for (int i = 0; i < numberPoint; i++) { boolean found = false;
				 * 
				 * while (!found) { double minDist = Double.MAX_VALUE; int nextint =
				 * rand.nextInt(poslist.length); System.out.println(nextint);
				 * 
				 * Spot p = new data.Spot(poslist[nextint][0], poslist[nextint][1], 0.0, 10.0,
				 * 1000.0, null); sub.addPoint(new data.Spot(p, poslist[nextint][0],
				 * poslist[nextint][1])); found=true;
				 * 
				 * } }
				 */

			}
		}

	}

	public GraphModel getGraphModel() {
		return graphModel;
	}

	public void setGraphModel(GraphModel graphModel) {
		this.graphModel = graphModel;
	}

	public ArrayList<Spot> getPointList() {
		return pointList;
	}

	public void setPointList(ArrayList<Spot> pointList) {
		this.pointList = pointList;
	}

	public ArrayList<Subgraph> getSubgraphList() {
		return subgraphList;
	}

	public void setSubgraphList(ArrayList<Subgraph> subgraphList) {
		this.subgraphList = subgraphList;
	}

}
