package gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputAdapter;

import core.BrickWall;
import core.Direction;
import core.EnemyTank;
import core.Flag;
import core.GameManager;
import core.Ice;
import core.PlayerTank;
import core.Speed;
import core.SteelWall;
import core.Tree;
import core.Water;
import core.World;

@SuppressWarnings("serial")
public class ConstructionPanel extends JPanel {

	private int heightMatrix;
	private int widthMatrix;
	private int tile;

	private int contBasic;
	private int contArmor;
	private int contFast;
	private int contPower;
	private int contFlag;
	private int contPlayerP1;
	private int contPlayerP2;

	private boolean isFlag;
	private boolean isPlayer;
	private boolean isEnemy;

	private TypeMatrix[][] matrix;
	private TypeMatrix type;
	private ArrayList<Rectangle> positionLabel;
	private ArrayList<Rectangle> positionCont;
	private ArrayList<JButton> buttons;

	private int DIM;
	private int cursorPosition;
	private int positionXObject;
	private int positionYObject;
	private boolean selection;
	private int totEnemyIntoMap;
	private int totEnemyForType;
	private PanelSwitcher switcher;
	private JFileChooserClass jfilechooser;
	private boolean saveFile;
	private MyListener myListener;
	private ArrayList<JLabel> numberOfEnemyPaint;
	private String stringOfEnemy;
	private JDialog dialog;
	private JButton[] bts;
	private int cursorPositionDialog;
	private boolean hide;
	private Flag flag;
	private WarningDialog warning;

	private int begin1;
	private int begin2;
	private int end1;
	private int sizeButton = 400;
	private Cell[][] matrixCell;

	public ConstructionPanel(final int w, final int h, PanelSwitcher switcher, Flag flag) {

		this.setPreferredSize(new Dimension(w, h));
		this.setBackground(Color.BLACK);
		this.setLayout(null);
		this.heightMatrix = 20;
		this.widthMatrix = 21;
		this.tile = 30;
		this.contBasic = contArmor = contFast = contPower = contFlag = contPlayerP1 = contPlayerP2 = 0;

		// se prima di salvare si dimentica qualcosa
		this.flag = flag;
		this.isFlag = false;
		this.isPlayer = false;
		this.isEnemy = false;
		this.type = TypeMatrix.EMPTY;
		this.DIM = 4;
		this.positionXObject = 850;
		this.positionYObject = 380;
		this.selection = false;
		this.totEnemyIntoMap = 20;
		this.totEnemyForType = 20;
		this.saveFile = false;
		this.numberOfEnemyPaint = new ArrayList<>();
		this.stringOfEnemy = new String();
		this.dialog = new JDialog(((MainFrame) switcher));
		this.cursorPositionDialog = 0;
		this.hide = false;
		this.matrix = new TypeMatrix[heightMatrix][widthMatrix];
		this.positionLabel = new ArrayList<>();
		this.positionCont = new ArrayList<>();
		this.begin1 = ((getWidth()) - (widthMatrix * tile) - sizeButton) / 4;
		this.begin2 = (getHeight() / 2) - ((heightMatrix * tile) / 2);
		this.end1 = heightMatrix * tile;
		this.numberOfEnemyPaint = new ArrayList<>();
		this.stringOfEnemy = new String();

		for (int i = 0; i < DIM; i++) {

			numberOfEnemyPaint.add(new JLabel());

			numberOfEnemyPaint.get(i).setForeground(Color.WHITE);
			numberOfEnemyPaint.get(i).setFont(MainFrame.customFontB);
			this.add(numberOfEnemyPaint.get(i));
		}

		this.myListener = new MyListener();
		this.jfilechooser = new JFileChooserClass(((MainFrame) switcher), false);

		setSwitcher(switcher);
		setCursorPosition(1);
		initMatrixCell(begin1, begin2);
		createButton();
		initialize();
	}

	void initMatrixCell(int dim, int dim2) {
		matrixCell = new Cell[heightMatrix][widthMatrix];
		for (int a = 0; a < heightMatrix; a++) {
			for (int b = 0; b < widthMatrix; b++) {
				matrixCell[a][b] = new Cell(tile, a, b, dim, dim2);
			}
		}
	}

	public void createButton() {

		buttons = new ArrayList<>();

		for (int i = 0; i < DIM; i++) {

			final int curRow = i;

			buttons.add(new JButton());
			buttons.get(i).setBorder(null);
			buttons.get(i).setContentAreaFilled(false);
			buttons.get(i).setBorderPainted(false);
			buttons.get(i).setFocusPainted(false);
			buttons.get(i).setFocusPainted(false);
			buttons.get(i).setFont(MainFrame.customFontM);
			buttons.get(i).setForeground(Color.WHITE);
			buttons.get(i).setBackground(Color.BLACK);
			buttons.get(i).setHorizontalAlignment(SwingConstants.LEFT);
			setBoundAndText(i);
			buttons.get(i).addMouseListener(new MouseInputAdapter() {

				@Override
				public void mousePressed(MouseEvent e) {

					if (e.getComponent().getY() == buttons.get(curRow).getY()) {
						cursorPosition = curRow;
						repaint();
					}
				}
			});
			buttons.get(i).addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {

					// appena si aziona disattivo il draw
					selection = false;
					type = TypeMatrix.DEFAULT;

					boolean enter = false;
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						((JButton) e.getComponent()).doClick();
					} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						enter = true;
						if (!empty()) {
							if (!saveFile) {
								SoundsProvider.playBulletHit1();
								((MainFrame) getSwitcher()).setTransparent(true);
								backDialog();
							} else {
								setCursorPosition(1);
								cleanMatrixOfType();
								saveFile = selection = isFlag = isPlayer = isEnemy = false;
								type = TypeMatrix.DEFAULT;
								((MainFrame) getSwitcher()).setTransparent(false);
								getSwitcher().showMenu();
							}
						} else {
							isFlag = isPlayer = isEnemy = false;
							repaint();
							cursorPosition = 1;
							((MainFrame) getSwitcher()).setTransparent(false);
							getSwitcher().showMenu();
						}
					} else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_LEFT) {
						enter = true;
						if (curRow < 1) {

							buttons.get(buttons.size() - 1).requestFocus();
							cursorPosition = buttons.size() - 1;
							repaint();
						} else {

							buttons.get(curRow - 1).requestFocus();
							cursorPosition = curRow - 1;
							repaint();
						}
					} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_RIGHT) {
						enter = true;
						buttons.get((curRow + 1) % buttons.size()).requestFocus();
						cursorPosition = (curRow + 1) % buttons.size();
						repaint();
					}
					if (enter) {
						SoundsProvider.playBulletHit1();
					}
				}
			});

			addActionListener(i);
			this.add(buttons.get(i));
		}

		this.addMouseListener(myListener);
		this.addMouseMotionListener(myListener);
	}

	public void addActionListener(int j) {
		switch (j) {
		case 0: // BACK
			buttons.get(j).addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					SoundsProvider.playBulletHit1();
					cursorPosition = j;
					selection = false;
					type = TypeMatrix.DEFAULT;
					cursorPosition = 1;

					if (!empty()) {
						if (!saveFile) {
							((MainFrame) getSwitcher()).setTransparent(true);
							backDialog();
						} else {
							setCursorPosition(1);
							cleanMatrixOfType();
							saveFile = selection = isFlag = isPlayer = isEnemy = false;
							type = TypeMatrix.DEFAULT;
							SoundsProvider.playBulletHit1();
							((MainFrame) getSwitcher()).setTransparent(false);
							getSwitcher().showMenu();
						}
					} else {
						isFlag = isPlayer = isEnemy = false;
						((MainFrame) getSwitcher()).setTransparent(false);
						getSwitcher().showMenu();
					}
					repaint();
				}
			});
			break;
		case 1: // LOAD
			buttons.get(j).addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					SoundsProvider.playBulletHit1();
					cursorPosition = j;
					selection = false;
					type = TypeMatrix.DEFAULT;

					if (jfilechooser.functionLoadFile()) {
						importFile(jfilechooser.getFilename(), jfilechooser.getDir());
						repaint();
					}
				}
			});
			break;
		case 2: // SAVE
			buttons.get(j).addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					SoundsProvider.playBulletHit1();
					cursorPosition = j;
					selection = false;
					type = TypeMatrix.DEFAULT;

					// temporaneo
					if (contFlag == 0)
						isFlag = true;
					if (contPlayerP1 == 0 && contPlayerP2 == 0)
						isPlayer = true;
					if (contBasic == 0 && contArmor == 0 && contFast == 0 && contPower == 0)
						isEnemy = true;

					if (contFlag == 1 && (contPlayerP1 == 1 || contPlayerP2 == 1)
							&& (contBasic > 0 || contArmor > 0 || contFast > 0 || contPower > 0) && attainableFlag()
							&& numbersMaxOfEnemy() && jfilechooser.functionSaveFile(contPlayerP1, contPlayerP2)) {
						saveFile = true;
						BufferedWriter b = null;
						FileWriter w = null;

						try {

							w = new FileWriter(jfilechooser.getDir().getText() + "/"
									+ jfilechooser.getFilename().getText() + ".txt");
							b = new BufferedWriter(w);

						} catch (IOException ex) {
							ex.printStackTrace();
						}

						writeMap(b, w);

						repaint();
					}
				}
			});
			break;
		case 3: // CANCEL
			buttons.get(j).addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					SoundsProvider.playBulletHit1();
					cursorPosition = j;
					selection = false;
					type = TypeMatrix.DEFAULT;

					cleanMatrixOfType();
					repaint();
				}
			});
			break;
		default:
			break;
		}
	}

	protected boolean numbersMaxOfEnemy() {
		if ((contArmor + contBasic + contFast + contPower) > 20
				&& ((contPlayerP1 == 1 && contPlayerP2 == 0) || (contPlayerP1 == 0 && contPlayerP2 == 1))) {
			setWarning(new WarningDialog("Numbers total Enemies is 20 with SinglePlayer!", ((MainFrame) switcher)));
			return false;
		} else if ((contArmor + contBasic + contFast + contPower) > 30 && contPlayerP1 == 1 && contPlayerP2 == 1) {
			setWarning(new WarningDialog("Numbers total Enemies is 30 with MultiPlayer!", ((MainFrame) switcher)));
			return false;
		}

		return true;
	}

	protected boolean attainableFlag() {
		World world = new World();
		initWorld(world);
		GameManager game = new GameManager(world, flag);
		EnemyTank e = new EnemyTank(0, 0, game.getMatrix(), Speed.SLOW, Speed.SLOW, Direction.STOP, 1, 1, 1);
		e.difficult(flag.getX(), flag.getY());
		if (e.isHasApath()) {
			return true;
		}
		((MainFrame) getSwitcher()).setTransparent(true);
		setWarning(new WarningDialog("Can't found path to Flag!", ((MainFrame) switcher)));
		return false;
	}

	private void initWorld(World world) {
		for (int a = 0; a < heightMatrix; a++) {
			for (int b = 0; b < widthMatrix; b++) {
				switch (matrix[a][b]) {
				case EMPTY:
					world.getWorld()[a][b] = null;
					break;
				case STEELWALL:
					world.getWorld()[a][b] = new SteelWall(a, b, world, 4);
					break;
				case ICE:
					world.getWorld()[a][b] = new Ice(a, b, world);
					break;
				case TREE:
					world.getWorld()[a][b] = new Tree(a, b, world);
					break;
				case BRICKWALL:
					world.getWorld()[a][b] = new BrickWall(a, b, world, 2);
					break;
				case WATER:
					world.getWorld()[a][b] = new Water(a, b, world);
					break;
				case PLAYER1:
					world.getWorld()[a][b] = new PlayerTank(a, b, world, "P1");
					break;
				case PLAYER2:
					world.getWorld()[a][b] = new PlayerTank(a, b, world, "P2");
					break;
				case FLAG:
					world.getWorld()[a][b] = new Flag(a, b, world);
					flag = new Flag(a, b, world);
					break;
				default:
				}// switch
			}
		}
	}

	public void backDialog() {

		dialog.setPreferredSize(new Dimension(300, 150));
		JPanel fullpanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (cursorPositionDialog == 0) {
					g.drawImage(ImageProvider.getCursorRight(), 75, 55, this);
				} else {
					g.drawImage(ImageProvider.getCursorRight(), 75, 103, this);
				}
			}
		};
		JLabel label = new JLabel("Do you want exit?");
		JPanel buttons = new JPanel(new GridLayout(2, 1, 0, 10));
		JPanel text = new JPanel(new GridLayout(1, 1, 0, 10));
		String[] buttonTxt = { "No", "Si" };
		fullpanel.setPreferredSize(new Dimension(300, 150));
		fullpanel.setBorder(BorderFactory.createLineBorder(Color.RED));
		fullpanel.setBackground(Color.BLACK);
		fullpanel.setLayout(new BoxLayout(fullpanel, BoxLayout.Y_AXIS));
		label.setFont(MainFrame.customFontM);
		label.setForeground(Color.WHITE);
		label.setHorizontalAlignment(JLabel.CENTER);
		text.setAlignmentX(Component.CENTER_ALIGNMENT);
		text.setPreferredSize(new Dimension(300, 70));
		text.setMaximumSize(new Dimension(300, 70)); // set max = pref
		text.setBackground(Color.BLACK);
		text.add(label);
		bts = new JButton[buttonTxt.length];
		for (int i = 0; i < buttonTxt.length; i++) {

			final int curRow = i;
			bts[i] = new JButton(buttonTxt[i]);
			bts[i].setFont(MainFrame.customFontM);
			bts[i].setBackground(Color.BLACK);
			bts[i].setForeground(Color.WHITE);
			bts[i].setContentAreaFilled(false);
			bts[i].setBorderPainted(false);
			bts[i].setFocusPainted(false);
			bts[i].setBorder(null);
			bts[i].setFocusPainted(false);
			bts[i].addMouseListener(new MouseInputAdapter() {

				@Override
				public void mousePressed(MouseEvent e) {

					if (e.getComponent().getY() == bts[curRow].getY()) {
						cursorPositionDialog = curRow;
						repaint();
					}
				}
			});
			bts[i].addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {

					boolean enter = false;
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						((JButton) e.getComponent()).doClick();
					} else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_LEFT) {
						enter = true;

						if (curRow < 1) {
							bts[bts.length - 1].requestFocus();
							cursorPositionDialog = bts.length - 1;

						} else {
							bts[curRow - 1].requestFocus();
							cursorPositionDialog = curRow - 1;
						}
					} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_RIGHT) {
						enter = true;
						bts[(curRow + 1) % bts.length].requestFocus();
						cursorPositionDialog = (curRow + 1) % bts.length;
					}
					fullpanel.repaint();
					if (enter)
						SoundsProvider.playBulletHit1();
				}
			});
			buttons.add(bts[i]);
			backDialogListener(i);
		}

		buttons.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttons.setPreferredSize(new Dimension(70, 100));
		buttons.setMaximumSize(new Dimension(70, 100));
		buttons.setBackground(Color.BLACK);
		fullpanel.add(text);
		fullpanel.add(buttons);
		dialog.setContentPane(fullpanel);
		dialog.setUndecorated(true);
		dialog.setModal(true);
		dialog.pack();
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
	}

	public void backDialogListener(int j) {

		switch (j) {
		case 0: // NO
			bts[j].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					SoundsProvider.playBulletHit1();
					((MainFrame) getSwitcher()).setTransparent(false);
					cursorPositionDialog = 0;
					dialog.dispose();
				}
			});
			break;
		case 1: // SI
			bts[j].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					SoundsProvider.playBulletHit1();

					setCursorPosition(1);
					cleanMatrixOfType();
					selection = false;
					isEnemy = false;
					isFlag = false;
					isPlayer = false;
					type = TypeMatrix.DEFAULT;

					((MainFrame) getSwitcher()).setTransparent(false);
					hide = false;
					dialog.dispose();
					getSwitcher().showMenu();
				}
			});
			break;
		default:
			break;
		}
	}

	private boolean empty() {
		if (contArmor != 0 || contBasic != 0 || contFast != 0 || contPower != 0) {
			return false;
		}

		for (int a = 0; a < heightMatrix; a++) {
			for (int b = 0; b < widthMatrix; b++) {
				if (matrix[a][b] != TypeMatrix.EMPTY)
					return false;
			}
		}
		return true;
	}

	public void setBoundAndText(int j) {
		switch (j) {
		case 0:
			buttons.get(j).setBounds(10, 10, 100, 30);
			buttons.get(j).setText("Back");
			break;
		case 1:

			buttons.get(j).setBounds(((MainFrame) switcher).getWIDTH() - getBegin1() - 125,
					((MainFrame) switcher).getHEIGHT() - 240, 70, 35);
			buttons.get(j).setText("Load");
			break;
		case 2:
			buttons.get(j).setBounds(((MainFrame) switcher).getWIDTH() - getBegin1() - 125,
					((MainFrame) switcher).getHEIGHT() - 170, 70, 35);
			buttons.get(j).setText("Save");
			break;
		case 3:
			buttons.get(j).setBounds(((MainFrame) switcher).getWIDTH() - getBegin1() - 125,
					((MainFrame) switcher).getHEIGHT() - 100, 120, 35);
			buttons.get(j).setText("Cancel");
		default:
			break;
		}
	}

	private void initialize() {

		cleanMatrixOfType();

		for (int a = 0; a < 13; a++) {
			Rectangle j = new Rectangle(tile, tile);
			positionLabel.add(j);
		}

		for (int a = 0; a < 8; a++) {
			Rectangle j = new Rectangle();
			positionCont.add(j);
		}
		((MainFrame) switcher).resizeComponent(this);
	}

	private void cleanMatrixOfType() {

		for (int a = 0; a < heightMatrix; a++) {
			for (int b = 0; b < widthMatrix; b++) {
				matrix[a][b] = TypeMatrix.EMPTY;
			}
		}

		contArmor = 0;
		contBasic = 0;
		contFast = 0;
		contPower = 0;
		contPlayerP1 = 0;
		contPlayerP2 = 0;
		contFlag = 0;
	}

	private void importFile(JTextField filename, JTextField dir) {
		int i = 0;
		cleanMatrixOfType();
		try {

			BufferedReader reader = new BufferedReader(
					new FileReader(dir.getText() + "/" + filename.getText() + ".txt"));
			String line = reader.readLine();

			while (i < heightMatrix) {

				StringTokenizer st = new StringTokenizer(line, " ");
				String tmp;
				int j = 0;

				while (st.hasMoreTokens()) {

					tmp = st.nextToken();

					switch (tmp) {
					case ("null"):
						matrix[i][j] = TypeMatrix.EMPTY;
						break;
					case ("[//]"):
						matrix[i][j] = TypeMatrix.STEELWALL;
						break;
					case ("@@@@"):
						matrix[i][j] = TypeMatrix.ICE;
						break;
					case ("TTTT"):
						matrix[i][j] = TypeMatrix.TREE;
						break;
					case ("[||]"):
						matrix[i][j] = TypeMatrix.BRICKWALL;
						break;
					case ("~~~~"):
						matrix[i][j] = TypeMatrix.WATER;
						break;
					case ("P1"):
						matrix[i][j] = TypeMatrix.PLAYER1;
						contPlayerP1++;
						if ((contPlayerP1 == 1 && contPlayerP2 == 0) || (contPlayerP1 == 0 && contPlayerP2 == 1))
							setTotEnemyIntoMap(20);
						else
							setTotEnemyIntoMap(40);
						break;
					case ("P2"):
						matrix[i][j] = TypeMatrix.PLAYER2;
						contPlayerP2++;
						if ((contPlayerP1 == 1 && contPlayerP2 == 0) || (contPlayerP1 == 0 && contPlayerP2 == 1))
							setTotEnemyIntoMap(20);
						else
							setTotEnemyIntoMap(40);
						break;
					case ("FLAG"):
						matrix[i][j] = TypeMatrix.FLAG;
						contFlag++;
						break;
					default:
						matrix[i][j] = TypeMatrix.DEFAULT;
					}// switch
					j++;
				} // while

				i++;
				line = reader.readLine();
			} // while

			while (line != null) {

				StringTokenizer st = new StringTokenizer(line, " ");

				String typology = null;
				String number = null;

				if (st.hasMoreTokens())
					typology = st.nextToken();

				if (st.hasMoreTokens())
					number = st.nextToken();

				if (typology != null && number != null)

					if (typology.equals("basic"))
						contBasic = Integer.parseInt(number);
					else if (typology.equals("armor"))
						contArmor = Integer.parseInt(number);
					else if (typology.equals("fast"))
						contFast = Integer.parseInt(number);
					else if (typology.equals("power"))
						contPower = Integer.parseInt(number);
				try {

					line = reader.readLine();

				} catch (IOException e) {
					e.printStackTrace();
				}
			} // while
			reader.close();
		} // try
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void writeMap(BufferedWriter b, FileWriter w) {

		try {

			for (int a = 0; a < heightMatrix; a++) {

				for (int c = 0; c < widthMatrix; c++) {

					switch (matrix[a][c]) {
					case EMPTY:
						b.write("null");
						break;
					case STEELWALL:
						b.write("[//]");
						break;
					case ICE:
						b.write("@@@@");
						break;
					case TREE:
						b.write("TTTT");
						break;
					case BRICKWALL:
						b.write("[||]");
						break;
					case WATER:
						b.write("~~~~");
						break;
					case FLAG:
						b.write("FLAG");
						break;
					case PLAYER1:
						b.write("P1");
						break;
					case PLAYER2:
						b.write("P2");
						break;
					case DEFAULT:
						break;
					}
					b.write(" ");
					b.flush();
				}
				b.write("\n");
			}
			b.write("\n");
			b.flush();

			b.write("basic " + contBasic + "\n");
			b.flush();

			b.write("armor " + contArmor + "\n");
			b.flush();

			b.write("fast " + contFast + "\n");
			b.flush();

			b.write("power " + contPower + "\n");
			b.flush();

			b.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		begin1 = ((getWidth()) - (widthMatrix * tile) - sizeButton) / 4;
		begin2 = (getHeight() / 2) - ((heightMatrix * tile) / 2);
		end1 = heightMatrix * tile;

		if (((MainFrame) getSwitcher()).isTransparent()) {

			// Apply our own painting effect
			Graphics2D g2d = (Graphics2D) g;
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .2f));
			g2d.setColor(getBackground());
			g2d.fill(getBounds());
		}
		
		//NON CANCELLAREEEEEE

		// for (int a = 0; a < heightMatrix; a++) {
		// for (int b = 0; b < widthMatrix; b++) {
		// g.setColor(Color.orange);
		// g.drawRect((int)matrixCell[a][b].r.getY(),(int)matrixCell[a][b].r.getX(),(int)
		// matrixCell[a][b].r.getWidth(),(int) matrixCell[a][b].r.getHeight());
		//
		// }
		// }

		if (!hide) {
			if (cursorPosition == 0)
				g.drawImage(ImageProvider.getCursorLeft(), buttons.get(cursorPosition).getX() + 85,
						buttons.get(cursorPosition).getY() - 6, this);
			else
				g.drawImage(ImageProvider.getCursorRight(), buttons.get(cursorPosition).getX() - 65,
						buttons.get(cursorPosition).getY() - 8, this);
		}

		// TANK BACKGROUND
		((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
		g.drawImage(ImageProvider.getBackground1P(),
				(begin1 + (widthMatrix * tile / 2)) - ImageProvider.getBackground1P().getWidth(null) / 2,
				(begin1 + (heightMatrix * tile / 2)) - ImageProvider.getBackground1P().getHeight(null) / 2, null);
		if (!((MainFrame) getSwitcher()).isTransparent())
			((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

		if (selection)
			g.drawImage(ImageProvider.getSelection(), positionXObject - 2, positionYObject - 2, null);

		if (!((MainFrame) getSwitcher()).isTransparent())
			((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));

		g.setColor(Color.WHITE);

		for (int i = 0; i <= heightMatrix; i++) {
			g.drawLine(begin1, begin2 + (i * tile), end1 + begin1 + tile, begin2 + (i * tile));
		}

		for (int i = 0; i <= widthMatrix; i++) {
			g.drawLine((begin1) + (i * tile), begin2, (begin1) + (i * tile), getHeight() - begin2);
		}

		if (!((MainFrame) getSwitcher()).isTransparent())
			((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

		for (int a = 0; a < positionLabel.size(); a++) {

			int x = (int) positionLabel.get(a).getX();
			int y = (int) positionLabel.get(a).getY();

			long time = (System.currentTimeMillis() / 400) % 2;

			if (a == 0)
				g.drawImage(ImageProvider.getBrick(), x, y, null);
			else if (a == 1)
				g.drawImage(ImageProvider.getSteel(), x, y, null);
			else if (a == 2)
				g.drawImage(ImageProvider.getIce(), x, y, null);
			else if (a == 3)
				g.drawImage(ImageProvider.getWaterA(), x, y, null);
			else if (a == 4)
				g.drawImage(ImageProvider.getTree(), x, y, null);
			else if (a == 5) {
				if (contFlag == 0) {
					if (isFlag && time == 0 && !((MainFrame) getSwitcher()).isTransparent()) {
						if (!((MainFrame) getSwitcher()).isTransparent())
							((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

						g.drawImage(ImageProvider.getFlag(), x, y, null);

						if (!((MainFrame) getSwitcher()).isTransparent())
							((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

					} else {
						g.drawImage(ImageProvider.getFlag(), x, y, null);
					}
				} else {

					if (!((MainFrame) getSwitcher()).isTransparent())
						((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

					g.drawImage(ImageProvider.getFlag(), x, y, null);

					if (!((MainFrame) getSwitcher()).isTransparent())
						((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
				}
			} else if (a == 6) {

				if (contPlayerP1 == 0) {
					if (isPlayer && time == 0) {
						if (!((MainFrame) getSwitcher()).isTransparent())
							((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

						g.drawImage(ImageProvider.getPlayer1A(), x, y, null);

						if (!((MainFrame) getSwitcher()).isTransparent())
							((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
					} else
						g.drawImage(ImageProvider.getPlayer1A(), x, y, null);
				} else {

					if (!((MainFrame) getSwitcher()).isTransparent())
						((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

					g.drawImage(ImageProvider.getPlayer1A(), x, y, null);

					if (!((MainFrame) getSwitcher()).isTransparent())
						((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
				}
			} else if (a == 7) {
				if (contPlayerP2 == 0) {
					if (isPlayer && time == 0) {
						if (!((MainFrame) getSwitcher()).isTransparent())
							((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

						g.drawImage(ImageProvider.getPlayer2A(), x, y, null);

						if (!((MainFrame) getSwitcher()).isTransparent())
							((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
					} else
						g.drawImage(ImageProvider.getPlayer2A(), x, y, null);
				} else {

					if (!((MainFrame) getSwitcher()).isTransparent())
						((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

					g.drawImage(ImageProvider.getPlayer2A(), x, y, null);

					if (!((MainFrame) getSwitcher()).isTransparent())
						((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
				}
			} else if (a == 8) {
				g.drawImage(ImageProvider.getTrash(), x, y, null);
			} else if (a == 9)

				if (contBasic > 0) {
					g.drawImage(ImageProvider.getBasicA(), x, y, null);
				} else {

					if (isEnemy && time == 1) {
						g.drawImage(ImageProvider.getBasicA(), x, y, null);
					} else {
						if (!((MainFrame) getSwitcher()).isTransparent())
							((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

						g.drawImage(ImageProvider.getBasicA(), x, y, null);

						if (!((MainFrame) getSwitcher()).isTransparent())
							((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
					}
				}
			else if (a == 10)

				if (contArmor > 0) {
					g.drawImage(ImageProvider.getArmorA(), x, y, null);
				} else {

					if (isEnemy && time == 1) {
						g.drawImage(ImageProvider.getArmorA(), x, y, null);
					} else {
						if (!((MainFrame) getSwitcher()).isTransparent())
							((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

						g.drawImage(ImageProvider.getArmorA(), x, y, null);

						if (!((MainFrame) getSwitcher()).isTransparent())
							((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
					}
				}
			else if (a == 11)

				if (contFast > 0) {
					g.drawImage(ImageProvider.getFastA(), x, y, null);
				} else {
					if (isEnemy && time == 1) {
						g.drawImage(ImageProvider.getFastA(), x, y, null);
					} else {
						if (!((MainFrame) getSwitcher()).isTransparent())
							((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

						g.drawImage(ImageProvider.getFastA(), x, y, null);

						if (!((MainFrame) getSwitcher()).isTransparent())
							((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
					}
				}
			else if (a == 12)

				if (contPower > 0) {
					g.drawImage(ImageProvider.getPowerA(), x, y, null);
				} else {

					if (isEnemy && time == 1) {
						g.drawImage(ImageProvider.getPowerA(), x, y, null);
					} else {
						if (!((MainFrame) getSwitcher()).isTransparent())
							((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

						g.drawImage(ImageProvider.getPowerA(), x, y, null);

						if (!((MainFrame) getSwitcher()).isTransparent())
							((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
					}
				}
		}

		paintNumber(g, contBasic, getWidth() - begin1 - 25, 170, numberOfEnemyPaint.get(0));
		paintNumber(g, contArmor, getWidth() - begin1 - 25, 220, numberOfEnemyPaint.get(1));
		paintNumber(g, contFast, getWidth() - begin1 - 25, 270, numberOfEnemyPaint.get(2));
		paintNumber(g, contPower, getWidth() - begin1 - 25, 320, numberOfEnemyPaint.get(3));

		for (int a = 0; a < positionCont.size(); a++) {

			int x = (int) positionCont.get(a).getX();
			int y = (int) positionCont.get(a).getY();

			if (a < 4)
				g.drawImage(ImageProvider.getPlus(), x, y, null);
			else
				g.drawImage(ImageProvider.getLess(), x, y - 5, null);
		}

		for (int a = 0; a < heightMatrix; a++) {

			for (int b = 0; b < widthMatrix; b++) {

				switch (matrix[a][b]) {
				case BRICKWALL:
					g.drawImage(ImageProvider.getBrick(), (b * tile) + begin1, (a * tile) + begin2,
							(int) (ImageProvider.getBrick().getWidth(null) * 0.9),
							(int) (ImageProvider.getBrick().getHeight(null) * 0.9), null);
					break;
				case STEELWALL:
					g.drawImage(ImageProvider.getSteel(), (b * tile) + begin1, (a * tile) + begin2,
							(int) (ImageProvider.getBrick().getWidth(null) * 0.9),
							(int) (ImageProvider.getBrick().getHeight(null) * 0.9), null);
					break;
				case ICE:
					g.drawImage(ImageProvider.getIce(), (b * tile) + begin1, (a * tile) + begin2,
							(int) (ImageProvider.getBrick().getWidth(null) * 0.9),
							(int) (ImageProvider.getBrick().getHeight(null) * 0.9), null);
					break;
				case WATER:
					g.drawImage(ImageProvider.getWaterA(), (b * tile) + begin1, (a * tile) + begin2,
							(int) (ImageProvider.getBrick().getWidth(null) * 0.9),
							(int) (ImageProvider.getBrick().getHeight(null) * 0.9), null);
					break;
				case FLAG:
					g.drawImage(ImageProvider.getFlag(), (b * tile) + begin1, (a * tile) + begin2,
							(int) (ImageProvider.getBrick().getWidth(null) * 0.9),
							(int) (ImageProvider.getBrick().getHeight(null) * 0.9), null);
					break;
				case TREE:
					g.drawImage(ImageProvider.getTree(), (b * tile) + begin1, (a * tile) + begin2,
							(int) (ImageProvider.getBrick().getWidth(null) * 0.9),
							(int) (ImageProvider.getBrick().getHeight(null) * 0.9), null);
					break;
				case PLAYER1:
					g.drawImage(ImageProvider.getPlayer1A(), (b * tile) + begin1, (a * tile) + begin2,
							(int) (ImageProvider.getBrick().getWidth(null) * 0.9),
							(int) (ImageProvider.getBrick().getHeight(null) * 0.9), null);
					break;
				case PLAYER2:
					g.drawImage(ImageProvider.getPlayer2A(), (b * tile) + begin1, (a * tile) + begin2,
							(int) (ImageProvider.getBrick().getWidth(null) * 0.9),
							(int) (ImageProvider.getBrick().getHeight(null) * 0.9), null);
					break;
				default:
					break;
				}
			}
		}
	}

	private void paintNumber(Graphics g, int numberEnemy, int positionX, int positionY, JLabel jLabel) {

		stringOfEnemy += numberEnemy;
		jLabel.setText(stringOfEnemy);
		jLabel.setBounds(positionX, positionY, 50, 40);
		stringOfEnemy = "";
	}

	private class MyListener extends MouseInputAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			if (type != TypeMatrix.DEFAULT) {
				double row = e.getX();
				double col = e.getY();

				for (int a = 0; a < heightMatrix; a++) {
					for (int b = 0; b < widthMatrix; b++) {

						if (matrixCell[a][b].intersect(col, row)
								&& (b < widthMatrix && a < heightMatrix && b >= 0 && a >= 0)) {
							if (((a == 0 && b == 0) || (b == widthMatrix / 2 && a == 0)
									|| (b == widthMatrix - 1 && a == 0)) && type != TypeMatrix.EMPTY) {
								matrix[a][b] = type;
								((MainFrame) getSwitcher()).setTransparent(true);
								setWarning(new WarningDialog("Warning! Cannot manage spawn positions.", matrix,
										((MainFrame) getSwitcher())));
							} else {
								if (matrix[a][b] == TypeMatrix.FLAG) {
									contFlag--;
								}

								if (matrix[a][b] == TypeMatrix.PLAYER1) {
									contPlayerP1--;
									if ((contPlayerP1 == 1 && contPlayerP2 == 0)
											|| (contPlayerP1 == 0 && contPlayerP2 == 1))
										setTotEnemyIntoMap(20);
									else
										setTotEnemyIntoMap(40);
								}

								if (matrix[a][b] == TypeMatrix.PLAYER2) {
									contPlayerP2--;
									if ((contPlayerP1 == 1 && contPlayerP2 == 0)
											|| (contPlayerP1 == 0 && contPlayerP2 == 1))
										setTotEnemyIntoMap(20);
									else
										setTotEnemyIntoMap(40);
								}

								if (((type == TypeMatrix.FLAG && contFlag == 0)
										|| (type == TypeMatrix.PLAYER1 && contPlayerP1 == 0)
										|| (type == TypeMatrix.PLAYER2 && contPlayerP2 == 0))
										|| (type != TypeMatrix.PLAYER2 && type != TypeMatrix.PLAYER1
												&& type != TypeMatrix.FLAG)) {
									matrix[a][b] = type;
									repaint();
								}

								if (type == TypeMatrix.FLAG && contFlag == 0) {
									contFlag++;
								}

								if (type == TypeMatrix.PLAYER1 && contPlayerP1 == 0) {
									contPlayerP1++;
									if ((contPlayerP1 == 1 && contPlayerP2 == 0)
											|| (contPlayerP1 == 0 && contPlayerP2 == 1))
										setTotEnemyIntoMap(20);
									else
										setTotEnemyIntoMap(40);
								}

								if (type == TypeMatrix.PLAYER2 && contPlayerP2 == 0) {
									contPlayerP2++;
									if ((contPlayerP1 == 1 && contPlayerP2 == 0)
											|| (contPlayerP1 == 0 && contPlayerP2 == 1))
										setTotEnemyIntoMap(20);
									else
										setTotEnemyIntoMap(40);
								}
							}
						}
					}
				}
			}

			for (int a = 0; a < positionLabel.size(); a++) {

				if (positionLabel.get(a).contains(e.getX(), e.getY())) {
					selection = true;

					repaint();
					if (a == 0) {

						type = TypeMatrix.BRICKWALL;
						positionXObject = positionLabel.get(0).x;
						positionYObject = positionLabel.get(0).y;
						break;
					} else if (a == 1) {

						type = TypeMatrix.STEELWALL;
						positionXObject = positionLabel.get(1).x;
						positionYObject = positionLabel.get(1).y;
						break;
					} else if (a == 2) {

						type = TypeMatrix.ICE;
						positionXObject = positionLabel.get(2).x;
						positionYObject = positionLabel.get(2).y;
						break;
					} else if (a == 3) {

						type = TypeMatrix.WATER;
						positionXObject = positionLabel.get(3).x;
						positionYObject = positionLabel.get(3).y;
						break;
					} else if (a == 4) {

						type = TypeMatrix.TREE;
						positionXObject = positionLabel.get(4).x;
						positionYObject = positionLabel.get(4).y;
						break;
					} else if (a == 5 && contFlag == 0) {

						type = TypeMatrix.FLAG;
						positionXObject = positionLabel.get(5).x;
						positionYObject = positionLabel.get(5).y;
						isFlag = false;
						break;
					} else if (a == 6 && contPlayerP1 == 0) {

						type = TypeMatrix.PLAYER1;
						positionXObject = positionLabel.get(6).x;
						positionYObject = positionLabel.get(6).y;
						isPlayer = false;
						break;
					} else if (a == 7) {
						type = TypeMatrix.PLAYER2;
						positionXObject = positionLabel.get(7).x;
						positionYObject = positionLabel.get(7).y;
						isPlayer = false;
						break;
					} else if (a == 8) {

						type = TypeMatrix.EMPTY;
						positionXObject = positionLabel.get(8).x;
						positionYObject = positionLabel.get(8).y;
						break;
					}
				}
			}

			for (int a = 0; a < positionCont.size(); a++) {

				if (positionCont.get(a).contains(e.getX(), e.getY())) {
					if (a == 0 && contBasic < totEnemyForType)
						contBasic++;
					else if (a == 1 && contArmor < totEnemyForType)
						contArmor++;
					else if (a == 2 && contFast < totEnemyForType)
						contFast++;
					else if (a == 3 && contPower < totEnemyForType)
						contPower++;
					else if (a == 4 && contBasic != 0)
						contBasic--;
					else if (a == 5 && contArmor != 0)
						contArmor--;
					else if (a == 6 && contFast != 0)
						contFast--;
					else if (a == 7 && contPower != 0)
						contPower--;
				}
			}

			if (contBasic > 0 || contArmor > 0 || contFast > 0 || contPower > 0)
				isEnemy = false;
			repaint();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (type != TypeMatrix.DEFAULT) {
				double row = e.getX();
				double col = e.getY();

				for (int a = 0; a < heightMatrix; a++) {
					for (int b = 0; b < widthMatrix; b++) {
						if (matrixCell[a][b].intersect(col, row) && ((b == 0 && a == 0)
								|| (b == widthMatrix / 2 && a == 0) || (b == widthMatrix - 1 && a == 0))
								&& type != TypeMatrix.EMPTY) {
							matrix[a][b] = type;
							((MainFrame) getSwitcher()).setTransparent(true);
							setWarning(new WarningDialog("Warning! Cannot manage spawn positions.", matrix,
									((MainFrame) getSwitcher())));
						} else if (matrixCell[a][b].intersect(col, row) && type != TypeMatrix.FLAG
								&& type != TypeMatrix.PLAYER1 && type != TypeMatrix.PLAYER2) {
							if (matrix[a][b] == TypeMatrix.FLAG) {
								contFlag--;
							}
							if (matrix[a][b] == TypeMatrix.PLAYER1) {
								contPlayerP1--;
								if ((contPlayerP1 == 1 && contPlayerP2 == 0)
										|| (contPlayerP1 == 0 && contPlayerP2 == 1))
									setTotEnemyIntoMap(20);
								else
									setTotEnemyIntoMap(40);
							} else if (matrix[a][b] == TypeMatrix.PLAYER2) {
								contPlayerP2--;
								if ((contPlayerP1 == 1 && contPlayerP2 == 0)
										|| (contPlayerP1 == 0 && contPlayerP2 == 1))
									setTotEnemyIntoMap(20);
								else
									setTotEnemyIntoMap(40);
							}

							matrix[a][b] = type;
							repaint();
						}
					}
				}
			}
		}
	}

	private class Cell {
		public Rectangle r;

		public Cell(int pixel, int a, int b, int dim1, int dim2) {
			r = new Rectangle((dim1 + pixel + (a * pixel)), (dim2 + pixel + (b * pixel)), pixel, pixel);
		}

		boolean intersect(double x, double y) {
			if (r.contains(x, y)) {
				return true;
			}
			return false;
		}

	}

	public int getCursorPosition() {
		return cursorPosition;
	}

	public void setCursorPosition(int cursorPosition) {
		this.cursorPosition = cursorPosition;
	}

	public JButton getButton(int i) {
		return buttons.get(i);
	}

	public PanelSwitcher getSwitcher() {
		return switcher;
	}

	public void setSwitcher(PanelSwitcher switcher) {
		this.switcher = switcher;
	}

	public int getTotEnemyIntoMap() {
		return totEnemyIntoMap;
	}

	public void setTotEnemyIntoMap(int totEnemyIntoMap) {
		this.totEnemyIntoMap = totEnemyIntoMap;
	}

	public WarningDialog getWarning() {
		return warning;
	}

	public void setWarning(WarningDialog warning) {
		this.warning = warning;
	}

	public ArrayList<Rectangle> getPositionLabel() {
		return positionLabel;
	}

	public int getBegin1() {
		return begin1;
	}

	public int getBegin2() {
		return begin2;
	}

	public int getEnd1() {
		return end1;
	}

	public int getSizeButton() {
		return sizeButton;
	}

	public ArrayList<Rectangle> getPositionCont() {
		return positionCont;
	}

	public void setBegin1(int begin1) {
		this.begin1 = begin1;
	}

	public int getWidthMatrix() {
		return widthMatrix;
	}

	public ArrayList<JButton> getButtons() {
		return buttons;
	}

	public int getHeightMatrix() {
		return heightMatrix;
	}

	public void setBegin2(int begin2) {
		this.begin2 = begin2;
	}

}
