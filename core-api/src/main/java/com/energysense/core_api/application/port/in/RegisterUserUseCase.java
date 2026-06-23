package com.energysense.core_api.application.port.in;

import com.energysense.core_api.application.command.RegisterCommand;

public interface RegisterUserUseCase {
    String register(RegisterCommand command);
}
