package ru.netology


data class Likes(
    val count: Int = 10,
    val userLikes: Boolean = true,
    val canLike: Boolean = true,
    val canRepost: Boolean = false
)

data class Post(
    val id: Int = 0,
    val date: String,
    val text: String,
    val friendsOnly: Boolean?,
    val copyright: String,
    val isPinned: Boolean?,
    val isFavorite: Boolean?,
    val marksAsAds: Boolean?,
    val likes: Likes,
)


class WallService {
    private var posts = emptyArray<Post>()
    private var unicId = 1

    fun add(post: Post): Post {
        val postWithUnicId = post.copy(id = unicId)
        posts += postWithUnicId
        unicId += 1

        return posts.last()
    }

    fun update(post: Post): Boolean {
        for ((index, postWithSameId) in posts.withIndex()) {
            if (postWithSameId.id == post.id) {
                posts[index] = post
                return true
            }
        }
        return false
    }
}


fun main() {
    val service = WallService()
    val likes = Likes()


    val post = Post(
        0,
        "11.11.11",
        "text",
        friendsOnly = null,
        "author",
        isPinned = null,
        isFavorite = null,
        marksAsAds = null,
        likes,
    )
    service.add(post)
    service.update(
        Post(
            1,
            "03.04.25",
            "Important text",
            false,
            "me",
            true,
            false,
            false,
            likes,

        )
    )
    println(post)
}