package mweirich.mcrenamer;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class GUI {

	Renamer r;
	private JButton okButton;
	private JTextField newNameField;
	private JLabel oldName;
	private JLabel statusLabel;

	public GUI() {
		r = new Renamer();
		initFrame();
		try {
			oldName.setText(r.load());
		} catch (FileNotFoundException ex) {
			statusLabel.setText("launcher_profiles.json konnte nicht gefunden werden. Ist minecraft installiert?");
		} catch (com.eclipsesource.json.ParseException ex) {
			statusLabel.setText("launcher_profiles.json enthällt Fehler!");
		} catch (IOException e) {
			statusLabel.setText("Fehler beim öffnen der Datei");
		}
		statusLabel.setText("Profil geladen");
	}

	private void initFrame() {
		JFrame frame = new JFrame("MC Renamer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		try {
			frame.setIconImage(ImageIO.read(getClass().getResource("/res/minecraft.png")));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		frame.setLayout(new GridBagLayout());

		statusLabel = new JLabel("Lade Datei...");
		frame.add(statusLabel, constraints(0, 0, 1, 3));

		JLabel oldNameLabel = new JLabel("Alter Name");
		frame.add(oldNameLabel, constraints(0, 1, 1, 1));

		oldName = new JLabel();
		frame.add(oldName, constraints(1, 1, 1, 2));

		JLabel newNameLabel = new JLabel("Neuer Name: ");
		frame.add(newNameLabel, constraints(0, 2, 1, 1));

		newNameField = new JTextField();
		newNameField.setColumns(15);
		frame.add(newNameField, constraints(1, 2, 1, 2));

		okButton = new JButton("Ok");
		okButton.setEnabled(false);
		frame.add(okButton, constraints(2, 3, 1, 1));

		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					r.write(newNameField.getText());
				} catch (FileNotFoundException ex) {
					ex.printStackTrace();
				} catch (Exception ex) {
					statusLabel.setText("Fehler beim speichern");
					ex.printStackTrace();
				}
				statusLabel.setText("Profil gespeichert");

			}
		});

		newNameField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				change();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				change();
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				change();
			}

			public void change() {

				if (newNameField.getDocument().getLength() == 0) {
					okButton.setEnabled(false);
				} else {
					okButton.setEnabled(true);
				}
			}
		});

		frame.pack();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
		frame.setMinimumSize(frame.getSize());
		frame.setVisible(true);
	}

	private GridBagConstraints constraints(int gridX, int gridY, int gridheight, int gridwidth) {

		GridBagConstraints c = new GridBagConstraints();
		c.gridheight = gridheight;
		c.gridwidth = gridwidth;
		c.gridx = gridX;
		c.gridy = gridY;
		c.fill = GridBagConstraints.VERTICAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(5, 5, 5, 5);
		c.weightx = 1;
		c.weighty = 1;
		c.ipadx = 0;
		c.ipady = 0;
		return c;
	}

	public static void main(String[] args) {
		new GUI();
	}

}
