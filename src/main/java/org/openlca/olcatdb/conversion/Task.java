package org.openlca.olcatdb.conversion;

import org.openlca.olcatdb.ResourceFolder;

/**
 * A conversion task (e.g. file transformation, validation, ...).
 * 
 * @author Michael Srocka
 * 
 */
public interface Task extends Runnable {

	/**
	 * Cancel a running task.
	 */
	void cancel();

	/**
	 * Set a monitor to observe a running task.
	 */
	void setMonitor(Monitor monitor);

	/**
	 * The result of the conversion task.
	 */
	ResourceFolder getResult();
}
