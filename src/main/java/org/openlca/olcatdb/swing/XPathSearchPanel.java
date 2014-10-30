package org.openlca.olcatdb.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import org.openlca.olcatdb.conversion.XPathSearch;

/**
 * The panel with the XPath search.
 * 
 * @author Michael Srocka
 *
 */
public class XPathSearchPanel extends JPanel {

	private static final long serialVersionUID = 4461144118652223470L;

	private JTextField sourceFileText;

	private File sourceFile;

	private JTextField queryText;

	private JEditorPane outputPane;

	public XPathSearchPanel() {
		super(new BorderLayout());
		init();
	}

	private void init() {

		this.setBackground(Color.WHITE);

		// create the search panel
		JPanel searchPanel = new JPanel(new GridBagLayout());
		searchPanel.setBackground(Color.WHITE);
		this.add(searchPanel, BorderLayout.NORTH);

		// the grid bag constraints
		GridBagConstraints grid = null;
		int row = 0;

		// the logo
		JLabel logo = new JLabel();
		logo.setIcon(new ImageIcon(this.getClass().getResource("logo.jpg")));
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.insets = new Insets(0, 10, 0, 10);
		grid.gridwidth = 4;
		searchPanel.add(logo, grid);

		row++;

		// the source file selection
		JLabel sourceLabel = new JLabel("Source:");
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.anchor = GridBagConstraints.LINE_START;
		grid.insets = new Insets(5, 5, 5, 5);
		searchPanel.add(sourceLabel, grid);

		sourceFileText = new JTextField();
		sourceFileText.setEditable(false);
		sourceFileText.setPreferredSize(new Dimension(300, 28));
		sourceFileText.setMinimumSize(new Dimension(300, 28));
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.insets = new Insets(5, 5, 5, 5);
		searchPanel.add(sourceFileText, grid);

		JButton sourceBrowseButton = new JButton();
		sourceBrowseButton.setIcon(new ImageIcon(this.getClass().getResource(
				"folder.png")));
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.insets = new Insets(5, 5, 5, 5);
		searchPanel.add(sourceBrowseButton, grid);
		sourceBrowseButton.addActionListener(new SourceFileSelection());

		row++;

		// the query text
		JLabel targetLabel = new JLabel("XPath:");
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.anchor = GridBagConstraints.LINE_START;
		grid.insets = new Insets(5, 5, 5, 5);
		searchPanel.add(targetLabel, grid);

		queryText = new JTextField();
		queryText.setEditable(true);
		queryText.setPreferredSize(new Dimension(300, 28));
		queryText.setMinimumSize(new Dimension(300, 28));
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.insets = new Insets(5, 5, 5, 5);
		searchPanel.add(queryText, grid);

		// the run button
		JButton runButton = new JButton();
		runButton
				.setIcon(new ImageIcon(this.getClass().getResource("run.png")));
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.insets = new Insets(5, 5, 5, 5);
		searchPanel.add(runButton, grid);
		runButton.addActionListener(new RunAction());

		// the output pane
		outputPane = new JEditorPane();
		outputPane.setEditable(true);
		this.add(new JScrollPane(outputPane), BorderLayout.CENTER);

		final JPopupMenu menu = new JPopupMenu();
		menu.add(new SaveAction());

		outputPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					menu.show(outputPane, e.getX(), e.getY());
				}
			}
		});

	}

	/**
	 * Saves the result to a text file.
	 */
	private class SaveAction extends AbstractAction {

		private static final long serialVersionUID = -7701604559722509406L;

		public SaveAction() {
			super("Save As...");
		}

		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setSelectedFile(new File("result.csv"));
			int save = fileChooser.showSaveDialog(XPathSearchPanel.this);
			File selFile = fileChooser.getSelectedFile();
			if (save == JFileChooser.APPROVE_OPTION && selFile != null) {

				boolean doIt = true;

				if (selFile.exists()) {
					int d = JOptionPane.showConfirmDialog(
							XPathSearchPanel.this,
							"The selected file already exists. "
									+ "Do you want to overwrite it?",
							"File exists", JOptionPane.OK_CANCEL_OPTION);
					doIt = d == JOptionPane.OK_OPTION;
				}

				if (doIt)
					try {
						StringReader reader = new StringReader(outputPane
								.getText());
						BufferedReader inBuffer = new BufferedReader(reader);
						FileWriter writer = new FileWriter(fileChooser
								.getSelectedFile());
						BufferedWriter outBuffer = new BufferedWriter(writer);

						String line = null;
						while ((line = inBuffer.readLine()) != null) {
							outBuffer.write(line);
							outBuffer.newLine();
						}

						outBuffer.flush();
						outBuffer.close();
						inBuffer.close();

					} catch (Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(XPathSearchPanel.this, ex
								.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					}
			}
		}

		public boolean isEnabled() {
			return true;
		}
	}

	/**
	 * Selection of a source file.
	 * 
	 */
	private class SourceFileSelection implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			if (ConversionWindow.lastSource != null) {
				fileChooser.setSelectedFile(new File(
						ConversionWindow.lastSource));
			}
			fileChooser.setFileFilter(new FileFilter() {

				@Override
				public String getDescription() {
					return "*.xml | .zip - XML or ZIP file";
				}

				@Override
				public boolean accept(File file) {
					String n = file.getName();
					return file.isDirectory() || n.endsWith(".XML")
							|| n.endsWith(".xml") || n.endsWith(".zip")
							|| n.endsWith(".ZIP");
				}
			});

			fileChooser.showOpenDialog(XPathSearchPanel.this);
			File selFile = fileChooser.getSelectedFile();
			if (selFile != null) {
				sourceFile = selFile;
				sourceFileText.setText(selFile.getAbsolutePath());
				sourceFileText.setSelectionStart(0);
				sourceFileText.setSelectionEnd(1);
				ConversionWindow.lastSource = selFile.getAbsolutePath();
			}
		}
	}

	/**
	 * Runs the XPath search.
	 */
	private class RunAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (sourceFile == null || !sourceFile.exists()) {
				JOptionPane.showMessageDialog(XPathSearchPanel.this,
						"There is no source file selected.", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else {

				String query = queryText.getText();
				if (query == null || query.length() == 0) {
					JOptionPane.showMessageDialog(XPathSearchPanel.this,
							"The query is empty.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}

				else {

					try {
						StringWriter writer = new StringWriter();
						BufferedWriter buffer = new BufferedWriter(writer);

						XPathSearch search = new XPathSearch(sourceFile, query);
						search.run();
						HashMap<String, String> result = search.getResult();
						if (result.isEmpty()) {
							buffer.write("no result");
							buffer.newLine();
						} else {
							List<String> keys = new ArrayList<String>(result
									.keySet());
							Collections.sort(keys);
							for (String key : keys) {
								String resLine = key + "; " + result.get(key);
								buffer.write(resLine);
								buffer.newLine();
							}
						}

						buffer.flush();
						buffer.close();

						outputPane.setText(writer.toString());
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(XPathSearchPanel.this, e2
								.getMessage(), "Error",
								JOptionPane.ERROR_MESSAGE);
					}

				}

			}

		}

	}

}
