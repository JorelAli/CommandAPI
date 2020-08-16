
//TODO: Remove on release!
public void deconstruct() {
    DedicatedServer server = ((CraftServer) Bukkit.getServer()).getHandle().getServer();
    DataPackResources datapackResources = server.dataPackResources;
    CustomFunctionManager functions = datapackResources.a();
    Map<MinecraftKey, CustomFunction> f = functions.a();
    
    Map<NamespacedKey, String[]> actualF = new HashMap<>(f.size());
    for(Entry<MinecraftKey, CustomFunction> ff : f.entrySet()) {
        NamespacedKey key = new NamespacedKey(ff.getKey().getNamespace(), ff.getKey().getKey());
        actualF.put(key, Arrays.stream(ff.getValue().b()).map(Object::toString).toArray(String[]::new));
    }
}