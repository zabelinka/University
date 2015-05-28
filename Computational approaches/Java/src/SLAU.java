import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.Scanner;
import java.util.Random;

public class SLAU {
    Scanner sc = new Scanner(System.in);
    public Matrix A;
    private Matrix copyA;                                //копия исходной матрицы для проверки
    public Vector b;
    private Vector newb;                                //копия исходного вектора для проверки
    private Vector x;                                   //вектор с решением
    private Matrix LU;
    private Matrix pStr;                                //матрица перестановок строк
    private Matrix pClm;                                //матрица перестановок для вектора решений. Учитывает перестановки столбцов
    private int[]numbersChangedStrings;            //запоминаем номера строк, которые меняем местами
    private int n;                                      //размерность системы
    private int rang;
    private double epsilon = 1E-13;                     //машинный эпсилон. потом домножим на норму матрицы А
    public boolean isSingular = false;					//вырожденость матрицы
    public boolean isJoint = true;                     //совместность системы
    private boolean changeCount = false;			    //сколько раз поменяли строки (для det): false - не домножаем определительна (-1), true - домножаем на (-1)

    public SLAU(){								//Создание системы с конкретными значениями
        System.out.println("Введите размерность матрицы: ");
        this.n = sc.nextInt();
        System.out.println("Введите коэффициенты матрицы: ");
        double arg[] = new double[n * n];				//создаем массив аргументов для создания матрицы
        for(int i = 0; i < n; i++){
            arg[i] = sc.nextDouble();
        }
        this.copyA = new Matrix(arg);
        System.out.println("Введите коэффициенты вектора: ");
        arg = new double[n];				//создаем массив аргументов для создания вектора
        for(int i = 0; i < n; i++){
            arg[i] = sc.nextDouble();
        }
        this.b = new Vector(arg);

        this.LU = new Matrix(n, 0);				//Создаем объект для матрицы разложения - нулевая матрица
        this.pClm = new Matrix(n, 1.0);
        this.pStr = new Matrix(n, 1.0);
        this.A = new Matrix(this.copyA);				//копируем исходную матрицу для дальнейшей проверки
        this.newb = new Vector(this.b);				//копируем исходный вектор для дальнейшей проверки
        this.x = new Vector(this.n, 0.0);           //будущий вектор решений
        this.epsilon *= this.copyA.getNorm();
    }
    public SLAU(int n){							//Создание матрицы n*n и вектора и заполнение их с клавиатуры
        this.n = n;
        this.copyA = new Matrix(n);
        this.b = new Vector(n);					//Создаем вектор b
        this.LU = new Matrix(n, 0);				//Создаем объект для матрицы разложения - нулевая матрица
        this.pClm = new Matrix(n, 1.0);
        this.pStr = new Matrix(n, 1.0);
        this.A = new Matrix(this.copyA);				//копируем исходную матрицу для дальнейшей проверки
        this.newb = new Vector(this.b);				//копируем исходный вектор для дальнейшей проверки
        this.x = new Vector(this.n, 0.0);           //будущий вектор решений
        this.epsilon *= this.copyA.getNorm();
    }
    public SLAU(int n, int d){					//Создание матрицы n*n и  вектора и заполнение их случайными числами в диапазоне [-d, d]
        this.n = n;
        this.copyA = new Matrix(n, d);
        this.b = new Vector(n, d);
        this.LU = new Matrix(n, 0);				//Создаем объект для матрицы разложения - нулевая матрица
        this.pClm = new Matrix(n, 1.0);
        this.pStr = new Matrix(n, 1.0);
        this.A = new Matrix(this.copyA);				//копируем исходную матрицу для дальнейшей проверки
        this.newb = new Vector(this.b);				//копируем исходный вектор для дальнейшей проверки
        this.x = new Vector(this.n, 0.0);           //будущий вектор решений
        this.epsilon *= this.copyA.getNorm();
    }
    public SLAU(Matrix myA, Vector myB){
        this.A = myA;				//копируем исходную матрицу для дальнейшей проверки -- не меняется
        this.copyA = new Matrix(myA);							//копируем А и b для вычислений
        this.b = myB;
        this.n = myA.getSize();
        this.LU = new Matrix(n, 0.0);				//Создаем объект для матрицы разложения - нулевая матрица
        this.pClm = new Matrix(n, 1.0);
        this.pStr = new Matrix(n, 1.0);
        this.newb = new Vector(this.b);				//копируем исходный вектор для дальнейшей проверки
        this.x = new Vector(this.n, 0.0);           //будущий вектор решений
        this.epsilon *= this.copyA.getNorm();
    }



    public void printSystem(){							//Вывод системы: матрица|вектор
        System.out.println("\ncopyA | b ");
        for(int i = 0; i < this.n; i++){
            copyA.printString(i);
            System.out.println(" |\t" + b.getElement(i));
        }
    }

    public void changeSystemStrings(int k, int l){
        if(k == l) return;							//не меняем строку саму на себя
        this.copyA.changeStrings(k, l);
        this.b.changeElements(k, l);
        this.pStr.changeStrings(k, l);
        changeCount = !changeCount;
    }

    public void changeSystemColumn(int k, int l){
        if(k == l) return;							//не меняем столбец сам на себя
        this.copyA.changeColumn(k, l);
        this.pClm.changeColumn(k, l);
        changeCount = !changeCount;
    }

    public void gauss(){								//метод Гаусса
        this.numbersChangedStrings = new int[this.n - 1];   //для каждого r записывает с какой строкой поменяли r-ую
        {				//ПРЯМОЙ ХОД -- обнуляет нижний треугольник: изменяет матрицу, вектор и записывает коэффициенты в LU

            for(int r = 0; r < this.n - 1; r++) {            //r - номер прохода
                //выбор ведущего элемента только по столюцу
                /*this.changeSystemStrings(r, this.copyA.chooseMax(r));			//если на диагонали не максимальный элемент --		 -- меняем строки
                if(this.copyA.getElement(r, r) == 0){							//то есть весь оставшийся столбец нулевой
                    System.out.println("Error! Матрица вырожденная111!!!");
                    this.isSingular = true;
                    this.singularGauss(r);
                    return;
                }*/
                //выбор ведущего элемента по всей матрице
                int maxI = (int) this.copyA.chooseMax(r, r).getElement(0);
                int maxJ = (int) this.copyA.chooseMax(r, r).getElement(1);
                this.changeSystemStrings(r, maxI);            //если на диагонали не максимальный элемент --		 -- меняем строки
                this.numbersChangedStrings[r] = maxI;           //запоминаем, с какой мы поменяли строку
                this.changeSystemColumn(r, maxJ);            //	 -- и столбцы
                if (Math.abs(this.copyA.getElement(r, r)) <= this.epsilon) {                            //то есть далее все нулевое  == 0
                    System.out.println("Error! Матрица вырожденная111!!!");
                    this.rang = r - 1;
                    this.isSingular = true;
                    this.singularGauss(r);
                    return;
                }
                /*
                //вывод матрицы - проверка на выбор ведущего элемента
                this.copyA.print("Выбрали ведущий элемент!!!");
                P.print("Перестановок:");
                System.out.println("");
                */
                //Если на диагонали не 0
                for(int i = r + 1; i < this.n; i++){
                    double k = this.copyA.getElement(i, r) / this.copyA.getElement(r, r);					//коэффициент, на который домножаем строку
                    for(int j = r; j < this.n; j++){
                        this.copyA.setElement(i, j, this.copyA.getElement(i, j) - this.copyA.getElement(r, j) * k);  //записываем новое значение для А
                    }
                    b.setElement(i, b.getElement(i) - b.getElement(r) * k);				//изменяем вектор решений
                    LU.setElement(i, r, k);										//и записываем коэффициент в матрицу L
                }

            }//сделали диагональную матрицу
            //Проверяем последний диагональный элемент. Если 0 => матрица вырожденная
            if(Math.abs(this.copyA.getElement(this.n - 1, this.n - 1)) <= epsilon){
                System.out.println("Error! Матрица вырожденная222!!!");
                this.isSingular = true;
                this.singularGauss(this.n - 1);
                return;
            }
            // -- иначе считаем детерминат - произведение элементов диагонвли * (-1)если надо
            this.copyA.countDet(changeCount);
            //по окончании вычисления треугольной матрицы А записываем значения в U
            for(int i = 0; i < this.n; i++){
                for(int j = i; j < this.n; j++){
                    LU.setElement(i, j, this.copyA.getElement(i, j));
                }
            }
            //получаем единицы на диагонали
            for(int i = 0; i < this.n; i++){
                if(this.copyA.getElement(i,  i) != 1){
                    b.setElement(i, b.getElement(i) / this.copyA.getElement(i, i));			//делим элемент вектора
                    for(int j = i + 1; j < this.n; j++){							//делим элементы строки правее диагонали
                        this.copyA.setElement(i, j, this.copyA.getElement(i, j) / copyA.getElement(i,  i));
                    }
                    this.copyA.setElement(i, i, this.copyA.getElement(i, i) / copyA.getElement(i,  i)); 	//замена самого диагонального элемента
                }
            }
        }//конец прямого хода метода Гаусса

        System.out.println("Система до обратного хода");
        this.printSystem();
        this.LU.print("LU: ");
        this.pStr.multiply(pClm).print("P: ");

        {				//ОБРАТНЫЙ ХОД -- обнуляет верхний треугольник
            for(int j = this.n - 1; j >= 0; j--){									//обратный ход
                for(int i = j - 1; i >= 0; i--){
                    if(this.copyA.getElement(i,  j) != 0){
                        b.setElement(i, b.getElement(i) - this.copyA.getElement(i,  j) * b.getElement(j));
                        this.copyA.setElement(i, j, this.copyA.getElement(i, j) - this.copyA.getElement(i, j));
                    }
                }
            }
        }
        System.out.println("Система после обратного хода");
        this.printSystem();

        x = pClm.multiply(b);                         //в векторе b находится решение с перестановками компонент. чтобы иметь нужный порядок -- домножаем на матрицу перестановок столбцов
        this.x.print("Решение СЛАУ : ");                             //вектор х -- вектор решений с правильном порядке
    }

    public void singularGauss(int r){			//r - первая нулевая строка

        for(int i = 0; i < this.n; i++){        //записываем остатки матрицы в U
            for(int j = i; j < this.n; j++){
                LU.setElement(i, j, this.copyA.getElement(i, j));
            }
        }

        this.printSystem();
        this.rang = r;                      //ранг матрицы
        System.out.println("RANG = " + this.rang);
        //проверяем, все ли элементы в хвосте вектора b равны 0 (совместна ли система)
        for(int i = r; i < this.n; i++){
            if(Math.abs(this.b.getElement(i)) > this.epsilon){
                this.isJoint = false;
                System.out.println("Система несовместна! Решений нет!");
                return;
            }
        }
        //если система совместна
        System.out.println("Система совместна.");
        for(int i = r; i < this.n; i++){
            this.x.setElement(i, (new Random()).nextDouble() * 2 * 10 - 10);           //свободным элементам присваиваем произвольное значение
        }
        this.printSystem();
        this.x.print();     //увидим, что задал рандом

        //получаем единицы на диагонали
        for(int i = 0; i < r; i++){
            if(this.copyA.getElement(i,  i) != 1){
                b.setElement(i, b.getElement(i) / this.copyA.getElement(i,  i));			//делим элемент вектора
                for(int j = i + 1; j < this.n; j++){							//делим элементы строки правее диагонали
                    this.copyA.setElement(i, j, this.copyA.getElement(i, j) / copyA.getElement(i,  i));
                }
                this.copyA.setElement(i, i, this.copyA.getElement(i, i) / copyA.getElement(i,  i)); 	//замена самого диагонального элемента
            }
        }
        System.out.println("Получили единицы на диагонали");
        this.printSystem();

        {				//ОБРАТНЫЙ ХОД -- обнуляет верхний треугольник
            for(int l = r - 1; l >= 0; l--){
                for(int i = l - 1; i >= 0; i--){
                    if(copyA.getElement(i, l) != 0) {
                        double k = copyA.getElement(i, l) / copyA.getElement(l, l);
                        for (int j = l; j < this.n; j++) {
                            copyA.setElement(i, j, copyA.getElement(i, j) - copyA.getElement(l, j) * k);
                        }
                        b.setElement(i, b.getElement(i) - b.getElement(l) * k );
                    }
                }
            }


        }
        System.out.println("После обратного хода!");
        this.printSystem();

        for(int i = 0; i < r; i++){                                 //из вектора b вычитаем свободные члены * на коэффициент из матрицы
            for(int j = r; j < this.n; j++){
                this.b.setElement(i, this.b.getElement(i) - this.x.getElement(j) * this.copyA.getElement(i, j));
            }
        }
        System.out.println("из вектора b вычли свободные члены * на коэффициент из матрицы");
        this.printSystem();


        for(int i = 0; i < r; i++){                     //получаем остальные решения
            this.x.setElement(i, b.getElement(i));
        }
        x = pClm.multiply(x);                         //в векторе b находится решение с перестановками компонент. чтобы иметь нужный порядок -- домножаем на матрицу перестановок столбцов
        this.x.print("Решение СЛАУ: ");                             //вектор х -- вектор решений в правильном порядке

    }

    public Matrix countA_1(){               //находит обратную к исходной матрице
        if(this.isSingular) {
            System.out.println("Обратная матрица не существует!");
            return null;
        }
        Matrix A_1 = new Matrix(this.n, 0.0);
        for(int i = 0; i < this.n; i++){
            //создаем вектор: нулевой и с единицей на i-ом месте
            Vector temp = new Vector(this.n, 0.0);
            temp.setElement(i, 1.0);
            //считаем решение и записываем его в i-ый столбец обратной матрицы
            A_1.setColumn(i, this.getSolution(temp));
        }
        return A_1;
    }

    public Vector getSolution(Vector v){        //получаем решение системы с исходной матрицей и новым ветором v


    /*преобразование вектора v как будто с матрицей -- по LU разложению*/
        for(int i = 0; i < this.n - 1; i++){
            v.changeElements(i, numbersChangedStrings[i]);
            for(int j = i + 1; j < this.n; j++){
                v.setElement(j, v.getElement(j) - v.getElement(i) * LU.getElement(j, i));
            }
        }//преобразовали матрицу к треугольной
        //v.print("после треугольника: ");
        for(int i = 0 ; i < this.n; i++){
            v.setElement(i, v.getElement(i) / LU.getElement(i, i));
        }//поделили на элементы диагонали -- теперь на диагонали матрицы единицы
        //v.print("привели к 1: ");
        for(int j = this.n - 1; j >= 0; j--){//обратный ход
            for(int i = j - 1; i >= 0; i--){
                if(this.LU.getElement(i,  j) != 0){
                    v.setElement(i, v.getElement(i) - v.getElement(j) * LU.getElement(i, j) / LU.getElement(i, i) );
                }
            }
        }
        return pClm.multiply(v);
    }

    //проверка
    public void control(){
            Matrix newL = new Matrix(n, 0.0);
            Matrix newU = new Matrix(n, 0.0);

            this.LU.divideLU(newL, newU);					//делим матрицу LU на две
            this.pStr.multiply(pClm).multiply(this.A).print("P * A = ");					//считаем произведение L*U и выводим


            newL.multiply(newU).print("L * U = ");					//считаем произведение L*U и выводим

            this.A.multiply(this.x).print("Произведение A * x = ");

            this.A.multiply(x).subtraction(this.newb).print("Отличие ");

            /*if(!this.isSingular){
                this.copyA.matrix_1.print("Обратная матрица: ");
                this.A.multiply(this.copyA.matrix_1).print("Проверка: ");
            }*/
    }



    public void qr(){       //считает QR-разложение

        Matrix Q_1 = new Matrix(this.n, 1.0);       //обратная к матрице Q
        for(int j = 0; j < this.n - 1; j++){
            for(int i = j + 1; i < this.n; i++){
                Q_1 = Q_1.rotation(i, j, this.A);   //получаем очередную Q_1
                //Q_1.print("Q_1[" + i + "][" + j + "] = ");
            }
        }
        Matrix R = Q_1.multiply(this.A);            //конечная матрица R
        R.print("R = ");
        Matrix Q = Q_1.transpose();                 //конечная матрица Q, транспонированная Q_1
        Q.print("Q = ");
        Q.multiply(Q_1).print("Q*QT = ");
        Q.multiply(R).print("Q * R = ");            //проверка. должно быть равно this.A

        //теперь ищем решение системы       Q*y = b;    R*x = y;
        Vector y = Q_1.multiply(this.b);
        y.print("y = ");
        //получаем единицы на диагонали
        for(int i = 0 ; i < this.n; i++){
            y.setElement(i, y.getElement(i) / R.getElement(i, i));
            for(int j = i + 1; j < this.n; j++){
                R.setElement(i, j, R.getElement(i, j) / R.getElement(i, i));
            }
            R.setElement(i, i, R.getElement(i, i) / R.getElement(i, i));
        }//поделили на элементы диагонали -- теперь на диагонали матрицы единицы
        y.print("88888888888888888888888888888888888888888888");
        for(int j = this.n - 1; j >= 0; j--){//обратный ход
            for(int i = j - 1; i >= 0; i--){
                if(R.getElement(i,  j) != 0){
                    y.setElement(i, y.getElement(i) - y.getElement(j) * R.getElement(i, j));
                }
            }
        }
        y.print("Решение: ");
    }

    public void jacobi(){
        //разложеие матрицы А на D + R
        Matrix R = new Matrix(this.A);          //матрица А только с нулевой диагональю
        for(int i = 0; i < this.n; i++){
            R.setElement(i, i, 0.0);
        }
        Matrix D_1 = new Matrix(this.n, 1.0);   //на диагонали: 1 / (элемент диагонали матрицы А)
        for(int i = 0; i < this.n; i++){
            D_1.setElement(i, i, 1 / this.A.getElement(i, i));
        }
        //выводим начальное приближение - векстор c
        D_1.multiply(this.b).print("x0 = ");
        Vector xk_1 = approxJacobi(1, D_1.multiply(this.b), D_1.multiply(R), D_1.multiply(this.b));     //первое приближение - начинаем вектором c
        Vector xk = approxJacobi(2, xk_1, D_1.multiply(R), D_1.multiply(this.b));                       //второе приближение
        //цикл работает пока не будет достаточно малой разницы между последующими приближениями
        for(int i = 3; xk.subtraction(xk_1).getNorm() > 1E-6; i++){
            xk_1 = xk;
            xk = approxJacobi(i, xk_1, D_1.multiply(R), D_1.multiply(this.b));
        }
        xk.subtraction(xk_1).print("Разность приближений ");
    }

    public Vector approxJacobi(int k, Vector xk_1, Matrix B, Vector c){
        System.out.println("Итерация № " + k);
        c.subtraction(B.multiply(xk_1)).print("x" + k + " = ");
        return c.subtraction(B.multiply(xk_1)) ;
    }


    public void seidel(){
        //разложеие матрицы А на L + D + R
        Matrix LD = new Matrix(this.n, 0.0);          //строго нижний треуголник матрицы А
        for(int i = 0; i < this.n; i++){
            for(int j = 0; j <= i; j++){
                LD.setElement(i, j, this.A.getElement(i, j));
            }
        }
        Matrix R = new Matrix(this.n, 0.0);          //строго верхний треуголник матрицы А
        for(int i = 0; i < this.n; i++){
            for(int j = i + 1; j < this.n; j++){
                R.setElement(i, j, this.A.getElement(i, j));
            }
        }
        SLAU helpCountLD_1 = new SLAU(LD, new Vector(this.n, 1.0));
        helpCountLD_1.gauss();
        LD = helpCountLD_1.countA_1();
        LD.print("LD_1");
        Matrix B = new Matrix(LD.multiply(R));        //B = (L + D)^1 * R
        //выводим начальное приближение - вектор c
        Vector c = new Vector(LD.multiply(this.b));
        c.print("x0 = ");

        Vector xk_1 = approxSeidel(1, c, B, c);     //первое приближение - начинаем вектором c
        Vector xk = approxSeidel(2, xk_1, B, c);                       //второе приближение
        //цикл работает пока не будет достаточно малой разницы между последующими приближениями
        for(int i = 3; xk.subtraction(xk_1).getNorm() > 1E-6; i++){
            xk_1 = xk;
            xk = approxSeidel(i, xk_1, B, c);
            System.out.println("Norm = "+xk.subtraction(xk_1).getNorm());
        }
        xk.subtraction(xk_1).print("Разность приближений ");
    }

    public Vector approxSeidel(int k, Vector xk_1, Matrix B, Vector c){
        System.out.println("Итерация № " + k);
        c.subtraction(B.multiply(xk_1)).print("x" + k + " = ");
        return c.subtraction(B.multiply(xk_1)) ;
    }
}//конец класса














