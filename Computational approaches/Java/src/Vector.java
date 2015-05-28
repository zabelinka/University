import java.util.Scanner;
import java.util.Random;

public class Vector {
    Scanner sc = new Scanner(System.in);
    private double vector[];
    private int n;

    public Vector(int n){							//Ввод значений вектора с клавиатуры
        this.vector = new double[n];
        this.n = n;
        System.out.println("Введите коэффициенты вектора: ");		//Заполняем матрицу
        for(int i = 0; i < n; i++){
            this.vector[i] = sc.nextDouble();
        }
    }
    public Vector(int n, double e){					//вектор с одинаковыми значениями e
        this.n = n;
        this.vector = new double[n];
        for(int i = 0; i < n; i++){
            this.vector[i] = e;
        }
    }
    public Vector(double...ds ){					//с заданными значениями
        this.n = (int)(ds.length);
        this.vector = new double[n];
        int r = 0;									//по каждому аргументу
        for(int i = 0; i < n; i++){					//по каждой строке
            this.vector[i] = ds[r];
            r++;
        }
    }
    public Vector(int n, int d, boolean isRandom){					//заполнение случайными числами в диапазоне [-d, d]
        this.n = n;
        this.vector = new double[n];
        for(int i = 0; i < n; i++){
            this.vector[i] = (new Random()).nextDouble() * 2 * d - d;    // изменить на = rand.nextDouble() * 2 * d - d;
        }
    }
    public Vector(Vector an){
        this.n = an.getSize();
        this.vector = new double[n];
        for(int i = 0; i < n; i++){
            this.vector[i] = an.vector[i];
        }
    }

    public int getSize(){							//Возвращает размерность вектора
        return this.n;
    }

    public Vector subtraction(Vector b){				//Вычитание двух векторов
        Vector result = new Vector(this.getSize(), 0.0);
        for(int i = 0; i < this.getSize(); i++){
            result.vector[i] = this.vector[i] - b.vector[i];
        }
        return result;
    }

    public void print(){							//Выводит вектор на экран вертикально
        for(int i = 0; i < n; i++){
            System.out.println(this.vector[i] + "\t");
        }
    }
    public void print(String message){							//Выводит вектор на экран вертикально
        System.out.println(message);
        for(int i = 0; i < n; i++){
            System.out.println(this.vector[i] + "\t");
        }
    }

    public double getElement(int i){
        return this.vector[i];
    }

    public void setElement(int i, double value){
        this.vector[i] = value;
    }

    public void changeElements(int k, int l){		//changes elementss with numbers k & l
        double temp = 0;
        temp = this.vector[k];
        this.vector[k] = this.vector[l];
        this.vector[l] = temp;
        //System.out.println("Смена элементов " + (k + 1) + " и " + (1 + l));
    }

    public int chooseMax(int k){					//выбирает ведущий элемент в текущем  k-ом столбце
        int maxPosition = k;						//строка, в которой находится максимальный в столбце элемент
        for(int i = k; i < this.n; i++){
            if(Math.abs(this.vector[i]) > Math.abs(this.vector[maxPosition]) ){			//если проходим через элемент больше максимального, заменяем строку с максимальным элементом на текущую
                maxPosition = i;
            }
        }
        return maxPosition;							//возвращает номер строки, с которой нужно поменять текущцую, чтобы на диагонали был максимальный элемент
    }

    public double getNorm(){
        double max = Math.abs(this.vector[0]);
        for(int i = 1; i < this.n; i++){
            if(Math.abs(this.vector[i]) > max){
                max = Math.abs(this.vector[i]);
            }
        }
        return max;
    }


}
