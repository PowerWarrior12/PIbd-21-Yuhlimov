import java.util.HashMap;

public class MyTreeNode {

    private String nameObject;
    private String fileFormat;
    private int nodeSize;
    private Cluster firstCluster;
    private boolean isFolder;
    HashMap<String,Integer> formats = new HashMap<>();
    public MyTreeNode() {
    }
    
    public MyTreeNode(String nameFolder, boolean isFolder) {
        this.nameObject = nameFolder;
        this.isFolder = isFolder;
        System.out.println(String.format("Create \"%s\" folder", nameFolder));
    }

    public MyTreeNode(String nameFile, String fileExtension, boolean isFolder) {
    	formats.put("txt",2);
    	formats.put("docx",4);
    	formats.put("xlsx",8);
    	formats.put("vpp",16);
    	formats.put("obj",16);
        this.nameObject = nameFile;
        this.fileFormat = fileExtension;
        this.isFolder = isFolder;
        if (formats.containsKey(fileExtension))
        	nodeSize = formats.get(fileExtension);
        else
        	nodeSize = 0;
        firstCluster = new Cluster(nodeSize, false);
        System.out.println(String.format("Create \"%s.%s\" file", nameFile, fileExtension));
    }

    public boolean isFolder() {
        return isFolder;
    }

    public String getName() {
        return nameObject;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void displayTheSelectedObject(int selectionType) {
        firstCluster.setSelectionType(selectionType);
    }

    public Cluster getFirstCluster() {
        return firstCluster;
    }

    public int getSize() {
        return nodeSize;
    }

    public int getSizeByFileFormat(String fileExtension) {
        if (formats.containsKey(fileExtension))
        	return nodeSize;
        else
        	return 0;
    }
   @Override
    public String toString() {
        if (isFolder) {
            return nameObject;
        }
        return nameObject + "." + fileFormat;
    }
}
