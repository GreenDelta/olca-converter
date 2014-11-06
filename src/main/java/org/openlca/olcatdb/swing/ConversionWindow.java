package org.openlca.olcatdb.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.openlca.olcatdb.database.Database;
import org.openlca.olcatdb.templates.TemplateLoader;

public class ConversionWindow extends JFrame {

	public static ConversionWindow conversionWindow;

	private static final long serialVersionUID = 3189547916668087580L;

	/**
	 * The directory where the last source file was taken from.
	 */
	static String lastSource;

	/**
	 * The directory where the last file was saved to.
	 */
	static String lastTarget;

	private JPanel menuPanel;

	public JPanel getMenuPanel() {
		return menuPanel;
	}

	private static ConversionWindow instance = new ConversionWindow();

	private Thread databaseThread;

	public Thread getDatabaseThread() {
		return databaseThread;
	}

	public static ConversionWindow getInstance() {
		return instance;
	}

	/**
	 * Creates a new instance of the conversion window.
	 */
	private ConversionWindow() {

		loadIni();

		// initialize the window
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(600, 600);
		getContentPane().setBackground(Color.WHITE);
		initComponents();
		setTitle("openLCA - Data Converter 3.0");
		setIconImage(new ImageIcon(this.getClass().getResource("app_icon.png"))
				.getImage());

		// center the frame on the display
		int width = getWidth();
		int height = getHeight();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - width) / 2;
		int y = (screen.height - height) / 2;
		setBounds(x, y, width, height);


		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				writeIni();
				try {
					Database.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		databaseThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Database.getInstance();
					TemplateLoader.getInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		databaseThread.start();
	}

	/**
	 * Loads the initialization file.
	 */
	private void loadIni() {
		File iniFile = new File("converter.ini");
		if (iniFile.exists()) {
			Properties props = new Properties();
			try {
				props.load(new FileReader(iniFile));
				lastSource = props.getProperty("lastSource");
				lastTarget = props.getProperty("lastTarget");
				String dbFolder = props.getProperty("dbFolder");
				if(dbFolder != null)
					Database.setFolder(new File(dbFolder));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void writeIni() {
		Properties props = new Properties();
		if (lastSource != null)
			props.put("lastSource", lastSource);
		if (lastTarget != null)
			props.put("lastTarget", lastTarget);
		File dbFolder = Database.getFolder();
		if(dbFolder != null)
			props.put("dbFolder", dbFolder.getAbsolutePath());
		if (!props.isEmpty()) {
			File iniFile = new File("converter.ini");
			try {
				props.store(new FileWriter(iniFile), "");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Initialize the window components.
	 */
	private void initComponents() {

		Container rootPanel = getContentPane();
		rootPanel.setLayout(new BorderLayout());

		menuPanel = new JPanel();
		menuPanel.setBackground(Color.WHITE);
		menuPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		rootPanel.add(menuPanel, BorderLayout.NORTH);
		menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 5));

		// create the menu links
		LinkGroup menuLinks = new LinkGroup();

		Link conversionLink = new Link("Conversion");
		menuPanel.add(conversionLink);
		menuLinks.manageLink(conversionLink);

		Link es2SettingLink = new Link("Settings");
		menuPanel.add(es2SettingLink);
		menuLinks.manageLink(es2SettingLink);

		Link storeLink = new Link("Database");
		menuPanel.add(storeLink);
		menuLinks.manageLink(storeLink);

		Link xpathLink = new Link("XPath Search");
		menuPanel.add(xpathLink);
		menuLinks.manageLink(xpathLink);

		Link aboutLink = new Link("About");
		menuPanel.add(aboutLink);
		menuLinks.manageLink(aboutLink);

		conversionLink.setActivated(true);

		// the content panel
		final CardLayout cardLayout = new CardLayout();
		final JPanel contentPanel = new JPanel(cardLayout);
		rootPanel.add(contentPanel, BorderLayout.CENTER);

		// the panels
		contentPanel.add(ConversionPanel.getInstance(), "Conversion");
		contentPanel.add(ES2SettingPanel.getInstance(), "Settings");
		contentPanel.add(new XPathSearchPanel(), "XPath");
		contentPanel.add(new StorePanel(), "Store");
		contentPanel.add(new AboutPanel(), "About");

		conversionLink.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				cardLayout.show(contentPanel, "Conversion");
			}
		});
		es2SettingLink.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				cardLayout.show(contentPanel, "Settings");
			}
		});
		xpathLink.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				cardLayout.show(contentPanel, "XPath");
			}
		});
		storeLink.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				cardLayout.show(contentPanel, "Store");
			}
		});
		aboutLink.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				cardLayout.show(contentPanel, "About");
			}
		});

	}

	private class Link extends JLabel {

		private static final long serialVersionUID = -7623040179235397166L;

		private String text;

		private boolean activated;

		public Link(String text) {
			super("<html><u>" + text + "</u></html>");
			this.text = text;
			setForeground(Color.BLUE);
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					if (!activated) {
						ConversionWindow.this.setCursor(new Cursor(
								Cursor.HAND_CURSOR));
					}
				}

				@Override
				public void mouseExited(MouseEvent e) {
					ConversionWindow.this.setCursor(new Cursor(
							Cursor.DEFAULT_CURSOR));
				}
			});
		}

		public void setActivated(boolean activated) {
			if (activated) {
				setText("<html><b>" + text + "</b></html>");
				setForeground(Color.GRAY);
			} else {
				setText("<html><u>" + text + "</u></html>");
				setForeground(Color.BLUE);
			}
			this.activated = activated;
		}

	}

	private class LinkGroup extends MouseAdapter {

		private List<Link> links = new ArrayList<Link>();

		public void manageLink(Link link) {
			this.links.add(link);
			link.addMouseListener(this);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			Link activatedLink = (Link) e.getSource();
			activatedLink.setActivated(true);
			for (Link link : links) {
				if (link != activatedLink) {
					link.setActivated(false);
				}
			}
		}

	}

}
