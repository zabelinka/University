function xn = Cardano(polynomCoeff)

% polynomCoeff - коэффициенты узлового полинома: w = x^3 + a(3)*x^2 + a(2)*x + a(1)
 
% коэффициенты кубческого уравнения
a = 1;
b = polynomCoeff(3);
c = polynomCoeff(2);
d = polynomCoeff(1);

% приведение к канонической форме
p = c / a - b^2 / (3 * a^2);
q = (2 * b^3 - 9 * a * b * c + 27 * a^2 * d) / (27 * a^3);
Q = (p^3) / 27 + (q^2) / 4;
sqrt(Q);

% считаем alfa
alfa = zeros(3, 1);
C = -q / 2 + sqrt(Q);            % комплексное число, из которго будем извлекать корни
r = sqrt(real(C)^2 + imag(C)^2);           % модуль комплексного числа
fi = atan(imag(C) / real(C));              % Угол
for k = 0 : 2
    alfa(k + 1) = r^(1/3) * (cos((fi + 2*pi*k)/3) + 1i*sin((fi + 2*pi*k)/3));
end

% для счета beta
beta = zeros(3, 1);
C = -q / 2 - sqrt(Q);            % комплексное число, из которго будем извлекать корни
r = sqrt(real(C)^2 + imag(C)^2);           % модуль комплексного числа
fi = atan(imag(C) / real(C));              % Угол
for k = 0 : 2
        beta(k + 1) = r^(1/3) * (cos((fi + 2*pi*k)/3) + 1i*sin((fi + 2*pi*k)/3));
end
xn = zeros(3, 1);
for k = 1 : 3
    if real(alfa(1)*beta(k)) == -p / 3
        xn(1) = alfa(1) + beta(k);
        break
    end
end
for k = 1 : 3
    if real(alfa(2)*beta(k)) == -p / 3
        xn(2) = - (alfa(2) + beta(k)) / 2 + 1i*((alfa(2) - beta(k)) * sqrt(3) / 2);
        break
    end
end
for k = 1 : 3
    if real(alfa(3)*beta(k)) == -p / 3
        xn(3) = - (alfa(3) + beta(k)) / 2 - 1i*((alfa(3) - beta(k)) * sqrt(3) / 2);
        break
    end
end
xn(1: 3, 1) = real(xn(1: 3, 1));
t = - b / (3 * a);
xn(:) = xn(:) + t;
disp(vpa(xn, 13))

