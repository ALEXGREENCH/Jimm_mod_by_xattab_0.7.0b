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

Манифест и JAD сохраняют историческое имя `J[im]m`, точку входа `jimm.Jimm`, значок `/icon.png`, профиль `MIDP-2.0`, конфигурацию `CLDC-1.0` и атрибуты майских JAR. `MIDlet-Version` — `0.7.0`, `Jimm-Version` — `0.7.0b`. Имя JAR и размер в JAD вычисляются для новой сборки. Метаданные проверяются вместе с ресурсами во всех 15 вариантах; дата 12.05.2010 обозначает целевую ревизию, а не дату нынешнего восстановления.

Исторический Ant-сценарий находится в корне: после настройки SDK и ProGuard используются `ant`, `ant -Dtarget=MOTOROLA -Dlang=EN` и `ant clean`. [Инструкция базы 0.6](BUILD-0.6.md) сохранена без изменений; этот способ сборки текущего дерева отдельно не проверен.

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

Оптимизация ProGuard `code/simplification/string` отключена: она сворачивала явное создание пустого `StringBuffer` и последующее добавление частей пути в другой конструктор. Это позволяет сохранить последовательность операций майских звуковых методов в конечном JAR. Остальные различия компилятора и оптимизатора остаются видимыми в отчётах.

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
python tools/test_request_info_categories.py --all-languages --skip-build
python tools/test_xstatus_catalog.py --all-languages --skip-build
python tools/audit_xstatus.py
python tools/audit_phone_book.py
python tools/test_save_info_locales.py --all-languages --skip-build
python tools/test_sound.py --skip-build
python tools/audit_sound.py
```

Первая команда собирает 15 сочетаний платформ и языков, проверяет конфигурации без модулей, выполняет сравнения поведения с майским JAR, сверку ресурсов и графических компонентов трёх платформ. Вторая сравнивает выбранные сигнатуры и инструкции байткода. Третья позволяет отдельно повторить проверку ресурсов уже собранных 15 JAR: файлы сравниваются побайтово, локализации — по восстановленным именам и декодированным значениям. `test_graphics.py` собирает три русских варианта и сравнивает значки, анимации и шрифт Motorola; `--skip-build` использует уже собранные полные RU-варианты. `audit_graphics.py` повторяет отдельную сверку их сигнатур и инструкций.

Серверные списки, удаление себя из списка и авторизация проверяются в общей функциональной серии `server_actions`: [отчёт](../preservation/reports/source-server-actions.json).

Native окно ввода пароля, его singleton, команды и ветки подключения/отмены проверяются серией `password`: [отчёт](../preservation/reports/source-password.json). Настоящий `TextBox` сохранён; конечные вызовы UI, подсветки, соединения и выхода наблюдаются на границе контроллера.

Native телефонная книга, команды звонка/SMS и ошибки отправки проверяются серией `phone_book`: [отчёт](../preservation/reports/source-phone-book.json). Сравниваются унаследованный исходник и доставляемый оптимизированный контроллер; физические звонки и SMS заменены сценарными границами. `audit_phone_book.py` сверяет четыре сигнатуры на MIDP2/Siemens2 и присутствие класса во всех 15 вариантах: [байткод и платформы](../preservation/reports/source-phone-book-bytecode.json).

Сохранение профиля, сериализация TLV и пакетов, выбранные аккаунты, ответы сервера, таймауты и повторные действия проверяются серией `save_info`: [отчёт](../preservation/reports/source-save-info.json). `test_save_info_locales.py --all-languages --skip-build` сравнивает целые оптимизированные MIDP2 JAR пяти языков: [отчёт локалей](../preservation/reports/source-save-info-locales.json). Соединение принимает настоящий объект пакета на сценарной границе; сериализация затем исполняется отдельно и её ошибки тоже сравниваются. Обе проверки включены в общую серию.

Дни рождения, календарные расчёты, контактные поля и реальные RMS-записи эмулятора проверяются серией `birthday`: [отчёт](../preservation/reports/source-birthday.json). Фиксируются часы и границы запуска/ожидания рабочего потока; физическая многопоточность не моделируется.

Сокетное соединение проверяется серией `socket`: [отчёт](../preservation/reports/source-socket.json). Настоящие чтение FLAP, очередь, счётчики и закрытие исполняются на сценарных потоках. `python tools/audit_socket.py` дополнительно сверяет все инструкции и обработчики `close()` на трёх платформах; [отчёт](../preservation/reports/source-socket-bytecode.json).

Запрос профиля и накопление ответов сервера проверяются серией `request_info`: [отчёт](../preservation/reports/source-request-info.json). Исполняются настоящий парсер, таймауты, обновление контактных полей и RMS. `test_request_info_categories.py --all-languages --skip-build` отдельно сравнивает собственные ключи, коды и подписи интересов пяти полных MIDP2-сборок; [отчёт](../preservation/reports/source-request-info-categories.json). Обе проверки входят в `--matrix`.

Запросы/ответы Xtraz, XML-преобразования, приватность и доставка ответа в чат проверяются серией `xtraz`: [отчёт](../preservation/reports/source-xtraz.json). Пакеты исполняются настоящим `ActionListener`; соединение и назначения чата/журнала наблюдаются на тестовых границах.

`test_xstatus_catalog.py --all-languages --skip-build` исполняет настоящий `ContactItem.setXStatus`, каталог GUID, подписи и значки пяти MIDP2-сборок. Проверяет совместимый GUID статуса «Сердце», повреждённые capabilities, порядок совпадений и реальные алиасы массивов; [отчёт каталога](../preservation/reports/source-xstatus-catalog.json). Эта проверка также входит в `--matrix` и вызывает `audit_xstatus.py`: [байткод каталога трёх платформ](../preservation/reports/source-xstatus-bytecode.json).

Диспетчер UI, упаковка аргументов, постановка задач в очередь, уведомления и ветки переподключения проверяются настоящим `RunnableImpl` в серии `runnable`: [отчёт](../preservation/reports/source-runnable.json). Системная очередь, ожидание и вызовы следующих компонентов перехватываются на границе этой серии.

Файловый браузер проверяется в общей функциональной серии. `test_filesystems.py` собирает три RU-варианта и отдельно сравнивает файловые адаптеры, включая JSR75 и собственный API Motorola; `--skip-build` использует уже собранные полные RU-варианты. `audit_filesystems.py` сверяет их сохранившиеся после оптимизации сигнатуры и инструкции. Эти проверки также входят в общую команду `--matrix`.

`audit_resources.py` требует совпадения буквальных коротких ключей и значений всех пятнадцати словарей; порядок сериализованных записей и их байтовые хеши сообщает отдельно. `test_languages.py` использует уже собранные пять MIDP2-вариантов и свежий отчёт ресурсов: исполняет настоящий загрузчик языков, проверяет все значения и ошибки словарей. Общая серия `--matrix` запускает его после сверки ресурсов. Загрузчик смайлов и их редактор дополнительно проверяются в общей функциональной серии `emotions`.

`test_convertor.py` использует готовые полные MIDP2-RU классы и сравнивает инициализацию, загрузку ресурса, парсер схем, транслитерацию и fallback регистра. Открытие ресурса и результаты `Character` заданы на тестовой границе; оригинальные алгоритмы исполняются. [Отчёт преобразователя](../preservation/reports/source-convertor.json) входит в общую функциональную серию.

`python tools/test_util_core.py --skip-build` использует готовые MIDP2-RU классы и исполняет настоящие методы `Util`: кодировки, байтовые операции, потоки, тарифы, обработку текста и MD5. Проверка включена в общую функциональную серию; [отчёт](../preservation/reports/source-util-core.json) перечисляет проверенные методы и ограничения параметров, специализированных оригинальным ProGuard. Без `--skip-build` сначала собирается MIDP2-RU. `python tools/audit_util.py` отдельно проверяет полный поимённый список 87 методов и 62 полей: [сигнатуры и инструкции](../preservation/reports/source-util-bytecode.json). Все 83 совпадающие оптимизированные сигнатуры также входят в основной `audit_source.py`.

`python tools/test_tariffs.py` после `test_source.py` использует подготовленный стенд Options и проверяет настоящий экран тарифов: начальные значения, ограничения полей, повторное сохранение/открытие, целые стоимости, валюту и сериализацию настроек. [Отчёт формы тарифов](../preservation/reports/source-tariffs.json) сохраняется отдельным шагом CI.

`python tools/test_contact_core.py --skip-build` проверяет настоящие конструкторы и модель ContactItem: упакованные свойства, IP, capability, счётчики, списки приватности, кэш имени и потоки. Проверяются также модификаторы методов и блокировка `hasCapability` монитором контакта. [Отчёт модели контакта](../preservation/reports/source-contact-core.json) сохраняется отдельным шагом CI; без `--skip-build` сначала собирается MIDP2-RU. `python tools/audit_members.py` после основного аудита сверяет классы, дополнительные сигнатуры/поля и `hasCapability` всех трёх RU-платформ: [полный список членов](../preservation/reports/source-member-inventory.json).

`test_packets.py` использует готовые полные MIDP2-RU классы и исполняет настоящие конструкторы, парсеры и сериализаторы FLAP/SNAC/TLV, включая повреждённые буферы и коды ошибок. Отдельный [отчёт пакетов](../preservation/reports/source-packets.json) входит в функциональную серию `test_source.py`; сетевой вход не выполняется.

`test_light.py` собирает RU-варианты MIDP2 и Motorola и сравнивает контроллер подсветки: аппаратные вызовы, состояние, задачи таймера и первоначальный таймаут. `--skip-build` использует готовые полные RU-сборки, `--seed 10` запускает один начальный таймаут. `audit_light.py` отдельно сверяет сохранившиеся после оптимизации сигнатуры и инструкции. Полная серия подсветки входит в `--matrix`; Siemens не содержит этого контроллера.

Отчёты: [функциональные проверки](../preservation/reports/source-tests.json), [сравнение байткода](../preservation/reports/source-bytecode-comparison.json), [ресурсы/локализации](../preservation/reports/source-resources.json), [загрузчик языков](../preservation/reports/source-language-loader.json), [графические компоненты](../preservation/reports/source-graphics.json) и [их байткод](../preservation/reports/source-graphics-bytecode.json), [файловые адаптеры](../preservation/reports/source-filesystems.json) и [их байткод](../preservation/reports/source-filesystems-bytecode.json), [подсветка](../preservation/reports/source-light.json) и [её байткод](../preservation/reports/source-light-bytecode.json). Область проверок и ограничения приведены в [описании восстановления](SOURCE-RECOVERY.md). Все сочетания модулей и поведение на реальных телефонах не проверены.

Журнал действий, его текст/теги/цвета, меню, буфер обмена, счётчик, клавиши и частичные ошибки перехода проверяются общей серией `magic_eye`: [отчёт](../preservation/reports/source-magic-eye.json). Исполняются реальные методы MagicEye/TextList, контактный lookup и создание временного контакта; конечные маршруты и часы захватываются на тестовой границе.

`python tools/audit_inheritance.py` проверяет все абстрактные объявления оригинала и их реальные переопределения, включая правильные роли семи одинаковых методов GroupItem: [отчёт иерархии](../preservation/reports/source-inheritance.json). Проверка использует готовые MIDP2-RU классы и включена в CI.

`python tools/audit_method_graph.py` воспроизводит вывод дополнительных имён по единственным совпадениям полных тел, в порядке подтверждённых зависимостей: [отчёт методов](../preservation/reports/source-method-graph.json). Проверяет также авторские объявления и явные специализации ProGuard; использует готовые MIDP2-RU классы и включён в CI.

`python tools/test_jimm_urls.py` после общей серии проверяет настоящие `JimmUI.gotoURL`, список ссылок и его команды: разбор URL, начертание, переносы, размеры текста, пиксели, выбор ссылки и возврат. Проверка уже включена в `test_source.py`; [отчёт](../preservation/reports/source-jimm-urls.json) сохраняется в CI. Захвачен только конечный вызов `platformRequest`, без открытия браузера; неизменность всего контроллера до перехвата и сохранение остальных инструкций после него проверяются отдельно.

`python tools/test_send_text.py` после общей серии сравнивает настоящий отправитель `JimmUI.sendMessage`, конструкторы сообщений/действий, ID, ошибки очереди, чат/историю и пакеты `Icq.beginTyping`. Проверка включает две реальные блокировки монитором класса и отдельное исполнение оптимизированного JAR. Она уже входит в `test_source.py`; [отчёт](../preservation/reports/source-send-text.json) и [платформенная сверка](../preservation/reports/source-send-text-bytecode.json) сохраняются в CI. Отдельно воспроизводится `python tools/audit_send_text.py`: сигнатуры, пределы частей, условия подсветки и статические вызовы typing во всех трёх RU-вариантах.

`python tools/test_editor.py` после общей серии сравнивает настоящие методы редактора `JimmUI`: заголовок и страницы, вставку длинного текста, команды, ограничения, создание и повторное открытие. Сравниваются авторские классы и оптимизированный JAR; [отчёт](../preservation/reports/source-editor.json) включён в общую серию и CI. Конечные переключение дисплея, GC, подсветка и typing захватываются; команды, listener и constraints делегируются настоящему `TextBox`. `python tools/audit_editor.py` проверяет собственные личности редактора, порядок команд и условия SiJaPP трёх RU-платформ: [платформенная сверка](../preservation/reports/source-editor-bytecode.json).

## Историческая сборка Ant

В корне сохранён `build.xml`, а в [BUILD-0.6.md](BUILD-0.6.md) — инструкция исходного репозитория для JDK 6, Ant и Wireless Toolkit. Она относится к базе 0.6.0a; актуальная проверенная сборка 0.7.0b описана выше.


Порядок подсветки, таймера автостатуса, реакции и callback для трёх платформ проверяется `python tools/test_key_routing.py --skip-build`. Та же команда проверяет обработку ошибок `Util.writeByteArray`; [отчёт](../preservation/reports/source-key-routing.json) сохраняется в общей матрице и CI. Границы этой проверки описаны в [восстановлении исходников](SOURCE-RECOVERY.md).
