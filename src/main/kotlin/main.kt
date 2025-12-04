package ru.netology

data class NoteToPost(
    val id: Int,
    val title: String,
    val text: String,
    val date: Int
)

data class Video(
    val id: Int,
    val ownerId: Int,
    val title: String,
    val duration: Int
)

data class Audio(
    val id: Int,
    val ownerId: Int,
    val artist: String,
    val title: String
)

data class Link(
    val url: String,
    val title: String,
    val description: String
)

data class Photo(
    val id: Int,
    val ownerId: Int,
    val descriptionText: String,
    val width: Int,
    val height: Int
)


data class Comment(
    val id: Int = 0,
    val authorId: Int = 0,
    val comment: String = "",
    val date: Int = 0,
    val isDeleted: Boolean = false
)

interface Attachment {
    val type: String
}

class VideoAttachment(
    val video: Video,
    override val type: String = "video"
) : Attachment

class AudioAttachment(
    val audio: Audio,
    override val type: String = "audio"
) : Attachment

class LinkAttachment(
    val link: Link,
    override val type: String = "link"
) : Attachment

class NoteAttachment(
    val note: NoteToPost,
    override val type: String = "note"
) : Attachment

class PhotoAttachment(
    val photo: Photo,
    override val type: String = "photo"
) : Attachment


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
    val attachments: Array<Attachment> = emptyArray(),
    val comment: Comment
)

class PostNotFoundException(message: String) : RuntimeException(message)


class WallService {
    private var posts = emptyArray<Post>()
    private var unicId = 1
    private var comments = emptyArray<Comment>()

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

    fun createComment(postId: Int, comment: Comment): Comment {
        var postFound = false
        for (post in posts) {
            if (post.id == postId) {
                postFound = true
                break
            }
        }
        if (!postFound) {
            throw PostNotFoundException("no post with id $postId")
        }
        comments += comment
        return comments.last()
    }
}


fun main() {
    val service = ChatService()

    service.createChat(1)
    service.createMessage(1, "Roman")
    service.createMessage(2, "Darov!")
    service.createMessage(2, "go buhat")

    println("Всего чатов: ${service.getChats().size}")
    println("Непрочитанных чатов: ${service.getUnreadChatsCount()}")
    println("Последние сообщения: ${service.getLastMessages()}")


    println("${service.getMessagesByUserId(2, 2)} \nПрочитано сообщения от юзера 2")
    println("Теперь непрочитанных чатов: ${service.getUnreadChatsCount()}")

    println("${service.getMessagesByUserId(1, 10)} \nПрочитано сообщения от юзера 1")
    println("Теперь непрочитанных чатов: ${service.getUnreadChatsCount()}")

    service.editMessage(2, 2, "go pivo pit, please")
    service.deleteMessage(2, 2)

    service.deleteChat(1)
    service.deleteChat(2)
}