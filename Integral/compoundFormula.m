a = 2.1;
b = 3.3;

N = 4;         %число кусочков на отрезке

n = 3; % трехточечная

iteration = 0;
Integral = zeros(3, 1);
R = -1;
%алгебраическая степень точности 
 m = 3;            % Ньютона-Котеса
% m = 6;             % Гаусса
while abs(R) > 1e-12
    iteration  = iteration + 1;
    disp('Итерация');
    disp(iteration);
    Integral(1) = 0;
    x1 = a;
    % disp('длина шага');
    H = (b - a) / N;    % шаг = длина кусочка
    for i = 1 : N       % проходим по всем кусочкам отрезка
        % disp('номер кусочка = ');
        % disp(i);
        x2 = a + i * H; % правая граница отрезка
        % считаем на данном кусочке интеграл:
        
        % по Ньютону-Котесу
         Integral(1) = Integral(1) + NewtonCotes(x1, x2);
      
        % по Гауссу
        % Integral(1) = Integral(1) + Gauss(x1, x2); 
        x1 = x2;
    end
    disp('Интеграл');
    disp(vpa(Integral(1), 13));
    % погрешность по Рунге
    R = (Integral(1) - Integral(2)) / (2^m - 1);
    disp('Погрешность');
    disp(R);
    
    % Эйткен
    Eitken = log2((Integral(2) - Integral(3))/(Integral(1) - Integral(2)));
    disp('Скорость по Эйткену')
    disp(Eitken);
    
    Integral(3) = Integral(2);  % предпредыдущий
    Integral(2) = Integral(1);  % предыдущий
    N = N * 2;          % увеличили число кусочков в 2р. = уменьшили шлину шага H на 2
    
    
    
end
disp(vpa(Integral(1), 13))

