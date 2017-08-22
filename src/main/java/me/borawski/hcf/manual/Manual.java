package me.borawski.hcf.manual;


import me.borawski.hcf.session.Rank;

import java.util.List;

public interface Manual {

    Rank getRank();

    List<ManualPage> getPages();

}
