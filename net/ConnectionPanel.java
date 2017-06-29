package net;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import progettoIGPE.davide.giovanni.unical2016.GUI.MainFrame;
import progettoIGPE.davide.giovanni.unical2016.GUI.PanelSwitcher;

@SuppressWarnings("serial")
public class ConnectionPanel extends JPanel {

	private final JTextField ipTextField;
	private final MainFrame mainFrame;
	private final JTextField nameTextField;
	private final JTextField portTextField;

	public ConnectionPanel(int w, int h, PanelSwitcher switcher) {
		super(new BorderLayout());
		
		mainFrame = ((MainFrame)switcher);
		final JPanel content = new JPanel(new GridBagLayout());
		final GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(20, 20, 20, 20);
		constraints.gridx = 0;
		constraints.gridy = 0;
		content.add(new JLabel("Server IP"), constraints);
		ipTextField = new JTextField(20);
		ipTextField.setText("127.0.0.1");
		constraints.gridx++;
		content.add(ipTextField, constraints);
		constraints.gridy++;
		constraints.gridx = 0;
		content.add(new JLabel("Server Port"), constraints);
		portTextField = new JTextField(20);
		portTextField.setText("1234");
		constraints.gridx++;
		content.add(portTextField, constraints);
		constraints.gridy++;
		constraints.gridx = 0;
		content.add(new JLabel("Name"), constraints);
		nameTextField = new JTextField(20);
		nameTextField.setText("P1");
		constraints.gridx++;
		content.add(nameTextField, constraints);
		constraints.gridy++;
		constraints.gridx = 0;

		final JButton connectButton = new JButton("Connect");
		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				connectButton.setEnabled(false);
				new Thread() {
					@Override
					public void run() {
						try {
							connectoToServer();
						} catch (final Exception e) {
							JOptionPane.showMessageDialog(connectButton,
									"Impossible to connect to " + ipTextField.getText() + ":" + portTextField.getText(),
									"Network Error", JOptionPane.ERROR_MESSAGE);
						}
					};
				}.start();

			}
		});
		content.add(connectButton, constraints);
		add(content, BorderLayout.CENTER);
	}

	protected void connectoToServer() throws Exception {
		final Socket socket = new Socket(ipTextField.getText(), Integer.parseInt(portTextField.getText()));
		final ConnectionManager connectionManager = new ConnectionManager(socket, nameTextField.getText(), mainFrame);
		new Thread(connectionManager, "Connection Manager").start();
	}
}
