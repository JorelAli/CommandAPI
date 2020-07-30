//TODO: Remove before release
        {
        	//success
	        LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
	        arguments.put("time", new TimeArgument().safeOverrideSuggestions(Time.days(2), Time.seconds(10)));
	        arguments.put("floats", new FloatArgument().safeOverrideSuggestions(2f, 3f));
	        
	        new CommandAPICommand("a")
	        .withArguments(arguments)
	        .executes((s, a) -> {
	        	System.out.println(Arrays.deepToString(a));
	        })
	        .register();
        }
        {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
        	arguments.put("nbt", new NBTCompoundArgument());

        	new CommandAPICommand("award")
        	    .withArguments(arguments)
        	    .executes((sender, args) -> {
        	        NBTContainer nbt = (NBTContainer) args[0];
        	        NBTItem.convertNBTtoItem(nbt);
        	        
        	    })
        	    .register();
        }
        {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
//	        arguments.put("text", new TextArgument().safeOverrideSuggestions("hello", "world!"));
	        arguments.put("fr", new FloatRangeArgument().safeOverrideSuggestions(FloatRange.floatRangeGreaterThanOrEq(2), new FloatRange(20, 40)));
	        
	        new CommandAPICommand("b")
	        .withArguments(arguments)
	        .executes((s, a) -> {
	        	System.out.println(Arrays.deepToString(a));
	        })
	        .register();
        } {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
        	// buggy environment - enum values are just wrong
	        arguments.put("environment", new EnvironmentArgument().safeOverrideSuggestions(Environment.NETHER, Environment.NORMAL));
			arguments.put("loc", new LocationArgument().safeOverrideSuggestions(
				s -> new Location[] { 
						((Player) s).getLocation(), 
						((Player) s).getWorld().getSpawnLocation() 
				})
			);
	        
	        new CommandAPICommand("c")
	        .withArguments(arguments)
	        .executesPlayer((s, a) -> {
	        	System.out.println(Arrays.deepToString(a));
	        })
	        .register();
        } {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
	        arguments.put("biome", new BiomeArgument().safeOverrideSuggestions(Biome.BADLANDS_PLATEAU));
	        arguments.put("adv", new AdvancementArgument().safeOverrideSuggestions(Bukkit.getAdvancement(new NamespacedKey("minecraft", "end/kill_dragon"))));
	        
	        new CommandAPICommand("d")
	        .withArguments(arguments)
	        .executesPlayer((s, a) -> {
	        	System.out.println(Arrays.deepToString(a));
	        })
	        .register();
        } {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
	        arguments.put("axis", new AxisArgument().safeOverrideSuggestions(EnumSet.allOf(Axis.class)));
	        arguments.put("cc", new ChatColorArgument().safeOverrideSuggestions(ChatColor.DARK_GREEN, ChatColor.RED));
	        
	        new CommandAPICommand("e")
	        .withArguments(arguments)
	        .executesPlayer((s, a) -> {
	        	System.out.println(Arrays.deepToString(a));
	        })
	        .register();
        }  {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
	        arguments.put("enchant", new EnchantmentArgument().safeOverrideSuggestions(Enchantment.DAMAGE_ALL));
	        arguments.put("entitytype", new EntityTypeArgument().safeOverrideSuggestions(EntityType.PIG, EntityType.ZOMBIE));
	        arguments.put("is", new ItemStackArgument().safeOverrideSuggestions(new ItemStack(Material.DIRT)));
	        
	        new CommandAPICommand("f")
	        .withArguments(arguments)
	        .executesPlayer((s, a) -> {
	        	System.out.println(Arrays.deepToString(a));
	        })
	        .register();
        } {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
	        arguments.put("particle", new ParticleArgument().safeOverrideSuggestions(Particle.BARRIER, Particle.CLOUD));
	        
	        new CommandAPICommand("g")
	        .withArguments(arguments)
	        .executesPlayer((s, a) -> {
	        	System.out.println(Arrays.deepToString(a));
	        })
	        .register();
        } {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
	        arguments.put("math", new MathOperationArgument().safeOverrideSuggestions(MathOperation.ADD, MathOperation.DIVIDE));
	        
	        new CommandAPICommand("h")
	        .withArguments(arguments)
	        .executesPlayer((s, a) -> {
	        	System.out.println(Arrays.deepToString(a));
	        })
	        .register();
        } {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
        	
        	ItemStack is = new ItemStack(Material.DIAMOND_SWORD);
        	is.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        	
        	ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(this, "myitem"), new ItemStack(Material.DIAMOND));
        	recipe.shape("AAA", "AAA", "AAA");
        	recipe.setIngredient('A', Material.CRAFTING_TABLE);
        	getServer().addRecipe(recipe);
        	
	        arguments.put("nbt", new NBTCompoundArgument().safeOverrideSuggestions(NBTItem.convertItemtoNBT(is)));
	        arguments.put("recipe", new RecipeArgument().safeOverrideSuggestions(recipe));
	        arguments.put("scoreboardslot", new ScoreboardSlotArgument().safeOverrideSuggestions(ScoreboardSlot.of(DisplaySlot.BELOW_NAME), ScoreboardSlot.ofTeamColor(ChatColor.AQUA)));
	        
	        new CommandAPICommand("i")
	        .withArguments(arguments)
	        .executesPlayer((s, a) -> {
	        	System.out.println(Arrays.deepToString(a));
	        })
	        .register();
        } {
        	
        	
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
        	for(PotionEffectType p : byId) {
        		try {
        			System.out.println(p.getName());
        		} catch(Exception e) {}
        	}
        	CommandAPIHandler.getNMS().convert(PotionEffectType.FAST_DIGGING);
        } {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
	        arguments.put("rot", new RotationArgument().safeOverrideSuggestions(new Rotation(2, 3)));
	        
	        new CommandAPICommand("j")
	        .withArguments(arguments)
	        .executesPlayer((s, a) -> {
	        	System.out.println(Arrays.deepToString(a));
	        })
	        .register();
        } {
        	Argument[] arguments = new Argument[] {
        		new AdvancementArgument(),
//        		new AxisArgument(),
        		new BiomeArgument(),
        		new BlockStateArgument(),
        		new BooleanArgument(),
        		new ChatArgument(),
        		new ChatColorArgument(),
        		new ChatComponentArgument(),
//        		new CustomArgument(),
        		new DoubleArgument(),
        		new EnchantmentArgument(),
//        		new EntitySelectorArgument(),
        		new EnchantmentArgument(),
        		new EntitySelectorArgument(EntitySelector.MANY_ENTITIES),
        		new EntityTypeArgument(),
        		new EnvironmentArgument(),
        		new FloatArgument(),
        		new FloatRangeArgument(),
        		new FunctionArgument(),
        		new GreedyStringArgument(),
        		new IntegerArgument(),
        		new IntegerRangeArgument(),
        		new ItemStackArgument(),
//        		new LiteralArgument(""),
        		new Location2DArgument(),
        		new LocationArgument(),
        		new LongArgument(),
        		new LootTableArgument(),
        		new MathOperationArgument(),
        		new NBTCompoundArgument(),
        		new ObjectiveArgument(),
        		new ObjectiveCriteriaArgument(),
        		new ParticleArgument(),
        		new PlayerArgument(),
        		new PotionEffectArgument(),
        		new RecipeArgument(),
        		new RotationArgument(),
        		new ScoreboardSlotArgument(),
        		new ScoreHolderArgument(ScoreHolderType.MULTIPLE),
        		new SoundArgument(),
        		new StringArgument(),
        		new TeamArgument(),
        		new TextArgument(),
        		new TimeArgument()
        	};
        	
        	for(Argument a : arguments) {
        		LinkedHashMap<String, Argument> args = new LinkedHashMap<>();
        		args.put(a.getClass().getSimpleName(), a);
        		
        		new CommandAPICommand(a.getClass().getSimpleName())
        		.withArguments(args)
        		.executes((c, arg) -> {
        		}).register();
        	}
        } {
        	
        	LinkedHashMap<String, Argument> args = new LinkedHashMap<>();
    		args.put("AxisArgument", new AxisArgument().safeOverrideSuggestions(
    			EnumSet.of(Axis.X),
    			EnumSet.of(Axis.Y),
    			EnumSet.of(Axis.Z),
    			EnumSet.of(Axis.X, Axis.Z),
    			EnumSet.of(Axis.X, Axis.Y),
    			EnumSet.of(Axis.Y, Axis.Z),
    			EnumSet.allOf(Axis.class)
			));
    		
    		new CommandAPICommand("AxisArgument")
    		.withArguments(args)
    		.executes((c, arg) -> {
    		}).register();
        } {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
	        arguments.put("snd", new SoundArgument().safeOverrideSuggestions(Sound.AMBIENT_BASALT_DELTAS_ADDITIONS, Sound.BLOCK_BONE_BLOCK_HIT));
	        
	        new CommandAPICommand("k")
	        .withArguments(arguments)
	        .executesPlayer((s, a) -> {
	        	System.out.println(Arrays.deepToString(a));
	        })
	        .register();
        } {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
	        arguments.put("loot", new LootTableArgument().safeOverrideSuggestions(LootTables.BURIED_TREASURE.getLootTable(), LootTables.PANDA.getLootTable()));

	        new CommandAPICommand("l")
	        .withArguments(arguments)
	        .executesPlayer((s, a) -> {
	        	System.out.println(Arrays.deepToString(a));
	        })
	        .register();
        } {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
        	
	        arguments.put("team", new TeamArgument().safeOverrideSuggestions(Bukkit.getScoreboardManager().getMainScoreboard().getTeams().toArray(new Team[0])));

	        new CommandAPICommand("m")
	        .withArguments(arguments)
	        .executesPlayer((s, a) -> {
	        	System.out.println(Arrays.deepToString(a));
	        })
	        .register();
        } {
        	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
        	
	        arguments.put("obj", new ObjectiveArgument().safeOverrideSuggestions(Bukkit.getScoreboardManager().getMainScoreboard().getObjectives().toArray(new Objective[0])));

	        new CommandAPICommand("n")
	        .withArguments(arguments)
	        .executesPlayer((s, a) -> {
	        	System.out.println(Arrays.deepToString(a));
	        })
	        .register();
        } {
        	
ItemStack emeraldSword = new ItemStack(Material.DIAMOND_SWORD);
ItemMeta meta = emeraldSword.getItemMeta();
meta.setDisplayName("Emerald Sword");
meta.setUnbreakable(true);
emeraldSword.setItemMeta(meta);

ShapedRecipe emeraldSwordRecipe = new ShapedRecipe(new NamespacedKey(this, "emerald_sword"), emeraldSword);
emeraldSwordRecipe.shape(
	"AEA", 
	"AEA", 
	"ABA"
);
emeraldSwordRecipe.setIngredient('A', Material.AIR);
emeraldSwordRecipe.setIngredient('E', Material.EMERALD);
emeraldSwordRecipe.setIngredient('B', Material.BLAZE_ROD);
getServer().addRecipe(emeraldSwordRecipe);

LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("recipe", new RecipeArgument().safeOverrideSuggestions(emeraldSwordRecipe));

new CommandAPICommand("giverecipe")
	.withArguments(arguments)
	.executesPlayer((player, args) -> {
		Recipe recipe = (Recipe) args[0];
		player.getInventory().addItem(recipe.getResult());
	})
	.register();
        } {
        	
        	EntityType[] forbiddenMobs = new EntityType[] {EntityType.ENDER_DRAGON, EntityType.WITHER};
        	List<EntityType> allowedMobs = Arrays.asList(EntityType.values());
        	allowedMobs.removeAll(Arrays.asList(forbiddenMobs));
        	
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("mob", new EntityTypeArgument().safeOverrideSuggestions(
	sender -> {
		if(sender.isOp()) {
			return EntityType.values();
		} else {
			return allowedMobs.toArray(new EntityType[0]);
		}
	})
);

new CommandAPICommand("spawnmob")
	.withArguments(arguments)
	.executesPlayer((player, args) -> {
		EntityType entityType = (EntityType) args[0];
		player.getWorld().spawnEntity(player.getLocation(), entityType);
	})
	.register();

        	
        } {

LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("target", new EntitySelectorArgument(EntitySelector.ONE_PLAYER));
arguments.put("potioneffect", new PotionEffectArgument().safeOverrideSuggestions(
	(sender, prevArgs) -> {
		Player target = (Player) prevArgs[0];
		return target.getActivePotionEffects().stream()
			.map(PotionEffect::getType)
			.toArray(PotionEffectType[]::new);
	})
);

new CommandAPICommand("spawnmob")
	.withArguments(arguments)
	.executesPlayer((player, args) -> {
		EntityType entityType = (EntityType) args[0];
		player.getWorld().spawnEntity(player.getLocation(), entityType);
	})
	.register();
        }