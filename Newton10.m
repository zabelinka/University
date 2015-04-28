xn = [0.5; 0.5; 1.5; -1.0; -0.5; 1.5; 0.5; -0.5; 1.5; -1.5];        %начальное приближение
J = setMatrixJ(xn)       %матрица частных производных в точке xn
y = setVectorF(xn)'      %вектор значений функций в точке xn
[L, U, P, counter] = myLU(J);    %делаем разложение, считаем количество произведенных операций
globalCounter = counter;        %количество арифметических операций, произведенных в методе Ньютона
[delta, counter] = LUsolveSLAU(J, -y, L, U, P)  %решаем СЛАУ, считаем операции
globalCounter = globalCounter + counter;        %количество арифметических операций, произведенных в методе Ньютона
i = 0;                   %счетчик номера итерации
while max(abs(delta)) > 1e-5 & i < 100
    xn = xn + delta;        %переход к следующему приближению
    i = i + 1;
    disp('Итерация номер ')
    disp(i)
    disp('   Погрешность ')
    disp(delta')
    disp('   Новое приближение ')
    disp(xn')
    %пересчитываем для нового xn
    J = setMatrixJ(xn);      %матрица частных производных в точке xn
    y = setVectorF(xn)';     %вектор значений функций в точке xn
   	[L, U, P, counter] = myLU(J);
    globalCounter = globalCounter + counter;        %количество арифметических операций, произведенных в методе Ньютона
    [delta, counter] = LUsolveSLAU(J, -y, L, U, P) 
    globalCounter = globalCounter + counter;        %количество арифметических операций, произведенных в методе Ньютона
end
disp('   Решение ')
disp(vpa(xn, 5))
disp('   Погрешность ')
disp(delta)
disp('   Произведено арифметических операций')
disp(globalCounter)