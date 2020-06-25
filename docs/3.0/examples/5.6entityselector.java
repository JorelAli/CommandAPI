//LinkedHashMap to store arguments for the command
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();

//Using a collective entity selector to select multiple entities
arguments.put("entities", new EntitySelectorArgument(EntitySelector.MANY_ENTITIES));

new CommandAPICommand("remove")
    .withArguments(arguments)
    .executes((sender, args) -> {
        //Parse the argument as a collection of entities (as stated above in the documentation)
        @SuppressWarnings("unchecked")
        Collection<Entity> entities = (Collection<Entity>) args[0];
        
        sender.sendMessage("Removed " + entities.size() + " entities");
        for(Entity e : entities) {
            e.remove();
        }
    })
    .register();