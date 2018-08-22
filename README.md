# testexercise
Требования для запуска:
-java 8
-maven
-nodejs v.6 и выше
-angular-cli

Запуск backend:
mvn clean install
mvn spring-boot:run

Запуск frontend:
Из папки src/frontend:
ng serve --proxy-config proxy.conf.local.json --port=3000