package data;

import java.util.ArrayList;

public enum Fname {
	pcf_p, pcf_p_randCond, pcf_j, pcf_e, pcf_e_inhom, pcf_p_e_inhom, pcf_p_e_inhom_random, pcf_p_j, pcf_p_j_randCond, pcf_j_p, pcf_j_p_randCond, pcf_p_e,
	pcf_p_e_random, pcf_p_e_ranCond, pcf_e_p_randCond, pcf_e_p, pcf_j_e, lk_p, lk_j, lk_p_j, lk_p_e, lkNN_p_j, lkNN_p_e, 
   nndist_a, nndist_j_p, nndist_j_e, nndist_e_j, nndist_e_p, nndist_p, nndist_j, nndist_e,nndist_j_p_random, nndist_p_random, nndist_e_p_random;

	public static int min = 50;

	public static int MAXLENGTH = 300;

	// pcf_p param : [bandwith, rlist]
	public static String[] getDefinition(Fname f, String[] param) throws Exception {

		// to set up a pcf_p we need the bandwith
		if (f.compareTo(pcf_p) == 0) {
			if (param.length != 1) {
				throw new Exception("bad param in : pcf_p param :  [bandwith] ");
			}
			return new String[] {
					" lpts_dot <- linearpcf(subset(LNPP, marks==\"points\") ,r=seq(from = 0, to = 300, by = 1), bw = "
							+ param[0] + ",endcorrect=TRUE ) ",
					"lpts_dot$r", "lpts_dot$est", "lpts_dot$alim" };
		}

		// ****************************************************************************
		// NEW
		// ****************************************************************************
		if (f.compareTo(pcf_p_randCond) == 0) {
			if (param.length != 1) {
				throw new Exception("bad p" + "aram in : pcf_p param :  [bandwith] ");
			}
			double newParam = Double.parseDouble(param[0]) * 2;
			return new String[] {
					" lpts_dot <- linearpcf(subset(LNPPR, marks==\"randsp\"),r=seq(from = 0, to = 300, by = 1), bw = "
							+ param[0] + ",endcorrect=TRUE ) ",
					"lpts_dot$r", "lpts_dot$est", "lpts_dot$alim" };
		}

		if (f.compareTo(pcf_e) == 0) {
			if (param.length != 1) {
				throw new Exception("bad param in : pcf_e param :  [bandwith] ");
			}
			return new String[] {
					" lpts_dot <- linearpcf( subset(LNPP, marks==\"ends\"),r=seq(from = 0, to = 300, by = 1), bw = "
							+ param[0] + " , endcorrect=TRUE )",
					"lpts_dot$r", "lpts_dot$est", "lpts_dot$alim" };
		}

		if (f.compareTo(pcf_e_inhom) == 0) {
			if (param.length != 1) {
				throw new Exception("bad param in : pcf_e param :  [bandwith] ");
			}
			return new String[] {
					"lpts_dot <- linearpcfinhom(LNPPE, lambda=lppm_e,r=seq(from = 0, to = 300, by = 1),  bw = "
							+ param[0] + ")",
					"lpts_dot$r", "lpts_dot$est", "lpts_dot$alim" };
		}

		if (f.compareTo(pcf_j) == 0) {
			if (param.length != 1) {
				throw new Exception("bad param in : pcf_j param :  [bandwith] ");
			}
			return new String[] {
					" lpts_dot <- linearpcf( subset(LNPP, marks==\"junction\"),r=seq(from = 0, to = 300, by = 1), bw = "
							+ param[0] + ", endcorrect=TRUE )",
					"lpts_dot$r", "lpts_dot$est", "lpts_dot$alim" };
		}

		if (f.compareTo(pcf_p_j) == 0) {
			if (param.length != 1) {
				throw new Exception("bad param in : pcf_p_j param :  [bandwith] ");
			}
			return new String[] {
					" lpts_dot <- linearpcfcross( LNPP, \"points\", \"junction\",r=seq(from = 0, to = 300, by = 1), bw = "
							+ param[0] + " , endcorrect=TRUE )",
					"lpts_dot$r", "lpts_dot$est", "lpts_dot$alim" };
		}

		if (f.compareTo(pcf_p_j_randCond) == 0) {
			if (param.length != 1) {
				throw new Exception("bad p" + "aram in : pcf_p param :  [bandwith] ");
			}

			return new String[] {
					" lpts_dot <- linearpcfcross(LNPPR, \"randsp\", \"junction\",r=seq(from = 0, to = 300, by = 1), bw = "
							+ param[0] + ",endcorrect=TRUE ) ",
					"lpts_dot$r", "lpts_dot$est", "lpts_dot$alim" };
		}

		if (f.compareTo(pcf_p_e_inhom) == 0) {
			if (param.length != 1) {
				throw new Exception("bad param in : pcf_p_j param :  [bandwith] ");
			}
			return new String[] {
					" lpts_dot <- linearpcfcross.inhom( LNPP, \"points\", \"ends\", lppm_p ,lppm_e,r=seq(from = 0, to = 300, by = 1), bw = "
							+ param[0] + "  )",
					"lpts_dot$r", "lpts_dot$est", "lpts_dot$alim" };
		}
		if (f.compareTo(pcf_p_e_inhom_random) == 0) {
			if (param.length != 1) {
				throw new Exception("bad param in : pcf_p_j param :  [bandwith] ");
			}

			double newParam = Double.parseDouble(param[0]) * 2;
			return new String[] { "X <- runiflpp( npoints( subset(LNPP, marks==\"points\" )) , as.linnet(LNPP))",
					"lppm_p_r<-density.ppp(sigma=5,x=as.ppp(X)) ", "marks(X)<-'rand'", "surimp<-superimpose(X,LNPP)",
					"lpts_dot<-linearpcfcross.inhom(surimp,'rand','ends',lppm_p_r,lppm_e, bw =" + param[0] + ")",
					"lpts_dot$r", "lpts_dot$est", "lpts_dot$alim" };
		}

		if (f.compareTo(pcf_p_e_random) == 0) {
			if (param.length != 1) {
				throw new Exception("bad param in : pcf_p_j param :  [bandwith] ");
			}
			double newParam = Double.parseDouble(param[0]) * 2;
			return new String[] { "Xrand <- runiflpp( npoints( subset(LNPP, marks==\"points\" )) , as.linnet(graph))",
					"marks(Xrand)<-'rand'", "surimp<-superimpose(Xrand,LNPP)",
					" lpts_dot <- linearpcfcross( surimp, \"rand\", \"ends\", bw = " + param[0]
							+ " , endcorrect=TRUE )",
					"lpts_dot$r", "lpts_dot$est", "lpts_dot$alim" };
		}
		//******************************************
		//CHANGE FOR TRUE
		//****************************************
		if (f.compareTo(pcf_p_e_ranCond) == 0) {
			if (param.length != 1) {
				throw new Exception("bad p" + "aram in : pcf_p param :  [bandwith] ");
			}
			double newParam = Double.parseDouble(param[0]) * 2;
			return new String[] {
					" lpts_dot <- linearpcfcross(LNPPR, \"randsp\", \"ends\",r=seq(from = 0, to = 300, by = 1), bw = "
							+ param[0] + ",endcorrect=TRUE ) ",
					"lpts_dot$r", "lpts_dot$est", "lpts_dot$alim" };
		}
		
		if (f.compareTo(pcf_e_p) == 0) {
			if (param.length != 1) {
				throw new Exception("bad param in : pcf_e_p param :  [bandwith] ");
			}
			return new String[] {
					" lpts_dot <- linearpcfcross( LNPP, \"ends\", \"points\", r=seq(from = 0, to = 300, by = 1), bw = "
							+ param[0] + " , endcorrect=TRUE )",
					"lpts_dot$r", "lpts_dot$est", "lpts_dot$alim" };
		}
		
		if (f.compareTo(pcf_e_p_randCond) == 0) {
			if (param.length != 1) {
				throw new Exception("bad p" + "aram in : pcf_e_p_randCond param :  [bandwith] ");
			}
			double newParam = Double.parseDouble(param[0]) * 2;
			return new String[] {
					" lpts_dot <- linearpcfcross(LNPPR,  \"ends\", \"randsp\" ,r=seq(from = 0, to = 300, by = 1), bw = "
							+ param[0] + ",endcorrect=TRUE ) ",
					"lpts_dot$r", "lpts_dot$est", "lpts_dot$alim" };
		}
		
		if (f.compareTo(pcf_j_p) == 0) {
			if (param.length != 1) {
				throw new Exception("bad param in : pcf_e_p param :  [bandwith] ");
			}
			return new String[] {
					" lpts_dot <- linearpcfcross( LNPP, \"junction\", \"points\", r=seq(from = 0, to = 300, by = 1), bw = "
							+ param[0] + " , endcorrect=TRUE )",
					"lpts_dot$r", "lpts_dot$est", "lpts_dot$alim" };
		}
		
		if (f.compareTo(pcf_j_p_randCond) == 0) {
			if (param.length != 1) {
				throw new Exception("bad p" + "aram in : pcf_e_p_randCond param :  [bandwith] ");
			}
			double newParam = Double.parseDouble(param[0]) * 2;
			return new String[] {
					" lpts_dot <- linearpcfcross(LNPPR,  \"junction\", \"randsp\" ,r=seq(from = 0, to = 300, by = 1), bw = "
							+ param[0] + ",endcorrect=TRUE ) ",
					"lpts_dot$r", "lpts_dot$est", "lpts_dot$alim" };
		}
			
		
		//******************************************
		//CHANGE FOR TRUE
		//****************************************
		if (f.compareTo(pcf_p_e) == 0) {
			if (param.length != 1) {
				throw new Exception("bad param in : pcf_p_j param :  [bandwith] ");
			}
			return new String[] {
					" lpts_dot <- linearpcfcross( LNPP, \"points\", \"ends\",r=seq(from = 0, to = 300, by = 1), bw = "
							+ param[0] + " , endcorrect=TRUE )",
					"lpts_dot$r", "lpts_dot$est", "lpts_dot$alim" };
		}
		if (f.compareTo(pcf_j_e) == 0) {
			if (param.length != 1) {
				throw new Exception("bad param in : pcf_j_e param :  [bandwith] ");
			}
			return new String[] {
					" lpts_dot <- linearpcfcross( LNPP, \"junction\", \"ends\",r=seq(from = 0, to = 300, by = 1), bw = "
							+ param[0] + " , endcorrect=TRUE )",
					"lpts_dot$r", "lpts_dot$est", "lpts_dot$alim" };
		}

		/*
		 * if (f.compareTo(pcf_j_e_ranCond) == 0) { if (param.length != 1) { throw new
		 * Exception("bad p" + "aram in : pcf_p param :  [bandwith] "); } double
		 * newParam = Double.parseDouble(param[0]) * 2; return new String[] {
		 * " lpts_dot <- linearpcfcross(LNPPR, \"randsp\", \"ends\", bw = " + newParam +
		 * ",endcorrect=FALSE ) ", "lpts_dot$r", "lpts_dot$est", "lpts_dot$alim" }; }
		 */

		if (f.compareTo(lk_p) == 0) {
			if (param.length != 0) {
				throw new Exception("bad param in : lk_p param :  [null] ");
			}
			return new String[] { " lpts_dot <- linearK(subset(LNPP, marks==\"points\") )", "lpts_dot$r",
					"lpts_dot$est", "lpts_dot$alim" };
		}
		if (f.compareTo(lk_j) == 0) {
			if (param.length != 0) {
				throw new Exception("bad param in : lk_j param :  [null] ");
			}
			return new String[] { " lpts_dot <- linearK(subset(LNPP, marks==\"junction\") )", "lpts_dot$r",
					"lpts_dot$est", "lpts_dot$alim" };
		}
		if (f.compareTo(lk_p_j) == 0 || f.compareTo(lkNN_p_j) == 0) {
			if (param.length != 0) {
				throw new Exception("bad param in : lk_p_j param :  [null] ");
			}
			return new String[] { " lpts_dot <- linearKcross( LNPP, \"junction\", \"points\")", "lpts_dot$r",
					"lpts_dot$est", "lpts_dot$alim" };
		}
		if (f.compareTo(lk_p_e) == 0 || f.compareTo(lkNN_p_e) == 0) {
			if (param.length != 0) {
				throw new Exception("bad param in : lk_p_j param :  [null] ");
			}
			return new String[] { " lpts_dot <- linearKcross( LNPP, \"ends\", \"points\")", "lpts_dot$r",
					"lpts_dot$est", "lpts_dot$alim" };
		}
		// ok dataframed
		if (f.compareTo(nndist_a) == 0) {
			if (param.length != 0) {
				throw new Exception("bad param in : lk_p_j param :  [null] ");
			}
			return new String[] {
					" lpts_dot <- as.numeric(colMeans( data.frame(nndist(LNPP,k=seq(from = 1, to = npoints(LNPP)-1, by = 1)))))",
					"seq(from = 1, to = npoints(LNPP)-1, by = 1)", "lpts_dot", "" };
		}
		// ok dataframed
		if (f.compareTo(nndist_j_p) == 0) {
			if (param.length != 0) {
				throw new Exception("bad param in : nndist_j_p param :  [null] ");
			}
			return new String[] {
					"lpts_dot <-  as.numeric( colMeans(  data.frame(nncross(subset(LNPP, marks==\"junction\"),subset(LNPP, marks==\"points\"), what = \"dist\", k=seq(from = 1, to = npoints(subset(LNPP, marks==\"points\")), by = 1))  )))",
					"seq(from = 1, to = npoints(subset(LNPP, marks==\"points\"))  , by = 1)", "lpts_dot", "" };

		}
		//TODO
		if (f.compareTo(nndist_j_p_random) == 0) {
			if (param.length != 0) {
				throw new Exception("bad param in : nndist_j_p param :  [null] ");
			}
			return new String[] {
					"lpts_dot <-  as.numeric( colMeans(  data.frame(nncross(subset(LNPPR, marks==\"junction\"),subset(LNPPR, marks==\"randsp\"), what = \"dist\", k=seq(from = 1, to = npoints(subset(LNPPR, marks==\"randsp\")), by = 1))  )))",
					"seq(from = 1, to = npoints(subset(LNPPR, marks==\"randsp\"))  , by = 1)", "lpts_dot", "" };
		}
		
		// ok dataframed
		if (f.compareTo(nndist_e_p) == 0) {
			if (param.length != 0) {
				throw new Exception("bad param in : nndist_e_p param :  [null] ");
			}
			return new String[] {
					"lpts_dot <-  as.numeric( colMeans(  data.frame( nncross(subset(LNPP, marks==\"ends\"),     subset(LNPP, marks==\"points\"), what = \"dist\", k=seq(from = 1, to = npoints(subset(LNPP, marks==\"points\")), by = 1))  ) ))",
					"seq(from = 1, to = npoints(subset(LNPP, marks==\"points\"))  , by = 1)", "lpts_dot", "" };
		}
		if (f.compareTo(nndist_e_p_random) == 0) {
			if (param.length != 0) {
				throw new Exception("bad param in : nndist_e_p param :  [null] ");
			}
			return new String[] {
					"lpts_dot <-  as.numeric( colMeans(  data.frame( nncross(subset(LNPPR, marks==\"ends\"),     subset(LNPPR, marks==\"randsp\"), what = \"dist\", k=seq(from = 1, to = npoints(subset(LNPPR, marks==\"randsp\")), by = 1))  ) ))",
					"seq(from = 1, to = npoints(subset(LNPPR, marks==\"randsp\"))  , by = 1)", "lpts_dot", "" };
		}
		
		
		// ok dataframed
		if (f.compareTo(nndist_e_j) == 0) {
			if (param.length != 0) {
				throw new Exception("bad param in : nndist_e_p param :  [null] ");
			}
			return new String[] {
					"lpts_dot <-  as.numeric( colMeans(  data.frame(nncross(subset(LNPP, marks==\"ends\"),     subset(LNPP, marks==\"junction\"), what = \"dist\", k=seq(from = 1, to = npoints(subset(LNPP, marks==\"junction\")), by = 1))   ) ))",
					"seq(from = 1, to = npoints(subset(LNPP, marks==\"junction\"))  , by = 1)", "lpts_dot", "" };
		}
		// ok dataframed
		if (f.compareTo(nndist_j_e) == 0) {
			if (param.length != 0) {
				throw new Exception("bad param in : nndist_e_p param :  [null] ");
			}
			return new String[] {
					"lpts_dot <-  as.numeric( colMeans(  data.frame(nncross(subset(LNPP, marks==\"junction\"),     subset(LNPP, marks==\"ends\"), what = \"dist\", k=seq(from = 1, to = npoints(subset(LNPP, marks==\"ends\")), by = 1))   ) ))",
					"seq(from = 1, to = npoints(subset(LNPP, marks==\"ends\"))  , by = 1)", "lpts_dot", "" };
		}

		// ok dataframed
		if (f.compareTo(nndist_p) == 0) {
			if (param.length != 0) {
				throw new Exception("bad param in : lk_p_j param :  [null] ");
			}
			return new String[] {
					" lpts_dot <- as.numeric(colMeans(  data.frame( nndist(subset(LNPP, marks==\"points\"),k=seq(from = 1, to = npoints(subset(LNPP, marks==\"points\"))-1, by = 1)))))",
					"seq(from = 1, to = npoints(subset(LNPP, marks==\"points\"))-1, by = 1)", "lpts_dot", "" };

		}
		if (f.compareTo(nndist_p_random) == 0) {
			if (param.length != 0) {
				throw new Exception("bad param in : nndist_j_p param :  [null] ");
			}
			return new String[] {
					" lpts_dot <- as.numeric(colMeans(  data.frame( nndist(subset(LNPPR, marks==\"randsp\"),k=seq(from = 1, to = npoints(subset(LNPPR, marks==\"randsp\"))-1, by = 1)))))",
					"seq(from = 1, to = npoints(subset(LNPPR, marks==\"randsp\"))-1, by = 1)", "lpts_dot", "" };
		}
		
		
		
		// ok dataframed
		if (f.compareTo(nndist_j) == 0) {
			if (param.length != 0) {
				throw new Exception("bad param in : lk_p_j param :  [null] ");
			}
			return new String[] {
					" lpts_dot <- as.numeric(colMeans(data.frame(nndist(subset(LNPP, marks==\"junction\"),k=seq(from = 1, to = npoints(subset(LNPP, marks==\"junction\"))-1, by = 1)))))",
					"seq(from = 1, to = npoints(subset(LNPP, marks==\"junction\"))-1, by = 1)", "lpts_dot", "" };
		}
		// ok dataframed
		if (f.compareTo(nndist_e) == 0) {
			if (param.length != 0) {
				throw new Exception("bad param in : lk_p_j param :  [null] ");
			}
			return new String[] {
					" lpts_dot <- as.numeric(colMeans( data.frame( nndist(subset(LNPP, marks==\"ends\"),k=seq(from = 1, to = npoints(subset(LNPP, marks==\"ends\"))-1, by = 1)))))",
					"seq(from = 1, to = npoints(subset(LNPP, marks==\"ends\"))-1, by = 1)", "lpts_dot", "" };
		}
		return null;
	}

	public static boolean isRandomCond(Fname f) throws Exception {

		if (f.compareTo(pcf_p_randCond) == 0) {
			return true;
		}
		if (f.compareTo(pcf_p_e_ranCond) == 0) {
			return true;
		}
		if (f.compareTo(pcf_p_j_randCond) == 0) {
			return true;
		}
		if(f.compareTo(pcf_e_p_randCond)==0) {
			return true;
		}
		if(f.compareTo(pcf_j_p_randCond)==0) {
			return true;
		}
		if (f.compareTo(nndist_j_p_random) == 0) {
			return true;
		}
		if (f.compareTo(nndist_p_random) == 0) {
			return true;
		}
		if (f.compareTo(nndist_e_p_random) == 0) {
			return true;
		}
		return false;
	}

	public static boolean checkSubgraph(Fname f, double nbAcceptedPoints, double nbAcceptedEnds,
			double nbAcceptedJunctions) throws Exception {

		if (f.compareTo(pcf_p) == 0) {
			return nbAcceptedPoints > 1;
		}
		if (f.compareTo(pcf_p_randCond) == 0) {
			return nbAcceptedPoints > 1;
		}
		if (f.compareTo(pcf_e) == 0) {
			return nbAcceptedEnds > 1;
		}
		if (f.compareTo(pcf_j) == 0) {
			return nbAcceptedJunctions > 1;
		}
		if (f.compareTo(pcf_e_inhom) == 0) {
			return nbAcceptedEnds > 1;
		}
		if (f.compareTo(pcf_p_j) == 0) {
			return nbAcceptedJunctions > 0 && nbAcceptedPoints > 0;
		}
		if (f.compareTo(pcf_p_j_randCond) == 0) {
			return nbAcceptedJunctions > 0 && nbAcceptedPoints > 0;
		}
		if (f.compareTo(pcf_p_e) == 0) {
			return nbAcceptedEnds > 0 && nbAcceptedPoints > 0;
		}
		if (f.compareTo(pcf_e_p) == 0) {
			return nbAcceptedEnds > 0 && nbAcceptedPoints > 0;
		}
		if (f.compareTo(pcf_e_p_randCond) == 0) {
			return nbAcceptedEnds > 0 && nbAcceptedPoints > 0;
		}
		
		if (f.compareTo(pcf_j_p) == 0) {
			return nbAcceptedJunctions > 0 && nbAcceptedPoints > 0;
		}
		if (f.compareTo(pcf_j_p_randCond) == 0) {
			return nbAcceptedJunctions > 0 && nbAcceptedPoints > 0;
		}
		
		if (f.compareTo(pcf_p_e_random) == 0) {
			return nbAcceptedEnds > 0 && nbAcceptedPoints > 0;
		}
		if (f.compareTo(pcf_p_e_inhom) == 0) {
			return nbAcceptedEnds > 0 && nbAcceptedPoints > 0;
		}
		if (f.compareTo(pcf_p_e_inhom_random) == 0) {
			return nbAcceptedEnds > 0 && nbAcceptedPoints > 0;
		}
		if (f.compareTo(pcf_p_e_ranCond) == 0) {
			return nbAcceptedEnds > 0 && nbAcceptedPoints > 0;
		}
		if (f.compareTo(pcf_j_e) == 0) {
			return nbAcceptedJunctions > 0 && nbAcceptedEnds > 0;
		}
		if (f.compareTo(lk_p) == 0) {
			return nbAcceptedPoints > 3;
		}
		if (f.compareTo(lk_j) == 0) {
			return nbAcceptedJunctions > 3;
		}
		if (f.compareTo(lk_p_j) == 0 || f.compareTo(lkNN_p_j) == 0) {
			return nbAcceptedJunctions > 3 && nbAcceptedPoints > 3;
		}
		if (f.compareTo(lk_p_e) == 0 || f.compareTo(lkNN_p_e) == 0) {
			return nbAcceptedEnds > 3 && nbAcceptedPoints > 3;
		}
		if (f.compareTo(nndist_a) == 0) {
			return nbAcceptedEnds + nbAcceptedPoints + nbAcceptedJunctions > 1;
		}
		if (f.compareTo(nndist_j_p) == 0) {
			return (nbAcceptedPoints > 0) && (nbAcceptedJunctions > 0);
		}
		if (f.compareTo(nndist_j_p_random) == 0) {
			return (nbAcceptedPoints > 0) && (nbAcceptedJunctions > 0);
		}
		if (f.compareTo(nndist_e_p) == 0) {
			return (nbAcceptedPoints > 0) && (nbAcceptedEnds > 0);
		}
		if (f.compareTo(nndist_e_p_random) == 0) {
			return (nbAcceptedPoints > 0) && (nbAcceptedEnds > 0);
		}
		if (f.compareTo(nndist_p) == 0) {
			return nbAcceptedPoints > 1;
		}
		if (f.compareTo(nndist_p_random) == 0) {
			return nbAcceptedPoints > 1;
		}
		if (f.compareTo(nndist_j) == 0) {
			return nbAcceptedJunctions > 1;
		}
		if (f.compareTo(nndist_e) == 0) {
			return nbAcceptedEnds > 1;
		}
		if (f.compareTo(nndist_j_e) == 0) {
			return nbAcceptedJunctions > 0 && nbAcceptedEnds > 0;
		}
		if (f.compareTo(nndist_e_j) == 0) {
			return nbAcceptedJunctions > 0 && nbAcceptedEnds > 0;
		}

		return false;
	}

	public static boolean isApprox(Fname f) throws Exception {

		if (f.compareTo(pcf_p) == 0) {
			return true;
		}
		if (f.compareTo(pcf_p_randCond) == 0) {
			return true;
		}
		if (f.compareTo(pcf_e) == 0) {
			return true;
		}
		if (f.compareTo(pcf_j) == 0) {
			return true;
		}
		if (f.compareTo(pcf_e_inhom) == 0) {
			return true;
		}
		if (f.compareTo(pcf_p_j) == 0) {
			return true;
		}
		if (f.compareTo(pcf_p_j_randCond) == 0) {
			return true;
		}
		if (f.compareTo(pcf_e_p) == 0) {
			return true;
		}
		if (f.compareTo(pcf_e_p_randCond) == 0) {
			return true;
		}
		
		if (f.compareTo(pcf_j_p) == 0) {
			return true;
		}
		if (f.compareTo(pcf_j_p_randCond) == 0) {
			return true;
		}
		
		
		if (f.compareTo(pcf_p_e) == 0) {
			return true;
		}
		if (f.compareTo(pcf_p_e_random) == 0) {
			return true;
		}
		if (f.compareTo(pcf_p_e_inhom) == 0) {
			return true;
		}
		if (f.compareTo(pcf_p_e_inhom_random) == 0) {
			return true;
		}
		if (f.compareTo(pcf_p_e_ranCond) == 0) {
			return true;
		}
		if (f.compareTo(pcf_j_e) == 0) {
			return true;
		}
		if (f.compareTo(lk_p) == 0) {
			return true;
		}
		if (f.compareTo(lk_j) == 0) {
			return true;
		}
		if (f.compareTo(lk_p_j) == 0 || f.compareTo(lkNN_p_j) == 0) {
			return true;
		}
		if (f.compareTo(lk_p_e) == 0 || f.compareTo(lkNN_p_e) == 0) {
			return true;
		}
		if (f.compareTo(nndist_a) == 0) {
			return false;
		}
		if (f.compareTo(nndist_p) == 0) {
			return false;
		}
		if (f.compareTo(nndist_p_random) == 0) {
			return false;
		}
		if (f.compareTo(nndist_j) == 0) {
			return false;
		}
		if (f.compareTo(nndist_e) == 0) {
			return false;
		}
		if (f.compareTo(nndist_j_p) == 0) {
			return false;
		}
		if (f.compareTo(nndist_j_p_random) == 0) {
			return false;
		}
		if (f.compareTo(nndist_e_p) == 0) {
			return false;
		}
		if (f.compareTo(nndist_e_p_random) == 0) {
			return false;
		}
		if (f.compareTo(nndist_j_e) == 0) {
			return false;
		}
		if (f.compareTo(nndist_e_j) == 0) {
			return false;
		}
		return false;
	}

	public static Subgraph join(Fname f, ArrayList<Subgraph> subList, Subgraph output) {

		double[] x = new double[MAXLENGTH];
		double[] y = new double[MAXLENGTH];

		if (subList.size() > 1) {

			if (f == pcf_p || f == pcf_p_randCond || f == pcf_e || f == pcf_j || f == lk_p || f == lk_j) {
				for (int i = 0; i < MAXLENGTH; i++) {
					x[i] = i;
					double ltot = 0;
					double nLiaison = 0;

					for (Subgraph sd : subList) {
						if (sd != null && sd.getFunctionList().containsKey(f)) {
							double l = sd.getLengthSubgraph();
							double n = 0;
							if (f == pcf_p || f == lk_p || f == pcf_p_randCond) {
								n = sd.nbAcceptedPoints;
							}
							if (f == pcf_e) {
								n = sd.nbAcceptedEnds;
							}
							if (f == pcf_j || f == lk_j) {
								n = sd.nbAcceptedJunctions;
							}

							double val = sd.getFunctionList().get(f).value[i];
							if (!Double.isNaN(val)&&val!=0) {
								y[i] += val * n * (n - 1) / l;
								ltot += l;
								nLiaison += n;

							}
						}
					}

					if (nLiaison != 0) {
						y[i] = ltot * y[i] / (nLiaison * (nLiaison - 1));
					} else {
						y[i] = 0;
					}
				}

				// System.out.println(Arrays.toString(y));
			}

			if (f == pcf_e_inhom) {
				for (int i = 0; i < MAXLENGTH; i++) {
					x[i] = i;
					double sumLambdaTot = 0;

					for (Subgraph sd : subList) {
						if (sd != null && sd.getFunctionList().containsKey(f)) {

							double sd1OnLambda = sd.sum_1_lambda;
							sumLambdaTot += sd1OnLambda;
							double val = sd.getFunctionList().get(f).value[i];
							if (!Double.isNaN(val) && !Double.isInfinite(sd1OnLambda) && !Double.isNaN(sd1OnLambda)) {
								y[i] += val * sd1OnLambda;
								System.out.println(sd1OnLambda);
							}

						}
					}

					if (sumLambdaTot != 0) {
						y[i] = y[i] / sumLambdaTot;
						System.out.println("SUM LAMBDA TOT");
					} else {
						y[i] = 0;
					}
				}

				// System.out.println(Arrays.toString(y));
			}

			if (f == pcf_p_j || f == pcf_p_j_randCond || f == pcf_p_e || f == pcf_e_p || f == pcf_j_p  || f == pcf_j_e || f == lk_p_j || f == lk_p_e
					|| f == pcf_p_e_inhom || f == pcf_p_e_inhom_random || f == pcf_p_e_random ||  f == pcf_e_p_randCond || f == pcf_p_e_ranCond|| f == pcf_j_p_randCond) {
				for (int i = 0; i < MAXLENGTH; i++) {
					x[i] = i;
					double ltot = 0;
					double nLiaison1 = 0;
					double nLiaison2 = 0;
					for (Subgraph sd : subList) {
						if (sd != null && sd.getFunctionList().containsKey(f)) {
							double l = sd.getLengthSubgraph();
							double n1 = 0;
							double n2 = 0;
							if (f == pcf_p_j || f == lk_p_j || f == pcf_p_j_randCond || f == pcf_j_p || f == pcf_j_p_randCond ) {
								n1 = sd.nbAcceptedPoints;
								n2 = sd.nbAcceptedJunctions;
							}
							if (f == pcf_p_e || f == pcf_e_p || f == lk_p_e || f == pcf_p_e_inhom || f == pcf_p_e_inhom_random
									|| f == pcf_p_e_random || f == pcf_p_e_ranCond || f == pcf_e_p_randCond) {
								n1 = sd.nbAcceptedPoints;
								n2 = sd.nbAcceptedEnds;
							}
							if (f == pcf_j_e) {
								n1 = sd.nbAcceptedJunctions;
								n2 = sd.nbAcceptedEnds;
							}
							double val = sd.getFunctionList().get(f).value[i];
							if (!Double.isNaN(val)&&val!=0) {
								y[i] += val * n1 * (n2) / l;
								ltot += l;
								nLiaison1 += n1;
								nLiaison2 += n2;
							}
						}
					}

					if (nLiaison1 != 0 && nLiaison2 != 0) {
						y[i] = ltot * y[i] / (nLiaison1 * (nLiaison2));
					} else {
						y[i] = 0;
					}

				}
			}
			if (f == lkNN_p_j || f == lkNN_p_e) {
				for (int i = 0; i < MAXLENGTH; i++) {
					x[i] = i;
					double n = 0;
					double nLiaison1 = 0;
					double nLiaison2 = 0;
					for (Subgraph sd : subList) {
						if (sd != null && sd.getFunctionList().containsKey(f)) {
							double l = sd.getLengthSubgraph();
							double n1 = 0;
							double n2 = 0;
							if (f == lkNN_p_j) {
								n1 = sd.nbAcceptedPoints;
								n2 = sd.nbAcceptedJunctions;
							}
							if (f == lkNN_p_e) {
								n1 = sd.nbAcceptedPoints;
								n2 = sd.nbAcceptedEnds;
							}
							double val = sd.getFunctionList().get(f).value[i];
							if (!Double.isNaN(val)) {
								y[i] += val * n1 * (n2) / l;
								n += 1;
								nLiaison1 += n1;
								nLiaison2 += n2;
							}
						}
					}

					if (nLiaison1 != 0 && nLiaison2 != 0) {
						y[i] = y[i] / (n);
					} else {
						y[i] = 0;
					}

				}

			}
		
			
			if (f == nndist_a || f == nndist_p || f == nndist_j || f == nndist_e || f == nndist_j_p || f == nndist_e_p
					|| f == nndist_e_j || f == nndist_j_e ||f == nndist_j_p_random||f == nndist_p_random || f == nndist_e_p_random) {
				for (int i = 0; i < MAXLENGTH; i++) {
					x[i] = i;
					double ntot = 0;
					for (Subgraph sd : subList) {
						if (sd != null && sd.getFunctionList().containsKey(f)) {
							double n = 0;
							if (f == nndist_a) {
								n = sd.getNbAcceptedEnds() + sd.getNbAcceptedJunctions() + sd.getNbAcceptedPoints();
							}
							if (f == nndist_p) {
								n = sd.getNbAcceptedPoints();
							}
							if (f == nndist_p_random) {
								n = sd.getNbAcceptedPoints();
							}
							if (f == nndist_j) {
								n = sd.getNbAcceptedJunctions();
							}
							if (f == nndist_e) {
								n = sd.getNbAcceptedEnds();
							}
							if (f == nndist_j_p) {
								n = sd.getNbAcceptedJunctions();
							}
							if (f == nndist_j_p_random) {
								n = sd.getNbAcceptedJunctions();
							}
							if (f == nndist_e_p) {
								n = sd.getNbAcceptedEnds();
							}
							if (f == nndist_e_p_random) {
								n = sd.getNbAcceptedEnds();
							}
							if (f == nndist_j_e) {
								n = sd.getNbAcceptedJunctions();
							}
							if (f == nndist_e_j) {
								n = sd.getNbAcceptedEnds();
							}
							if (sd.getFunctionList().get(f).value.length > i) {
								double val = sd.getFunctionList().get(f).value[i];
								if (!Double.isNaN(val) && !Double.isInfinite(val)) {
									y[i] += val * n;
									ntot += n;
								}
							}
						}
					}
					if (ntot != 0) {
						y[i] = y[i] / ntot;
					} else {
						y[i] = 0;
					}
				}
			}

			Function func = new Function(f, x, y);
			output.functionList.put(f, func);
		}

		else {
			for (int i = 0; i < MAXLENGTH; i++) {
				if (subList.get(0) != null && subList.get(0).getFunctionList().containsKey(f)) {
					x[i] = i;
					y[i] = subList.get(0).getFunctionList().get(f).value[i];
				}
			}

			Function func = new Function(f, x, y);
			output.functionList.put(f, func);
		}
		return output;
	}

}
