function y = setVectorF(x)
y(1) = cos(x(1)*x(2)) - exp(-3*x(3)) + x(4)*(x(5)^2) - x(6) - sinh(2*x(8))*x(9) + 2*x(10) + 2.0004339741653854440;
y(2) = sin(x(1)*x(2)) + x(3)*x(9)*x(7) - exp(-x(10) + x(6)) + 3*(x(5) ^2) - x(6)*(x(8) + 1) + 10.886272036407019994;    
y(3) = x(1) - x(2) + x(3) - x(4) + x(5) - x(6) + x(7) - x(8) + x(9) - x(10) - 3.1361904761904761904; 
y(4) = 2 * cos(x(4) - x(9)) + x(5)/(x(3)+x(1)) - sin(x(2)^2) + (cos(x(7)*x(10)))^2 -x(8) - 0.1707472705022304757;
y(5) = sin(x(5)) + 2*x(8)*(x(3) + x(1)) - exp(-x(7)*(x(6) - x(10))) + 2*cos(x(2)) - 1/(x(4) - x(9)) - 0.3685896273101277862;
y(6) = exp(x(1)-x(4)-x(9)) + x(5)^2 / x(8) + 0.5 * cos(3*x(10)*x(2)) - x(6)*x(3) + 2.0491086016771875115;
y(7) = x(2)^3 * x(7) - sin(x(10)/x(5) + x(8)) + (x(1) - x(6))*cos(x(4)) + x(3) - 0.7380430076202798014;
y(8) = x(5) *(x(1) - 2*x(6))^2 - 2 * sin (-x(9) + x(3)) + 1.5*x(4) - exp(x(2)*x(7)+x(10)) + 3.5668321989693809040;
y(9) = 7/x(6) + exp(x(5)+x(4))- 2*x(2)*x(8)*x(10)*x(7) + 3*x(9) - 3*x(1) - 8.4394734508383257499;
y(10) = x(1)*x(10) + x(2)*x(9) - x(3)*x(8) + sin(x(4)+x(5)+x(6))* x(7) - 0.78238095238095238096;