xn = [0.5; 0.5; 1.5; -1.0; -0.2; 1.5; 0.5; -0.5; 1.5; -1.5];
J = setMatrixJ(xn)       %������� ������� ����������� � ����� xn
y = setVectorF(xn)'      %������ �������� ������� � ����� xn
[L, U, P, counter] = myLU(J);    %������ ����������, ������� ���������� ������������� ��������
globalCounter = counter;        %���������� �������������� ��������, ������������� � ������ �������
[delta, counter] = LUsolveSLAU(J, -y, L, U, P)  %������ ����, ������� ��������
globalCounter = globalCounter + counter;   
i = 0;                   %������� ������ ��������
k = 9;                  %����� ����� �������� ��������� � ����������������� ������ �������
%������� ����� ������� ��� i < k
while max(abs(delta)) > 1e-5 & i < k
    xn = xn + delta;        %������� � ���������� �����������
    i = i + 1;
    disp('�������� ����� ')
    disp(i)
    disp('   ����������� ')
    disp(delta)
    disp('   ����� ����������� ')
    disp(xn)
    %������������� ��� ������ xn
    J = setMatrixJ(xn);      %������� ������� ����������� � ����� xn
    y = setVectorF(xn)';     %������ �������� ������� � ����� xn
    [L, U, P, counter] = myLU(J);    %������ ����������, ������� ���������� ������������� ��������
    globalCounter = globalCounter + counter;  
    [delta, counter] = LUsolveSLAU(J, -y, L, U, P)  %������ ����, ������� ��������
    globalCounter = globalCounter + counter;
end
disp('   ����������� �������������� ��������')
disp(globalCounter)
disp('������� � ����������������� ������ �������')
% ����� ����� ���������� ��������� ��������� LUP-����������
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
    y = setVectorF(xn)';     %������ �������� ������� � ����� x
    [delta, counter] = LUsolveSLAU(J, -y, L, U, P)  %������ ����, ������� ��������
    globalCounter = globalCounter + counter;
end
disp('   ������� ')
disp(vpa(xn', 5))
disp('   ����������� ')
disp(delta')
disp('   ����������� �������������� ��������')
disp(globalCounter)
disp('� ��������')
disp(i)
disp('   �������� ')
y = setVectorF(xn)    