package net.dinglezz.torgrays_trials.effect;

import net.dinglezz.torgrays_trials.entity.Mob;

public class GenerousCoins extends Effect {
    public GenerousCoins(int duration, Mob host) {
        super("Generous Coins", duration, host);
        registerEffectImage("entity/item/coin");
    }

    @Override
    public void onApply() {}

    @Override
    public void during() {}

    @Override
    public void onEnd() {}
}
