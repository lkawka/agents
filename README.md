To run:
```
javac -classpath lib\jade.jar -d classes bidding\behaviours\*.java bidding\biddingOntology\*.java bidding\utils\*.java bidding\agents\*.java bidding\*.java
java -cp lib\jade.jar;classes jade.Boot -gui -agents TR1:agents.TR;TR2:agents.TR;TR3:agents.TR
```