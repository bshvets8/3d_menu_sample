package bogdan_shvets.eleks.com.a3dmenulibrary.util;

import android.support.annotation.Nullable;

/**
 * Created by Богдан on 16.10.2016
 */
public class CoordinateHelper {

	private static final int COORDINATES = 3;

	/**
	 * @param sphericalCoordinates in form (distance from center, zenithal angle, azimuthal angle)
	 * @return array transformed to cartesian system
	 */
	@Nullable
	public static float[] fromSpherical(float[] sphericalCoordinates) {
		if (sphericalCoordinates.length % COORDINATES != 0)
			return null;

		float[] result = new float[sphericalCoordinates.length];

		for (int i = 0; i < sphericalCoordinates.length; i = i + 3) {
			result[i] = (float) (sphericalCoordinates[i] * Math.sin(Math.toRadians(sphericalCoordinates[i + 1])) * Math.cos(Math.toRadians(sphericalCoordinates[i + 2])));
			result[i + 1] = (float) (sphericalCoordinates[i] * Math.sin(Math.toRadians(sphericalCoordinates[i + 1])) * Math.sin(Math.toRadians(sphericalCoordinates[i + 2])));
			result[i + 2] = (float) (sphericalCoordinates[i] * Math.cos(Math.toRadians(sphericalCoordinates[i + 1])));
		}

		return result;
	}

	public static float[] getSphericalCoordinatesForRectangle(float distanceFromCenter, float degrees) {
		float half = degrees / 2;

		return new float[] {
				distanceFromCenter, -half,  half,
				distanceFromCenter,  half, -half,
				distanceFromCenter, -half, -half,
				distanceFromCenter,  half,  half
		};
	}
}
