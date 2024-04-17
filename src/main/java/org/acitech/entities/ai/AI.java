package org.acitech.entities.ai;

import org.acitech.entities.Entity;

public interface AI {

    void execute(double delta);
    void damageHandler(Entity damager);

}
