package table;

import java.util.concurrent.Semaphore;

public class ElemSemaphore extends Element {
    private Semaphore sem;

    public ElemSemaphore() {
        this.readCount = 0;
        sem = new Semaphore(1);
    }

    public void startRead() throws InterruptedException {
        sem.acquire();
    }

    public void stopRead() {
        sem.release();
    }
}
