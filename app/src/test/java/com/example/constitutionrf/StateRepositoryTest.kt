package com.example.constitutionrf

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.constitutionrf.sampledata.AppDatabaseModel
import com.example.constitutionrf.sampledata.StateModel
import com.example.constitutionrf.sampledata.StateRepositoryModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@org.robolectric.annotation.Config(manifest = org.robolectric.annotation.Config.NONE)
class StateRepositoryTest {
    private lateinit var stateRepository: StateRepositoryModel
    private lateinit var db: AppDatabaseModel

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabaseModel::class.java
        ).allowMainThreadQueries().build()

        stateRepository = StateRepositoryModel(db.stateDAO())
    }

    @After
    fun tearDown() { db.close() }

    @Test
    fun test1() {
        val testArticle = StateModel(num = 1, text = "Текст статьи 1", favourites = false)

        stateRepository.addProject(testArticle)
        val found = stateRepository.findByNum(1)

        assertNotNull(found)
        assertEquals(1, found?.num)
        assertEquals("Текст статьи 1", found?.text)
    }
    @Test
    fun test2() {
        val testArticle = StateModel(num = 2, text = "Текст статьи 2", favourites = true)

        stateRepository.addProject(testArticle)
        val found = stateRepository.findByNum(2)

        assertNotNull(found)
        assertEquals(2, found?.num)
        assertEquals("Текст статьи 2", found?.text)
    }

}