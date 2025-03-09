package com.vakantes.Vakantes.model.dtos;

import com.vakantes.Vakantes.model.enums.UserRole;
import jakarta.annotation.Nonnull;


public record RegisterDto(@Nonnull String login, @Nonnull String senha, @Nonnull UserRole role) {
}
