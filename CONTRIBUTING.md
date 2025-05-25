# Contributing Guide for EstimationTool

Tak fordi du ønsker at bidrage til EstimationTool! Dette dokument beskriver, hvordan du som nyt teammedlem kan bidrage til koden. Vi vedlægger i bunden af vores guidelines en "Dos and Don'ts".

## 📌 Sådan bidrager du

1. **Fork repositoryet på GitHub**
   - Klik på "Fork" øverst til højre i repositoryet.
   - Klon dit forkede repository til din lokale maskine:
     ```bash
     git clone https://github.com/din-bruger/estimation_tool.git
     ```

2. **Opret en ny branch**
   - Vi navngiver vores branches med et keyword som feature eller bugfix efterfulgt af dit navn og evt. en beskrivelse af branchens formål, fx `feature/Joakim-nyt-feature` eller `bugfix/Joakim-fix-denne-issue`.
     ```bash
     git checkout -b feature/navn-bidrag
     ```

3. **Lav dine ændringer**
   - Følg projektets kodestandarder. 
     De er ikke strenge. Vi kræver blot udpenslet og læsbar kode med inline comments i nødvendigt omfang, samt at de eksisterende naming conventions følges.
   - Skriv meningsfulde commit-beskeder. 
     Sådan kan vi alle følge med i, hvad der sker.

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

## 📦 GitHub Projects
Vi bruger GitHub Projects til at holde et overblik over projektets gang. Herinde kan man se en oversigt over de åbne issues, oprettede tasks og åbne bugs der venter på fixes.
Vi har nogle få konventioner til, hvordan der skal interageres med vores Board:

- Når en task rykkes fra Sprint Backlog til In Progress, så skal du assigne dig selv til tasken og dens User Story, samt rykke begge til In Progress.
- Når du rykker en task og en User Story til In Progress vil vi gerne have, at du markerer en startdato for arbejdet på disse.
- Når tasken er gennemfør bør du markere slutdatoen og tage stilling til eventuelle Interne Dev Kriterier, der ligger under den givne task. Sæt hak i dem, der er opfyldt så vi ved, hvad der er gjort.
- Hvis du har udarbejdet tests på din udførte task bør du også give denne task Test-labelen, så vi kan se, at den er testet.
- Når alle tasks tilhørende en User Story er færdige og rykket til Done, kan User Storyen også få en slutdato og blive rykket til Done.
- Hvis der er en fejl i en task, bør du markere denne med Bug-labelen og vedlægge en kommentar der beskriver denne bug grundigt, så andre kan se, at der er en bug i denne funktion og hvordan de eventuelt skal arbejde på at løse den.

## 🔍 Retningslinjer for bidrag i korte træk
Opret en ny branch for din feature eller rettelse. Se nærmere beskrivelse under "📌 Sådan bidrager du"-afsnittet.

1. **Brug meningsfulde commit-beskeder.**

2. **Opret en pull request til development branch, og beskriv ændringerne tydeligt.**

3. **Sørg for at alle tests kører uden fejl, før pull request oprettes.**

4. **For større ændringer, start med at åbne en issue for diskussion.**

5. **Brug GitHub Project Board til at holde styr på opgaver og fremdrift.**

## 📘 Hjælp og support
1. **Scrum board: GitHub Project Board**

2. **Brug issues i GitHub til fejlrapporter og support.**

## ❤️‍🔥 Afsluttende kommentarer
Vi siger tak for din opmærksomhed og din fremtidige villighed til at dedikere din tid og din passion til vores fælles projekt!

Oversigt over Dos and Don'ts:
![Do's   Dont's](https://github.com/user-attachments/assets/068754c0-f731-4abb-9880-1ddd4d279789)

