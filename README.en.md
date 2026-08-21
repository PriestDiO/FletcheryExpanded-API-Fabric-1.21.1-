# Fletchery Expanded

[Русский](README.md) | **English**

Fletchery Expanded is a Minecraft mod (Fabric, 1.21.1) that turns the fletching table into a full
arrow-crafting workshop.

Combine 4 arrow components — feather, shaft, tip, and effect — to craft one of **thousands** of unique
arrows, each with its own model, stats, and behavior.

- Custom fletching table GUI with a 3D arrow preview
- Full JEI / REI integration (shows every available combination)
- Cloth Config settings screen
- Flexible balance configuration (config file)
- Open API for adding your own components from other mods

## Installation

1. Install [Fabric Loader](https://fabricmc.net/) and [Fabric API](https://modrinth.com/mod/fabric-api)
2. Download the mod from [Modrinth](https://modrinth.com/mod/fletchery-expanded) or from the [Releases](../../releases) page of this repository
3. Put the `.jar` file into your `mods` folder

Requirements: Minecraft 1.21.1, Java 21+.

## For players

Open the fletching table, place components into the four slots — feather, shaft, tip, effect — and get
an arrow. You can browse the full list of available components and their effects in-game via JEI/REI.

## For mod developers (API)

Fletchery Expanded exposes an open API that lets other mods register their own feathers, shafts, tips,
and effects, which are automatically integrated into crafting, JEI/REI, and get their own model.

Full guide: **[docs/API.en.md](docs/API.en.md)**

Working addon example: **[examples/example-addon](examples/example-addon)**

## Localization

The mod is translated into 10 languages: Russian, English, Spanish, French, Hindi, Indonesian,
Kazakh, Portuguese, Ukrainian, Belarusian, Simplified Chinese.

Want to add your own translation? See [CONTRIBUTING.md](CONTRIBUTING.md).

## Building from source

```bash
git clone https://github.com/<your-username>/fletchery-expanded.git
cd fletchery-expanded
./gradlew build
```
The built jar will appear in `build/libs/`.

## License

Licensed under [MIT](LICENSE).

## Contributing

Pull requests, bug reports, and balance suggestions are welcome — see [CONTRIBUTING.md](CONTRIBUTING.md).
