package data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import org.rosuda.REngine.REXPMismatchException;
import elementDescriptor.SpatStatNetworkDescriptionBlock;
import fiji.plugin.trackmate.Spot;
import graph.ConnectedGraphAnalyzer;
import rServe.Connection;

public class Subgraph {

	int MARGIN = 100;

	SpatStatNetworkDescriptionBlock sNetwork;

	ArrayList<data.Spot> pointInSubgraph = null;
	ArrayList<data.Spot> ends = null;
	ArrayList<data.Spot> junctions = null;

	HashMap<Fname, Function> functionList = new HashMap<Fname, Function>();

	double nbAcceptedPoints;
	double nbAcceptedEnds;
	double nbAcceptedJunctions;
	double lengthSubgraph;
	double RatioAcceptedPoint;

	double[] lambdaEnd;
	double sum_1_lambda = 0;
	Random rand  = RandomSingle.getInstance().getRand();

	
	public Subgraph(ConnectedGraphAnalyzer connected) {

		sNetwork = connected.getDescription();

		ends = new ArrayList<data.Spot>();
		for (double[] pos : connected.get1SEPosition()) {
			ends.add(new data.Spot(pos[0], pos[1], 0.0, 1, 0, "SE1"));
		}

		junctions = new ArrayList<data.Spot>();
		for (double[] pos : connected.get3SEPosition()) {
			junctions.add(new data.Spot(pos[0], pos[1], 0.0, 1, 0, "SE1"));
		}

		functionList = new HashMap<Fname, Function>();

	}

	public Subgraph() {
	}

	// add point in the category of point not structurally defined
	public void addPoint(data.Spot p) {
		if (pointInSubgraph == null) {
			pointInSubgraph = new ArrayList<data.Spot>();
		}
		pointInSubgraph.add(p);
	}

	public double[] getMaxPosition() {

		double[] position = new double[2];

		for (double[] pos : sNetwork.getPosList()) {
			if (pos[0] > position[0]) {
				position[0] = pos[0];
			}
			if (pos[1] > position[1]) {
				position[1] = pos[1];
			}
		}
		return position;
	}

	public void sendGraphToR(Connection c, boolean print) throws Exception {

		if (!c.checkConnection()) {
			throw (new Exception("problem with connection"));
		}

		c.clear();

		double[] pos = this.getMaxPosition();

		int width = (int) pos[0] + MARGIN;
		int heigth = (int) pos[1] + MARGIN;

		String vertexDesc = "v <- ppp(x=   " + getXVector() + ", y =" + getYVector() + ", c(0," + width + ")  , "
				+ "c(0," + heigth + ")    )";

		String vectorConnection = "vect <- " + getConnectionVector();

		String vx = "vx <- c(";
		String vy = "vy <- c(";
		String label = "lab <- factor( c(";

		boolean first = true;

		if (pointInSubgraph != null && pointInSubgraph.size() > 0) {
			for (int i = 0; i < pointInSubgraph.size(); i++) {
				double x = pointInSubgraph.get(i).networkx;
				double y = pointInSubgraph.get(i).networky;
				if (first) {
					vx += x;
					vy += y;
					label += "\"points\"";
					first = false;
				} else {
					vx += "," + x;
					vy += "," + y;
					label += "," + "\"points\"";
				}
			}
		}

		if (ends != null && ends.size() > 0) {
			for (int i = 0; i < ends.size(); i++) {
				double x = ends.get(i).getDoublePosition(0);
				double y = ends.get(i).getDoublePosition(1);

				if (first) {
					vx += x;
					vy += y;
					label += "\"ends\"";
					first = false;
				} else {
					vx += "," + x;
					vy += "," + y;
					label += "," + "\"ends\"";
				}
			}
		}

		if (junctions != null && junctions.size() > 0) {
			for (int i = 0; i < junctions.size(); i++) {
				double x = junctions.get(i).getDoublePosition(0);
				double y = junctions.get(i).getDoublePosition(1);

				if (first) {
					vx += x;
					vy += y;
					label += "\"junction\"";
					first = false;
				} else {
					vx += "," + x;
					vy += "," + y;
					label += "," + "\"junction\"";
				}
			}
		}

		vx += ")";
		vy += ")";
		label += "))";

		try {
			c.runRserve(vertexDesc);
			c.runRserve(vectorConnection);
			c.runRserve(label);

		} catch (Exception e) {
			e.printStackTrace();
			throw (new Exception("problem with vertexDesc or  vectorConnection or label in subgraph description in R"));
		}
		String out = "edg <- matrix(  vect , nrow=" + sNetwork.getConnectionList().size() + ",ncol=2 , byrow = TRUE)";
		try {
			c.runRserve(out);
		} catch (Exception e) {
			e.printStackTrace();
			throw (new Exception("problem with edg <- matrix  in subgraph description in R"));
		}
		out = "graph <- linnet(v, edges=edg, , sparse=TRUE)";
		try {
			c.runRserve(out);
		} catch (Exception e) {
			e.printStackTrace();
			throw (new Exception("problem with  graph <- linnet(  in subgraph description in R"));
		}
		try {
			c.runRserve(vx);
			c.runRserveTalk(vy);
		} catch (Exception e) {
			e.printStackTrace();
			throw (new Exception("problem with  vx or vy in subgraph description in R"));
		}
		out = " PPP <- ppp(x=vx, y=vy," + " c(0," + width + ")  , " + "c(0," + heigth + ") , marks= lab  ) ";
		try {
			c.runRserve(out);
		} catch (Exception e) {
			e.printStackTrace();
			throw (new Exception("problem with PPP  in subgraph description in R"));
		}
		try {
			c.runRserve("LNPP<- lpp(PPP, graph) ");
		} catch (Exception e) {
			e.printStackTrace();
			throw (new Exception("problem with lpp  in subgraph description in R"));
		}
		try {
			c.runRserve("LNPPE<- lpp(subset(PPP, marks==\"ends\"), graph)   ");
		} catch (Exception e) {
			e.printStackTrace();
			throw (new Exception("problem with lpp  in subgraph description in R"));
		}

		if (ends != null && ends.size() > 0) {
			try {

				//c.runRserve("png('graph.png' ,res = 120 ) ");
				//c.runRserve("plot(  subset(LNPP, marks==\"ends\")  )");
				//c.runRserve("dev.off()");

				c.runRserve("lppm_e <- density.ppp(sigma=5, x=as.ppp(subset(PPP, marks==\"ends\")) )");
				System.out.println("min of distrib");
				// System.out.println(c.runRserveTalk("lppm_e[LNPPE,drop=FALSE]").asDoubles());
				// System.out.println(c.runRserveTalk("min(lppm_e[LNPPE,drop=FALSE])").asDoubles());
				// System.out.println(c.runRserveTalk("max(lppm_e[LNPPE,drop=FALSE])").asDoubles());

				//c.runRserve("png('lppm_e.png' ,res = 120 ) ");
				//c.runRserve("plot(  lppm_e )");
				//c.runRserve("dev.off()");

			} catch (Exception e) {
				e.printStackTrace();
				throw (new Exception("problem with lppm for ends in subgraph description in R"));
			}
		}
		if (pointInSubgraph != null && pointInSubgraph.size() > 0) {
			try {
				c.runRserve("lppm_p <- lppm(subset(LNPP, marks==\"points\")) ");

			//	c.runRserve("png('lppm_p.png' ,res = 120 ) ");
			//	c.runRserve("plot(  lppm_p )");
			//	c.runRserve("dev.off()");
			} catch (Exception e) {
				e.printStackTrace();
				throw (new Exception("problem with lppm for ends in subgraph description in R"));
			}
		}
		if (print) {
			try {
				Random r = new Random();

				//c.runRserve("jpeg('graph_" + r + "_.png' , width = 8, height = 8, units = 'in' ,  res = 700 )");
				//c.runRserve("plot(  PPP )");
				//c.runRserve("dev.off()");
				//c.runRserve("jpeg('lpp_" + r + "_.png' , width = 8, height = 8, units = 'in' ,  res = 700 )");
				//c.runRserve("plot(  subset(LNPP, marks==\"points\")  )");
				//c.runRserve("dev.off()");

			} catch (Exception e) {
				e.printStackTrace();
				throw (new Exception("problem with graph printing"));
			}

		}

	}

	public void sendTemporyPoint(Connection c, ArrayList<data.Spot> spotlist, boolean print) throws Exception {

		if (!c.checkConnection()) {
			throw (new Exception("problem with connection"));
		}
		
		double[] pos = this.getMaxPosition();

		int width = (int) pos[0] + MARGIN;
		int heigth = (int) pos[1] + MARGIN;

		String vertexDesc = "vR <- ppp(x=   " + getXVector() + ", y =" + getYVector() + ", c(0," + width + ")  , "
				+ "c(0," + heigth + ")    )";

		String vectorConnection = "vectR <- " + getConnectionVector();
		
		boolean first =true;
		String vx = "vxR <- c(";
		String vy = "vyR <- c(";
		String label = "labR <- factor( c(";
		
		/*
		if (spotlist != null && spotlist.size() > 0) {
			for (int i = 0; i < spotlist.size(); i++) {
				double x = spotlist.get(i).getDoublePosition(0);
				double y = spotlist.get(i).getDoublePosition(1);
				if (first) {
					vx += x;
					vy += y;
					label += "\"randsp\"";
					first = false;
				} else {
					vx += "," + x;
					vy += "," + y;
					label += "," + "\"randsp\"";
				}
			}
		}
		*/
		
		if (ends != null && ends.size() > 0) {
			for (int i = 0; i < ends.size(); i++) {
				double x = ends.get(i).getDoublePosition(0);
				double y = ends.get(i).getDoublePosition(1);

				if (first) {
					vx += x;
					vy += y;
					label += "\"ends\"";
					first = false;
				} else {
					vx += "," + x;
					vy += "," + y;
					label += "," + "\"ends\"";
				}
			}
		}

		if (junctions != null && junctions.size() > 0) {
			for (int i = 0; i < junctions.size(); i++) {
				double x = junctions.get(i).getDoublePosition(0);
				double y = junctions.get(i).getDoublePosition(1);

				if (first) {
					vx += x;
					vy += y;
					label += "\"junction\"";
					first = false;
				} else {
					vx += "," + x;
					vy += "," + y;
					label += "," + "\"junction\"";
				}
			}
		}

		vx += ")";
		vy += ")";
		label += "))";

		try {
			c.runRserve(vertexDesc);
			c.runRserve(vectorConnection);
			c.runRserve(label);

		} catch (Exception e) {
			e.printStackTrace();
			throw (new Exception("problem with vertexDesc or  vectorConnection or label in subgraph description in R"));
		}
		String out = "edgR <- matrix(  vectR , nrow=" + sNetwork.getConnectionList().size() + ",ncol=2 , byrow = TRUE)";
		try {
			c.runRserve(out);
		} catch (Exception e) {
			e.printStackTrace();
			throw (new Exception("problem with edg <- matrix  in subgraph description in R"));
		}
		out = "graphR <- linnet(vR, edges=edgR, , sparse=TRUE)";
		try {
			c.runRserve(out);
		} catch (Exception e) {
			e.printStackTrace();
			throw (new Exception("problem with  graph <- linnet(  in subgraph description in R"));
		}
		try {
			c.runRserve(vx);
			c.runRserveTalk(vy);
		} catch (Exception e) {
			e.printStackTrace();
			throw (new Exception("problem with  vx or vy in subgraph description in R"));
		}
		out = " PPPR <- ppp(x=vxR, y=vyR," + " c(0," + width + ")  , " + "c(0," + heigth + ") , marks= labR  ) ";
		try {
			c.runRserve(out);
		} catch (Exception e) {
			e.printStackTrace();
			throw (new Exception("problem with PPP  in subgraph description in R"));
		}
		try {
			c.runRserve("LNPPR<- lpp(PPPR, graph) ");
		} catch (Exception e) {
			e.printStackTrace();
			throw (new Exception("problem with lpp  in subgraph description in R"));
		}
	
		int nb = spotlist.size();
		
		try {
			c.runRserve("TEMPS<- runiflpp(" + nb + ", graph) ");
			c.runRserve("marks(TEMPS) <- \"randsp\" ");
			c.runRserve("LNPPR<- superimpose(LNPPR, TEMPS) ");
		} catch (Exception e) {
			e.printStackTrace();
			throw (new Exception("problem with lpp  in subgraph description in R"));
		}
	
		
		
		
		
		if (print||true) {
			try {
				
				if(this.ends.size()>2) {
					//Random r = new Random();
					//c.runRserve("jpeg('graph_Random_" + r.nextInt(50) +"nb_ends"+this.ends.size() +"_.png' , width = 20, height = 20, units = 'in' ,  res = 700 )");
					//c.runRserve("plot(  LNPPR )");
					//c.runRserve("dev.off()");
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw (new Exception("problem with graph printing"));
			}

		}

	}
	
	public void compute(Fname fname, String[] param, Connection c, boolean usecondition, double distanceThreshold,
			boolean lessThan) throws Exception {

		if (!functionList.keySet().contains(fname)) {
			if (Fname.checkSubgraph(fname, nbAcceptedPoints, nbAcceptedEnds, nbAcceptedJunctions)) {

				if (Fname.isApprox(fname)) {
					String[] desc = Fname.getDefinition(fname, param);

					if(Fname.isRandomCond(fname)){
						ArrayList<data.Spot> newSpot = generateRandomPoint((int)nbAcceptedPoints, usecondition, distanceThreshold, lessThan);
						this.sendTemporyPoint(c, newSpot, true);
						
						
					}
				
					
					if (desc.length >= 7) {
						c.runRserve(desc[0]);
						c.runRserve(desc[1]);
						c.runRserve(desc[2]);
						c.runRserve(desc[3]);
						c.runRserve(desc[4]);
						c.runRserve("f <- approxfun(" + desc[5] + "," + desc[6] + ")");
					} else {
						c.runRserve(desc[0]);
						c.runRserve("f <- approxfun(" + desc[1] + "," + desc[2] + ")");
						System.out.println("ending command");
					}
					double[] r = c.runRserve("seq(from = 0, to = 300, by = 1)").asDoubles();
					double[] y = c.runRserve("f(seq(from = 0, to = 300, by = 1))").asDoubles();
					
					
					if(fname.compareTo(Fname.pcf_p)==0) {
					System.out.println(Arrays.toString(r));
					System.out.println(Arrays.toString(y));
					}
					
					
					Function f = new Function(fname, r, y);
					functionList.put(fname, f);

				} else {
					

					if(Fname.isRandomCond(fname)){
						ArrayList<data.Spot> newSpot = generateRandomPoint((int)nbAcceptedPoints, usecondition, distanceThreshold, lessThan);
						this.sendTemporyPoint(c, newSpot, true);
						
						
					}
					
					
					String[] desc = Fname.getDefinition(fname, param);
					c.runRserve(desc[0]);
					double[] r = c.runRserve(desc[1]).asDoubles();
					double[] y = c.runRserve(desc[2]).asDoubles();
				
					
					Function f = new Function(fname, r, y);
					functionList.put(fname, f);
				}
			}
		}
	}

	public ArrayList<data.Spot> generateRandomPoint(int numberPoint, boolean usecondition, double distanceThreshold, boolean lessThan) {
		ArrayList<data.Spot> pointList = new ArrayList<data.Spot>();
		
		double[][] poslist = this.sNetwork.getPosList();

		System.out.println(poslist.length);

		for (int i = 0; i < numberPoint; i++) {
			boolean found = false;

			while (!found) {
				double minDist = Double.MAX_VALUE;
				int nextint = rand.nextInt(poslist.length);
				System.out.println(nextint);
				
				
				for (Spot end : this.getEnds()) {
					double distance = Math.sqrt(Math.pow(((data.Spot) end).getDoublePosition(0) - poslist[nextint][0], 2)
							+ Math.pow(((data.Spot) end).getDoublePosition(1) - poslist[nextint][1], 2));
				
					if (distance < minDist) {
						minDist = distance;
					}
				}
				//System.out.println(minDist);
				
				if (usecondition) {

					if (lessThan) {
						if (minDist <= distanceThreshold) {
							pointList.add(new  data.Spot(poslist[nextint][0], poslist[nextint][1], 0.0, 10.0, 1000.0, null));
							found=true;
						}
					} else {
						if (minDist >= distanceThreshold) {
							pointList.add(new  data.Spot(poslist[nextint][0], poslist[nextint][1], 0.0, 10.0, 1000.0, null));
							found=true;
						}
					}
				} else {
					pointList.add(new  data.Spot(poslist[nextint][0], poslist[nextint][1], 0.0, 10.0, 1000.0, null));
					found=true;
				}
			}
		}

		return pointList;
	}

	public void computePointSelection(Connection c) throws REXPMismatchException, Exception {

		double[] d = c.runRserve("volume(graph)").asDoubles();
		double[] pointsNb = c.runRserve("npoints(subset(LNPP,marks ==\"points\") )").asDoubles();
		double[] EndsNb = c.runRserve("npoints(subset(LNPP,marks ==\"ends\") )").asDoubles();
		double[] JunctionNb = c.runRserve("npoints(subset(LNPP,marks ==\"junction\") )").asDoubles();

		nbAcceptedPoints = pointsNb[0];
		nbAcceptedEnds = EndsNb[0];
		nbAcceptedJunctions = JunctionNb[0];
		lengthSubgraph = d[0];
		if (pointInSubgraph != null) {
			RatioAcceptedPoint = nbAcceptedPoints / ((double) pointInSubgraph.size());
		} else {
			RatioAcceptedPoint = 1;
		}
		
	}

	public String getXVector() {
		return this.sNetwork.getXvector();
	}

	public String getYVector() {
		return this.sNetwork.getYvector();
	}

	public String getConnectionVector() {
		return this.sNetwork.getLinkvector();
	}

	public SpatStatNetworkDescriptionBlock getsNetwork() {
		return sNetwork;
	}

	public void setsNetwork(SpatStatNetworkDescriptionBlock sNetwork) {
		this.sNetwork = sNetwork;
	}

	public ArrayList<data.Spot> getPointInSubgraph() {
		return pointInSubgraph;
	}

	public void setPointInSubgraph(ArrayList<data.Spot> pointInSubgraph) {
		this.pointInSubgraph = pointInSubgraph;
	}

	public ArrayList<data.Spot> getEnds() {
		return ends;
	}

	public void setEnds(ArrayList<data.Spot> ends) {
		this.ends = ends;
	}

	public ArrayList<data.Spot> getJunctions() {
		return junctions;
	}

	public void setJunctions(ArrayList<data.Spot> junctions) {
		this.junctions = junctions;
	}

	public int getMARGIN() {
		return MARGIN;
	}

	public void setMARGIN(int mARGIN) {
		MARGIN = mARGIN;
	}

	public HashMap<Fname, Function> getFunctionList() {
		return functionList;
	}

	public void setFunctionList(HashMap<Fname, Function> functionList) {
		this.functionList = functionList;
	}

	public double getNbAcceptedPoints() {
		return nbAcceptedPoints;
	}

	public void setNbAcceptedPoints(double nbAcceptedPoints) {
		this.nbAcceptedPoints = nbAcceptedPoints;
	}

	public double getNbAcceptedEnds() {
		return nbAcceptedEnds;
	}

	public void setNbAcceptedEnds(double nbAcceptedEnds) {
		this.nbAcceptedEnds = nbAcceptedEnds;
	}

	public double getNbAcceptedJunctions() {
		return nbAcceptedJunctions;
	}

	public void setNbAcceptedJunctions(double nbAcceptedJunctions) {
		this.nbAcceptedJunctions = nbAcceptedJunctions;
	}

	public double getLengthSubgraph() {
		return lengthSubgraph;
	}

	public void setLengthSubgraph(double lengthSubgraph) {
		this.lengthSubgraph = lengthSubgraph;
	}

	public double getRatioAcceptedPoint() {
		return RatioAcceptedPoint;
	}

	public void setRatioAcceptedPoint(double ratioAcceptedPoint) {
		RatioAcceptedPoint = ratioAcceptedPoint;
	}

	public double[] getLambdaEnd() {
		return lambdaEnd;
	}

	public void setLambdaEnd(double[] lambdaEnd) {
		this.lambdaEnd = lambdaEnd;
	}

}
