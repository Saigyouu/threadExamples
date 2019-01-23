import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TableLocks {
    private Elem[][] table;
    private int lines;
    private int columns;
    private ReentrantLock[] mutexArr;
    private Condition[] conArr;

    public TableLocks(int line, int column){
        this.lines = (line < 1) ? 1 : line;
        this.columns = (column < 1) ? 1 : column;
        this.table = new Elem[this.lines][columns];
        // Mutex und Conditions-Array erzeugen
        mutexArr = new ReentrantLock[this.lines];
        conArr = new Condition[this.lines];

        for(int i = 0; i < mutexArr.length; i++) {
            mutexArr[i] = new ReentrantLock();
            conArr[i] = mutexArr[i].newCondition();
            for(int j = 0; j < column; j++) {
                table[i][j] = new Elem(i, j);
            }
        }
    }
    public int lines() {return this.lines;}
    public int columns() {return this.columns;}
    public Elem readElem(int line, int col) throws InterruptedException {
        if (line < 0 || line >= this.lines || col < 0 || col >= this.columns)
            return null;
        table[line][col].startRead();
        this.table[line][col].incReadCount();
        table[line][col].stopRead();
        return this.table[line][col];
    }
    public Elem setElem(Elem e,int line,int col) throws InterruptedException {
        if (line < 0 || line >= this.lines || col < 0 || col >= this.columns)
            return null;
        while(!mutexArr[line].tryLock())
            conArr[line].await();

        table[line][col].startRead();

        Elem current = this.table[line][col];

        // Falls jemand auf die glorreiche Idee kommt, das gleiche reinzuschreiben, obwohl es schon drin steht
        if(current != e) {
            e.startRead();
            this.table[line][col] = e;
            e.stopRead();
        }

        table[line][col].stopRead();
        conArr[line].signal();
        mutexArr[line].unlock();
        return current;
    }
}