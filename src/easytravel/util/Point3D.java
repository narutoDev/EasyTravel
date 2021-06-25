package easytravel.util;

public class Point3D {

	private int x;
	private int y;
	private int z;

	public Point3D(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public static int getDistanceSquared(Point3D p1, Point3D p2) {
		int distX = Math.abs(p1.getX() - p2.getX());
		int distY = Math.abs(p1.getY() - p2.getY());
		int distZ = Math.abs(p1.getZ() - p2.getZ());

		return Utils.square(distX) + Utils.square(distY) + Utils.square(distZ);
	}

	public static float getDistance(Point3D p1, Point3D p2) {
		return (float) Math.sqrt(Point3D.getDistanceSquared(p1, p2));
	}

	public int getDistanceSquared(Point3D p2) {
		return Point3D.getDistanceSquared(this, p2);
	}

	public float getDistance(Point3D p2) {
		return Point3D.getDistance(this, p2);
	}

}
