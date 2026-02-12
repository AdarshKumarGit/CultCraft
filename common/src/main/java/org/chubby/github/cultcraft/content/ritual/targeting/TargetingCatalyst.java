package org.chubby.github.cultcraft.content.ritual.targeting;

import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import org.chubby.github.cultcraft.content.ritual.compoents.ICatalyst;
import java.util.UUID;
import java.util.Optional;

//TODO::REPLACE VIAL,EFFIGY,FOCUS WITH THEIR RESPECTIVE ITEMS!
public class TargetingCatalyst implements ICatalyst
{

    public static class BloodVial implements ICatalyst
    {
        private final ItemStack vial;

        public BloodVial(ItemStack stack)
        {
            this.vial = stack;
        }

        public Optional<UUID> getStoredEntityUUID()
        {
            CompoundTag tag = vial.getTag();
            if (tag != null && tag.hasUUID("StoredEntityUUID"))
            {
                return Optional.of(tag.getUUID("StoredEntityUUID"));
            }
            return Optional.empty();
        }

        public Optional<String> getStoredEntityName()
        {
            CompoundTag tag = vial.getTag();
            if (tag != null && tag.contains("StoredEntityName"))
            {
                return Optional.of(tag.getString("StoredEntityName"));
            }
            return Optional.empty();
        }

        public static ItemStack createVial(UUID entityUUID, String entityName)
        {
            ItemStack stack = ItemStack.EMPTY;
            CompoundTag tag = stack.getOrCreateTag();
            tag.putUUID("StoredEntityUUID", entityUUID);
            tag.putString("StoredEntityName", entityName);
            tag.putLong("CollectionTime", System.currentTimeMillis());
            return stack;
        }

        public boolean isExpired()
        {
            CompoundTag tag = vial.getTag();
            if (tag != null && tag.contains("CollectionTime"))
            {
                long collectionTime = tag.getLong("CollectionTime");
                long currentTime = System.currentTimeMillis();
                long dayInMillis = 24 * 60 * 60 * 1000;
                return (currentTime - collectionTime) > (7 * dayInMillis);
            }
            return true;
        }
    }

    public static class Effigy implements ICatalyst
    {
        private final ItemStack effigy;

        public Effigy(ItemStack stack)
        {
            this.effigy = stack;
        }

        public Optional<String> getBoundPlayerName()
        {
            CompoundTag tag = effigy.getTag();
            if (tag != null && tag.contains("BoundPlayer"))
            {
                return Optional.of(tag.getString("BoundPlayer"));
            }
            return Optional.empty();
        }

        public Optional<UUID> getBoundPlayerUUID()
        {
            CompoundTag tag = effigy.getTag();
            if (tag != null && tag.hasUUID("BoundPlayerUUID"))
            {
                return Optional.of(tag.getUUID("BoundPlayerUUID"));
            }
            return Optional.empty();
        }

        public boolean hasPersonalItem()
        {
            CompoundTag tag = effigy.getTag();
            return tag != null && tag.getBoolean("HasPersonalItem");
        }

        public int getPowerLevel()
        {
            int level = 0;
            CompoundTag tag = effigy.getTag();
            if (tag == null) return level;

            if (tag.getBoolean("HasPersonalItem")) level++;
            if (tag.getBoolean("HasHair")) level++;
            if (tag.getBoolean("HasBlood")) level++;
            if (tag.getBoolean("HasNail")) level++;

            return level;
        }

        public static ItemStack createEffigy(UUID playerUUID, String playerName)
        {
            ItemStack stack = ItemStack.EMPTY;
            CompoundTag tag = stack.getOrCreateTag();
            tag.putUUID("BoundPlayerUUID", playerUUID);
            tag.putString("BoundPlayer", playerName);
            tag.putBoolean("HasPersonalItem", false);
            tag.putBoolean("HasHair", false);
            tag.putBoolean("HasBlood", false);
            tag.putBoolean("HasNail", false);
            return stack;
        }
    }

    public static class BoundToken implements ICatalyst
    {
        private final ItemStack token;

        public BoundToken(ItemStack stack)
        {
            this.token = stack;
        }

        public Optional<UUID> getBoundEntityUUID()
        {
            CompoundTag tag = token.getTag();
            if (tag != null && tag.hasUUID("BoundEntity"))
            {
                return Optional.of(tag.getUUID("BoundEntity"));
            }
            return Optional.empty();
        }

        public Optional<String> getBindingType()
        {
            CompoundTag tag = token.getTag();
            if (tag != null && tag.contains("BindingType"))
            {
                return Optional.of(tag.getString("BindingType"));
            }
            return Optional.empty();
        }

        public int getRemainingUses()
        {
            CompoundTag tag = token.getTag();
            if (tag != null && tag.contains("RemainingUses"))
            {
                return tag.getInt("RemainingUses");
            }
            return 0;
        }

        public void consumeUse()
        {
            CompoundTag tag = token.getOrCreateTag();
            int uses = tag.getInt("RemainingUses");
            tag.putInt("RemainingUses", Math.max(0, uses - 1));
        }

        public static ItemStack createToken(UUID entityUUID, String bindingType, int uses)
        {
            ItemStack stack = ItemStack.EMPTY;
            CompoundTag tag = stack.getOrCreateTag();
            tag.putUUID("BoundEntity", entityUUID);
            tag.putString("BindingType", bindingType);
            tag.putInt("RemainingUses", uses);
            return stack;
        }
    }

    public static class AstralFocus implements ICatalyst
    {
        private final ItemStack focus;

        public AstralFocus(ItemStack stack)
        {
            this.focus = stack;
        }

        public Optional<UUID> getScryedEntityUUID()
        {
            CompoundTag tag = focus.getTag();
            if (tag != null && tag.hasUUID("ScryedEntity"))
            {
                return Optional.of(tag.getUUID("ScryedEntity"));
            }
            return Optional.empty();
        }

        public boolean isAttuned()
        {
            CompoundTag tag = focus.getTag();
            return tag != null && tag.getBoolean("Attuned");
        }

        public long getLastScryTime()
        {
            CompoundTag tag = focus.getTag();
            if (tag != null && tag.contains("LastScryTime"))
            {
                return tag.getLong("LastScryTime");
            }
            return 0;
        }

        public static ItemStack createFocus(UUID entityUUID)
        {
            ItemStack stack = ItemStack.EMPTY;
            CompoundTag tag = stack.getOrCreateTag();
            tag.putUUID("ScryedEntity", entityUUID);
            tag.putBoolean("Attuned", true);
            tag.putLong("LastScryTime", System.currentTimeMillis());
            return stack;
        }
    }
}