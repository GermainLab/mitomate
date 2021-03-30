package importer;

import java.io.File;
import java.util.ArrayList;

import data.Selector;
  
public class ImagesImporter {

	public static ArrayList<String[]> ImagesFromMainPath(String mainPath, Selector fileType) {

		ArrayList<String[]> fileToProcess = new ArrayList<String[]>();

		File folder = new File(mainPath);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {

			for (int j = 0; j < listOfFiles.length; j++) {

				if (listOfFiles[i].isFile() && listOfFiles[j].isFile()) {
					String[] list = null;

					if (fileType.equals(Selector.normal)) {
						list = linkedNormal(listOfFiles[i].getPath(), listOfFiles[j].getPath());
					}
					if(fileType.equals(Selector.gerald)) {
						list = linkedGerald(listOfFiles[i].getPath(), listOfFiles[j].getPath());

					}
					else {
						list = linkedNormal(listOfFiles[i].getPath(), listOfFiles[j].getPath());
					}
					if (list != null) {
						fileToProcess.add(list);
					}
				}
			}

		}

		return fileToProcess;

	}


	private static String[] linkedGerald(String filepath1, String filepath2) {
		String[] part1 = filepath1.split("_");
		String[] part2 = filepath2.split("_");
		String[] out = null;
		System.out.println(filepath1);

	
		if(part2.length>3&&part2[1].compareTo("T20")==0) {
			if(part1.length>3&&part1[1].compareTo("DRP1")==0) {
				System.out.println("kjsdfghsukfh");
				System.out.println(part1[3]);
				System.out.println(part2[3]);
				
				if(part1[3].split("\\.").length>1&&part2[3].compareTo(part1[3].split("\\.")[0])==0) {
					System.out.println("here3");
					if(filepath2.contains("autoBinned")) {
						out = new String[3];
						out[0] = filepath2;
						out[1] = filepath1;
						out[2] = part1[1] + "_" + part1[2] + "_" + part1[3] + "_" + part2[1] + "_"
								+ part2[2] + "_" + part2[3];
					}
					
				}
				
			}
		}
		
		
		return out;
	}

	public static String[] linkedNormal(String filepath1, String filepath2) {

		String[] part1 = filepath1.split("-");
		String[] part2 = filepath2.split("-");
		String[] out = null;

		System.out.println(filepath1);
		System.out.println(filepath2);
		if (part1.length >= 2 && part2.length >= 2 && part1[4].compareTo(part2[4]) == 0) {

			if (part1[5].contains(".") && part1[3].compareTo("DRP1") == 0) {

				if (part2[3].compareTo("TOM20") == 0) {
					if (part1[5].split("\\.")[0].compareTo(part2[5].split("_")[0]) == 0) {
						System.out.println(part1[5] + "   ,   " + part2[5]);
						System.out.println(part1[3] + "   ,   " + part2[3]);

						out = new String[3];
						out[0] = filepath2;
						out[1] = filepath1;
						out[2] = part1[3] + "_" + part1[4] + "_" + part1[5].split("\\.")[0] + "_" + part2[3] + "_"
								+ part2[4] + "_" + part2[5].split("_")[0];

					}
				}
			}
		}

		return out;
	}

}
