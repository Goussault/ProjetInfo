package TuxPowa.View;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import TuxPowa.Control.*;
import TuxPowa.Model.VideoProcessor;

public class InterfaceUtilisateur {

	// Center panel
	public JLabel videoPanel = new JLabel();
	public JLabel frameIndicatorLabel = new JLabel();
	public JPanel paneCenter = new JPanel();
	public VideoProcessor videoProcessor = new VideoProcessor();
	public boolean playingVideo = false;
	public JPopupMenu mainWindowPopupMenu = new JPopupMenu();
	public JMenuItem openFilePopupOption = new JMenuItem(InformationLabel.openFile);
	public JMenuItem closeFilePopupOption = new JMenuItem(InformationLabel.closeFile);
	public JMenuItem exitApplicationPopupOption = new JMenuItem(InformationLabel.quit);
	public JMenuItem nameTeamPopupOption = new JMenuItem(InformationLabel.nameTeam);
	public JMenuItem drawEdgePopupOption = new JMenuItem(InformationLabel.draw);
	public JMenuItem sampleBallPopupOption = new JMenuItem(InformationLabel.sample);
	public JMenuItem treatFileNowPopupOption = new JMenuItem(InformationLabel.treatement);
	public JMenuItem makeFilePopupOptionMd = new JMenuItem(InformationLabel.generateFileMd);
	public JMenuItem makeFilePopupOptionJson = new JMenuItem(InformationLabel.generateFileJson);

	// East panel
	public JPanel paneEast = new JPanel();
	public DefaultListModel<String> eventsListModel = new DefaultListModel<String>();
	public JList<?> eventsText = new JList<>(eventsListModel);
	public JScrollPane eventsShower = new JScrollPane(eventsText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	public JTextField scoreShower = new JTextField(5);

	// South panel
	public JPanel paneSouth = new JPanel();
	public final JLabel videoProgressBarLabel = new JLabel(InformationLabel.frames);
	public JButton playPauseButton = new JButton(InformationLabel.play);
	public JButton nextImageButton = new JButton(InformationLabel.nextImage);
	public JButton lastImageButton = new JButton(InformationLabel.lastImage);
	public JTextField FrameLocator = new JTextField(3);
	public JButton locateButton = new JButton(InformationLabel.go);
	public int startFrame = 0;
	public int endFrame = 100;
	public int currentFrame = 0;
	public JSlider videoProgressBar = new JSlider(JSlider.HORIZONTAL, startFrame, endFrame, startFrame);

	// Main window
	public ImageIcon currentImage = null;
	public BufferedImage accueilImage = null;
	public int updateRateMillisecond = 1000 / 60;
	public String currentVideoName = null;
	public JFrame mainWindow = new JFrame(InformationLabel.windowName);
	public JMenuBar mainWindowMenuBar = new JMenuBar();
	public JMenu fileOption = new JMenu(InformationLabel.file);
	public JMenuItem openFileOption = new JMenuItem(InformationLabel.openFile);
	public JMenuItem closeFileOption = new JMenuItem(InformationLabel.closeFile);
	public JMenuItem exitApplicationOption = new JMenuItem(InformationLabel.quit);
	public JMenu treatFileOption = new JMenu(InformationLabel.treat);
	public JMenuItem nameTeamOption = new JMenuItem(InformationLabel.nameTeam);
	public JMenuItem drawEdgeOption = new JMenuItem(InformationLabel.draw);
	public JMenuItem sampleBallOption = new JMenuItem(InformationLabel.sample);
	public JMenuItem treatFileNowOption = new JMenuItem(InformationLabel.treatement);
	public JMenuItem makeFileOptionMd = new JMenuItem(InformationLabel.generateFileMd);
	public JMenuItem makeFileOptionJson = new JMenuItem(InformationLabel.generateFileJson);
	public JMenu historyOption = new JMenu(InformationLabel.history);
	public List<JMenuItem> historyOptionItem = new ArrayList<>();
	public JLabel drawBordersSketchpad = new JLabel();
	public BufferedImage drawBordersImage = null;
	public Graphics2D drawBordersImageEditable = null;
	public JLabel drawBordersLabel = new JLabel(InformationLabel.draw1);
	public JPanel drawBordersPanel = new JPanel();
	public drawBordersMouseEvent drawBordersMouseEvent = new drawBordersMouseEvent(this);
	public JLabel sampleBallSketchpad = new JLabel();
	public BufferedImage sampleBallImage = null;
	public Graphics2D sampleBallImageEditable = null;
	public JLabel sampleBallLabel = new JLabel(InformationLabel.sample);
	public JButton sampleBallNextImageButton = new JButton(InformationLabel.nextImage);
	public JButton sampleBallZoomButton = new JButton(InformationLabel.zoom);
	public JPanel sampleBallPanel = new JPanel();
	public sampleBallMouseEvent sampleBallMouseEvent = new sampleBallMouseEvent(this);
	public sampleBallNextImageEvent sampleBallNextImageEvent = new sampleBallNextImageEvent(this);
	public Timer playingControlTimer = new Timer(updateRateMillisecond, new updateImageAction(this));

	/**
	 * It's the constructor of the class InterfaceUtilisateur. After the object
	 * is initialized, it will generate a window which contains a video player,
	 * a control panel and a information area.
	 * 
	 * @author ZHANG Heng, YAN Yutong
	 * 
	 * @version 1.0
	 * 
	 * @since 1.0
	 */
	public InterfaceUtilisateur() {
		configureCenterPanel();
		configureEastPanel();
		configureSouthPanel();
		configureMainWindow();
	}

	public void setFrameIndicatorLabel() {
		if (videoProcessor.getFileName() == null) {
			frameIndicatorLabel.setText(InformationLabel.pleaseOpenFile);
		} else {
			String temp = "";
			if (videoProcessor.isNamed()) {
				temp = " / " + videoProcessor.getNames()[0] + " VS " + videoProcessor.getNames()[1];
			}
			frameIndicatorLabel.setText("file: " + new File(videoProcessor.getFileName()).getName() + temp + " / fps: "
					+ videoProcessor.getVideoFrameRate() + " / nbFrame: "
					+ String.valueOf(videoProcessor.getVideoLengthFrame()) + " / curFrame:"
					+ String.valueOf(currentFrame) + " / treated:" + String.valueOf(videoProcessor.isTreated()));

		}
	}

	public void initializationVideoPanel() {
		changeToPausedState();
		videoProcessor.close();
		currentFrame = startFrame;
		videoProgressBar.setValue(currentFrame);
		setFrameIndicatorLabel();
		try {
			currentImage = new ImageIcon(
					resizeBufferedImage(accueilImage, videoPanel.getWidth(), videoPanel.getHeight()));
		} catch (Exception e) {
			currentImage = new ImageIcon(accueilImage);
		}

		videoPanel.setIcon(currentImage);
		videoPanel.repaint();

		eventsListModel.clear();
		scoreShower.setText("00:00");
	}

	public void configureCenterPanel() {
		// load home image
		try {
			accueilImage = ImageIO.read(new File("img" + File.separator + "TSE.jpg"));
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(mainWindow, new JLabel(InformationLabel.cantFindImage), "Erreur",
					JOptionPane.ERROR_MESSAGE);
		}

		// configure of the pop up options
		openFilePopupOption.addActionListener(new openFileAction(this));
		closeFilePopupOption.addActionListener(new closeFileAction(this));
		exitApplicationPopupOption.addActionListener(new exitApplicationAction(this));
		nameTeamPopupOption.addActionListener(new nameTeamAction(this));
		drawEdgePopupOption.addActionListener(new drawEdgeAction(this));
		sampleBallPopupOption.addActionListener(new sampleBallAction(this));
		treatFileNowPopupOption.addActionListener(new treatFileAction(this));
		makeFilePopupOptionMd.addActionListener(new makeFileActionMd(this));
		makeFilePopupOptionJson.addActionListener(new makeFileActionJson(this));

		mainWindowPopupMenu.add(openFilePopupOption);
		mainWindowPopupMenu.add(closeFilePopupOption);
		mainWindowPopupMenu.addSeparator();
		mainWindowPopupMenu.add(nameTeamPopupOption);
		mainWindowPopupMenu.addSeparator();
		mainWindowPopupMenu.add(drawEdgePopupOption);
		mainWindowPopupMenu.addSeparator();
		mainWindowPopupMenu.add(sampleBallPopupOption);
		mainWindowPopupMenu.addSeparator();
		mainWindowPopupMenu.add(treatFileNowPopupOption);
		mainWindowPopupMenu.addSeparator();
		mainWindowPopupMenu.add(makeFilePopupOptionMd);
		mainWindowPopupMenu.add(makeFilePopupOptionJson);

		mainWindowPopupMenu.setBorder(new TitledBorder(new EtchedBorder(), InformationLabel.TSE));
		videoPanel.setComponentPopupMenu(mainWindowPopupMenu);

		// configure of the center panel
		paneCenter.setLayout(new BorderLayout(0, 0));
		paneCenter.add(videoPanel, BorderLayout.CENTER);
		paneCenter.add(frameIndicatorLabel, BorderLayout.SOUTH);

		// initialization of the video panel
		initializationVideoPanel();
	}

	public void configureEastPanel() {
		eventsShower.setBorder(new TitledBorder(new EtchedBorder(), InformationLabel.events));

		scoreShower.setEditable(false);
		scoreShower.setBorder(new TitledBorder(new EtchedBorder(), InformationLabel.score));

		eventsListModel.clear();
		scoreShower.setText("00:00");

		paneEast.setLayout(new BorderLayout(20, 20));
		paneEast.setBorder(new TitledBorder(new EtchedBorder(), InformationLabel.matchInformation));
		paneEast.add(eventsShower, BorderLayout.CENTER);
		paneEast.add(scoreShower, BorderLayout.SOUTH);

	}

	public void configureSouthPanel() {

		// configure of the video progress bar
		videoProgressBar.setPaintTicks(true);
		videoProgressBar.setMajorTickSpacing(endFrame / 10);
		videoProgressBar.setMinorTickSpacing(endFrame / 20);
		videoProgressBar.setPaintLabels(true);
		videoProgressBar.setEnabled(true);
		videoProgressBar.addChangeListener(new locateFrameAction(this));

		// combine the video progress bar and the video progress bar label
		Box videoProgressBarShower = Box.createHorizontalBox();
		videoProgressBarShower.add(videoProgressBarLabel);
		videoProgressBarShower.add(videoProgressBar);

		// configure of the buttons

		lastImageButton.addActionListener(new lastImageAction(this));
		playPauseButton.addActionListener(new playPauseAction(this));
		nextImageButton.addActionListener(new nextImageAction(this));
		locateButton.addActionListener(new locateFrameAction(this));

		Box buttonBarShower = Box.createHorizontalBox();

		buttonBarShower.add(lastImageButton);
		buttonBarShower.add(playPauseButton);
		buttonBarShower.add(nextImageButton);
		buttonBarShower.add(FrameLocator);
		buttonBarShower.add(locateButton);

		// combine the south panel
		paneSouth.setLayout(new BorderLayout(20, 20));
		paneSouth.add(videoProgressBarShower, BorderLayout.CENTER);
		paneSouth.add(buttonBarShower, BorderLayout.SOUTH);
		paneSouth.setBorder(new TitledBorder(new EtchedBorder(), InformationLabel.videoControl));
	}

	public void configureMainWindow() {
		// configure of the menu bar
		openFileOption.addActionListener(new openFileAction(this));
		closeFileOption.addActionListener(new closeFileAction(this));
		exitApplicationOption.addActionListener(new exitApplicationAction(this));

		fileOption.add(openFileOption);
		fileOption.add(closeFileOption);
		fileOption.addSeparator();
		fileOption.add(exitApplicationOption);

		nameTeamOption.addActionListener(new nameTeamAction(this));
		drawEdgeOption.addActionListener(new drawEdgeAction(this));
		sampleBallOption.addActionListener(new sampleBallAction(this));
		treatFileNowOption.addActionListener(new treatFileAction(this));
		makeFileOptionMd.addActionListener(new makeFileActionMd(this));
		makeFileOptionJson.addActionListener(new makeFileActionJson(this));

		treatFileOption.add(nameTeamOption);
		treatFileOption.addSeparator();
		treatFileOption.add(drawEdgeOption);
		treatFileOption.addSeparator();
		treatFileOption.add(sampleBallOption);
		treatFileOption.addSeparator();
		treatFileOption.add(treatFileNowOption);
		treatFileOption.addSeparator();
		treatFileOption.add(makeFileOptionMd);
		treatFileOption.add(makeFileOptionJson);

		List<String> history = videoProcessor.getHistory();
		for (String his : history) {
			JMenuItem temp = new JMenuItem(his);
			temp.addActionListener(new historyAction(this));
			historyOptionItem.add(temp);
			historyOption.add(temp);
		}

		mainWindowMenuBar.add(fileOption);
		mainWindowMenuBar.add(treatFileOption);
		mainWindowMenuBar.add(historyOption);

		mainWindow.setJMenuBar(mainWindowMenuBar);

		mainWindow.setLayout(new BorderLayout(0, 0));
		mainWindow.add(paneCenter, BorderLayout.CENTER);
		mainWindow.add(paneEast, BorderLayout.EAST);
		mainWindow.add(paneSouth, BorderLayout.SOUTH);
		mainWindow.addWindowListener(new exitApplicationAction(this));
		mainWindow.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent evt) {
				if (!videoProcessor.isOpened()) {
					initializationVideoPanel();
				} else {
					locateFrame();
				}
			}
		});
		mainWindow.pack();
		mainWindow.setVisible(true);
	}

	public void changeToPlayState() {
		playingControlTimer.start();
		playPauseButton.setText(InformationLabel.pause);
		playingVideo = true;
	}

	public void changeToPausedState() {
		playingControlTimer.stop();
		playPauseButton.setText(InformationLabel.play);
		playingVideo = false;
	}

	public void locateFrame() {
		videoProgressBar.setValue(currentFrame);
		setFrameIndicatorLabel();
		videoProcessor.setCurrentPositionFrame(currentFrame);
		if (videoProcessor.hasNextImage()) {
			currentImage = new ImageIcon(
					resizeBufferedImage(videoProcessor.nextImage(), videoPanel.getWidth(), videoPanel.getHeight()));
		}
		videoPanel.setIcon(currentImage);
		videoPanel.repaint();
	}

	public static BufferedImage resizeBufferedImage(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}

	public static void main(String[] args) {

		new InterfaceUtilisateur();

	}
}
