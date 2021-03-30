package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Consolidation {

	public static void FindFile(String mainPath, String contain, ArrayList<String> fileFound) {

		File file = new File(mainPath);

		// if it's a directory
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				FindFile(f.getAbsolutePath(), contain, fileFound);
			}
		} else {
			if (file.getName().contains(contain) && file.getName().contains(".csv") && !file.getName().contains("nbEnd")) {
				fileFound.add(file.getAbsolutePath());
				System.out.println(file.getAbsolutePath());
			}
		}

	}

	public static void ConsolidateColumn(String mainPath, String finalPath, String contain,
			ArrayList<Integer> columnList) {

		ArrayList<String> fileFound = new ArrayList<String>();
		FindFile(mainPath, contain, fileFound);
		
		System.out.println("start here");
		for(String s : fileFound) {
			System.out.println(s);
		}
		System.out.println("end");
		ConsolidateData(fileFound, columnList, finalPath);

	}

	public static void ConsolidateData(ArrayList<String> filePathList, ArrayList<Integer> columnList,
			String finalPath) {

		// COLUMN , FILE, ROW
		ArrayList<ArrayList<ArrayList<Double>>> data = new ArrayList<ArrayList<ArrayList<Double>>>();
		ArrayList<String> columnTitle = new ArrayList<String>();
		ArrayList<double[]> mean = new ArrayList<double[]>();
		ArrayList<double[]> nbFile = new ArrayList<double[]>();
		boolean firstFile = true;

		for (String filecsv : filePathList) {

			System.out.println(filecsv);

			ArrayList<ArrayList<Double>> fileArray = new ArrayList<ArrayList<Double>>();
			for (int i = 0; i < columnList.size(); i++) {
				fileArray.add(new ArrayList<Double>());
			}

			System.out.println(columnList.size());
			System.out.println(columnList.size());
			System.out.println(columnList.size());
			System.out.println(columnList.size());

			try {
				BufferedReader br = new BufferedReader(new FileReader(filecsv));
				String line = br.readLine();
				String[] parts = line.split(";");

				System.out.println(line);

				if (firstFile) {
					for (Integer column : columnList) {
						System.out.println(column);
						columnTitle.add(parts[column]);
					}
					firstFile = false;
				}

				line = br.readLine();
				while (line != null) {
					parts = line.split(";");

					int colNo = 0;
					for (Integer column : columnList) {
						boolean done = false;

						if (Double.parseDouble(parts[column]) != 0) {
							if (!Double.isNaN(Double.parseDouble(parts[column]))) {
								fileArray.get(colNo).add(Double.parseDouble(parts[column]));
								done = true;
							}
						}
						if (!done) {
							fileArray.get(colNo).add(Double.NaN);
						}
						colNo++;
					}
					line = br.readLine();
				}
				data.add(fileArray);
				br.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		int length = data.get(0).get(0).size();

		for (int i = 0; i < columnList.size(); i++) {
			mean.add(new double[length]);
			nbFile.add(new double[length]);
		}

		for (ArrayList<ArrayList<Double>> fileData : data) {

			int colNo = 0;
			for (int c = 0; c < columnList.size(); c++) {
				for (int i = 0; i < fileData.get(colNo).size(); i++) {
					if (!Double.isNaN(fileData.get(colNo).get(i))) {
						mean.get(colNo)[i] += fileData.get(colNo).get(i);
						nbFile.get(colNo)[i] += 1;
					}
				}
				colNo++;
			}
		}

		BufferedWriter writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(finalPath), "utf-8"));

			for (String col : columnTitle) {
				writer.write(col + " ;");
			}
			writer.write("\n");

			for (int i = 0; i < mean.get(0).length; i++) {
				for (int co = 0; co < columnTitle.size(); co++) {
					System.out.println(mean.size());
					double value = mean.get(co)[i] / nbFile.get(co)[i];
					writer.write(value + " ;");
				}
				writer.write("\n");
			}

			writer.close();

		} catch (IOException ex) {
			// Report
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
				/* ignore */}
		}

	}

	
	public static void Consolidate2Column(String mainPath, String finalPath, String contain,
			 int column1, int column2) {

		ArrayList<String> fileFound = new ArrayList<String>();
		FindFile(mainPath, contain, fileFound);
		ConsolidateData2Column(fileFound,   column1,  column2, finalPath);

	}
	
	
	public static void ConsolidateData2Column(ArrayList<String> filePathList, int column1, int column2,
			String finalPath) {

		ArrayList<Integer> columnList = new ArrayList<Integer>();
		columnList.add(column1);
		columnList.add(column2);

		// COLUMN , FILE, ROW
		ArrayList<ArrayList<ArrayList<Double>>> data = new ArrayList<ArrayList<ArrayList<Double>>>();
		ArrayList<String> columnTitle = new ArrayList<String>();
		boolean firstFile = true;

		for (String filecsv : filePathList) {

			System.out.println(filecsv);

			ArrayList<ArrayList<Double>> fileArray = new ArrayList<ArrayList<Double>>();
			fileArray.add(new ArrayList<Double>());
			fileArray.add(new ArrayList<Double>());

			try {
				BufferedReader br = new BufferedReader(new FileReader(filecsv));
				String line = br.readLine();
				String[] parts = line.split(";");

				System.out.println(line);

				if (firstFile) {
					columnTitle.add(parts[column1]);
					columnTitle.add(parts[column2]);
					firstFile = false;
				}

				line = br.readLine();
				while (line != null) {
					parts = line.split(";");

					int colNo = 0;
					for (Integer column : columnList) {
						boolean done = false;

						if (Double.parseDouble(parts[column]) != 0) {
							if (!Double.isNaN(Double.parseDouble(parts[column]))) {
								fileArray.get(colNo).add(Double.parseDouble(parts[column]));
								done = true;
							}
						}
						if (!done) {
							fileArray.get(colNo).add(Double.NaN);
						}
						colNo++;
					}
					line = br.readLine();
				}
				data.add(fileArray);
				br.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		BufferedWriter writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(finalPath), "utf-8"));

			for (int i = 0; i < data.size(); i++) {
				for (String col : columnTitle) {
					writer.write(col + " ;");
				}
				writer.write(  " ratio  ;");
			}
			writer.write("\n");

			for (int i = 0; i < data.get(0).get(0).size(); i++) {

				for (int f = 0; f < data.size(); f++) {
					
					writer.write(data.get(f).get(0).get(i)+ " ; ");
					writer.write(data.get(f).get(1).get(i)+ " ; ");
					writer.write( " ; ");
				}
				
				writer.write("\n");
			}

			writer.close();

		} catch (IOException ex) {
			// Report
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
				/* ignore */}
		}

	}
}
