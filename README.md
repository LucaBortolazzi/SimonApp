# Simon App

Prototipo del gioco **Simon** realizzato in Android con Jetpack Compose

---

## Dispositivo di sviluppo

| Parametro          | Valore |
|--------------------|---|
| Dispositivo fisico | Google Pixel 9a |
| Versione Android   | Android 16 |
| `minSdk`           | 24 (Android 7.0) |
| `targetSdk`        | 36 |
| `compileSdk`       | 36 |

---

## Funzionalità implementate

- **Lista delle Partite — GameHistoryScreen**
    - Lista dinamica delle partite concluse, persistente anche dopo chiusura o riavvio
    - Per ogni partita: lunghezza massima della sequenza repllicata correttamente e sequenza 
  completa con parte errata evidenziata in rosso
    - Click su una partita per vedere il dettaglio completo
    - Pulsante *Nuova partita* per avviare una sessione di gioco

- **Dettaglio Partita — GameDetailScreen**
    - Visualizzazione della partita selezionata con più spazio
    - Uscita con tasto Back di sistema

- **Schermata di Gioco — GameplayScreen**
    - Griglia 3×2 di rettangoli colorati (R, G, B, M, Y, C)
    - Logica completa del gioco Simon: il computer propone una sequenza crescente, 
  il giocatore deve replicarla
    - Feedback visivo sui rettangoli (illuminazione e modifica bordi)
    - Feedback uditivo con note musicali diverse per ogni colore
    - Area di testo con la sequenza di colori premuti
    - Indicatore del turno corrente (computer / giocatore / pausa)
    - Pulsante *Avvia partita*, *Pausa/Riprendi*, *Fine partita*
    - Dialogo di erore quando il giocatore preme il colore sbagliato
    - Stato della partita preservato durante cambi di configurazione (portrait/landscape)

- **Database**
    - Persistenza delle partite con Room su SQLite
    - Le partite rimangono salvate anche dopo chiusura o riavvio del dispositivo

---

## Architettura

- **Pattern**: MVVM (Model-View-ViewModel)
- **UI**: Jetpack Compose
- **Navigazione**: Navigation Compose
- **Database**: Room con KSP
- **Stato**: ViewModel + StateFlow

---

## Lingue supportate

- Italiano
- Inglese

---

## Note

Ho notato solo dopo parecchi commit di aver utilizzato un account GitHub diverso
da quello usato nel commit iniziale. Entrambi sono miei account: uno privato
e uno istituzionale