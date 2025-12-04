package ru.netology

interface Entity {
    val id: Int
}

data class Note(
    override val id: Int = 0,
    val title: String = "",
    val text: String = "",
    val date: Int = 0,
) : Entity


interface CrudService<E : Entity> {
    fun add(entity: E): Int
    fun update(entity: E): Boolean
    fun delete(id: Int): Boolean
    fun get(): List<E>
    fun getById(id: Int): E?
    fun restore(id: Int): Boolean
}

class NoteService : CrudService<Note> {
    private val notes = mutableListOf<Note>()
    private var unicId = 1
    private var comments = mutableListOf<Comment>()

    override fun add(entity: Note): Int {
        val note = entity.copy(id = unicId)
        notes.add(note)
        unicId += 1
        return note.id
    }

    override fun update(entity: Note): Boolean {
        for ((index, noteWithSameId) in notes.withIndex()) {
            if (noteWithSameId.id == entity.id) {
                notes[index] = entity
                return true
            }
        }
        return false
    }

    override fun delete(id: Int): Boolean {
        val note = notes.find { it.id == id }
        if (note != null) {
            notes.removeAt(note.id - 1)
            return true
        } else {
            return false
        }
    }

    override fun get(): List<Note> {
        if (notes.size != 0) {
            return notes
        } else {
            throw PostNotFoundException("notes are not found!")
        }
    }

    override fun getById(id: Int): Note? {
        return notes.find { it.id == id }
    }

    override fun restore(id: Int): Boolean {
        return false
    }

    fun createComment(noteId: Int, comment: Comment): Comment {
        var noteFound = false
        val idInComments = 1
        for (note in notes) {
            if (note.id == noteId) {
                noteFound = true
                break
            }
        }
        if (!noteFound) {
            throw PostNotFoundException("no note with id $noteId")
        }
        val commentWithNewId = comment.copy(id = comments.size + idInComments)
        comments.add(commentWithNewId)
        return comments.last()
    }

    fun updateComment(comment: Comment): Boolean {
        for ((index, commentWithSameId) in comments.withIndex()) {
            if (commentWithSameId.id == comment.id) {
                comments[index] = comment
                return true
            }
        }
        return false
    }

    fun getComments(): List<Comment> {
        return comments.filter { !it.isDeleted }
    }

    fun deleteComment(id: Int): Boolean {
        val comment = comments.find { it.id == id && !it.isDeleted }
        if (comment != null) {
            val index = comments.indexOf(comment)
            comments[index] = comment.copy(isDeleted = true)
            return true
        } else {
            return false
        }
    }

    fun restoreComment(id: Int): Boolean {
        val comment = comments.find { it.id == id && it.isDeleted }
        if (comment != null) {
            val index = comments.indexOf(comment)
            comments[index] = comment.copy(isDeleted = false)
            return true
        } else {
            return false
        }
    }

}