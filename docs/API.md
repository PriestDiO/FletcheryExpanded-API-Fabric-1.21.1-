# Fletchery Expanded — API для разработчиков модов

**Русский** | [English](API.en.md)

Этот документ описывает, как зарегистрировать собственные компоненты стрел (перья, древки,
наконечники, эффекты) из другого мода, чтобы они появились в верстаке лучника, в JEI/REI и
получили собственную текстуру.

Рабочий пример полностью настроенного аддона лежит в [`examples/example-addon`](../examples/example-addon).

## Как это работает

Fletchery Expanded ищет entrypoint `fletchery_component` в `fabric.mod.json` каждого установленного
мода. Когда регистр компонентов инициализируется, для каждого найденного entrypoint вызывается метод
`onRegisterArrowComponents()`, внутри которого нужно зарегистрировать свои компоненты через
`FletcheryComponentRegistry`.

Слот (`FEATHER`, `SHAFT`, `TIP`, `EFFECT`) определяет, в какую из четырёх ячеек верстака лучника
попадёт компонент.

## Шаг 1. Добавь зависимость

Добавь Fletchery Expanded как compile-time зависимость (Maven-репозиторий/Jar укажи по своему
способу распространения, например через Modrinth Maven или локальный jar в `libs/`):

```gradle
repositories {
    maven { url = "https://api.modrinth.com/maven" }
}

dependencies {
    modImplementation "maven.modrinth:fletchery-expanded:<version>"
}
```

## Шаг 2. Зарегистрируй entrypoint

В `fabric.mod.json` своего мода добавь:

```json
{
  "entrypoints": {
    "fletchery_component": [
      "com.example.fletcheryaddon.MyFletcheryComponents"
    ]
  }
}
```

## Шаг 3. Реализуй `FletcheryComponentInitializer`

```java
package com.example.fletcheryaddon;

import com.fletchery.mod.api.FletcheryComponentInitializer;
import com.fletchery.mod.api.FletcheryComponentRegistry;
import com.fletchery.mod.api.FletcheryComponentRegistry.Slot;

public class MyFletcheryComponents implements FletcheryComponentInitializer {
    @Override
    public void onRegisterArrowComponents() {
        FletcheryComponentRegistry.register(Slot.TIP, new RubyArrowTip());
    }
}
```

## Шаг 4. Реализуй `ArrowComponent`

```java
package com.example.fletcheryaddon;

import com.fletchery.mod.api.ArrowComponent;
import com.fletchery.mod.arrow.ArrowProperties;
import com.fletchery.mod.config.ModConfig;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class RubyArrowTip implements ArrowComponent {

    @Override
    public Identifier id() {
        // уникальный идентификатор компонента, обычно = registry id предмета
        return Identifier.of("example_addon", "ruby");
    }

    @Override
    public Item item() {
        // предмет, который игрок кладёт в слот верстака, чтобы использовать этот компонент
        return MyItems.RUBY;
    }

    @Override
    public String modelFile() {
        // имя файла модели наконечника (без расширения), см. раздел "Модели и текстуры"
        return "ruby_tip";
    }

    @Override
    public void apply(ArrowProperties props, ModConfig cfg) {
        // здесь настраиваются характеристики итоговой стрелы
        props.bonusDamage += 3;
        props.armorPiercing = true;
    }
}
```

### Поля `ArrowProperties`

`ArrowProperties` — это набор публичных полей (урон, скорость, гравитация, поджиг, эффекты зелий,
пробивание брони, взрыв и т.д.), которые можно изменять в `apply()`. Полный и актуальный список полей
смотри в исходниках: `com.fletchery.mod.arrow.ArrowProperties`. Компоненты применяются последовательно
для всех четырёх слотов, так что можно как добавлять к существующим значениям (`+=`), так и
устанавливать флаги (`= true`).

## Модели и текстуры

Для каждого компонента нужна текстура/модель предмета (для отображения в инвентаре и в 3D-превью
верстака) — регистрируй её обычным для Fabric способом (resource pack твоего мода,
`assets/<namespace>/models/item/...`, `assets/<namespace>/textures/item/...`).

`modelFile()` используется системой Fletchery для генерации составной модели итоговой стрелы —
убедись, что указанное имя соответствует ресурсам, которые предоставляет твой мод.

## Регистрация и порядок инициализации

- Все обращения к `FletcheryComponentRegistry` (кроме самой регистрации через entrypoint) вызывают
  `ensureReady()`, которая лениво инициализирует реестр при первом использовании — вручную вызывать
  её не нужно.
- Регистрация одного и того же `id()` дважды выбросит `IllegalStateException` — идентификаторы
  должны быть уникальны в пределах слота.
- Не регистрируй компоненты вне `onRegisterArrowComponents()` (например, в статическом
  инициализаторе класса) — это может привести к состоянию гонки при старте игры.

## JEI / REI

Компоненты, зарегистрированные через API, автоматически попадают в комбинации, которые показывает
JEI/REI интеграция мода — отдельная регистрация рецептов не требуется.

## Совместимость с версиями

API стабилен в пределах мажорной версии мода (см. `CHANGELOG.md`). Ломающие изменения интерфейса
`ArrowComponent`/`ArrowProperties` будут анонсированы отдельно и сопровождаться миграционным гайдом.

## Вопросы

Если что-то не работает или не хватает описания — открой [issue](../../issues) в этом репозитории.
