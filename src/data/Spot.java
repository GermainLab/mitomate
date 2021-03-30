package data;

public class Spot extends  fiji.plugin.trackmate.Spot {

	
	public double networkx=0;
	public double networky=0;
	
	public Spot(fiji.plugin.trackmate.Spot spot) {
		super(spot);
	}

	
	public Spot(final double x, final double y, final double z, final double radius, final double quality, final String name) {
		
		super(  x, y,  z,  radius, quality,  name );
	}
	
	public Spot(fiji.plugin.trackmate.Spot spot, double networkx, double networky) {
		super(spot);
		this.networkx=networkx;
		this.networky=networky;
	}


	public double getNetworkx() {
		return networkx;
	}


	public void setNetworkx(double networkx) {
		this.networkx = networkx;
	}


	public double getNetworky() {
		return networky;
	}


	public void setNetworky(double networky) {
		this.networky = networky;
	}
}
