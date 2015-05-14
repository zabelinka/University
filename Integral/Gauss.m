function Integral = Gauss(a, b)
%a = 2.1; 
%b = 3.3;
n = 3; % трехточечная

disp('узлы квадратурной формулы (3 шт.)')
x = [a, ((b - a) / 2), b]

disp('моменты весовой функции (2n шт.)');
u = zeros(6, 1);
yu = @(x) (1) ./ ((x - 2.1) .^ 0.4);
u(1) = integral(yu, a, b);
yu = @(x) (x) ./ ((x - 2.1) .^ 0.4);
u(2) = integral(yu, a, b);
yu = @(x) (x.^2) ./ ((x - 2.1) .^ 0.4);
u(3) = integral(yu, a, b);
yu = @(x) (x.^3) ./ ((x - 2.1) .^ 0.4);
u(4) = integral(yu, a, b);
yu = @(x) (x.^4) ./ ((x - 2.1) .^ 0.4);
u(5) = integral(yu, a, b);
yu = @(x) (x.^5) ./ ((x - 2.1) .^ 0.4);
u(6) = integral(yu, a, b);
disp(u)

 %ищем коэффициенты узлового многочлена
 % матрица для СЛАУ
 matrix = zeros(n, n);
 for i = 1 : n
    matrix(i, :) = u(i : i + n - 1);
 end
 disp('Mатрица для СЛАУ');
 disp(matrix)
 
% делаем LU разложение и решаем систему
[L U P] = myLU(matrix);
polynomCoeff = LUsolveSLAU(matrix, -u(n + 1 : 2 * n), L, U, P);
disp('Коэффициенты узлового полинома');
disp(vpa(polynomCoeff, 13));

% корни узлового полинома методом Ньютона
xn = nodalPolynomRoots(a, b);

% корни узлового полинома по формулам Кардано
%xn = Cardano();

if min(xn) < a | max(xn) > b
    disp('Выход за рамки интервала!!!')
    %return
end

%ищем коэффициенты квадратурной формулы
% новая матрица для СЛАУ
matrix = zeros(n, n);
for s = 0 : n - 1
    matrix(s + 1, :) = xn.^s;
end
disp('Mатрица для СЛАУ');
disp(matrix)

% делаем LU разложение и решаем систему
[L U P] = myLU(matrix);
coeffA = LUsolveSLAU(matrix, u(1 : n), L, U, P);
disp('Коэффициенты A');
disp(coeffA);

Integral = 0;
for i = 1 : n
    Integral = Integral + coeffA(i) * f(xn(i));
end
disp('Integral = ')
disp(vpa(Integral, 13))

