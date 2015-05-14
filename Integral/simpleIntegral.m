%a = 2.1; b = 3.3; alpha = 2/5; beta = 0; 
%f(x) = 4.5 * cos(7*x) * exp((- 2 *x)/3) + 1.4 * sin(1.5 * x) * exp(- x/3) + 3;
%p(x) = 1 / ((x - a)^alpha * (b - x)^beta)
function Integral = simpleIntegral(string, a, b)
n = 1000;               %число кусочков
h = (b - a) / n;        %шаг = длина кусочка
f = inline(string)
Integral = 0;
for i = 1 : n
    Integral = Integral + f(a + (i - 0.5) * h);
end
Integral = Integral * h;
disp(vpa(Integral, 13));