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

		float distanceFromCenter, zenithalAngle, azimuthalAngle;

		for (int i = 0; i < sphericalCoordinates.length; i += 3) {
			distanceFromCenter = sphericalCoordinates[i];
			zenithalAngle = sphericalCoordinates[i + 1];
			azimuthalAngle = sphericalCoordinates[i + 2];

			float halfWidth = (float) Math.abs(Math.sin(Math.toRadians(azimuthalAngle / 2)) * distanceFromCenter * 2);

//			Workaround to avoid improper coordinate converting
			result[i + 1] = (azimuthalAngle < 0) ? -halfWidth : halfWidth;
//			result[i + 1] = (float) (distanceFromCenter * Math.sin(Math.toRadians(zenithalAngle)) * Math.sin(Math.toRadians(azimuthalAngle)));

			result[i] =		(float) (distanceFromCenter * Math.sin(Math.toRadians(zenithalAngle)) * Math.cos(Math.toRadians(azimuthalAngle)));
			result[i + 2] = (float) (distanceFromCenter * Math.cos(Math.toRadians(zenithalAngle)));
		}

		return result;
	}

	public static float[] getSphericalCoordinatesForRectangle(float distanceFromCenter, float widthInDegrees, float offsetDegrees) {
		float half = widthInDegrees / 2;

		return new float[] {
				distanceFromCenter, -half + offsetDegrees, -half,
				distanceFromCenter,  half + offsetDegrees, -half,
				distanceFromCenter, -half + offsetDegrees,  half,
				distanceFromCenter,  half + offsetDegrees,  half
		};
	}
}
