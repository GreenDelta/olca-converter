package org.openlca.olcatdb.conversion;

/**
 * An interface for monitoring a conversion task.
 * 
 * @author Michael Srocka
 * 
 */
public interface Monitor {

	/**
	 * A new task with the given size is started.
	 */
	void begin(int size);

	/**
	 * A step of the task is made.
	 */
	void progress(String step, int worked);

	/**
	 * Indicates that the task is finished.
	 */
	void finished(Task task);

}
