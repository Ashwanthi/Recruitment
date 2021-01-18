package com.qbrainx_recruitment.event;

import com.qbrainx_recruitment.model.Users;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class UserAccountChangeEvent extends ApplicationEvent {

    private static final long serialVersionUID = 3898681439653723595L;

    private Users users;
    private String action;
    private String actionStatus;

    public UserAccountChangeEvent(final Users users, final String action, final String actionStatus) {
        super(users);
        this.users = users;
        this.action = action;
        this.actionStatus = actionStatus;
    }
}
