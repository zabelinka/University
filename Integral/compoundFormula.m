a = 2.1;
b = 3.3;

N = 4;         %число кусочков на отрезке
H = (b - a) / N;

n = 3; % трехточечная

IntegralPrev = 0;
R = -1;
while abs(R) > 1e-12
    b = 2.1;
    k = 1;
    Integral = 0;
    while b < 3.3
        disp('номер кусочка = ')
        disp(k)
        a = b;
        b = a + H;
        % по Ньютону-Котесу
         Integral = Integral + NewtonCotes(a, b)
         m = 3;
        % по Гауссу
        % Integral = Integral + Gauss(a, b)
        % m = 6;
        k = k + 1;
    end
    H = H / 2;
    % погрешность по Рунге
    R = (Integral - IntegralPrev) / (2^m - 1)
    IntegralPrev = Integral;
end
disp(vpa(Integral, 13))



k*H
f(x) / (x - x1)