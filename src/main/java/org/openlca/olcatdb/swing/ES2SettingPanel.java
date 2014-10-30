package org.openlca.olcatdb.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * 
 * @author Imo Graf
 * 
 */
public class ES2SettingPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2516967703870897494L;

	private static ES2SettingPanel instance = new ES2SettingPanel();

	public static ES2SettingPanel getInstance() {
		return instance;
	}

	private ES2SettingPanel() {
		super(new BorderLayout());
		init();
	}

	private void init() {
		setLayout(new BorderLayout());

		// Logo
		setBackground(Color.WHITE);
		JPanel logoPanel = new JPanel(new GridBagLayout());
		logoPanel.setBackground(Color.WHITE);
		add(logoPanel, BorderLayout.NORTH);
		GridBagConstraints grid = null;
		int row = 0;
		JLabel logo = new JLabel();
		logo.setIcon(new ImageIcon(this.getClass().getResource("logo.jpg")));
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.insets = new Insets(0, 10, 0, 10);
		grid.gridwidth = 4;
		logoPanel.add(logo, grid);

		JTabbedPane tabbedPane = new JTabbedPane();
		add(tabbedPane, BorderLayout.CENTER);
		tabbedPane.addTab("Product name", ES2NameSettingPanel.getInstance());
		tabbedPane.addTab("Compartments", ES2CompartmentPanel.getInstance());
		tabbedPane.addTab("Geography", ES2GeoMappingPanel.getInstance());
		tabbedPane.addTab("Energy Units",
				ES2ElecMappingPanel.getInstance());

	}
}
