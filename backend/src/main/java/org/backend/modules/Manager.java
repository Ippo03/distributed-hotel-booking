package org.backend.modules;

import java.io.Serializable;
import java.util.Objects;

public class Manager implements Serializable {
    private final int managerId;

    public Manager(int managerId){
        this.managerId = managerId;
    }

    public int getManagerId() {
        return this.managerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Manager manager = (Manager) o;
        return managerId == manager.managerId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(managerId);
    }
}
