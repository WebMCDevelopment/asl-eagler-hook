package xyz.webmc.asleaglerhook.base;

import xyz.webmc.asleaglerhook.base.asl.EaglerPingEventWrapper;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Base64;

import javax.imageio.ImageIO;

import ch.andre601.advancedserverlist.api.profiles.ProfileEntry;
import ch.andre601.advancedserverlist.core.events.PingEventHandler;

public final class ASLEaglerHook {
  public static final ProfileEntry handleEvent(final EaglerPingEventWrapper wrapper) {
    return PingEventHandler.handleEvent(wrapper);
  }

  public static final byte[] getDataURIBytes(final String uri) {
    final String data = uri.substring(uri.indexOf(",") + 1);
    return Base64.getDecoder().decode(data);
  }

  public static final byte[] convertFavicon(final byte[] favicon) {
    try {
      BufferedImage img = ImageIO.read(new ByteArrayInputStream(favicon));
      if (img.getWidth() != 64 || img.getHeight() != 64) {
        final Image scaled = img.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        final BufferedImage tmp = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = tmp.createGraphics();
        g.drawImage(scaled, 0, 0, null);
        g.dispose();
        img = tmp;
      } else if (img.getType() != BufferedImage.TYPE_INT_ARGB) {
        final BufferedImage tmp = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = tmp.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        img = tmp;
      }

      final int[] px = img.getRGB(0, 0, 64, 64, null, 0, 64);
      final byte[] ret = new byte[64 * 64 * 4];

      int o = 0;
      for (int i = 0; i < px.length; i++) {
        final int p = px[i];
        ret[o++] = (byte) ((p >> 16) & 0xFF);
        ret[o++] = (byte) ((p >> 8) & 0xFF);
        ret[o++] = (byte) (p & 0xFF);
        ret[o++] = (byte) ((p >> 24) & 0xFF);
      }

      return ret;
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static final boolean isMaintenance() {
    try {
      final ClassLoader cl = ASLEaglerHook.class.getClassLoader();
      final Class<?> cls = Class.forName("eu.kennytv.maintenance.api.MaintenanceProvider", true, cl);
      final Object provider = cls.getMethod("get").invoke(null);
      return (boolean) provider.getClass().getMethod("isMaintenance").invoke(provider);
    } catch (final Exception e) {
      return false;
    }
  }
}
