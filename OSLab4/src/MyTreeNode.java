import java.util.HashMap;

public class MyTreeNode {

    protected String nameObject;
    protected int nodeSize;
    protected Cluster firstCluster;
    protected boolean isFolder;
    protected HashMap<String,Integer> formats = new HashMap<>();
    public MyTreeNode() {
    	formats.put("txt",2);
    	formats.put("docx",4);
    	formats.put("xlsx",8);
    	formats.put("vpp",16);
    	formats.put("obj",16);
    }
    
    public MyTreeNode(String nameFolder, boolean isFolder) {
        this.nameObject = nameFolder;
        this.isFolder = isFolder;
        System.out.println(String.format("Create \"%s\" folder", nameFolder));
    }

    public boolean isFolder() {
        return isFolder;
    }

    public String getName() {
        return nameObject;
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

}
