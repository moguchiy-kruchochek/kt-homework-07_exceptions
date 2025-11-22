package ru.netology

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

data class Note(
    val id: Int,
    val ownerId: Int,
    val title: String,
    val text: String
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
    val date: Int = 0
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
    val note: Note,
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
    val service = WallService()
    val likes = Likes()

    val video = Video(1, 1, "video_title", 50)
    val videoAttachment = VideoAttachment(video)

    val audio = Audio(2, 2, "artist", "song")
    val audioAttachment = AudioAttachment(audio)

    val photo = Photo(1, 1, "description here", 640, 480)
    val photoAttachment = PhotoAttachment(photo)

    val note = Note(1, 1, "note", "note's text")
    val noteAttachment = NoteAttachment(note)

    val link = Link("www.url.com", "link", "good link")
    val linkAttachment = LinkAttachment(link)


    val comment = Comment(0, 0,"comment",2025)

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
        attachments = arrayOf(videoAttachment, audioAttachment, photoAttachment, noteAttachment, linkAttachment),
        comment
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
            arrayOf(videoAttachment, audioAttachment, photoAttachment, noteAttachment),
            comment
        )
    )
    println(post)
}