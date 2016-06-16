SET host=192.168.229.1
SET port=1099

java -cp ./target/Supermarket-0.0.1-SNAPSHOT.jar jade.Boot -container -container-name customerGUI customerGUI:com.davidflex.supermarket.agents.customer.CustomerGuiAgent -host %host% -port %port%