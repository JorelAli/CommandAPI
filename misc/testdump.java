// TODO: Remove before release
		{
			{
				new CommandAPICommand("killall").executes((s, a) -> {
					System.out.println("KillAll");
				}).register();
			}
			{
				LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
				arguments.put("message", new ChatArgument());

				new CommandAPICommand("pbroadcast").withArguments(arguments).executes((sender, args) -> {
					BaseComponent[] message = (BaseComponent[]) args[0];

					// Broadcast the message to everyone on the server
					Bukkit.getServer().spigot().broadcast(message);
				}).register();
			}
			LiteralCommandNode randomChance = Brigadier.registerNewLiteral("randomchance");

			//Declare arguments as normal
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
			arguments.put("numerator", new IntegerArgument(0));
			arguments.put("denominator", new IntegerArgument(1));

			//firstVal and secondVal should be able to be constructed using the CommandAPI
			ArgumentBuilder numerator = Brigadier.argBuildOf(arguments, "numerator");
			ArgumentBuilder denominator = Brigadier.argBuildOf(arguments, "denominator")
					.fork(Brigadier.getRootNode().getChild("execute"), Brigadier.fromPredicate((sender, args) -> {
						// Parse arguments like normal
						int first = (int) args[0];
						int second = (int) args[1];

						// Return boolean with a first/second chance
						return Math.ceil(Math.random() * (double) second) <= (double) first;
					}, arguments));
			// .executes(Brigadier.fromCommand(new CommandAPICommand("") /* And so on...
			// */));
			
			//Optionally, you can include another 'execute' here, so you could do '/execute if <firstVal> <secondVal>' and that returns a value
			
			//Add <firstVal> -> <secondVal> as a child of randomchance
			randomChance.addChild(numerator.then(denominator).build());

			//Add (randomchance -> <firstVal> -> <secondVal>) as a child of (execute -> if)
			//This results in execute -> if -> randomchance -> <firstVal> -> <secondVal>
			Brigadier.getRootNode().getChild("execute").getChild("if").addChild(randomChance);
		}
		{
			// TODO: Remove before release
			{
				// success
				LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
				arguments.put("time", new TimeArgument().safeOverrideSuggestions(Time.days(2), Time.seconds(10)));
				arguments.put("floats", new FloatArgument().safeOverrideSuggestions(2f, 3f));

				new CommandAPICommand("a").withArguments(arguments).executes((s, a) -> {
					System.out.println(Arrays.deepToString(a));
				}).register();
			}
			{
				LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
				arguments.put("nbt", new NBTCompoundArgument());

				new CommandAPICommand("award").withArguments(arguments).executes((sender, args) -> {
					NBTContainer nbt = (NBTContainer) args[0];
					NBTItem.convertNBTtoItem(nbt);

				}).register();
			}
			{
				LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
//		        arguments.put("text", new TextArgument().safeOverrideSuggestions("hello", "world!"));
				arguments.put("fr", new FloatRangeArgument()
						.safeOverrideSuggestions(FloatRange.floatRangeGreaterThanOrEq(2), new FloatRange(20, 40)));

				new CommandAPICommand("b").withArguments(arguments).executes((s, a) -> {
					System.out.println(Arrays.deepToString(a));
				}).register();
			}
			{
				LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
				// buggy environment - enum values are just wrong
				arguments.put("environment",
						new EnvironmentArgument().safeOverrideSuggestions(Environment.NETHER, Environment.NORMAL));
				arguments.put("loc", new LocationArgument().safeOverrideSuggestions(s -> new Location[] {
						((Player) s).getLocation(), ((Player) s).getWorld().getSpawnLocation() }));

				new CommandAPICommand("c").withArguments(arguments).executesPlayer((s, a) -> {
					System.out.println(Arrays.deepToString(a));
				}).register();
			}
			{
				LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
				arguments.put("biome", new BiomeArgument().safeOverrideSuggestions(Biome.BADLANDS_PLATEAU));
				arguments.put("adv", new AdvancementArgument().safeOverrideSuggestions(
						Bukkit.getAdvancement(new NamespacedKey("minecraft", "end/kill_dragon"))));

				new CommandAPICommand("d").withArguments(arguments).executesPlayer((s, a) -> {
					System.out.println(Arrays.deepToString(a));
				}).register();
			}
			{
				LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
				arguments.put("axis", new AxisArgument().safeOverrideSuggestions(EnumSet.allOf(Axis.class)));
				arguments.put("cc",
						new ChatColorArgument().safeOverrideSuggestions(ChatColor.DARK_GREEN, ChatColor.RED));

				new CommandAPICommand("e").withArguments(arguments).executesPlayer((s, a) -> {
					System.out.println(Arrays.deepToString(a));
				}).register();
			}
			{
				LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
				arguments.put("enchant", new EnchantmentArgument().safeOverrideSuggestions(Enchantment.DAMAGE_ALL));
				arguments.put("entitytype",
						new EntityTypeArgument().safeOverrideSuggestions(EntityType.PIG, EntityType.ZOMBIE));
				arguments.put("is", new ItemStackArgument().safeOverrideSuggestions(new ItemStack(Material.DIRT)));

				new CommandAPICommand("f").withArguments(arguments).executesPlayer((s, a) -> {
					System.out.println(Arrays.deepToString(a));
				}).register();
			}
			{
				LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
				arguments.put("particle",
						new ParticleArgument().safeOverrideSuggestions(Particle.BARRIER, Particle.CLOUD));

				new CommandAPICommand("g").withArguments(arguments).executesPlayer((s, a) -> {
					System.out.println(Arrays.deepToString(a));
				}).register();
			}
			{
				LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
				arguments.put("math",
						new MathOperationArgument().safeOverrideSuggestions(MathOperation.ADD, MathOperation.DIVIDE));

				new CommandAPICommand("h").withArguments(arguments).executesPlayer((s, a) -> {
					System.out.println(Arrays.deepToString(a));
				}).register();
			}
			{
				LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();

				ItemStack is = new ItemStack(Material.DIAMOND_SWORD);
				is.addEnchantment(Enchantment.DAMAGE_ALL, 2);

				ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(this, "myitem"),
						new ItemStack(Material.DIAMOND));
				recipe.shape("AAA", "AAA", "AAA");
				recipe.setIngredient('A', Material.CRAFTING_TABLE);
				getServer().addRecipe(recipe);

				arguments.put("nbt", new NBTCompoundArgument().safeOverrideSuggestions(NBTItem.convertItemtoNBT(is)));
				arguments.put("recipe", new RecipeArgument().safeOverrideSuggestions(recipe));
				arguments.put("scoreboardslot", new ScoreboardSlotArgument().safeOverrideSuggestions(
						ScoreboardSlot.of(DisplaySlot.BELOW_NAME), ScoreboardSlot.ofTeamColor(ChatColor.AQUA)));

				new CommandAPICommand("i").withArguments(arguments).executesPlayer((s, a) -> {
					System.out.println(Arrays.deepToString(a));
				}).register();
			}
			{

				Field f = null;
				try {
					f = PotionEffectType.class.getDeclaredField("byId");
				} catch (NoSuchFieldException | SecurityException e) {
					e.printStackTrace();
				}
				f.setAccessible(true);
				PotionEffectType[] byId = null;
				try {
					byId = (PotionEffectType[]) f.get(null);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
				for (PotionEffectType p : byId) {
					try {
						System.out.println(p.getName());
					} catch (Exception e) {
					}
				}
				CommandAPIHandler.getNMS().convert(PotionEffectType.FAST_DIGGING);
			}
			{
				LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
				arguments.put("rot", new RotationArgument().safeOverrideSuggestions(new Rotation(2, 3)));

				new CommandAPICommand("j").withArguments(arguments).executesPlayer((s, a) -> {
					System.out.println(Arrays.deepToString(a));
				}).register();
			}
			{
				Argument[] arguments = new Argument[] { new AdvancementArgument(),
//	        		new AxisArgument(),
						new BiomeArgument(), new BlockStateArgument(), new BooleanArgument(), new ChatArgument(),
						new ChatColorArgument(), new ChatComponentArgument(),
//	        		new CustomArgument(),
						new DoubleArgument(), new EnchantmentArgument(),
//	        		new EntitySelectorArgument(),
						new EnchantmentArgument(), new EntitySelectorArgument(EntitySelector.MANY_ENTITIES),
						new EntityTypeArgument(), new EnvironmentArgument(), new FloatArgument(),
						new FloatRangeArgument(), new FunctionArgument(), new GreedyStringArgument(),
						new IntegerArgument(), new IntegerRangeArgument(), new ItemStackArgument(),
//	        		new LiteralArgument(""),
						new Location2DArgument(), new LocationArgument(), new LongArgument(), new LootTableArgument(),
						new MathOperationArgument(), new NBTCompoundArgument(), new ObjectiveArgument(),
						new ObjectiveCriteriaArgument(), new ParticleArgument(), new PlayerArgument(),
						new PotionEffectArgument(), new RecipeArgument(), new RotationArgument(),
						new ScoreboardSlotArgument(), new ScoreHolderArgument(ScoreHolderType.MULTIPLE),
						new SoundArgument(), new StringArgument(), new TeamArgument(), new TextArgument(),
						new TimeArgument() };

				for (Argument a : arguments) {
					LinkedHashMap<String, Argument> args = new LinkedHashMap<>();
					args.put(a.getClass().getSimpleName(), a);

					new CommandAPICommand(a.getClass().getSimpleName()).withArguments(args).executes((c, arg) -> {
					}).register();
				}
			}
			{

				LinkedHashMap<String, Argument> args = new LinkedHashMap<>();
				args.put("AxisArgument",
						new AxisArgument().safeOverrideSuggestions(EnumSet.of(Axis.X), EnumSet.of(Axis.Y),
								EnumSet.of(Axis.Z), EnumSet.of(Axis.X, Axis.Z), EnumSet.of(Axis.X, Axis.Y),
								EnumSet.of(Axis.Y, Axis.Z), EnumSet.allOf(Axis.class)));

				new CommandAPICommand("AxisArgument").withArguments(args).executes((c, arg) -> {
				}).register();
			}
			{
				LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
				arguments.put("snd", new SoundArgument().safeOverrideSuggestions(Sound.AMBIENT_BASALT_DELTAS_ADDITIONS,
						Sound.BLOCK_BONE_BLOCK_HIT));

				new CommandAPICommand("k").withArguments(arguments).executesPlayer((s, a) -> {
					System.out.println(Arrays.deepToString(a));
				}).register();
			}
			{
				LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
				arguments.put("loot", new LootTableArgument().safeOverrideSuggestions(
						LootTables.BURIED_TREASURE.getLootTable(), LootTables.PANDA.getLootTable()));

				new CommandAPICommand("l").withArguments(arguments).executesPlayer((s, a) -> {
					System.out.println(Arrays.deepToString(a));
				}).register();
			}
			{
				LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();

				arguments.put("team", new TeamArgument().safeOverrideSuggestions(
						s -> Bukkit.getScoreboardManager().getMainScoreboard().getTeams().toArray(new Team[0])));

				new CommandAPICommand("m").withArguments(arguments).executesPlayer((s, a) -> {
					System.out.println(Arrays.deepToString(a));
				}).register();
			}
			{
				LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();

				arguments.put("obj", new ObjectiveArgument().safeOverrideSuggestions(s -> Bukkit.getScoreboardManager()
						.getMainScoreboard().getObjectives().toArray(new Objective[0])));

				new CommandAPICommand("n").withArguments(arguments).executesPlayer((s, a) -> {
					System.out.println(Arrays.deepToString(a));
				}).register();
			}
			{

				ItemStack emeraldSword = new ItemStack(Material.DIAMOND_SWORD);
				ItemMeta meta = emeraldSword.getItemMeta();
				meta.setDisplayName("Emerald Sword");
				meta.setUnbreakable(true);
				emeraldSword.setItemMeta(meta);

				ShapedRecipe emeraldSwordRecipe = new ShapedRecipe(new NamespacedKey(this, "emerald_sword"),
						emeraldSword);
				emeraldSwordRecipe.shape("AEA", "AEA", "ABA");
				emeraldSwordRecipe.setIngredient('A', Material.AIR);
				emeraldSwordRecipe.setIngredient('E', Material.EMERALD);
				emeraldSwordRecipe.setIngredient('B', Material.BLAZE_ROD);
				getServer().addRecipe(emeraldSwordRecipe);

				LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
				arguments.put("recipe", new RecipeArgument().safeOverrideSuggestions(emeraldSwordRecipe));

				new CommandAPICommand("giverecipe").withArguments(arguments).executesPlayer((player, args) -> {
					Recipe recipe = (Recipe) args[0];
					player.getInventory().addItem(recipe.getResult());
				}).register();
			}
			{
				// TODO: Rewrite the documentation for whatever this is - it's kinda buggy!
				EntityType[] forbiddenMobs = new EntityType[] { EntityType.ENDER_DRAGON, EntityType.WITHER };
				List<EntityType> allowedMobs = new ArrayList<>(Arrays.asList(EntityType.values()));
				allowedMobs.removeAll(Arrays.asList(forbiddenMobs));

				LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
				arguments.put("mob", new EntityTypeArgument().safeOverrideSuggestions(sender -> {
					if (sender.isOp()) {
						return EntityType.values();
					} else {
						return allowedMobs.toArray(new EntityType[0]);
					}
				}));

				new CommandAPICommand("spawnmob").withArguments(arguments).executesPlayer((player, args) -> {
					EntityType entityType = (EntityType) args[0];
					player.getWorld().spawnEntity(player.getLocation(), entityType);
				}).register();

			}
			{

				LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
				arguments.put("target", new EntitySelectorArgument(EntitySelector.ONE_PLAYER));
				arguments.put("potioneffect", new PotionEffectArgument().safeOverrideSuggestions((sender, prevArgs) -> {
					Player target = (Player) prevArgs[0];
					return target.getActivePotionEffects().stream().map(PotionEffect::getType)
							.toArray(PotionEffectType[]::new);
				}));

				new CommandAPICommand("spawnmob").withArguments(arguments).executesPlayer((player, args) -> {
					EntityType entityType = (EntityType) args[0];
					player.getWorld().spawnEntity(player.getLocation(), entityType);
				}).register();
			}
		}

		//TODO: Remove before release
		{
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
			arguments.put("val", new IntegerArgument());

			new CommandAPICommand("hello").withArguments(arguments).withAliases("bye", "cya", "hi").executes((s, a) -> {
				System.out.println(Arrays.deepToString(a));
			}).register();

			new CommandAPICommand("hello2").withAliases("bye2", "cya2", "hi2").executes((s, a) -> {
				System.out.println(Arrays.deepToString(a));
			}).register();
		}
		{
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
			arguments.put("uuidarg", new UUIDArgument());

			new CommandAPICommand("u1").withArguments(arguments).executes((s, a) -> {
				System.out.println(Arrays.deepToString(a));
				System.out.println(Bukkit.getEntity((UUID) a[0]).getName());
			}).register();
		}
		{
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
			arguments.put("uuidarg", new UUIDArgument().safeOverrideSuggestions(UUID.randomUUID(), UUID.randomUUID()));

			new CommandAPICommand("u2").withArguments(arguments).executes((s, a) -> {
				System.out.println(Arrays.deepToString(a));
				System.out.println(Bukkit.getEntity((UUID) a[0]).getName());
			}).register();
		}
		{
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
			arguments.put("items", new ItemStackPredicateArgument());

			new CommandAPICommand("rem").withArguments(arguments).executesPlayer((player, args) -> {

				@SuppressWarnings("unchecked")
				Predicate<ItemStack> predicate = (Predicate<ItemStack>) args[0];

				for (ItemStack item : player.getInventory()) {
					if (predicate.test(item)) {
						player.getInventory().remove(item);
					}
				}
				player.getInventory().forEach(i -> {
					if (predicate.test(i)) {
						player.getInventory().remove(i);
					}
				});
			}).register();
		}