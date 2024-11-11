package net.steampn.createhorsepower.registry;

import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import net.steampn.createhorsepower.client.ponders.HorseCrankBaseScene;

import static net.steampn.createhorsepower.CreateHorsePower.MODID;

public class PonderRegistry {
    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(MODID);

    public static void register(){
        HELPER.forComponents(BlockRegister.HORSE_CRANK)
                .addStoryBoard("horse_crank/horse_crank_intro", HorseCrankBaseScene::intro);
    }
}
