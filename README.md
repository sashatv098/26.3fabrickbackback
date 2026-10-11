# Simple Backpack (Fabric, Minecraft 26.3)

Рюкзак на 27 слотов. ПКМ с рюкзаком в руке открывает интерфейс, содержимое хранится в самом предмете.

Крафт:
```
L L L      L = кожа
L C L      C = сундук
L _ L
```

## Сборка
1. Установите JDK 25.
2. В папке проекта: `gradle wrapper --gradle-version 9.6.0` (один раз), затем `./gradlew build`.
3. Готовый мод: `build/libs/simplebackpack-1.0.0.jar` → в папку mods вместе с Fabric API.
Для запуска из IDE: `./gradlew runClient`.

## Сборка без установки JDK (через GitHub)
Загрузите все файлы проекта (включая папку `.github`) в новый репозиторий на GitHub.
Вкладка **Actions** → сборка запустится сама → откройте последний запуск → внизу **Artifacts** → скачайте `simplebackpack`.
Внутри будет jar. Берите тот, что без `-sources` в названии.
