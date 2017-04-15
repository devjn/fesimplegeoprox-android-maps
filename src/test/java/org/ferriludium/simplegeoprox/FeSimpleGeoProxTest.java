/*******************************************************************************
 * Copyright [2016] [Cornelius Perkins]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *    Cornelius Perkins - initial API and implementation and/or initial documentation
 *    
 * Author Cornelius Perkins (ccperkins at both github and bitbucket)
 *******************************************************************************/ 

package org.ferriludium.simplegeoprox;

import com.google.android.gms.maps.model.LatLng;
import com.javadocmd.simplelatlng.util.LengthUnit;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FeSimpleGeoProxTest {
	static class TestClientMapObject {
		final public String desc;
		LatLng loc;
		public TestClientMapObject(String desc, LatLng loc) {
			super();
			this.desc = desc;
			this.loc = loc;
		}
		@Override
		public String toString() {
			return "TestClientMapObject [desc=" + desc + "]";
		}
	}

    static class MapObjectClusterItem<T> extends MapClusterItem {
        LatLng loc;
        protected MapObjectClusterItem(LatLng loc, T clientObject) {
            super(clientObject);
            this.loc = loc;
        }

        @Override
        public LatLng getPosition() {
            return loc;
        }

        @Override
        public String getTitle() {
            return "Title";
        }

        @Override
        public String getSnippet() {
            return "Snippet";
        }
    }

	public static MapClusterItem<TestClientMapObject> makeObj(String desc, LatLng loc) {
		return new MapObjectClusterItem<TestClientMapObject> (loc, new TestClientMapObject(desc, loc));
	}	


	@Test
	public void testGetPointsWithin_1Mile_NorthernHemisphere() {
		assertSearchMethodsReturnSameResults(
				givenAWorld_with_20000_points_inNorthernHemisphere(),
				new LatLng (41.0, -73.0), 1.0, LengthUnit.MILE);
	}
	@Test
	public void testGetPointsWithin_20Miles_NorthernHemisphere() {
		assertSearchMethodsReturnSameResults(
				givenAWorld_with_20000_points_inNorthernHemisphere(),
				new LatLng (41.0, -73.0), 20.0, LengthUnit.MILE);
	}
	@Test
	public void testGetPointsWithin_1000Miles_NorthernHemisphere() {
		assertSearchMethodsReturnSameResults(
				givenAWorld_with_20000_points_inNorthernHemisphere(),
				new LatLng (41.0, -73.0), 1000.0, LengthUnit.MILE);
	}

	@Test
	public void testGetPointsWithin_1Mile_OfAntiMeridian() {
		assertSearchMethodsReturnSameResults(
				givenAWorld_with_20000_points_nearAntiMeridian(),
				new LatLng (41.0, -179.0), 1.0, LengthUnit.MILE);
	}
	@Test
	public void testGetPointsWithin_20Miles_OfAntiMeridian() {
		assertSearchMethodsReturnSameResults(
				givenAWorld_with_20000_points_nearAntiMeridian(),
				new LatLng (41.0, -179.0), 20.0, LengthUnit.MILE);
	}
	@Test
	public void testGetPointsWithin_1000Miles_OfAntiMeridian() {
		assertSearchMethodsReturnSameResults(
				givenAWorld_with_20000_points_nearAntiMeridian(),
				new LatLng (41.0, -179.0), 1000.0, LengthUnit.MILE);
	}
	@Test
	public void testGetPointsWithin_1Mile_NearPole() {
		assertSearchMethodsReturnSameResults(
				givenAWorld_with_20000_points_nearPole(),
				new LatLng (0.0, 89.9), 1, LengthUnit.MILE);
	}
	@Test
	public void testGetPointsWithin_20Miles_NearPole() {
		assertSearchMethodsReturnSameResults(
				givenAWorld_with_20000_points_nearPole(),
				new LatLng (0.0, 89.9), 20, LengthUnit.MILE);
	}
	@Test
	public void testGetPointsWithin_1000Miles_NearPole() {
		assertSearchMethodsReturnSameResults(
				givenAWorld_with_20000_points_nearPole(),
				new LatLng (0.0, 89.9), 1000, LengthUnit.MILE);
	}

	public static Collection<MapClusterItem<TestClientMapObject>> searchByBruteForce (FeSimpleGeoProx<TestClientMapObject> world 
			, LatLng start, double radius, LengthUnit units ) {
		Collection<MapClusterItem<TestClientMapObject>> coll = new ArrayList<MapClusterItem<TestClientMapObject>>();
		for (MapClusterItem<TestClientMapObject> obj: world) 
			if (LatLngTool.distance(start, obj.getPosition(), units) <= radius)
				coll.add(obj);
		return coll;

	}
	public static void assertSearchMethodsReturnSameResults(FeSimpleGeoProx<TestClientMapObject> world, LatLng start, double radius, LengthUnit units) { 
		Collection<MapClusterItem<TestClientMapObject>> pointsByBruteForce = searchByBruteForce(world, start, radius, units);
		Collection<MapClusterItem<TestClientMapObject>> pointsByWorldSearch = world.find (start, radius, units);
		assertPointCollectionsEqual (pointsByBruteForce, pointsByWorldSearch);
	}
	public static void assertPointCollectionsEqual(Collection<MapClusterItem<TestClientMapObject>> left, Collection<MapClusterItem<TestClientMapObject>> right) {
		assertEquals(left.size(), right.size());
		for (MapClusterItem<TestClientMapObject> point: left) {
			if (! right.contains(point)) {
				assertTrue(false);
			}
		}
	}


	public static Collection<MapClusterItem<TestClientMapObject>> make_many_points_around_200_miles_from_anti_meridian() {
		LatLng start = new LatLng(41, 180.0);
		LatLng westOfStart = LatLngTool.travel(start, LatLngTool.Bearing.WEST, 200, LengthUnit.MILE);
		LatLng eastOfStart = LatLngTool.travel(start, LatLngTool.Bearing.EAST, 200, LengthUnit.MILE);
		LatLng northOfStart = LatLngTool.travel(start, LatLngTool.Bearing.NORTH, 200, LengthUnit.MILE);
		LatLng southOfStart = LatLngTool.travel(start, LatLngTool.Bearing.SOUTH, 200, LengthUnit.MILE);

		//System.out.println("went 200 miles west and came to longitude " + westOfStart.getLongitude());
		//System.out.println("went 200 miles east and came to longitude " + eastOfStart.getLongitude());
		//System.out.println("went 200 miles north and came to latitude " + northOfStart.getLatitude());
		//System.out.println("went 200 miles south and came to latitude " + southOfStart.getLatitude());

		Collection<MapClusterItem<TestClientMapObject>> westBlock = make_many_points (southOfStart.latitude, northOfStart.latitude,
				westOfStart.longitude, 180.0);
		Collection<MapClusterItem<TestClientMapObject>> eastBlock = make_many_points (southOfStart.latitude, northOfStart.latitude,
				-180.0, eastOfStart.longitude);

		Collection<MapClusterItem<TestClientMapObject>> ret = westBlock;
		ret.addAll(eastBlock);
		return ret;

	}


	Collection<MapClusterItem<TestClientMapObject>> make_many_points_around_20_miles_from_41_neg73() {
		return make_many_points (40.0, 42.0, -73.5, -72.5);
	}
	public static Collection<MapClusterItem<TestClientMapObject>> make_many_points(double minLat, double maxLat, double minLong, double maxLong) {
		//System.out.println ( "makeManyPoints: minLat=" + minLat + ", maxLat=" + maxLat + ", minLong=" + minLong + ", maxLong=" + maxLong );
		double latStep = 0.00578927;
		double lngStep = 0.00767079;
		Collection<MapClusterItem<TestClientMapObject>> coll = new ArrayList<MapClusterItem<TestClientMapObject>>();
		for (double lat = minLat; lat < maxLat; lat += latStep) {
			for (double lng= minLong; lng < maxLong; lng += lngStep) {
				String coords="|" + lat + "/" + lng + "|";
				MapClusterItem<TestClientMapObject> obj = makeObj (coords, new LatLng(lat, lng));
				coll.add(obj);
				//System.out.println(obj);
			}
		}
		return coll;
	}

	public FeSimpleGeoProx<TestClientMapObject> givenAWorld_with_20000_points_inNorthernHemisphere () {
		double startLat  = 41.2; 
		double startLong = -73.3;
		double bumpLat = -0.004;
		double bumpLong = 0.006;
		return givenAWorld_With_20000_points(startLat, startLong, bumpLat, bumpLong);
	}

	public FeSimpleGeoProx<TestClientMapObject> givenAWorld_with_20000_points_nearAntiMeridian () {
		Collection<MapClusterItem<TestClientMapObject>> mapObjects = make_many_points (40.0, 42.0, -180.0, -179.0);
		mapObjects.addAll(make_many_points (40.0, 42.0, 179.0, 180.0));
		FeSimpleGeoProx<TestClientMapObject> gm = new FeSimpleGeoProx<TestClientMapObject>(mapObjects);
		return gm;

	}
	public FeSimpleGeoProx<TestClientMapObject> givenAWorld_with_20000_points_nearPole () {
		double startLat  = 89.8;
		double startLong = -73.3; 
		double bumpLat = -0.004; 
		double bumpLong = 0.006;
		return givenAWorld_With_20000_points(startLat, startLong, bumpLat, bumpLong);
	}
	public FeSimpleGeoProx<TestClientMapObject> givenAWorld_With_20000_points (double startLat, double startLong, double bumpLat, double bumpLong) {
		List<MapClusterItem<TestClientMapObject>> mapObjects = new ArrayList<>();

		for (int dup=0; dup<=1; dup++) { // Proving duplicates work
			for (int ii=0; ii < 100; ii++) {
				for (int jj=0; jj < 100; jj++) {
					LatLng loc = new LatLng (startLat+(ii*bumpLat), startLong+(jj*bumpLong));
					MapClusterItem<TestClientMapObject> go = makeObj ("test_" + ii + "_" + jj + "_" + dup, loc);
					mapObjects.add(go);
				}
			}
		}

		FeSimpleGeoProx<TestClientMapObject> gm = new FeSimpleGeoProx<TestClientMapObject>(mapObjects);
		return gm;

	}

}
