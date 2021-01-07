% script Matlab per la simulazione di spectrum sensing tramite metodo ED (energy detector)
% svolto in locale sul singolo SU (secondary user)
% Il PU (primary user) è un segnale modulato QPSK
% Le prestazioni sono calcolate in termini di Detection probability con formule teoriche e di simulazione
% Prof. F. Benedetto - Corso di Software Cognitive Radio - Maggio 2018
clear all clc close all
% Parametri della simulazione Pfa = 10^-2;
SNR_dB = [-25:1:-5]; %SNR in dB SNR_L = 10.^(SNR_dB./10);
Ps = 1;
Pn = Ps./SNR_L; % potenza ovvero varianza di rumore
std_rumore = sqrt(Pn); % dev. std. del rumore
N_prove_H0 = 10000;
N_prove_H1 = 1000;
N_campioni = 100;
segnale_PU = sign(0.5 - rand(1, N_campioni)) + 1i*sign(0.5 - rand(1, N_campioni)); %segnale primary user, modulato QPSK
segnale_PU = segnale_PU./std(segnale_PU); % rendo il segnale PU a potenza 1
% calcoliamo la soglia condizionatamente al caso H0 % per ogni valore di SNR
for i=1:length(SNR_dB)
for j=1:N_prove_H0
rumore = randn(1, N_campioni) + 1i*randn(1, N_campioni); % generazione del rumore rumore = rumore./std(rumore); % rumore ora sia a potenza unitaria
rumore = std_rumore(i).*rumore; % rumore a potenza Pn -> ad SNR_dB
energia_H0(j) = sum(abs(rumore).^2)/N_campioni; % calcolo dell'energia in H0 end
% calcolo soglia per via teorica
media_H0 = mean(energia_H0);
var_H0 = var(energia_H0);
soglia_theor(i) = media_H0 + ((2*var_H0)^.5)*erfinv(1-2*Pfa); %Threshold_theor(i) = (Mean_H0) + ((2*Var_H0)^.5) * erfinv(1 - 2 * P_FA);
% calcolo soglia per simulazione
energia_H0_sorted = sort(energia_H0); % vettore di energie ordinato in senso crescente indice_soglia = ceil(N_prove_H0 * Pfa); % numero di VT sopra soglia per avere la Pfa desiderata soglia_sim (i) = energia_H0_sorted(length(energia_H0_sorted)-indice_soglia);
end
for i=1:length(SNR_dB)
for j=1:N_prove_H1
rumore = randn(1, N_campioni)+1i*randn(1, N_campioni); % generazione del rumore
rumore = rumore./std(rumore); % normalizzato a 1 in potenza
rumore = std_rumore(i).*rumore;
segnale_ricevuto = segnale_PU + rumore; % generazione del segnale rumoroso ricevuto a valle del canale
energia_H1(j) = sum(abs(segnale_ricevuto).^2)/N_campioni; % variabili di test nel caso H1 end
Pd_theor(i) = .5 + .5*erf((-soglia_theor(i)+mean(energia_H1))*((2*var(energia_H1))^(-.5)));%PD_Theor(i) = .5 + .5 * erf((-Threshold_theor(i) + (Mean_H1))*(2*Var_H1)^(-.5));
cont_sim = sum(energia_H1>soglia_sim(i));
Pd_Sim(i) = cont_sim/N_prove_H1; % calcolo probabilità di detection per simulazione
Pd_semi(i) = sum(energia_H1>soglia_theor(i))/N_prove_H1; % calcolo della detection semianalitica

end
plot(SNR_dB, Pd_theor, SNR_dB, Pd_semi, SNR_dB, Pd_Sim) title ('Detection')
legend ('Teorica', 'Semi-analitica', 'Simulata')
grid on