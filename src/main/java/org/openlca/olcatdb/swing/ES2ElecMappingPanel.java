package org.openlca.olcatdb.swing;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.openlca.olcatdb.conversion.es2tocsv.ES2DatasetScanner;
import org.openlca.olcatdb.conversion.es2tocsv.ES2DatasetScanner.ScanMode;
import org.openlca.olcatdb.conversion.es2tocsv.ES2MappingFileUtil;

public class ES2ElecMappingPanel extends ES2AbstractMappingPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8216957104204171923L;

	private static ES2ElecMappingPanel instance = new ES2ElecMappingPanel();

	public static ES2ElecMappingPanel getInstance() {
		return instance;
	}

	private ES2ElecMappingPanel() {
		mappingFile = new File(path + "ES2_TO_CSV_ELECTRICITY_UNITS.csv");
		initComponents();

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ConversionWindow.getInstance().getDatabaseThread().join();
					setTableRowsFromFile(mappingFile);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

	@Override
	protected void initComponents() {
		subtitle.setText("  Energy Units");
		scanButton.setToolTipText("Scan all units in the dataset");

		// table
		table = new JTable();
		String subtitles[] = { "Unit" };
		String rows[][] = null;
		model = new DefaultTableModel(rows, subtitles);
		table.setModel(model);
		table.setBackground(Color.WHITE);

		tableJPane.add(table);
		tableJPane.add(new JScrollPane(table));

	}

	@Override
	protected void setTableRowsFromFile(File file) {
		try {
			for (String unit : ES2MappingFileUtil.readElecricityUnits(file)) {
				Object[] rowData = { unit };
				model.addRow(rowData);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void scanButtonClicked(MouseEvent e) {
		ES2DatasetScanner scanner = new ES2DatasetScanner(ScanMode.UNITS,
				ConversionPanel.getInstance().getSourceFile());

		ProgressDialog dialog = new ProgressDialog("Scan units",
				ConversionPanel.getInstance(), scanner);
		Thread thread = new Thread(scanner);
		thread.start();
		dialog.setVisible(true);

		try {
			thread.join();
			for (String unit : scanner.getUnits()) {
				Object[] rowData = { unit };
				model.addRow(rowData);
			}

		} catch (InterruptedException e2) {
		}

	}

	@Override
	protected void addButtonClicked(MouseEvent e) {
		Object[] rowData = { "" };
		model.addRow(rowData);
		table.scrollRectToVisible(table.getCellRect(table.getRowCount() - 1, 0,
				true));

	}

}
