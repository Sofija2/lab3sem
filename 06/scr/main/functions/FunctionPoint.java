package functions;

import java.io.Serializable;

public class FunctionPoint implements Serializable {

    private double x;
    private double y;

    // Конструктор с заданными координатами
    public FunctionPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Конструктор копирования
    public FunctionPoint(FunctionPoint point) {
        this.x = point.x;
        this.y = point.y;
    }

    // Конструктор по умолчанию
    public FunctionPoint() {
        this(0, 0);
    }

    // Геттеры
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // Сеттеры
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String toString() {
        return "(" + x + "; " + y + ")";
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        FunctionPoint that = (FunctionPoint) obj;
        return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0;
    }

    public int hashCode() {
        long lx = Double.doubleToLongBits(this.getX()), ly = Double.doubleToLongBits(this.getY());
        int hash = 9;
        hash = 76 * hash + (int) (lx ^ (lx >>> 32));
        hash = 76 * hash + (int) (ly ^ (ly >>> 32));
        return hash;
    }

    public Object clone() throws CloneNotSupportedException {
        return new FunctionPoint(this);
    }
}
