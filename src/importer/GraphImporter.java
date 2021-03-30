package importer;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import graph.CompleteGraphAnalyzer;
import graph.lengthInterpreter.DisjktraLength;
import graph.lengthInterpreter.ILenghtInterpreter;
import graph.probabilityInterpreter.FixedInterpreter;
import graph.probabilityInterpreter.IProbInterpreter;
import graph.sampleGenerator.Sampler;
import graph.sampleGenerator.TreeSampler;
import ij.process.BinaryProcessor;
import image.ImageManager;


public class GraphImporter {

	public static CompleteGraphAnalyzer importGraphTest(String[] path, String output) throws Exception {

		
		ImageManager oneLayer;
		ImageManager simpleLayer = null;
		ImageManager skeleton = null;
		
		
		int CHANNEL = 0;
		System.out.println(path);
		System.out.println(path);
		System.out.println(path[0]);
		simpleLayer = new ImageManager(path[0], CHANNEL);
		System.out.println(simpleLayer);
		
		
		ImageManager blurredSimpleLayer = new ImageManager(simpleLayer.getImage(), CHANNEL);
		blurredSimpleLayer.getImageProcessor().blurGaussian(0);
		ImageManager AutoThreshold = new ImageManager(blurredSimpleLayer.getImage(), CHANNEL);
		AutoThreshold.threshold(150, 0);
		oneLayer = AutoThreshold;


		try {
		    // retrieve image
			BinaryProcessor binPro = new BinaryProcessor(oneLayer.getImageProcessor().convertToByteProcessor());
		    BufferedImage bi = binPro.getBufferedImage();
		    File outputfile = new File(output+"pre_skeleeton.png");
		    ImageIO.write(bi, "png", outputfile);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		
		ILenghtInterpreter lengthInter = new DisjktraLength();

		IProbInterpreter probInterp = new FixedInterpreter();
		Sampler sampleGen = new TreeSampler(0, 0, probInterp, lengthInter);

		// ********************************
		// ANALYSIS
		// *******************************

		oneLayer.label_8Connected(0, 0);
		oneLayer.getLabelling();
		BinaryProcessor binPro = new BinaryProcessor(oneLayer.getImageProcessor().convertToByteProcessor());
		// print skeleton

		binPro.skeletonize();

		try {
		    // retrieve image
		    BufferedImage bi = binPro.getBufferedImage();
		    File outputfile = new File(output+"skeleeton.png");
		    ImageIO.write(bi, "png", outputfile);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		
		
		skeleton = new ImageManager(binPro.createImage(), CHANNEL);

		skeleton.label_8Connected(0, 0);

		skeleton.setSkeletonArray();
		skeleton.destroyStar();

		//skeleton.prunneSkeleton();
		
		
		skeleton.destroyStar();
		skeleton.destroyStar();
		skeleton.deleteRedStub();
		skeleton.deleteRedStub();
		skeleton.deleteRedStub();
		skeleton.destroyStar();
		skeleton.deleteRedStub();
		
		skeleton.findCubePixel();
		skeleton.findCubePixel();
		skeleton.findTriCycle();
		//skeleton.findTriCycle();

		skeleton.setEndPixel();

		CompleteGraphAnalyzer analGraph = null;

		analGraph = new graph.CompleteGraphAnalyzer(skeleton, 0, 1, 1, 1, sampleGen);

		analGraph.labbelImage();

		return analGraph;

	}
}
