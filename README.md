# Protools POC using Activiti
![](https://github.com/Stage2022/protools-activiti/blob/main/images/bpmn.png?raw=true)
### Installation
[Lien vers le deployment](https://protools-activiti.dev.insee.io/)

##### Installation via Docker
```bash
docker pull mailinenguyen/protools-activiti
docker run -d --name protoolsactiviti -p 8080:8080  mailinenguyen/protools-activiti:latest
```
##### Installation manuelle
``` bash
git clone git@github.com:Stage2022/protools-activiti.git
cd protools-activiti
./mvnw spring-boot:run
```
