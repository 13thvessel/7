# CloudStream Desktop (Windows 11)

Riscrittura desktop di CloudStream 4.8.0 con **Kotlin + Compose Multiplatform**.
Riusa il modulo `library` originale (MainAPI, modelli, extractor, rete) copiato in
`src/commonMain`, `src/jvmCommonMain`, `src/desktopMain`; l'interfaccia e il player sono nuovi
(`src/desktopMain/kotlin/com/lagradost/desktop`).

> Stato: **non ancora compilato**. Scritto senza accesso a Gradle/Maven, quindi il primo build può
> richiedere piccole correzioni (versioni dei plugin, import).

## Requisiti (Windows 11)
- JDK 17 o superiore (es. Temurin 21)
- [VLC media player 64 bit](https://www.videolan.org/) installato (usato dal player integrato)
- Per l'installer `.exe`: [WiX Toolset 3.x](https://github.com/wixtoolset/wix3/releases) nel PATH

## Compilare
```powershell
.\gradlew run                  # avvia l'app in sviluppo
.\gradlew createDistributable  # cartella portabile con CloudStream.exe  -> build\compose\binaries\main\app\
.\gradlew packageExe           # installer .exe (serve WiX)             -> build\compose\binaries\main\exe\
```
Senza Windows locale: lancia il workflow **Build Windows .exe** su GitHub (Actions) e scarica l'artifact.

## Cosa c'è
Home per provider, ricerca su tutte le estensioni, pagina dettagli (film/serie/anime con episodi
e doppiaggio), selezione sorgente, player VLC con volume, seek e sottotitoli, preferiti,
gestione estensioni. Dati in `%APPDATA%\CloudStreamDesktop`.

## Estensioni
I `.cs3` Android sono DEX e **non funzionano**. Un plugin desktop è un `.jar` con:
- `manifest.json` alla radice: `{"name":"Mio","pluginClassName":"com.esempio.MioPlugin","version":1}`
- una classe che estende `com.lagradost.cloudstream3.plugins.BasePlugin` e in `load()` chiama
  `registerMainAPI(MioProvider())` (la stessa API delle estensioni Android: `search`, `load`, `loadLinks`).
Compila il plugin contro questo progetto (o contro il target `jvm` della `library`) e mettilo in
`%APPDATA%\CloudStreamDesktop\plugins` oppure installalo dalla schermata Estensioni.

## Non incluso (rispetto all'app Android)
Download offline, Chromecast, sync account (AniList/MAL/Trakt), tracking progressi, temi,
WebView resolver, import repository `.cs3`.
