# MMOitemsADDON

[![](https://jitpack.io/v/apo2073/MMOitemsADDON.svg)](https://jitpack.io/#apo2073/MMOitemsADDON)

---

### (soft)Depend
* [ApoLib](https://github.com/apo2073/ApoLib)
* [PlaceHolder](https://github.com/PlaceholderAPI/PlaceholderAPI)
* [Skript](https://skunity.com/downloads)

__how to use__
```gradle
	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
	dependencies {
	        implementation 'com.github.apo2073:MMOitemsADDON:Tag'
	}
```

__Example__
* java
```java
public ItemStack test(ItemStack item /*mmoitem*/) {
    MMoAddon mma=new MMoAddon(item);
    JsonArray json=mma.getAbilityToJSon("FIREBOLT", "SNEAK"); // ability to jsonArray
    mma.removeAbilities("FIREBALL"); // remove ability on item
    mma.addAbilities("FIREBOLT", "SNEAK"); // add ability to mmoitem
    return mma.getItem; // return modified item
}
```

* skript
```yaml
command /aa:
    trigger:
        give a skill book of "FIREBOLT" on "RIGHT_CLICK" with "cooldown: 10, mana: 5" to player
```
