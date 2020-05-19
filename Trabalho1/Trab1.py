import math
import numpy as np
import scipy.constants as sci
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation

def source_1(t):
    return 2 * unit_step(t)

def source_2(t, l):
    k = t - (l/(9*sci.speed_of_light))
    return unit_step(t) - unit_step(k)

def unit_step(t):
    return 1 if t >= 0 else 0

def anim_v(n):
    ln2.set_ydata(V[n,:])
    return ln2,

def anim_i(n):
    ln1.set_ydata(I[n,:])
    return ln1,

l = 30000                   #30Km
u = 0.9*sci.speed_of_light  #0.9c
Z0 = 50                     #line impedancy
Zg = 75                     #source impedancy
Zc = 000                    #charge impedancy

C = 1/(Z0*u)                #capacitor
L = Z0/u                    #inducer

hit_value = 10              #number of reflections
Nz = 100                    #number of space partitions
dz = l/Nz
dt = dz/u
dt = 0.5*dt                 #stability condition

T = (hit_value+0.5)*(l/u)   #total time
M = (int)(T/dt)             #number of time partitions

V = np.zeros((M, Nz))       #voltage matrix
I = np.zeros((M, Nz-1))     #current matrix
xv = np.linspace(0, l, Nz)
xi = np.linspace(0, l, Nz-1)

#constants
tz = dt/dz
beta1 = 2*tz/(Zg*C)
beta2 = 2*tz/(Zc*C) if Zc != 0 else math.inf
r = pow(tz, 2)/(L*C)

for n in range(0, M):
    if n == 0: #initial conditions
        I[0, 0] = ( 1/(Zg+Z0))*source_1(n)
        V[0, 0] = Z0*I[0, 0]

    elif n > 0:
        #border conditions
        V[n, 0] = (1-beta1)*V[n-1, 0] - 2*I[n-1, 0] + (2/Zg)*source_1(dt*(n-1))
        V[n, Nz-1] = (1-beta2)*V[n-1, Nz-1] + 2*I[n-1, Nz-2]
        I[n, Nz-2] = tz*V[n, Nz-1]/(C*Zc)
        
        #Voltage
        for k in range (1, Nz-2):
            V[n, k] = V[n-1, k] - (I[n-1, k] - I[n-1, k-1])
        
        #Current
        for k in range (0, Nz-2):
            I[n, k] = I[n-1, k] - r*(V[n, k+1] - V[n, k])
#V = tz*V/C
fig2, ax2 = plt.subplots()
ln2, = ax2.plot(xv, V[0])
plt.grid(True)
plt.ylim(-0.3, 0.3)
plt.xlim(-10, l+10)
plt.title('Voltage')
anim2 = FuncAnimation(fig2, func=anim_v, frames=np.arange(0, M), interval = 100,blit = True)

fig1, ax1 = plt.subplots()
ln1, = ax1.plot(xi, I[0])
plt.grid(True)
plt.ylim(-0.1, 0.1)
plt.xlim(-10, l+10)
plt.title('Current')
anim1 = FuncAnimation(fig1, func=anim_i, frames=np.arange(0, M), interval = 100,blit = True)

plt.show()

'''
if __name__ == '__main__':
    main()
'''