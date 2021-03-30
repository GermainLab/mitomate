package util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import data.Subgraph;
import elementDescriptor.SpatStatNetworkDescriptionBlock;
import fiji.plugin.trackmate.Spot;
import graph.CompleteGraphAnalyzer;

public class Drawer {

	public static void drawPoint(ArrayList<Subgraph> subgraphList, ArrayList<Spot> pointList, int size, String path,
			CompleteGraphAnalyzer cga, double DISTANCETHRESHOLD) {

		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

		Graphics2D g = img.createGraphics();

		// CompleteGraphAnalyzer cga = GraphImporter.importGraphTest(mitoPath);
		// for (double[] pos : sNetwork.getPosList())
		for (Subgraph sub : subgraphList) {
			g.setColor(Color.getHSBColor((float) Math.random(), (float) Math.random(), (float) Math.random()));
			SpatStatNetworkDescriptionBlock sNetwork = sub.getsNetwork();
			for (double[] pos : sNetwork.getPosList()) {
				g.drawRect((int) pos[0], (int) pos[1], 1, 1);
			}

		}
		for (Subgraph sub : subgraphList) {

			for (Spot ends : sub.getEnds()) {
				g.setColor(Color.YELLOW);
				g.drawRect((int) ends.getDoublePosition(0), (int) ends.getDoublePosition(1), 1, 1);
			}
			for (Spot ends : sub.getJunctions()) {
				g.setColor(Color.WHITE);
				g.drawRect((int) ends.getDoublePosition(0), (int) ends.getDoublePosition(1), 1, 1);
			}
		}

		for (Spot p : pointList) {
			g.setColor(Color.RED);
			g.drawRect((int) p.getDoublePosition(0), (int) p.getDoublePosition(1), 1, 1);
		}
		
		
		for (Subgraph subg : subgraphList) {
			
			if(subg.getPointInSubgraph()!=null){
				for(data.Spot p : subg.getPointInSubgraph()){
					
					
					g.setColor(Color.BLUE);
					g.drawLine((int) p.getDoublePosition(0),(int) p.getDoublePosition(1), (int) p.getNetworkx(), (int) p.getNetworky() );
					g.setColor(Color.GREEN);
					g.drawRect((int) p.getDoublePosition(0), (int) p.getDoublePosition(1), 1, 1);
				}
			}
		}
		

		g.dispose();
		File outputfile = new File(path+File.separator+"graph.png");

		try {
			outputfile.createNewFile();
			ImageIO.write(img, "png", outputfile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}
}
