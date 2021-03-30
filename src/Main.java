import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import data.Data;
import data.Fname;
import data.Selector;
import data.Subgraph;
import fiji.plugin.trackmate.SpotCollection;
import graph.CompleteGraphAnalyzer;
import importer.GraphImporter;
import importer.ImagesImporter;
import importer.PointImporter;
import importer.SettingPoint;
import rServe.Connection;
import rServe.InvokeRserve;
import util.Drawer;
import util.Printer;

public class Main {

	public static void main(String[] args) {
		
		Selector fileType;
		fileType= Selector.normal;
		//Selector.normal; Selector.gerald
		
		
		//distance maximale du squelette en pixel acceptÈ 
				double maxAnalDist = 6;
				//trackmate rayon de la boulle en pixel (3px)
				double radius = 2;
				//double radius = 1;
				//facteur multiplicatif de la qualitÈ (ex: 0.75 prend plus de point) 
				double factorQuality = 0.5;
				//pour aller plus vite il doit Ítre sous le threshold de qualitÈ 
				double thresholdQuality = 0.5;
				//(Bandwith) size of the gaussian convolution
				int bandwith = 5; 
				

				String[] pathTODO = new String[] { "/Users/marc 1/Desktop/Test/Test/" };
				
				//si on utilise une condition pour prendre certain points du rÈseau (true or false)
				boolean conditionEnd = false;
				//distance utilisÈe pour la sÈlection des points (RADIUS Not on the network)
				double distanceThresholdEnd =5;
				//If true keep point less than the treshold distance if false take the point with higher distance 
				boolean lessThanEnd = true;
				
				//number of time the prunning operator is applied
				int NBPRUNNING = 0;
				//Minimum length of branch allowed 
				int MAXLENGTH = 0;
				
				
				//Condition distance a une jonction 
				boolean conditionJunction = false;
				//distance utilisÈe pour la sÈlection des points
				double distanceThresholdJunction =5;
				//If true keep point less than the treshold distance if false take the point with higher distance (RADIUS Not on the network)
				boolean lessThanJunction = true;
				
				//Remove random point with probability removeProbability if true
				boolean removePoint = false; 
				//probability to remove a point between 0 and 1 (1= remove all points) 
				double probabilityRemovePoint = 0.63; 

				//print all subgragh
				boolean printAllsubgraph = false;
				

				//do we take the real dots (NEVER CHANGE IT)
				boolean TOTALRANDOM = false;
				double pointIntensity = 0.018;
		
				String[] param = new String[] { "5" };

		
		
		for (String mainPath : pathTODO) {

			// String mainPath =
			// "C://Users//Administrateur//Desktop//TOM20//TOM20-DRP1//08012018//DMEM";

			ArrayList<String[]> file = ImagesImporter.ImagesFromMainPath(mainPath,fileType);
			System.out.println(file.size());

			for (String[] string : file) {

				try {
					InvokeRserve.invoke();

					System.out.println("invocation done");
					Connection conn = new Connection();

					System.out.println(string[0] + "  ,  " + string[1]);
					String folderpath = mainPath + File.separator + string[2];
					boolean dire = new File(folderpath).mkdir();

					System.out.println(dire);

					CompleteGraphAnalyzer gI = null;

					try {
						gI = GraphImporter.importGraphTest(string, folderpath + File.separator);
					} catch (Exception e) {
						e.printStackTrace();
					}

					SettingPoint set = new SettingPoint();
					set.autoQuality = true;
					set.radius = radius;
					set.threshold = thresholdQuality;

					SpotCollection spotCol = PointImporter.importPoint(string, set, folderpath + File.separator,
							factorQuality);
					Data dat;

					
					if (TOTALRANDOM) {
						dat = new Data(pointIntensity, gI, maxAnalDist);
					} else {
						dat = new Data(spotCol, gI, maxAnalDist, conditionEnd, distanceThresholdEnd, lessThanEnd, conditionJunction, distanceThresholdJunction, lessThanJunction, removePoint, probabilityRemovePoint );

					}
					if(true) {
					Drawer.drawPoint(dat.getSubgraphList(), dat.getPointList(), 5000, folderpath + File.separator,
							dat.getGraphModel().getCompleteGraph(), maxAnalDist);
					}
										
					Fname[] paramFname = new Fname[] { Fname.pcf_p, Fname.pcf_p_randCond, Fname.pcf_e,
							 Fname.pcf_j, Fname.pcf_p_j, Fname.pcf_p_j_randCond, Fname.pcf_p_e,
							Fname.pcf_p_e_ranCond, Fname.pcf_j_e, Fname.pcf_e_p, Fname.pcf_e_p_randCond , Fname.pcf_j_p, Fname.pcf_j_p_randCond };

					String[] emptyString = new String[] {};
					Fname[] emptyFname = new Fname[] { Fname.nndist_a, Fname.nndist_e, Fname.nndist_j, Fname.nndist_p,
							Fname.nndist_j_p, Fname.nndist_e_p, Fname.nndist_j_e, Fname.nndist_e_j, Fname.nndist_j_p_random,
							 Fname.nndist_p_random, Fname.nndist_e_p_random };
					
					boolean fail = false;

					// pcf_p_j_randCond
					for (Subgraph s : dat.getSubgraphList()) {
						try {
							s.sendGraphToR(conn, false);
							s.computePointSelection(conn);
							
							for (Fname f : paramFname) {

								System.out.println(f.toString());
								s.compute(f, param, conn, conditionEnd, distanceThresholdEnd, lessThanEnd);
								System.out.println(f.toString());

							}
							for (Fname f : emptyFname) {
								
							
								System.out.println(f.toString());
								s.compute(f, emptyString, conn, conditionEnd, distanceThresholdEnd, lessThanEnd);
								System.out.println(f.toString());
							}

						} catch (Exception e) {
							fail = true;
							break;
						}
					}
					if (!fail) {
						Subgraph completeGraph = new Subgraph();
						for (Fname f : paramFname) {
							Fname.join(f, dat.getSubgraphList(), completeGraph);
						}
						for (Fname f : emptyFname) {
							Fname.join(f, dat.getSubgraphList(), completeGraph);
						}
	
						
						try {
							System.out.print("in printer");
							Printer.print(completeGraph, folderpath + File.separator + "resB.csv", paramFname);
							Printer.print(completeGraph, folderpath + File.separator + "resE.csv", emptyFname);
							Printer.printSubgraphInfo(dat.getSubgraphList(), folderpath + File.separator, maxAnalDist, radius, factorQuality, thresholdQuality, conditionEnd, distanceThresholdEnd, lessThanEnd);
							if (printAllsubgraph) {
								int k = 0;
								for (Subgraph s : dat.getSubgraphList()) {

									System.out.println("here");
									Subgraph graph = new Subgraph();
									ArrayList<Subgraph> sub = new ArrayList<Subgraph>();
									sub.add(s);
									for (Fname f : paramFname) {
										Fname.join(f, sub, graph);
									}
									Printer.print(graph, folderpath + File.separator + "resB" + k + "_nbEnd="
											+ s.getNbAcceptedEnds() + ".csv", paramFname);
								
									k++;

								}
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					continue;
				}

			}
		}
		}
	}
