package com.lunarclient.bukkitapi.cooldown;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

import java.util.*;

@UtilityClass
public class LunarClientAPICooldown {

    private final Map<String, LCCooldown> registeredCooldowns = new HashMap<>();

    /**
     * Used to register a persisting cooldown.
     * Ideally 1 {@link LCCooldown} will be created for each task.
     * Create the {@link LCCooldown} once, register it and then send it to each player.
     *
     * @param cooldown The cooldown to register
     */
    public void registerCooldown(LCCooldown cooldown) {
        registeredCooldowns.put(cooldown.getName().toLowerCase(), cooldown);
    }

    /**
     * Used to unregister a persisting cooldown.
     * If a cooldown will no longer be used or in shutdown, unregistering is a good idea.
     *
     * @param cooldown The cooldown to register
     */
    public void unregisterCooldown(LCCooldown cooldown) {
        registeredCooldowns.remove(cooldown.getName().toLowerCase());
    }

    /**
     * Sends a cooldown to a Lunar Client player that has previously been registered.
     * This could be used instead of passing around a {@link LCCooldown} instance.
     *
     * @param player The player to send a cooldown to
     * @param cooldownName The name of the {@link LCCooldown} that is sent.
     */
    public void sendCooldown(Player player, String cooldownName) {
        String cooldownId = cooldownName.toLowerCase();
        if (!registeredCooldowns.containsKey(cooldownId)) {
            throw new IllegalStateException("Attempted to send a cooldown that isn't registered [" + cooldownName + "]");
        }
        registeredCooldowns.get(cooldownId).send(player);
    }

    /**
     * Removes the {@link LCCooldown} associated with the provided cooldown name that is
     * currently registered from a Lunar Client player
     *
     * @param player The player to remove the cooldown for
     * @param cooldownName The name of the {@link LCCooldown} to remove
     */
    public void clearCooldown(Player player, String cooldownName) {
        String cooldownId = cooldownName.toLowerCase();
        if (!registeredCooldowns.containsKey(cooldownId)) {
            throw new IllegalStateException("Attempted to send a cooldown that isn't registered [" + cooldownName + "]");
        }
        registeredCooldowns.get(cooldownId).clear(player);
    }

    /**
     * Checks to see if the provided cooldown name is a currently registered cooldown
     *
     * @param cooldownName The name of the {@link LCCooldown} to check
     * @return {@code true} if the provided name is currently registered
     */
    public boolean isCooldownRegistered(String cooldownName) {
        return registeredCooldowns.containsKey(cooldownName);
    }

    /**
     * Gets an {@link Optional} that either contains the {@link LCCooldown} associated with
     * the provided name or an empty {@link Optional} if {@link #isCooldownRegistered(String)}
     * returns {@code false} for the provided name.
     *
     * @param cooldownName The name of the {@link LCCooldown} to get
     * @return An {@link Optional} that either contains the {@link LCCooldown} associated with
     * the provided name or an empty {@link Optional} if {@link #isCooldownRegistered(String)}
     * returns {@code false} for the provided name.
     */
    public Optional<LCCooldown> getCooldown(String cooldownName) {

        Optional<LCCooldown> cooldownOptional;

        if(isCooldownRegistered(cooldownName)) {
            cooldownOptional = Optional.of(registeredCooldowns.get(cooldownName));
        }
        else {
            cooldownOptional = Optional.empty();
        }

        return cooldownOptional;
    }
}
