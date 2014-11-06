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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.openlca.olcatdb.database.Database;

public class StorePanel extends JPanel {

	private static final long serialVersionUID = -576755786625142901L;

	private JTextField queryText;

	private JEditorPane outputPane;

	public StorePanel() {
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

		// the database selection text
		JLabel folderLabel = new JLabel("Database folder:");
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.anchor = GridBagConstraints.LINE_START;
		grid.insets = new Insets(5, 5, 5, 5);
		searchPanel.add(folderLabel, grid);

		JTextField folderText = new JTextField();
		folderText.setText(Database.getFolder().getAbsolutePath());
		folderText.setEditable(false);
		folderText.setPreferredSize(new Dimension(300, 28));
		folderText.setMinimumSize(new Dimension(300, 28));
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.insets = new Insets(5, 5, 5, 5);
		searchPanel.add(folderText, grid);

		JButton folderButton = new JButton();
		folderButton.setToolTipText("Select the database location");
		folderButton.setIcon(new ImageIcon(this.getClass()
				.getResource("folder.png")));
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.insets = new Insets(5, 5, 5, 5);
		searchPanel.add(folderButton, grid);

		row++;

		// the query text
		JLabel sourceLabel = new JLabel("Query:");
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.anchor = GridBagConstraints.LINE_START;
		grid.insets = new Insets(5, 5, 5, 5);
		searchPanel.add(sourceLabel, grid);

		queryText = new JTextField();
		queryText.setText("SELECT * FROM ES1_COMPARTMENTS");
		queryText.setEditable(true);
		queryText.setPreferredSize(new Dimension(300, 28));
		queryText.setMinimumSize(new Dimension(300, 28));
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.insets = new Insets(5, 5, 5, 5);
		searchPanel.add(queryText, grid);

		JButton runButton = new JButton();
		runButton
				.setIcon(new ImageIcon(this.getClass().getResource("run.png")));
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.insets = new Insets(5, 5, 5, 5);
		searchPanel.add(runButton, grid);
		runButton.addActionListener(new SearchAction());
		runButton.setToolTipText("Run SQL query");

		row++;

		// the output pane
		outputPane = new JEditorPane();
		outputPane.setEditable(true);
		this.add(new JScrollPane(outputPane), BorderLayout.CENTER);
		final JPopupMenu menu = new JPopupMenu();
		outputPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					menu.show(outputPane, e.getX(), e.getY());
				}
			}
		});

		folderButton.addActionListener(
				new SwitchDatabaseAction(folderText, outputPane));
	}

	private class SearchAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				StringBuilder builder = new StringBuilder();
				String query = queryText.getText();
				ResultSet resultSet = Database.getInstance().query(query);
				ResultSetMetaData md = resultSet.getMetaData();
				int c = md.getColumnCount();
				while (resultSet.next()) {
					for (int i = 1; i <= c; i++) {
						Object o = resultSet.getObject(i);
						if (o != null)
							builder.append(o.toString());
						if(i < c)
							builder.append("; ");
					}
					builder.append("\n");
				}
				outputPane.setText(builder.toString());

			} catch (Exception exc) {
				exc.printStackTrace();
				JOptionPane.showMessageDialog(StorePanel.this,
						exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}

		}
	}
}
