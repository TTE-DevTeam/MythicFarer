package de.dertoaster.mythicfarer.modules.skill.remotesign.mechanic;

import de.dertoaster.mythicfarer.modules.skill.AbstractTargetedCraftSkill;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import net.countercraft.movecraft.MovecraftLocation;
import net.countercraft.movecraft.config.Settings;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.craft.SinkingCraft;
import net.countercraft.movecraft.localisation.I18nSupport;
import net.countercraft.movecraft.sign.AbstractMovecraftSign;
import net.countercraft.movecraft.sign.CraftSignManager;
import net.countercraft.movecraft.sign.RemoteSign;
import net.countercraft.movecraft.sign.SignListener;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Tag;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.event.block.Action;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class RemoteSignMechanic extends AbstractTargetedCraftSkill {

    protected final String targetIdent;
    protected final Action clickType;

    public RemoteSignMechanic(MythicLineConfig mlc) {
        super(mlc);
        this.targetIdent = mlc.getString("targetIdent", "");
        this.clickType = mlc.getEnum(new String[]{"clickType"}, Action.class, Action.RIGHT_CLICK_BLOCK);
    }

    @Override
    public SkillResult castAtCraft(SkillMetadata skillMetadata, AbstractEntity entityTarget, Craft craftTarget) {
        if (craftTarget instanceof SinkingCraft) {
            return SkillResult.INVALID_TARGET;
        }
        final Entity interactor = entityTarget.getBukkitEntity();

        Map<AbstractMovecraftSign, LinkedList<SignListener.SignWrapper>> foundTargetSigns = new HashMap<>();
        boolean firstError = true;
        final CraftSignManager signManager = CraftSignManager.of(craftTarget);
        if (signManager == null) {
            return SkillResult.SUCCESS;
        }
        for (MovecraftLocation tloc : signManager.getSignsOfClass(AbstractMovecraftSign.class)) {
            BlockState tstate = craftTarget.getWorld().getBlockAt(tloc.getX(), tloc.getY(), tloc.getZ()).getState();
            if (!Tag.ALL_SIGNS.isTagged(tstate.getType())) {
                continue;
            }
            if (!(tstate instanceof Sign)) {
                continue;
            }
            Sign ts = (Sign) tstate;

            SignListener.SignWrapper[] targetSignWrappers = SignListener.INSTANCE.getSignWrappers(ts);

            if (targetSignWrappers != null) {
                for (SignListener.SignWrapper wrapper : targetSignWrappers) {
                    // Matches source?
                    final String signHeader = PlainTextComponentSerializer.plainText().serialize(wrapper.line(0));
                    AbstractMovecraftSign signHandler = AbstractMovecraftSign.get(signHeader);
                    // Ignore other remove signs
                    if (signHandler == null || signHandler instanceof RemoteSign) {
                        continue;
                    }
                    // But does it match the source man?
                    if (RemoteSign.matchesDescriptor(targetIdent, wrapper)) {
                        // Forbidden strings
                        if (RemoteSign.hasForbiddenString(wrapper)) {
                            if (firstError) {
                                Bukkit.getConsoleSender().sendMessage(I18nSupport.getInternationalisedString("Remote Sign - Forbidden string found"));
                                firstError = false;
                            }
                            interactor.sendMessage(" - ".concat(tloc.toString()).concat(" : ").concat(ts.getLine(0)));
                        } else {
                            LinkedList<SignListener.SignWrapper> value = foundTargetSigns.computeIfAbsent(signHandler, (a) -> new LinkedList<>());
                            value.add(wrapper);
                        }
                    }
                }
            }
        }
        if (!firstError) {
            return SkillResult.CONDITION_FAILED;
        }
        else if (foundTargetSigns.isEmpty()) {
            Bukkit.getConsoleSender().sendMessage(I18nSupport.getInternationalisedString("Remote Sign - Could not find target sign"));
            return SkillResult.CONDITION_FAILED;
        }

        if (Settings.MaxRemoteSigns > -1) {
            int foundLocCount = foundTargetSigns.size();
            if(foundLocCount > Settings.MaxRemoteSigns) {
                Bukkit.getConsoleSender().sendMessage(String.format(I18nSupport.getInternationalisedString("Remote Sign - Exceeding maximum allowed"), foundLocCount, Settings.MaxRemoteSigns));
                return SkillResult.CONDITION_FAILED;
            }
        }

        // call the handlers!
        foundTargetSigns.entrySet().forEach(entry -> {
            AbstractMovecraftSign signHandler = entry.getKey();
            for (SignListener.SignWrapper wrapper : entry.getValue()) {
                signHandler.processSignClick(clickType, wrapper, interactor);
            }
        });

        return SkillResult.SUCCESS;
    }
}
