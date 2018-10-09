package org.example.model;

import lombok.Data;

/**
 * @author Valentyn.Moliakov
 */
@Data
public class UsersReport {
    private long activeUsers;
    private long inactiveUsers;
    private long deletedUsers;

    public UsersReport() {
    }

    public UsersReport(long activeUsers, long inactiveUsers, long deletedUsers) {
        this.activeUsers = activeUsers;
        this.inactiveUsers = inactiveUsers;
        this.deletedUsers = deletedUsers;
    }
}
