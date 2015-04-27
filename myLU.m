function [L, U, P, counter] = myLU(A)
L = zeros(size(A));
U = zeros(size(A));
P = eye(size(A));
% assert(forall(size(A) == size(L)))
counter = 0;
for r = 1:size(A)-1
    APL = [A P L];
    i = r + 1;
    %если на диагонали 0 -- меняем строки
    while APL(r, r) == 0 
        if i>size(APL, 1)
            disp('Матрица вырожденная');
            return;
        end;
        %disp('Меняем строки');
        temp = APL(i, :);
        APL(i, :) = APL(r, :);
        APL(r, :) = temp;
        i = i+1;
    end
    A = APL(:, 1:size(A, 2));
    P = APL(:, [1:size(A, 2)] + size(A, 2));
    L = APL(:, [1:size(A, 2)] + 2 * size(A, 2));
    %делаем верхний треугольник L
    L(r, r) = 1;
    
    L(r, r+1:end) = zeros(1, size(L, 2) - r);
    
    %далее на диагонали A не 0
    for i = r+1:size(A)
        k = A(i, r) / A(r, r);		%коэффициент, на который домножаем строку
        counter = counter+1;        %считаем арифметические операции
        A(i, r:size(A)) = A(i, r:size(A)) - A(r, r:size(A)) * k;  %записываем новое значение для А
        counter = counter + 2 * length(r:size(A)); 
        %записываем коэффициент в матрицу L
        L(i, r) = k;
    end; 
end; %сделали диагональную матрицу
L(end, end) = 1;
U = A;      %матрица U -- остаток матрицы A
end