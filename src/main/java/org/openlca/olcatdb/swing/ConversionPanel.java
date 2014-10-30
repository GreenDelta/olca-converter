package org.openlca.olcatdb.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.apache.batik.util.gui.xmleditor.XMLEditorKit;
import org.openlca.olcatdb.ResourceFolder;
import org.openlca.olcatdb.conversion.Conversion;
import org.openlca.olcatdb.conversion.ConversionDispatch;
import org.openlca.olcatdb.conversion.Validation;
import org.openlca.olcatdb.conversion.XMLProcessFormat;
import org.openlca.olcatdb.conversion.es2tocsv.ES2DatasetScanner;
import org.openlca.olcatdb.conversion.es2tocsv.ES2DatasetScanner.ScanMode;
import org.openlca.olcatdb.database.ElemFlowMap;

/**
 * The panel for the conversion of data sets. This is the main page of the
 * application.
 * 
 * @author Michael Srocka
 * 
 */
public class ConversionPanel extends JPanel {

	private static final long serialVersionUID = 5389417304119100396L;

	/**
	 * The text field with the path to the source file.
	 */
	private JTextField sourceFileText;

	/**
	 * The text field with the target directory.
	 */
	private JTextField targetDirText;

	/**
	 * The currently selected target directory.
	 */
	private File targetDirectory;

	/**
	 * The currently selected source file.
	 */
	private File sourceFile;

	/**
	 * The combo with the possible target formats.
	 */
	private JComboBox targetFormatCombo;

	/**
	 * 
	 */
	private JCheckBox csvSplitCheckBox;

	private JCheckBox isProxyCheckBox;

	/**
	 * The output pane of the conversion.
	 */
	private JEditorPane outputPane;

	/**
	 * The button for starting a conversion.
	 */
	private JButton runButton;

	/**
	 * The output folder of the last conversion.
	 */
	private ResourceFolder lastResult;

	/**
	 * The options for the last conversion result (e.g. open in browser, ...).
	 */
	private List<JButton> resultOptions = new ArrayList<JButton>();

	// CSV-ES2 components

	private String lang = "en";

	private JTextField mdText;

	private JLabel mdLabel;

	private JButton mdButton;

	private File mdFile = null;

	private JLabel langJLabel;

	private JLabel csvCheckBoxLabel;

	private JLabel isProxyCheckBoxLabel;

	private JComboBox langComboBox;

	private JButton scanLangButton;

	private JPanel langPanel;

	private static ConversionPanel instance = null;

	private JButton validationButton;

	private ConversionPanel() {
		super(new BorderLayout());
		init();
		setES2Fields(false);
	}

	public static ConversionPanel getInstance() {
		if (instance == null)
			instance = new ConversionPanel();

		return instance;
	}

	public File getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(File sourceFile) {
		this.sourceFile = sourceFile;
	}

	/**
	 * Initializes the page content.
	 */
	private void init() {
		this.setBackground(Color.WHITE);

		JPanel configPanel = new JPanel(new GridBagLayout());
		configPanel.setBackground(Color.WHITE);
		this.add(configPanel, BorderLayout.NORTH);

		GridBagConstraints grid = null;

		int row = 0;

		JLabel logo = new JLabel();
		logo.setIcon(new ImageIcon(this.getClass().getResource("logo.jpg")));
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.insets = new Insets(0, 10, 0, 10);
		grid.gridwidth = 4;
		configPanel.add(logo, grid);

		row++;

		// the source file selection
		JLabel sourceLabel = new JLabel("Source:");
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.anchor = GridBagConstraints.LINE_START;
		grid.insets = new Insets(5, 5, 5, 5);
		configPanel.add(sourceLabel, grid);

		sourceFileText = new JTextField();
		sourceFileText.setEditable(false);
		sourceFileText.setPreferredSize(new Dimension(300, 28));
		sourceFileText.setMinimumSize(new Dimension(300, 28));
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.insets = new Insets(5, 5, 5, 5);
		configPanel.add(sourceFileText, grid);

		JButton sourceBrowseButton = new JButton();
		sourceBrowseButton.setIcon(Image.makeIcon("folder.png"));
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.insets = new Insets(5, 5, 5, 5);
		configPanel.add(sourceBrowseButton, grid);
		sourceBrowseButton.addActionListener(new SourceFileSelection());

		row++;

		// the target file selection
		JLabel targetLabel = new JLabel("Target:");
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.anchor = GridBagConstraints.LINE_START;
		grid.insets = new Insets(5, 5, 5, 5);
		configPanel.add(targetLabel, grid);

		targetDirText = new JTextField();
		targetDirText.setEditable(false);
		targetDirText.setPreferredSize(new Dimension(300, 28));
		targetDirText.setMinimumSize(new Dimension(300, 28));
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.insets = new Insets(5, 5, 5, 5);
		configPanel.add(targetDirText, grid);

		JButton targetBrowseButton = new JButton();
		targetBrowseButton.setIcon(Image.makeIcon("folder.png"));
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.anchor = GridBagConstraints.LINE_START;
		grid.insets = new Insets(5, 5, 5, 5);
		configPanel.add(targetBrowseButton, grid);
		targetBrowseButton.addActionListener(new TargetDirSelection());

		row++;

		// Master data

		mdLabel = new JLabel("Master data:");
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.anchor = GridBagConstraints.LINE_START;
		grid.insets = new Insets(5, 5, 5, 5);
		configPanel.add(mdLabel, grid);

		mdText = new JTextField();
		mdText.setEditable(false);
		mdText.setPreferredSize(new Dimension(300, 28));
		mdText.setMinimumSize(new Dimension(300, 28));
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.insets = new Insets(5, 5, 5, 5);
		configPanel.add(mdText, grid);

		mdButton = new JButton();
		mdButton.setIcon(Image.makeIcon("folder.png"));
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.insets = new Insets(5, 5, 5, 5);
		configPanel.add(mdButton, grid);
		List<String> mdFilterList = new ArrayList<String>();
		mdFilterList.add(".zip");

		mdButton.addActionListener(new MasterDataFileSelection());

		row++;

		// the config ES2 section

		langJLabel = new JLabel("Preferred language:");
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.anchor = GridBagConstraints.LINE_START;
		grid.insets = new Insets(5, 5, 5, 5);
		configPanel.add(langJLabel, grid);

		langPanel = new JPanel();
		langPanel.setBackground(Color.WHITE);
		langPanel.setLayout(new FlowLayout());
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.anchor = GridBagConstraints.LINE_START;
		grid.insets = new Insets(5, 5, 5, 5);
		configPanel.add(langPanel, grid);

		langComboBox = new JComboBox();
		langComboBox.setPreferredSize(new Dimension(100, 28));
		langComboBox.addItem("en (default)");
		langPanel.add(langComboBox);
		langComboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				String item = String.class.cast(langComboBox.getSelectedItem());
				if (item != null) {
					if (item.equals("en (default)"))
						lang = "en";
					else
						lang = item;
				}
			}
		});

		row++;

		scanLangButton = new JButton("Scan languages");
		langPanel.add(scanLangButton);
		scanLangButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ES2DatasetScanner scanner = new ES2DatasetScanner(
						ScanMode.LANG, sourceFile);

				ProgressDialog dialog = new ProgressDialog(
						"Scan languages ...", ConversionPanel.this, scanner);
				Thread thread = new Thread(scanner);
				thread.start();
				dialog.setLocationRelativeTo(ConversionPanel.this);
				dialog.setVisible(true);

				try {
					thread.join();
					if (scanner.getLangs().size() > 0)
						langComboBox.removeAllItems();
					for (String s : scanner.getLangs()) {
						if (!s.equals(""))
							langComboBox.addItem(s);
					}
				} catch (InterruptedException e2) {
				}
			}
		});

		row++;

		// csv checkbox

		csvCheckBoxLabel = new JLabel("Split into separate files:");
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.anchor = GridBagConstraints.LINE_START;
		grid.insets = new Insets(5, 5, 5, 5);
		configPanel.add(csvCheckBoxLabel, grid);

		csvSplitCheckBox = new JCheckBox();
		configPanel.add(csvSplitCheckBox, grid);

		// isProxy checkbox

		isProxyCheckBoxLabel = new JLabel("Use proxies in mappings");
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.anchor = GridBagConstraints.LINE_START;
		grid.insets = new Insets(5, 5, 5, 5);
		configPanel.add(isProxyCheckBoxLabel, grid);

		isProxyCheckBox = new JCheckBox();
		configPanel.add(isProxyCheckBox, grid);

		row++;

		JPanel runPanel = new JPanel(new FlowLayout());
		runPanel.setBackground(Color.WHITE);
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.gridwidth = 4;
		grid.insets = new Insets(0, 5, 5, 5);
		configPanel.add(runPanel, grid);

		// the target format combo
		targetFormatCombo = new JComboBox();
		targetFormatCombo.setPreferredSize(new Dimension(160, 28));
		// initialize the combo with the possible formats
		for (XMLProcessFormat format : XMLProcessFormat.values()) {
			targetFormatCombo.addItem(format);
		}
		runPanel.add(targetFormatCombo);
		targetFormatCombo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (targetFormatCombo.getSelectedItem() == XMLProcessFormat.CSV) {
					setES2Fields(true);

				} else {
					setES2Fields(false);
				}
			}
		});

		// the run button
		runButton = new JButton();
		runButton.setIcon(Image.makeIcon("run.png"));
		runButton.setDisabledIcon(Image.makeIcon("run_dis.png"));
		runButton.setEnabled(false);
		runPanel.add(runButton, grid);
		runButton.addActionListener(new RunAction());

		// create the browser panel with navigation, editor, and progress bar
		JPanel browserPanel = new JPanel(new BorderLayout());
		this.add(browserPanel, BorderLayout.CENTER);
		browserPanel.setBackground(Color.WHITE);

		// creates the buttons with the options
		// for a conversion result
		JPanel buttonPanel = buttonPanel();
		browserPanel.add(buttonPanel, BorderLayout.NORTH);

		// creates the
		editorPane();
		browserPanel.add(new JScrollPane(outputPane), BorderLayout.CENTER);

	}

	private void setES2Fields(final boolean visible) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (visible) {
					ConversionWindow.getInstance().setSize(600, 670);
					ConversionWindow.getInstance().getMenuPanel()
							.getComponent(1).setVisible(visible);
				} else {
					ConversionWindow.getInstance().setSize(600, 600);
					ConversionWindow.getInstance().getMenuPanel()
							.getComponent(1).setVisible(visible);
				}
			}
		});

		langJLabel.setVisible(visible);
		langPanel.setVisible(visible);
		csvSplitCheckBox.setVisible(visible);
		csvCheckBoxLabel.setVisible(visible);

		isProxyCheckBox.setVisible(!visible);
		isProxyCheckBoxLabel.setVisible(!visible);

		// TODO read the master data currently not used
		mdButton.setVisible(false);
		mdLabel.setVisible(false);
		mdText.setVisible(false);
	}

	/**
	 * Create the editor.
	 */
	private void editorPane() {
		outputPane = new JEditorPane();
		outputPane.setEditorKitForContentType(XMLEditorKit.XML_MIME_TYPE,
				new XMLEditorKit());
		try {
			outputPane.setPage(this.getClass().getResource("welcome.html"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		outputPane.setEditable(false);
		outputPane.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				try {
					if (e.getEventType() == EventType.ACTIVATED) {

						URL url = e.getURL();
						if (url == null) {
							File f = new File(e.getDescription());
							if (!f.exists()) {
								throw new RuntimeException(
										"Unexpectedly file not found from hyperlinkevent: "
												+ e + ", desc: "
												+ e.getDescription());
							}
							outputPane.setPage(f.toURI().toURL());
							showCSVOrXML(f.getAbsolutePath());

						}
						if (url != null) {

							if (url.toString().startsWith("http")) {

								// open in an external browser
								Browser.open(url.toURI());

							} else {

								String file = e.getURL().getFile();
								outputPane.setPage(e.getURL());
								showCSVOrXML(file);
							}
						}
					}
				} catch (Exception exc) {
					JOptionPane.showMessageDialog(ConversionPanel.this,
							"Cannot open referenced file.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	protected void showCSVOrXML(String file) throws BadLocationException {
		if (file.toLowerCase().endsWith(".xml")
				|| file.toLowerCase().endsWith(".csv")) {
			// show the content in the XML editor
			Document doc = outputPane.getDocument();
			String text = doc.getText(doc.getStartPosition().getOffset(), doc
					.getEndPosition().getOffset());
			outputPane.setContentType(XMLEditorKit.XML_MIME_TYPE);
			outputPane.setText(text);
		}
	}

	/**
	 * Creates the panel with the options for a conversion result.
	 */
	private JPanel buttonPanel() {
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 5));
		buttonPanel.setBackground(Color.WHITE);

		// the home button
		JButton homeButton = new JButton();
		homeButton.setIcon(Image.makeIcon("home.png"));
		homeButton.setDisabledIcon(Image.makeIcon("home_dis.png"));
		homeButton.setToolTipText("Conversion index");
		homeButton.setEnabled(false);
		buttonPanel.add(homeButton);
		homeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (lastResult != null) {
					File indexFile = lastResult.getIndexFile();
					if (indexFile != null) {
						try {
							outputPane.setPage(indexFile.toURI().toURL());
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(ConversionPanel.this,
									ex.getMessage(), "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		});
		resultOptions.add(homeButton);

		validationButton = new JButton();
		validationButton.setIcon(Image.makeIcon("validation.png"));
		validationButton.setDisabledIcon(Image.makeIcon("validation_dis.png"));
		validationButton.setEnabled(false);
		validationButton.setToolTipText("Validate output");
		buttonPanel.add(validationButton);
		validationButton.addActionListener(new ValidationAction());
		resultOptions.add(validationButton);

		// the browser button
		JButton browserButton = new JButton();
		browserButton.setIcon(Image.makeIcon("browser.png"));
		browserButton.setDisabledIcon(Image.makeIcon("browser_dis.png"));
		browserButton.setEnabled(false);
		browserButton.setToolTipText("Open in browser");
		buttonPanel.add(browserButton);
		browserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (lastResult != null) {
					File indexFile = lastResult.getIndexFile();
					if (indexFile != null) {
						try {
							Browser.open(indexFile.toURI());
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(ConversionPanel.this,
									ex.getMessage(), "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		});
		resultOptions.add(browserButton);

		// the file system button
		JButton folderButton = new JButton();
		folderButton.setIcon(Image.makeIcon("file_system.png"));
		folderButton.setDisabledIcon(Image.makeIcon("file_system_dis.png"));
		folderButton.setEnabled(false);
		folderButton.setToolTipText("Open in file explorer");
		buttonPanel.add(folderButton);
		folderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (lastResult != null && lastResult.getRootDir() != null
						&& lastResult.getRootDir().exists()) {
					Browser.open(lastResult.getRootDir().toURI());
				}
			}
		});
		resultOptions.add(folderButton);

		return buttonPanel;
	}

	/**
	 * Sets the given folder as last conversion result of this page.
	 */
	void setResourceFolder(ResourceFolder folder) {
		this.lastResult = folder;
		if (folder != null) {
			File indexFile = folder.getIndexFile();
			if (indexFile != null) {
				try {
					Document doc = outputPane.getDocument();
					doc.putProperty(Document.StreamDescriptionProperty, null);
					outputPane.setPage(indexFile.toURI().toURL());

				} catch (Exception e) {
					JOptionPane.showMessageDialog(ConversionPanel.this,
							e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		for (JButton b : resultOptions) {
			b.setEnabled(folder != null);
		}
	}

	void validationPerformed() {
		if (lastResult != null) {
			File logFile = lastResult.getValidationLog();
			if (logFile != null && logFile.exists()) {
				try {
					outputPane.setPage(logFile.toURI().toURL());
				} catch (Exception e) {
					JOptionPane.showMessageDialog(ConversionPanel.this,
							e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	/**
	 * Checks if the conversion can started. A c
	 */
	private void checkCanRun() {

		boolean canRun = false;

		// test the target directory
		canRun = targetDirectory != null && targetDirectory.isDirectory();

		// test the source file
		if (canRun) {
			canRun = sourceFile != null && sourceFile.isFile();
		}

		// set the button enabled / disabled
		if (runButton != null) {
			runButton.setEnabled(canRun);
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
					return "*.csv | *.spold | *.xml | *.zip - XML or ZIP file with LCA process data";
				}

				@Override
				public boolean accept(File file) {
					String n = file.getName().toLowerCase();
					boolean b = false;
					List<String> list = new ArrayList<String>();
					list.add(".xml");
					list.add(".zip");
					list.add(".spold");
					list.add(".csv");

					for (String s : list) {
						if (n.endsWith(s)) {
							b = true;
							break;
						}
					}

					return file.isDirectory() || b;
				}
			});

			fileChooser.showOpenDialog(ConversionPanel.this);
			File selFile = fileChooser.getSelectedFile();
			if (selFile != null) {
				sourceFileText.setText(selFile.getAbsolutePath());
				sourceFileText.setSelectionStart(0);
				sourceFileText.setSelectionEnd(1);
				sourceFile = selFile;
				ConversionWindow.lastSource = selFile.getAbsolutePath();

			}
			checkCanRun();
		}
	}

	/**
	 * Selection of a source file.
	 * 
	 * @author Imo Graf
	 * 
	 */
	private class MasterDataFileSelection implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();

			fileChooser.setFileFilter(new FileFilter() {

				@Override
				public String getDescription() {
					return "*.zip - ZIP file with LCA master data";
				}

				@Override
				public boolean accept(File file) {
					String n = file.getName().toLowerCase();
					return file.isDirectory() || n.endsWith(".zip");
				}
			});

			fileChooser.showOpenDialog(ConversionPanel.this);
			File selFile = fileChooser.getSelectedFile();
			if (selFile != null) {
				mdText.setText(selFile.getAbsolutePath());
				mdFile = selFile;
			}

		}
	}

	/**
	 * Selection of the target directory.
	 * 
	 * @author Michael Srocka
	 * 
	 */
	private class TargetDirSelection implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if (ConversionWindow.lastTarget != null) {
				fileChooser.setSelectedFile(new File(
						ConversionWindow.lastTarget));
			}
			fileChooser.showOpenDialog(ConversionPanel.this);
			File file = fileChooser.getSelectedFile();
			if (file != null && file.isDirectory()) {
				targetDirText.setText(file.getAbsolutePath());
				targetDirectory = file;
				targetDirText.setSelectionStart(0);
				targetDirText.setSelectionEnd(1);
				ConversionWindow.lastTarget = file.getAbsolutePath();
			}
			checkCanRun();
		}
	}

	/**
	 * The action that starts the conversion.
	 * 
	 * @author Michael Srocka
	 * 
	 */
	private class RunAction implements ActionListener {
		private XMLProcessFormat targetFormat;

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				ElemFlowMap.setIsProxy(isProxyCheckBox.isSelected());

				targetFormat = (XMLProcessFormat) targetFormatCombo
						.getSelectedItem();

				Conversion conversion = ConversionDispatch.createConversion(
						sourceFile, targetDirectory, targetFormat,
						csvSplitCheckBox.isSelected(), lang, mdFile);
				ProgressDialog dialog = new ProgressDialog("Run conversion",
						ConversionPanel.this, conversion);
				Thread thread = new Thread(conversion);
				thread.start();
				dialog.setLocationRelativeTo(ConversionPanel.this);
				dialog.setVisible(true);
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(ConversionPanel.this,
						ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}

			if (targetFormat == XMLProcessFormat.CSV)
				validationButton.setEnabled(false);
			else
				validationButton.setEnabled(true);
		}
	}

	private class ValidationAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				if (lastResult == null) {
					JOptionPane.showMessageDialog(ConversionPanel.this,
							"Cannot run validation", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					Validation validation = new Validation(lastResult);
					ProgressDialog dialog = new ProgressDialog(
							"Run validation", ConversionPanel.this, validation);
					Thread thread = new Thread(validation);
					thread.start();
					dialog.setLocationRelativeTo(ConversionPanel.this);
					dialog.setVisible(true);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(ConversionPanel.this,
						ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Get the window of the given container.
	 */
	JFrame getFrame(Container container) {
		if (container instanceof JFrame) {
			return (JFrame) container;
		} else if (container.getParent() != null) {
			return getFrame(container.getParent());
		} else {
			return null;
		}
	}

}
