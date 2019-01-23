package table;

public class Main {

    static TableSemaphore a;
    static TableLocks b;

    private static class TestThread implements Runnable {
        public void run() {
            int[] colCheck = {4, 2, 0, 6, 5, 6, 4, 2, 6, 3};
            int[] rowCheck = {3, 0, 1, 8, 5, 3, 3, 0, 8, 3};

                for(int i = 0; i < colCheck.length; i++) {
                    try {
                        a.readElem(colCheck[i], rowCheck[i]);
                        b.readElem(colCheck[i], rowCheck[i]);
                        Thread.sleep(5);
                    } catch(InterruptedException e) {}
                }
        }
    }

    public static void main(String[] args) {
        a = new TableSemaphore(7, 9);
        b = new TableLocks(7, 9);
        Thread thread1 = new Thread(new TestThread());
        Thread thread2 = new Thread(new TestThread());
        Thread thread3 = new Thread(new TestThread());

        thread1.run();
        thread2.run();
        thread3.run();

        System.out.println("Tabelle mit Semaphoren");

        for(int i = 0; i < 7; i++) {
            for(int j = 0; j < 9; j++) {
                System.out.print(a.getElemReadCount(i, j) + "\t");
            }
            System.out.println();
        }

        System.out.println("\n Tabelle mit Mutexes");

        for(int i = 0; i < 7; i++) {
            for(int j = 0; j < 9; j++) {
                System.out.print(b.getElemReadCount(i, j) + "\t");
            }
            System.out.println();
        }
    }
}