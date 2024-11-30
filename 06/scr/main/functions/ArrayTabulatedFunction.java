package functions;

import java.io.Serializable;

public class ArrayTabulatedFunction implements TabulatedFunction, Serializable {

    private FunctionPoint[] arr;
    private int countP;
    private static final double MULTI = 2.0;

    public ArrayTabulatedFunction(FunctionPoint[] arr) throws IllegalArgumentException {
        if (arr.length < 2) {
            throw new IllegalArgumentException("Must have at least two points");
        }

        // Проверка упорядоченности точек по абсциссе
        for (int i = 1; i < arr.length; i++) {
            if (arr[i].getX() <= arr[i - 1].getX()) {
                throw new IllegalArgumentException("Points must be sorted by x coordinate");
            }
        }
        // Инициализация списка
        this.arr = new FunctionPoint[arr.length];
        // Копирование точек
        for (int i = 0; i < arr.length; i++) {
            this.arr[i] = new FunctionPoint(arr[i].getX(), arr[i].getY());
        }
        this.countP = arr.length;
    }

    public String toString() {
        String res = "{ ";
        for (int i = 0; i < this.getPointsCount(); i++) {
            res = res + this.getPoint(i).toString() + (i != this.getPointsCount() - 1 ? ", " : " }");
        }
        return res;
    }

    public boolean equals(Object o) {
        if (o instanceof TabulatedFunction) {
            if (o instanceof ArrayTabulatedFunction) {
                if (((ArrayTabulatedFunction) o).getPointsCount() != this.getPointsCount()) {
                    return false;
                }
                for (int i = 0; i < this.getPointsCount(); i++) {
                    if (this.getPointX(i) != ((ArrayTabulatedFunction) o).getPointX(i) || this.getPointY(i) != ((ArrayTabulatedFunction) o).getPointY(i)) {
                        return false;
                    }
                }
                return true;
            } else {
                if (((TabulatedFunction) o).getPointsCount() != this.getPointsCount()) {
                    return false;
                }
                for (int i = 0; i < this.getPointsCount(); i++) {
                    if (!this.getPoint(i).equals(((TabulatedFunction) o).getPoint(i))) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    public int hashCode(){
        int hash = 3;
        for (int i = 0; i<this.getPointsCount();i++){
            hash+=52*i*this.getPoint(i).hashCode();
        }
        hash+=52*this.getPointsCount();
        return hash;
    }

    public Object clone() throws CloneNotSupportedException {
        FunctionPoint[] values = new FunctionPoint[this.getPointsCount()];
        for (int i = 0; i < this.getPointsCount(); i++) {
            values[i] = (FunctionPoint) this.getPoint(i).clone();
        }
        return new ArrayTabulatedFunction(values);
    }

    public ArrayTabulatedFunction(double leftX, double rightX, int pointCount) {
        if (pointCount < 2 || Double.compare(leftX, rightX) == 0 || rightX < leftX) {
            throw new IllegalArgumentException("Illegal argument:Count = " + pointCount + ", leftX = " + leftX + ", rightX = " + rightX);
        }
        this.arr = new FunctionPoint[pointCount + 10];
        this.countP = pointCount;
        double length = (rightX - leftX) / (pointCount - 1.);
        for (int i = 0; i < pointCount; ++i) {
            this.arr[i] = new FunctionPoint(leftX + length * i, 0);
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] arr) {
        if (arr.length == 0 || Double.compare(leftX, rightX) == 0 || rightX < leftX) {
            throw new IllegalArgumentException("Illegal argument: leftX = " + leftX + ", rightX = " + rightX);
        }
        int pointCount = arr.length;
        this.arr = new FunctionPoint[pointCount + 10];
        this.countP = pointCount;
        double length = (rightX - leftX) / (pointCount - 1.);
        for (int i = 0; i < pointCount; ++i) {
            this.arr[i] = new FunctionPoint(leftX + length * i, arr[i]);
        }
    }

    public double getLeftDomainBorder() {
        return this.arr[0].getX();
    }

    public double getRightDomainBorder() {
        return this.arr[this.countP - 1].getX();
    }

    public double getFunctionValue(double x) {
        int i;
        if (this.arr[0].getX() > x || this.arr[this.countP - 1].getX() < x) {
            return Double.NaN;
        }
        for (i = 1; i < this.countP && this.arr[i].getX() < x; ++i) ;
        double leftX = this.arr[i - 1].getX();
        double leftY = this.arr[i - 1].getY();
        double rightX = this.arr[i].getX();
        double rightY = this.arr[i].getY();
        return ((rightY - leftY) * (x - leftX)) / (rightX - leftX) + leftY;
    }

    public int getPointsCount() {
        return this.countP;
    }

    public FunctionPoint getPoint(int index) {
        OutBord(index);
        return this.arr[index];
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        OutBord(index);
        if (index == 0) {
            if (point.getX() < this.arr[index + 1].getX()) {
                arr[index] = new FunctionPoint(point);
            } else {
                throw new InappropriateFunctionPointException("Your point have incorrect value");
            }
        } else if (index == this.countP - 1) {
            if (point.getX() >= this.arr[index - 1].getX()) {
                arr[index] = new FunctionPoint(point);
            } else {
                throw new InappropriateFunctionPointException("Your point have incorrect value");
            }
        } else {
            if (point.getX() >= this.arr[index - 1].getX() && point.getX() <= this.arr[index + 1].getX()) {
                arr[index] = new FunctionPoint(point);
            } else {
                throw new InappropriateFunctionPointException("Your point have incorrect value");
            }
        }
    }

    public double getPointX(int index) {
        OutBord(index);
        return arr[index].getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        OutBord(index);
        setPoint(index, new FunctionPoint(x, arr[index].getY()));
    }

    public double getPointY(int index) {
        OutBord(index);
        return arr[index].getY();
    }

    public void setPointY(int index, double y) {
        OutBord(index);
        getPoint(index).setY(y);
    }

    public void deletePoint(int index) {
        if (this.countP < 3) {
            throw new IllegalStateException("Function must have at least two points");
        }
        OutBord(index);
        this.arr[index] = null;
        for (; index < this.countP; ++index) {
            this.arr[index] = this.arr[index + 1];
        }
        --this.countP;
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException//задание6
    {
        if (countP == arr.length) {
            realloc();
        }
        if (Constants(point.getX())) {
            throw new InappropriateFunctionPointException("Replay count X point");
        }
        int dot = FindPoint(point.getX());
        System.arraycopy(arr, dot, arr, dot + 1, countP - dot);
        arr[dot] = point;
        ++countP;
    }

    private void realloc() {
        FunctionPoint[] buff = new FunctionPoint[(int) ((arr.length == 0 ? 1 : arr.length)
                * MULTI)];
        System.arraycopy(arr, 0, buff, 0, countP);
        arr = buff;
    }

    private boolean Constants(double x) {
        for (int i = 0; i < countP; ++i) {
            if (arr[i].getX() == x) {
                return true;
            }
        }
        return false;
    }

    private int FindPoint(double x) {
        for (int i = countP - 1; i >= 0; --i) {
            if (x > arr[i].getX()) {
                return i + 1;
            }
        }
        return 0;
    }

    private void OutBord(int index) {
        if (index < 0 || index >= countP) {
            throw new FunctionPointIndexOutOfBoundsException("Incorrect index");
        }
    }

}
