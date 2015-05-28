function Integral = NewtonCotes(a, b)
A = 2.1; 
%b = 3.3;
y = @(x)(4.5 .* cos(7.*x) .* exp((- 2 .*x)./3) + 1.4 .* sin(1.5 .* x) .* exp(- x./3) + 3);

n = 3; % ������������

% ���� ������������ ������� -- ��� ����� � ��������� �� ���������� ����������
% disp('���� ������������ ������� ')
x = [a, (a + b) / 2, b];

% disp('������� ������� �������');
u = zeros(n, 1);
yu = @(x) (1) ./ ((x - A) .^ 0.4);
u(1) = integral(yu, a, b);
yu = @(x) (x) ./ ((x - A) .^ 0.4);
u(2) = integral(yu, a, b);
yu = @(x) (x.^2) ./ ((x - A) .^ 0.4);
u(3) = integral(yu, a, b);
% disp(u)

% ������� ��� ����
matrix = zeros(n, n);
for s = 0 : n - 1
    matrix(s + 1, :) = x.^s;
end
% disp('M������ ��� ����');
% disp(matrix)

% ������ LU ���������� � ������ �������
[L U P] = myLU(matrix);
coeffA = LUsolveSLAU(matrix, u, L, U, P);
% disp('������������ A');
% disp(coeffA);


Integral = 0;

for i = 1 : n
    Integral = Integral + coeffA(i) * y(x(i));
end
% disp('Integral = ');
% disp(vpa(Integral, 13));



