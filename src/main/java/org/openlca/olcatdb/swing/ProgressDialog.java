package org.openlca.olcatdb.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.openlca.olcatdb.conversion.Conversion;
import org.openlca.olcatdb.conversion.Monitor;
import org.openlca.olcatdb.conversion.Task;
import org.openlca.olcatdb.conversion.Validation;

/**
 * A modal dialog for visualization of the conversion progress in the
 * {@link ConversionPanel}.
 * 
 * @author Michael Srocka
 * 
 */
public class ProgressDialog extends JDialog implements Monitor {

	private static final long serialVersionUID = 1750356891833727862L;

	/**
	 * The conversion running in this progress.
	 */
	private Task task;

	/**
	 * The conversion panel owning this dialog.
	 */
	private ConversionPanel conversionPanel;

	/**
	 * The progress bar.
	 */
	private JProgressBar progressBar;

	/**
	 * The status label of the dialog.
	 */
	private JLabel statusLabel;

	/**
	 * The cancel button
	 */
	private JButton cancelButton;

	public ProgressDialog(String title, ConversionPanel panel, Task conversion) {
		super(panel.getFrame(panel.getParent()), title, true);
		this.task = conversion;
		this.task.setMonitor(this);
		this.conversionPanel = panel;

		// initialize the user interface
		JPanel content = (JPanel) getContentPane();
		content.setBackground(Color.WHITE);

		// the progress bar
		progressBar = new JProgressBar();
		content.add(progressBar, BorderLayout.CENTER);

		// the status label
		statusLabel = new JLabel("Start conversion...");
		JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		labelPanel.setBackground(Color.WHITE);
		content.add(labelPanel, BorderLayout.NORTH);
		labelPanel.add(statusLabel);

		// the cancel button
		cancelButton = new JButton("Cancel");
		JPanel cancelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		cancelPanel.setBackground(Color.WHITE);
		content.add(cancelPanel, BorderLayout.SOUTH);
		cancelPanel.add(cancelButton);

		// the cancel action
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (ProgressDialog.this.task != null) {
					ProgressDialog.this.statusLabel.setText("Stop process...");
					ProgressDialog.this.cancelButton.setEnabled(false);
					ProgressDialog.this.task.cancel();
				}
			}
		});

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(300, 125);

	}

	@Override
	public void begin(int size) {
		progressBar.setMaximum(size);
		progressBar.setValue(0);
	}

	@Override
	public void finished(Task task) {
		if (task instanceof Conversion) {
			conversionPanel.setResourceFolder(task.getResult());
		} else if (task instanceof Validation) {
			conversionPanel.validationPerformed();
		}
		this.dispose();
	}

	@Override
	public void progress(String step, int worked) {
		statusLabel.setText(step);
		progressBar.setValue(worked);
	}

}
