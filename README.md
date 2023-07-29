# RCore
Мое ядро для плагинов майнкрафт 1.16.5

Реализовано:
1. Запускаеться как отдельный плагин
2. Все плагины которые используют ядро должны добавлять себя в методе onEnable(): PluginHelper.onEnable(this);
3. Все конфиги реализуют интерфес RConf. Файлы можно открыть через метод ConfigHelper.loadConfigFile(plugin, filepath)
4. Кастомные предметы грузятся командой CustomMaterial.load(this, rConfig);
5. Кастомыне рецепты грузхяться командой CustomRecipe.load(this, rConfig);
6. Кастомные блоки грузяться командой CustomBlock.load(this, rConfig);
7. Встроена база данных ormLite
8. кастомыное меню команд создаеться через разширение абстрактного класа CustomMenuCommand
