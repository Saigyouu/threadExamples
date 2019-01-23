package table;

import java.util.concurrent.Semaphore;

public class TableSemaphore extends Table {
    private Semaphore[] semArr;

    public TableSemaphore(int line, int column){
        super(line, column);
        semArr = new Semaphore[this.lines];
        for(int i = 0; i < semArr.length; i++) {
            semArr[i] = new Semaphore(1);
            for(int j = 0; j < column; j++) {
                table[i][j] = new ElemSemaphore();
            }
        }
    }

    public ElemSemaphore readElem(int line, int col) throws InterruptedException {
        if (line < 0 || line >= this.lines || col < 0 || col >= this.columns)
            return null;
        table[line][col].startRead();
        this.table[line][col].incReadCount();
        table[line][col].stopRead();
        return this.table[line][col];
    }
    public ElemSemaphore setElem(ElemSemaphore e, int line, int col) throws InterruptedException {
        if (line < 0 || line >= this.lines || col < 0 || col >= this.columns)
            return null;
        semArr[line].acquire();
        table[line][col].startRead();
        ElemSemaphore current = this.table[line][col];

        e.startRead();
        this.table[line][col] = e;
        e.stopRead();

        table[line][col].stopRead();
        semArr[line].release();
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

        semArr[line].acquire();
        this.table[line][col].startRead();

        Value current = this.table[line][col].getVal();
        this.table[line][col].setVal(val);

        this.table[line][col].stopRead();
        semArr[line].release();

        return current;
    }
}