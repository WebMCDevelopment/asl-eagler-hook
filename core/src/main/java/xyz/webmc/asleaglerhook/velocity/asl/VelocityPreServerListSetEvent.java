package xyz.webmc.asleaglerhook.velocity.asl;

import ch.andre601.advancedserverlist.api.profiles.ProfileEntry;
import ch.andre601.advancedserverlist.api.velocity.events.PreServerListSetEvent;

public final class VelocityPreServerListSetEvent extends PreServerListSetEvent {
  public VelocityPreServerListSetEvent(ProfileEntry entry) {
    super(entry);
  }
}
