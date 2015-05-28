x = -4:0.1: 4;
y = 1.5 - cos(x);
y1 = -4: 0.1: 4;
x1 = (1 + sin(y1 - 0.5)) / 2;
plot(x, y, x1, y1)
grid on