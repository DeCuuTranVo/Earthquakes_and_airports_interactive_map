package module5;

import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.Microsoft;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/** Implements a visual marker for ocean earthquakes on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 *
 */
public class OceanQuakeMarker extends EarthquakeMarker {
	
	public OceanQuakeMarker(PointFeature quake) {
		super(quake);
		
		// setting field in earthquake marker
		isOnLand = false;
	}
	

	/** Draw the earthquake as a square */
	@Override
	public void drawEarthquake(PGraphics pg, float x, float y) {
		pg.rect(x-radius, y-radius, 2*radius, 2*radius);
		
		if (this.clicked) { // draw line to all the markers within threatCircle
			// ScreenPosition
			String cityFile = "city-data.json";
			EarthquakeCityMap earthquakeCityMap = new EarthquakeCityMap();
			List<Feature> cities = GeoJSONReader.loadData(earthquakeCityMap, cityFile);
			UnfoldingMap map = new UnfoldingMap(earthquakeCityMap, 200, 50, 650, 600, new Microsoft.AerialProvider());

			
//			System.out.println(cities.size());
			
			double threatDistance = this.threatCircle();
			for (Feature city : cities) {		
//				System.out.println(city.getProperties()); //{country=Iran, name=Tehran, coastal=false, population=8.15}
				double distance = this.getDistanceTo(((PointFeature)city).getLocation());
//				System.out.println(distance);
				if (distance < threatDistance) {
//					System.out.println(distance + " " + threatDistance);
//					System.out.println(city.getStringProperty("name"));
					
					// Get screen positions of city PointFeature:
//					Location cityLocation = ((PointFeature)city).getLocation();
//					ScreenPosition cityPosition = map.getScreenPosition(cityLocation);
					CityMarker cityMarker = new CityMarker(city);
					
					float outsetX = this.getScreenPosition(map).x-x;
					float outsetY = this.getScreenPosition(map).y-y;
						
					float cityX = cityMarker.getScreenPosition(map).x;
					float cityY = cityMarker.getScreenPosition(map).y;
					
					// Draw line between screen position of OceanQuakeMarker and mutiple PointFeatures
					pg.strokeWeight(4);
					pg.line(x,y,cityX - outsetX,cityY -outsetY);
					
					
//					System.out.println(cityPosition.x + " " + cityPosition.y + " " + x + " " + y);

				}
//				pg.fill(0,0,0);
				pg.strokeWeight(1);
			}
		} else { // hide those line
			// noStroke() 
			
		}
	}
	

	

}
