import math
import scipy.constants as spc

def main():
    # --- Valores Adotados ---
    # comp - Comprimento da linha transm
    comp = 3000

    freq = 2 * pow(10, 6)

    omega = 2 * freq * spc.pi

    u = 0.6 * spc.speed_of_light
    
    alfa = 0
    beta = omega / u

    gama = complex(alfa, beta)

    # --- Valores Dados ---
    # Z_C - Impedancia Carga (0, inf, 10)
    # Z_C = 0
    # Z_C = inf
    # Z_C = 10
    
    ZC = complex(99999999999999, 0)

    # Z_0 - Impedancia Caracteristica da Linha Transmissao
    Z0 = complex(75, 0)

    # R_S - Impedancia da Fonte
    ZG = complex(75, 0)

    # Fonte
    t = 0
    z = comp

    VG = V1(t)
    #VG = V2(comp, t)

    coef_reflexao = (ZC - Z0) / (ZC + Z0)
    
    coef_reflexao = coef_reflexao.real
    
    print(coef_reflexao)

    u = omega/beta

    L = (Z0 * beta)/omega

    C = beta / (Z0 * omega)
    
    j = complex(0, 1)

    Z_in =  Z0 * ( ZC + (Z0*j)*(math.tan(beta*comp)) ) / ( Z0 + (ZC*j)*(math.tan(beta*comp)) )
    
    V0 = (Z_in * VG) / (Z_in + ZG)

    I0 = VG / (Z_in + ZG)

    V0_1 = (V0 + Z0*I0)/ 2

    V0_2 = (V0 - Z0*I0)/ 2

    I0_1 = V0_1 / Z0

    I0_2 = V0_2 / Z0

    aaa = complex(math.cos(beta * z), math.sin(beta * z))
    bbb = complex(math.cos(beta * z), -math.sin(beta * z))
    ccc = complex(math.cos(omega * t), math.sin(omega * t))
    V_fasor = V0_1*(bbb) + V0_2*(aaa)
    V = V_fasor * ccc
    V = V.real

    print('V0+ : ', V0_1)
    print('V0- : ', V0_2)
    
    print('Vs(Z): ', V_fasor)
    print('V(z,t): ', V)
    
def V1(t):
    return 2 * unit_step(t)

def V2(comp, t):
    u = 0.9 * spc.speed_of_light
    K = t - comp/(10*u)
    
    return unit_step(t) - unit_step(K)

def unit_step(t):
    if t < 0.0:
        return 0
    if t >= 0.0:
        return 1


if __name__ == '__main__':
    main()