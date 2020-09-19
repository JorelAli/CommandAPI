LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("player", new PlayerArgument());
arguments.put("contents", new ChatComponentArgument());

new CommandAPICommand("makebook")
    .withArguments(arguments)
    .executes((sender, args) -> {
        Player player = (Player) args[0];
        BaseComponent[] arr = (BaseComponent[]) args[1];
        
        //Create book
        ItemStack is = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) is.getItemMeta(); 
        meta.setTitle("Custom Book");
        meta.setAuthor(player.getName());
        meta.spigot().setPages(arr);
        is.setItemMeta(meta);
        
        //Give player the book
        player.getInventory().addItem(is);
    })
    .register();