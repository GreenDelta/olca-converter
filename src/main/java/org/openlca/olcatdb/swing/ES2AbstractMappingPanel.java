package org.openlca.olcatdb.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.openlca.olcatdb.conversion.es2tocsv.ES2MappingFileUtil;

public abstract class ES2AbstractMappingPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4964792093986966944L;

	protected String path = System.getProperty("user.dir") + File.separator
			+ "database" + File.separator;

	protected File mappingFile;

	protected JTable table;

	protected DefaultTableModel model;

	protected JPanel tableJPane;

	protected JLabel subtitle;

	protected JButton removeButton;

	protected JButton addButton;

	protected JButton saveButton;

	protected JButton openButton;

	protected JButton scanButton;

	protected final FileFilter csvFileFilter = new FileFilter() {

		@Override
		public String getDescription() {
			return "*.csv";
		}

		@Override
		public boolean accept(File f) {
			String n = f.getName().toLowerCase();
			return f.isDirectory() || n.endsWith(".csv");
		}
	};

	public ES2AbstractMappingPanel() {
		initDefaultComponents();
	}

	protected abstract void initComponents();

	protected abstract void setTableRowsFromFile(File file);

	protected abstract void scanButtonClicked(MouseEvent e);

	protected abstract void addButtonClicked(MouseEvent e);

	protected void removeButtonClicked(MouseEvent e) {
		int[] rows = table.getSelectedRows();
		TableModel tm = table.getModel();

		while (rows.length > 0) {
			((DefaultTableModel) tm).removeRow(table
					.convertRowIndexToModel(rows[0]));
			rows = table.getSelectedRows();
		}
		table.clearSelection();
	}

	protected void openButtonClicked(MouseEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(csvFileFilter);
		fileChooser.setSelectedFile(mappingFile);
		int status = fileChooser.showOpenDialog(ES2SettingPanel.getInstance());
		if (status == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			if (file != null)
				setTableRowsFromFile(file);
		}
	}

	protected void saveButtonClicked(MouseEvent e) {
		try {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(csvFileFilter);
			fileChooser.setSelectedFile(mappingFile);
			int status = fileChooser.showSaveDialog(ES2SettingPanel
					.getInstance());
			if (status == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();

				if (!file.getName().toLowerCase().endsWith(".csv")) {
					file = new File(file.getAbsolutePath() + ".csv");
					System.out.println(file.getAbsolutePath());
				}

				List<List<String>> rows = getRows();
				ES2MappingFileUtil.writeCSVFile(file, rows);
				JOptionPane.showMessageDialog(ES2SettingPanel.getInstance(),
						"Your mapping file was successfully saved to: \""
								+ file.getAbsolutePath() + "\"");
			}
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(ConversionWindow.getInstance(),
					"Write error", "", JOptionPane.ERROR_MESSAGE);
		}

	}

	protected void initDefaultComponents() {
		setBackground(Color.WHITE);
		setLayout(new BorderLayout());

		subtitle = new JLabel();

		tableJPane = new JPanel();
		tableJPane.setBackground(Color.WHITE);
		add(tableJPane, BorderLayout.WEST);

		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.setBackground(Color.WHITE);
		add(northPanel, BorderLayout.NORTH);

		JPanel menuPanel = new JPanel();
		menuPanel.setBackground(Color.WHITE);
		menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		northPanel.add(subtitle, BorderLayout.NORTH);
		northPanel.add(menuPanel, BorderLayout.SOUTH);

		scanButton = new JButton();
		menuPanel.add(scanButton);
		scanButton.setIcon(new ImageIcon(this.getClass().getResource(
				"search_decl_obj.gif")));
		scanButton.setToolTipText("Scan the data in the source file. It may take a while.");
		scanButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				scanButtonClicked(e);
			}

		});

		openButton = new JButton();
		openButton.setIcon(new ImageIcon(this.getClass().getResource(
				"opentype.gif")));
		openButton.setToolTipText("Open...");
		menuPanel.add(openButton);
		openButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				openButtonClicked(e);
			}

		});

		saveButton = new JButton();
		saveButton.setIcon(new ImageIcon(this.getClass().getResource(
				"save_edit.gif")));
		saveButton.setToolTipText("Save");
		menuPanel.add(saveButton);
		saveButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				saveButtonClicked(e);
			}

		});

		addButton = new JButton();
		addButton.setIcon(new ImageIcon(this.getClass().getResource(
				"add_obj.gif")));
		addButton.setToolTipText("Add row");
		menuPanel.add(addButton);
		addButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				addButtonClicked(e);
			}

		});

		removeButton = new JButton();
		removeButton.setIcon(new ImageIcon(this.getClass().getResource(
				"delete_obj.gif")));
		removeButton.setToolTipText("Remove selected rows");
		menuPanel.add(removeButton);
		removeButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				removeButtonClicked(e);
			}

		});
	}

	public List<List<String>> getRows() {
		List<List<String>> rows = new ArrayList<List<String>>();

		TableModel tm = table.getModel();
		int colCount = table.getColumnCount();
		int rowCount = table.getRowCount();

		for (int row = 0; row < rowCount; row++) {
			List<String> list = new ArrayList<String>();

			for (int col = 0; col < colCount; col++) {
				list.add(tm.getValueAt(row, col).toString());
			}
			rows.add(list);
		}

		return rows;
	}

}
