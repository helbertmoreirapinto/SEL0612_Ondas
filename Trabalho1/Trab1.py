import math
import numpy as np
import scipy.constants as sci
import matplotlib
import matplotlib.animation as animation
import matplotlib.pyplot as plt
from matplotlib.widgets import Slider


def source1(tn):
    return 2*unit_step(tn)


def source2(tn):
    k = tn - (l/(9*sci.speed_of_light))
    return unit_step(tn) - unit_step(k)


def unit_step(tn):
    return 1 if tn >= 0 else 0


def anim(n):
    ln1.set_ydata(V[n,:])
    ln2.set_ydata(I[n,:])
    h = (int)(n*(hits+0.5)/M)
    txt = 'Hits = %d\nTime t = %.4f ms' % (h, 1000*n*dt)
    text_time.set_text(txt)
    return ln1, ln2,


def upd_slider(val):
    n = (int)(slider.val*((M-1)/T))
    ln1.set_ydata(V[n,:])
    ln2.set_ydata(I[n,:])
    h = (int)(n*(hits+0.5)/M)
    txt = 'Hits = %d\nTime t = %.4f ms' % (h, 1000*n*dt)
    text_time.set_text(txt)
    plt.draw()


u = 0.9*sci.speed_of_light  #0.9c
Z0 = 50                     #line impedancy
C = 1/(Z0*u)                #capacitor
L = Z0/u                    #inducer
Zg = 75                     #source impedancy
Zc = Z0               #charge impedancy

hits = 10                   #number of reflections
l = 70000                   #line size
Nz = 500                    #number of space partitions
dz = l/Nz
alfa = 0.7                  #stability constant
dt = alfa*dz/u              #stability condition
T = (hits+0.5)*(l/u)        #total time
M = (int)(T/dt)             #number of time partitions

V = np.zeros((M, Nz))       #voltage matrix
I = np.zeros((M, Nz-1))     #current matrix
xv = np.linspace(0, l, Nz)
xi = np.linspace(0, l, Nz-1)

#constants
tz = dt/dz
beta1 = 2*tz/(Zg*C)
beta2 = 0
r = pow(tz, 2)/(L*C)
short_circuit = False
gama_g = (Zg-Z0)/(Zg+Z0)
if Zc == 0:
    beta2 = math.inf
    gama_c = 1.0
    short_circuit = True
elif Zc == math.inf:
    beta2 = 0.0
    gama_c = -1.0
else:
    beta2 = 2*tz/(Zc*C)
    gama_c = (Zc-Z0)/(Zc+Z0)

print('gama source %.2f' % gama_g)
print('gama charge %.2f' % gama_c)

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
V = V*tz/C


#plot graphics
#grid limits
lim_v_y = 0
lim_i_y = 0
for i in ((int)(M/20), M-1):
    val_v = max(V[i,:], key=abs)
    val_i = max(I[i,:], key=abs)
    if val_v > lim_v_y:
        lim_v_y = val_v
    if val_i > lim_i_y:
        lim_i_y = val_i
lim_v_y += 0.5*lim_v_y
lim_i_y += 0.5*lim_i_y
d_lim_x = l
d_lim_x = 0.01*d_lim_x

'''
Writer = animation.writers['ffmpeg']
writer = Writer(fps=30, metadata=dict(artist='Me'), bitrate=1800)
matplotlib.use("Agg")
'''
fig, (ax1, ax2) = plt.subplots(2, 1)
txt = 'Hits = %d\nTime t = %.4f ms' % (0, 0)
info_line = 'Vs(t) = 2u(t) and RL = Z0 (max power transfer)'
ln1, = ax1.plot(xv, V[0], color='xkcd:red', label='Voltage [V]')
ax1.set_xlim(-d_lim_x, l+d_lim_x)
ax1.set_ylim(-lim_v_y, lim_v_y)
ax1.grid(True)
ax1.legend()

ln2, = ax2.plot(xi, I[0], color='xkcd:black', label='Current [A]')
ax2.set_xlim(-d_lim_x, l+d_lim_x)
ax2.set_ylim(-lim_i_y, lim_i_y)
ax2.grid(True)
ax2.legend()

text_time = plt.figtext(0.1, 0.9, txt)
plt.figtext(0.35, 0.9, info_line)
#for to use only slider, comment the line below (anim)
anim = animation.FuncAnimation(fig, func=anim, frames=np.arange(0, M, (int)(Nz/100)), interval=100, repeat=False)
#anim.save('teste.mp4', writer=writer)

plt.subplots_adjust(bottom=0.25)
pos_slider = plt.axes([0.1, 0.1, 0.8, 0.03])
slider = Slider(pos_slider, 'time', 0, T, valinit=0)
slider.on_changed(upd_slider)

plt.show()