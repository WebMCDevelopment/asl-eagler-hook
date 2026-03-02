package xyz.webmc.asleaglerhook.base;

import ch.andre601.advancedserverlist.api.events.GenericServerListEvent;
import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.api.profiles.ProfileEntry;
import ch.andre601.advancedserverlist.core.interfaces.core.PluginCore;
import ch.andre601.advancedserverlist.core.objects.CachedPlayer;

public interface IASLEaglerHookPlugin<F> {
  public abstract byte[] getFaviconBytes(final F favicon);

  public abstract Class<F> getFaviconClass();

  public abstract String parsePAPIPlaceholders(final String text, final GenericPlayer player);

  public abstract PluginCore<F> getASL();

  public abstract GenericPlayer createPlayer(final CachedPlayer player, final int protocol);

  public abstract GenericServer createServer(final int playersOnline, final int playersMax, final String host);

  public abstract GenericServerListEvent callEvent(final ProfileEntry entry);

  public abstract boolean isMaintenancePluginLoaded();
}
