# Auctions Exercise
Hogeschool PXL - Exercise Auctions

Fork this project to get started!

### Inleiding

Start de spring boot toepassing en verken de code.
Om de applicatie uit te voeren heb je een databank met de naam auctionsdb nodig.
Je vindt een docker-compose.yml bestand om deze databank aan te maken. 
De User-tabel wordt aangemaakt (of aangepast) door hibernate.

De volgende REST endpoints zijn beschikbaar:
1. Het opslaan van een nieuwe user

POST http://localhost:8080/auctions/users
met body:

`{
"firstName": "Sophie",
"lastName": "De Tester",
"email": "sophie@pxl.be",
"dateOfBirth": "24/04/1998"
}`

2. Het opzoeken van alle users

GET http://localhost:8080/auctions/users
   

3. Het opzoeken van een user adhv de primary key

GET http://localhost:8080/auctions/users/{userId}

### Opdracht 1: Unit testing

- implement the unit tests
- to test the service layer you have to use Mockito 
(Pluralsight course on Mockito: https://app.pluralsight.com/library/courses/tdd-junit5/table-of-contents)
- the persistence layer is tested with an in-memory h2 database

### Opdracht 2: Extra entity klassen

Er zullen items aangeboden in veilingen (auctions). Iedere veiling heeft een beschrijving
(description) en een einddatum (endDate). Gebruikers kunnen een bod uitbrengen op een item.
De entity klasse User hadden wij reeds voorzien. Pas nu
de klassen Auction en Bid aan zodat dit ook geldige entity klassen worden. 
Voorzie de bidirectionele relatie tussen Auction en Bid (cascadetype in de klasse Auction wordt ALL). 
Voorzie in de klasse Auction de methode addBid() waarmee de  bi-directionele relatie steeds 
wordt gerespecteerd.

Implementeer in de klasse Auction verder ook de methoden isFinished() en findHighestBid().
- isFinished() geeft false indien de einddatum van de veiling nog niet is verstreken, en true
indien de einddatum van de veiling is verstreken.
   
- findHighestBid() geeft het Bid-object met de hoogste waarde. Indien er nog geen biedingen zijn wordt null gegeven. 
  
Schrijf unit testen voor de methoden isFinished() en findHighestBid().

### Opdracht 3: AuctionDao en AuctionDaoImpl

De AuctionDao voorziet de volgende functionaliteit:

```java
public interface AuctionDao {
   Auction saveAuction(Auction auction);
   Auction findAuctionById(long auction);
   List<Auction> findAllAuctions();
}
```

### Opdracht 4: Een nieuwe veiling aanmaken (auction)

Zorg voor een REST endpoint waarmee een nieuwe veiling aangemaakt kan worden. Een veiling moet ook
adhv zijn primary key opgevraagd kunnen worden. Je krijgt alle biedingen voor de veiling te zien,
maar de volgende voeg je nog extra toe in de DTO: numberOfBids, highestBid en highestBidBy. 
Voorzie ook een REST endpoint waarmee je alle
huidige veilingen kan opvragen.

### Opdracht 5: Biedingen registreren

Voorzie een REST endpoint en implementeer het registreren van biedingen.

POST http://localhost:8080/auctions/rest/auctions/{auctionId}/bids met body
`
{
"email": "sophie@pxl.be",
"price": 22.5
}
`

Zorg dat onderstaand business regels voldaan worden en zorg ook voor de nodige unit testen om je code
te valideren.

- Gooi een exception op indien de auction en/of de user niet bestaan.
- Gooi een exception op indien het nieuwe bod lager is dat het huidige hoogste bod.
- Gooi een exception op indien de user reeds het hoogste bod heeft.
- Gooi een exception op indien de auction reeds afgelopen is.
