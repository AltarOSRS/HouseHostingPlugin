package net.runelite.client.plugins.househosting;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Color;
import javax.inject.Inject;

import net.runelite.api.GameObject;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

/**
 * HouseHostingOverlay displays the current number of guests in the player's POH
 * and draws outlines around unlit burners.
 */
public class HouseHostingOverlay extends Overlay {
    private final HouseHostingPlugin plugin;
    private final HouseHostingConfig config;
    private final PanelComponent panelComponent = new PanelComponent();
    private final ModelOutlineRenderer modelOutlineRenderer;

    @Inject
    public HouseHostingOverlay(HouseHostingPlugin plugin, HouseHostingConfig config,
                               ModelOutlineRenderer modelOutlineRenderer) {
        this.plugin = plugin;
        this.config = config;
        this.modelOutlineRenderer = modelOutlineRenderer;
        setPosition(OverlayPosition.TOP_LEFT);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        // Render POH Guests counter
        if (config.pohGuestTrackerEnabled() && plugin.isInPOH() && plugin.getGuestCount() >= 0) {
            panelComponent.getChildren().clear();

            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("POH Guests")
                    .build());

            panelComponent.getChildren().add(TitleComponent.builder()
                    .text(String.valueOf(plugin.getGuestCount()))
                    .build());

            panelComponent.render(graphics);
        }

        // Draw outlines around unlit burners
        if (config.enableUnlitBurnerOutline()) {
            Color highlightColor = config.unlitBurnerHighlightColor();
            int borderWidth = config.unlitBurnerBorderWidth();
            int outlineFeather = config.unlitBurnerOutlineFeather();

            for (GameObject unlitBurner : plugin.getUnlitBurners().keySet()) {
                modelOutlineRenderer.drawOutline(unlitBurner, borderWidth, highlightColor, outlineFeather);
            }
        }

        return null;
    }
}