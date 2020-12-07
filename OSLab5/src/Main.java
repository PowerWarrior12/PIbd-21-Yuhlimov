public class Main {

    public static void main(String[] args) throws CloneNotSupportedException {
        Core core = new Core();
        core.createProcesses();
        core.processesPlanningWithoutBlocking();
        core.processesPlanningWithBlocking();
        core.compareResult();
    }
}