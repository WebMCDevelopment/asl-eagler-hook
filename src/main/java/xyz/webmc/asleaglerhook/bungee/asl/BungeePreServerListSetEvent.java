package xyz.webmc.asleaglerhook.bungee.asl;

import ch.andre601.advancedserverlist.api.profiles.ProfileEntry;
import ch.andre601.advancedserverlist.api.bungeecord.events.PreServerListSetEvent;

public final class BungeePreServerListSetEvent extends PreServerListSetEvent {
  public BungeePreServerListSetEvent(ProfileEntry entry) {
    super(entry);
  }
}
