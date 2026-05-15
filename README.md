# Simon App

Prototipo del gioco **Simon** realizzato in Android con Jetpack Compose.

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

- **Schermata 1 — GameplayScreen**
    - Griglia 3×2 di rettangoli colorati (R, G, B, M, Y, C)
    - Area di testo con la sequenza di colori premuti
    - Pulsante *Cancella* per azzerare sequenza corrente
    - Pulsante *Fine partita* per salvare sequenza e passare alla schermata 2
    - Layout adattivo portrait e landscape

- **Schermata 2 — GameHistoryScreen**
    - Lista dinamica delle partite concluse
    - Per ogni partita: numero di colori premuti e sequenza

---

## Lingue supportate

- Italiano
- Inglese

---

## Note

Ho notato solo dopo parecchi commit di aver utilizzato un account 
GitHub diverso da quello usato nel commit iniziale. Entrambi sono
miei account: uno privato e uno istituzionale.
