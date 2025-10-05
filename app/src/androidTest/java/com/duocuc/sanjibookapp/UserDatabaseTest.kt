package com.duocuc.sanjibookapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.duocuc.sanjibookapp.data.dao.UserDao
import com.duocuc.sanjibookapp.data.database.AppDatabase
import com.duocuc.sanjibookapp.models.Role
import com.duocuc.sanjibookapp.models.User
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDatabaseTest {

    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        userDao = db.userDao()
        val roleDao = db.roleDao()

        // Insertamos usuario de prueba
        runBlocking {
            roleDao.insert(Role(1, "Administrador"))
            roleDao.insert(Role(3, "Usuario"))
            userDao.insert(
                User(
                    0,
                    "admin@demo.com",
                    "admin123",
                    "Admin",
                    "user",
                    "",
                    "",
                    true,
                    1
                )
            ) // admin
            userDao.insert(
                User(
                    0,
                    "user@demo.com",
                    "user123",
                    "Normal",
                    "user",
                    "",
                    "",
                    true,
                    3
                )
            )   // user normal
        }
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun testUserExists() = runBlocking {
        val user = userDao.getUserByEmail("admin@demo.com")
        assertNotNull(user)
        assertEquals("Admin", user?.nombre)
    }

    @Test
    fun login_successful_with_correct_credentials() = runBlocking {
        val email = "admin@demo.com"
        val password = "admin123"

        val user = userDao.getUserByEmail(email)
        assertNotNull(user)
        assertEquals(password, user?.password)
    }

    @Test
    fun login_fails_with_nonexistent_user() = runBlocking {
        val email = "noexiste@demo.com"
        val password = "1234"

        val user = userDao.getUserByEmail(email)
        assertNull(user)
    }

    @Test
    fun login_fails_with_wrong_password() = runBlocking {
        val email = "user@demo.com"
        val password = "wrongpassword"

        val user = userDao.getUserByEmail(email)
        assertNotNull(user)
        assertNotEquals(password, user?.password)
    }

    @Test
    fun login_checks_user_role_admin() = runBlocking {
        val email = "admin@demo.com"
        val user = userDao.getUserByEmail(email)

        assertNotNull(user)
        assertEquals(1, user?.roleId) // rol admin
    }
}