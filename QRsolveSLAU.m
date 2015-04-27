function x = QRsolveSLAU(M, b)
[Q, R] = qr(M);

b = Q' * b;
%получаем единицы на диагонали
for i = 1:size(R)
    b(i) = b(i)./R(i, i);
    R(i, :) = R(i, :)./R(i, i);
end;
%обратный ход
for j = size(M) : -1 : 1
    for i = j-1 : -1: 1
        if R(i, j) ~= 0 
            b(i) = b(i) - b(j) * R( i, j);
        end
    end
end
x = b;
    
              
        
