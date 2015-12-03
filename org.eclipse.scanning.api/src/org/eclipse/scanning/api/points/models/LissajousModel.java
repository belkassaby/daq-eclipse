package org.eclipse.scanning.api.points.models;

public class LissajousModel implements IBoundingBoxModel {

	private IBoundingBoxModel boundingBox = new BoundingBoxModel();
	private double a = 1;
	private double b = 0.25;
	private double delta = 0;
	private double thetaStep = 0.05;

	public double getA() {
		return a;
	}
	public void setA(double a) {
		this.a = a;
	}
	public double getB() {
		return b;
	}
	public void setB(double b) {
		this.b = b;
	}
	public double getDelta() {
		return delta;
	}
	public void setDelta(double delta) {
		this.delta = delta;
	}
	public double getThetaStep() {
		return thetaStep;
	}
	public void setThetaStep(double thetaStep) {
		this.thetaStep = thetaStep;
	}
	@Override
	public double getWidth() {
		return boundingBox.getWidth();
	}
	@Override
	public void setWidth(double width) {
		boundingBox.setWidth(width);
	}
	@Override
	public double getHeight() {
		return boundingBox.getHeight();
	}
	@Override
	public void setHeight(double height) {
		boundingBox.setHeight(height);
	}
	@Override
	public boolean isParentRectangle() {
		return boundingBox.isParentRectangle();
	}
	@Override
	public void setParentRectangle(boolean isParentRectangle) {
		boundingBox.setParentRectangle(isParentRectangle);
	}
	@Override
	public double getxStart() {
		return boundingBox.getxStart();
	}
	@Override
	public void setxStart(double xStart) {
		boundingBox.setxStart(xStart);
	}
	@Override
	public double getyStart() {
		return boundingBox.getyStart();
	}
	@Override
	public void setyStart(double yStart) {
		boundingBox.setyStart(yStart);
	}
	@Override
	public double getAngle() {
		return boundingBox.getAngle();
	}
	@Override
	public void setAngle(double angle) {
		boundingBox.setAngle(angle);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(a);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(b);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((boundingBox == null) ? 0 : boundingBox.hashCode());
		temp = Double.doubleToLongBits(delta);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(thetaStep);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		LissajousModel other = (LissajousModel) obj;
		if (Double.doubleToLongBits(a) != Double.doubleToLongBits(other.a))
			return false;
		if (Double.doubleToLongBits(b) != Double.doubleToLongBits(other.b))
			return false;
		if (boundingBox == null) {
			if (other.boundingBox != null)
				return false;
		} else if (!boundingBox.equals(other.boundingBox))
			return false;
		if (Double.doubleToLongBits(delta) != Double
				.doubleToLongBits(other.delta))
			return false;
		if (Double.doubleToLongBits(thetaStep) != Double
				.doubleToLongBits(other.thetaStep))
			return false;
		return true;
	}
}
