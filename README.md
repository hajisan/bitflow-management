# EstimationTool – Projektplanlægning og Tidsstyring

Med mange ansatte fordelt over flere lande er projektstyring en kompleks opgave. Dette system hjælper med at nedbryde og planlægge projektopgaver, lave tidskalkulationer og etablere pipelines til automatisering.

## 📌 Om projektet
- Opgave- og projektnedbrydning (f.eks. som at bygge et hus i mindre opgaver)  
- Tidsstyring: beregning af tidsforbrug, deadlines og arbejdsdage  
- CI/CD pipeline med automatiserede tests og deployment til Azure  
- Brugergrænseflade til oprettelse, vedligeholdelse og oversigt over projekter

## 🌟 Hvorfor projektet er nyttigt
Projektet giver virksomheden et klart overblik over projekter, delprojekter og opgaver, samt kalkulering af tidsforbrug og deadlines for at planlægge arbejdstid effektivt. Dette hjælper både projektledere og teammedlemmer med at bevare overblik og sikre, at deadlines overholdes.

## 🚀 Hvordan brugere kan komme i gang
1. Klon projektet fra GitHub  
2. Åbn i IntelliJ IDEA 2024.3.5 Ultimate  
3. Installer afhængigheder via Maven  
   Højreklik på projektet → Maven → Reload project  
4. Konfigurer MySQL-database  
   Opret en MySQL-database i Azure og tilpas application.properties.  
5. Kør applikationen  
6. Kør tests  
7. Deploy til Azure  
   GitHub Actions er sat op til at håndtere CI/CD.  

🔗 Kørende applikation på Azure: (indsæt-link-her) http://alphasolution-estimation-tool-fdf6h3fwcvgcdhfq.swedencentral-01.azurewebsites.net

## 📘 Hvor brugere kan få hjælp
- Scrum board og projektoversigt: GitHub Project Board  
- Opret issues i GitHub for fejl og support.

## 👥 Hvem vedligeholder og bidrager
- hajisan  
- JoakimRSWasTaken  
- sobr0002

## 💻 Softwaremæssige forudsætninger
- Java 21  
- IntelliJ IDEA 2024.3.5 Ultimate  
- Spring Boot 3.x  
- MySQL & H2 Database  
- Maven  
- JUnit 5, Spring Boot Test  
- Thymeleaf, HTML, CSS  
- GitHub & GitHub Actions  
- GitBash  
- Azure App Service & MySQL DB

## 🧪 Test
Vi har systematisk testet koden med enhedstests (JUnit 5), integrationstests (Spring Boot Test) og automatiserede testkørsler i CI/CD-processen.

## 🗄 SQL-scripts
SQL-scripts til databaseoprettelse og seed-data findes i src/main/resources.

## 🏗 Arkitektur og Design
- Arkitektur: Lagdelt Spring Boot-arkitektur med controller, service og repository lag.  
- Designprincipper: MVC, GRASP og best practices.  
- Diagrammer:  
  o Domænemodel  
  o ER-model  
  o Klassediagram

Domænemodel:
![Domænemodel - Project Manager](https://github.com/user-attachments/assets/e5e53c3a-45a6-4b49-ae4a-a667aaa0b2fe)

ER-model:
![ER Model_v10](https://github.com/user-attachments/assets/7bb4d6fc-c21b-4a41-9a85-6e1a7ae663e9)

Klassediagram:    
![Class Diagram - EstimationToolWithAttributes](https://github.com/user-attachments/assets/c8d7c13e-1d2a-4a47-bf01-ad994a5d64b7)
