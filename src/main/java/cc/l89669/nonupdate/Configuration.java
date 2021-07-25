package cc.l89669.nonupdate;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;

public class Configuration {

    private final ForgeConfigSpec spec;
    private final EnumValue<LoggingLevel> loggingLevel;
    private final ForgeConfigSpec.BooleanValue defaultBlocking;
    private final ConfigValue<List<? extends String>> blacklist;

    public Configuration() {
        super();
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("General settings for the mod.");
        builder.push("general");
        builder.comment("Logging level: MUTE, BLOCKED, DETAILED, VERBOSE", "MUTE: do not output anything", "BLOCKED: output blocked info", "DETAILED: output detailed blocked info", "VERBOSE: output every attemption, blocked and permitted");
        this.loggingLevel = builder.defineEnum("loggingLevel", LoggingLevel.BLOCKED);
        builder.comment("Default block all address and use blacklist as whitelist?");
        this.defaultBlocking = builder.define("defaultBlocking", false);
        builder.comment("Blocked/Allowed domain/ip address");
        this.blacklist = builder.defineList("blacklist", Lists.newArrayList("raw.githubusercontent.com", "yumc.pw"), line -> true);
        this.spec = builder.build();
    }

    public ForgeConfigSpec getSpec() {
        return this.spec;
    }

    public LoggingLevel loggingLevel() {
        return this.loggingLevel.get();
    }

    public boolean defaultBlocking() {
        return this.defaultBlocking.get().booleanValue();
    }

    public boolean checkBlockURL(String url) {
        for (String line : blacklist.get()) {
            if (url.contains(line.toLowerCase()))
                return this.defaultBlocking() ^ true;
        }
        return this.defaultBlocking() ^ false;
    }

}
