package org.eclipse.scanning.test.malcolm.device;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Callable;

import org.eclipse.dawnsci.analysis.api.dataset.IDataset;
import org.eclipse.dawnsci.analysis.api.dataset.ILazyWriteableDataset;
import org.eclipse.dawnsci.analysis.api.monitor.IMonitor;
import org.eclipse.dawnsci.analysis.api.tree.GroupNode;
import org.eclipse.dawnsci.analysis.dataset.impl.Dataset;
import org.eclipse.dawnsci.analysis.dataset.impl.LazyWriteableDataset;
import org.eclipse.dawnsci.analysis.dataset.impl.Random;
import org.eclipse.dawnsci.hdf5.nexus.NexusFileFactoryHDF5;
import org.eclipse.dawnsci.nexus.NexusFile;
import org.eclipse.scanning.api.event.scan.DeviceState;
import org.eclipse.scanning.api.malcolm.MalcolmDeviceException;
import org.eclipse.scanning.api.malcolm.event.MalcolmEventBean;
import org.eclipse.scanning.api.malcolm.models.MapMalcolmDetectorModel;
import org.eclipse.scanning.api.scan.ScanningException;


/**
 * Device which pretends to write a 64x64 file a number of times
 * @author fri44821
 *
 */
public class MockedWriteInLoopPausableMalcolmDevice extends LoopingMockedMalcolmDevice {

	private NexusFileFactoryHDF5 factory;
	private NexusFile file;
	private ILazyWriteableDataset writer;

	public MockedWriteInLoopPausableMalcolmDevice(final String name, final LatchDelegate latcher) throws Exception {		
		
		super(name, latcher);
		
		factory = new NexusFileFactoryHDF5();
		final File ret = File.createTempFile("temp_transient_file", ".h5");
		ret.deleteOnExit();
		
		this.file = factory.newNexusFile(ret.getAbsolutePath(), false);
		file.openToWrite(true);
		GroupNode par = file.getGroup("/entry/data", true); // DO NOT COPY!

		final int[] shape = new int[] { 1, 64, 64 };
		final int[] max = new int[] { -1, 64, 64 };
		writer = new LazyWriteableDataset("image", Dataset.FLOAT, shape, max, shape, null); // DO NOT COPY!
		file.createData(par, writer);
		
		/**
		 * The task to be executed repeatably
		 */
		callableTask = new Callable<Long>() {
			
			@Override
			public Long call() throws Exception {

				int[] shape = (int[])model.getParameterMap().get("shape");
				if (shape==null) shape = new int[]{64,64};
				IDataset       rimage   = Random.rand(shape);
				rimage.setName("image");
				
				int[] start = { count, 0, 0 };
				int[] stop = { count + 1, 64, 64 };
				count++;
				
				writer.setSlice(new IMonitor.Stub(), rimage, start, stop, null);
				file.flush(); // remove explicit flush

				final MalcolmEventBean bean = new MalcolmEventBean(getState());
				bean.setPercentComplete((count/amount)*100d);	
				bean.setFilePath((String)model.getParameterMap().get("file"));
				bean.setDatasetPath("/entry/data");
				
				// Hardcoded shape change of dataset, in reality it will not be so simple.
				bean.setOldShape(new int[]{count-1, shape[0], shape[1]});
				bean.setNewShape(new int[]{count, shape[0], shape[1]});
				bean.setPreviousState(getDeviceState());
				bean.setDeviceState(getDeviceState());
				sendEvent(bean);

				System.err.println("> HDF5 wrote image to " + ret);
				System.err.println("> New shape " + Arrays.toString(writer.getShape()));
				
				return null;
			}
		};
	}

	@Override
	public MapMalcolmDetectorModel validate(MapMalcolmDetectorModel model) throws MalcolmDeviceException {
		Map<String, Object> params = model.getParameterMap();
		if (!params.containsKey("shape")) throw new MalcolmDeviceException(this, "shape must be set!");
		if (!params.containsKey("nframes")) throw new MalcolmDeviceException(this, "nframes must be set!");
		if (!params.containsKey("file")) throw new MalcolmDeviceException(this, "file must be set!");
		if (!params.containsKey("exposure")) throw new MalcolmDeviceException(this, "exposure must be set!");
		return null;
	}

	@Override
	public void configure(MapMalcolmDetectorModel params) throws ScanningException {
		
		validate(params);
		setState(DeviceState.CONFIGURING);
		this.model = params;
		if (params.getParameterMap().containsKey("configureSleep")) {
			try {
				long sleepTime = Math.round(((double)params.getParameterMap().get("configureSleep"))*1000d);
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				throw new MalcolmDeviceException(this, "Cannot sleep during configure!", e);
			}
		}
		setState(DeviceState.READY);
		
		// We configure a bean with all the scan specific things
		final MalcolmEventBean bean = new MalcolmEventBean();
		bean.setFilePath(params.getParameterMap().get("file").toString());
		bean.setDatasetPath("/entry/data");
		bean.setDeviceName(getName());
		bean.setBeamline("Testing");
		bean.setPercentComplete(0d);
        setTemplateBean(bean);
	}
}
