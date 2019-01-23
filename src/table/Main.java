package table;

public class Main {

    static TableSemaphore a;

    private static class TestThread implements Runnable {
        public void run() {
            int[] colCheck = {4, 2, 0, 9, 5, 6, 4, 2, 9, 3};
            int[] rowCheck = {3, 0, 1, 7, 5, 3, 3, 0, 7, 3};

                for(int i = 0; i < colCheck.length; i++) {
                    try {
                        a.readElem(colCheck[i], rowCheck[i]);
                        Thread.sleep(250);
                    } catch(InterruptedException e) {}
                }
        }
    }

    public static void main(String[] args) {
        a = new TableSemaphore(7, 9);
        Thread thread1 = new Thread(new TestThread());
        Thread thread2 = new Thread(new TestThread());
        Thread thread3 = new Thread(new TestThread());

        thread1.start();
        thread2.start();
        thread3.start();

        for(int i = 0; i < 7; i++) {
            for(int j = 0; j < 9; j++) {
                System.out.println("readCount[" + i + "][" + j + "] = " + a.getElemReadCount(i, j));
            }
        }
    }
}