# Unplugged AFK for Forge

An LGPL-3.0 Forge port of
[Sakura-Ryoko's Unplugged-AFK](https://github.com/sakura-ryoko/unplugged-afk).
It lets a player disconnect while a server-side replacement remains loaded at
their farm. No client installation is required.

## Supported version

| Minecraft | Forge | Java |
| --- | --- | --- |
| 1.20.1 | 47.4.0 (47.x) | 17 |

Install the JAR on the Forge server. Players do not need the mod client-side.
Build and launch the development environment with Java 17.

## Commands

- `/unplug [minutes] [reason]` leaves an offline replacement and disconnects
  the caller. The integrated-server owner cannot use it.
- `/afk` is an optional alias, disabled by default.
- `/unplug status` shows the caller's automatic-unplug preference.
- `/unplug next [minutes]` arms only the caller's next logout.
- `/unplug auto on [minutes]` enables persistent opt-in automatic unplug;
  `/unplug auto off` disables it.
- `/unplug cancel` cancels an armed or delayed automatic transition.
- `/unplugged-admin info [player]`
- `/unplugged-admin list`
- `/unplugged-admin save|reload|purge`
- `/unplugged-admin spawn <player> [minutes] [reason]`
- `/unplugged-admin kick <player>`
- `/unplugged-admin set <section.option> <value>` can update any primitive
  configuration field when `main.advancedAdminOptions` is enabled. Command
  suggestions list the available dotted option names; use `&` for color codes.

Configuration is generated at `config/unplugged_afk.json`. Active and ended
session state is stored in `<world>/unplugged_afk_sessions.json`; normal
Minecraft playerdata remains authoritative for inventory, effects, position,
dimension, game mode and other entity state.

Access can be controlled with `access.mode` (`EVERYONE`, `ALLOWLIST`, or
`DENYLIST`) and UUIDs in `access.players`. Names are accepted for convenience,
but UUIDs are recommended. Operators bypass access and duration limits by
default. `unplugged.maximumUnpluggedTimeout` caps a normal player's requested
duration, and `unplugged.maximumSimultaneousPlayers` protects the server from
too many representatives.

Automatic logout behavior is controlled by `automatic.mode`: `DISABLED`,
`OPT_IN` (the default), or `EVERYONE`. A normal logout is converted only after
`automatic.delaySeconds`, so a quick reconnect cancels it. Pending automatic
sessions are discarded during server shutdown. `/unplug next` is one-shot;
`/unplug auto on` preferences persist in the server config.

When FTB Ranks is installed, the optional integration recognizes
`unplugged_afk.use`, `unplugged_afk.admin`, `unplugged_afk.auto`,
`unplugged_afk.bypass_duration_limit`, `unplugged_afk.bypass_session_limit`,
and the numeric `unplugged_afk.duration.max` node. The legacy combined
`unplugged_afk.bypass_limits` node remains supported. Explicit FTB Ranks values take
priority over the access list; the mod has no required FTB dependency.

Offline replacements show `PlayerName [UNPLUGGED]` above their head and in the TAB
list by default. Set `unplugged.showAfkNameplate` or
`unplugged.showAfkInTabList` to `false` to disable either indicator, and change
the `label.unplugged_afk.afk` entry through a resource pack to customize the
label. This deliberately distinguishes a disconnected representative from an
ordinary, still-connected AFK player. The existing
`unplugged.unpluggedHidePlayer` option still takes
precedence and hides both.

All player-facing text uses translation keys from
`assets/unplugged_afk/lang/en_us.json`; the server config contains behavior
and formatting options only.

## Build

Check out the desired branch and run:

```powershell
.\gradlew.bat clean build
```

The distributable JAR is written to `build/libs`. On Linux/macOS, use
`./gradlew clean build` instead. The first build downloads and prepares the
matching Minecraft and Forge development artifacts.

For a dedicated-server development launch:

```powershell
.\gradlew.bat runServer
```

Accept the EULA in the generated versioned run directory first (for example,
`run-1.20.1/eula.txt`). Dedicated-server testing
is strongly recommended because fake players exercise login, playerdata and
chunk-tracking code that a client-only launch does not cover.

### IntelliJ setup

Set IntelliJ's Gradle JVM and Minecraft run configuration JRE to Java 17.
After importing or changing JDKs, reload the Gradle project and generate run
configurations with:

```powershell
.\gradlew.bat genIntellijRuns
```

## Implementation notes

- Replacements are real `ServerPlayer` subclasses with an inert embedded
  connection, so vanilla farms and chunk tracking see a player entity.
- The real player is saved before replacement. Vanilla playerdata is loaded
  for both newly created and restart-restored replacements.
- Timeout, death, administrator removal and real-player reconnection transition
  the persisted session state and notify registered API listeners.
- Visibility mode removes hidden replacements from both entity tracking and
  the player-info list, with a separate operator exception.

## CurseForge listing

The ready-to-paste CurseForge project page copy is in
[`metadata/curseforge-description.md`](metadata/curseforge-description.md).

Upload the Forge JAR from `build/libs` and select Minecraft 1.20.1 and Forge.


## License and attribution

This project and the upstream-derived work are licensed under LGPL-3.0. See
`LICENSE` and `NOTICE`. This port is independent and is not endorsed by the
original author or Forge.
