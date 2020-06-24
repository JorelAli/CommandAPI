LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("contents", new ChatComponentArgument());

new CommandAPICommand("makebook")
    .withArguments(arguments)
    .executesPlayer((player, args) -> {
        BaseComponent[] arr = (BaseComponent[]) args[0];
        
        //Create book
        ItemStack is = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) is.getItemMeta(); 
        meta.spigot().addPage(arr);
        is.setItemMeta(meta);
        
        //Give player the book
        player.getInventory().addItem(is);
    })
    .register();