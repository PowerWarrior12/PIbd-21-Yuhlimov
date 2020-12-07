import java.util.ArrayList;
import java.util.Random;

public class Core {

    private ArrayList <Process> processesListOne = new ArrayList <>();
    private ArrayList <Process> processesListTwo = new ArrayList <>();
    private ArrayList <Process> listOfBlockedProcesses = new ArrayList <>();
    private Random rnd = new Random();
    private int numberOfProcesses = 6;
    private int quantumTime = 100;
    private int allTime = 0;
    private int maxWaitingTime = Integer.MAX_VALUE;

    public void createProcesses() throws CloneNotSupportedException {
        for (int pid = 0; pid < numberOfProcesses; pid++) {

            Process process;
            //0 - not blocked
            //1 - blocked
            int blockProcess = rnd.nextInt(2);
            if (blockProcess == 0) {
                process = new Process(pid, quantumTime, false);
            } else {
                process = new Process(pid, quantumTime, true);
            }
            processesListOne.add(process);
            //Cloning processes
            Process clone = (Process) process.clone();
            processesListTwo.add(clone);
        }
    }
    public void processesPlanningWithoutBlocking() {

        System.out.println("=============================================\n");

        int index = 0;
        int time;
        allTime = 0;

        while (!processesListOne.isEmpty()) {

            int currentQuantumTime = quantumTime;
            Process process = processesListOne.get(index);
            if (process.isAwaitingIO()) {
                System.out.println(String.format("Process: %d is waiting for an I|O device\n", process.getPID()));
                allTime += process.getTimeout();
            }
            if (process.getPerformTime() >= currentQuantumTime) {
                System.out.println(String.format("Process: %d has a : %d ms", process.getPID(), currentQuantumTime));
                time = currentQuantumTime;
                process.setExecutionTime(currentQuantumTime);
            } else {
                System.out.println(String.format("Process: %d completed in %d ms", process.getPID(), process.getPerformTime()));
                time = process.getPerformTime();
                process.performProcess();
            }

            allTime += time;
            currentQuantumTime -= time;
            System.out.println(String.format("Quantum size: %d ms\n", currentQuantumTime));
            if (processesListOne.get(index).isCompleted()) {
                processesListOne.remove(index);
                index--;
            }
            index++;
            if (index >= processesListOne.size()) {
                index = 0;
            }
        }
        System.out.println(String.format("Total time: %d ms", allTime));
    }
    public void processesPlanningWithBlocking() {
        System.out.println("=============================================\n");
        int index = 0;
        int time;
        allTime = 0;
        while (!processesListTwo.isEmpty()) {
            if (index >= processesListTwo.size()) {
                index = 0;
                if(listOfBlockedProcesses.size() == processesListTwo.size()) {
                    System.out.println("All processes are waiting for an I/O device");
                    int waitingTime = maxWaitingTime;
                    for (Process blockedProcess : listOfBlockedProcesses) {
                        waitingTime = Integer.min(waitingTime, blockedProcess.getTimeout());
                    }
                    for (Process blockedProcess: listOfBlockedProcesses) {
                        System.out.println(String.format("Process: %d is waiting for an I|O device", blockedProcess.getPID()));
                        if (blockedProcess.getTimeout() <= waitingTime) {
                            blockedProcess.performWait();
                        } else {
                            blockedProcess.setTimeout(waitingTime);
                        }
                    }
                    for(int i = 0; i < listOfBlockedProcesses.size(); i++) {
                        if (!listOfBlockedProcesses.get(i).isAwaitingIO()) {
                            listOfBlockedProcesses.remove(i);
                        }
                    }
                    allTime += waitingTime;
                    System.out.println(String.format("Waiting time: %d ms\n", waitingTime));
                }
            }
            int currentQuantumTime = quantumTime;
            Process process = processesListTwo.get(index);
            if (listOfBlockedProcesses.contains(process)) {
                index++;
                continue;
            }
            if (process.isAwaitingIO()) {
                System.out.println(String.format("Process: %d was blocked, because it is waiting for I|O\n", process.getPID()));
                listOfBlockedProcesses.add(process);
                index++;
                continue;
            }
            if (process.getPerformTime() >= currentQuantumTime) {
                System.out.println(String.format("Process: %d has a runtime: %d ms", process.getPID(), currentQuantumTime));
                time = currentQuantumTime;
                process.setExecutionTime(currentQuantumTime);
            } else {
                System.out.println(String.format("Process: %d completed in %d ms", process.getPID(), process.getPerformTime()));
                time = process.getPerformTime();
                process.performProcess();
            }
            allTime += time;
            currentQuantumTime -= time;

            System.out.println(String.format("Quantum size: %d ms\n", currentQuantumTime));

            for (Process blockedProcess: listOfBlockedProcesses) {
                System.out.println(String.format("Process: %d is waiting for an I|O device\n", blockedProcess.getPID()));
            }
            for(int i = 0; i < listOfBlockedProcesses.size(); i++) {
                if (!listOfBlockedProcesses.get(i).isAwaitingIO()) {
                    listOfBlockedProcesses.remove(i);
                }
            }
            if (processesListTwo.get(index).isCompleted()) {
                processesListTwo.remove(index);
                index--;
            }

            index++;
        }
        System.out.println(String.format("Total time: %d ms", allTime));
        System.out.println("=============================================\n");
    }
}