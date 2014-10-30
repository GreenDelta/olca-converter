package org.openlca.olcatdb.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent.EventType;

/**
 * The panel with the tool information.
 * 
 * @author Michael Srocka
 * 
 */
public class AboutPanel extends JPanel {

	private static final long serialVersionUID = 7672736187363120541L;

	public AboutPanel() {
		super(new BorderLayout());
		init();
	}

	private void init() {
		this.setBackground(Color.WHITE);
		JPanel logoPanel = new JPanel(new GridBagLayout());
		logoPanel.setBackground(Color.WHITE);
		this.add(logoPanel, BorderLayout.NORTH);

		GridBagConstraints grid = null;

		int row = 0;

		JLabel logo = new JLabel();
		logo.setIcon(new ImageIcon(this.getClass().getResource("logo.jpg")));
		grid = new GridBagConstraints();
		grid.gridy = row;
		grid.insets = new Insets(0, 10, 0, 10);
		grid.gridwidth = 4;
		logoPanel.add(logo, grid);

		JEditorPane pane = new JEditorPane();
		pane.setEditable(false);
		try {
			pane.setPage(this.getClass().getResource("about.html"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.add(new JScrollPane(pane), BorderLayout.CENTER);

		pane.addHyperlinkListener(new HyperlinkListener() {

			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				try {
					if (e.getEventType() == EventType.ACTIVATED) {

						URL url = e.getURL();
						if (url != null) {
							Browser.open(url.toURI());
						}
					}
				} catch (Exception exc) {
					exc.printStackTrace();
					JOptionPane.showMessageDialog(AboutPanel.this,
							"Cannot open referenced file.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}

		});

	}

}
