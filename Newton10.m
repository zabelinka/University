xn = [0.5; 0.5; 1.5; -1.0; -0.5; 1.5; 0.5; -0.5; 1.5; -1.5];
J = setMatrixJ(xn)       %матрица частных производных в точке xn
y = setVectorF(xn)'      %вектор значений функций в точке xn
[L, U, P] = myLU(J);
delta = LUsolveSLAU(J, -y, L, U, P) 
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
   	[L, U, P] = myLU(J);
    delta = LUsolveSLAU(J, -y, L, U, P) 
end
disp('   Решение ')
    disp(xn)
%disp('   Погрешность ')
%disp(delta)