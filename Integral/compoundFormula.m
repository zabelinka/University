a = 2.1;
b = 3.3;

N = 4;         %����� �������� �� �������

n = 3; % ������������

iteration = 0;
Integral = zeros(3, 1);
R = -1;
%�������������� ������� �������� 
 m = 3;            % �������-������
% m = 6;             % ������
while abs(R) > 1e-12
    iteration  = iteration + 1;
    disp('��������');
    disp(iteration);
    Integral(1) = 0;
    x1 = a;
    % disp('����� ����');
    H = (b - a) / N;    % ��� = ����� �������
    for i = 1 : N       % �������� �� ���� �������� �������
        % disp('����� ������� = ');
        % disp(i);
        x2 = a + i * H; % ������ ������� �������
        % ������� �� ������ ������� ��������:
        
        % �� �������-������
         Integral(1) = Integral(1) + NewtonCotes(x1, x2);
      
        % �� ������
        % Integral(1) = Integral(1) + Gauss(x1, x2); 
        x1 = x2;
    end
    disp('��������');
    disp(vpa(Integral(1), 13));
    % ����������� �� �����
    R = (Integral(1) - Integral(2)) / (2^m - 1);
    disp('�����������');
    disp(R);
    
    % ������
    Eitken = log2((Integral(2) - Integral(3))/(Integral(1) - Integral(2)));
    disp('�������� �� �������')
    disp(Eitken);
    
    Integral(3) = Integral(2);  % ��������������
    Integral(2) = Integral(1);  % ����������
    N = N * 2;          % ��������� ����� �������� � 2�. = ��������� ����� ���� H �� 2
    
    
    
end
disp(vpa(Integral(1), 13))

