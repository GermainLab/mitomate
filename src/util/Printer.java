package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import data.Fname;
import data.Subgraph;

public class Printer {

	public static void print(Subgraph sub, String filepath, Fname[] toPrint) {
		String firstRow = "";
		for (Fname f : toPrint) {
			firstRow += " r  ; " + f.toString() + "  ;  ";
		}
		String[][] out = new String[2 * toPrint.length][Fname.MAXLENGTH];

		for (int i = 0; i < toPrint.length; i++) {
			for (int j = 0; j < Fname.MAXLENGTH; j++) {
				out[2 * i][j] = sub.getFunctionList().get(toPrint[i]).r[j] + "";
				out[2 * i + 1][j] = sub.getFunctionList().get(toPrint[i]).value[j] + "";
			}
		}

		try {
			FileWriter writer4 = new FileWriter(new File(filepath));
			writer4.append(firstRow);
			writer4.append("\n");
			for (int j = 0; j < Fname.MAXLENGTH; j++) {
				for (int i = 0; i < toPrint.length; i++) {
					writer4.append(out[2 * i][j] + " ; " + out[2 * i + 1][j] + " ; ");
				}
				writer4.append("\n");
			}
			writer4.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void printSubgraphInfo(ArrayList<Subgraph> sub, String filepath, double maxAnalDist, double radius,
			double factorQuality, double thresholdQuality, boolean usecondition, double distanceThreshold,
			boolean lessThan) throws IOException {

		double nbAcceptedPoints_graph = 0;
		double nbAcceptedEnds_graph = 0;
		double nbAcceptedJunctions_graph = 0;
		double RatioAcceptedPoin_graph = 0;

		double totalLength = 0;

		double point_length;
		double point_sub;
		double junction_length;

		for (Subgraph s : sub) {
			nbAcceptedPoints_graph += s.getNbAcceptedPoints();
			nbAcceptedEnds_graph += s.getNbAcceptedEnds();
			nbAcceptedJunctions_graph += s.getNbAcceptedJunctions();
			RatioAcceptedPoin_graph += s.getRatioAcceptedPoint();
			totalLength += s.getLengthSubgraph();
		}

		//point_sub = nbAcceptedPoints_graph / ((double) sub.size());
		//nbAcceptedPoints_graph = nbAcceptedPoints_graph / ((double) sub.size());
		//nbAcceptedEnds_graph = nbAcceptedEnds_graph / ((double) sub.size());
		//nbAcceptedJunctions_graph = nbAcceptedJunctions_graph / ((double) sub.size());
		RatioAcceptedPoin_graph = RatioAcceptedPoin_graph / ((double) sub.size());
		point_length = nbAcceptedPoints_graph / totalLength;
		junction_length = nbAcceptedJunctions_graph / totalLength;

		FileWriter writer4 = new FileWriter(new File(filepath + "param.csv"));
		writer4.append(
				"sub_nb  ;  Ok_pts  ;  end ;  junc  ;  ratio_ok_pts  ;  pts_length  ;  junction_length ; total length ; maxAnalDist ; radius ; factorQuality ; thresholdQuality ; usecondition ; distanceThreshold ; lessThan ;  \n");
		writer4.append(sub.size()  + " ; " + nbAcceptedPoints_graph + " ; " + nbAcceptedEnds_graph
				+ " ; " + nbAcceptedJunctions_graph + " ; " + RatioAcceptedPoin_graph + " ; " + point_length + " ; "
				+ junction_length + " ; " + totalLength + " ; " + maxAnalDist + " ; " + radius + " ; "+ factorQuality + " ; " + thresholdQuality + " ; " +usecondition + " ; " + distanceThreshold+ " ; " + lessThan);
		writer4.close();
	}

	public static void collapse(ArrayList<String> inputPath, String outputPath) throws IOException {

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ";";

		ArrayList<String[][]> dataList = new ArrayList<String[][]>();

		for (int i = 0; i < inputPath.size(); i++) {
			ArrayList<ArrayList<String>> fileData = new ArrayList<ArrayList<String>>();
			try {
				br = new BufferedReader(new FileReader(inputPath.get(i)));
				while ((line = br.readLine()) != null) {
					ArrayList<String> lineList = new ArrayList<String>();
					// use comma as separator
					String[] l = line.split(cvsSplitBy);
					for (String s : l) {
						lineList.add(s);
					}
					fileData.add(lineList);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			int heigth = fileData.size();
			int wide = fileData.get(0).size();
			String[][] data = new String[heigth][wide];
			for (int h = 0; h < heigth; h++) {
				for (int w = 0; w < wide; w++) {
					data[h][w] = fileData.get(h).get(w);
				}
			}
			dataList.add(data);
		}

		int heigth = dataList.get(0).length;
		int wide = dataList.get(0)[0].length;
		double[][] dat = new double[heigth][wide];
		double[][] nb = new double[heigth][wide];

		for (String[][] slist : dataList) {
			for (int i = 1; i < heigth; i++) {
				for (int j = 0; j < wide; j++) {

					try {
						double val = Double.valueOf(slist[i][j]);
						if (val != 0) {
							dat[i][j] += val;
							nb[i][j] += 1;
						}
					} catch (java.lang.NumberFormatException e) {
						System.out.println("Problem with :" + slist[i][j]);
					}
				}
			}
		}
		for (int i = 1; i < heigth; i++) {
			for (int j = 0; j < wide; j++) {
				dat[i][j] = dat[i][j] / nb[i][j];
			}
		}

		FileWriter writer4 = new FileWriter(new File(outputPath));
		for (int i = 0; i < dat.length; i++) {
			if (i == 0) {
				for (int j = 0; j < dat[0].length; j++) {
					writer4.append(dataList.get(0)[0][j] + ";");
				}
			} else {
				for (int j = 0; j < dat[0].length; j++) {
					writer4.append(dat[i][j] + ";");
				}
			}
			writer4.append("\n");
		}

		writer4.close();
	}

	public static ArrayList<String> ImagesFromMainPath(String mainPath, String matching) {

		ArrayList<String> fileToProcess = new ArrayList<String>();

		File folder = new File(mainPath);

		for (File f : folder.listFiles()) {

			if (f.isDirectory()) {
				for (File g : f.listFiles()) {
					if (g.getName().contains(matching)) {
						fileToProcess.add(g.getPath());
					}
				}
			}
		}

		return fileToProcess;

	}

}
