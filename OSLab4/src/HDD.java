import javax.swing.*;
import java.awt.*;

public class HDD extends JPanel {

    private final int width = 28, height = 28;
    private int countOfEmptySectors;
    private Cluster[] diskSectors;
    private Cluster tempFileCluster;

    public HDD(int diskPartitionSize, int diskSectorSize) {
        countOfEmptySectors = diskPartitionSize / diskSectorSize;
        diskSectors = new Cluster[countOfEmptySectors];

        for (int i = 0; i < diskSectors.length; i++) {
            diskSectors[i] = new Cluster(0, true);
        }
    }

    //заполнение кластеров диска
    public void addToHDD(MyTreeNode direction) {
    	System.out.println(String.format("Adding a file of size %d", direction.getSize()));
        tempFileCluster = direction.getFirstCluster();
        for (int i = 0; i < direction.getSize(); i++) {
            for (int j = 0; j < diskSectors.length; j++) {
                if (diskSectors[j].getSelectionType() == 0) {
                    System.out.println(String.format("Filling cluster of disk %d with cluster of \"%s\" file", j, direction.getName()));
                    diskSectors[j] = tempFileCluster;
                    tempFileCluster = tempFileCluster.getLinkOnNextCluster();
                    break;
                }
            }
        }
        countOfEmptySectors -= direction.getSize();
    }
    
    //заполнение кластеров диска
    public void addToHDD(File file) {
    	System.out.println(String.format("Adding a file of size %d", file.getSize()));
        tempFileCluster = file.getFirstCluster();
        for (int i = 0; i < file.getSize(); i++) {
            for (int j = 0; j < diskSectors.length; j++) {
                if (diskSectors[j].getSelectionType() == 0) {
                    System.out.println(String.format("Filling cluster of disk %d with cluster of \"%s\" file", j, file.getName(), file.getFileFormat()));
                    diskSectors[j] = tempFileCluster;
                    tempFileCluster = tempFileCluster.getLinkOnNextCluster();
                    break;
                }
            }
        }
        countOfEmptySectors -= file.getSize();
    }

    //получаем кол-во пустых кластеров
    public int getCountOfEmptySectors() {
        return countOfEmptySectors;
    }

    public void setCountOfEmptySectors(int countOfEmptySectors) {
        this.countOfEmptySectors += countOfEmptySectors;
    }

    //удаление выделения у секторов
    public void removeSelection() {
        for(int i = 0; i < diskSectors.length; i++) {
            if (!(diskSectors[i].getSelectionType() == 0)) {
                diskSectors[i].setSelectionType(1);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int x = 10, y = 10;

        for (int i = 0; i < diskSectors.length; i++) {
            if (x + width >= this.getWidth()) {
                x = 10;
                y += height;
            }
        	switch(diskSectors[i].getSelectionType()) {
        		case 0:
        			g.setColor(Color.GRAY);
        			break;
        		case 1:
        			g.setColor(Color.GREEN);
        			break;
        		case 2:
        			g.setColor(Color.RED);
        			break;
        		case 3:
        			g.setColor(Color.BLUE);
        			break;
        	}


            g.fillRect(x, y, width - 3, height - 3);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, width - 3, height - 3);
            x += width;
        }
    }
}
