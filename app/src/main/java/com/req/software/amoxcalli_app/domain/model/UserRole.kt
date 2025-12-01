package com.req.software.amoxcalli_app.domain.model

/**
 * User roles based on roles.csv
 * UUIDs match the backend role system
 */
enum class UserRole(val id: String, val displayName: String) {
    USER("21ec53cd-4864-4086-b6d4-7f3a27a594a3", "Usuario"),
    ADMIN("3f37abbf-da39-4004-b88e-3d082a04ae67", "Administrador"),
    SUPERADMIN("57aa1fa8-adad-41a0-aa1a-002bb8191dd6", "Superadministrador");

    companion object {
        /**
         * Get role from UUID string
         */
        fun fromId(id: String?): UserRole {
            return values().find { it.id == id } ?: USER
        }

        /**
         * Check if role ID is admin or superadmin
         */
        fun isAdmin(roleId: String?): Boolean {
            val role = fromId(roleId)
            return role == ADMIN || role == SUPERADMIN
        }

        /**
         * Check if role ID is superadmin
         */
        fun isSuperAdmin(roleId: String?): Boolean {
            return fromId(roleId) == SUPERADMIN
        }
    }
}
