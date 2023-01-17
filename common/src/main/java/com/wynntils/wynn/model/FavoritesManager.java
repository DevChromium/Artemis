/*
 * Copyright © Wynntils 2023.
 * This file is released under AGPLv3. See LICENSE for full license details.
 */
package com.wynntils.wynn.model;

import com.wynntils.core.components.Manager;
import com.wynntils.features.user.ItemFavoriteFeature;
import com.wynntils.mc.utils.ComponentUtils;
import com.wynntils.utils.Pair;
import com.wynntils.wynn.handleditems.WynnItem;
import com.wynntils.wynn.handleditems.items.game.GearBoxItem;
import com.wynntils.wynn.handleditems.items.game.IngredientItem;
import com.wynntils.wynn.handleditems.items.gui.IngredientPouchItem;
import com.wynntils.wynn.objects.profiles.ingredient.IngredientProfile;
import com.wynntils.wynn.utils.WynnUtils;
import java.util.List;
import java.util.Set;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public final class FavoritesManager extends Manager {
    private int revision = 1;

    public FavoritesManager() {
        super(List.of());
    }

    public boolean isFavorite(String unformattedName) {
        return getFavoriteItems().contains(unformattedName);
    }

    public boolean isFavorite(Component component) {
        return isFavorite(ComponentUtils.getUnformatted(component));
    }

    public boolean isFavorite(ItemStack itemStack) {
        return isFavorite(itemStack.getHoverName());
    }

    public boolean calculateFavorite(ItemStack itemStack, WynnItem wynnItem) {
        String unformattedName = WynnUtils.normalizeBadString(ComponentUtils.getUnformatted(itemStack.getHoverName()));

        if (isFavorite(unformattedName)) {
            return true;
        }

        if (wynnItem instanceof IngredientItem ingredientItem) {
            return isFavorite(ingredientItem.getIngredientProfile().getDisplayName());
        }

        if (wynnItem instanceof IngredientPouchItem pouchItem) {
            for (Pair<IngredientProfile, Integer> ingredientPair : pouchItem.getIngredients()) {
                IngredientProfile ingredientProfile = ingredientPair.a();
                if (isFavorite(ingredientProfile.getDisplayName())) {
                    return true;
                }
            }
        }

        if (wynnItem instanceof GearBoxItem gearBoxItem) {
            for (String possibleItem : gearBoxItem.getItemPossibilities()) {
                if (isFavorite(possibleItem)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void addFavorite(String unformattedName) {
        getFavoriteItems().add(unformattedName);
        revision++;
    }

    public void removeFavorite(String unformattedName) {
        getFavoriteItems().remove(unformattedName);
        revision++;
    }

    public void toggleFavorite(String unformattedName) {
        if (isFavorite(unformattedName)) {
            removeFavorite(unformattedName);
        } else {
            addFavorite(unformattedName);
        }
    }

    // Used to track changes in the set of favorites
    public int getRevision() {
        return revision;
    }

    private Set<String> getFavoriteItems() {
        // This is a hack to allow saving of favorites in the config
        return ItemFavoriteFeature.INSTANCE.favoriteItems;
    }
}