import org.junit.Test

import org.junit.Assert.*
import ru.netology.Comment
import ru.netology.Note
import ru.netology.NoteService
import ru.netology.PostNotFoundException

class NoteServiceTest {

    val note = Note(
        0,
        "NOTE TITLE",
        "NOTE text",
        2025
    )
    val comment = Comment(
        0,
        0,
        "Я был здесь Первый!",
        2025,
        false
    )

    @Test
    fun add() {
        val noteService = NoteService()

        val result = noteService.add(note)
        assertEquals(1, result)
    }

    @Test
    fun updateWithTrueResult() {
        val noteService = NoteService()

        noteService.add(note)
        noteService.add(
            Note(
                0,
                "NOTE TITLE",
                "NOTE text",
                2025
            )
        )
        val update = (Note(
            2,
            "Note title-title",
            "note text-text",
            2026
        )
                )
        val result = noteService.update(update)
        assertTrue("YES!", result)
    }

    @Test
    fun updateWithFalseResult() {
        val noteService = NoteService()

        noteService.add(note)
        noteService.add(
            Note(
                0,
                "NOTE TITLE",
                "NOTE text",
                2025
            )
        )
        val update = (Note(
            22,
            "Note title-title",
            "note text-text",
            2026
        )
                )
        val result = noteService.update(update)
        assertFalse("NO!", result)
    }

    @Test
    fun deleteWithTrueResult() {
        val noteService = NoteService()

        noteService.add(note)
        val result = noteService.delete(1)
        assertTrue("YES!", result)
    }

    @Test
    fun deleteWithFalseResult() {
        val noteService = NoteService()

        noteService.add(note)
        val result = noteService.delete(11)
        assertFalse("NO!", result)
    }

    @Test
    fun getNotes() {
        val noteService = NoteService()

        noteService.add(note)

        val result = noteService.get()
        assertEquals(1, result.size)
    }

    @Test(expected = PostNotFoundException::class)
    fun getNotesShouldThrowException() {
        val noteService = NoteService()
        noteService.get()
    }

    @Test
    fun getExistingNoteById() {
        val noteService = NoteService()

        noteService.add(note)
        noteService.add(
            Note(
                0,
                "NOTE TITLE",
                "NOTE text",
                2025
            )
        )
        val result = noteService.getById(1)
        assertEquals(1, result?.id)
    }

    @Test
    fun getNonExistingNoteById() {
        val noteService = NoteService()

        noteService.add(note)
        noteService.add(
            Note(
                0,
                "NOTE TITLE",
                "NOTE text",
                2025
            )
        )
        val result = noteService.getById(10)
        assertNull(result)
    }

    @Test
    fun getByIdFromEmptyList() {
        val noteService = NoteService()

        val result = noteService.getById(1)
        assertNull(result)
    }

    @Test
    fun createCommentAtExistingNote() {
        val noteService = NoteService()

        noteService.add(note)
        val result = noteService.createComment(1, comment)
        assertEquals(1, result.id)
    }

    @Test(expected = PostNotFoundException::class)
    fun createCommentAtNonExistingNote() {
        val noteService = NoteService()

        noteService.add(note)
        noteService.createComment(2, comment)
    }

    @Test
    fun updateCommentWithTrueResult() {
        val noteService = NoteService()

        noteService.add(note)
        noteService.createComment(1, comment)
        val result = noteService.updateComment(
            Comment(
                1,
                1,
                "new edited comment",
                222,
                false
            )
        )
        assertTrue("YES!", result)
    }

    @Test
    fun updateCommentWithFalseResult() {
        val noteService = NoteService()

        noteService.add(note)
        noteService.createComment(1, comment)
        val result = noteService.updateComment(
            Comment(
                2,
                1,
                "new edited comment",
                222,
                false
            )
        )
        assertFalse("NO!", result)
    }

    @Test
    fun getComments() {
        val noteService = NoteService()
        noteService.add(note)
        noteService.createComment(1, comment)
        val result = noteService.getComments()
        assertEquals(1, result.size)
    }

    @Test
    fun getShouldReturnNonDeletedComments() {
        val noteService = NoteService()
        noteService.add(note)
        noteService.createComment(1, comment)
        val deletedComment = comment.copy(2, 2, "text", 11, true)
        noteService.createComment(1, deletedComment)
        val result = noteService.getComments()
        assertEquals(1, result.size)
    }

    @Test
    fun deleteExistingNoteComment() {
        val noteService = NoteService()
        noteService.add(note)
        noteService.createComment(1, comment)
        val result = noteService.deleteComment(1)
        assertTrue("YES!", result)
    }

    @Test
    fun deleteNonExistingNoteComment() {
        val noteService = NoteService()
        noteService.add(note)
        noteService.createComment(1, comment)
        val result = noteService.deleteComment(2)
        assertFalse("YES!", result)
    }

    @Test
    fun deleteDeletedNoteComment() {
        val noteService = NoteService()
        noteService.add(note)
        noteService.createComment(1, comment)
        val deletedComment = Comment(0, 2, "text", 22, true)
        noteService.createComment(1, deletedComment)
        val result = noteService.deleteComment(2)
        assertFalse("NO!", result)
    }

    @Test
    fun restoreExistingComment() {
        val noteService = NoteService()
        noteService.add(note)
        noteService.createComment(1, comment)
        noteService.deleteComment(1)
        val result = noteService.restoreComment(1)
        assertTrue("YES!", result)
    }

    @Test
    fun restoreNonExistingComment() {
        val noteService = NoteService()
        noteService.add(note)
        noteService.createComment(1, comment)
        noteService.deleteComment(1)
        val result = noteService.restoreComment(2)
        assertFalse("NO!", result)
    }

    @Test
    fun restoreNonDeletedNoteComment() {
        val noteService = NoteService()
        noteService.add(note)
        noteService.createComment(1, comment)
        val result = noteService.restoreComment(1)
        assertFalse("NO!", result)
    }
}