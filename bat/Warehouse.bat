SET host=192.168.229.1
SET port=1099
SET wname=%1
SET x=%2
SET y=%3

java -cp ./target/Supermarket-0.0.1-SNAPSHOT.jar jade.Boot -container -container-name %wname% %wname%:com.davidflex.supermarket.agents.shop.WarehouseAgent(shop,%x%,%y%) -host %host% -port %port%