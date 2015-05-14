function Integral = NewtonCotes(a, b)
%a = 2.1; 
%b = 3.3;
n = 3; % ������������

% ���� ������������ ������� -- ��� ����� � ��������� �� ���������� ����������
disp('���� ������������ ������� ')
x = [a, ((b - a) / 2), b]

disp('������� ������� �������');
u = zeros(n, 1);
yu = @(x) (1) ./ ((x - 2.1) .^ 0.4);
u(1) = integral(yu, a, b);
yu = @(x) (x) ./ ((x - 2.1) .^ 0.4);
u(2) = integral(yu, a, b);
yu = @(x) (x.^2) ./ ((x - 2.1) .^ 0.4);
u(3) = integral(yu, a, b);
disp(u)

% ������� ��� ����
matrix = zeros(n, n);
for s = 0 : n - 1
    matrix(s + 1, :) = x.^s;
end
disp('M������ ��� ����');
disp(matrix)

% ������ LU ���������� � ������ �������
[L U P] = myLU(matrix);
coeffA = LUsolveSLAU(matrix, u, L, U, P);
disp('������������ A');
disp(coeffA);

Integral = 0;
for i = 1 : n
    Integral = Integral + coeffA(i) * f(x(i));
end
disp('Integral = ')
disp(vpa(Integral, 13))
