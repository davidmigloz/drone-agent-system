SET host=192.168.43.100
SET port=1099

java -cp ./target/Supermarket-0.0.1-SNAPSHOT.jar jade.Boot -container -container-name shopGUI -host %host% -port %port% shopGUI:com.davidflex.supermarket.agents.shop.ShopAgentGuiAgent