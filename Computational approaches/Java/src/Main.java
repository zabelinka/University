import java.util.Scanner;



public class Main {
    public static void main(String[] args){
        //System.out.println("Введите размерность матрицы: ");
        Scanner sc = new Scanner(System.in);

        //задаем конкретными значениями Matrix( 1, 3, 2, 1, 2, 5, 4, 1, 2, 10, 9, 7, 3, 8, 9, 2); Vector(11, 20, 40, 37);
        //  1   3   2   1   |   11
        //  2   5   4   1   |   20
        //  2   10  9   7   |   40
        //  3   8   9   2   |   37
        //решение должно быть (1, 2, 2, 0)
        //Обратная матрица:
        // -21      15      2       -4
        //  10.(3)  -6.(6)  -1      -1.(6)
        //  -0.(3)  -0.(3)  0       0.(3)
        //  -8.(3)   5.(6)  1       -1.(6)

        Matrix B = new Matrix(1, 3, 2, 1, 2, 5, 4, 1, 2, 10, 9, 7, 3, 8, 9, 2);
        //Matrix B = new Matrix(1, 3, 2, 1, 0, 0, 0, 0, 2, 10, 0, 7, 0, 8, 9, 2);
        Vector c = new Vector(11, 20, 40, 37);
        //Vector c = new Vector(11, 20, 40, 37);
        //Matrix B = new Matrix(10, 5, true);   				//10*10 случайными числами от -5 до 5
        //Vector c = new Vector(10, 5, true);
        //B.print();

        //final int N = 5;
        //для Якоби-Зейделя -- матрица с рандомными числами [-100, 100] с диагональным преобладанием или положительно определенная
        //Matrix B = new Matrix(N, 10, true);
        //делаем диагональное преобладание
        /*for(int i = 0; i < B.getSize(); i++){
            double sum = 0;
            for(int j = 0; j < B.getSize(); j++){
                if(i != j){
                    sum += Math.abs(B.getElement(i, j));
                }
            }
            B.setElement(i, i, Math.abs(B.getElement(i, i)) + sum);
        }*/

        //делаем положительную определенность
        //B = B.multiply(B.transpose());
        //B.print();

        //Vector c = new Vector(N, 10, true);

        //Matrix B = new Matrix(10, 3, 2, 1, 2, 10, 4, 1, 2, 10, 20, 7, 3, 8, 9, 30);
        //Vector c = new Vector(11, 20, 40, 37);

        SLAU mySystem = new SLAU(B, c);
        mySystem.printSystem();
        /*mySystem.gauss();				//считаем методом Гаусса, находим решение
        if(mySystem.isJoint) mySystem.control();				//запускаем проверку если матрица не выроджденная

        if(!mySystem.isSingular){
            B.setA_1(mySystem.countA_1());
            System.out.println("Обратная матрица: ");
            B.matrix_1.print();
            System.out.println("Проверка ");
            B.multiply(B.matrix_1).print();
            System.out.println("Число обусловленности  " + B.conditionNumber());
        }*/

        mySystem.qr();

        //mySystem.jacobi();
        //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        //mySystem.seidel();

    }
}
