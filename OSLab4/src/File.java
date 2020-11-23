import java.util.HashMap;

public class File extends MyTreeNode{

    private String nameObject;
    private String fileFormat;
    private int nodeSize;
    private Cluster firstCluster;
    public File() {
    }

    public File(String nameFile, String fileExtension) {
    	formats.put("txt",2);
    	formats.put("docx",4);
    	formats.put("xlsx",8);
    	formats.put("vpp",16);
    	formats.put("obj",16);
    	this.isFolder = false;
        this.nameObject = nameFile;
        this.fileFormat = fileExtension;
        if (formats.containsKey(fileExtension))
        	nodeSize = formats.get(fileExtension);
        else
        	nodeSize = 0;
        firstCluster = new Cluster(nodeSize, false);
        System.out.println(String.format("Create \"%s.%s\" file", nameFile, fileExtension));
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

   @Override
    public String toString() {
        return nameObject + "." + fileFormat;
    }
}
