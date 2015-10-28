/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.duo.magicallyous.utils;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import org.duo.magicallyous.Main;
//import tonegod.gui.controls.buttons.Button;
//import tonegod.gui.controls.extras.Indicator;
//import tonegod.gui.controls.lists.Table;
//import tonegod.gui.controls.windows.Panel;
//import tonegod.gui.controls.windows.Window;
//import tonegod.gui.core.Element;
//import tonegod.gui.core.Screen;

/**
 *
 * @author duo
 */
public class ToneGodGuiState extends AbstractAppState {

    Main app;
    Node player;
    Node actualScene;
    Node underworld;
/*
    private Screen screen;
    Indicator healthBarIndicator;
    Table statsTable;
    int playerDamage;
    int playerDefense;
*/
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (Main) app;
        System.out.println("Starting toneGod!");
        actualScene = (Node) this.app.getRootNode().getChild(this.app.getActualSceneName());
        player = (Node) actualScene.getChild("player");
/*
        playerDamage = getPlayerDamage();
        playerDefense = getPlayerDefense();
        screen = new Screen(this.app);
        this.app.getGuiNode().addControl(screen);
        Panel footerPanel = getFooterPanel();
        screen.addElement(footerPanel);
        healthBarIndicator = getHealthBarIndicator();
        footerPanel.addChild(healthBarIndicator);
        Window window = getStatsElement();
        screen.addElement(window);
        if (this.app.getActualSceneName().equals(this.app.getUnderworldSceneName())) {
            Button revive = getReviveButton();
            screen.addElement(revive);
        }
    }

    @Override
    public void update(float tpf) {
        int health = getPlayerHealth();
        player.setUserData("health", health);
        healthBarIndicator.setCurrentValue(getPlayerHealth());
        if(playerDamage != getPlayerDamage()) {
            playerDamage = getPlayerDamage();
            Element e = (Element) statsTable.getRow(1).getChild(1);
            e.setText("" + playerDamage);
        }
        if(playerDefense != getPlayerDefense()) {
            playerDefense = getPlayerDefense();
            Element e = (Element) statsTable.getRow(2).getChild(1);
            e.setText("" + playerDefense);
        }
    }

    @Override
    public void cleanup() {
        super.cleanup();
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
        //this is called on the OpenGL thread after the AppState has been detached
    }

    private Panel getFooterPanel() {
        Panel panel = new Panel(screen, "Panel",
                new Vector2f(0, screen.getHeight() * 0.9f),
                new Vector2f(screen.getWidth(), screen.getHeight() * 0.05f),
                new Vector4f(14, 14, 14, 14),
                "tonegod/gui/style/def/Window/panel_x.png");
        panel.getElementMaterial().setColor("Color", new ColorRGBA(0.0f, 0.0f, 0.0f, 0.1f));
        return panel;
    }

    private Indicator getHealthBarIndicator() {
        Indicator indicator = new Indicator(screen, "indicator", new Vector2f(10, 10), 
                new Vector2f(screen.getWidth() * 0.20f, screen.getHeight() * 0.02f),
                Element.Orientation.HORIZONTAL) {
            @Override
            public void onChange(float currentValue, float currentPercentage) {
                if (currentPercentage <= 0) {
                    this.setIndicatorColor(ColorRGBA.Red);
                } else {
                    float value = 0.01f * currentPercentage;
                    ColorRGBA newColor = new ColorRGBA();
                    newColor.interpolate(new ColorRGBA(1.0f, 0.0f, 0.0f, 0.2f), 
                            new ColorRGBA(0.0f, 1.0f, 0.0f, 0.2f), value);
                    this.setIndicatorColor(newColor);
                }
            }
        };
        indicator.setMaxValue(100);
        indicator.setCurrentValue(100);
        indicator.setDisplayPercentage();
        indicator.setIndicatorColor(new ColorRGBA(1.0f, 0.0f, 0.0f, 0.4f));
        //indicator.setTextPosition(5, 5);
        indicator.setTextVAlign(BitmapFont.VAlign.Center);
        indicator.setTextAlign(BitmapFont.Align.Left);
        indicator.setText("Health Bar");
        return indicator;
    }

    private Window getStatsElement() {
        statsTable = new Table(screen, 
                new Vector2f(screen.getWidth() * 0.75f, screen.getHeight() * 0.75f), 
                new Vector2f(screen.getWidth() * 0.15f, screen.getHeight() * 0.10f)
                ) {
            @Override
            public void onChange() {
            }
        };
        statsTable.setHeadersVisible(false);
        Table.TableColumn nameColumn = new Table.TableColumn(statsTable, screen, "nameColumn");
        Table.TableColumn valueColumn = new Table.TableColumn(statsTable, screen, "valueColumn");
        statsTable.addColumn(nameColumn);
        statsTable.addColumn(valueColumn);
        Table.TableRow emptyRow = new Table.TableRow(screen, statsTable);
        emptyRow.setAsContainerOnly();
        emptyRow.addCell("", "e1");
        emptyRow.addCell("", "e2");
        statsTable.addRow(emptyRow, true);
        Table.TableRow damageRow = new Table.TableRow(screen, statsTable);
        damageRow.setAsContainerOnly();
        damageRow.addCell("Damage", "damage");
        damageRow.addCell("" + getPlayerDamage(), "damValue");
        for(Spatial s : damageRow.getChildren()) {
            System.out.println("TADA: " + s.getName() + " : " + s.toString());
        }
        Element e = (Element) damageRow.getChild(1);
        e.setTextAlign(BitmapFont.Align.Right);
        statsTable.addRow(damageRow, true);
        Table.TableRow defenseRow = new Table.TableRow(screen, statsTable);
        defenseRow.setAsContainerOnly();
        defenseRow.addCell("Defense", "defense");
        defenseRow.addCell("" + getPlayerDefense(), "defValue");
        e = (Element) defenseRow.getChild(1);
        e.setTextAlign(BitmapFont.Align.Right);
        statsTable.addRow(defenseRow, true);
        statsTable.setIsMovable(true);
        statsTable.setCollapseChildrenOnParentCollapse(true);
        statsTable.setAsContainerOnly();
        Window window = new Window(screen, 
                new Vector2f(screen.getWidth() * 0.75f, screen.getHeight() * 0.75f), 
                new Vector2f(screen.getWidth() * 0.15f, screen.getHeight() * 0.10f)
                );
        window.addChild(statsTable);
        statsTable.centerToParent();
        window.getElementMaterial().setColor("Color", new ColorRGBA(0.0f, 0.0f, 0.0f, 0.4f));
        window.setIsMovable(true);
        window.setIsResizable(true);
        window.setText(getPlayerName() + "'s STATS");
        window.setTextAlign(BitmapFont.Align.Center);
        window.setTextPadding(10.0f);
        return window;
    }

    private Button getReviveButton() {
        Button revive = new Button(screen, new Vector2f(screen.getWidth()* 0.50f, screen.getHeight()* 0.20f)) {
            @Override
            public void onButtonMouseLeftDown(MouseButtonEvent mbe, boolean bln) {
                //((Main) app).switchAppState(new MagicallyousAppState());
            }
            
            @Override
            public void onButtonMouseRightDown(MouseButtonEvent mbe, boolean bln) {
            }
            
            @Override
            public void onButtonMouseLeftUp(MouseButtonEvent mbe, boolean bln) {
            }
            
            @Override
            public void onButtonMouseRightUp(MouseButtonEvent mbe, boolean bln) {
            }
            
            @Override
            public void onButtonFocus(MouseMotionEvent mme) {
            }
            
            @Override
            public void onButtonLostFocus(MouseMotionEvent mme) {
            }
        };
        revive.setLabelText("Revive");
        return revive;
    }

    public String getPlayerName() {
        return player.getUserData("name");
    }

    public int getPlayerHealth() {
        return player.getUserData("health");
    }

    public int getPlayerDamage() {
        return player.getUserData("damage");
    }

    public int getPlayerDefense() {
        return player.getUserData("defense");
*/
        }

    }
