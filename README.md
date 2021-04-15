## Anunțuri
- **Iterația 1**
  - jobware.mdj (cazuri de utilizare și diagrama de clase)
  - jobware_cazuri_utilizare.docx (cazurile de utilizare detaliate)
  - LoginWindow.fxml (interafața pentru logare), EmployeeWindow.fxml (interfața pentru angajat), BossWindow.fxml (interfața pentru șef)

## 4. MONITORIZARE ANGAJAȚI 
### Într-o firmă, șeful monitorizează angajații prezenți la lucru și le traseaza sarcini individuale. Firma are o aplicație care oferă: 
1. O fereastră pentru șef:
- vede lista angajaților prezenți în firma, un element din lista precizând numele angajatului și ora la care s-a logat în sistem.
- poate transmite o sarcină unui angajat prezent astfel: selectează angajatul din lista, introduce o descriere a sarcinii și declanșeaza un buton "transmite sarcina".
-- imediat după transmiterea unei sarcini, aceasta poate fi consultată de către angajatul respectiv.
2. Câte o fereastra pentru fiecare angajat:
- atunci când angajatul vine la serviciu, introduce ora sosirii și declanșeaza un buton "prezent". Imediat dupa declanșarea butonului, șeful vede în lista lui că angajatul este prezent.
- cât timp angajatul stă la serviciu, el primește și, în consecință, vede în fereastra lui, sarcinile transmise de șef. La plecare, angajatul închide fereastra sau acționează un buton prin care se delogheaza, moment în care șeful este notificat.
3. Sarcina pe care o declanșează șeful poate avea 3 niveluri de importanță (verde - mică, galben - medie, roșu - mare). Un angajat poate avea cel mult 2 sarcini roșii și prin intermediul unui buton, acesta poate marca o sarcina ca fiind realizată.
