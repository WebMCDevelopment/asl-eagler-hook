package xyz.webmc.asleaglerhook.base;

import ch.andre601.advancedserverlist.api.events.GenericServerListEvent;
import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.api.profiles.ProfileEntry;
import ch.andre601.advancedserverlist.core.interfaces.core.PluginCore;
import ch.andre601.advancedserverlist.core.objects.CachedPlayer;

public interface IASLEaglerHookPlugin<F> {
  public byte[] getFaviconBytes(final F favicon);

  public Class<F> getFaviconClass();

  public String parsePAPIPlaceholders(final String text, final GenericPlayer player);

  public PluginCore<F> getASL();

  public GenericPlayer createPlayer(final CachedPlayer player, final int protocol);

  public GenericServer createServer(final int playersOnline, final int playersMax, final String host);

  public GenericServerListEvent callEvent(final ProfileEntry entry);

  public boolean isMaintenancePluginLoaded();
}
