xn = [0.5; 0.5; 1.5; -1.0; -0.2; 1.5; 0.5; -0.5; 1.5; -1.5];
J = setMatrixJ(xn)       %матрица частных производных в точке xn
y = setVectorF(xn)'      %вектор значений функций в точке xn
[L, U, P, counter] = myLU(J);    %делаем разложение, считаем количество произведенных операций
globalCounter = counter;        %количество арифметических операций, произведенных в методе Ньютона
[delta, counter] = LUsolveSLAU(J, -y, L, U, P)  %решаем СЛАУ, считаем операции
globalCounter = globalCounter + counter;   
i = 0;                   %счетчик номера итерации
k = 9;                  %после какой итерации переходим к модифицированному методу Ньютона
%обычный метод Ньютона для i < k
while max(abs(delta)) > 1e-5 & i < k
    xn = xn + delta;        %переход к следующему приближению
    i = i + 1;
    disp('Итерация номер ')
    disp(i)
    disp('   Погрешность ')
    disp(delta)
    disp('   Новое приближение ')
    disp(xn)
    %пересчитываем для нового xn
    J = setMatrixJ(xn);      %матрица частных производных в точке xn
    y = setVectorF(xn)';     %вектор значений функций в точке xn
    [L, U, P, counter] = myLU(J);    %делаем разложение, считаем количество произведенных операций
    globalCounter = globalCounter + counter;  
    [delta, counter] = LUsolveSLAU(J, -y, L, U, P)  %решаем СЛАУ, считаем операции
    globalCounter = globalCounter + counter;
end
disp('   Произведено арифметических операций')
disp(globalCounter)
disp('Переход к модифицированному методу Ньютона')
% далее везде используем последнее найденное LUP-разложение
while max(abs(delta)) > 1e-5 & i < 100
    xn = xn + delta;        %переход к следующему приближению
    i = i + 1;
    disp('Итерация номер ')
    disp(i)
    disp('   Погрешность ')
    disp(delta)
    disp('   Новое приближение ')
    disp(xn)
    %пересчитываем для нового xn
    y = setVectorF(xn)';     %вектор значений функций в точке x
    [delta, counter] = LUsolveSLAU(J, -y, L, U, P)  %решаем СЛАУ, считаем операции
    globalCounter = globalCounter + counter;
end
disp('   Решение ')
disp(vpa(xn', 5))
disp('   Погрешность ')
disp(delta')
disp('   Произведено арифметических операций')
disp(globalCounter)
disp('и итераций')
disp(i)
disp('   Проверка ')
y = setVectorF(xn)    