package moduleProject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
import module6.CityMarker;
import module6.EarthquakeMarker;
import moduleProject.CommonMarker;
import de.fhpotsdam.unfolding.geo.Location;
import parsing.ParseFeed;
import processing.core.PApplet;

/** An applet that shows airports (and routes)
 * on a world map.  
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMap extends PApplet {
	
	UnfoldingMap map;
	private List<Marker> airportList;
	List<Marker> routeList;
	private HashMap<Integer, Location> airports;
	
	private moduleProject.CommonMarker lastSelected;
	private moduleProject.CommonMarker lastClicked;
	
	public void setup() {
		// setting up PAppler
		size(1200,800, OPENGL);
		
		// setting up map and default events
		map = new UnfoldingMap(this, 250, 50, 900, 700, new Microsoft.AerialProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// get features from airport data
		List<PointFeature> features = ParseFeed.parseAirports(this, "airports.dat");
		
		// list for markers, hashmap for quicker access when matching with routes
		airportList = new ArrayList<Marker>();
		airports = new HashMap<Integer, Location>();
		
		// create markers from features
		for(PointFeature feature : features) {
			AirportMarker m = new AirportMarker(feature);
	
			m.setRadius(5); //5
			airportList.add(m);
			
			// put airport in hashmap with OpenFlights unique id for key
			airports.put(Integer.parseInt(feature.getId()), feature.getLocation());
			
//			System.out.println(feature.getProperties()); // {country="United States", altitude=22, code="MTN", city="Baltimore", name="Martin State"}
		
		}
		
		
		// parse route data
		List<ShapeFeature> routes = ParseFeed.parseRoutes(this, "routes.dat");
		routeList = new ArrayList<Marker>();
		for(ShapeFeature route : routes) {
			
			// get source and destination airportIds
			int source = Integer.parseInt((String)route.getProperty("source"));
			int dest = Integer.parseInt((String)route.getProperty("destination"));
			
			// get locations for airports on route
			if(airports.containsKey(source) && airports.containsKey(dest)) {
				route.addLocation(airports.get(source));
				route.addLocation(airports.get(dest));
			}
			
			SimpleLinesMarker sl = new SimpleLinesMarker(route.getLocations(), route.getProperties());
		
//			System.out.println(sl.getProperties()); //{destination=2913, source=2912}
			
			//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
			routeList.add(sl);
		}
		
		
		
		//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
		map.addMarkers(routeList);
		
		map.addMarkers(airportList);
		
//		System.out.println(airports); // {1=(-6.082, 145.392), 2=(-5.207, 145.789), 3=(-5.827, 144.296)}
	}
	
	public void draw() {
		background(0);
		map.draw();	
		addKey();
	}
	
	// helper method to draw key in GUI
	private void addKey() {
		// Draw box and title
		fill(171, 178, 185);
		int xbase = 25;
		int ybase = 50;
		
		// Draw box and title
		rect(xbase, ybase, 150, 250);
		
		fill(0);
		textAlign(LEFT, CENTER);
		textSize(12);
		text("Airport key", xbase+25, ybase+15);
		
		// Draw Text
		fill(0, 0, 0);
//		textAlign(LEFT, CENTER);
		text("Clicked airport", xbase + 50,  ybase + 50);	
		text("Hovered airport ", xbase+50, ybase+70);
		text("Normal airport", xbase+50, ybase+90);
		text("Fight route", xbase+50, ybase+110);
		
		// Draw clicked circle
		stroke(0, 255, 0);
		strokeWeight(3);
		fill(color(0, 0, 0));
		ellipse(xbase+35, ybase+50, 15, 15);

		// Draw hovered circle
		stroke(255, 255, 255);
		strokeWeight(3);
		fill(color(0, 0, 0));
		ellipse(xbase+35, ybase+70, 15, 15);
		
		// Draw normal circle
		stroke(255, 255, 255);
		strokeWeight(1);
		fill(color(0, 0, 0));
		ellipse(xbase+35, ybase+90, 5, 5);
		
		// Draw route line
		stroke(128, 139, 150);
		strokeWeight(2);
		fill(128, 139, 150);
		int centerx = xbase+35;
		int centery = ybase+110;
		line(centerx-12, centery, centerx+12, centery);
	}
	
//	// helper method to draw key in GUI
//	private void addKey() {	
//		// Remember you can use Processing's graphics methods here
//		fill(255, 250, 240);
//		
//		int xbase = 25;
//		int ybase = 50;
//		
//		// Draw box and title
//		rect(xbase, ybase, 150, 250);
//		
//		fill(0);
//		textAlign(LEFT, CENTER);
//		textSize(12);
//		text("Airport key", xbase+25, ybase+25);
//		
//		// Draw Triangle
//		fill(150, 30, 30);
//		int tri_xbase = xbase + 35;
//		int tri_ybase = ybase + 50;
//		triangle(tri_xbase, tri_ybase-CityMarker.TRI_SIZE, tri_xbase-CityMarker.TRI_SIZE, 
//				tri_ybase+CityMarker.TRI_SIZE, tri_xbase+CityMarker.TRI_SIZE, 
//				tri_ybase+CityMarker.TRI_SIZE);
//
//		// Draw Text
//		fill(0, 0, 0);
//		textAlign(LEFT, CENTER);
//		text("City Marker", tri_xbase + 15, tri_ybase);
//		
//		text("Land Quake", xbase+50, ybase+70);
//		text("Ocean Quake", xbase+50, ybase+90);
//		text("Size ~ Magnitude", xbase+25, ybase+110);
//		
//		// Draw ocean quake
//		fill(255, 255, 255);
//		ellipse(xbase+35, 
//				ybase+70, 
//				10, 
//				10);
		// Draw land quake
//		rect(xbase+35-5, ybase+90-5, 10, 10);
//		
//		// Draw circles
//		fill(color(255, 255, 0));
//		ellipse(xbase+35, ybase+140, 12, 12);
//		fill(color(0, 0, 255));
//		ellipse(xbase+35, ybase+160, 12, 12);
//		fill(color(255, 0, 0));
//		ellipse(xbase+35, ybase+180, 12, 12);
//		
//		textAlign(LEFT, CENTER);
//		fill(0, 0, 0);
//		text("Shallow", xbase+50, ybase+140);
//		text("Intermediate", xbase+50, ybase+160);
//		text("Deep", xbase+50, ybase+180);
//
//		text("Past hour", xbase+50, ybase+200);
//		
//		fill(255, 255, 255);
//		int centerx = xbase+35;
//		int centery = ybase+200;
//		ellipse(centerx, centery, 12, 12);
//
//		strokeWeight(2);
//		line(centerx-8, centery-8, centerx+8, centery+8);
//		line(centerx-8, centery+8, centerx+8, centery-8);
//	}
	
	/** Event handler that gets called automatically when the 
	 * mouse moves.
	 */
	@Override
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;	
		}
		selectMarkerIfHover(airportList);
		//loop();
	}
	
	
	// If there is a marker selected 
	private void selectMarkerIfHover(List<Marker> markers)
	{
		// Abort if there's already a marker selected
		if (lastSelected != null) {
			return;
		}
		
		for (Marker m : markers) 
		{
			moduleProject.CommonMarker marker = (moduleProject.CommonMarker)m;
			if (marker.isInside(map,  mouseX, mouseY)) {
				lastSelected = marker;
				marker.setSelected(true);
				return;
			}
		}
	}
	
	/** The event handler for mouse clicks
	 * If an airport is clicked, 
	 * it will hide other airports,
	 * and then display the route out of that airport
	 */
	@Override
	public void mouseClicked()
	{
		if (lastClicked != null) {
			unhideMarkers();
			lastClicked.clicked = false;
			lastClicked = null;
		}
		else if (lastClicked == null) 
		{
			checkAirportsForClick();
		}
	}
	
	// Helper method that will check if an airport marker was clicked on
	// and respond appropriately
	private void checkAirportsForClick() {
		// Safety check
		if (lastClicked != null) {
			return;
		}
		
		// Loop over the airports markers to see if one of them is selected
		for (Marker marker : airportList) {
			CommonMarker currentMarker = (CommonMarker) marker;
			
			// Find lastCliecked CommonMarker
			if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
				System.out.println(currentMarker.toString());
				currentMarker.setHidden(false);
				lastClicked = currentMarker;
				lastClicked.clicked = true;
				continue;
			} 			
		}
		
		// If no marker are clicked, return
		if (lastClicked == null) {
			return;
		}
		
		// Hide the markers that are different from airport markers
		for (Marker marker : airportList) {
			CommonMarker currentMarker = (CommonMarker) marker;		
			if (currentMarker != lastClicked) {
				currentMarker.setHidden(true);
			}
		}
		
		// Find routes that come from that airport and unhide
		// Hide routes that are not those routes (hide routes not come from airports)
		Location lastClickedLocation = lastClicked.getLocation();
		for (Marker marker : routeList) {
			SimpleLinesMarker currentRoute = (SimpleLinesMarker) marker;
			Integer sourceLocationKey = Integer.parseInt(currentRoute.getStringProperty("source"));
			Location sourceLocation = airports.get(sourceLocationKey);
			Integer destinationLocationKey = Integer.parseInt(currentRoute.getStringProperty("destination"));
			Location destinationLocation = airports.get(destinationLocationKey);
			
//			System.out.println(sourceLocation + "-" + destinationLocation);
			
			if (lastClickedLocation.equals(sourceLocation)) {
				currentRoute.setHidden(false);
				for (Marker airportMarker : airportList) {
					CommonMarker currentAirport = (CommonMarker) airportMarker;
					if (currentAirport.getLocation().equals(destinationLocation)) {
						currentAirport.setHidden(false);
					}
				}
			} else if (lastClickedLocation.equals(destinationLocation)){
				currentRoute.setHidden(false);
				for (Marker airportMarker : airportList) {
					CommonMarker currentAirport = (CommonMarker) airportMarker;
					if (currentAirport.getLocation().equals(sourceLocation)) {
						currentAirport.setHidden(false);
					}
				}
			} else {
				currentRoute.setHidden(true);
			}
		}	
	}
	
//	// Helper method that will check if a city marker was clicked on
//	// and respond appropriately
//	private void checkCitiesForClick()
//	{
//		if (lastClicked != null) return;
//		// Loop over the earthquake markers to see if one of them is selected
//		for (Marker marker : cityMarkers) {
//			if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
//				lastClicked = (CommonMarker)marker;
//				// Hide all the other earthquakes and hide
//				for (Marker mhide : cityMarkers) {
//					if (mhide != lastClicked) {
//						mhide.setHidden(true);
//					}
//				}
//				for (Marker mhide : quakeMarkers) {
//					EarthquakeMarker quakeMarker = (EarthquakeMarker)mhide;
//					if (quakeMarker.getDistanceTo(marker.getLocation()) 
//							> quakeMarker.threatCircle()) {
//						quakeMarker.setHidden(true);
//					}
//				}
//				return;
//			}
//		}		
//	}
	
	// loop over and unhide all markers
	private void unhideMarkers() {
		for(Marker marker : airportList) {
			marker.setHidden(false);
		}
			
		for(Marker marker : routeList) {
			marker.setHidden(false);
		}
	}
	
	// loop over and hide all markers
	private void hideMarkers() {
		for (Marker marker: airportList) {
			marker.setHidden(true);
		}
		for (Marker marker: routeList) {
			marker.setHidden(true);
		}
	}
	
//	// Helper method that will check if an earthquake marker was clicked on
//	// and respond appropriately
//	private void checkEarthquakesForClick()
//	{
//		if (lastClicked != null) return;
//		// Loop over the earthquake markers to see if one of them is selected
//		for (Marker m : quakeMarkers) {
//			EarthquakeMarker marker = (EarthquakeMarker)m;
//			if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
//				lastClicked = marker;
//				// Hide all the other earthquakes and city
//				for (Marker mhide : quakeMarkers) {
//					if (mhide != lastClicked) {
//						mhide.setHidden(true);
//					}
//				}
//				for (Marker mhide : cityMarkers) {
//					if (mhide.getDistanceTo(marker.getLocation()) 
//							> marker.threatCircle()) {
//						mhide.setHidden(true);
//					}
//				}
//				return;
//			}
//		}
//	}
}


//Display all the airports in the world as features, and then display additional information about them when the user hovers over them.
//
//Display only a subset of the airports in the world, based on some criteria.
//
//Display airports, and when the user clicks on one, display the routes out of that airport.
//
//Display all the airports that you have been to.
//
//… the list of possibilities is endless!