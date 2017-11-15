package com.brandon3055.projectintelligence.client.gui.guielements;

import com.brandon3055.brandonscore.client.ResourceHelperBC;
import com.brandon3055.brandonscore.client.gui.modulargui.baseelements.GuiButton;
import com.brandon3055.projectintelligence.client.PITextures;
import com.brandon3055.projectintelligence.client.StyleHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

import static com.brandon3055.projectintelligence.client.StyleHandler.StyleType.*;

/**
 * Created by brandon3055 on 14/08/2017.
 */
public class StyledGuiButton extends GuiButton {

    private String prop;
    protected boolean includeColour;
    protected boolean includeVanilla = true;

    public StyledGuiButton(String prop, boolean includeColour) {
        this.prop = prop;
        this.includeColour = includeColour;
    }

    public StyledGuiButton(String prop) {
        this(prop, true);
    }

    public StyledGuiButton setIncludeVanilla(boolean includeVanilla) {
        this.includeVanilla = includeVanilla;
        return this;
    }

    @Override
    public int getTextColour(boolean hovered, boolean disabled) {
        if (includeColour) {
            return StyleHandler.getColour(prop + "." + (hovered ? TEXT_HOVER : TEXT_COLOUR).getName()).rgb();
        }
        return super.getTextColour(hovered, disabled);
    }

    @Override
    public void renderElement(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        boolean mouseOver = isMouseOver(mouseX, mouseY) || (getToggleMode() && getToggleState());

        int border = StyleHandler.getInt(prop + "." + BORDER.getName());
        int borderHover = StyleHandler.getInt(prop + "." + BORDER_HOVER.getName());
        if (includeVanilla && StyleHandler.getBoolean(prop + "." + VANILLA_TEXTURE.getName())) {
            int texV = 48 + (getRenderState(mouseOver) * 20);

            if (mouseOver) {
                StyleHandler.getColour(prop + "." + HOVER.getName()).glColour();
            }
            else {
                StyleHandler.getColour(prop + "." + COLOUR.getName()).glColour();
            }

            ResourceHelperBC.bindTexture(PITextures.PI_PARTS);
            drawTiledTextureRectWithTrim(xPos(), yPos(), xSize(), ySize(), 2, 2, 2, 2, 0, texV, 200, 20);
            GlStateManager.color(1, 1, 1, 1);
            drawBorderedRect(xPos(), yPos(), xSize(), ySize(), 1, 0, mouseOver ? borderHover : border);
        }
        else {
            int fill = StyleHandler.getInt(prop + "." + COLOUR.getName());
            int fillHover = StyleHandler.getInt(prop + "." + HOVER.getName());
            drawBorderedRect(xPos(), yPos(), xSize(), ySize(), 1, mouseOver ? fillHover : fill, mouseOver ? borderHover : border);
        }

        super.renderElement(mc, mouseX, mouseY, partialTicks);
    }
}
