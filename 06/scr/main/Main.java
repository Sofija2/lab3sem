import functions.*; 

public class Main {

    public static void main(String[] args) throws Exception {

        TabulatedFunction af1 = new ArrayTabulatedFunction(-3, 3, new double[]{9, 4, 1, 0, 1, 4, 9});    
        TabulatedFunction llf1 = new LinkedListTabulatedFunction(-3, 3, new double[]{9, 4, 1, 0, 1, 4, 9});    
        //toString
        System.out.println("toString() for ArrayTabulatedFunction: " + af1);
        System.out.println("toString() for LinkedListTabulatedFunction: " + llf1);


        //equals
        ArrayTabulatedFunction af2 = new ArrayTabulatedFunction(-3, 3, new double[]{9, 4, 1, 0, 1, 4, 9});
        LinkedListTabulatedFunction llf2 = new LinkedListTabulatedFunction(-3, 3, new double[]{9, 4, 1, 0, 1, 4, 9});
        ArrayTabulatedFunction af3 = new ArrayTabulatedFunction(-1, 1, new double[]{-1,0,1});

        System.out.println("\nequals() for ArrayTabulated: " + af1.equals(af2)); // true
        System.out.println("equals() fpr LinkedList: " + llf1.equals(af1)); // true
        System.out.println("equals() for  ArrayTabulated: " + af1.equals(af3)); // false


        //hashCode
        System.out.println("\nhashCode() for af1: " + af1.hashCode());
        System.out.println("hashCode() for llf1: " + llf1.hashCode());
        System.out.println("hashCode() for af2 (af1): " + af2.hashCode());
        System.out.println("hashCode() for llf2 (llf1): " + llf2.hashCode());

        // Изменение координаты 
        af1.setPointY(2, 1.001);
        System.out.println("\nhashCode() change " + af1.hashCode());
         af1.setPointY(2, 1);


        //clone
        ArrayTabulatedFunction afClone = (ArrayTabulatedFunction) af1.clone();
        LinkedListTabulatedFunction llfClone = (LinkedListTabulatedFunction) llf1.clone();

        System.out.println("\nChecking clone() for ArrayTabulatedFunction:");
        System.out.println("original: " + af1);
        System.out.println("clone: " + afClone);
        af1.setPointY(0, 9.001);
        System.out.println("change original: " + af1);
        System.out.println("change clone: " + afClone); 


        System.out.println("\nChecking clone() fo LinkedListTabulatedFunction:");
        System.out.println("original: " + llf1);
        System.out.println("clone: " + llfClone);
        llf1.setPointY(1, 1.0001);
        System.out.println("original after change: " + llf1);
        System.out.println("clone after change: " + llfClone); 

    }
}
