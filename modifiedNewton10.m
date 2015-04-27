xn = [0.5; 0.5; 1.5; -1.0; -0.5; 1.5; 0.5; -0.5; 1.5; -1.5];
J = setMatrixJ(xn)       %������� ������� ����������� � ����� xn
y = setVectorF(xn)'      %������ �������� ������� � ����� xn
[L, U, P] = myLU(J);
delta = LUsolveSLAU(J, -y, L, U, P)   %������� ����� ������������ �������������
i = 0;                   %������� ������ ��������
while max(abs(delta)) > 1e-5 & i < 100
    xn = xn + delta;        %������� � ���������� �����������
    i = i + 1;
    disp('�������� ����� ')
    disp(i)
    disp('   ����������� ')
    disp(delta)
    disp('   ����� ����������� ')
    disp(xn)
    %������������� ��� ������ xn
    y = setVectorF(xn)';     %������ �������� ������� � ����� xn
    delta = LUsolveSLAU(J, -y, L, U, P);   %������� ����� ������������ �������������
end