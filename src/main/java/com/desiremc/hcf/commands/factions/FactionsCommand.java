package com.desiremc.hcf.commands.factions;

import com.desiremc.core.api.newcommands.ValidBaseCommand;

public class FactionsCommand extends ValidBaseCommand
{

    public FactionsCommand()
    {
        super("factions", "All factions commands.", new String[] {"f", "faction", "fact"});

        addSubCommand(new FactionAnnounceCommand());
        addSubCommand(new FactionBypassCommand());
        addSubCommand(new FactionClaimCommand());
        //addSubCommand(new FactionConfigCommand());
        addSubCommand(new FactionCreateCommand());
        addSubCommand(new FactionDescriptionCommand());
        addSubCommand(new FactionDisbandCommand());
        addSubCommand(new FactionDTRCommand());
        //addSubCommand(new FactionFocusCommand());
        addSubCommand(new FactionHomeCommand());
        addSubCommand(new FactionLeaderCommand());
        addSubCommand(new FactionLocationCommand());
        addSubCommand(new FactionSethomeCommand());
        addSubCommand(new FactionShowCommand());
        addSubCommand(new FactionInviteCommand());
        addSubCommand(new FactionDeinviteCommand());
        addSubCommand(new FactionLeaveCommand());
        addSubCommand(new FactionKickCommand());
        addSubCommand(new FactionJoinCommand());
        addSubCommand(new FactionPromoteCommand());
        addSubCommand(new FactionDemoteCommand());
        addSubCommand(new FactionBalanceCommand());
        addSubCommand(new FactionWithdrawCommand());
        addSubCommand(new FactionDepositCommand());
        addSubCommand(new FactionChatCommand());
        addSubCommand(new FactionAllyCommand());
        addSubCommand(new FactionNeutralCommand());
        addSubCommand(new FactionNameCommand());
        addSubCommand(new FactionInvitesCommand());
        addSubCommand(new FactionListCommand());
        addSubCommand(new FactionLeaderboardCommand());
        addSubCommand(new FactionMapCommand());
        addSubCommand(new FactionUnclaimCommand());
        addSubCommand(new FactionLogsCommand());
    }

}
