str1 = 'cos(x) + y - 1.5';
str2 = '2*x - sin(y - 0.5) - 1';
f1 = sym(str1)
f2 = sym(str2)
df1x = inline(diff(sym(str1), 'x'))
df1y = inline(diff(sym(str1), 'y'))
df2x = inline(diff(sym(str2), 'x'))
df2y = inline(diff(sym(str2), 'y'))
f1 = inline(f1);
f2 = inline(f2);
drow_plots;
xn = [0.5; 0.8];
yn = [f1(xn(1), xn(2)); f2(xn(1), xn(2))] ; 
J = eye(ndims(yn), ndims(xn))   ;                 %матрица Якоби
J(1, 2) = df1y(xn(2));
J(2, 1) = df2x(xn(1))
[L, U, P] = myLU(J);
delta = LUsolveSLAU(J, -yn, L, U, P);
i = 0;
while abs(delta) > 1e-5
    i = i + 1;
    disp('Итерация номер ')
    disp(i)
    yn = [f1(xn(1), xn(2)); f2(xn(1), xn(2))] ;    %вектор значений функций f(xn)
    J(1, 1) = df1x(xn(1));
    J(2, 2) = df2y(xn(2))
    [L, U, P] = myLU(J);
    delta = LUsolveSLAU(J, -yn, L, U, P)  
    xn = xn + delta
    
end
