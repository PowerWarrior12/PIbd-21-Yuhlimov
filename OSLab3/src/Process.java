import java.util.ArrayList;
import java.util.Random;

public class Process {
    private final ArrayList<Page> VirtualMemory;
    private final int ID;
    Random r = new Random();

    Process(int ID) {
        this.ID = ID;
        VirtualMemory = new ArrayList<>();
        int pagesCount = r.nextInt(4) + 2;
        for (int i = 1; i <= pagesCount; i++) {
        	VirtualMemory.add(new Page(i, ID));
        }
        System.out.println("Process ID " + ID + " : " + pagesCount + " pages");
    }

    public ArrayList<Page> getVirtualMemory() {
        return VirtualMemory;
    }

    public int getID() {
        return ID;
    }
}
