function [x, counter] = LUsolveSLAU (A, b, L, U, P) % ���������� ������� ���� A * x = b� ������� ���������� LUP - ����������
counter = 0;        %������� ������������� �������������� ��������
x = P * b;      %������������ �������� � �������
%�������������� � ������� L -- ������ ���
for k = 1 : size(b) - 1
    for i = k + 1 : size(b)
        x(i) = x(i) - x(k) * L(i, k);
        counter = counter + 2;
    end
end
%�������� ������� �� ���������
x = x ./ diag(U); 
counter = counter + 1;
%�������� ���
for j = size(b): -1 :2
    for i = j-1:-1:1
        if U(i, j) ~= 0
            x(i) = x(i) - x(j) * U(i, j) / U(i, i);
            counter = counter + 3;
        end
    end
end