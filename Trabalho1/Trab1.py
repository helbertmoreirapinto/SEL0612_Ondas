import math
import numpy as np
import scipy.constants as sci
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation

def source1(t):
    return 2 * unit_step(t)

def source2(t):
    k = t - (l/(9*sci.speed_of_light))
    return unit_step(t) - unit_step(k)

def unit_step(t):
    return 1 if t >= 0 else 0

def anim(n):
    ln1.set_ydata(I[n,:])
    ln2.set_ydata(V[n,:])
    return ln1, ln2,

l = 70000                   #line size 70Km
u = 0.9*sci.speed_of_light  #0.9c
Z0 = 50                     #line impedancy
Zg = 75                     #source impedancy
Zc = 100                    #charge impedancy

C = 1/(Z0*u)                #capacitor
L = Z0/u                    #inducer

hits = 10                   #number of reflections
Nz = 100                    #number of space partitions
dz = l/Nz
alfa = 0.8                  #stability constant
dt = alfa*dz/u              #stability condition
        
T = (hits+0.5)*(l/u)   #total time
M = (int)(T/dt)             #number of time partitions

V = np.zeros((M, Nz))       #voltage matrix
I = np.zeros((M, Nz-1))     #current matrix
xv = np.linspace(0, l, Nz)
xi = np.linspace(0, l, Nz-1)

#constants
tz = dt/dz
beta1 = 2*tz/(Zg*C)
beta2 = 0
beta3 = 0
r = pow(tz, 2)/(L*C)
short_circuit = False

gama1 = (Zg-Z0)/(Zg+Z0)
if Zc == 0:
    beta2 = math.inf
    beta3 = math.inf
    gama2 = 1.0
    short_circuit = True
elif Zc == math.inf:
    beta2 = 0.0
    beta3 = 0.0
    gama2 = -1.0
else:
    beta2 = 2*tz/(Zc*C)
    beta3 = tz/(Zc*C)
    gama2 = (Zc-Z0)/(Zc+Z0)

for n in range(0, M):
    if n == 0: #initial conditions
        I[0, 0] = source1(n)/(Zg+Z0)
        V[0, 0] = Z0*I[0, 0]
    elif n > 0:
        #border conditions
        V[n, 0] = (1-beta1)*V[n-1, 0] - 2*I[n-1, 0] + (2/Zg)*source1(dt*(n-1))
        if short_circuit:
            V[n, Nz-1] = 0
        else:
            V[n, Nz-1] = (1-beta2)*V[n-1, Nz-1] + 2*I[n-1, Nz-2]
        
        #Voltage
        for k in range (1, Nz-1):
            V[n, k] = V[n-1, k] - (I[n-1, k] - I[n-1, k-1])
        
        #Current
        for k in range (0, Nz-1):
            I[n, k] = I[n-1, k] - r*(V[n, k+1] - V[n, k])

print('gama source', gama1)
print('gama charge', gama2)

lim_y = 0
for i in ((int)(M/20), M-1):
    aux1 = max(V[i,:], key=abs)
    aux2 = max(I[i,:], key=abs)
    if aux1 > lim_y:
        lim_y = aux1
    if aux2 > lim_y:
        lim_y = aux2
lim_y += 0.5*lim_y

dlim_x = l
dlim_x = 0.01*dlim_x

fig, ax = plt.subplots()
ln1, = ax.plot(xi, I[0], color='xkcd:blue', label='Current [A]')
ln2, = ax.plot(xv, V[0], color='xkcd:red', label='Voltage [V]')
plt.xlabel('Distance [m]')
plt.grid(True)
plt.ylim(-lim_y, lim_y)
plt.xlim(-dlim_x, l+dlim_x)
plt.legend()
anim = FuncAnimation(fig, func=anim, frames=np.arange(0, M), interval = 100,blit = True)
plt.show()