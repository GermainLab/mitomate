import java.util.ArrayList;

import util.Consolidation;

public class Main_Consolidation {

	public static void main(String[] args) {

		boolean ConsolidateNewCollumn = true;
		
		String celnum = "4_1sra";
		String mainPath = "/Users/marc 1/Desktop/pcf_points fixed/DRP1_" + celnum + "_TOM20_" + celnum;
		//String mainPath = "/Users/marc 1/Desktop/Test/Test/DRP1_50_2_TOM20_50_2";
		String finalPath_nndist= mainPath + "/nndist.csv";	
		String finalPath_pure= mainPath + "/pure.csv";	
		String finalPath_p= mainPath + "/pcf_p_randcond.csv";	
		String finalPath_p_e_random= mainPath +"/pcf_p_e_randcond.csv";	
		//String finalPath_p_e= mainPath +"/pcf_p_e.csv";	
		String finalPath_p_j_random= mainPath +"/pcf_p_j_randcond.csv";	
		String finalPath_e_p_random = mainPath + "/pcf_e_p_randcond.csv";
		String finalPath_j_p_random = mainPath + "/pcf_j_p_randcond.csv";
		
		String finalPath_nndist_j_p_random= mainPath +"/nndist_j_p_randcond.csv";
		String finalPath_nndist_p_random= mainPath +"/nndist_p_randcond.csv";
		String finalPath_nndist_e_p_random= mainPath +"/nndist_e_p_randcond.csv";
		
		
		String resE = "resE.csv";
		String resB = "resB.csv";
		
		ArrayList<Integer> columnList = new ArrayList<Integer>();
		columnList.add(0);
		columnList.add(1);
		columnList.add(3);
		columnList.add(5);
		columnList.add(7);
		columnList.add(9);
		columnList.add(11);
		columnList.add(13);
		columnList.add(15);
		columnList.add(17);
		columnList.add(19);
		columnList.add(21);
		ArrayList<Integer> columnListResB = new ArrayList<Integer>();
		columnListResB.add(0);
		columnListResB.add(1);
		columnListResB.add(5);
		columnListResB.add(7);
		columnListResB.add(9);
		columnListResB.add(13);
		columnListResB.add(17);
		
		if (ConsolidateNewCollumn) {
			columnListResB.add(19);
			columnListResB.add(21);
			columnListResB.add(23);
			columnListResB.add(25);
		}
		Consolidation.ConsolidateColumn(mainPath,finalPath_nndist ,resE , columnList);
		Consolidation.ConsolidateColumn(mainPath,finalPath_pure ,resB , columnListResB);
		Consolidation.Consolidate2Column(mainPath,finalPath_p ,resB , 1,3);
		Consolidation.Consolidate2Column(mainPath,finalPath_p_j_random ,resB , 9,11);
		Consolidation.Consolidate2Column(mainPath,finalPath_p_e_random ,resB , 13,15);
		
		if (ConsolidateNewCollumn) {
			Consolidation.Consolidate2Column(mainPath, finalPath_e_p_random, resB, 19, 21);
			Consolidation.Consolidate2Column(mainPath, finalPath_j_p_random, resB, 23, 25);
		}
		Consolidation.Consolidate2Column(mainPath,finalPath_nndist_j_p_random ,resE , 9,17);
		Consolidation.Consolidate2Column(mainPath,finalPath_nndist_p_random ,resE , 7,19);
		Consolidation.Consolidate2Column(mainPath,finalPath_nndist_e_p_random ,resE , 11,21);
		
		
	}

}
