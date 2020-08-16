To run:
```
javac -classpath lib\jade.jar -d classes bidding\behaviours\*.java bidding\biddingOntology\*.java bidding\utils\*.java bidding\*.java
java -cp lib\jade.jar;classes jade.Boot -gui -agents TR1:Bidder;TR2:Bidder;TR3:Bidder
```