public class Direction extends MyTreeNode{
    
    public Direction(String nameFolder) {
        this.nameObject = nameFolder;
        this.isFolder = true;
        System.out.println(String.format("Create \"%s\" folder", nameFolder));
    }
    
}
