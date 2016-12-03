package TuxPowa.Control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import TuxPowa.View.InformationLabel;
import TuxPowa.View.InterfaceUtilisateur;

public class treatFileAction implements ActionListener {

	InterfaceUtilisateur interfaceUtilisateur;

	public treatFileAction(InterfaceUtilisateur interfaceUtilisateur) {
		super();
		this.interfaceUtilisateur = interfaceUtilisateur;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (!interfaceUtilisateur.videoProcessor.isOpened()) {
			JOptionPane.showMessageDialog(interfaceUtilisateur.mainWindow, new JLabel(InformationLabel.pleaseOpenFile),
					InformationLabel.error, JOptionPane.ERROR_MESSAGE);
		} else if (!interfaceUtilisateur.videoProcessor.isDrawed()) {
			JOptionPane.showMessageDialog(interfaceUtilisateur.mainWindow, new JLabel(InformationLabel.pleaseDrawEdge),
					InformationLabel.error, JOptionPane.ERROR_MESSAGE);
		} else if (!interfaceUtilisateur.videoProcessor.isSampled()) {
			JOptionPane.showMessageDialog(interfaceUtilisateur.mainWindow,
					new JLabel(InformationLabel.pleaseSampleBall), InformationLabel.error, JOptionPane.ERROR_MESSAGE);
		} else if (!interfaceUtilisateur.videoProcessor.isNamed()) {
			JOptionPane.showMessageDialog(interfaceUtilisateur.mainWindow, new JLabel(InformationLabel.pleaseNameTeam),
					InformationLabel.error, JOptionPane.ERROR_MESSAGE);
		} else {
			treatFile();
		}
	}

	private void treatFile() {

		interfaceUtilisateur.eventsListModel.clear();
		interfaceUtilisateur.scoreShower.setText("00:00");
		interfaceUtilisateur.locateFrame();

		JOptionPane.showMessageDialog(interfaceUtilisateur.mainWindow, new JLabel(InformationLabel.treating), InformationLabel.info,
				JOptionPane.PLAIN_MESSAGE);

		interfaceUtilisateur.videoProcessor.treat();
		String[] events = interfaceUtilisateur.videoProcessor.getEvents();
		for (String event : events) {
			interfaceUtilisateur.eventsListModel.addElement(event);
		}
		Integer[] score = interfaceUtilisateur.videoProcessor.getScore();
		interfaceUtilisateur.scoreShower.setText(score[0] + ":" + score[1]);
		JOptionPane.showMessageDialog(interfaceUtilisateur.mainWindow, new JLabel(InformationLabel.treated),
				InformationLabel.info, JOptionPane.PLAIN_MESSAGE);
		interfaceUtilisateur.drawBordersMouseEvent.init();
		interfaceUtilisateur.locateFrame();

		interfaceUtilisateur.historyOption.removeAll();
		List<String> history = interfaceUtilisateur.videoProcessor.getHistory();
		for (String his : history) {
			JMenuItem temp = new JMenuItem(his);
			temp.addActionListener(new historyAction(interfaceUtilisateur));
			interfaceUtilisateur.historyOptionItem.add(temp);
			interfaceUtilisateur.historyOption.add(temp);
		}
	}

}