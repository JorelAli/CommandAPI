# TODO

## Things to do before releasing 2.1

* Create trailer!!!!! ~
* Document LootTable
* Document SoundArgument
* Document AdvancementArgument
* Document RecipeArgument
* Update arguments.md with new list of arguments

Bunch of code to help with documenting:

```java

		LinkedHashMap<String, Argument> args = new LinkedHashMap<>();
		args.put("sound", new SoundArgument());
		CommandAPI.getInstance().register("dosound", args, (s, a) -> {
			Sound sound = (Sound) a[0];
			Player player = (Player) s;
			player.getWorld().playSound(player.getLocation(), sound, 100.0f, 1.0f);
		});
		
		args.clear();
		args.put("lt", new LootTableArgument());
		CommandAPI.getInstance().register("dlt", args, (s, a) -> {
			LootTable t = (LootTable) a[0];
			Player player = (Player) s;
			t.fillInventory(player.getInventory(), new Random(), new LootContext.Builder(player.getLocation()).lootedEntity(player).lootingModifier(3).luck(0.0f).build());
		}); 
		
		args.clear();
		args.put("ad", new AdvancementArgument());
		CommandAPI.getInstance().register("gadv", args, (s, a) -> {
			Advancement ad = (Advancement) a[0];
			Player player = (Player) s;
			ad.getCriteria().forEach(c -> {
				player.getAdvancementProgress(ad).awardCriteria(c);
			});
		});
		
		args.clear();
		args.put("rec", new RecipeArgument());
		CommandAPI.getInstance().register("rec", args, (s, a) -> {
			Recipe rec = (Recipe) a[0];
			Player player = (Player) s;
			player.getInventory().addItem(rec.getResult());
		});
```

