package org.bytedancer.crayzer.design_mode_pattern.creational.builder.sample01;

import org.bytedancer.crayzer.design_mode_pattern.creational.builder.sample01.ceilling.LevelOneCeiling;
import org.bytedancer.crayzer.design_mode_pattern.creational.builder.sample01.ceilling.LevelTwoCeiling;
import org.bytedancer.crayzer.design_mode_pattern.creational.builder.sample01.coat.DuluxCoat;
import org.bytedancer.crayzer.design_mode_pattern.creational.builder.sample01.coat.LiBangCoat;
import org.bytedancer.crayzer.design_mode_pattern.creational.builder.sample01.floor.ShengXiangFloor;
import org.bytedancer.crayzer.design_mode_pattern.creational.builder.sample01.tile.DongPengTile;
import org.bytedancer.crayzer.design_mode_pattern.creational.builder.sample01.tile.MarcoPoloTile;

/**
 * @author yizhe.chen
 */

public class Builder {

    public IMenu levelOne(Double area) {
        return new DecorationPackageMenu(area, "豪华欧式")
                .appendCeiling(new LevelTwoCeiling())
                .appendCoat(new DuluxCoat())
                .appendFloor(new ShengXiangFloor());
    }

    public IMenu levelTwo(Double area) {
        return new DecorationPackageMenu(area, "轻奢⽥园")
                .appendCeiling(new LevelTwoCeiling())
                .appendCoat(new LiBangCoat())
                .appendTile(new MarcoPoloTile());
    }

    public IMenu levelThree(Double area) {
        return new DecorationPackageMenu(area, "现代简约")
                .appendCeiling(new LevelOneCeiling())
                .appendCoat(new LiBangCoat())
                .appendTile(new DongPengTile());
    }
}
