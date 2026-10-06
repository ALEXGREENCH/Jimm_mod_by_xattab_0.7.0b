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
python tools/test_graphics.py
python tools/audit_graphics.py
```

Первая команда собирает 15 сочетаний платформ и языков, проверяет конфигурации без модулей, выполняет сравнения поведения с майским JAR, сверку ресурсов и графических компонентов трёх платформ. Вторая сравнивает выбранные сигнатуры и инструкции байткода. Третья позволяет отдельно повторить проверку ресурсов уже собранных 15 JAR: файлы сравниваются побайтово, локализации — по восстановленным именам и декодированным значениям. `test_graphics.py` собирает три русских варианта и сравнивает значки, анимации и шрифт Motorola; `--skip-build` использует уже собранные полные RU-варианты. `audit_graphics.py` повторяет отдельную сверку их сигнатур и инструкций.

Отчёты: [функциональные проверки](../preservation/reports/source-tests.json), [сравнение байткода](../preservation/reports/source-bytecode-comparison.json), [ресурсы/локализации](../preservation/reports/source-resources.json), [графические компоненты](../preservation/reports/source-graphics.json) и [их байткод](../preservation/reports/source-graphics-bytecode.json). Область проверок и ограничения приведены в [описании восстановления](SOURCE-RECOVERY.md). Все сочетания модулей и поведение на реальных телефонах не проверены.

## Историческая сборка Ant

В корне сохранён `build.xml`, а в [BUILD-0.6.md](BUILD-0.6.md) — инструкция исходного репозитория для JDK 6, Ant и Wireless Toolkit. Она относится к базе 0.6.0a; актуальная проверенная сборка 0.7.0b описана выше.
