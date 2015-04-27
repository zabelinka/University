function [x, counter] = myLUsolveSLAU (A, b)

[L, U, P] = myLU(A);
counter = 0;
%переставляем элементы в векторе
x = P*b;
%преобразования с помощью L -- прямой ход
for k = 1:size(b)-1
    for i = k+1:size(b)
        x(i) = x(i) - x(k)*L(i, k);
        counter = counter + 1;
    end
end
%получаем единицы на диагонали
x = x./diag(U); 
counter = counter + 1;
%обратный ход
for k = size(b):-1:2
    for i = k-1:-1:1
        if U(i, k)~= 0
            x(i) = x(i) - x(k)*U(i, k)/U(i, i);
            counter = counter + 1;
        end
    end
end
%disp(' При решении линейной системы произведено операций ')
%disp(counter)