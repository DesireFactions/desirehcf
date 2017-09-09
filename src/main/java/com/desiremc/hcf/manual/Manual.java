package com.desiremc.hcf.manual;


import java.util.List;

import com.desiremc.hcf.session.Rank;

public interface Manual {

    Rank getRank();

    List<ManualPage> getPages();

}
