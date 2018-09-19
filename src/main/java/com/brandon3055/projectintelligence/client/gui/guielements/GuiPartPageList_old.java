package com.brandon3055.projectintelligence.client.gui.guielements;

import codechicken.lib.colour.Colour;
import codechicken.lib.math.MathHelper;
import com.brandon3055.brandonscore.client.ResourceHelperBC;
import com.brandon3055.brandonscore.client.gui.modulargui.MGuiElementBase;
import com.brandon3055.brandonscore.client.gui.modulargui.baseelements.GuiButton;
import com.brandon3055.brandonscore.client.gui.modulargui.baseelements.GuiPopUpDialogBase;
import com.brandon3055.brandonscore.client.gui.modulargui.baseelements.GuiScrollElement;
import com.brandon3055.brandonscore.client.gui.modulargui.baseelements.GuiSlideControl;
import com.brandon3055.brandonscore.client.gui.modulargui.guielements.GuiTextField;
import com.brandon3055.brandonscore.client.gui.modulargui.guielements.GuiTexture;
import com.brandon3055.brandonscore.utils.DataUtils;
import com.brandon3055.projectintelligence.client.PITextures;
import com.brandon3055.projectintelligence.client.StyleHandler;
import com.brandon3055.projectintelligence.client.StyleHandler.*;
import com.brandon3055.projectintelligence.client.gui.GuiProjectIntelligence_old;
import com.brandon3055.projectintelligence.client.gui.PIConfig;
import com.brandon3055.projectintelligence.client.gui.TabManager;
import com.brandon3055.projectintelligence.docmanagement.ContentRelation;
import com.brandon3055.projectintelligence.docmanagement.DocumentationManager;
import com.brandon3055.projectintelligence.docmanagement.DocumentationPage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static com.brandon3055.brandonscore.client.gui.modulargui.baseelements.GuiScrollElement.ListMode.VERT_LOCK_POS;
import static com.brandon3055.brandonscore.client.gui.modulargui.baseelements.GuiSlideControl.SliderRotation.VERTICAL;
import static com.brandon3055.brandonscore.client.gui.modulargui.lib.GuiAlign.LEFT;
import static com.brandon3055.brandonscore.client.gui.modulargui.lib.GuiAlign.TextRotation.NORMAL;
import static com.brandon3055.brandonscore.client.gui.modulargui.lib.GuiAlign.TextRotation.ROT_CC;

/**
 * Created by brandon3055 on 10/07/2017.
 */
public class GuiPartPageList_old extends MGuiElementBase<GuiPartPageList_old> {
    private final int HIDDEN_X_SIZE = 12;

    /**
     * Nav Bar Y Size
     */
    private final int NAV_BAR_SIZE = 14;
    /**
     * Title Bar Y Size
     */
    private final int TITLE_BAR_SIZE = 0;
    /**
     * Directory Bar X Size
     */
    private final int DIR_BAR_SIZE = 12;
    /**
     * Footer Y Size
     */
    private final int FOOTER_SIZE = 16;

    private int extendedXSize = 150;
    private boolean extended = true;
    private GuiSlideControl scrollBar;
    private GuiScrollElement scrollElement;
    private List<GuiButton> navButtons = new LinkedList<>();
    private GuiProjectIntelligence_old mainWindow;
    private String buttonController = "";
    private GuiTextField searchBox;
    private GuiButton backButton;
    private GuiButton forwardButton;
    private String currentPage = "";

    public GuiPartPageList_old(GuiProjectIntelligence_old mainWindow) {
        this.mainWindow = mainWindow;
    }

    @Override
    public void addChildElements() {
        super.addChildElements();

        GuiButton toggleView = new GuiButton().setSize(10, 10).setHoverText(I18n.format("pi.button.toggle_nav_window.info"));
        GuiTexture tex = new GuiTexture(0, 16, 6, 7, PITextures.PI_PARTS);
        tex.setPreDrawCallback((minecraft, mouseX, mouseY, partialTicks, mouseOver) -> StyleHandler.getColour("page_list.hide_button." + (toggleView.isMouseOver(mouseX, mouseY) ? "hover" : "colour")).glColour());
        tex.setPostDrawCallback(IDrawCallback::resetColour);
        tex.setTexXGetter(() -> extended ? 0 : 9);
        toggleView.addChild(tex);
        toggleView.addAndFireReloadCallback(guiButton -> guiButton.setYPos(yPos() + (NAV_BAR_SIZE - toggleView.ySize()) / 2));
        toggleView.setListener(() -> extended = !extended);
        tex.setXPosMod(() -> maxXPos() - 10).translate(0, 2);
        toggleView.setXPosMod(() -> maxXPos() - 11);
        addChild(toggleView);

        backButton = new GuiButton().setSize(10, 12).setHoverText(I18n.format("pi.button.go_back"));
        tex = new GuiTexture(0, 34, 7, 10, PITextures.PI_PARTS);
        tex.setPreDrawCallback((minecraft, mouseX, mouseY, partialTicks, mouseOver) -> {
            Colour c = StyleHandler.getColour("page_list.hide_button." + (backButton.isMouseOver(mouseX, mouseY) && !backButton.isDisabled() ? "hover" : "colour"));
            if (backButton.isDisabled()) {
                c = c.copy().set(changeShade(c.argb(), -0.5));
            }
            c.glColour();
        });
        tex.setPostDrawCallback(IDrawCallback::resetColour);
        tex.setTexSizeOverride(8, 12);
        backButton.addChild(tex);
        backButton.addAndFireReloadCallback(guiButton -> guiButton.setYPos(yPos() + (NAV_BAR_SIZE - backButton.ySize()) / 2));
        tex.setXPosMod(() -> maxXPos() - extendedXSize + 5).translate(0, 1);
        backButton.setXPosMod(() -> maxXPos() - extendedXSize + 4);
        addChild(backButton);
        backButton.setListener(() -> {
            TabManager.goBack();
            reloadPageButtons(true);
        });
        backButton.setEnabledCallback(() -> xSize() == extendedXSize);

        forwardButton = new GuiButton().setSize(10, 12).setHoverText(I18n.format("pi.button.go_forward"));
        tex = new GuiTexture(8, 34, 7, 10, PITextures.PI_PARTS);
        tex.setPreDrawCallback((minecraft, mouseX, mouseY, partialTicks, mouseOver) -> {
            Colour c = StyleHandler.getColour("page_list.hide_button." + (forwardButton.isMouseOver(mouseX, mouseY) && !forwardButton.isDisabled() ? "hover" : "colour"));
            if (forwardButton.isDisabled()) {
                c = c.copy().set(changeShade(c.argb(), -0.5));
            }
            c.glColour();
        });
        tex.setPostDrawCallback(IDrawCallback::resetColour);
        tex.setTexSizeOverride(8, 12);
        forwardButton.addChild(tex);
        forwardButton.addAndFireReloadCallback(guiButton -> guiButton.setYPos(yPos() + (NAV_BAR_SIZE - forwardButton.ySize()) / 2));
        tex.setXPosMod(() -> maxXPos() - extendedXSize + 10 + 6).translate(0, 1);
        forwardButton.setXPosMod(() -> maxXPos() - extendedXSize + 10 + 4);
        addChild(forwardButton);
        forwardButton.setListener(() -> {
            TabManager.goForward();
            reloadPageButtons(true);
        });
        forwardButton.setEnabledCallback(() -> xSize() == extendedXSize);

        scrollBar = new GuiSlideControl(VERTICAL);
        scrollBar.setXPosMod((guiSlideControl, integer) -> maxXPos() - (scrollBar.xSize() + (scrollBar.xSize() == 4 ? 2 : 2)));
        scrollBar.setYPosMod((guiSlideControl, integer) -> yPos() + NAV_BAR_SIZE + TITLE_BAR_SIZE);
        scrollBar.setSize(10, ySize() - (NAV_BAR_SIZE + TITLE_BAR_SIZE + FOOTER_SIZE));

        scrollBar.setBackgroundElement(new StyledGuiRect("page_list.scroll_bar") {
            @Override
            public boolean isMouseOver(int mouseX, int mouseY) {
                return super.isMouseOver(mouseX, mouseY) || scrollBar.isDragging();
            }
        });
        scrollBar.setSliderElement(new StyledGuiRect("page_list.scroll_bar.scroll_slider") {
            @Override
            public boolean isMouseOver(int mouseX, int mouseY) {
                return super.isMouseOver(mouseX, mouseY) || scrollBar.isDragging();
            }
        });
        scrollBar.getBackgroundElement().setXPosMod((o, integer) -> scrollBar.xPos()).setYPosMod((o, integer) -> scrollBar.yPos());
        scrollBar.getSliderElement().setXPosMod((o, integer) -> scrollBar.getInsetRect().x);
        scrollBar.setEnabledCallback(() -> extended);

        scrollElement = new GuiScrollElement();
        scrollElement.setVerticalScrollBar(scrollBar);
        scrollElement.setListMode(VERT_LOCK_POS);
        scrollElement.setStandardScrollBehavior();
        scrollElement.setXSizeMod((guiScrollElement, integer) -> xSize() - HIDDEN_X_SIZE);
        scrollElement.setAllowedScrollAxes(true, false);
        addChild(scrollElement);

        scrollElement.getVerticalScrollBar().setXSize(10).updateElements();

        addSearchBox();
    }

    private void addSearchBox() {
        int settingsSize = FOOTER_SIZE - 4;

        searchBox = new GuiTextField();
        searchBox.setXPosMod(() -> maxXPos() - extendedXSize + 2);
        searchBox.addAndFireReloadCallback(field -> {
            field.setSize(extendedXSize - settingsSize - 5, FOOTER_SIZE - 4);
            field.setYPos(maxYPos() - FOOTER_SIZE + 2);
        });
        searchBox.setChangeListener(() -> reloadPageButtons(false));
        searchBox.setFocusListener(focused -> {
            if (focused) reloadPageButtons(false);
        });
        searchBox.setEnabledCallback(() -> xSize() == extendedXSize);

        GuiButton searchSettings = new GuiButton().setSize(16, 16).setHoverText(I18n.format("pi.button.search_settings"));
        GuiTexture settingsTex = new GuiTexture(16, 0, settingsSize, settingsSize, PITextures.PI_PARTS);
        settingsTex.setXPosMod(() -> searchBox.maxXPos() + 1);
        settingsTex.setTexSizeOverride(16, 16);
        settingsTex.setPreDrawCallback((minecraft, mouseX, mouseY, partialTicks, mouseOver) -> StyleHandler.getColour("page_list.search.settings_button." + (mouseOver ? "hover" : "colour")).glColour());
        settingsTex.setPostDrawCallback(IDrawCallback::resetColour);
        searchSettings.addChild(settingsTex);
        searchSettings.setXPosMod(() -> searchBox.maxXPos() + 2);
        searchSettings.addAndFireReloadCallback(guiButton -> guiButton.setYPos(searchBox.yPos()));
        searchSettings.setListener(() -> {
            GuiPopUpDialogBase dialog = new GuiPopUpDialogBase(this);
            dialog.setSize(searchBox.xSize(), (PIConfig.SearchMode.values().length * 13) + 5);
            dialog.addChild(new StyledGuiRect("user_dialogs").setPosAndSize(dialog));
            dialog.setPos(searchBox.xPos(), searchBox.yPos() - dialog.ySize());

            int y = dialog.yPos() + 3;
            for (PIConfig.SearchMode mode : PIConfig.SearchMode.values()) {
                StyledGuiButton button = new StyledGuiButton("user_dialogs.button_style");
                button.setTrim(false);
                button.setText(I18n.format(mode.getUnlocalizedName()));
                button.setPos(dialog.xPos() + 3, y).setSize(dialog.xSize() - 6, 12);
                button.setToggleMode(true).setToggleStateSupplier(() -> PIConfig.searchMode == mode);
                button.setListener(() -> changeSearchMode(mode));
                dialog.addChild(button);
                y += 13;
            }

            dialog.show();
        });
        searchSettings.setEnabledCallback(() -> xSize() == extendedXSize);
        addChild(searchSettings);

        addChild(searchBox);
    }

    private void changeSearchMode(PIConfig.SearchMode mode) {
        PIConfig.searchMode = mode;
        PIConfig.save();
        reloadPageButtons(false);
    }

    @Override
    public void reloadElement() {
        String newController = TabManager.getButtonController();
        double pos = scrollBar.getRawPos();

        extendedXSize = Math.min(150, mainWindow.xSize() / 3);
        setXSize(extended ? extendedXSize : HIDDEN_X_SIZE);

        scrollBar.setYSize(ySize() - (NAV_BAR_SIZE + TITLE_BAR_SIZE + FOOTER_SIZE));
        scrollBar.getBackgroundElement().setSize(10, ySize() - (NAV_BAR_SIZE + TITLE_BAR_SIZE + FOOTER_SIZE));

        scrollElement.setPos(xPos() + DIR_BAR_SIZE, yPos() + TITLE_BAR_SIZE + NAV_BAR_SIZE + 1);
        scrollElement.setSize(xSize() - 12, ySize() - TITLE_BAR_SIZE - NAV_BAR_SIZE - FOOTER_SIZE - 2);
        scrollElement.setListSpacing(1);

        reloadPageButtons(true);
//        updateStyles();

        super.reloadElement();
        if (buttonController.equals(newController)) {
            scrollBar.updateRawPos(pos);
        }
        buttonController = newController;
    }

    private void reloadPageButtons(boolean updateNav) {
        scrollElement.clearElements();
        LinkedList<DocumentationPage> pages;
        String search = searchBox.getText().toLowerCase();

        if (!search.isEmpty() && !updateNav) {
            pages = searchPages(search, PIConfig.searchMode);
        }
        else {
            pages = TabManager.getSubPages();
        }

        pages.sort(Comparator.comparingInt(page -> (page.treeDepth * 5000) + page.getSortingWeight()));

        addPageButtons(pages);
        if (updateNav) {
            updateNavButtons();
        }
    }

    private LinkedList<DocumentationPage> searchPages(String search, PIConfig.SearchMode mode) {
        LinkedList<DocumentationPage> candidates = new LinkedList<>();
        LinkedList<DocumentationPage> results = new LinkedList<>();
        DocumentationPage activePage = TabManager.getActiveTab().getDocPage();

        switch (mode) {
            case EVERYWHERE:
                candidates.addAll(DocumentationManager.getAllPages());
                break;
            case SELECTED_MOD:
                DataUtils.addIf(DocumentationManager.getAllPages(), candidates, page -> page.getModid().equals(activePage.getModid()));
                break;
            case PAGE_SUB_PAGES:
                addChildPagesRecursively(activePage, candidates);
                break;
            case PAGE_ONLY:
                candidates.addAll(activePage.getSubPages());
                break;
        }

        for (DocumentationPage page : candidates) {
            if (doesPageMatchSearch(page, search) && (!page.isHidden() || PIConfig.editMode())) {
                results.add(page);
            }
        }

        return results;
    }

    private void addChildPagesRecursively(DocumentationPage page, LinkedList<DocumentationPage> list) {
        for (DocumentationPage child : page.getSubPages()) {
            list.add(child);
            addChildPagesRecursively(child, list);
        }
    }

    private boolean doesPageMatchSearch(DocumentationPage page, String search) {
        if (page.getDisplayName().toLowerCase().contains(search)) {
            return true;
        }

        for (ContentRelation relation : page.getRelations()) {
            if (relation.getLocalizedName().toLowerCase().contains(search)) {
                return true;
            }
        }

        return false;
    }

    private void addPageButtons(LinkedList<DocumentationPage> buttons) {
        for (DocumentationPage page : buttons) {
            PageButton_old button = new PageButton_old(page);
            button.setXSize(scrollElement.xSize() - (scrollBar.xSize() + 4));
            button.setXPosMod((guiButton, integer) -> scrollElement.maxXPos() - guiButton.xSize() - (scrollBar.xSize() + 3));
            scrollElement.addElement(button);
        }
    }

    @Override
    public boolean onUpdate() {
        int targetSize = extended ? extendedXSize : HIDDEN_X_SIZE;
        int moveSpeed = 30;

        if (xSize() != targetSize) {
            addToXSize(MathHelper.clip(targetSize - xSize(), -moveSpeed, moveSpeed));
            if (xSize() == targetSize) {
                mainWindow.contentWindow.reloadElement();
            }
        }

        if (StyleHandler.getBoolean("page_list.scroll_bar." + StyleType.COMPACT_BAR.getName())) {
            if (scrollBar.xSize() != 4) {
                scrollBar.setXSize(4);
                scrollBar.getBackgroundElement().setXSize(4);
                scrollBar.getSliderElement().setXSize(4);
                scrollBar.setInsets(1, 0, 1, 0);
                scrollElement.reloadElement();
                reloadPageButtons(false);
            }
        }
        else if (scrollBar.xSize() != 10) {
            scrollBar.setXSize(10);
            scrollBar.getBackgroundElement().setXSize(10);
            scrollBar.getSliderElement().setXSize(8);
            scrollBar.setInsets(1, 1, 1, 1);
            scrollElement.reloadElement();
            reloadPageButtons(false);
        }

        //Need to detect when the active page is changes externally e.g. via the API
        String openPage = TabManager.getActiveTab().pageURI;
        if (!currentPage.equals(openPage)) {
            currentPage = openPage;
            reloadPageButtons(true);
        }

        searchBox.setTextColor(searchBoxProps.textColour());
        searchBox.fillColour = searchBoxProps.colour();
        searchBox.borderColour = searchBoxProps.border();

        return super.onUpdate();
    }

    public void updateNavButtons() {
        TabManager.TabData activeTab = TabManager.getActiveTab();
        backButton.setDisabled(!activeTab.canGoBack());
        forwardButton.setDisabled(!activeTab.canGoForward());

        DocumentationPage page = TabManager.getActiveTab().getDocPage();
        currentPage = page.getPageURI();
        page = page.getParent();

        removeChildByGroup("TREE_BUTTON_GROUP");
        navButtons.clear();

        int currentLength = 0;

        while (page != null) {
            String name = page.getDisplayName().length() > 18 ? page.getDisplayName().substring(0, 16) + ".." : page.getDisplayName();
            int height = fontRenderer.getStringWidth(name) + 6;
            if (currentLength + height > ySize() - NAV_BAR_SIZE - FOOTER_SIZE) {
                if (currentLength == 0) {
                    name = fontRenderer.trimStringToWidth(page.getDisplayName(), ySize() - NAV_BAR_SIZE - FOOTER_SIZE - 8) + "..";
                    height = fontRenderer.getStringWidth(name) + 6;
                }
                else {
                    break;
                }
            }

            GuiButton newButton = new StyledGuiButton("page_list.dir_buttons", true).setText(name).setTrim(false).setShadow(false).setRotation(ROT_CC);
            newButton.setSize(DIR_BAR_SIZE - 1, height - 1);
            newButton.textXOffset = 1;
            newButton.setYPos(yPos() + NAV_BAR_SIZE);
            newButton.setXPosMod((guiButton, integer) -> xPos());
            newButton.addToGroup("TREE_BUTTON_GROUP");
            DocumentationPage thisPage = page;
            newButton.setListener(() -> {
                TabManager.openPage(thisPage.getPageURI(), false);
                mainWindow.reloadGui();
            });
            addChild(newButton);

            for (GuiButton button : navButtons) {
                button.translate(0, height);
            }

            navButtons.add(newButton);
            currentLength += newButton.ySize();

            if (page.getParent() == page) {
                page = null;
            }
            else {
                page = page.getParent();
            }
        }
    }

    @Override
    public void renderElement(Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {

        //Header
        {
            boolean shadedBorders = headerProps.shadeBorders();
            boolean vanillaT = headerProps.vanillaTex();
            int border = headerProps.border();
            int colour = headerProps.colour();
            boolean thickBorders = headerProps.thickBorders();

            if (shadedBorders || !vanillaT) {
                int light = changeShade(border, 0.2);
                int dark = changeShade(border, -0.2);

                if (shadedBorders) {
                    double b = thickBorders ? 1 : 0.5;

                    drawColouredRect(xPos(), yPos(), xSize(), NAV_BAR_SIZE, colour);                     //Bar
                    drawColouredRect(xPos(), yPos(), xSize(), b, light);                       //Bar Top Accent
                    drawColouredRect(xPos(), yPos() + NAV_BAR_SIZE - b, xSize(), b, dark);               //Bar Bottom Accent
                }
                else {
                    drawColouredRect(xPos(), yPos(), xSize(), NAV_BAR_SIZE, border);
                    drawColouredRect(xPos() + 1, yPos() + 1, xSize() - 2, NAV_BAR_SIZE - 1, colour);
                }
            }
            else {
                headerProps.glColour();
                ResourceHelperBC.bindTexture(PITextures.VANILLA_GUI_SQ);
                drawTiledTextureRectWithTrim(xPos(), yPos(), xSize(), NAV_BAR_SIZE, 4, 4, 0, 4, 0, 128, 256, 128);
                GlStateManager.color(1, 1, 1, 1);
            }
        }

        boolean thickBoarders = !bodyProps.shadeBorders() || bodyProps.thickBorders();
        double raWidth = thickBoarders ? 1 : 0.5; //Right Accent Width
        //Body
        {
            boolean shadedBorders = bodyProps.shadeBorders();
            boolean vanillaT = bodyProps.vanillaTex();
            int border = bodyProps.border();
            int colour = bodyProps.colour();

            if (shadedBorders || !vanillaT) {
                int light = changeShade(border, 0.15);
                int dark = changeShade(border, -0.15);

                if (shadedBorders) {
                    drawColouredRect(xPos(), yPos() + NAV_BAR_SIZE, xSize(), ySize() - NAV_BAR_SIZE, colour);           //Background
                    drawColouredRect(xPos() + DIR_BAR_SIZE - raWidth, yPos() + NAV_BAR_SIZE, raWidth, ySize() - NAV_BAR_SIZE - FOOTER_SIZE, dark);            //Left Divider
                    drawColouredRect(xPos() + DIR_BAR_SIZE, yPos() + NAV_BAR_SIZE, raWidth, ySize() - NAV_BAR_SIZE - FOOTER_SIZE, light);                     //Left Divider

                    drawColouredRect(xPos() + xSize() - raWidth * 2, yPos(), raWidth, ySize(), light);                  //Right Accent
                    drawColouredRect(xPos() + xSize() - raWidth, yPos(), raWidth, ySize(), dark);                       //Right Accent
                    raWidth *= 2;
                }
                else {
                    drawBorderedRect(xPos(), yPos() + NAV_BAR_SIZE, DIR_BAR_SIZE, ySize() - NAV_BAR_SIZE, 1, colour, border);

                    drawColouredRect(xPos() + DIR_BAR_SIZE, yPos() + NAV_BAR_SIZE + TITLE_BAR_SIZE, xSize() - DIR_BAR_SIZE, 1, border);
                    drawColouredRect(maxXPos() - 11, yPos() + NAV_BAR_SIZE + TITLE_BAR_SIZE + 1, 1, ySize() - NAV_BAR_SIZE - TITLE_BAR_SIZE - 1, border);
                    drawColouredRect(xPos() + DIR_BAR_SIZE, yPos() + NAV_BAR_SIZE + TITLE_BAR_SIZE + 1, xSize() - DIR_BAR_SIZE - 1, ySize() - NAV_BAR_SIZE - TITLE_BAR_SIZE - 2, colour);

                    drawColouredRect(xPos() + DIR_BAR_SIZE - raWidth, yPos() + NAV_BAR_SIZE, raWidth, ySize() - NAV_BAR_SIZE - FOOTER_SIZE, light);            //Left Divider
                    drawColouredRect(xPos() + xSize() - raWidth, yPos(), raWidth, ySize(), dark);                  //Right Accent
                }
            }
            else {
                bodyProps.glColour();
                ResourceHelperBC.bindTexture(PITextures.VANILLA_GUI_SQ);

                drawTiledTextureRectWithTrim(xPos(), yPos() + NAV_BAR_SIZE + TITLE_BAR_SIZE, xSize(), ySize() - NAV_BAR_SIZE - TITLE_BAR_SIZE, 4, 4, 4, 4, 0, 128, 256, 128);
                drawTiledTextureRectWithTrim(xPos(), yPos() + NAV_BAR_SIZE, DIR_BAR_SIZE, ySize() - NAV_BAR_SIZE - FOOTER_SIZE, 4, 4, 0, 4, 0, 128, 256, 124);

                GlStateManager.color(1, 1, 1, 1);
            }
        }

        //Footer
        {
            boolean shadedBorders = footerProps.shadeBorders();
            boolean vanillaT = footerProps.vanillaTex();
            int border = footerProps.border();
            int colour = footerProps.colour();
            boolean thickBorders = footerProps.thickBorders();

            if (shadedBorders || !vanillaT) {
                int light = changeShade(border, 0.15);
                int dark = changeShade(border, -0.15);

                if (shadedBorders) {
                    double b = thickBorders ? 1 : 0.5;

                    drawColouredRect(xPos(), yPos() + ySize() - FOOTER_SIZE, xSize() - raWidth, FOOTER_SIZE, colour);  //Background
                    drawColouredRect(xPos(), yPos() + ySize() - FOOTER_SIZE, b, FOOTER_SIZE, light);                            //Left Accent

                    drawColouredRect(xPos(), yPos() + ySize() - FOOTER_SIZE, xSize() - raWidth, b, light);            //Search Divider
                    drawColouredRect(xPos(), yPos() + ySize() - b, xSize() - raWidth, b, dark);                      //Search Divider
                }
                else {
                    drawColouredRect(xPos(), yPos() + ySize() - FOOTER_SIZE, xSize() - raWidth, FOOTER_SIZE, border);
                    drawColouredRect(xPos(), yPos() + ySize() - FOOTER_SIZE + 1, xSize() - raWidth - 1, FOOTER_SIZE - 2, colour);
                }

            }
            else {
                footerProps.glColour();
                ResourceHelperBC.bindTexture(PITextures.VANILLA_GUI_SQ);
                drawTiledTextureRectWithTrim(xPos(), yPos() + ySize() - FOOTER_SIZE, xSize(), FOOTER_SIZE, 4, 4, 4, 4, 0, 128, 256, 128);

                GlStateManager.color(1, 1, 1, 1);
            }
        }

//        DocumentationPage selected = TabManager.getActiveTab().getDocPage();
//        String page = "//" + (selected == null ? "null-Page" : selected.getDisplayName());
//        drawCustomString(fontRenderer, page, xPos() + DIR_BAR_SIZE, yPos() + NAV_BAR_SIZE + TITLE_BAR_SIZE - fontRenderer.FONT_HEIGHT, xSize() - DIR_BAR_SIZE, 0xFFFFFF, CENTER, NORMAL, false, true, false); //Todo style and stuff

        String navTitle = I18n.format("pi.gui.navigation.title");
        int width = fontRenderer.getStringWidth(navTitle);
        drawCustomString(fontRenderer, navTitle, xPos() + xSize() / 2F - width / 2F, yPos() + NAV_BAR_SIZE - fontRenderer.FONT_HEIGHT - 1, xSize() - 20, 0xFFFFFF, LEFT, NORMAL, false, true, false); //Todo style and stuff

        super.renderElement(minecraft, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (!searchBox.isMouseOver(mouseX, mouseY)) {
            searchBox.setFocused(false);
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

//    private static void updateStyles() {
//        pageButtonProps.updateProps();
//        searchBoxProps.updateProps();
//        headerProps.updateProps();
//        bodyProps.updateProps();
//        footerProps.updateProps();
//    }

    public static PropertyGroup pageButtonProps = new PropertyGroup("page_list.page_buttons");
    public static PropertyGroup searchBoxProps = new PropertyGroup("page_list.search");
    public static PropertyGroup headerProps = new PropertyGroup("page_list.header");
    public static PropertyGroup bodyProps = new PropertyGroup("page_list.body");
    public static PropertyGroup footerProps = new PropertyGroup("page_list.footer");

//    static {
//        StyleHandler.addReloadListener(GuiPartPageList::updateStyles);
//        updateStyles();
//    }
}