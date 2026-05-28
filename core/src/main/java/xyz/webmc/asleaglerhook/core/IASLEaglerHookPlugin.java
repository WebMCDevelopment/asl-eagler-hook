package xyz.webmc.asleaglerhook.core;

import ch.andre601.advancedserverlist.api.events.GenericServerListEvent;
import ch.andre601.advancedserverlist.api.objects.GenericPlayer;
import ch.andre601.advancedserverlist.api.objects.GenericServer;
import ch.andre601.advancedserverlist.api.profiles.ProfileEntry;
import ch.andre601.advancedserverlist.core.interfaces.core.PluginCore;
import ch.andre601.advancedserverlist.core.objects.CachedPlayer;

public interface IASLEaglerHookPlugin<F> {
  byte[] getFaviconBytes(F favicon);

  Class<F> getFaviconClass();

  String parsePAPIPlaceholders(String text, GenericPlayer player);

  PluginCore<F> getASL();

  GenericPlayer createPlayer(CachedPlayer player, int protocol);

  GenericServer createServer(int playersOnline, int playersMax, String host);

  GenericServerListEvent callEvent(ProfileEntry entry);

  boolean isMaintenancePluginLoaded();
}
