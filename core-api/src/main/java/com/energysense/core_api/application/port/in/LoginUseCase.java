package com.energysense.core_api.application.port.in;

import com.energysense.core_api.application.command.LoginCommand;

public interface LoginUseCase {
    String login(LoginCommand command);
}
