a = 2.1;
b = 3.3;

N = 4;         %����� �������� �� �������
H = (b - a) / N;

n = 3; % ������������

IntegralPrev = 0;
R = -1;
while abs(R) > 1e-12
    b = 2.1;
    k = 1;
    Integral = 0;
    while b < 3.3
        disp('����� ������� = ')
        disp(k)
        a = b;
        b = a + H;
        % �� �������-������
         Integral = Integral + NewtonCotes(a, b)
         m = 3;
        % �� ������
        % Integral = Integral + Gauss(a, b)
        % m = 6;
        k = k + 1;
    end
    H = H / 2;
    % ����������� �� �����
    R = (Integral - IntegralPrev) / (2^m - 1)
    IntegralPrev = Integral;
end
disp(vpa(Integral, 13))



k*H
f(x) / (x - x1)