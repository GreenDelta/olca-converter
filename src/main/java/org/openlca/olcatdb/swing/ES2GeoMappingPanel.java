package org.openlca.olcatdb.swing;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.openlca.olcatdb.conversion.es2tocsv.ES2DatasetScanner;
import org.openlca.olcatdb.conversion.es2tocsv.ES2DatasetScanner.ScanMode;
import org.openlca.olcatdb.conversion.es2tocsv.ES2MappingFileUtil;

import com.greendeltatc.simapro.csv.model.types.Geography;

public class ES2GeoMappingPanel extends ES2AbstractMappingPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8478443023119978317L;

	/**
	 * The mapping list for the compartments
	 */
	private Map<String, String> rowMap = new HashMap<String, String>();

	private static ES2GeoMappingPanel instance = new ES2GeoMappingPanel();

	public static ES2GeoMappingPanel getInstance() {
		return instance;
	}

	private ES2GeoMappingPanel() {
		mappingFile = new File(path + "ES2_TO_CSV_GEOGRAPHY_MAP.csv");

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
		subtitle.setText("  Geography mapping");

		// combo box
		JComboBox comboBox = new JComboBox();
		for (String s : getComboItems()) {
			comboBox.addItem(s);
		}
		comboBox.setEnabled(true);
		comboBox.setVisible(true);

		// table

		table = new JTable();
		String subtitles[] = { "EcoSpold 2", "SimaPro " };
		String rows[][] = null;
		model = new DefaultTableModel(rows, subtitles);
		table.setModel(model);
		table.getColumnModel().getColumn(1)
				.setCellEditor(new DefaultCellEditor(comboBox));
		table.setBackground(Color.WHITE);

		tableJPane.add(table);
		tableJPane.add(new JScrollPane(table));
	}

	@Override
	protected void scanButtonClicked(MouseEvent e) {
		ES2DatasetScanner scanner = new ES2DatasetScanner(ScanMode.GEO,
				ConversionPanel.getInstance().getSourceFile());

		ProgressDialog dialog = new ProgressDialog("Scan geographies ...",
				ConversionPanel.getInstance(), scanner);
		Thread thread = new Thread(scanner);
		thread.start();

		dialog.setVisible(true);

		try {
			thread.join();
			for (String geo : scanner.getGeos()) {
				Object[] rowData = { geo, "unknown" };
				model.addRow(rowData);
			}

		} catch (InterruptedException e2) {
		}

	}

	@Override
	protected void addButtonClicked(MouseEvent e) {
		Object[] rowData = { "", "unknown" };
		model.addRow(rowData);
		table.scrollRectToVisible(table.getCellRect(table.getRowCount() - 1, 0,
				true));

	}

	@Override
	protected void setTableRowsFromFile(File file) {
		readMappingfile(file);
		for (Map.Entry<String, String> entry : rowMap.entrySet()) {
			Object[] row = { entry.getKey(), entry.getValue() };
			model.addRow(row);
		}
	}

	private void readMappingfile(File file) {
		try {
			Map<String, Geography> geoMap = new HashMap<String, Geography>();
			geoMap = ES2MappingFileUtil.readGeography(file);

			for (Map.Entry<String, Geography> entry : geoMap.entrySet()) {
				if (!entry.getKey().equals("")
						&& !entry.getKey().equals(entry.getValue().getValue())) {
					rowMap.put(entry.getKey(), entry.getValue().getValue());
				}
			}
		} catch (Exception e) {
			// TODO right error message
			JOptionPane
					.showMessageDialog(
							ConversionWindow.getInstance(),
							"Error while reading the file \"ES2_TO_CSV_GEOGRAPHY_MAP.csv\"",
							"ERROR", JOptionPane.ERROR_MESSAGE);
		}

	}

	private List<String> getComboItems() {
		List<String> list = new ArrayList<String>();
		for (Geography geography : Geography.values()) {
			list.add(geography.getValue());
		}
		Collections.sort(list);
		return list;
	}

}
