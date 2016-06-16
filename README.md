# Supermarket agent system

Projects for Agent Systems and Applications at Warsaw University of Technology.

![Screenshot](https://raw.githubusercontent.com/davidmigloz/supermarket-agent-system/master/src/main/resources/img/screenshot.jpg)

#### Team 
- **Constantin Grinda**
- **David Miguel**

#### Presentations

- [1º presentation](https://drive.google.com/open?id=0B2aEyRV9M0lJTkpjOTJSaGJ6d1E)
- [2º presentation](https://drive.google.com/open?id=12V8Tjkx-c9VSXX1L477lBETtkwmw4xSInk_B-8T2T2s)
- [3º presentation](https://drive.google.com/open?id=1DdmBigvGwiWSKQNg1pgzymJv6_ncaHGVgLyAK2GzOmM)
- [Final presentation](https://drive.google.com/open?id=1hLi-I9B4hDYvsHSdTCRLf1VoHezDzqg9uBuCVAfICds)

#### Download

> [supermarket-agent-system.v0.jar](https://github.com/davidmigloz/supermarket-agent-system/releases/download/v0/supermarket-agent-system.v0.jar)

#### Run 

##### 1º Run JADE platform:
```
java -cp supermarket-agent-system.v0.jar jade.Boot -gui
```

##### 2º Run Shop
*Set `host` and `port` where JADE is running.
```
java -cp supermarket-agent-system.v0.jar jade.Boot -container -container-name shopGUI -host %host% -port %port% 
shopGUI:com.davidflex.supermarket.agents.shop.ShopAgentGuiAgent
```

##### 3º Add warehouses:
*Set `host` and `port` where JADE is running.
*Set `wmane` warehouse name and `x`, `y` its location.
```
java -cp supermarket-agent-system.v0.jar jade.Boot -container -container-name %wname% -host %host% -port %port%
%wname%:com.davidflex.supermarket.agents.shop.WarehouseAgent(shop,%x%,%y%)
```

##### 4º Run client:
*Set `host` and `port` where JADE is running.
```
java -cp supermarket-agent-system.v0.jar jade.Boot -container -container-name customerGUI
customerGUI:com.davidflex.supermarket.agents.customer.CustomerGuiAgent -host %host% -port %port%
```
