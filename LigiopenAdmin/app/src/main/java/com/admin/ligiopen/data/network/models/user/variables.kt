package com.admin.ligiopen.data.network.models.user

val user = UserDto(
    id = 1,
    username = "Test Test",
    email = "test@gmail.com",
    role = "SUPER_ADMIN",
    createdAt = "2025-05-16T11:55:29.830334",
    archived = false,
    archivedAt = null,
    administeringClubId = 2
)

val emptyUser = UserDto(
    id = 0,
    username = "",
    email = "",
    role = "",
    createdAt = "",
    archived = false,
    archivedAt = null,
    administeringClubId = 0
)

val users = List(10) {
    UserDto(
        id = 1,
        username = "Test Test",
        email = "test@gmail.com",
        role = "SUPER_ADMIN",
        createdAt = "2025-05-16T11:55:29.830334",
        archived = false,
        archivedAt = null,
        administeringClubId = 2
    )
}