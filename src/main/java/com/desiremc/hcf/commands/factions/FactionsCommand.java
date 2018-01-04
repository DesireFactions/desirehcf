package com.desiremc.hcf.commands.factions;

import com.desiremc.core.api.newcommands.ValidBaseCommand;

public class FactionsCommand extends ValidBaseCommand
{

    public FactionsCommand()
    {
        super("factions", "All factions commands.", new String[] { "f", "faction", "fact" });

        addSubCommand(new FactionAnnounceCommand());
        addSubCommand(new FactionAllyCommand());
        addSubCommand(new FactionBalanceCommand());
        addSubCommand(new FactionBypassCommand());
        addSubCommand(new FactionChatCommand());
        addSubCommand(new FactionClaimCommand());
        //addSubCommand(new FactionConfigCommand());
        addSubCommand(new FactionCreateCommand());
        addSubCommand(new FactionDeinviteCommand());
        addSubCommand(new FactionDemoteCommand());
        addSubCommand(new FactionDepositCommand());
        addSubCommand(new FactionDescriptionCommand());
        addSubCommand(new FactionDisbandCommand());
        addSubCommand(new FactionDTRCommand());
        //addSubCommand(new FactionFocusCommand());
        addSubCommand(new FactionHomeCommand());
        addSubCommand(new FactionInviteCommand());
        addSubCommand(new FactionInvitesCommand());
        addSubCommand(new FactionJoinCommand());
        addSubCommand(new FactionKickCommand());
        addSubCommand(new FactionLeaderCommand());
        addSubCommand(new FactionLeaderboardCommand());
        addSubCommand(new FactionLeaveCommand());
        addSubCommand(new FactionListCommand());
        addSubCommand(new FactionLocationCommand());
        addSubCommand(new FactionLogsCommand());
        addSubCommand(new FactionMapCommand());
        addSubCommand(new FactionNameCommand());
        addSubCommand(new FactionNeutralCommand());
        addSubCommand(new FactionPromoteCommand());
        addSubCommand(new FactionSethomeCommand());
        addSubCommand(new FactionShowCommand());
        addSubCommand(new FactionStuckCommand());
        addSubCommand(new FactionUnclaimCommand());
        addSubCommand(new FactionWithdrawCommand());
    }

}
