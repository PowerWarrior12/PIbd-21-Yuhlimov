import java.util.HashMap;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class Methods {

    private HDD hardDrive;
    private JTree fileManager;
    private DefaultMutableTreeNode folder;
    private DefaultMutableTreeNode file;
    private MyTreeNode treeNode;
    private MyTreeNode newTreeNode;
    private DefaultMutableTreeNode newJTreeNode;
    private DefaultMutableTreeNode newNode;
    private DefaultMutableTreeNode defaultMutableTreeNode;
    private boolean check = false;
    private HashMap<String, Integer> identicalObjectsForFile = new HashMap<>();
    private HashMap<String, Integer> identicalObjectsForFolder = new HashMap<>();
    private int countCopy = 0;
    private int nodeSize = 0;
    private StringBuilder nameObject;

    public Methods(DefaultMutableTreeNode root, HDD hardDrive, JTree fileManager) {
        this.hardDrive = hardDrive;
        this.fileManager = fileManager;
        initializeJTree(root);
    }

    //������������� ��������� �����
    public void setOptions(String nameNode, String FileExpansion) {
        treeNode = new MyTreeNode(nameNode, FileExpansion, false);
        file = new DefaultMutableTreeNode(treeNode);
        folder.add(file);
        hardDrive.addToHDD(treeNode);
    }

    //�������������� ������
    public void initializeJTree(DefaultMutableTreeNode root) {
        folder = new DefaultMutableTreeNode(new MyTreeNode("Folder1", true));
        root.add(folder);
        setOptions("File1", "txt");
        setOptions("File2", "docx");
        setOptions("File3", "xlsx");
        displayTheSelectedObject(folder, 1);

        folder = new DefaultMutableTreeNode(new MyTreeNode("Folder2", true));
        root.add(folder);
        setOptions("File_1", "vpp");
        setOptions("File_2", "txt");
        setOptions("File_3", "obj");
        displayTheSelectedObject(folder, 1);
    }

    //�������� ������ ����
    public int getNodeSize(DefaultMutableTreeNode node) {
        nodeSize = 0;
        if(((MyTreeNode)node.getUserObject()).isFolder()) {
            for (int i = 0; i < node.getChildCount(); i++) {
                defaultMutableTreeNode = (DefaultMutableTreeNode)node.getChildAt(i);
                if (!((MyTreeNode) defaultMutableTreeNode.getUserObject()).isFolder()){
                    nodeSize += ((MyTreeNode) defaultMutableTreeNode.getUserObject()).getSize();
                } else {
                    nodeSize += getNodeSize((DefaultMutableTreeNode) node.getChildAt(i));
                }
            }
            System.out.println(String.format("Folder size %s is %d", node, nodeSize));
            return nodeSize;
        } else {
        	System.out.println(String.format("File size %s is %d", node, nodeSize));
            return nodeSize = ((MyTreeNode) defaultMutableTreeNode.getUserObject()).getSize();
        }
    }

    //��������� ���� � ������
    public MyTreeNode addToJTree(DefaultMutableTreeNode selectedNode, MyTreeNode node) {
        nameObject = new StringBuilder(node.getName());
        check = false;
        if(!(identicalObjectsForFile.containsKey(node.getName())) && !(node.isFolder())) {
            identicalObjectsForFile.put(node.getName(), 1);
        } else if(!(identicalObjectsForFolder.containsKey(node.getName())) && (node.isFolder())) {
            identicalObjectsForFolder.put(node.getName(), 1);
        }

        for (; ;) { //��������� ������� ������������ ������� � ����� ������
            for (int i = 0; i < selectedNode.getChildCount(); i++) {
                //���� ����������� ���� ��� �����, ������� ��� ���� � ������
                if ((!(((MyTreeNode) ((DefaultMutableTreeNode) selectedNode.getChildAt(i)).getUserObject()).isFolder())
                        && ((MyTreeNode) ((DefaultMutableTreeNode) selectedNode.getChildAt(i)).getUserObject()).getName().contentEquals(nameObject)
                        && ((MyTreeNode) ((DefaultMutableTreeNode) selectedNode.getChildAt(i)).getUserObject()).getFileFormat().equals(node.getFileFormat()))
                        || (((MyTreeNode) ((DefaultMutableTreeNode) selectedNode.getChildAt(i)).getUserObject()).isFolder()
                        && ((MyTreeNode) ((DefaultMutableTreeNode) selectedNode.getChildAt(i)).getUserObject()).getName().contentEquals(nameObject))) {
                    check = true;
                }
            }
            //������� ������� ��� ���� ���� ��� ����� ����������� � ������
            if (check) {
                if(identicalObjectsForFile.containsKey(node.getName()) && !(node.isFolder())) {
                    countCopy = identicalObjectsForFile.get(node.getName());
                    identicalObjectsForFile.put(node.getName(), identicalObjectsForFile.get(node.getName()) + 1);
                } else if(identicalObjectsForFolder.containsKey(node.getName()) && (node.isFolder())) {
                    countCopy = identicalObjectsForFolder.get(node.getName());
                    identicalObjectsForFolder.put(node.getName(), identicalObjectsForFolder.get(node.getName()) + 1);
                }
                check = false;
                nameObject.append(String.format(" (%d)", countCopy));
            } else {
                break;
            }
        }

        if(node.isFolder()) {
            newTreeNode = new MyTreeNode(nameObject.toString(), true);
        } else {
            newTreeNode = new MyTreeNode(nameObject.toString(), node.getFileFormat(), false);
        }

        newJTreeNode = new DefaultMutableTreeNode(newTreeNode);
        selectedNode.add(newJTreeNode);

        DefaultTreeModel defaultTreeModel = (DefaultTreeModel) fileManager.getModel();
        defaultTreeModel.reload();

        fileManager.scrollPathToVisible(new TreePath(newJTreeNode.getPath()));
        fileManager.setSelectionPath(new TreePath(newJTreeNode.getPath()));
        return newTreeNode;
    }

    //������� ���� �� ������
    public void removeFromJTree(boolean removeFromHDD) {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)fileManager.getLastSelectedPathComponent();
        if(selectedNode != fileManager.getModel().getRoot()) {
            if (removeFromHDD) {
                displayTheSelectedObject(selectedNode, 0);
            } else {
                displayTheSelectedObject(selectedNode, 3);
            }

            hardDrive.setCountOfEmptySectors(getNodeSize(selectedNode));

            MyTreeNode node = (MyTreeNode) selectedNode.getUserObject();
            String nameNode = node.getName();
            if(!node.isFolder()) {
                String fileExtension = node.getFileFormat();
                System.out.println(String.format("Remove from HDD \"%s.%s\"", nameNode, fileExtension));
            } else {
            	System.out.println(String.format("Remove from HDD \"%s\"", nameNode));
            }

            DefaultTreeModel defaultTreeModel = (DefaultTreeModel)fileManager.getModel();
            defaultTreeModel.removeNodeFromParent(selectedNode);
            defaultTreeModel.reload();
        }
    }

    //���������� ���� � ��������� �����, ������ �� ��������
    public void moveInFolder (DefaultMutableTreeNode selectedNode, DefaultMutableTreeNode newNode){
        copyTheNode(selectedNode, newNode, false);
        fileManager.setSelectionPath(new TreePath(selectedNode.getPath()));
        removeFromJTree(false);
    }

    //�������� ���� � ��� �������� (���� ����)
    public void copyTheNode(DefaultMutableTreeNode selectedNode, DefaultMutableTreeNode newNodeToCopy, boolean createNewFile) {
        MyTreeNode object = (MyTreeNode) selectedNode.getUserObject();
        nameObject = new StringBuilder(object.getName());
        check = false;

        for (; ;) {
            for (int i = 0; i < newNodeToCopy.getChildCount(); i++) {
                //��������� ������� ������� � ������: ���� ��� ����� ��� ����, � ������� ��������� �������� � ��� ���������� � ������
                if (!(((MyTreeNode) ((DefaultMutableTreeNode) newNodeToCopy.getChildAt(i)).getUserObject()).isFolder())
                        && ((MyTreeNode) ((DefaultMutableTreeNode) newNodeToCopy.getChildAt(i)).getUserObject()).getName().contentEquals(nameObject)
                        && ((MyTreeNode) ((DefaultMutableTreeNode) newNodeToCopy.getChildAt(i)).getUserObject()).getFileFormat().equals(object.getFileFormat())
                        || ((MyTreeNode) ((DefaultMutableTreeNode) newNodeToCopy.getChildAt(i)).getUserObject()).isFolder()
                        && ((MyTreeNode) ((DefaultMutableTreeNode) newNodeToCopy.getChildAt(i)).getUserObject()).getName().contentEquals(nameObject)) {
                    check = true;
                }
            }

            if (check) {
                nameObject.append(" � copy");
                check = false;
            } else {
                break;
            }
        }

        if(createNewFile) {
            if(object.isFolder()) {
                newTreeNode = new MyTreeNode(nameObject.toString(), true);
            } else {
                newTreeNode = new MyTreeNode(nameObject.toString(), object.getFileFormat(), false);
                hardDrive.addToHDD(newTreeNode);
            }
        } else {
            newTreeNode = (MyTreeNode) selectedNode.getUserObject();
        }

        //�������� ���� � ��� ��������, � ����� ��������� � ������
        newNode = new DefaultMutableTreeNode(newTreeNode);
        newNodeToCopy.add(newNode);
        for (int i = 0; i < selectedNode.getChildCount(); i++) {
            copyTheNode((DefaultMutableTreeNode)selectedNode.getChildAt(i), newNode, createNewFile);
        }

        DefaultTreeModel defaultTreeModel = (DefaultTreeModel) fileManager.getModel();
        defaultTreeModel.reload();
        fileManager.scrollPathToVisible(new TreePath(newNode.getPath()));
        displayTheSelectedObject(newNode, 3);
    }

    //���������� ��������� � ������ ������ (������� ���������, ���� �� ��� �� ������)
    public void displayTheSelectedObject(DefaultMutableTreeNode selectedNode, int selectionType) {

        //���� ������� �����, �� �������� ���� ��������, ����� ������ ���� � �������� ������ ���
        if (!selectedNode.isRoot() && ((MyTreeNode) selectedNode.getUserObject()).isFolder()) {
            for (int i = 0; i < selectedNode.getChildCount(); i++) {
                defaultMutableTreeNode = (DefaultMutableTreeNode) selectedNode.getChildAt(i);
                displayTheSelectedObject(defaultMutableTreeNode, selectionType);
            }
        } else if (!selectedNode.isRoot() && !((MyTreeNode) selectedNode.getUserObject()).isFolder()) {
            ((MyTreeNode) selectedNode.getUserObject()).displayTheSelectedObject(selectionType);
        }
        hardDrive.repaint();
    }
}
