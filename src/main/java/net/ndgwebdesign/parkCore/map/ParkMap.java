package net.ndgwebdesign.parkCore.map;

import net.ndgwebdesign.parkCore.managers.AttractionManager;
import net.ndgwebdesign.parkCore.objects.Attraction;
import org.bukkit.World;
import org.bukkit.block.Block;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collection;

public class ParkMap {

    private static final int IMAGE_SIZE = 1000;
    private static final int SCALE = 2;          // blocks per pixel
    private static final int PADDING = 30;       // extra ruimte rond park

    public static File generate(World world, File outputFile) {

        Collection<Attraction> attractions = AttractionManager.getAll();
        if (attractions.isEmpty()) return null;

        /* ------------------------------ */
        /* Bepaal grenzen                 */
        /* ------------------------------ */
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        int minZ = Integer.MAX_VALUE, maxZ = Integer.MIN_VALUE;

        for (Attraction a : attractions) {
            if (a.getLocation() == null) continue;

            int x = a.getLocation().getBlockX();
            int z = a.getLocation().getBlockZ();

            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
            minZ = Math.min(minZ, z);
            maxZ = Math.max(maxZ, z);
        }

        minX -= PADDING;
        minZ -= PADDING;
        maxX += PADDING;
        maxZ += PADDING;

        BufferedImage image =
                new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        g.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );

        /* ------------------------------ */
        /* Wereld renderen                */
        /* ------------------------------ */
        for (int px = 0; px < IMAGE_SIZE; px++) {
            for (int pz = 0; pz < IMAGE_SIZE; pz++) {

                int worldX = minX + (px * (maxX - minX) / IMAGE_SIZE);
                int worldZ = minZ + (pz * (maxZ - minZ) / IMAGE_SIZE);

                Block block = world.getHighestBlockAt(worldX, worldZ);

                Color color = BlockColorManager.getColor(block.getType());
                image.setRGB(px, pz, color.getRGB());
            }
        }

        /* ------------------------------ */
        /* Attracties tekenen             */
        /* ------------------------------ */
        g.setFont(new Font("Arial", Font.BOLD, 14));

        for (Attraction a : attractions) {
            if (a.getLocation() == null) continue;

            int px = (int) ((a.getLocation().getBlockX() - minX)
                    / (double) (maxX - minX) * IMAGE_SIZE);
            int pz = (int) ((a.getLocation().getBlockZ() - minZ)
                    / (double) (maxZ - minZ) * IMAGE_SIZE);

            g.setColor(Color.RED);
            g.fillOval(px - 4, pz - 4, 8, 8);

            g.setColor(Color.BLACK);
            g.drawString(a.getName(), px + 6, pz);
        }

        g.dispose();

        try {
            ImageIO.write(image, "png", outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputFile;
    }
}
