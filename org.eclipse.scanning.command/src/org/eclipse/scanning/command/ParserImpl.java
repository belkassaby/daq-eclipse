package org.eclipse.scanning.command;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.scanning.api.points.models.StepModel;
import org.eclipse.scanning.api.scan.IParser;

class ParserImpl implements IParser<StepModel> {
	
	static {
		System.out.println("Started Parser Service");
	}

	private final String command;

	private Map<String, StepModel> scannables;

	private Map<String, Number> detectorExposures;

	ParserImpl(String command) {
		this.command = command;
	}

	protected void setScannables(LinkedHashMap<String, StepModel> scannables) {
		this.scannables = scannables;
	}

	protected void setDetectors(LinkedHashMap<String, Number> detectorExposures) {
		this.detectorExposures = detectorExposures;
	}

	@Override
	public String getCommand() {
		return command;
	}

	@Override
	public List<String> getScannableNames() {
		return new ArrayList<String>(scannables.keySet());
	}

	@Override
	public List<String> getDetectorNames() {
		return new ArrayList<String>(detectorExposures.keySet());
	}

	@Override
	public Map<String, Number> getExposures() {
		return detectorExposures;
	}

	@Override
	public StepModel getModel(String scannableName) {
		return scannables.get(scannableName);
	}

}
