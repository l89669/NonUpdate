package cc.l89669.nonupdate;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.electronwill.nightconfig.core.file.FileNotFoundAction;
import com.electronwill.nightconfig.toml.TomlFormat;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod("nonupdate")
public class NonUpdate {
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static Configuration configuration;

    public NonUpdate() {
        configuration = new Configuration();
        configuration.getSpec().setConfig(TomlFormat.instance().createParser().parse(new File("config", "nonupdate-common.toml"), FileNotFoundAction.READ_NOTHING));
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, configuration.getSpec());
        // Register the setup method for modloading
        // FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        System.setSecurityManager(new NetworkManager());
    }

}
