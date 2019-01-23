import java.util.concurrent.Semaphore;

public class ElemSemaphore {
    private int readCount;
    private Value val;
    private Semaphore sem;

    public ElemSemaphore() {
        this.readCount = 0;
        sem = new Semaphore(1);
    }

    public void incReadCount() {
        this.readCount++;
    }

    public void startRead() throws InterruptedException {
        sem.acquire();
    }

    public void stopRead() {
        sem.release();
    }

    public void setVal(Value val) {
        this.val = val;
    }

    public Value getVal() {
        return val;
    }
}
