# SMP Expansion

This plugin adds some basic SMP features and makes some gameplay modifications to vanilla survival Minecraft with the goal of improving game balance and the gameplay experience in general.  It is not intended to be used in servers with mods or plugins that also modify gameplay.

## Weapons, Tools, and Armor
Various changes have been made to equipment and enchantments to improve game balance:
* Most items have modest durability increases, but gold equipment has a more significant durability increase.
* Gold equipment also has exclusive enchantments geared towards unique use cases. (currently planned)
* Damage of some weapons has been tweaked slightly.
* Spear charge attacks and Mace smash attacks are calculated differently. Their damage now increases logarithmically to prevent these weapons from dealing too much damage.
* Bows and Crossbows fired by players are reworked to deal consistent damage.
* Projectiles no longer give damage immunity to targets.  This makes Multishot crossbows and harming arrows more effective.
* Some enchantments have been changed:
  * Protection has been reworked to only protect against damage sources not defended against by armor, such as magic damage.  Damage sources that bypass the Resistance effect will still bypass Protection.  To compensate, its damage reduction has been doubled to 8% per level.
  * Reinforcement is a new enchantment that only protects against damage sources defended against by armor.  Its damage reduction is 4% per level.
  * Projectile Protection has been removed due to it not having a good use case.
  * Smite, Bane of Arthropods, and Impaling now provide half the damage increase of Sharpness against all targets.  Against their specific targets, they provide a total of 3 times the damage boost of Sharpness.
  * Power can now be applied to Crossbows.
* Equipment created before this plugin is installed on a server will be converted.  They will keep their damage (durability lost), enchantments, and cosmetic modifications.
  * Due to the removal of Projectile Protection, items with this enchantment will have it replaced with Protection.  Items that already have the vanilla Protection enchtantment will have the enchantment replaced with Reinforcement.
 
Read the full changes here.

## Enchanting and Anvils
Enchanting has been reworked to remove some randomness:
* Enchantments are instead selected and applied invididually.
  * The cost (level cost, level requirement, and lapis lazuli cost) depends on the level of the enchantment.
* At first, most enchantments and their higher levels are locked.  Unlock these by gaining enchantment XP.
  * Enchantment XP is obtained by grindstoning enchantments from any item.
* Books can still be enchanted with random enchantments.  This is a good way to obtain enchantment XP or try your luck.  Read more about it here.
* Bookshelves no longer have an effect on enchanting tables.

Read more about the new enchanting mechanics here.

Anvils have also been reworked:
* More options have been added to renaming items.  It is now possible to add color and formatting options.
* Anvils gain a new option to upgrade the tiers of equipment.  This is done by sacrificing a piece of equipment of the next tier.  The total durability damage of both items will be combined.  This is useful for preserving enchantments or other customizations on lower tier items.
* Anvil XP cost has been reworked.  It is now only dependent on the current operation, not any previous operations.  This also means that operations will never become "too expensive".

Read more about the new anvil mechanics here.

## Smithing
Netherite swords, axes, and armor can now be upgraded further:
* For swords and axes, this adds a little bit of damage.
* For armor, this adds a little bit of armor toughness, increasing damage resistance against high incoming damage.
* All items receive a small durability increase.
* Each upgrade is done by using a Netherite Upgrade template plus one of the following items:
  * Nether Star to upgrade to level 2
  * Conduit to upgrade to level 3
  * Enchanted Golden Apple to upgrade to level 4
  * Heavy Core to upgrade to level 5

Applying armor trims no longer consumes the trim template, but using a Netherite Upgrade template still consumes it.

Read more about the new smithing mechanics here.

# Villager Trading
Villager trading has been changed to make it easier to get desired trades.
* Each villager with a profession now has a type.  This is randomly chosen whenever it gains this profession.  The type determines all of the villager's trades that will be unlocked in the future.  To reroll the type, just reroll the villager's profession.
* The trades themselves are mostly the same as from vanilla, with a few changes:
  * Librarians will only sell low-level enchantments at Novice level.  To get the higher level enchantments, the villager must be leveled up. 
  * Mending and other treasure Enchantments are no longer sold by librarian villagers.  It is still possible to repair equipment forever by using an anvil due to the rework of anvil XP cost.
  * Stone masons now sell sand to address the issue of sand non-renewability in some worlds.
  * All trades start at a fixed price, and all enchantments on sold items are predetermined.

Read about the new villager trades here.

# SMP Features
These features are useful if you plan to play with friends or strangers.  If you as the server owner wish to enforce certain settings across all players, you can configure them in the plugin config.
* Players can set their personal game difficulty, which affects the damage taken from mobs:
  * Easy: 30% less damage
  * Normal (default): No modifier
  * Hard: 30% more damage
  * Extra Hard: 60% more damage
* Players can toggle Keep Inventory, as well as what parts of their inventory are actually saved.  The categories are: Armor, Hotbar & Offhand, Inventory, and XP.
* PvP is off by default but can be toggled on:
  * Only players who have turned PvP on can fight each other.  Note however that this may not prevent players from indirectly damaging each other.
  * Combat tagging can be enabled in the config.  This prevents players from toggling PvP off for 30 seconds after they are involved in combat.  Logging out during this time will result in automatic player death.
* A server currency, backed by diamonds, is introduced:
  * 1 diamond exchanges for $100.
  * Players can withdraw banknotes as physical items to make transactions.
* Players can form teams with their friends and allies.
  * As of now, the primary function of teams is to enable/disable PvP between teammates and to control claim permissions.
* Players can now claim chunks to protect their builds from other players.
  * As of now, only the Overworld can be claimed.
  * This plugin uses a system of "outposts" and "claims" where outposts are more expensive but can be placed anywhere, and claims are cheaper but must be connected to an outpost.  
  * This makes it easier to claim a large contiguous amount of land for a large build while discouraging players from scattering random claims all across the world.
  * Players start with 1 outpost and 24 claims, and can buy more for $100 per claim and $1000 per outpost.  The maximum number of outposts a player can own is 21.
  * Within a claim, the owner can configure permissions for teammates and other players.  For instance, they can prevent other players from breaking blocks, opening chests, or opening doors.

Read about these features here.

