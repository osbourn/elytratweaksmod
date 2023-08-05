# Elytra Tweaks Mod

Have you ever felt that the Elytra in Minecraft was too powerful and rendered other
transportation systems useless? Several mods resolve this issue by nerfing Elytra
mechanics by making it slower or more expensive to travel long distances (for example,
by removing fireworks). The problem with this solution is that it makes the Elytra
less fun to use because it is now much less capable.

This mod decides to nerf the Elytra in a different way: by making it very *dangerous*.
It is now much easier to die when flying if you aren't paying much attention or make
mistakes. This provides a strong reason to avoid relying on Elytra as your primary
means of transportation, but makes it a very capable tool for those who are able to
master it.

In vanilla Minecraft, you typically won't die when using an Elytra if you even a
bit careful because flying into a wall doesn't do enough damage to kill you in most
circumstances. This mod multiplies that damage by a configurable amount to make flying
close to blocks a much more risky endeavor.

Elytra Tweaks also provides a new landing mechanic which requires special attention to
avoid taking lots of damage and losing armor durability.

I recommend using the [Do a Barrel Roll mod](https://www.curseforge.com/minecraft/mc-mods/do-a-barrel-roll)
mod as well if you are interested in making Elytra something that you have to master.

## Requirements

Elytra Tweaks is currently only available for Fabric 1.20.1. I might port it to other versions
or mod loaders if it is requested, but I can't make any guarantees (you are of course allowed
to do this yourself if you know how).

Elytra Tweaks requires the [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api).
[Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu) and
[Cloth Config](https://www.curseforge.com/minecraft/mc-mods/cloth-config) are highly recommended.

## Features

All features can be disabled in the config.

- Players take increased damage when flying into walls (double damage by default)
- When a player takes damage midair, their elytra will deactivate. They can reactivate
it immediately, but can it require a good reaction time to avoid falling too far.
(Note that some damage sources do not trigger this, like friction damage, which is
explained later.)
- (Disabled by default) When flying too high, the atmosphere gets thin and you start to
lose air. By default, this happens at above y=200, which is about cloud level.
The rate at which you lose air depends on how far you are above y=200. At y=230, you lose
air at 30% of rate as if you were underwater. At y=270, this increases to 70%, and
at y=300 you lose air at the same speed as if you were underwater. Similarly, between
y=150 and y=200, you regain air at a reduced rate depending on your y-level.
- Instead of exiting flight immediately when hitting the ground, you scrape across the ground
and slow down gradually over time.
- Optionally, this friction caused by slowing down when landing can deal damage to the player.
It resets the player's damage immunity when dealing damage, so the player is damaged once
per tick.

## Config

The config is stored in a JSON file, but if you are using Mod Menu and Cloth Config you
will see a config menu. Cloth Config is highly recommended because it shows a description
of each setting.

## Bugs

This is my first mod in a very long time, so there might be a lot of bugs. I'm also not sure
if this conflicts with any major mods, because it uses the `@Redirect` mixin a lot. If you
experience a bug or have a feature request, please let me know in the
[GitHub issue tracker](https://github.com/osbourn/elytratweaksmod/issues).

## License

Elytra Tweaks is licensed under the GNU General Public License, version 3 or later. In short,
this means you can modify and distribute the mod provided you make your new version open source
under the GPLv3. You are allowed to use this in any modpack. Please contact me if you would like
any permissions beyond what is in the GPL.

The source code can be found here: https://github.com/osbourn/elytratweaksmod
