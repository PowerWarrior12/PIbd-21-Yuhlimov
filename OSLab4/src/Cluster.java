public class Cluster {

    private Cluster nextCluster;
    private int needCountCluster;
    private boolean isEmpty;
    private int Type = 0;

    public Cluster(int countClusters, boolean isEmpty) {
        this.needCountCluster = countClusters;
        if (!isEmpty) {
        	Type = 1;
        }
        if (needCountCluster > 0) {
        	System.out.println(String.format("Create a file cluster. It remains to create %d more cluster(s)", needCountCluster));
            nextCluster = new Cluster(needCountCluster - 1, false);
        }
    }
    public int getSelectionType() {
        return Type;
    }
    public void setSelectionType(int selectionType) {
        this.Type = selectionType;
        if(selectionType == 0) {
            isEmpty = true;
        }
        if(nextCluster != null) {
            nextCluster.setSelectionType(selectionType);
        }
    }
    public Cluster getLinkOnNextCluster() {
        return nextCluster;
    }
    public boolean isEmpty() {
        return isEmpty;
    }
}
