package table;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TableLocks extends Table {
    private ReentrantLock[] mutexArr;
    private Condition[] conArr;

    public TableLocks(int line, int column){
        super(line, column);
        this.table = new ElemLocks[this.lines][columns];
        // Mutex und Conditions-Array erzeugen
        mutexArr = new ReentrantLock[this.lines];
        conArr = new Condition[this.lines];

        for(int i = 0; i < mutexArr.length; i++) {
            mutexArr[i] = new ReentrantLock();
            conArr[i] = mutexArr[i].newCondition();
            for(int j = 0; j < column; j++) {
                table[i][j] = new ElemLocks();
            }
        }
    }

    public Element readElem(int line, int col) throws InterruptedException {
        if (line < 0 || line >= this.lines || col < 0 || col >= this.columns)
            return null;
        table[line][col].startRead();
        this.table[line][col].incReadCount();
        table[line][col].stopRead();
        return this.table[line][col];
    }

    public Element setElem(Element e, int line, int col) throws InterruptedException {
        if (line < 0 || line >= this.lines || col < 0 || col >= this.columns)
            return null;
        while(!mutexArr[line].tryLock())
            conArr[line].await();

        table[line][col].startRead();

        Element current = this.table[line][col];

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

    /* Unterschied modify und set?
     * Vielleicht könnt ihr mir ja helfen.. :D
     */
    public Value modifyElem(Value val, int line, int col) throws InterruptedException{
        if (line < 0 || line >= this.lines || col < 0 || col >= this.columns)
            return null;
        if (this.table[line][col] == null)
            return null;

        while(!this.mutexArr[line].tryLock())
            conArr[line].await();

        table[line][col].startRead();

        Value current = this.table[line][col].getVal();
        this.table[line][col].setVal(val);

        table[line][col].stopRead();
        conArr[line].signal();
        mutexArr[line].unlock();
        return current;
    }
}