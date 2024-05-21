package org.acitech.cat.entities.ai;

import org.acitech.cat.entities.Entity;

public interface AI {

    void execute(double delta);
    void damageHandler(Entity damager);

}
