package com.duocuc.sanjibookapp.data.dao

import androidx.room.*
import com.duocuc.sanjibookapp.models.Role

@Dao
interface RoleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(role: Role): Long

    @Update
    suspend fun update(role: Role)

    @Delete
    suspend fun delete(role: Role)

    @Query("SELECT * FROM roles WHERE id = :id LIMIT 1")
    suspend fun getRoleById(id: Int): Role?

    @Query("SELECT * FROM roles WHERE name = :name LIMIT 1")
    suspend fun getRoleByName(name: String): Role?

    @Query("SELECT * FROM roles")
    suspend fun getAllRoles(): List<Role>
}
