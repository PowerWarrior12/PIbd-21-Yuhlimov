import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.tree.DefaultTreeCellRenderer;

public class Main {

	private HDD hardDrive;
	private JTree fileManager;
	private Methods methods;

	private JFrame frame;
	private JComboBox comboBoxFileFormats;
	private DefaultMutableTreeNode selectedItem;
	private DefaultMutableTreeNode newItemInTree;
	private JScrollPane treeViewScrollPane;
	private JScrollPane consoleScrollPane;
	private JTextField textField;
	private JLabel labelNameObject;
	private JRadioButton radioButtonFolder;
	private JRadioButton radioButtonFile;
	private ButtonGroup group;
	private JButton buttonCopyInFolder;
	private JButton buttonMoveInFolder;
	private JButton buttonCreate;
	private JButton buttonDelete;
	private JButton buttonCopy;
	private JButton buttonMove;
	private MyTreeNode node;

	private final int diskPartitionSize = 2048; // размеры дискового раздела
	private final int diskSectorSize = 4; // размеры сектора диска
	private String[] fileFormats = { "txt", "docx", "xlsx", "vpp", "obj" };

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private Main() {
		initialize();
	}

	public void initialize() {

		frame = new JFrame();
		frame.setBounds(100, 100, 1200, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		Font font = new Font("Serif", Font.BOLD, 14);

		hardDrive = new HDD(diskPartitionSize, diskSectorSize);
		hardDrive.setBorder(new LineBorder(new Color(0, 0, 0)));
		hardDrive.setBounds(5, 10, 1169, 400);
		hardDrive.setBackground(Color.WHITE);
		hardDrive.setPreferredSize(new Dimension(800, 1000));
		frame.getContentPane().add(hardDrive);

		// создаем и инициализируем дерево
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(new MyTreeNode("Hard Disk Drive", true).getName());
		fileManager = new JTree(root);
		methods = new Methods(root, hardDrive, fileManager);
		fileManager.setEditable(true);
		fileManager.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		DefaultTreeModel model = (DefaultTreeModel) fileManager.getModel();
		model.reload();

		// отображаем занятое пространство выбранного узла в дереве
		fileManager.addTreeSelectionListener(e -> {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) fileManager.getLastSelectedPathComponent();
			if (node != null) {
				hardDrive.removeSelection();
				methods.displayTheSelectedObject(node, 2);
			}
		});
		fileManager.setCellRenderer(new DefaultTreeCellRenderer());

		treeViewScrollPane = new JScrollPane(fileManager);
		treeViewScrollPane.setBounds(702, 416, 472, 229);
		frame.getContentPane().add(treeViewScrollPane);

		textField = new JTextField();
		textField.setBounds(318, 421, 269, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		comboBoxFileFormats = new JComboBox(fileFormats);
		comboBoxFileFormats.setBounds(592, 421, 100, 20);
		comboBoxFileFormats.setFocusable(false);
		comboBoxFileFormats.setEditable(false);
		frame.getContentPane().add(comboBoxFileFormats);

		labelNameObject = new JLabel("Enter name :");
		labelNameObject.setBounds(250, 421, 95, 20);
		frame.getContentPane().add(labelNameObject);

		radioButtonFolder = new JRadioButton("Folder");
		radioButtonFolder.setBounds(250, 503, 100, 25);
		frame.getContentPane().add(radioButtonFolder);

		radioButtonFile = new JRadioButton("File");
		radioButtonFile.setBounds(592, 503, 100, 25);
		frame.getContentPane().add(radioButtonFile);

		group = new ButtonGroup();
		group.add(radioButtonFile);
		group.add(radioButtonFolder);

		node = new MyTreeNode();

		buttonCreate = new JButton("Create");
		buttonCreate.setBorder(new LineBorder(new Color(0, 0, 0)));
		buttonCreate.setFocusPainted(false);
		buttonCreate.setContentAreaFilled(false);
		buttonCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (radioButtonFile.isSelected()) {
					if (node.getSizeByFileFormat((String) comboBoxFileFormats.getSelectedItem()) <= hardDrive
							.getCountOfEmptySectors() && radioButtonFile.isSelected()) {
						selectedItem = (DefaultMutableTreeNode) fileManager.getLastSelectedPathComponent();
						if (selectedItem.isRoot() || ((MyTreeNode) selectedItem.getUserObject()).isFolder()) {
							MyTreeNode treeNode = methods.addToJTree(selectedItem, new MyTreeNode(textField.getText(),
									(String) comboBoxFileFormats.getSelectedItem(), false));
							if (treeNode != null) {
								hardDrive.addToHDD(treeNode);
							}
						} else {
							JOptionPane.showMessageDialog(hardDrive, "Can not add file to file", "Warning!",
									JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(frame, "Not enough free space", "Warning!",
								JOptionPane.ERROR_MESSAGE);
					}
				} else if (radioButtonFolder.isSelected()) {
					selectedItem = (DefaultMutableTreeNode) fileManager.getLastSelectedPathComponent();
					methods.addToJTree(selectedItem, new MyTreeNode(textField.getText(), true));
				} else {
					JOptionPane.showMessageDialog(frame, "Indicate what you are creating", "Warning!",
							JOptionPane.INFORMATION_MESSAGE);
				}
				hardDrive.repaint();
			}
		});
		buttonCreate.setBounds(251, 462, 441, 25);
		frame.getContentPane().add(buttonCreate);

		buttonDelete = new JButton("Delete");
		buttonDelete.setBorder(new LineBorder(new Color(0, 0, 0)));
		buttonDelete.setFocusPainted(false);
		buttonDelete.setContentAreaFilled(false);
		buttonDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				methods.removeFromJTree(true);
				hardDrive.repaint();
			}
		});
		buttonDelete.setBounds(251, 535, 441, 25);
		frame.getContentPane().add(buttonDelete);

		buttonCopy = new JButton("Copy");
		buttonCopy.setBorder(new LineBorder(new Color(0, 0, 0)));
		buttonCopy.setFocusPainted(false);
		buttonCopy.setContentAreaFilled(false);
		buttonCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedItem = (DefaultMutableTreeNode) fileManager.getLastSelectedPathComponent();
				if (methods.getNodeSize(selectedItem) <= hardDrive.getCountOfEmptySectors()) {
					buttonCopy.setEnabled(false);
					buttonCopyInFolder.setEnabled(true);
				} else {
					JOptionPane.showMessageDialog(hardDrive, "Object size is too large", "Warning!",
							JOptionPane.ERROR_MESSAGE);
				}
				hardDrive.repaint();
			}
		});
		buttonCopy.setBounds(423, 571, 269, 25);
		buttonCopy.setEnabled(true);
		frame.getContentPane().add(buttonCopy);

		buttonCopyInFolder = new JButton("Copy to folder");
		buttonCopyInFolder.setBorder(new LineBorder(new Color(0, 0, 0)));
		buttonCopyInFolder.setFocusPainted(false);
		buttonCopyInFolder.setContentAreaFilled(false);
		buttonCopyInFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonCopy.setEnabled(true);
				buttonCopyInFolder.setEnabled(false);
				newItemInTree = (DefaultMutableTreeNode) fileManager.getLastSelectedPathComponent();
				if (!(selectedItem == newItemInTree)) {
					methods.copyTheNode(selectedItem, newItemInTree, true);
				} else {
					JOptionPane.showMessageDialog(hardDrive, "An error occurred while copying", "Warning!",
							JOptionPane.ERROR_MESSAGE);
				}
				hardDrive.repaint();
			}
		});
		buttonCopyInFolder.setBounds(251, 571, 164, 25);
		buttonCopyInFolder.setEnabled(false);
		frame.getContentPane().add(buttonCopyInFolder);

		buttonMove = new JButton("Move");
		buttonMove.setBorder(new LineBorder(new Color(0, 0, 0)));
		buttonMove.setFocusPainted(false);
		buttonMove.setEnabled(true);
		buttonMove.setContentAreaFilled(false);
		buttonMove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonMove.setEnabled(false);
				buttonMoveInFolder.setEnabled(true);
				selectedItem = (DefaultMutableTreeNode) fileManager.getLastSelectedPathComponent();
				hardDrive.repaint();
			}
		});
		buttonMove.setBounds(423, 607, 269, 25);
		buttonMove.setEnabled(true);
		frame.getContentPane().add(buttonMove);

		buttonMoveInFolder = new JButton("Move to folder");
		buttonMoveInFolder.setBorder(new LineBorder(new Color(0, 0, 0)));
		buttonMoveInFolder.setFocusPainted(false);
		buttonMoveInFolder.setEnabled(true);
		buttonMoveInFolder.setContentAreaFilled(false);
		buttonMoveInFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonMove.setEnabled(true);
				buttonMoveInFolder.setEnabled(false);
				newItemInTree = (DefaultMutableTreeNode) fileManager.getLastSelectedPathComponent();
				if (!(selectedItem == newItemInTree)) {
					methods.moveInFolder(selectedItem, newItemInTree);
				} else {
					JOptionPane.showMessageDialog(hardDrive, "You can not move the folder to itself", "Warning!",
							JOptionPane.ERROR_MESSAGE);
				}
				hardDrive.repaint();
			}
		});
		buttonMoveInFolder.setBounds(251, 607, 164, 25);
		buttonMoveInFolder.setEnabled(false);
		frame.getContentPane().add(buttonMoveInFolder);
	}
}
