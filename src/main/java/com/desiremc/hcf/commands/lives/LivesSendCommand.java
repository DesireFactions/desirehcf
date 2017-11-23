package com.desiremc.hcf.commands.lives;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.IntegerParser;
import com.desiremc.core.parsers.PlayerParser;
import com.desiremc.core.session.Rank;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;
import com.desiremc.hcf.validator.PlayerHasEnoughLivesValidator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LivesSendCommand extends ValidCommand
{
    public LivesSendCommand()
    {
        super("send", "send lives", Rank.GUEST, new String[] {"target", "amount"});
        addParser(new PlayerParser(), "target");
        addParser(new IntegerParser(), "amount");

        addValidator(new PlayerHasEnoughLivesValidator(), "amount");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player player = (Player) args[0];
        int amount = (Integer) args[1];

        HCFSession target = HCFSessionHandler.getHCFSession(player.getUniqueId());
        HCFSession source = HCFSessionHandler.getHCFSession(sender);
        source.takeLives(amount);
        target.addLives(amount);

        DesireHCF.getLangHandler().sendRenderMessage(sender, "lives.send", "{amount}", String.valueOf(amount), "{player}", target.getName());
        DesireHCF.getLangHandler().sendRenderMessage(player, "lives.send_target", "{amount}", String.valueOf(amount), "{player}", sender.getName());
    }
}
