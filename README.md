# BioRaznoobrazie
Проект по кроссплатформенной разработке на тему "Биоразнообразие"

Проект уже изначально собирается под 3 таргета: веб, андроид, десктоп

./gradlew :desktopApp:run - запуск на десктоп
./gradlew :webApp:wasmJsBrowserDevelopmentRun - запуск на браузер
./gradlew :androidApp:assembleDebug - запуск на андроид

python tools/check-strings.py - проверка локализации