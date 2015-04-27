function [L, U, P, counter] = myLU(A)
L = zeros(size(A));
U = zeros(size(A));
P = eye(size(A));
% assert(forall(size(A) == size(L)))
counter = 0;
for r = 1:size(A)-1
    APL = [A P L];
    i = r + 1;
    %���� �� ��������� 0 -- ������ ������
    while APL(r, r) == 0 
        if i>size(APL, 1)
            disp('������� �����������');
            return;
        end;
        %disp('������ ������');
        temp = APL(i, :);
        APL(i, :) = APL(r, :);
        APL(r, :) = temp;
        i = i+1;
    end
    A = APL(:, 1:size(A, 2));
    P = APL(:, [1:size(A, 2)] + size(A, 2));
    L = APL(:, [1:size(A, 2)] + 2 * size(A, 2));
    %������ ������� ����������� L
    L(r, r) = 1;
    
    L(r, r+1:end) = zeros(1, size(L, 2) - r);
    
    %����� �� ��������� A �� 0
    for i = r+1:size(A)
        k = A(i, r) / A(r, r);		%�����������, �� ������� ��������� ������
        counter = counter+1;        %������� �������������� ��������
        A(i, r:size(A)) = A(i, r:size(A)) - A(r, r:size(A)) * k;  %���������� ����� �������� ��� �
        counter = counter + 2 * length(r:size(A)); 
        %���������� ����������� � ������� L
        L(i, r) = k;
    end; 
end; %������� ������������ �������
L(end, end) = 1;
U = A;      %������� U -- ������� ������� A
end