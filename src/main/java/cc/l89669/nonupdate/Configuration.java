package cc.l89669.nonupdate;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class Configuration {

    private final ForgeConfigSpec spec;
    private final ForgeConfigSpec.BooleanValue defaultBlocking;
    private final ConfigValue<List<? extends String>> blacklist;

    public Configuration() {
        super();
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("General settings for the mod.");
        builder.push("general");
        builder.comment("Use blacklist as whitelist?");
        this.defaultBlocking = builder.define("defaultBlocking", false);
        builder.comment("Blocking/Allowing sites");
        this.blacklist = builder.defineList("blacklist", new ArrayList<String>(), line -> true);
        this.spec = builder.build();
    }

    public ForgeConfigSpec getSpec() {
        return this.spec;
    }

    public boolean defaultBlocking() {
        return this.defaultBlocking.get().booleanValue();
    }

    public boolean checkURL(String url) {
        for (String line : blacklist.get()) {
            if (url.contains(line.toLowerCase()))
                return true;
        }
        return false;
    }

}
