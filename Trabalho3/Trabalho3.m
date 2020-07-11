clear all;
clc;

xdim=200;
ydim=200;

time_tot=350;

xsource=100;
ysource=100;

S=1/(2^0.5);

epsilon0=(1/(36*pi))*1e-9;
mu0=4*pi*1e-7;
c=3e+8;

delta=1e-6;
deltat=S*delta/c;

Ez=zeros(xdim,ydim);
Hy=zeros(xdim,ydim);
Hx=zeros(xdim,ydim);

epsilon=epsilon0*ones(xdim,ydim);
mu=mu0*ones(xdim,ydim);

sigma=4e-4*ones(xdim,ydim);
sigma_star=4e-4*ones(xdim,ydim);

amplit=50;
frequency=1.5e+13;
gaussian=0;
sine=0;
impulse=0;

A=((mu-0.5*deltat*sigma_star)./(mu+0.5*deltat*sigma_star)); 
B=(deltat/delta)./(mu+0.5*deltat*sigma_star);
                          
C=((epsilon-0.5*deltat*sigma)./(epsilon+0.5*deltat*sigma)); 
D=(deltat/delta)./(epsilon+0.5*deltat*sigma);                     

for n=1:1:time_tot
    
    if gaussian==0 && sine==0 && n==1
        Ez(xsource,ysource)=amplit;
    end
    
    if n<xsource-2
        n1=xsource-n-1;
    else
        n1=1;
    end
    if n<xdim-1-xsource
        n2=xsource+n;
    else
        n2=xdim-1;
    end
    if n<ysource-2
        n11=ysource-n-1;
    else
        n11=1;
    end
    if n<ydim-1-ysource
        n21=ysource+n;
    else
        n21=ydim-1;
    end
    
    Hy(n1:n2,n11:n21)=A(n1:n2,n11:n21).*Hy(n1:n2,n11:n21)+B(n1:n2,n11:n21).*(Ez(n1+1:n2+1,n11:n21)-Ez(n1:n2,n11:n21));
    Hx(n1:n2,n11:n21)=A(n1:n2,n11:n21).*Hx(n1:n2,n11:n21)-B(n1:n2,n11:n21).*(Ez(n1:n2,n11+1:n21+1)-Ez(n1:n2,n11:n21));
    
    Ez(n1+1:n2+1,n11+1:n21+1)=C(n1+1:n2+1,n11+1:n21+1).*Ez(n1+1:n2+1,n11+1:n21+1)+D(n1+1:n2+1,n11+1:n21+1).*(Hy(n1+1:n2+1,n11+1:n21+1)-Hy(n1:n2,n11+1:n21+1)-Hx(n1+1:n2+1,n11+1:n21+1)+Hx(n1+1:n2+1,n11:n21));
    
    Ez(1:xdim,1)=0;
    Ez(1:xdim,ydim)=0;
    Ez(1,1:ydim)=0;
    Ez(xdim,1:ydim)=0;
    
    if impulse==0
        if gaussian==0 && sine==0
            Ez(xsource,ysource)=amplit;
        end
        if sine==1
            tstart=1;
            N_lambda=c/(frequency*delta);
            Ez(xsource,ysource)=amplit*sin(((2*pi*(c/(delta*N_lambda))*(n-tstart)*deltat)));
        end
        if gaussian==1
            if n<=42
                Ez(xsource,ysource)=amplit*(10-15*cos(n*pi/20)+6*cos(2*n*pi/20)-cos(3*n*pi/20))/32;
            else
                Ez(xsource,ysource)=0;
            end
        end
    else
        Ez(xsource,ysource)=0;
    end
    
    imagesc(delta*(1:1:xdim)*1e+6,(1e+6*delta*(1:1:ydim))',Hy',[-1,1]);colorbar;
    title(['\fontsize{20}Colour-scaled image plot of Ez in a spatial domain with PEC boundary and at time = ',num2str(round(n*deltat*1e+15)),' fs']); 
    xlabel('x (in um)','FontSize',20);
    ylabel('y (in um)','FontSize',20);
    set(gca,'FontSize',20);
    getframe;
end