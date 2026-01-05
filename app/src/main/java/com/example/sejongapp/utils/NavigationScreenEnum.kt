package com.example.sejongapp.utils

enum class NavigationScreenEnum {
    SIDEBAR,
    ANNOUNCEMENTS,
    HOMEPAGE,
    SCHEDULE,
    LIBRARY,
    MAGAZINES

}


enum class UserStatusEnum{
    STUDENT,
    TEACHER,
    ADMIN,
    UNKNOWN;

    companion object {
        fun fromOrdinal(ordinal: Int): UserStatusEnum {
            return entries.getOrNull(ordinal) ?: UNKNOWN
        }
    }
}
