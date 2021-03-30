package importer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import fiji.plugin.trackmate.Settings;
import fiji.plugin.trackmate.Spot;
import fiji.plugin.trackmate.SpotCollection;
import fiji.plugin.trackmate.TrackMate;
import fiji.plugin.trackmate.detection.DetectorKeys;
import fiji.plugin.trackmate.detection.LogDetectorFactory;
import fiji.plugin.trackmate.features.FeatureFilter;
import fiji.plugin.trackmate.features.spot.SpotAnalyzerFactory;
import fiji.plugin.trackmate.util.TMUtils;
import ij.ImagePlus;
import ij.process.ImageProcessor;

public class PointImporter {

	@SuppressWarnings("rawtypes")
	public static SpotCollection importPoint(String[] path, SettingPoint ptsetting, String outputPath, double factor) {

		ImagePlus imP = new ImagePlus(path[1]);

		Settings setting = new Settings();
		setting.imp = imP;
		setting.polygon = null;
		setting.tstart = 0;
		setting.tend = 0;
		setting.xend = imP.getWidth();
		setting.yend = imP.getHeight();

		TrackMate tMate = new TrackMate(setting);

		System.out.println("start is done");
		tMate.getSettings().detectorFactory = new LogDetectorFactory();

		java.util.Map<String, Object> param = tMate.getSettings().detectorFactory.getDefaultSettings();

		if (ptsetting.radius != null) {
			param.put(DetectorKeys.KEY_RADIUS, ptsetting.radius);
		}
		if (ptsetting.threshold != null) {
			param.put(DetectorKeys.KEY_THRESHOLD, ptsetting.threshold);
		}

		setting.detectorFactory = tMate.getSettings().detectorFactory;
		setting.detectorSettings = param;
		setting.nframes = 1;
		setting.nslices = 1;
		setting.setFrom(imP);

		System.out.println(tMate.getSettings().getSpotAnalyzerFactories().size());
		for (SpotAnalyzerFactory<?> fact : tMate.getSettings().getSpotAnalyzerFactories()) {
			System.out.println(fact.toString());
		}

		System.out.println(tMate.execDetection());

		tMate.computeSpotFeatures(true);

		System.out.println(tMate.getModel().getSpots().toString());

		System.out.println(tMate.getModel().getFeatureModel().getSpotFeatureNames());
		System.out.println(tMate.getModel().getFeatureModel().getSpotFeatures());
		System.out.println(tMate.getModel().getFeatureModel().getSpotFeatureNames());

		if (ptsetting.autoQuality != null) {

			final String selectedFeature = "QUALITY";
			final double[] values = tMate.getModel().getSpots().collectValues(selectedFeature, false);
			double threshold = 0;

			if (null != values) {
				threshold = TMUtils.otsuThreshold(values);
				System.out.println(Arrays.toString(values));
			}

			System.out.println("the threshold is : " + threshold);
			threshold = threshold * factor;

			FeatureFilter ff = new FeatureFilter(selectedFeature, threshold, true);

			ArrayList<FeatureFilter> filterList = new ArrayList<FeatureFilter>();
			filterList.add(ff);
			tMate.getModel().filterSpots(filterList, true);

		}

		System.out.println(tMate.getModel().getSpots().toString());
		ImageProcessor imgCopy = imP.getProcessor();

		for (Spot s : tMate.getModel().getSpots().iterable(true)) {
			imgCopy.setColor(Color.WHITE);
			imgCopy.drawOval((int) s.getDoublePosition(0), (int) s.getDoublePosition(1),
					(int) ptsetting.radius.intValue(), (int) ptsetting.radius.intValue());
		}

		ImagePlus imgPlusOval = new ImagePlus("yolo", imgCopy);

		try {
			// retrieve image
			BufferedImage bi = imgPlusOval.getBufferedImage();
			File outputfile = new File(outputPath + "selectedPoint.jpg");
			ImageIO.write(bi, "jpg", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return tMate.getModel().getSpots();

	}

}
