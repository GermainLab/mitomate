import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import util.Printer;

public class Merger {

	public static void main(String[] args) {
		String mainPath = "C:\\Users\\Administrateur\\Desktop\\Mef-mfn-done\\";
		String mathching = "resB"; 

		
ArrayList<String> files = Printer.ImagesFromMainPath(mainPath, mathching);
		
		try {
			Printer.collapse(files, mainPath+File.separator+"resB.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mathching = "resE";
		
		ArrayList<String> files2 = Printer.ImagesFromMainPath(mainPath, mathching);
		
		try {
			Printer.collapse(files2, mainPath+File.separator+"resA.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mathching = "param";
		
		ArrayList<String> files3 = Printer.ImagesFromMainPath(mainPath, mathching);
		
		try {
			Printer.collapse(files3, mainPath+File.separator+"param.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
