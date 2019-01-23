package table;

public abstract class Table {
    protected int lines;
    protected int columns;
    protected Element[][] table;

    public Table(int line, int column) {
        this.lines = (line < 1) ? 1 : line;
        this.columns = (column < 1) ? 1 : column;
    }

    public int lines() {return this.lines;}
    public int columns() {return this.columns;}
    public int getElemReadCount(int line, int column) { return table[line][column].getReadCount(); }

    public abstract Element readElem(int line, int col) throws InterruptedException;
    public abstract Element setElem(Element e, int line, int col) throws InterruptedException;

}
