function [x, counter] = LUsolveSLAU (A, b, L, U, P) % возвращает решение СЛАУ A * x = bс помощью известного LUP - разложения
counter = 0;        %счетчик произведенных арифметических операций
x = P * b;      %переставляем элементы в векторе
%преобразования с помощью L -- прямой ход
for k = 1 : size(b) - 1
    for i = k + 1 : size(b)
        x(i) = x(i) - x(k) * L(i, k);
        counter = counter + 2;
    end
end
%получаем единицы на диагонали
x = x ./ diag(U); 
counter = counter + 1;
%обратный ход
for j = size(b): -1 :2
    for i = j-1:-1:1
        if U(i, j) ~= 0
            x(i) = x(i) - x(j) * U(i, j) / U(i, i);
            counter = counter + 3;
        end
    end
end