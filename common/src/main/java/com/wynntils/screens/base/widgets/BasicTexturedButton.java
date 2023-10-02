/*
 * Copyright © Wynntils 2022-2023.
 * This file is released under LGPLv3. See LICENSE for full license details.
 */
package com.wynntils.screens.base.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import com.wynntils.screens.base.TooltipProvider;
import com.wynntils.utils.mc.ComponentUtils;
import com.wynntils.utils.render.RenderUtils;
import com.wynntils.utils.render.Texture;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class BasicTexturedButton extends WynntilsButton implements TooltipProvider {
    private final Texture texture;

    private final Consumer<Integer> onClick;
    private List<Component> tooltip;

    private boolean renderTooltipAboveMouse;
    private final boolean scaleTexture;

    public BasicTexturedButton(
            int x, int y, int width, int height, Texture texture, Consumer<Integer> onClick, List<Component> tooltip) {
        this(x, y, width, height, texture, onClick, tooltip, false);
    }

    public BasicTexturedButton(
            int x,
            int y,
            int width,
            int height,
            Texture texture,
            Consumer<Integer> onClick,
            List<Component> tooltip,
            boolean scaleTexture) {
        super(x, y, width, height, Component.literal("Basic Button"));
        this.texture = texture;
        this.onClick = onClick;
        this.scaleTexture = scaleTexture;
        this.setTooltip(tooltip);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        PoseStack poseStack = guiGraphics.pose();

        if (scaleTexture) {
            RenderUtils.drawScalingTexturedRect(
                    poseStack,
                    texture.resource(),
                    this.getX(),
                    this.getY(),
                    0,
                    getWidth(),
                    getHeight(),
                    texture.width(),
                    texture.height());
        } else {
            RenderUtils.drawTexturedRect(poseStack, texture, this.getX(), this.getY());
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isMouseOver(mouseX, mouseY)) return false;

        onClick.accept(button);

        return true;
    }

    @Override
    public void onPress() {}

    @Override
    public List<Component> getTooltipLines() {
        return tooltip;
    }

    public void setTooltip(List<Component> newTooltip) {
        tooltip = ComponentUtils.wrapTooltips(newTooltip, 250);
    }
}
