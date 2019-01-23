package table;

public abstract class Element {
    protected int readCount;
    protected Value val;

    public void incReadCount() {
        this.readCount++;
    }

    public void setVal(Value val) {
        this.val = val;
    }

    public Value getVal() {
        return val;
    }

    public abstract void startRead() throws InterruptedException;
    public abstract void stopRead();
}
