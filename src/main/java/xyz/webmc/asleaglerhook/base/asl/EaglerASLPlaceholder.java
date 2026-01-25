package xyz.webmc.asleaglerhook.base.asl;

import ch.andre601.advancedserverlist.api.PlaceholderProvider;
import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import net.lax1dude.eaglercraft.backend.server.api.IEaglerXServerAPI;

@SuppressWarnings({ "rawtypes" })
public final class EaglerASLPlaceholder extends PlaceholderProvider {
  private final IEaglerXServerAPI api;

  public EaglerASLPlaceholder(final IEaglerXServerAPI api) {
    super("eagler");
    this.api = api;
  }

  @Override
  public final String parsePlaceholder(final String placeholder, final GenericPlayer player, final GenericServer server) {
    if (placeholder.equals("status")) {
      return (player instanceof EaglerASLPlayer) ? "EAGLER" : "JAVA";
    } else if (placeholder.equals("server")) {
      return this.api.getServerName();
    } else if (placeholder.equals("platform") || placeholder.equals("plaf")) {
      return this.api.getPlatformType().getName().toUpperCase();
    } else {
      return null;
    }
  }
}
