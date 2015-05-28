function [x, counter] = myLUsolveSLAU (A, b)

[L, U, P] = myLU(A);
counter = 0;
%������������ �������� � �������
x = P*b;
%�������������� � ������� L -- ������ ���
for k = 1:size(b)-1
    for i = k+1:size(b)
        x(i) = x(i) - x(k)*L(i, k);
        counter = counter + 1;
    end
end
%�������� ������� �� ���������
x = x./diag(U); 
counter = counter + 1;
%�������� ���
for k = size(b):-1:2
    for i = k-1:-1:1
        if U(i, k)~= 0
            x(i) = x(i) - x(k)*U(i, k)/U(i, i);
            counter = counter + 1;
        end
    end
end
%disp(' ��� ������� �������� ������� ����������� �������� ')
%disp(counter)