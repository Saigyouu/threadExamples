import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ElemLocks {
    private int readCount;
    private ReentrantLock mutex = new ReentrantLock();
    private Condition con;

    public ElemLocks() {
        this.readCount = 0;
        con = mutex.newCondition();
    }

    public void incReadCount() {
        this.readCount++;
    }

    public void startRead() throws InterruptedException {
        while(!mutex.tryLock())
            con.await();
    }

    /* (Folie 431) Vor dem Signal immer das entsprechende Lock erwerben
     * Sonst könnte das Signal verloren gehen
     * Sonst könnte der Empfänger ewig warten
     */
    public void stopRead() {
        this.con.signal();
        this.mutex.unlock();
    }
}