
	
	public void a() {
		getBrigadierDispatcher().register(CommandDispatcher.a("reload")
			.requires(clw -> clw.hasPermission(2))
			.executes((cmdCtx) -> {
				CommandListenerWrapper commandlistenerwrapper = cmdCtx.getSource();
				MinecraftServer minecraftserver = commandlistenerwrapper.getServer();
				ResourcePackRepository resourcepackrepository = minecraftserver.getResourcePackRepository();
				SaveData savedata = minecraftserver.getSaveData();
				Collection<String> collection = resourcepackrepository.d();
				
				resourcepackrepository.a();
				Collection<String> c1 = Lists.newArrayList(collection); //collection1
				Collection<String> c2 = savedata.D().b(); //collection2
				
				Iterator<String> iterator = resourcepackrepository.b().iterator();

				while (iterator.hasNext()) {
					String s = (String) iterator.next();
					if (!c2.contains(s) && !c1.contains(s)) {
						c1.add(s);
					}
				}
				Collection<String> collection1 = c1;
				
				commandlistenerwrapper.sendMessage(new ChatMessage("commands.reload.success"), true);
				
				commandlistenerwrapper.getServer().a(collection1).exceptionally((throwable) -> {
					LogManager.getLogger().warn("Failed to execute reload", throwable);
					commandlistenerwrapper.sendFailureMessage(new ChatMessage("commands.reload.failure"));
					return null;
				});
				
				return 0;
			})
		);
	}