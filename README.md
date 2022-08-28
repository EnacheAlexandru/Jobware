
<p align="center">
  <img src="https://user-images.githubusercontent.com/63500798/187095641-55adf32b-9fa6-4364-abb5-61f3d6388e34.jpg">
</p>

## Aplicație pentru monitorizarea angajaților
### Într-o firmă, șeful monitorizează angajații prezenți la lucru și le traseaza sarcini individuale. Firma are o aplicație care oferă: 
1. O fereastră pentru șef:
- vede lista angajaților prezenți în firma, un element din lista precizând numele angajatului și ora la care s-a logat în sistem.
- poate transmite o sarcină unui angajat prezent astfel: selectează angajatul din lista, introduce o descriere a sarcinii și declanșeaza un buton "Transmite sarcina".
- imediat după transmiterea unei sarcini, aceasta poate fi consultată de către angajatul respectiv.
2. Câte o fereastra pentru fiecare angajat:
- atunci când angajatul vine la serviciu, se loghează cu datele sale și declanșeaza un buton "Login". Imediat dupa declanșarea butonului, șeful vede în lista lui că angajatul este prezent.
- cât timp angajatul stă la serviciu, el primește și, în consecință, vede în fereastra lui, sarcinile transmise de șef. La plecare, angajatul închide fereastra sau acționează un buton prin care se delogheaza, moment în care șeful este notificat.
3. Sarcina pe care o declanșează șeful poate avea 3 niveluri de importanță (mică, medie, mare). Prin intermediul unui buton, acesta poate marca o sarcina ca fiind realizată.

## Anunțuri
### **Faza 3**
- **Varianta finală a aplicației;**
- Fișierul *jobware.mdj* (actualizat și reîncărcat) - diagrama cazurilor de utilizare, diagrama de clase;
  - diagrame de secvență pentru: logare, delogare, afișarea angajaților logați, afișarea notificărilor cu angajații delogați, afișarea sarcinilor distribuite (șef), afișarea sarcinilor atribuite (un angajat);
  - diagrame de comunicare pentru: trimiterea unei sarcini (șef), finalizarea unuei sarcini (un angajat), notificarea șefului când un angajat se deloghează;
- Directorul *jobware* (actualizat și reîncărcat) - aplicație cu sursa scrisă în Java (folosind Gradle) cu tehnologii precum: Spring (client-server), Hibernate, JavaFX, SQLite.

### **Faza 2**
- jobware.mdj => diagrama cazurilor de utilizare / diagrama de clase pentru iterația 1 / diagrama de secvență pentru cazurile de la iterația 1 (login, logout)
- jobware => proiect cu sursa scrisă în Java (folosind Gradle) cu tehnologii precum: Spring, Hibernate.

### **Faza 1**
- jobware.mdj (diagrama cazurilor de utilizare și diagrama de clase)
- jobware_cazuri_utilizare.docx (cazurile de utilizare detaliate)
- (în directorul *resources*) LoginWindow.fxml (interafața pentru logare), EmployeeWindow.fxml (interfața pentru angajat), BossWindow.fxml (interfața pentru șef)
