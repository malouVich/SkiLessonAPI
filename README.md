# Ski Lessons Management System

Dette projekt er et system til at administrere skiundervisning. Systemet giver mulighed for at tilføje og administrere instruktører, lektioner og locationer for skiundervisning. Det er bygget med Java og bruger JPA (Jakarta Persistence API) til at interagere med databasen.

## Funktionalitet

- **Instruktører**: Administrer instruktører med deres oplysninger som navn, email, telefonnummer og erfaring.
- **Ski-lektioner**: Opret og administrer ski-lektioner, som har en specifik starttid, sluttid, pris og niveau (f.eks. nybegynder, øvet).
- **Lokation**: Hver lektion har en tilknyttet lokation, som er gemt med latitude og longitude.
- **Relationer**: En instruktør kan have flere lektioner. Lektioner er knyttet til en instruktør via en mange-til-en relation.

## Teknologier

- **Java 17+**
- **Jakarta Persistence (JPA)** for databaseinteraktion
- **Lombok** for at reducere boilerplate kode


## Hvordan køres programmet
1. opret en database i din lokale Postgres instans kaldet skilesson
2. Kør main metoden i Main klassen for at starter serveren på port 7070
3. Brug dev.http filen til at teste routes. 
4. I dev.http kan man også registere en bruger og logge ind. 
4. Kør først GET {{url}}/skilessons/populate/ for at populere databasen med data.
5. Brug dev.http filen til at teste GET/POST/PUT/DELETE requests som er tilgængelige


