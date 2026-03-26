package com.ricardthegreat.holdmetight.mixins;

import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import com.ricardthegreat.holdmetight.HoldMeTight;

import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.versions.forge.ForgeVersion;

public class HoldMeTightMixinConfigPlugin implements IMixinConfigPlugin {

    private static final String MIXIN_PACKAGE = "com.ricardthegreat.holdmetight.mixins";
    private static final ArtifactVersion FORGE_VERSION = new DefaultArtifactVersion(ForgeVersion.getVersion());
    private static final String FORGE_CUTOFF = "47.4.1";

    @Override
    public void onLoad(String mixinPackage) {
        if (!mixinPackage.startsWith(MIXIN_PACKAGE))
		{
			throw new IllegalArgumentException(
				String.format("Invalid package: Expected \"%s\", but found \"%s\".", MIXIN_PACKAGE, mixinPackage)
			);
		}
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.equals("com.ricardthegreat.holdmetight.mixins.pehkui.IForgePlayerMixin")) {
            if (FORGE_VERSION.compareTo(new DefaultArtifactVersion(FORGE_CUTOFF)) <= 0) {
                HoldMeTight.LOGGER.error("cancelling mixin:"+mixinClassName+" i recommend using the latest version of forge to prevent errors");
                return false;
            }
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
    
}
