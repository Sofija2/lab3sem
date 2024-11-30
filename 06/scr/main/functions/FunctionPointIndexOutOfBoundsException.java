package functions;

// Исключение выхода за границы массива точек
public class FunctionPointIndexOutOfBoundsException extends IndexOutOfBoundsException {
    public FunctionPointIndexOutOfBoundsException(String message) {
        super(message);
    }
}