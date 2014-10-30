package org.openlca.olcatdb.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.openlca.olcatdb.conversion.es2tocsv.CompartmentModel;
import org.openlca.olcatdb.conversion.es2tocsv.ES2DatasetScanner;
import org.openlca.olcatdb.conversion.es2tocsv.ES2DatasetScanner.ScanMode;
import org.openlca.olcatdb.conversion.es2tocsv.ES2MappingFileUtil;

import com.greendeltatc.simapro.csv.model.types.ElementaryFlowType;
import com.greendeltatc.simapro.csv.model.types.SubCompartment;

/**
 * 
 * @author Imo Graf
 * 
 */
public class ES2CompartmentPanel extends ES2AbstractMappingPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8478443023119978317L;

	private JComboBox comboBox;

	private static ES2CompartmentPanel instance = null;

	public static ES2CompartmentPanel getInstance() {
		if (instance == null)
			instance = new ES2CompartmentPanel();

		return instance;
	}

	/**
	 * 
	 */
	private ES2CompartmentPanel() {
		mappingFile = new File(path + "ES2_TO_CSV_COMPARTMENT_MAP.csv");
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

	/**
	 * 
	 */
	@Override
	protected void initComponents() {
		subtitle.setText("  Compartment mapping");

		comboBox = new JComboBox();
		comboBox.setEditable(true);
		comboBox.setVisible(true);

		for (ElementaryFlowType flowType : ElementaryFlowType.values()) {
			String comp = flowType.getValue();
			comboBox.addItem(comp);
			for (SubCompartment subCompartment : flowType.getSubCompartments()) {
				comboBox.addItem(comp + "/" + subCompartment.getValue());
			}
		}

		// table

		table = new JTable();
		String subtitles[] = { "ES2 Compartment", "ES2 Subcompartment",
				"SimaPro Element Type/Subcompartment" };
		String rows[][] = null;
		model = new DefaultTableModel(rows, subtitles);
		model.addTableModelListener(new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent arg0) {
				// TODO Auto-generated method stub

			}

		});

		table.setModel(model);
		table.getColumnModel().getColumn(2)
				.setCellEditor(new DefaultCellEditor(comboBox));
		// table.getColumnModel().getColumn(3)
		// .setCellEditor(new DefaultCellEditor(subCompartmentComboBox));
		table.setBackground(Color.WHITE);
		tableJPane.add(table);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(590, 450));
		tableJPane.add(scrollPane);

		table.getColumnModel().getColumn(0).setPreferredWidth(2);
		table.getColumnModel().getColumn(1).setPreferredWidth(2);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);

	}

	@Override
	protected void scanButtonClicked(MouseEvent e) {
		ES2DatasetScanner scanner = new ES2DatasetScanner(
				ScanMode.COMPARTMENTS, ConversionPanel.getInstance()
						.getSourceFile());

		ProgressDialog dialog = new ProgressDialog("Scan compartments",
				ConversionPanel.getInstance(), scanner);
		Thread thread = new Thread(scanner);
		thread.start();
		dialog.setVisible(true);

		try {
			thread.join();
			Map<String, String[]> compartments = scanner.getComparments();

			for (String a[] : compartments.values()) {
				Object[] rowData = { a[0], a[1], "-" };
				model.addRow(rowData);
			}

		} catch (InterruptedException e2) {
		}

	}

	@Override
	protected void addButtonClicked(MouseEvent e) {
		Object[] rowData = { "", "", "" };
		model.addRow(rowData);
		table.scrollRectToVisible(table.getCellRect(table.getRowCount() - 1, 0,
				true));
	}

	@Override
	protected void setTableRowsFromFile(File file) {
		List<CompartmentModel> list = null;
		try {
			list = ES2MappingFileUtil.readCompartments(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (list.size() > 0) {
			// delete all rows
			if (model.getRowCount() > 0) {
				for (int i = model.getRowCount() - 1; i > -1; i--) {
					model.removeRow(i);
				}
			}

			// set new rows
			for (CompartmentModel m : list) {
				String sima = "";
				if (m.getSimaProSubcompartment().equals("")) {
					sima = m.getSimaProElemType();
				} else {
					sima = m.getSimaProElemType() + "/"
							+ m.getSimaProSubcompartment();
				}

				Object[] rowData = { m.getEs2Compartment(),
						m.getEs2Subcompartment(), sima };
				model.addRow(rowData);
			}

		}
	}

	@Override
	public List<List<String>> getRows() {
		List<List<String>> rows = new ArrayList<List<String>>();

		TableModel tm = table.getModel();
		int colCount = table.getColumnCount();
		int rowCount = table.getRowCount();

		for (int row = 0; row < rowCount; row++) {
			List<String> list = new ArrayList<String>();

			for (int col = 0; col < colCount; col++) {
				if (col == 2) {
					String cols[] = tm.getValueAt(row, col).toString()
							.split("/");
					if (cols.length != 2) {
						list.add(cols[0]);
						list.add("");
					} else {
						list.add(cols[0]);
						list.add(cols[1]);
					}
				} else {
					list.add(tm.getValueAt(row, col).toString());
				}
			}
			rows.add(list);
		}

		return rows;
	}

}
