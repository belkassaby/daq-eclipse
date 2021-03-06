/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.scanning.api.ui;

import org.eclipse.scanning.api.event.status.StatusBean;

public interface IResultHandler<T extends StatusBean> extends IHandler<T> {
	
	/**
	 * Called to open the result from the beam.
	 * @param bean
	 * @throws Exception if something unexpected goes wrong
	 * @return true if result open handled ok, false otherwise. Normally if the user chooses not to proceed true is still returned.
	 */
	public boolean open(T bean) throws Exception;
}
