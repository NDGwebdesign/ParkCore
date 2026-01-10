# ParkCore

**ParkCore** is a lightweight Minecraft Bukkit/Spigot plugin for managing theme-park features (attractions, warps, ranks and admin menus).

---

## 🔧 Features

- Attractions management
  - Create/delete attractions with region support
  - Maintain attraction status: OPEN / CLOSED / MAINTENANCE
  - Attraction status signs (colored sign text showing name + status)
  - Create optional RideOperate control panels via chat confirmation (if RideOperate plugin is present)
- Warps
  - Create and remove warps automatically for attractions
  - Command-based warp management and usage permissions
- Rank system and permissions
  - Manage ranks, prefixes, suffixes and inheritance via YAML (`Ranks/ranks.yml`)
  - GUI-based Rank Manager and Permission management
  - Permission search and filter UI (search by plugin or query)
- Admin & Player GUIs
  - Park menu (configurable via `Menu/menu.yml`)
  - Admin menu with Rank Manager and Permission Manager shortcuts
- Internal API server (optional) for integrations
- Internationalization-ready: user-facing strings are in English (config/menu text supports color codes and & → § translation)

---

## 📁 Files & Configuration

- plugin.yml — plugin metadata & permission nodes
- config.yml — general configuration
- attractions.yml — attractions data (regions, status, location)
- warps.yml — warp data
- Menu/menu.yml — inventory GUIs (items, slots, titles)
- Ranks/ranks.yml — ranks, prefixes, permissions
- Ranks/players.yml — player → rank mappings

Tip: the `Menu/menu.yml` is used to build the in-game Park menu; change item names / lore to customize the UI.

---

## ✅ Permissions

Important permissions (from `plugin.yml`):

- `parkcore.admin` — admin parent permission (default: op)
- `parkcore.visitor` — default visitor permission (default: true)
- `parkcore.gamemode` — parent for gamemode permissions (default: op)

Specific permissions:
- `parkcore.warp.use` — use warps
- `parkcore.warp.create` — create warps
- `parkcore.warp.delete` — delete warps
- `parkcore.rank.gui` — open Rank GUI
- `parkcore.rank.create` — create ranks
- `parkcore.rank.delete` — delete ranks
- `parkcore.rank.permission` — manage rank permissions
- `parkcore.admin.gui` — open Admin GUI
- `parkcore.gm.gms` / `gmc` / `gma` / `gmsp` — gamemode commands

---

## 🛠 Commands & Usage

ParkCore uses a central `/parkcore` command with subcommands (also tab-completed). Common subcommands:

Attractions (under `/parkcore att`):
- `/parkcore att create <region> <name>` — create a new attraction (saves location + creates warp)
- `/parkcore att delete <name>` — delete attraction (or `/parkcore att delete <region> <name>`)
- `/parkcore att status <attraction> <open|closed|maintenance>` — set the attraction status

Ranks (under `/parkcore rank`):
- `/parkcore rank create <name>` — create rank
- `/parkcore rank delete <name>` — delete rank
- `/parkcore rank set <player> <rank>` — set player's rank
- `/parkcore rank info <player>` — show player's rank info
- `/parkcore rank gui <player>` — open Rank GUI for a player
- `/parkcore rank perm add <rank> <permission>` — add permission to rank
- `/parkcore rank perm remove <rank> <permission>` — remove permission from rank

Warps:
- `/warp` — warp system (usage and subcommands provided by `WarpCommand`)

Gamemode shortcuts (permissions required):
- `/gms` — survival
- `/gmc` — creative
- `/gma` — adventure
- `/gmsp` — spectator

GUI interactions:
- Park menu: open via config-defined item, click items to open attractions, audio menu, admin GUI, etc.
- Rank GUI: set rank, right-click to open permission manager
- Permission GUI: toggle permission on/off, filter by plugin or search

---

## 🧭 Attraction status signs

- When creating an attraction sign it will use the following lines (colors supported):
  - `[ParkCore]`
  - `Attraction` (or custom menu text)
  - Attraction name
  - Colored status (OPEN / CLOSED / MAINTENANCE)

Signs are updated automatically when the attraction status changes.

---

## 🔗 RideOperate integration

If the RideOperate plugin is present, ParkCore can create a RideOperate panel during attraction creation via a chat confirmation (YES/NO). This is optional and only triggers when the hook is detected.

---

## 🧪 Build / Install

Requirements:
- Java JDK (matching your server)
- Maven

Build:
```bash
mvn -DskipTests package
```

Install:
1. Copy the generated `ParkCore.jar` into your server's `plugins/` folder.
2. Start the server to generate default config files in `plugins/ParkCore/`.
3. Adjust `Menu/menu.yml`, `attractions.yml`, `Ranks/ranks.yml` as needed and reload.

---

## 📝 Notes for server admins

- Strings in `Menu/menu.yml` support color codes — use `&` in files (they are replaced at load time).
- After editing YAML config files, use the plugin reload mechanism (or restart server) to apply changes.
- Permissions described in `plugin.yml` determine which GUI entries and commands are available to a player.

---

## 📦 Development & Contributing

- Project structure follows standard Maven layout under `src/main/java` and `src/main/resources`.
- Please run `mvn package` and test on a dev server (Paper/Spigot compatible) before making PRs.

---

## ❓ Questions / Missing items

If you'd like the README to include screenshots of GUIs, examples of menu.yml entries, or a suggested `plugin.yml` roles table, tell me which you'd prefer and I will add them.

---

*Authors:* FriendsparkMC, NDG-webdesign

*License:* (not specified) — add a `LICENSE` file to indicate desired license.
