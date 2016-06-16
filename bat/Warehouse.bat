SET host=192.168.43.100
SET port=1099
SET wname=%1
SET x=%2
SET y=%3

java -cp ./target/Supermarket-0.0.1-SNAPSHOT.jar jade.Boot -container -container-name %wname% -host %host% -port %port% %wname%:com.davidflex.supermarket.agents.shop.WarehouseAgent(shop,%x%,%y%)