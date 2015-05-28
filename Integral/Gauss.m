function Integral = Gauss(a, b)
A = 2.1; 
%b = 3.3;

n = 3; % ������������

% disp('������� ������� ������� (2n ��.)');
u = zeros(6, 1);
yu = @(x) (1) ./ ((x - A) .^ 0.4);
u(1) = integral(yu, a, b);
yu = @(x) (x) ./ ((x - A) .^ 0.4);
u(2) = integral(yu, a, b);
yu = @(x) (x.^2) ./ ((x - A) .^ 0.4);
u(3) = integral(yu, a, b);
yu = @(x) (x.^3) ./ ((x - A) .^ 0.4);
u(4) = integral(yu, a, b);
yu = @(x) (x.^4) ./ ((x - A) .^ 0.4);
u(5) = integral(yu, a, b);
yu = @(x) (x.^5) ./ ((x - A) .^ 0.4);
u(6) = integral(yu, a, b);
% disp(u)

 %���� ������������ �������� ����������
 % ������� ��� ����
 matrix = zeros(n, n);
 for i = 1 : n
    matrix(i, :) = u(i : i + n - 1);
 end
 % disp('M������ ��� ���� - ������� ������������ �������� ����������');
 % disp(matrix)
 
% ������ LU ���������� � ������ �������
[L U P] = myLU(matrix);
polynomCoeff = LUsolveSLAU(matrix, -u(n + 1 : 2 * n), L, U, P);
% disp('������������ �������� ��������');
% disp(vpa(polynomCoeff, 13));

% ����� �������� �������� ������� �������
%xn = nodalPolynomRoots_Newton(a, b, polynomCoeff);
xn = roots([1 polynomCoeff(3) polynomCoeff(2) polynomCoeff(1)]);
% ����� �������� �������� �� �������� �������
%xn = nodalPolynomRoots_Cardano();

if a - min(xn) > 0.1 | max(xn) - b > 0.1
    disp('����� �� ����� ���������!!!')
    disp('������� �������');
    disp(a)
    disp(b)
    disp('���������� �����')
    disp(xn)
     return
end

%���� ������������ ������������ �������
% ����� ������� ��� ����
matrix = zeros(n, n);
for s = 0 : n - 1
    matrix(s + 1, :) = xn.^s;
end
% disp('M������ ��� ����');
% disp(matrix)

% ������ LU ���������� � ������ ������� - 
% ���� ������������ ������������ �������
[L U P] = myLU(matrix);
coeffA = LUsolveSLAU(matrix, u(1 : n), L, U, P);
% disp('������������ A');
% disp(coeffA);

Integral = 0;
for i = 1 : n
    Integral = Integral + coeffA(i) * f(xn(i));
end
% disp('Integral = ')
% disp(vpa(Integral, 13))

