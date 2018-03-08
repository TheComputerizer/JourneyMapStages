package net.darkhax.jmapstages;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;

import journeymap.client.forge.event.KeyEventHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class JMapActionManager {
    
    private ListMultimap<Integer, Object> minimapPreviewActions;
    private ListMultimap<Integer, Object> inGameActions;
    private ListMultimap<Integer, Object> inGuiActions;
    
    private Multimap<String, Object> sortedActions;
    
    private Map<String, Object> minimapPreviewActionsSorted;
    private Map<String, Object> inGameActionsSorted;
    private Map<String, Object> inGuiActionsSorted;
    
    public void loadActions () {
        
        this.sortedActions = ArrayListMultimap.create();
        
        this.minimapPreviewActions = ReflectionHelper.getPrivateValue(KeyEventHandler.class, KeyEventHandler.INSTANCE, "minimapPreviewActions");
        this.inGameActions = ReflectionHelper.getPrivateValue(KeyEventHandler.class, KeyEventHandler.INSTANCE, "inGameActions");
        this.inGuiActions = ReflectionHelper.getPrivateValue(KeyEventHandler.class, KeyEventHandler.INSTANCE, "inGuiActions");
        
        this.minimapPreviewActionsSorted = this.sortActionMap(this.minimapPreviewActions);
        this.inGameActionsSorted = this.sortActionMap(this.inGameActions);
        this.inGuiActionsSorted = this.sortActionMap(this.inGuiActions);
    }
    
    public Collection<Object> getAction (String key) {
        
        return this.sortedActions.get(key);
    }
    
    public void disableAction (String actionKey) {
        
        for (Object action : this.getAction(actionKey)) {
            
            this.disableAction(action);
        }
    }
    
    public void disableAction (Object actionObj) {
        
        this.minimapPreviewActions.values().remove(actionObj);
        this.inGameActions.values().remove(actionObj);
        this.inGuiActions.values().remove(actionObj);
    }
    
    public void enableAction (String actionKey) {
        
        for (Object action : this.getAction(actionKey)) {
            
            this.enableAction(action);
        }
    }
    
    public void enableAction (Object actionObj) {
        
        attemptAdding(actionObj, this.minimapPreviewActions, this.minimapPreviewActionsSorted);
        attemptAdding(actionObj, this.inGameActions, this.inGameActionsSorted);
        attemptAdding(actionObj, this.inGuiActions, this.inGuiActionsSorted);
    }
    
    public void toggleAction (String actionKey, boolean enable) {
        
        for (Object action : this.getAction(actionKey)) {
            
            this.toggleAction(action, enable);
        }
    }
    
    public void toggleAction (Object actionObj, boolean enable) {
        
        if (enable) {
            
            this.enableAction(actionObj);
        }
        
        else {
            
            this.disableAction(actionObj);
        }
    }
    
    private static void attemptAdding (Object actionObj, ListMultimap<Integer, Object> data, Map<String, Object> sorted) {
        
        final KeyBinding key = getKeyFromAction(actionObj);
        
        if (key != null && sorted.containsKey(key.getKeyDescription()) && !data.get(key.getKeyCode()).contains(actionObj)) {
            
            data.put(key.getKeyCode(), actionObj);
        }
    }
    
    private Map<String, Object> sortActionMap (ListMultimap<Integer, Object> inputMap) {
        
        final Map<String, Object> actionMap = new HashMap<>();
        
        for (final Object actionObj : inputMap.values()) {
            
            final KeyBinding action = getKeyFromAction(actionObj);
            
            if (action != null) {
                
                actionMap.put(action.getKeyDescription(), actionObj);
                this.sortedActions.put(action.getKeyDescription(), actionObj);
            }
        }
        
        return actionMap;
    }
    
    private static KeyBinding getKeyFromAction (Object action) {
        
        try {
            
            final Method getKeyBind = action.getClass().getDeclaredMethod("getKeyBinding");
            getKeyBind.setAccessible(true);
            return (KeyBinding) getKeyBind.invoke(action);
        }
        
        catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            
            e.printStackTrace();
        }
        
        return null;
    }
}