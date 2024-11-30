package functions;

import functions.meta.*;

public class Functions {

    private Functions() {
    }

    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }

    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }

    public static Function power(Function f, double power) {
        return new Power(f, power);
    }

    public static Function sum(Function f1, Function f2) {
        return new Sum(f1, f2);
    }

    public static Function mult(Function f1, Function f2) {
        return new Mult(f1, f2);
    }

    public static Function composition(Function f1, Function f2) {
        return new Composition(f1, f2);
    }

     public static double integrate(Function function, double left, double right, double step) {
        if (left < function.getLeftDomainBorder() || right > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("The integration interval goes beyond the boundaries of the function definition area");
        }
        double integral = 0;
        int numSteps = (int) Math.ceil((right-left)/step);
        double currentX = left;
        for(int i = 0; i < numSteps -1; i++){
            double nextX = currentX + step;
            integral += (function.getFunctionValue(currentX) + function.getFunctionValue(nextX)) * step /2;
            currentX = nextX;
        }
        double lastStep = right - currentX;
        integral += (function.getFunctionValue(currentX) + function.getFunctionValue(right)) * lastStep /2;
        return integral;

    }
}