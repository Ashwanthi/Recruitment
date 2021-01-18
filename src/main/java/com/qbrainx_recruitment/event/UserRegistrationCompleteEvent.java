package com.qbrainx_recruitment.event;

import com.qbrainx_recruitment.dto.UsersDto;
import com.qbrainx_recruitment.model.Users;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.util.UriComponentsBuilder;

@Getter
@Setter
public class UserRegistrationCompleteEvent extends ApplicationEvent {

    private static final long serialVersionUID = -8779813944636822324L;

    private transient UriComponentsBuilder redirectUrl;
    private final UsersDto usersDto;

    public UserRegistrationCompleteEvent(final UsersDto usersDto, final UriComponentsBuilder redirectUrl) {
        super(usersDto);
        this.usersDto = usersDto;
        this.redirectUrl = redirectUrl;
    }
}
