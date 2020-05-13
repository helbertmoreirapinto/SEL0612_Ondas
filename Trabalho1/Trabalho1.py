import math
import scipy.constants as spc

def main():
    # --- Valores Adotados ---
    # comp - Comprimento da linha transm
    comp = 3000

    freq = 2 * pow(10, 6)

    omega = 2 * freq * spc.pi

    u = 0.6 * spc.speed_of_light

    beta = omega / u

    # --- Valores Dados ---
    # Z_C - Impedancia Carga (0, inf, 10)
    # Z_C = 0
    # Z_C = inf
    # Z_C = 10
    
    ZC = complex(10, 0)

    # Z_0 - Impedancia Caracteristica da Linha Transmissao
    Z0 = complex(10, 0)

    # R_S - Impedancia da Fonte
    ZG = complex(75, 0)

    # Fonte
    t = 1.11
    #VG = V1(t)
    VG = V2(comp, t)

    coef_reflexao = (ZC - Z0) / (ZC + Z0)

    u = omega/beta

    L = (Z0 * beta)/omega

    C = beta / (Z0 * omega)
    
    j = complex(0, 1)

    Z_in =  Z0 * ( ZC + (Z0*j)*(math.tan(beta*comp)) ) / ( Z0 + (ZC*j)*(math.tan(beta*comp)) )
    

    V0 = (Z_in * VG) / (Z_in + ZG)

    I0 = VG / (Z_in + ZG)


def V1(t):
    return 2 * unit_step(t)

def V2(comp, t):
    u = 0.9 * spc.speed_of_light
    K = t - comp/(10*u)
    print(comp/(10*u))
    return unit_step(t) - unit_step(K)

def unit_step(t):
    if t < 0.0:
        return 0
    if t >= 0.0:
        return 1


if __name__ == '__main__':
    main()