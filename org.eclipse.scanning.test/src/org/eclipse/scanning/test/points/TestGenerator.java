package org.eclipse.scanning.test.points;

import java.util.Iterator;

import org.eclipse.scanning.api.points.AbstractGenerator;
import org.eclipse.scanning.api.points.IPosition;

public class TestGenerator extends AbstractGenerator<TestGeneratorModel> {

	@Override
	protected Iterator<IPosition> iteratorFromValidModel() {
		throw new IllegalArgumentException("Not designed to be run, just to test extension point for when people want to load by extension!");
	}

	@Override
	protected void validateModel() { }

}
