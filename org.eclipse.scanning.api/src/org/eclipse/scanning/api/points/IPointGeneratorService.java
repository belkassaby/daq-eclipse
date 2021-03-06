package org.eclipse.scanning.api.points;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.scanning.api.points.models.CompoundModel;
import org.eclipse.scanning.api.points.models.IScanPathModel;

/**
 * This service generates points for a given scan type.
 * <p>
 * Usage:
 * <usage><code><pre>
 * IPointGeneratorService pservice  = ... // OSGi
 *
 * LissajousModel model = new LissajousModel();
 *  ... // Set values
 *
 * IPointGenerator<LissajousModel,Point> generator = pservice.createGenerator(model, roi);
 *
 * Iterator<Point> it = generator.iterator();
 * ... // Use iterator in a scan.
 *
 * // Use size to tell user in GUI the whole size. Avoids making all points if it can
 * int size = generator.size();
 *
 * // Create and return all the points in memory (might be large). Avoid if possible
 * List<Point> allPoints = generator.createPoints();
 * </pre></code></usage>
 *
 * @author Matthew Gerring
 *
 */
public interface IPointGeneratorService {

	/**
	 * Used to create a point generator of a given type
	 * @param model
	 * @return
	 */
	default <T extends IScanPathModel> IPointGenerator<T> createGenerator(T model) throws GeneratorException {
		return createGenerator(model, Collections.emptyList());
	}

	/**
	 * Used to create a point generator of a given type.
	 * <p>
	 * Convenience implementation when using only one region of interest
	 * 
	 * @param model
	 * @param region a reference to an IROI for instance
	 * @return
	 */
	default <T extends IScanPathModel,R> IPointGenerator<T> createGenerator(T model, R region) throws GeneratorException {
		return createGenerator(model, Arrays.asList(region));
	}

	/**
	 * Used to create a point generator of a given type
	 * @param model
	 * @param regions a reference to zero or more IROIs for instance
	 * @return
	 */
	<T extends IScanPathModel,R> IPointGenerator<T> createGenerator(T model, Collection<R> regions) throws GeneratorException;

	/**
	 * Create a nested or compound generator.
	 * Each generator in the varargs argument is another level to the loop.
	 * 
	 * @param generators
	 * @return
	 * @throws GeneratorException
	 */
	IPointGenerator<?> createCompoundGenerator(IPointGenerator<?>... generators) throws GeneratorException;
	
	/**
	 * Create a nested or compound generator from a list of models.
	 * 
	 * @param cmodel
	 * @return
	 * @throws GeneratorException
	 */
	IPointGenerator<?> createCompoundGenerator(CompoundModel cmodel) throws GeneratorException;

	/**
	 * 
	 * @param cmodel
	 * @param models
	 * @return
	 * @throws GeneratorException
	 */
	<R> Collection<R> findRegions(CompoundModel cmodel, IScanPathModel models) throws GeneratorException;

	/**
	 * Each IPointGenerator must have a unique id which is used to refer to it in the user interface.
	 * @return
	 */
	Collection<String> getRegisteredGenerators();

	/**
	 * Creates a generator by id which has an model associated with it.
	 * The model may either be retrieved and have fields set or the generator
	 * may have a new model set in it.
	 * 
	 * @param id
	 * @return
	 */
	<T extends IScanPathModel> IPointGenerator<T> createGenerator(String id) throws GeneratorException;
}
