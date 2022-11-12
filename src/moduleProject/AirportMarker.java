package moduleProject;

import java.util.List;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import processing.core.PConstants;
import processing.core.PGraphics;

/** 
 * A class to represent AirportMarkers on a world map.
 *   
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMarker extends CommonMarker {
	public static List<SimpleLinesMarker> routes;
	
	public AirportMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
	}
	
	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		if (this.clicked) {
			pg.stroke(0, 255, 0);
			pg.strokeWeight(3);
			pg.fill(0, 0, 0);
			pg.ellipse(x, y, 15, 15);	
		} else if (this.isSelected()) {
			pg.stroke(255, 255, 255);
			pg.strokeWeight(3);
			pg.fill(0, 0, 0);
			pg.ellipse(x, y, 15, 15);	
		} else {
			pg.stroke(255, 255, 255);
			pg.strokeWeight(1);
			pg.fill(0, 0, 0);
			pg.ellipse(x, y, 5, 5);		
		}
	}

	/** Show the title of the airport if this marker is selected */
	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		// show rectangle with title
		String title = this.getName().replace("\"", "") + ", " + this.getCity().replace("\"", "") + ", " + this.getCountry().replace("\"", "");
//		System.out.println(title);
		pg.pushStyle();
		pg.fill(255, 255, 255);
		pg.textSize(12);
		pg.rectMode(PConstants.CORNER);
		pg.stroke(0, 0, 0);
		pg.rect(x, y + 15, pg.textWidth(title) +6, 18, 5);
		pg.fill(0, 0, 0);
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.text(title, x + 3 , y +18);
		pg.popStyle();
		// show routes		
	}
	
	private String getCountry()
	{
		return this.getStringProperty("country");
	}
	
	private int getAltitude() {
		return Integer.parseInt(this.getStringProperty("altitude"));
	}
	
	private String getCode() {
		return this.getStringProperty("code");
	}
	
	private String getCity()
	{
		return this.getStringProperty("city");
	}
	
	private String getName()
	{
		return this.getStringProperty("name");
	}
	
	@Override
	public String toString() {
		return this.getName().replace("\"", "") + ", " + this.getCity().replace("\"", "") + ", " + this.getCountry().replace("\"", "");
	}
}
