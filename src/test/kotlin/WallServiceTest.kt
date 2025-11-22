import org.junit.Test

import org.junit.Assert.*
import ru.netology.*

class WallServiceTest {

    @Test
    fun add() {
        val service = WallService()
        val likes = Likes()


        val post = Post(
            3,
            "31.10.21",
            "text33",
            true,
            "NEauthor",
            true,
            true,
            false,
            likes,
            arrayOf()
        )

        val result = service.add(post)
        assertEquals(1, result.id)
    }

    @Test
    fun updateExistingWithTrueResult() {
        val service = WallService()
        val likes = Likes()

        service.add(
            Post(
                0,
                "11.11.20",
                "text51",
                true,
                "authorr",
                true,
                false,
                null,
                likes,
                arrayOf()
            )
        )
        service.add(
            Post(
                0,
                "23.12.21",
                "super text",
                false,
                "corp",
                null,
                true,
                false,
                likes,
                arrayOf()
            )
        )
        service.add(
            Post(
                0,
                "03.03.25",
                "important",
                null,
                "mine",
                true,
                false,
                false,
                likes,
                arrayOf()
            )
        )

        val update = Post(
            3,
            "03.04.25",
            "IMPORTANT!",
            true,
            "me",
            null,
            null,
            null,
            likes,
            arrayOf()
        )

        val result = service.update(update)

        assertTrue("The Post updated!", result)
    }

    @Test
    fun updateWithFalseResult() {
        val service = WallService()
        val likes = Likes()

        service.add(
            Post(
                0,
                "11.11.20",
                "text51",
                true,
                "authorr",
                true,
                false,
                true,
                likes,
                arrayOf()
            )

        )
        service.add(
            Post(
                0,
                "23.12.21",
                "super text",
                false,
                "corp",
                false,
                true,
                false,
                likes,
                arrayOf(),
            )
        )
        service.add(
            Post(
                0,
                "03.03.25",
                "important",
                false,
                "mine",
                true,
                false,
                false,
                likes,
                arrayOf()
            )
        )

        val update = Post(
            6,
            "03.04.25",
            "IMPORTANT!",
            null,
            "me",
            null,
            null,
            null,
            likes,
            arrayOf()
        )

        val result = service.update(update)

        assertFalse("No Posts with such ID :(", result)
    }
}
