## About 

Command line tool that retrieves daily currency rates from Bank of Lithuania website http://www.lb.lt.

## How to build

Execute command:

    mvn package

## How to run 

Execute command: 
    
    java -jar currencyRatesRetriever-0.0.1-SNAPSHOT-jar-with-dependencies.jar

## Notes

for Saturdays, Sundays, New Year’s Day (1 January), Good Friday, Easter Monday, Labour Day (1 May), and Christmas (25 and 26 December)
program retrieves rates of last updated dates.

Supported currencies can be found in: 

https://www.lb.lt/lt/kasdien-skelbiami-euro-ir-uzsienio-valiutu-santykiai-skelbia-europos-centrinis-bankas