# Contributing Guide for EstimationTool

Tak fordi du ønsker at bidrage til EstimationTool! Dette dokument beskriver, hvordan du som nyt teammedlem kan bidrage til koden.

## 📌 Sådan bidrager du

1. **Fork repositoryet på GitHub**
   - Klik på "Fork" øverst til højre i repositoryet.
   - Klon dit forkede repository til din lokale maskine:
     ```bash
     git clone https://github.com/din-bruger/estimation_tool.git
     ```

2. **Opret en ny branch**
   - Brug et beskrivende navn, fx `feature/nyt-feature` eller `bugfix/fix-issue`.
     ```bash
     git checkout -b feature/mit-bidrag
     ```

3. **Lav dine ændringer**
   - Følg projektets kodestandarder.
   - Skriv meningsfulde commit-beskeder.

4. **Kør tests for at sikre, at alt virker**
   ```bash
   mvn test

 ##  🌿 Branch-konventioner
For at holde repositoryet organiseret og let at vedligeholde, bruger vi følgende branch-konventioner:

1. **main**
Indeholder den nyeste, stabile og kørende version af applikationen. Ingen direkte commits til denne branch. Bruges kun til releases.

2. **development**
Hovedbranchen for udvikling. Alle feature branches og bugfixes flettes ind her efter pull requests. Når udviklingen er stabil, flettes development til main.

3. **feature/**
Alle nye funktioner udvikles i branches, der starter med feature/.
Eksempel: feature/ny-funktion

4. **bugfix/**
Alle fejlrettelser udvikles i branches, der starter med bugfix/.
Eksempel: bugfix/fix-login-issue

## 🔍 Retningslinjer for bidrag
Opret en ny branch for din feature eller rettelse.

1. **Brug meningsfulde commit-beskeder.**

2. **Opret en pull request til development branch, og beskriv ændringerne tydeligt.**

3. **Sørg for at alle tests kører uden fejl, før pull request oprettes.**

4. **For større ændringer, start med at åbne en issue for diskussion.**

5. **Brug GitHub Project Board til at holde styr på opgaver og fremdrift.**

## 📘 Hjælp og support
1. **Scrum board: GitHub Project Board**

2. **Brug issues i GitHub til fejlrapporter og support.**



