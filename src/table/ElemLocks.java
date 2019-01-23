package table;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ElemLocks extends Element {
    private ReentrantLock mutex = new ReentrantLock();
    private Condition con;

    public ElemLocks() {
        this.readCount = 0;
        con = mutex.newCondition();
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