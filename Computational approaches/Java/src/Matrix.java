import java.util.Scanner;
import java.util.Random;

public class Matrix {
    Scanner sc = new Scanner(System.in);
    private double matrix[][];
    private int n;
    public double det;
    public Matrix matrix_1;



    public Matrix(int n){							//Ввод значений матрицы с клавиатуры
        this.matrix = new double[n][n];
        this.n = n;
        System.out.println("Введите коэффициенты матрицы A: ");		//Заполняем матрицу
        for(int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                this.matrix[i][j] = sc.nextDouble();
            }
        }
    }
    public Matrix(int n, double e){					//диагональная матрица с e на диагонали
        this.n = n;
        this.matrix = new double[n][n];
        for(int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                if(i==j){
                    this.matrix[i][j] = e;
                }
                else{
                    this.matrix[i][j] = 0;
                }
            }
        }
    }
    public Matrix(double...ds ){					//с заданными значениями
        this.n = (int)Math.sqrt(ds.length);
        this.matrix = new double[n][n];
        int r = 0;									//по каждому аргументу
        for(int i = 0; i < n; i++){					//по каждой строке
            for(int j = 0; j < n; j++){				//по столбцу
                this.matrix[i][j] = ds[r];
                r++;
            }
        }
    }
    public Matrix(int n, int d, boolean isRandom){					//заполнение случайными числами в диапазоне [-d, d]
        this.n = n;
        this.matrix = new double[n][n];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                this.matrix[i][j] = (new Random()).nextDouble() * 2 * d - d;    // изменить на = rand.nextDouble() * 2 * d - d;
            }
        }
    }
    public Matrix(Matrix another){					//создание копии
        this.n = another.getSize();
        this.matrix = new double[n][n];
        for(int i = 0; i < n; i++){
            System.arraycopy(another.matrix[i], 0, this.matrix[i], 0, n);
        }
    }
    public int getSize(){							//Возвращает размерность матрицы
        return this.n;
    }
    public double getElement(int i, int j){			//Возвращает [i][j] элемент
        return this.matrix[i][j];
    }
    public void setElement(int i, int j, double value){		//Устанавливает новое значение элементу матрицы
        this.matrix[i][j] = value;
    }

    public double countDet(boolean changeCount){
        //!!!!!!!!!------------Считаем детерминант - произведение диагональных элементов
        this.det = 1;
        for(int i = 0; i < this.n; i++){
            this.det *= this.matrix[i][i];
        }
        if(changeCount) this.det = - this.det;          //домножаем на (-1), если нечетное число перестановок строк и столбцов
        System.out.println("Определитель матрицы: " + this.det);
        return this.det;
    }

    public void print(){								//Выводит матрицу на экран
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                System.out.print(this.matrix[i][j] + "\t");
            }
            System.out.println();
        }
    }
    public void print(String message){								//Выводит матрицу на экран
        System.out.println(message);
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                System.out.print(this.matrix[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public void printString(int i){					//печать определенной строки матрицы
        for(int j = 0; j < n; j++){
            System.out.print(this.matrix[i][j] + "\t");
        }
    }

    public void setColumn(int j, Vector column){                //устанавливает переданный вектор в качестве j-ого столбца матрицы
        for(int i = 0; i < this.n; i++){
            this.matrix[i][j] = column.getElement(i);
        }
    }

    public void changeStrings(int k, int l){		//changes strings with numbers k & l
        double temp;
        for(int j = 0; j < this.n; j++){
            temp = this.matrix[k][j];
            this.matrix[k][j] = this.matrix[l][j];
            this.matrix[l][j] = temp;
        }
        System.out.println("Смена строк " + (k) + " и " + (l));
    }

    public void changeColumn(int k, int l){
        double temp;
        for(int i = 0; i < this.n; i++){
            temp = this.matrix[i][k];
            this.matrix[i][k] = this.matrix[i][l];
            this.matrix[i][l] = temp;
        }
        System.out.println("Смена столбцов " + (k) + " и " + (l));
    }

    public Matrix multiply(Matrix b){				//перемножение матриц: this * b
        Matrix result = new Matrix(n, 0.0);
        if(this.n != b.n){
            System.out.println("Такие матрицы перемножать нельзя! Проверьте размерности.");
            return null;
        }
        else{
            for(int r = 0; r < n; r++){
                for(int i = 0; i < n; i++){
                    for(int j = 0; j < n; j++){
                        result.matrix[i][j] += this.matrix[i][r] * b.matrix[r][j];
                    }
                }
            }
            return result;
        }
    }
    public Vector multiply(Vector x){
        Vector result = new Vector(n, 0.0);
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                result.setElement(i, result.getElement(i) + this.matrix[i][j] * x.getElement(j));
            }
        }
        return result;
    }

    public int chooseMax(int k){					//выбирает ведущий элемент в текущем  k-ом столбце
        int maxPosition = k;						//строка, в которой находится максимальный в столбце элемент
        for(int i = k; i < this.n; i++){
            if(Math.abs(this.matrix[i][k]) > Math.abs(this.matrix[maxPosition][k]) ){			//если проходим через элемент больше максимального, заменяем строку с максимальным элементом на текущую
                maxPosition = i;
            }
        }
        return maxPosition;							//возвращает номер строки, с которой нужно поменять текущцую, чтобы на диагонали был максимальный элемент
    }
    public Vector chooseMax(int str, int clm){                         //выбирает ведущий элемент во всей матрице ниже элемента[str][clm]
        Vector maxPosition = new Vector ((double)str, (double)clm);                 //расположенае ведущего элемента
        for(int i = str; i < this.n; i++){
            for(int j = clm; j < this.n; j++){
                //если проходим через элемент больше максимального, заменяем позицию максимального элемента на текущую
                if(Math.abs(this.matrix[i][j]) > Math.abs(this.matrix[(int)maxPosition.getElement(0)][(int)maxPosition.getElement(1)])){
                    maxPosition.setElement(0, i);
                    maxPosition.setElement(1, j);
                }
            }
        }
        System.out.println(this.getElement((int)maxPosition.getElement(0), (int)maxPosition.getElement(1)) );
        return maxPosition;
    }

    public void divideLU(Matrix L, Matrix U){				//разделяет матрицу на две: нижне и верхнетреугольную: L и U
        for(int i = 0; i < this.n; i++){					//заполняем новые матрицы L и U
            System.arraycopy(this.matrix[i], i, U.matrix[i], i, this.n - i);
        }
        for(int i = 0; i < this.n; i++){
            System.arraycopy(this.matrix[i], 0, L.matrix[i], 0, i);
            L.matrix[i][i] = 1;
        }
    }

    public void setA_1(Matrix m){
        this.matrix_1 = new Matrix(m);
    }

    public double getNorm(){                                //считает норму бесконечность матрицы -- максимальная сумма модулей элементов в строке
        double max = 0;
        double temp = 0;
        for(int i = 0; i < this.n; i++){
            for(int j = 0; j < this.n; j++){
                temp += Math.abs(this.matrix[i][j]);
            }
            if (temp > max) {
                max = temp;
            }
            temp = 0;
        }
        return max;
    }

    public double conditionNumber(){
        return this.getNorm() * this.matrix_1.getNorm();
    }   //число обусловленности

    public Matrix transpose(){
        Matrix temp = new Matrix(this.n, 0.0);
        for(int i = 0; i < this.n; i++){
            for(int j = 0; j < this.n; j++){
                temp.matrix[i][j] = this.matrix[j][i];
            }
        }
        return temp;
    }

    public double multiply(int i, int j, Matrix b){
        double result = 0;
        for(int r = 0; r < this.n; r++){
            result += this.matrix[i][r] * b.matrix[r][j];
        }
        return result;
    }

    public Matrix rotation(int i, int j, Matrix A){     //метод вращений -- нахождение матрицы Q для QR - разложения. [i][j] -- номер итерации (какую строку с какой складываем), А -- исходная матрица
        double ajj = this.multiply(j, j, A);            //вычисляем только необходимые коэффициенты матрицы Q_1 * A
        double aij = this.multiply(i, j, A);
                                         //косинус и синус псевдоугла вращения
        double c = ajj/Math.sqrt(Math.pow(aij, 2) + Math.pow(ajj, 2));
        double s = aij/Math.sqrt(Math.pow(aij, 2) + Math.pow(ajj, 2));
       // System.out.println("\nc[" + i + "][" + j + "] = " + c + "   s[" + i + "][" + j + "] = " + s);
        Matrix M = new Matrix(this.n, 1.0);             //матрица поворота
        M.setElement(i, i, c);                          //задаем ей значения
        M.setElement(j, j, c);
        M.setElement(i, j, -s);
        M.setElement(j, i, s);
        return M.multiply(this);                        //возвращаем Q_1 на данной итерации
    }

    public Matrix summ(Matrix b){
        Matrix result = new Matrix(this.n, 0.0);
        for(int i = 0; i < this.n; i++){
            for(int j = 0; j < this.n; j++){
                result.matrix[i][j] = this.matrix[i][j] + b.matrix[i][j];
            }
        }
        return result;
    }
}//конец класса
