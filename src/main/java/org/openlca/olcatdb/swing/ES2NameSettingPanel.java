package org.openlca.olcatdb.swing;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import org.openlca.olcatdb.conversion.es2tocsv.ProductNameSavePreferences;

public class ES2NameSettingPanel extends JPanel {

	private static final long serialVersionUID = -3021253759031890669L;
	private ProductNameSavePreferences saves = new ProductNameSavePreferences();
	private JCheckBox chckbxFlowName;
	private JCheckBox chckbxActivityName;
	private JCheckBox chckbxGeography;
	private JCheckBox chckbxSystemModelName;
	private JCheckBox chckbxActivityType;
	private JTextField txtFlowName;
	private JTextField txtGeo;
	private JTextField txtActivityName;
	private JTextField txtSystemModelName;
	private JTextField txtType;
	private JButton btnSave;
	private JButton btnDefaults;
	private JLabel lblExample;
	private JLabel lblSpecifyTheOrder;
	private JLabel lblNewLabel;
	private Queue<String> valueOfCurrentTextField = new LinkedList<String>();

	private static ES2NameSettingPanel instance = new ES2NameSettingPanel();

	public static ES2NameSettingPanel getInstance() {
		return instance;
	}

	private ES2NameSettingPanel() {
		initComponentsWithBuilder();
		initComponents();
		setValuesToComponents();
	}

	private void initComponentsWithBuilder() {
		setBackground(Color.WHITE);

		chckbxFlowName = new JCheckBox("Flow Name (1000)");
		chckbxActivityName = new JCheckBox("Activity Name (100)");
		chckbxGeography = new JCheckBox("Geography (410)");
		chckbxSystemModelName = new JCheckBox("System Model Name (3005)");
		chckbxActivityType = new JCheckBox("Activity Type (110)");

		JLabel lblSelect = new JLabel(
				"Name (FieldID) from the Ecospold 2 documentation");

		JLabel lblSort = new JLabel("Order");

		btnSave = new JButton();
		btnSave.setIcon(new ImageIcon(this.getClass().getResource(
				"save_edit.gif")));
		btnSave.setToolTipText("Save");

		btnDefaults = new JButton();

		btnDefaults.setIcon(new ImageIcon(this.getClass().getResource(
				"defaults_ps.gif")));
		btnDefaults.setToolTipText("Restore Defaults");

		txtGeo = new JTextField();
		txtGeo.setHorizontalAlignment(SwingConstants.CENTER);
		txtGeo.setColumns(1);

		txtActivityName = new JTextField();
		txtActivityName.setHorizontalAlignment(SwingConstants.CENTER);
		txtActivityName.setColumns(1);

		txtSystemModelName = new JTextField();
		txtSystemModelName.setHorizontalAlignment(SwingConstants.CENTER);
		txtSystemModelName.setColumns(1);

		txtType = new JTextField();
		txtType.setHorizontalAlignment(SwingConstants.CENTER);
		txtType.setColumns(1);

		txtFlowName = new JTextField();
		txtFlowName.setHorizontalAlignment(SwingConstants.CENTER);
		txtFlowName.setColumns(1);

		lblExample = new JLabel("Example with default Settings (1,2,3,4,5):\n");

		lblSpecifyTheOrder = new JLabel(
				"Specify the order of elements in product flow names.");

		lblNewLabel = new JLabel(new ImageIcon());

		JLabel lblNewLabel_1 = new JLabel("");

		lblNewLabel_1.setIcon(Image.makeIcon("productNameExample.png"));

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout
				.setHorizontalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addContainerGap()
																		.addGroup(
																				groupLayout
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addGroup(
																								groupLayout
																										.createSequentialGroup()
																										.addGroup(
																												groupLayout
																														.createParallelGroup(
																																Alignment.LEADING)
																														.addGroup(
																																groupLayout
																																		.createSequentialGroup()
																																		.addGroup(
																																				groupLayout
																																						.createParallelGroup(
																																								Alignment.LEADING)
																																						.addGroup(
																																								groupLayout
																																										.createParallelGroup(
																																												Alignment.LEADING,
																																												false)
																																										.addComponent(
																																												txtSystemModelName,
																																												GroupLayout.DEFAULT_SIZE,
																																												35,
																																												Short.MAX_VALUE)
																																										.addComponent(
																																												txtType,
																																												GroupLayout.DEFAULT_SIZE,
																																												35,
																																												Short.MAX_VALUE)
																																										.addComponent(
																																												txtGeo,
																																												GroupLayout.DEFAULT_SIZE,
																																												35,
																																												Short.MAX_VALUE)
																																										.addComponent(
																																												txtActivityName))
																																						.addComponent(
																																								txtFlowName,
																																								GroupLayout.PREFERRED_SIZE,
																																								35,
																																								GroupLayout.PREFERRED_SIZE))
																																		.addGap(18))
																														.addGroup(
																																groupLayout
																																		.createSequentialGroup()
																																		.addComponent(
																																				lblSort,
																																				GroupLayout.DEFAULT_SIZE,
																																				47,
																																				Short.MAX_VALUE)
																																		.addPreferredGap(
																																				ComponentPlacement.RELATED)))
																										.addGroup(
																												groupLayout
																														.createParallelGroup(
																																Alignment.LEADING)
																														.addGroup(
																																groupLayout
																																		.createSequentialGroup()
																																		.addGroup(
																																				groupLayout
																																						.createParallelGroup(
																																								Alignment.LEADING)
																																						.addComponent(
																																								chckbxActivityName)
																																						.addComponent(
																																								chckbxGeography)
																																						.addGroup(
																																								groupLayout
																																										.createSequentialGroup()
																																										.addComponent(
																																												chckbxActivityType)
																																										.addPreferredGap(
																																												ComponentPlacement.RELATED,
																																												215,
																																												Short.MAX_VALUE))
																																						.addComponent(
																																								chckbxSystemModelName)
																																						.addComponent(
																																								lblSelect,
																																								GroupLayout.PREFERRED_SIZE,
																																								365,
																																								GroupLayout.PREFERRED_SIZE))
																																		.addGap(21))
																														.addComponent(
																																chckbxFlowName,
																																GroupLayout.PREFERRED_SIZE,
																																189,
																																GroupLayout.PREFERRED_SIZE)))
																						.addComponent(
																								lblExample)
																						.addComponent(
																								lblNewLabel)
																						.addComponent(
																								lblNewLabel_1,
																								GroupLayout.DEFAULT_SIZE,
																								439,
																								Short.MAX_VALUE)
																						.addComponent(
																								lblSpecifyTheOrder)))
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addComponent(
																				btnSave)
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				btnDefaults)))
										.addGap(5)));
		groupLayout
				.setVerticalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(btnSave)
														.addComponent(
																btnDefaults))
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addComponent(lblSpecifyTheOrder)
										.addPreferredGap(
												ComponentPlacement.UNRELATED)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(lblSort)
														.addComponent(lblSelect))
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																txtFlowName,
																GroupLayout.PREFERRED_SIZE,
																23,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																chckbxFlowName))
										.addGap(4)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																txtGeo,
																GroupLayout.PREFERRED_SIZE,
																23,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																chckbxGeography))
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																txtActivityName,
																GroupLayout.PREFERRED_SIZE,
																23,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																chckbxActivityName))
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																chckbxSystemModelName)
														.addComponent(
																txtSystemModelName,
																GroupLayout.PREFERRED_SIZE,
																23,
																GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																chckbxActivityType)
														.addComponent(
																txtType,
																GroupLayout.PREFERRED_SIZE,
																23,
																GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addComponent(lblExample)
										.addPreferredGap(
												ComponentPlacement.RELATED)
										.addComponent(lblNewLabel_1)
										.addGap(63)
										.addComponent(lblNewLabel)
										.addContainerGap(
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));
		setLayout(groupLayout);
	}

	private void setDefaultsClicked(MouseEvent e) {
		saves = new ProductNameSavePreferences();
		ProductNameSavePreferences.writeDatabase(saves);
		setValuesToComponents();
	}

	private void initComponents() {

		btnSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				setSavedValues();
			}
		});

		btnDefaults.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setDefaultsClicked(e);
			}
		});

		txtFlowName.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0) {
				// textField1.setText(checkTextField(textField1));
				changeTextInput(txtFlowName);

			}

			@Override
			public void focusGained(FocusEvent arg0) {
				textFieldFocusGained(arg0, txtFlowName);
			}
		});
		txtGeo.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0) {
				// textField2.setText(checkTextField(textField2));
				changeTextInput(txtGeo);

			}

			@Override
			public void focusGained(FocusEvent arg0) {
				textFieldFocusGained(arg0, txtGeo);

			}
		});

		txtActivityName.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0) {
				// textField3.setText(checkTextField(textField3));
				changeTextInput(txtActivityName);

			}

			@Override
			public void focusGained(FocusEvent arg0) {
				textFieldFocusGained(arg0, txtActivityName);

			}
		});

		txtSystemModelName.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0) {
				// textField4.setText(checkTextField(textField4));
				changeTextInput(txtSystemModelName);

			}

			@Override
			public void focusGained(FocusEvent arg0) {
				textFieldFocusGained(arg0, txtSystemModelName);

			}
		});

		txtType.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent arg0) {
				// textField5.setText(checkTextField(textField5));
				changeTextInput(txtType);

			}

			@Override
			public void focusGained(FocusEvent arg0) {
				textFieldFocusGained(arg0, txtType);

			}

		});

	}

	private void textFieldFocusGained(FocusEvent arg0, JTextField textField) {
		valueOfCurrentTextField.add(textField.getText());
	}

	private void setMapValueSafe(Map<Integer, JTextField> map,
			JTextField textField, JTextField changedField, String oldValue) {
		if (textField != changedField) {
			map.put(Integer.valueOf(textField.getText()), textField);
		} else {
			map.put(Integer.valueOf(oldValue), textField);
		}
	}

	private void changeTextInput(JTextField textField) {
		String oldValue = valueOfCurrentTextField.poll();
		Map<Integer, JTextField> map = new HashMap<Integer, JTextField>();
		setMapValueSafe(map, txtFlowName, textField, oldValue);
		setMapValueSafe(map, txtGeo, textField, oldValue);
		setMapValueSafe(map, txtActivityName, textField, oldValue);
		setMapValueSafe(map, txtSystemModelName, textField, oldValue);
		setMapValueSafe(map, txtType, textField, oldValue);

		try {
			int value = Integer.parseInt(textField.getText());

			if (value < 1 || value > 5) {
				textField.setText(oldValue);
				showWrongInputMessage();
			} else {
				map.get(value).setText(oldValue);
			}

		} catch (NumberFormatException e) {
			showWrongInputMessage();
		}

	}

	private void showWrongInputMessage() {
		String message = "Only allow the characters from 1 to 5.";
		JOptionPane.showMessageDialog(new JFrame(), message);
	}

	public void setSavedValues() {
		saves.setChkBoxFlowName(chckbxFlowName.isSelected());
		saves.setChkBoxGeo(chckbxGeography.isSelected());
		saves.setChkBoxActivityName(chckbxActivityName.isSelected());
		saves.setChkBoxSystemModelName(chckbxSystemModelName.isSelected());
		saves.setChkBoxActivityType(chckbxActivityType.isSelected());
		saves.setSortFlowName(Integer.parseInt(txtFlowName.getText()));
		saves.setSortGeo(Integer.parseInt(txtGeo.getText()));
		saves.setSortActivityName(Integer.parseInt(txtActivityName.getText()));
		saves.setSortSystemModelName(Integer.parseInt(txtSystemModelName
				.getText()));
		saves.setSortAcitvityType(Integer.parseInt(txtType.getText()));

		if (!ProductNameSavePreferences.writeDatabase(saves)) {
			JOptionPane.showMessageDialog(new JFrame(), "Save Error!");
		} else {
			JOptionPane.showMessageDialog(new JFrame(), "Saved!");
		}
	}

	public void setValuesToComponents() {
		saves = ProductNameSavePreferences.readDatabase();
		chckbxFlowName.setSelected(saves.isChkBoxFlowName());
		chckbxActivityName.setSelected(saves.isChkBoxActivityName());
		chckbxGeography.setSelected(saves.isChkBoxGeo());
		chckbxSystemModelName.setSelected(saves.isChkBoxSystemModelName());
		chckbxActivityType.setSelected(saves.isChkBoxActivityType());
		txtFlowName.setText(String.valueOf(saves.getSortFlowName()));
		txtGeo.setText(String.valueOf(saves.getSortGeo()));
		txtActivityName.setText(String.valueOf(saves.getSortActivityName()));
		txtSystemModelName.setText(String.valueOf(saves
				.getSortSystemModelName()));
		txtType.setText(String.valueOf(saves.getSortAcitvityType()));
	}
}
