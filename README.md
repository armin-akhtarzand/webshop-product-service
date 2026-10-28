# Webshop Product Service

Product Service ansvarar för webshopens produkter och lagersaldo.
Produktinformationen lagras i PostgreSQL.

## Ansvar och samverkan

- Hämtar alla produkter eller en produkt via dess ID.
- Skapar produkter.
- Tar bort produkter.
- Minskar lagersaldot för en lista med produkter.

Frontend skickar API-anrop via Gateway, som vidarebefordrar
produktanrop till Product Service.

Auth Service utfärdar JWT-token. Product Service hämtar publika
nycklar från Auth Services JWKS-endpoint för att verifiera token.

Order Service hanterar beställningar och betalningar.
Mail Service skickar orderbekräftelser.

## Tekniker

- Java 17
- Spring Boot 4.0.6
- Spring Web MVC
- Spring Security och OAuth2 Resource Server
- Spring Data JPA
- PostgreSQL
- Bean Validation
- Springdoc OpenAPI 3.0.2 och Swagger UI
- Lombok
- Maven och Maven Wrapper

## Förutsättningar

- JDK 17.
- Tillgång till en PostgreSQL-databas med förberett schema.
- En tillgänglig Auth Service för JWT-verifiering.
- Git för att klona projektet.

Maven Wrapper ingår, så Maven behöver inte installeras separat.

## Hämta projektet

```bash
git clone https://github.com/armin-akhtarzand/webshop-product-service.git
cd webshop-product-service
```

## Miljövariabler

Konfigurationen finns i `src/main/resources/application.properties`.

Följande variabler måste anges:

- `DB_URL`: JDBC-adress till PostgreSQL.
  Exempel: `jdbc:postgresql://localhost:5432/webshop_product`.
- `DB_USERNAME`: databasens användarnamn.
- `DB_PASSWORD`: databasens lösenord.

Följande variabler har standardvärden:

- `PORT`: `8081`.
- `AUTH_JWKS_URI`: `http://localhost:8080/auth/jwks`.

`AUTH_JWKS_URI` ska peka på JWKS-endpointen hos den Auth Service
som utfärdar de JWT-token som används.

Ange variablerna i terminalen eller i IntelliJ under
Run → Edit Configurations → Environment variables.

Exempel för macOS och Linux, med egna lokala databasuppgifter:

```bash
export DB_URL='jdbc:postgresql://localhost:5432/webshop_product'
export DB_USERNAME='din_databasanvandare'
export DB_PASSWORD='ditt_lokala_losenord'
export PORT='8081'
export AUTH_JWKS_URI='http://localhost:8080/auth/jwks'
```

Ersätt exempelvärdena med dina egna uppgifter innan tjänsten startas.
En `.env`-fil läses inte automatiskt av den visade konfigurationen.
Spara inte riktiga lösenord i Git.

## Databas

Tjänsten använder PostgreSQL.

Projektet är konfigurerat med:

```properties
spring.jpa.hibernate.ddl-auto=none
```

Hibernate skapar eller uppdaterar därför inte tabeller automatiskt.
Databasen och det schema som produktentiteterna behöver måste
förberedas innan produktfunktionerna används.

För teamets befintliga databas används det förberedda schemat.
Kontakta teamet för databasåtkomst och instruktioner för att
förbereda en ny lokal databas.

H2 finns som beroende i projektet, men standardkonfigurationen
i `application.properties` använder PostgreSQL.

## Starta lokalt

Förbered PostgreSQL-databasen och dess schema. Ange databasvariablerna. Starta Auth Service och kontrollera
`AUTH_JWKS_URI`. Starta Product Service från projektets rotmapp.

### macOS och Linux

```bash
./mvnw spring-boot:run
```

Om skriptet saknar körbehörighet:

```bash
bash ./mvnw spring-boot:run
```

### Windows PowerShell

Ange motsvarande miljövariabler i PowerShell eller IntelliJ.
Starta sedan tjänsten:

```powershell
.\mvnw.cmd spring-boot:run
```

Tjänstens standardadress är:

```text
http://localhost:8081
```

Om `PORT` ändras behöver även adressen som Gateway använder
för Product Service uppdateras.

För att använda produktfunktionerna i webbgränssnittet behöver
även Gateway och Frontend vara igång.

## API

Följande endpoints finns i ProductController:

- `GET /products`: hämtar alla produkter.
- `GET /products/{id}`: hämtar en produkt via ID.
- `POST /products`: skapar en produkt och returnerar status 201.
- `DELETE /products/{id}`: tar bort en produkt och returnerar status 204.
- `POST /products/stock/decrease`: minskar lagersaldot.
  Tar emot en lista med `ProductStockRequest`.

Produktanrop kräver en giltig JWT-token och de behörigheter
som anges i `SecurityConfig`.

Att skapa och ta bort produkter kräver `ROLE_ADMIN`.
Övriga rollkrav finns i `SecurityConfig`.

Vid direkta anrop till Product Service skickas token i headern:

```text
Authorization: Bearer <JWT_TOKEN>
```

Rollerna hämtas från JWT-tokenens `roles`-claim.
Frontend använder Gateways inloggningsflöde med HttpOnly-cookie.

## API-dokumentation

När tjänsten körs på standardporten finns dokumentationen här:

- Swagger UI: http://localhost:8081/swagger-ui/index.html
- OpenAPI: http://localhost:8081/v3/api-docs

Dokumentationsadresserna är tillgängliga utan inloggning.
Skyddade produktanrop kräver fortfarande giltig token och behörighet.

Se Swagger UI och projektets DTO-klasser för request- och responseformat.

## Test och bygge

Kör tester:

```bash
./mvnw test
```

Bygg och verifiera projektet:

```bash
./mvnw clean verify
```

På Windows används `.\mvnw.cmd` i stället för `./mvnw`.

Tester som startar applikationskontexten kan behöva
databasinställningar och andra beroenden enligt testkonfigurationen.

## Felsökning

- Saknad miljövariabel: kontrollera `DB_URL`, `DB_USERNAME`
  och `DB_PASSWORD`.
- Databasanslutningen misslyckas: kontrollera adress,
  inloggningsuppgifter och att databasen är tillgänglig.
- Tabell saknas: kontrollera att databasschemat har förberetts.
  `ddl-auto=none` skapar inga tabeller.
- Status 401: kontrollera att JWT-token är giltig och att
  rätt publika nyckel kan hämtas via `AUTH_JWKS_URI`.
- Status 403: kontrollera att användaren har rätt roll för anropet.
- Porten används redan: stoppa den andra processen eller ändra `PORT`.

## Systemets repositories

- [Auth Service](https://github.com/armin-akhtarzand/webshop-auth-server)
- [Product Service](https://github.com/armin-akhtarzand/webshop-product-service)
- [Order Service](https://github.com/armin-akhtarzand/webshop-order-service)
- [Mail Service](https://github.com/armin-akhtarzand/webshop-mail-service)
- [Gateway](https://github.com/SofiaTekle/webshop-gateway)
- [Frontend](https://github.com/SofiaTekle/webshop-frontend)