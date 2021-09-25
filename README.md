# SCALA REALTIME BIDDING

### A Project for advert bidding

if a user sends a bid request, and the request matches any of the
available campaigns a bid response will be return 
else No content will be returned

## How TO RUN
  Port 8083 is being used, the port number can be changed in the Main File
 - Ensure Java and scala is installed on your computer Scala 2.13.6 
  precisely 
   
 - Clone the Project
 - Run this Command sbt run Main

## How To Test 
 - RUn Command sbt test

## Routes
-  GET / 8083 is the index route
-  POST /bidrequest (requires a JSON BidRequest Input)

## How To RUN With Docker AND Docker Compose

  
