package com.example.sprintm6

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.sprintm6.datasoure.RestDataSource
import com.example.sprintm6.model.User
import com.example.sprintm6.model.UserDao
import com.example.sprintm6.repository.UserRepositoryImp
import com.example.sprintm6.util.Constants.Companion.ENDPOINT_LOCATION
import com.example.sprintm6.util.Constants.Companion.ENDPOINT_NAME
import com.example.sprintm6.util.Constants.Companion.ENDPOINT_PICTURE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.StandardCharsets



private val sujetoPrueba1 = User("Ivan", "zamorano", "Santiago", "http://..."  )
private val sujetoPrueba2 = User("Marcelo", "Salas", "Temuco", "http://...")


class UserRepositoryTest {
   private val mockWebServer = MockWebServer().apply{
       url("/")
       dispatcher = myDispatcher
   }
   private val restDataSource = Retrofit.Builder()
       .baseUrl(mockWebServer.url("/"))
       .client(OkHttpClient.Builder().build())
       .addConverterFactory(GsonConverterFactory.create())
       .build()
       .create(RestDataSource::class.java)

   private val newsRepository = UserRepositoryImp(restDataSource, MockUserDao())

   @get:Rule
   val rule = InstantTaskExecutorRule()

   @After
   fun tearDown() {
       mockWebServer.shutdown()
   }

   @Test
   fun `verifica la obtencion de usuarios desde la DB`()  = runBlocking{
       val users = newsRepository.getAllUser().first()
       assertEquals(2, users.size)
   }

   @Test
   fun `verifica si se elimina un usuario correctamente`()  = runBlocking{
        newsRepository.deleteUser(sujetoPrueba1)
        val users = newsRepository.getAllUser().first()
        assertEquals(1, users.size)
    }

   @Test
   fun `verifica si se crea un usuario correctamente`()  = runBlocking{
        val newUsers = newsRepository.getNewUser()
        val users = newsRepository.getAllUser().first()
        assertEquals(3, users.size)
    }
}

class MockUserDao: UserDao {
    private val users = MutableStateFlow<List<User>>(listOf(sujetoPrueba1, sujetoPrueba2))

    override fun insert(user: User) {
        users.value = users.value.toMutableList().apply{ add(user) }
    }

    override fun getAll(): Flow<List<User>> = users

    override fun delete(user: User) {
        users.value = users.value.toMutableList().apply{ remove(user) }
    }
}

val myDispatcher: Dispatcher = object : Dispatcher() {
    override fun dispatch(request: RecordedRequest): MockResponse {
        return when (request.path) {
            "/$ENDPOINT_NAME" -> MockResponse().apply{addResponse("api_name.json") }
            "/$ENDPOINT_PICTURE" -> MockResponse().apply{addResponse("api_picture.json") }
            "/$ENDPOINT_LOCATION" -> MockResponse().apply{addResponse("api_location.json") }
            else -> MockResponse().setResponseCode(404)
        }
    }
}

fun MockResponse.addResponse(filePath: String): MockResponse{
        val inputStream = javaClass.classLoader?.getResourceAsStream(filePath)
        val source = inputStream?.source()?.buffer()
        source?.let{
            setResponseCode(200)
            setBody(it.readString(StandardCharsets.UTF_8))
        }
        return this
}