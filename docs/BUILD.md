# Сборка Jimm by XaTTaB 0.7.0b

## Подготовка

Установите Python 3.9+ и JDK 11–17. Команды `python`, `java` и `javac` должны быть доступны в `PATH`. Все команды ниже выполняются из корня репозитория.

При первом запуске нужен доступ к сети: сценарий скачивает инструменты с проверкой SHA-256. Отдельно устанавливать Ant и Wireless Toolkit для этого способа сборки не требуется.

## Телефон и язык

```sh
python tools/build_source.py
python tools/build_source.py --target MOTOROLA --language EN
python tools/build_source.py --target SIEMENS2 --language UA
```

| Параметр | Значения | По умолчанию |
|---|---|---|
| `--target` | `MIDP2`, `MOTOROLA`, `SIEMENS2` | `MIDP2` |
| `--language` | `RU`, `UA`, `RO`, `EN`, `CZ` | `RU` |
| `--modules` | Список модулей через запятую | `SMILES,TRAFFIC,HISTORY,FILES,PROXY,ANISMILES` |
| `--compile-only` | Только компиляция, без упаковки MIDlet | Выключен |
| `--smile-pack` | `big`, `small`, `animated-big`, `animated-small`, `gif` | `big` |

Результат полной сборки — `dist/source/Jimm-<TARGET>-<LANG>.jar` и соответствующий `.jad`. Промежуточные файлы находятся в `build/source/<TARGET>-<LANG>`.

Стандартный набор `big` — статические смайлы 22×22 из майских сборок. Код поддержки анимации остаётся включённым, как в оригинале. Старые альтернативные ресурсы сохранены: например, `--smile-pack animated-small` выбирает набор из `res/MODULES/ANISMILES_SMALL` и требует модуля `ANISMILES`; `gif` требует `GIFSMILES`. Эти наборы не являются стандартными ресурсами майского JAR.

## Модули

| Модуль | Назначение |
|---|---|
| `SMILES` | Смайлы |
| `ANISMILES` | Анимированные смайлы; используется вместе с `SMILES` |
| `TRAFFIC` | Учёт трафика |
| `HISTORY` | История сообщений |
| `FILES` | Передача файлов |
| `PROXY` | Подключение через прокси |

Например, сборка со смайлами, историей и учётом трафика:

```sh
python tools/build_source.py --modules SMILES,HISTORY,TRAFFIC
```

Препроцессор обрабатывает условия платформ и модулей в `src`. Используются исходные **SiJaPP** и **LangsTask** из `util`, затем ECJ и ProGuard с CLDC preverification. Классы оригинальных JAR в результат не подмешиваются; заглушки vendor API нужны только для компиляции и в MIDlet не включаются.

## Проверки

```sh
python tools/test_source.py --matrix
python tools/audit_source.py
python tools/audit_resources.py
python tools/test_languages.py
python tools/test_packets.py
python tools/test_graphics.py
python tools/audit_graphics.py
python tools/test_filesystems.py
python tools/audit_filesystems.py
python tools/test_light.py
python tools/audit_light.py
python tools/test_convertor.py
```

Первая команда собирает 15 сочетаний платформ и языков, проверяет конфигурации без модулей, выполняет сравнения поведения с майским JAR, сверку ресурсов и графических компонентов трёх платформ. Вторая сравнивает выбранные сигнатуры и инструкции байткода. Третья позволяет отдельно повторить проверку ресурсов уже собранных 15 JAR: файлы сравниваются побайтово, локализации — по восстановленным именам и декодированным значениям. `test_graphics.py` собирает три русских варианта и сравнивает значки, анимации и шрифт Motorola; `--skip-build` использует уже собранные полные RU-варианты. `audit_graphics.py` повторяет отдельную сверку их сигнатур и инструкций.

Серверные списки, удаление себя из списка и авторизация проверяются в общей функциональной серии `server_actions`: [отчёт](../preservation/reports/source-server-actions.json).

Дни рождения, календарные расчёты, контактные поля и реальные RMS-записи эмулятора проверяются серией `birthday`: [отчёт](../preservation/reports/source-birthday.json). Фиксируются часы и границы запуска/ожидания рабочего потока; физическая многопоточность не моделируется.

Сокетное соединение проверяется серией `socket`: [отчёт](../preservation/reports/source-socket.json). Настоящие чтение FLAP, очередь, счётчики и закрытие исполняются на сценарных потоках. `python tools/audit_socket.py` дополнительно сверяет все инструкции и обработчики `close()` на трёх платформах; [отчёт](../preservation/reports/source-socket-bytecode.json).

Запросы/ответы Xtraz, XML-преобразования, приватность и доставка ответа в чат проверяются серией `xtraz`: [отчёт](../preservation/reports/source-xtraz.json). Пакеты исполняются настоящим `ActionListener`; соединение и назначения чата/журнала наблюдаются на тестовых границах.

Диспетчер UI, упаковка аргументов, постановка задач в очередь, уведомления и ветки переподключения проверяются настоящим `RunnableImpl` в серии `runnable`: [отчёт](../preservation/reports/source-runnable.json). Системная очередь, ожидание и вызовы следующих компонентов перехватываются на границе этой серии.

Файловый браузер проверяется в общей функциональной серии. `test_filesystems.py` собирает три RU-варианта и отдельно сравнивает файловые адаптеры, включая JSR75 и собственный API Motorola; `--skip-build` использует уже собранные полные RU-варианты. `audit_filesystems.py` сверяет их сохранившиеся после оптимизации сигнатуры и инструкции. Эти проверки также входят в общую команду `--matrix`.

`test_languages.py` использует уже собранные пять MIDP2-вариантов и свежий отчёт `audit_resources.py`: исполняет настоящий загрузчик языков, проверяет все значения и ошибки словарей. Общая серия `--matrix` запускает его после сверки ресурсов. Загрузчик смайлов и их редактор дополнительно проверяются в общей функциональной серии `emotions`.

`test_convertor.py` использует готовые полные MIDP2-RU классы и сравнивает инициализацию, загрузку ресурса, парсер схем, транслитерацию и fallback регистра. Открытие ресурса и результаты `Character` заданы на тестовой границе; оригинальные алгоритмы исполняются. [Отчёт преобразователя](../preservation/reports/source-convertor.json) входит в общую функциональную серию.

`test_packets.py` использует готовые полные MIDP2-RU классы и исполняет настоящие конструкторы, парсеры и сериализаторы FLAP/SNAC/TLV, включая повреждённые буферы и коды ошибок. Отдельный [отчёт пакетов](../preservation/reports/source-packets.json) входит в функциональную серию `test_source.py`; сетевой вход не выполняется.

`test_light.py` собирает RU-варианты MIDP2 и Motorola и сравнивает контроллер подсветки: аппаратные вызовы, состояние, задачи таймера и первоначальный таймаут. `--skip-build` использует готовые полные RU-сборки, `--seed 10` запускает один начальный таймаут. `audit_light.py` отдельно сверяет сохранившиеся после оптимизации сигнатуры и инструкции. Полная серия подсветки входит в `--matrix`; Siemens не содержит этого контроллера.

Отчёты: [функциональные проверки](../preservation/reports/source-tests.json), [сравнение байткода](../preservation/reports/source-bytecode-comparison.json), [ресурсы/локализации](../preservation/reports/source-resources.json), [загрузчик языков](../preservation/reports/source-language-loader.json), [графические компоненты](../preservation/reports/source-graphics.json) и [их байткод](../preservation/reports/source-graphics-bytecode.json), [файловые адаптеры](../preservation/reports/source-filesystems.json) и [их байткод](../preservation/reports/source-filesystems-bytecode.json), [подсветка](../preservation/reports/source-light.json) и [её байткод](../preservation/reports/source-light-bytecode.json). Область проверок и ограничения приведены в [описании восстановления](SOURCE-RECOVERY.md). Все сочетания модулей и поведение на реальных телефонах не проверены.

## Историческая сборка Ant

В корне сохранён `build.xml`, а в [BUILD-0.6.md](BUILD-0.6.md) — инструкция исходного репозитория для JDK 6, Ant и Wireless Toolkit. Она относится к базе 0.6.0a; актуальная проверенная сборка 0.7.0b описана выше.
