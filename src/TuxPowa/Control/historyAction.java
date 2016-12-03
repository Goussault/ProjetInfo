package TuxPowa.Control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import TuxPowa.View.InterfaceUtilisateur;

public class historyAction implements ActionListener {

	InterfaceUtilisateur interfaceUtilisateur;

	public historyAction(InterfaceUtilisateur interfaceUtilisateur) {
		super();
		this.interfaceUtilisateur = interfaceUtilisateur;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		interfaceUtilisateur.initializationVideoPanel();
		interfaceUtilisateur.currentVideoName = ((JMenuItem) e.getSource()).getText();
		interfaceUtilisateur.videoProcessor.open(interfaceUtilisateur.currentVideoName);
		interfaceUtilisateur.currentFrame = 0;
		interfaceUtilisateur.locateFrame();
		interfaceUtilisateur.endFrame = interfaceUtilisateur.videoProcessor.getVideoLengthFrame();
		interfaceUtilisateur.updateRateMillisecond = 1000 / interfaceUtilisateur.videoProcessor.getVideoFrameRate();
		interfaceUtilisateur.playingControlTimer.setDelay(interfaceUtilisateur.updateRateMillisecond);
		interfaceUtilisateur.videoProgressBar.setMaximum(interfaceUtilisateur.endFrame);
		interfaceUtilisateur.videoProgressBar.setMajorTickSpacing(interfaceUtilisateur.endFrame / 10);
		interfaceUtilisateur.videoProgressBar.setMinorTickSpacing(interfaceUtilisateur.endFrame / 20);
		interfaceUtilisateur.eventsListModel.clear();
		interfaceUtilisateur.scoreShower.setText("00:00");
		if (interfaceUtilisateur.videoProcessor.isTreated()) {
			String[] events = interfaceUtilisateur.videoProcessor.getEvents();
			for (String event : events) {
				interfaceUtilisateur.eventsListModel.addElement(event);
			}
			Integer[] score = interfaceUtilisateur.videoProcessor.getScore();
			interfaceUtilisateur.scoreShower.setText(score[0] + ":" + score[1]);
		}
		interfaceUtilisateur.setFrameIndicatorLabel();
	}
}